package com.qait.happyhours.rest.service;

import java.io.InputStream;
import java.util.ArrayList;
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
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qait.happyhours.domain.Deal;
import com.qait.happyhours.domain.DealImages;
import com.qait.happyhours.domain.User;
import com.qait.happyhours.dto.SendPasswordDTO;
import com.qait.happyhours.enums.EmailType;
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
	@Path("/uploadImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveImage(@FormDataParam("file") InputStream ins,
			@FormDataParam("file") FormDataContentDisposition desDisposition,
			@FormDataParam("dealID") Long dealID,
			@FormDataParam("mainImage") boolean isMainImage) {

		boolean isError = false;
		String imageName = null;

		String rootFolderPath = HappyHoursPropertiesFileReaderUtil
				.getApplicationProperty("deal.image.public.url");
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();

		DealService dealService = (DealService) appContext
				.getBean("dealService");

		try {

			imageName = HappyHoursUtil.uploadImageOnServer(ins, dealID);

		} catch (Exception e) {
			e.printStackTrace();
			isError = true;
			response.setMessage(HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("imageUpload002"));
			response.setCode("imageUpload002");
		}

		if (!isError) {

			Deal savedDeal = dealService.getDealByID(dealID);

			if (isMainImage == false) {

				Set<DealImages> dealImagesList = savedDeal.getDealImagesList();
				DealImages dealImage = new DealImages();
				dealImage.setImage(rootFolderPath + "/" + dealID.toString()
						+ "/" + imageName);
				dealImagesList.add(dealImage);

			} else {

				savedDeal.setDealMainImage(rootFolderPath + "/"
						+ dealID.toString() + "/" + imageName);
			}

			boolean dealSaved = dealService.saveDeal(savedDeal);

			if (dealSaved) {
				response.setMessage(HappyHoursPropertiesFileReaderUtil
						.getPropertyValue("imageUpload001"));
				response.setCode("imageUpload001");
			} else {
				response.setMessage(HappyHoursPropertiesFileReaderUtil
						.getPropertyValue("imageUpload002"));
				response.setCode("imageUpload002");
			}
		}

		return Response.status(200).entity(response).build();
	}
}
