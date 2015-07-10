package com.teamchat.integration.instagram.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.integration.instagram.bot.TeamchatPost;

/**
 * Servlet implementation class CallBack
 */
@WebServlet("/CallBack")
public class CallBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallBack() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if(request.getParameter("hub.verify_token").equals("instabot"))
		{
			String huchg=request.getParameter("hub.challenge");			
//			response.sendRedirect(response.encodeRedirectURL("https://api.instagram.com/v1/subscriptions/?hub.challenge="+huchg));
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$ Success  "+huchg);
			PrintWriter out=response.getWriter();
			out.print(huchg);
			out.flush();
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("############webhook###################");
		String res="";
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
				                StringBuffer sb = new StringBuffer("");
				                String line = "";
				                while ((line = in.readLine()) != null) {
				                    sb.append(line);
				                    res+=line+"<br>";
				                }
				                in.close();
//				                new TeamchatPost().postMsg(sb.toString());
				                new TeamchatPost().postjsonMsg(sb.toString());
				                
	}

}
