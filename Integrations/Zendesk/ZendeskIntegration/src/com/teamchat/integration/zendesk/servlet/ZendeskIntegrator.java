package com.teamchat.integration.zendesk.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.fabric.xmlrpc.base.Data;
import com.teamchat.client.sdk.Chatlet;
import com.teamchat.client.sdk.Reply;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;

/**
 * Servlet implementation class Integrator
 */
@WebServlet("/integrator")
public class ZendeskIntegrator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ZendeskIntegrator() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// processRequest(request, response);
//		PrintWriter out = response.getWriter();
//		out.println("test");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String jsonString = request.getParameter("extractD");
		JSONObject data = new JSONObject(jsonString);
		
		//System.out.println(data.getString("pubComment"));
		
		String ticketId = String.valueOf(data.getInt("ticketId"));
		String comment = data.getString("comment");
		String status = data.getString("status");
		PrintWriter out = response.getWriter();
	out.println(status);
		
		 
		 String path =
			 "/home/anuj-intern22/Desktop/gup/eclipse/ZendeskBot/data/zendesk-config.properties";
			 Properties configProps = new Properties();
			
			 try {
			 configProps = loadPropertyFileFromDisk(path);
			 } catch (Exception e1) {
			 e1.printStackTrace();
			 }

			 if(status.equals("solved"))
				{
			 try {
					updatestatus(ticketId,status,configProps);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
//			 else
//			 {
//				 try {
//					updatecommentzendesk(comment,ticketId,configProps);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				 
//			 }
		
	}
	
	public static void updatecommentzendesk(String comment,String ticketId,Properties configProps)throws Exception
	{
		
		 DBHandler checkData = new DBHandler();
		 String[] roomForm = checkData.getRoomForm(ticketId);
		
		
		 
		// System.out.println(roomForm);
		
		 TeamchatAPI api = TeamchatAPI.fromFile("teamchat99.data")
		 .setEmail(configProps.getProperty("botemail").trim()).setPassword(configProps.getProperty("botpass").trim()).setEndPoint(configProps.getProperty("endpoint").trim());
		
		 Room r = api.context().byId(roomForm[0]);
		 Chatlet c = api.context().chatletById(roomForm[1]);
		 Reply rep = c.createReply().addField("upCom", comment);
		
		 System.out.println(rep);
		
		 api.perform(r.reply(rep));
		
	}
	
	public static void updatestatus(String ticketId,String status,Properties configProps)throws Exception
	{
		 DBHandler datacng = new DBHandler();
		
			datacng.rs = datacng.stmt.executeQuery("SELECT * FROM " + configProps.getProperty("dbname").trim() +"."+configProps.getProperty("tablename").trim() + " where ticketid='" + ticketId + "'");

			        while (datacng.rs.next()) {
			        	//String cng = datacng.rs.getStr/ng(columnLabel)
			           	datacng.rs.updateString("TktStatus", status);
			            datacng.rs.updateRow();
			        }
			
		
	}
	
	
	public static Properties loadPropertyFileFromDisk(String filePath)
			throws Exception {

		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		fIS.close();
		return configProp;
	}
}