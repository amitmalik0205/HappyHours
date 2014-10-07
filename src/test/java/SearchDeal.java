import java.io.IOException;

import javax.xml.bind.JAXBException;


public class SearchDeal {

	public static void main(String[] args) throws JAXBException, IOException {
		
		String url = "http://localhost:8081/happyhours/rest/happy-hours-service/search-deal?searchString=offer";
	
		TestUtil.sendRequest(url, "", "GET");
		
	}




}
