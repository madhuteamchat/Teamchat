package com.teamchat.youtube_integration.connect;

import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.teamchat.youtube_integration.upload.MyUploads;

public class YoutubeConnect {
	
	
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  
	  
	 
	 
	  private static class DefaultTrustManager implements X509TrustManager {

	        @Override
	        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

	        @Override
	        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

	        @Override
	        public X509Certificate[] getAcceptedIssuers() {
	            return null;
	        }
	    }
	  
	  public void youtubeLogin(String sname)
	  {
		  System.out.println( "login fun()....................." );
		  String line;
		  
			try 
			{ 
				SSLContext ctx = SSLContext.getInstance("TLS");
	        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
	        SSLContext.setDefault(ctx);
				URL url = new URL( "https://localhost:8443/YoutubeIntegration/YoutubeBotServelet?name="+sname );
				HttpsURLConnection uc=(HttpsURLConnection) url.openConnection();
				
				uc.setDoOutput(true);
//				BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream())); 
				 uc.setHostnameVerifier(new HostnameVerifier() {
			            @Override
			            public boolean verify(String arg0, SSLSession arg1) {
			                return true;
			            }
			        });
				int response=uc.getResponseCode();
				
//				while ( (response = in.readLine()) != null ) {
					System.out.println( response );
//					}

//				in.close(); 
			}
			catch (Exception e)
			{ 
				e.printStackTrace(); 
			} 		  		 
	  }
	  
	  public void youtubeLogout(String sname)
	  {
		  File f=new File("/home/intern11/"+sname+".properties");
		  if(f.exists())
		  {
			  f.delete();
		  }
	  }
}
