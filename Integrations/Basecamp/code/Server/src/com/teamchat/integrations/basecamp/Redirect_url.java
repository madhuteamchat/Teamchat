package com.teamchat.integrations.basecamp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.apache.jasper.tagplugins.jstl.core.Out;
import org.json.JSONObject;

import com.google.gson.Gson;


/**
 * Servlet implementation class Redirect_url
 */
@WebServlet(description = "get the verfication code from this url", urlPatterns = { "/Redirect_url" })
public class Redirect_url extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String code;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Redirect_url() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		// TODO Do required initialization
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	//	/**
	//	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	//	 *      response)
	//	 */
	//	protected void service(HttpServletRequest request,
	//			HttpServletResponse response) throws ServletException, IOException {
	//		String code = request.getParameter("code"), user_agent = request
	//				.getHeader("User-Agent"), client_id = "8f6bc64ea4e27e738ab826f47832df331009feb9", 
	//				client_secret = "4ab6e59509ac6fe6878c88c5a6face6f9d253afb", 
	//				redirect_uri = "http://localhost:8080/Basecamp_servlet/Redirect_url",
	//				access_token, refresh_token = request
	//				.getParameter("refresh_token"), expires_in = request
	//				.getParameter("expires_in");
	//		PrintWriter out = response.getWriter();
	//		out.println("To out-put All the request-attributes received from request - ");
	//		try {
	//			//JSONObject uriparam = new JSONObject(request);
	//			//access_token = (String) uriparam.get("access_token");
	////			response.getWriter().print(code + "or" + access_token);
	//			response.getWriter().print(request.toString());
	//			 sendPost("https://launchpad.37signals.com/authorization/token?",
	//			 user_agent,"type=web_server&client_id=" + client_id +
	//			 "&redirect_uri=" + redirect_uri
	//			 +"&client_secret=" + client_secret
	//			 +"&code=" + code);
	//		} catch (Exception e) {
	//			// TODO: handle exception
	//			out.println(e);
	//		}
	//	}

	// HTTP POST request
	private void sendPost(String url, String User_agent, String urlParameters)
			throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", User_agent);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		// urlParameters must be in this format
		// "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// retrieve verification code
		code = request.getParameter("code");
		String user_agent = request
				.getHeader("User-Agent"), client_id = "8f6bc64ea4e27e738ab826f47832df331009feb9", client_secret = "4ab6e59509ac6fe6878c88c5a6face6f9d253afb", redirect_uri = "http://localhost:8080/Basecamp_servlet/Redirect_url",
				access_token = new String(), refresh_token, expires_in;
		PrintWriter out = response.getWriter();
		boolean isPost = "POST".equals(request.getMethod());
		out.println(isPost);
		try {
			out.println(code + "or" + access_token);			
			sendPost("https://launchpad.37signals.com/authorization/token",
					user_agent,"type=web_server&client_id=" + client_id +
					"&redirect_uri=" + redirect_uri
					+"&client_secret=" + client_secret
					+"&code=" + code);
			//doPost(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			out.println(e);
		}
		// TODO retrieve CLIENT_ID ,etc from db
		// if (code.equals("null")) {
		// try {
		// Redirect_url r_url = new Redirect_url();
		// r_url.sendPost("https://launchpad.37signals.com/authorization/token",
		// user_agent,"?type=web_server&client_id=" + client_id +
		// "&redirect_uri=" + redirect_uri
		// +"&client_secret=" + client_secret
		// +"&code=" + code);
		// doPost("", response);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// }
		out.println("man this is not the post!!!");
		try{
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}
			//Token token = (Token) gson.fromJson(sb.toString(), Token.class);
			JSONObject jobj = new JSONObject(sb.toString());
			out.println(jobj);
		}
		catch(Exception e){
			out.println(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// System.out.println(request.getParameter("token"));
		// TODO Auto-generated method stub
		// String user = request.getParameter("user");
		// String pass = request.getParameter("password");
		// if ("edu4java".equals(user) && "eli4java".equals(pass)) {
		// response(response, "login ok");
		// } else {
		// response(response, "invalid login");
		// }
		//		String code = request.getParameter("code"), user_agent = request
		//				.getHeader("User-Agent"), client_id = "8f6bc64ea4e27e738ab826f47832df331009feb9", client_secret = "4ab6e59509ac6fe6878c88c5a6face6f9d253afb", redirect_uri = "http://localhost:8080/Basecamp_servlet/Redirect_url", access_token = request
		//				.getParameter("access_token"), refresh_token = request
		//				.getParameter("refresh_token"), expires_in = request
		//				.getParameter("expires_in");
		//		response.getWriter().print(code + "or" + access_token);
		String access_token, refresh_token, expires_in;
		PrintWriter out = response.getWriter();
		out.println("man this is the post!!!");
		try{
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}
			//Token token = (Token) gson.fromJson(sb.toString(), Token.class);
			JSONObject jobj = new JSONObject(sb.toString());
			out.println(jobj);
		}
		catch(Exception e){
			out.println(e);
		}
	}

}
