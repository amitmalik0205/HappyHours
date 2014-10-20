package com.qait.happyhours.rest.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qait.happyhours.domain.Category;
import com.qait.happyhours.domain.Deal;
import com.qait.happyhours.domain.DealImages;
import com.qait.happyhours.domain.User;
import com.qait.happyhours.dto.SendPasswordDTO;
import com.qait.happyhours.enums.EmailType;
import com.qait.happyhours.service.CategoryService;
import com.qait.happyhours.service.DealService;
import com.qait.happyhours.service.UserService;
import com.qait.happyhours.util.EmailUtil;
import com.qait.happyhours.util.HappyHoursPropertiesFileReaderUtil;
import com.qait.happyhours.util.HappyHoursUtil;

@Path("happy-hours-service")
public class HappyHoursService {

	private static final Logger logger = Logger
			.getLogger(HappyHoursService.class);

	ApplicationContext appContext = new ClassPathXmlApplicationContext(
			"../applicationContext.xml");

	@Path("/text")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String text() {
		return "Its working";
	}

	@POST
	@Path("register-new-user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(User user) {
		UserService userService = (UserService) appContext.getBean("userService");
		return userService.saveUser(user);
	}

	@POST
	@Path("sign-in")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authinticateUser(User user) {
		UserService userService = (UserService) appContext
				.getBean("userService");
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();

		User savedUser = userService.authenticateUser(user.getUserName(),
				user.getPassword());

		if (savedUser != null) {
			String token = HappyHoursUtil.getAuthToken();

			savedUser.setToken(token);

			if (userService.updateUser(savedUser)) {
				response.setToken(token);
				response.setCode("signIn001");
				response.setMessage(HappyHoursPropertiesFileReaderUtil
						.getPropertyValue("signIn001"));
			} else {
				response.setCode("signIn003");
				response.setMessage(HappyHoursPropertiesFileReaderUtil
						.getPropertyValue("signIn003"));
			}
		} else {
			response.setCode("signIn002");
			response.setMessage(HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("signIn002"));
		}

		return Response.status(200).entity(response).build();
	}

	/**
	 * Service will return the List of Deal's matching with @param searchStr
	 * 
	 * @param searchStr
	 *            - Search String
	 * @return - List of Deal's
	 */
	@GET
	@Path("search-deal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchDeal(@QueryParam("searchString") String searchStr) {
		
		DealService dealService = (DealService) appContext.getBean("dealService");
			
		List<Deal> dealList = dealService.getMatchingDealsBySearchStr(searchStr);
		
		for(Deal deal : dealList) {
			deal.setDealMainImage(HappyHoursUtil.appendServerUrlToPath(deal.getDealMainImage()));
			Set<DealImages> dealImagesList = deal.getDealImagesList();
			for (DealImages dealImages : dealImagesList) {
				dealImages.setImage(HappyHoursUtil
						.appendServerUrlToPath(dealImages.getImage()));
			}
		}
		return Response.status(200).entity(dealList).build();
	}
	
	@POST
	@Path("geo-search-deal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchDealByGeoLocation(Deal dealDTO) {
		
		DealService dealService = (DealService) appContext
				.getBean("dealService");

		List<Deal> dealList = new ArrayList<Deal>();
		List<Deal> allDealList = dealService.getAllActiveDealsList();

		for (Deal deal : allDealList) {
			
			Double distance = HappyHoursUtil.distance(
					dealDTO.getLatitude(), dealDTO.getLongitude(),
					deal.getLatitude(), deal.getLongitude());
			
			if (distance < dealDTO.getRequiredDistance()) {
				deal.setRelativeDistance(distance);
				deal.setDealMainImage(HappyHoursUtil.appendServerUrlToPath(deal.getDealMainImage()));
				Set<DealImages> dealImagesList = deal.getDealImagesList();
				for (DealImages dealImages : dealImagesList) {
					dealImages.setImage(HappyHoursUtil
							.appendServerUrlToPath(dealImages.getImage()));
				}
				dealList.add(deal);
			}
		}

		return Response.status(200).entity(dealList).build();
	}

	/**
	 * Service will send email to the registered email ID.
	 * 
	 * @param userID
	 * @return
	 */
	@POST
	@Path("recover-password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response recoverPassword(String userID) {
		UserService userService = (UserService) appContext
				.getBean("userService");
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();

		User savedUser = userService.getUserByUserId(userID);
		if (savedUser == null) {

			response.setCode("recoverPassword002");
			response.setMessage(HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("recoverPassword002"));

		} else {

			SendPasswordDTO templateModel = new SendPasswordDTO();
			templateModel.setUserID(savedUser.getUserName());
			templateModel.setPassword(savedUser.getPassword());

			boolean isEmailSent = EmailUtil
					.sendEmail(
							HappyHoursPropertiesFileReaderUtil
									.getVelocityTemplateProperties("send.password.email.subject"),
							savedUser.getEmail(), EmailType.SEND_PASSWORD,
							templateModel);

			if (isEmailSent) {
				response.setCode("recoverPassword001");
				response.setMessage(HappyHoursPropertiesFileReaderUtil
						.getPropertyValue("recoverPassword001"));
			} else {
				response.setCode("recoverPassword003");
				response.setMessage(HappyHoursPropertiesFileReaderUtil
						.getPropertyValue("recoverPassword003"));
			}
		}

		return Response.status(200).entity(response).build();
	}
	
