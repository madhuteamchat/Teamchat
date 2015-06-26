package com.teamchat.integration.youtube.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class YoutubeConnect {
	
	/*  private static class DefaultTrustManager implements X509TrustManager {

	        @Override
	        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

	        @Override
	        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

	        @Override
	        public X509Certificate[] getAcceptedIssuers() {
	            return null;
	        }
	    }*/
	  
	  public void youtubeLogin()
	  {
		  
			/*try 
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
			} 		  		 */
	  }
	  
	  public void youtubeLogout(String sname)
	  {
		  Properties props = new Properties();
  	    InputStream is = null;
  	  File f=null;
		   try {    // First try loading from the current directory
	    	   
	    	    f = new File("uid.properties");
	    	        is = new FileInputStream( f );  	  }
	    catch ( Exception e ) { is = null; }
	    
	    try { 
	    	        if(is==null)  
	    	 is=getClass().getResourceAsStream("uid.properties");
	    	  
	    	        // Try loading properties from the file (if found)
	    	        props.load( is );
	    	        System.out.println("before"+props.getProperty(sname));
	    	        props.remove(sname);
	    	        System.out.println("after"+props.getProperty(sname));
	    	        OutputStream out = new FileOutputStream( f );
			        props.store(out, "Refresh Tokens");
	    	        
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	  }
}
