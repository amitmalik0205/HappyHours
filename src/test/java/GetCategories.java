import java.io.IOException;

import javax.xml.bind.JAXBException;

public class GetCategories {

	public static void main(String[] args) throws JAXBException, IOException {

		String url = "http://localhost:8081/happyhours/rest/happy-hours-service/get-category-list";

		TestUtil.sendRequest(url, "", "GET");

	}
}
