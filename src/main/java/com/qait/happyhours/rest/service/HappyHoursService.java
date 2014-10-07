package com.qait.happyhours.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qait.happyhours.domain.Deal;
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
		 
		Deal deal=new Deal();
		deal.setLatitude(28.585148);
		deal.setLongitude(77.311413);
		deal.setRequiredDistance((double) 100);
		/*return searchDealByGeoLocation(s, deal);*/
		/*Deal deal=new Deal();
		deal.setTitle("Brand New Car");
		deal.setDescription("You can win brand new car, thats it");
		deal.setLocation("UAE");
		deal.setLatitude(28.578383);
		deal.setLongitude(77.317507);
		deal.setOriginalPrice("50000");
		deal.setNewPrice("900");
		deal.setDiscount("50");
		deal.setDealMainImage("/brand/images.jpg");
		deal.setStartDate(new Date());
		deal.setEndDate(new Date());
		deal.setDealType(true);
		deal.setIsExpired(false);
		saveDeal(deal);*/
		return "Its working";
	}

	@POST
	@Path("register-new-user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(User user) {
		
		System.out.println("login hit user data is :" + user);
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
	public Response searchDeal(@QueryParam("dealString") String searchStr) {
		
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();
		
		DealService dealService = (DealService) appContext.getBean("dealService");
		
		UserService userService = (UserService) appContext.getBean("userService");
		
		/*Boolean isAuthorized=userService.checkAuthorizationByToken(authorizationValue);*/
		
		/*if(isAuthorized){*/
			
		List<Deal> dealList = dealService.getMatchingDealsBySearchStr(searchStr);

		return Response.status(200).entity(dealList).build();
		
		/*}else{
			
			response.setCode("authorizationToken001");
			response.setMessage(HappyHoursPropertiesFileReaderUtil.getPropertyValue("authorizationToken001"));
			return Response.status(200).entity(response).build();
		}*/
	}
	
	@GET
	@Path("geo-search-deal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchDealByGeoLocation(Deal dealDTO) {
		
		HappyHoursServiceResponse response = new HappyHoursServiceResponse();
		DealService dealService = (DealService) appContext
				.getBean("dealService");
		
		UserService userService = (UserService) appContext
				.getBean("userService");
		
		/*Boolean isAuthorized=userService.checkAuthorizationByToken(authorizationValue);*/
		Boolean isAuthorized=true;
		
		if(isAuthorized){
		List<Deal> dealList=new ArrayList<Deal>();
		List<Deal> allDealList = dealService.getAllActiveDealsList();
		
		for (Deal deal2 : allDealList) {
			Double distance=distance(dealDTO.getLatitude(),dealDTO.getLongitude(),deal2.getLatitude(),deal2.getLongitude());
			if(distance < dealDTO.getRequiredDistance()){
				deal2.setRelativeDistance(distance);
				dealList.add(deal2);
			}
		}

		return Response.status(200).entity(dealList).build();
		
		}else{
			
			response.setCode("authorizationToken001");
			response.setMessage(HappyHoursPropertiesFileReaderUtil
					.getPropertyValue("authorizationToken001"));
			return Response.status(200).entity(response).build();
			
		}
	}


	private Double distance(Double latitude, Double longitude, Double latitude2,
			Double longitude2) {
		Long R = (long) 6371; // km
	    Double dLat = (latitude2 - latitude) * Math.PI / 180;
	    Double dLon = (longitude2 - longitude) * Math.PI / 180;
	    Double lat1 = latitude * Math.PI / 180;
	    Double lat2 = latitude2 * Math.PI / 180;
	 
	    Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	            Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	    Double d = (Double)(R * c);
	    /*Double m = d * 0.621371;*/
		return d;
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
		
		System.out.println("Deal data is :" + deal);
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
}
