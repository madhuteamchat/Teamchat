package chatletcreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;


//import com.webaroo.teamchat.solutions.server.commons.constant.Constants;
//import com.webaroo.teamchat.solutions.server.commons.http.SolutionHTTPAdapter;
//import com.webaroo.teamchat.solutions.server.commons.util.SolutionCommonUtil;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String teamchatAuthUrl;
	private static Logger logger = Logger.getLogger(Login.class);
	//private static Properties endPointproperties;
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        //endPointproperties =  loadPropertyFromClasspath("endpoint.properties", Login.class);
		//teamchatAuthUrl = endPointproperties.getProperty("teamchatAuthUrl");
		teamchatAuthUrl="http://enterprise.teamchat.com/GatewayAPI2/rest/login";
    }

    public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
	{

		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;

	}
    
    /**
     * reloads configuration file
     */
    public static void reload(){
    	//endPointproperties = loadPropertyFromClasspath("endpoint.properties", Login.class);
		//teamchatAuthUrl = endPointproperties.getProperty("teamchatAuthUrl");
		teamchatAuthUrl="http://enterprise.teamchat.com/GatewayAPI2/rest/login";
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		Token.setAuthEmail(email);
		Token.setAuthPass(password);
		String authUrl = teamchatAuthUrl;
		logger.info("Authenticating user : "+email);
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("password", password);
		//SolutionHTTPAdapter httpAdapter = new SolutionHTTPAdapter(null, params, Constants.HTTP.METHOD_POST, authUrl);
		//HttpPost post=new HttpPost(authUrl);
		try
		{
			//String authResponse = httpAdapter.execute();
			HttpPost httppost = new HttpPost(authUrl+"?email="+email+"&password="+password);
			System.out.println(httppost.toString());
			HttpClient httpclient=new DefaultHttpClient();
		
			HttpResponse authResponse = httpclient.execute(httppost);
			BufferedReader in = new BufferedReader(new
					InputStreamReader(authResponse.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "";
					                while ((line = in.readLine()) != null) {
					                    sb.append(line +"\n");
					                }
					                
					                System.out.println(sb);
			
					                
			JSONObject json = new JSONObject(sb.toString());
			String profileName = json.getJSONObject("profile").getString("profileName");
			String userId=json.getJSONObject("profile").getString("userId");
			String token=json.getJSONObject("token").getString("token");
			Token.setToken(token);
			Token.setUserId(userId);
			String rooms=new SendChatlet().getRooms(userId, token);
			Token.setRooms(rooms);
			
			logger.info("Authentication successful : "+authResponse);
			//String successUrl = endPointproperties.getProperty("successUrl");
			String successUrl = "index.html";
			HttpSession session = request.getSession();
			session.setAttribute("profileName", profileName);
			session.setAttribute("email", email);
			session.setAttribute("password", EncryptionUtil.encrypt(password));
			response.sendRedirect(successUrl);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Authentication failure", e);
			//String failureUrl = endPointproperties.getProperty("failureUrl");
			String failureUrl = "login.html";
			//send the error message as url query string for redirect
			response.sendRedirect(failureUrl+"?errMsg=Invalid login details.");
		}
		
	}
	
	
}
