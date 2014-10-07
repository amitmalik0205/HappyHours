import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qait.happyhours.domain.Deal;


public class DealGeoLocation {

	public static void main(String[] args) throws JAXBException, IOException {
		
		String url = "http://localhost:8081/happyhours/rest/happy-hours-service/geo-search-deal";
		
		Deal deal = new Deal();
		deal.setLongitude(77.311415);
		deal.setLatitude(28.585139);
		deal.setRequiredDistance(1000.0);
		
		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(deal);
		System.out.println(content);

		TestUtil.sendRequest(url, content, "POST");
		
	}




}
