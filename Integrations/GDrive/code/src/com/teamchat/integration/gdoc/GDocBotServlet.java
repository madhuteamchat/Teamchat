package com.teamchat.integration.gdoc;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.integration.database.User;
/**
 * 
 * @author Mallika Gogoi
 *
 */
@WebServlet("/GDocBotServlet")
public class GDocBotServlet extends HttpServlet {
	
		private static final long serialVersionUID = 1L;
	
		String client_id,client_secret;
		String redirecturi;
		String usrname="";
		String email="";
		Properties configProps;
		
		public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
		{
			Properties prop = new Properties();
			prop.load(type.getClassLoader().getResourceAsStream(fileName));
			return prop;
		}
		
	    public GDocBotServlet() {
	        super();
	    }
	    
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	
	    	configProps = loadPropertyFromClasspath("gdrive.properties", GDocBotServlet.class);
	    	redirecturi=configProps.getProperty("redirecturi");
	    	
	    	client_id=request.getParameter("client_id");
	    	client_secret=request.getParameter("client_secret");
            email=request.getParameter("email");	    	
	    	GDocConnect.client_id=client_id;
	    	GDocConnect.client_secret=client_secret;
	    	GDocConnect.uid=request.getParameter("name");
	    	String requestUrl ="https://accounts.google.com/o/oauth2/auth?"
					+"client_id="+client_id+"&"
					+"redirect_uri="+redirecturi+"&"
					+"state="+email+"&"
					+"scope=https://www.googleapis.com/auth/drive&"
					+"response_type=code&";
			response.setContentType("application/x-www-form-urlencoded");
	    	response.sendRedirect(response.encodeRedirectURL(requestUrl));			
		}
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
		}
}
