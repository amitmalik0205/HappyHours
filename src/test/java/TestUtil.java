import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class TestUtil {

	public static void  sendRequest(String urlStr, String param, String method) throws IOException {
		
		URL url = new URL(urlStr);
		
		HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setRequestMethod(method);
	     
	    if(method.equals("POST")) {
	    	conn.setDoOutput(true);
	    	OutputStream os = conn.getOutputStream();
			os.write(param.getBytes());
			os.flush();
	    }
		
		conn.connect();
		
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
 
		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
 
		conn.disconnect();
	}
}
