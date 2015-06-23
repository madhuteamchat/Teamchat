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

/**
 * Servlet implementation class CreateChatletRoom
 */
@WebServlet("/createchatletroom")
public class CreateChatletRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside Chatlet Romm");
		PrintWriter out=response.getWriter();
		System.out.println("working...");
		BufferedReader rdr=request.getReader();
		StringBuilder sb=new StringBuilder();
		String line="";
		while((line=rdr.readLine())!=null)
			sb.append(line);
		String output=sb.toString();
		
		JSONObject j=new JSONObject(output);
		JSONObject data=j.getJSONObject("data");
		String roomId=j.getString("value");
		
		
		try
		{
			new SendChatlet().roomMessage(roomId, data.toString());;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}

}
