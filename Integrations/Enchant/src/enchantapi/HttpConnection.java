package enchantapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class HttpConnection {
	
	
  public static String getBodyOfResponse(HttpURLConnection yc) throws Exception{
	  String inputline;
	  String result="";
	  BufferedReader in = new BufferedReader(new InputStreamReader(
              yc.getInputStream()));
      
        while ((inputline = in.readLine()) != null)
      	result+=inputline;
        in.close();
	    System.out.println(result);
	  return result;
  }
}
