import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qait.happyhours.domain.Category;
import com.qait.happyhours.domain.Deal;
import com.qait.happyhours.domain.DealOffers;


public class UplaodImage {

	public static void main(String[] args) throws JsonProcessingException {
		
	   Set<DealOffers> dealOffersList = new HashSet<DealOffers>();
	   DealOffers dealOffers = new DealOffers();
	   dealOffers.setOfferName("Offer test");

	   DealOffers dealOffers2 = new DealOffers();
	   dealOffers2.setOfferName("Offer test2");
		
	   dealOffersList.add(dealOffers);
	   dealOffersList.add(dealOffers2);
	   
	   Category category = new Category();
	   category.setCategoryID(new Long(1));
	   category.setCategoryName("Luxury Bars");
		
       Deal deal = new Deal();		
       deal.setDealOffersList(dealOffersList);
       deal.setDescription("Description");
       deal.setDealType(true);
       deal.setDiscount("20");
       deal.setLatitude(25.359405);
       deal.setLongitude(56.269405);
       deal.setOriginalPrice("10000");
       deal.setNewPrice("8000");
       deal.setTitle("My Deal Test Title");
       deal.setSubTitle("My Deal Test Sub Title");
       deal.setIsExpired(false);
       deal.setLocation("Gurgaon");
       deal.setDealCategory(category);
       
       String startDateString = "15-10-2014 15:00:00";
       DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
       Date startDate=null;
       try {
           startDate = df.parse(startDateString);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       
       String endDateString = "17-10-2014 15:00:00";
       DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
       Date endDate=null;
       try {
           endDate = df2.parse(endDateString);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       
       deal.setStartDate(startDate);
       deal.setEndDate(endDate);
       
       ObjectMapper mapper = new ObjectMapper();
       String dealJSON = mapper.writeValueAsString(deal);

	   HttpClient httpclient = HttpClientBuilder.create().build();
	   HttpPost httppost = new HttpPost("http://localhost:8081/happyhours/rest/happy-hours-service/save-deal");
	   
	   try {
		   
		   MultipartEntityBuilder multipart = MultipartEntityBuilder.create();
		   multipart.addPart("deal", new StringBody(dealJSON));
		   multipart.addPart("mainImage", new FileBody(new File("C:\\happyhours_data\\deal_images\\4.png")));
		   multipart.addPart("images", new FileBody(new File("C:\\happyhours_data\\deal_images\\2.png")));
		   multipart.addPart("images", new FileBody(new File("C:\\happyhours_data\\deal_images\\3.png")));
		   multipart.addPart("images", new FileBody(new File("C:\\happyhours_data\\deal_images\\1.png")));
		   
		   HttpEntity entity2 = multipart.build();
		   httppost.setEntity(entity2);
		   
		   httpclient.execute(httppost);
		 
	   }catch (Exception e) {
		   e.printStackTrace();
	   
	   }
		
	}
}