	@POST
	@Path("save-new-deal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveDeal(Deal deal) {
		
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();
		DealService dealService = (DealService) appContext.getBean("dealService");
		
		response.setCode("saveDeal001");
		String status = HappyHoursPropertiesFileReaderUtil
				.getPropertyValue("saveDeal001");
		if (!dealService.saveDeal(deal)) {
			response.setCode("saveDeal002");
			status = HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("saveDeal002");
		}
		response.setMessage(status);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Path("/save-deal")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveImage(FormDataMultiPart multiPart) {
		
		DealService dealService = (DealService) appContext.getBean("dealService");
		CategoryService categoryService = (CategoryService) appContext
				.getBean("categoryService");
		
		boolean isError = false;
		String imageName = null;
		
		List<InputStream> inputStreams = new ArrayList<InputStream>();
		List<String> imageNames = new ArrayList<String>();

		String rootFolderPath = HappyHoursPropertiesFileReaderUtil
				.getApplicationProperty("deal.image.public.url");
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();
		
		//Get entity which contains Deal details
		List<FormDataBodyPart> dealPart = multiPart.getFields("deal"); 
		Deal dealObj = null;
		
		//Convert deal JSON to object
		ObjectMapper mapper = new ObjectMapper();
		try {
			dealObj = mapper.readValue(dealPart.get(0).getValueAs(String.class), Deal.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			isError = true;
			response.setMessage(HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("saveDeal003"));
			response.setCode("saveDeal003");
		} 
		
		if(!isError) {
			// Store main image as InputStream
			List<FormDataBodyPart> mainImageList = multiPart.getFields("mainImage");  
			FormDataBodyPart mainImage = mainImageList.get(0);
			inputStreams.add(mainImage.getValueAs(InputStream.class));
			
			// Store other image as InputStream
			List<FormDataBodyPart> imageList = multiPart.getFields("images");    
		    for(FormDataBodyPart image : imageList) { 
		    	inputStreams.add(image.getValueAs(InputStream.class));
		    }
			
		    //Upload images one by one and store their names in list
		    String randomFolderName = HappyHoursUtil.getAuthToken().toString();
		    for(InputStream stream : inputStreams) {
		    	try {

					imageName = HappyHoursUtil.uploadImageOnServer(stream, randomFolderName);
					imageNames.add(randomFolderName + "/" + imageName);

				} catch (Exception e) {
					e.printStackTrace();
					isError = true;
					response.setMessage(HappyHoursPropertiesFileReaderUtil
							.getPropertyValue("imageUpload002"));
					response.setCode("imageUpload002");
				}
		    	
		    	if(isError) {
		    		break;
		    	}
		    }
		    
		    if(!isError) {
		    	//Set main images for deal
		    	dealObj.setDealMainImage(rootFolderPath + "/" + imageNames.get(0));
		    	
		    	//Set other images for deal
		    	Set<DealImages> dealImagesList = new HashSet<DealImages>();
		    	for(int i=1; i<=imageNames.size()-1; i++) {
		    		DealImages dealImage = new DealImages();
					dealImage.setImage(rootFolderPath + "/"  + imageNames.get(i));
					dealImagesList.add(dealImage);
		    	}
		    	
		    	dealObj.setDealImagesList(dealImagesList);
		    	
		    	Category category = categoryService.loadCategoryID(dealObj.getDealCategory().getCategoryID());
		    	dealObj.setDealCategory(category);
		    	
		    	boolean dealSaved = dealService.saveDeal(dealObj);

				if (dealSaved) {
					response.setMessage(HappyHoursPropertiesFileReaderUtil
							.getPropertyValue("saveDeal001"));
					response.setCode("saveDeal001");
				} else {
					response.setMessage(HappyHoursPropertiesFileReaderUtil
							.getPropertyValue("saveDeal002"));
					response.setCode("saveDeal002");
				}
		    }
		}
	    
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Path("get-category-list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoryList() {
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();
		
		CategoryService categoryService = (CategoryService) appContext
				.getBean("categoryService");
		List<Category> list = categoryService.getCategoryList();
		
		if (list == null) {
			response.setMessage(HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("getCategoryList001"));
			response.setCode("getCategoryList001");
			return Response.status(200).entity(response).build();
		} else {
			return Response.status(200).entity(list).build();
		}
	}
}
