package chatletcreator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

/**
 * Servlet implementation class CreateChatlet
 */
@WebServlet("/createchatlet")
public class CreateChatlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	

   	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		System.out.println("working...");
		BufferedReader rdr=request.getReader();
		StringBuilder sb=new StringBuilder();
		String line="";
		while((line=rdr.readLine())!=null)
			sb.append(line);
		String output=sb.toString();
		
		JSONObject j=new JSONObject(output);
		JSONObject j1=j.getJSONObject("data");
		String email=j.getString("email");
		
		try
		{
			new PrintChatlet().set(j1.toString(),email);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}

}
