package com.teamchat.integration.yammer;
/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class YammerClient1 {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String OAUTH_ACCESS_TOKEN_URL = "https://www.yammer.com/oauth";

    private static final String OAUTH_REQUEST_TOKEN_URL = "https://www.yammer.com/oauth/request_token";

	  private static String oauth_headers(String token, String token_secret,
              String applicationKey, String applicationSecret) {
      StringBuffer buff = new StringBuffer();
      buff.append("OAuth realm=\"");
      buff.append("\", oauth_consumer_key=\"");
      buff.append(applicationKey);
      buff.append("\", ");

      if (token != null) {
              buff.append("oauth_token=\"");
              buff.append(token);
              buff.append("\", ");
      }

      buff.append("oauth_signature_method=\"");
      buff.append("PLAINTEXT");
      buff.append("\", oauth_signature=\"");
      buff.append(applicationSecret);
      buff.append("%26");
      if (token_secret != null) {
              buff.append(token_secret);
      }
      buff.append("\", oauth_timestamp=\"");
      buff.append(new Date().getTime());
      buff.append("\", oauth_nonce=\"");
      buff.append(new Date().getTime());
      buff.append("\", oauth_version=\"1.0\"");

      System.out.println(buff.toString());
      return buff.toString();
}

	  public String getTokenParameters() {
		  String requestTokenResponseString ="";
		  HttpClient httpclient = new DefaultHttpClient();
          try {
                  HttpPost httpost = new HttpPost(OAUTH_REQUEST_TOKEN_URL);
                  httpost.addHeader(AUTHORIZATION_HEADER, oauth_headers(null, null,
                                  "VxmwJQvzJg8wR3nVO49nTA","Z8nna7DDumINJmn8A7VEth6xQJMPdIcUm95bJR5w"));

                  BufferedReader reader = getResponseReader(httpclient
                                  .execute(httpost));

                  // Request token parameters are on the first line of the response
                  requestTokenResponseString = reader.readLine();
                  parseQueryStringParameters(requestTokenResponseString);
                  
	      }
          catch(Exception e) {
        	  e.printStackTrace();
          }
          return requestTokenResponseString;
	  }
	  
	   public static void getUsers(String accessAuthToken,
               String accessAuthSecret, String message, String group,
               String applicationKey, String applicationSecret)
               throws ClientProtocolException, IOException {
       HttpClient httpclient = new DefaultHttpClient();

       try {
               HttpPost httpPost = new HttpPost(YAMMER_API_V1_MESSAGES);
               httpPost.addHeader(AUTHORIZATION_HEADER, oauth_headers(
                               accessAuthToken, accessAuthSecret, applicationKey,
                               applicationSecret));

               List<NameValuePair> nvps = new ArrayList<NameValuePair>();
               nvps.add(new BasicNameValuePair(MESSAGE_BODY_PARAM_NAME, message));

               // If a group name is supplied find the group id and post the
               // message to it
               if (group != null && !group.equals("")) {
                       nvps.add(new BasicNameValuePair(MESSAGE_GROUP_ID_PARAM_NAME,
                                       group));
               }

               httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
               httpclient.execute(httpPost);
       } finally {
               httpclient.getConnectionManager().shutdown();
       }
}

	  
	    private static BufferedReader getResponseReader(HttpResponse response)
                throws IllegalStateException, IOException {
        HttpEntity entity = response.getEntity();

        if (entity != null) {
                InputStream instream = entity.getContent();
                return new BufferedReader(new InputStreamReader(instream));
        }
        return null;
        }
	    
	    private static Map<String, String> parseQueryStringParameters(
                String queryString) {

        Map<String, String> parametersMap = new HashMap<String, String>();
        StringTokenizer tokenizerNameValuePair = new StringTokenizer(
                        queryString, "&");

        while (tokenizerNameValuePair.hasMoreTokens()) {
                try {
                        String strNameValuePair = tokenizerNameValuePair.nextToken();
                        StringTokenizer tokenizerValue = new StringTokenizer(
                                        strNameValuePair, "=");

                        String strName = tokenizerValue.nextToken();
                        String strValue = tokenizerValue.nextToken();

                        parametersMap.put(strName, strValue);
                } catch (Throwable t) {
                        // If we cannot parse a parameter, ignore it
                }
        }
        for(String s:parametersMap.keySet()) {
        	System.out.println(s+" = "+parametersMap.get(s));
        }
        return parametersMap;
}

	    
	    public static void main(String argv[]) {
	    	YammerClient1 yc=new YammerClient1();
	    	System.out.println(yc.getTokenParameters());
	    }

	  
}
*/