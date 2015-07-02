package com.teamchat.integrations.wunderlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

/**
 * Servlet implementation class WunderlistServlet
 */
public class WunderlistServlet extends HttpServlet {
	String client_id = PropertiesFile.getValue("client_id");
	String client_secret = PropertiesFile.getValue("client_secret");
	String redirectUrl = PropertiesFile.getValue("callback_url");
	private static final long serialVersionUID = 1L;
	/**
	 * Default constructor.
	 */
	public WunderlistServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		if (request.getParameter("email") != null) {
			
			String mail = request.getParameter("email");
			String room_id=request.getParameter("room_id");
			String state=mail+","+room_id;
			String contextPath = "https://www.wunderlist.com/oauth/authorize?client_id="
					+ client_id
					+ "&redirect_uri="
					+ redirectUrl+"&state="
					+ state;
			response.sendRedirect(response.encodeRedirectURL(contextPath));
		} else if (request.getParameter("code") != null) {
			int resp_code=404;
			try {

					String state = request.getParameter("state");
					String[] param=state.split(",");
					String mail=param[0];
					String room_id=param[1];
					String token = request.getParameter("code");
					System.out.println("code=" + token);
					String url = "https://www.wunderlist.com/oauth/access_token";
					HttpClient client = HttpClientBuilder.create().build();
					HttpPost post = new HttpPost(url);

					post.setHeader("Content-Type",
							"application/json;charset=UTF-8");

					JSONObject jobj = new JSONObject();
					jobj.put("client_id", client_id);
					jobj.put("client_secret", client_secret);
					jobj.put("code", token);
					String jsonstring = jobj.toString();
					System.err.println(jsonstring);
					StringEntity entity = new StringEntity(jsonstring, "UTF-8");
					entity.setContentType("application/json");
					post.setEntity(entity);

					HttpResponse res = null;

					res = client.execute(post);

					resp_code = res.getStatusLine().getStatusCode();
					System.out.println(resp_code);
					String inputline, result = "";
					BufferedReader in = new BufferedReader(
							new InputStreamReader(res.getEntity().getContent()));
					while ((inputline = in.readLine()) != null) {
						result += inputline;
					}
					System.err.println("JSON is" + result);
					in.close();
					JSONObject ob = new JSONObject(result);

					String accessToken = ob.getString("access_token");
					System.out.println(accessToken);
					PrintWriter writer = response.getWriter();
					writer.print("Connected successfully. Now you can access your Wunderlist from your Teamchat");
					ManageDB.insert(mail, accessToken);
					new WunderlistBot().postMsg("connected...",room_id);
			}
			 catch (Exception e) {
				response.getWriter().print(e.getMessage());
			}
			response.setContentType("text/html");
	        // New location to be redirected
	        String site = "";
	        if (resp_code <= 200) {
	            site = "success.html";
	        } else {
	            site = "error.html";
	        }
	        // redirect to location
	        response.sendRedirect(site);


		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
