package com.teamchat.integration.gdoc;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Mallika Gogoi
 *
 */
	@WebServlet("/GDocCallback")
	public class GDocCallback extends HttpServlet {
		private static final long serialVersionUID = 1L;
	     
	    public GDocCallback() {
	        super();
	    }
	    
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("CallBack");
			String email=request.getParameter("state");
			System.err.println(email);
			GDocConnect gdc=new GDocConnect();
			gdc.getaccesstoken(request.getParameter("code"),email);
			PrintWriter writer = response.getWriter();
			writer.println("<script>window.close();</script>");
		}

		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		}

}
