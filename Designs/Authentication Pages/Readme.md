## Live Demo ##
[Success Page](http://madhuteamchat.github.io/Teamchat/Designs/Authentication%20Pages/success.html)
[Error Page](http://madhuteamchat.github.io/Teamchat/Designs/Authentication%20Pages/error.html)

## Setup ##

 1. Copy the contents of this folder into the Webcontent folder of your Bot template
 2. Replace the image in img/App_logo.png with your the logo of your app and name it as App_logo.png.
 3. Use the code snippet below in your servlet.
 
 Put this code inside the doGet method of the servlet (at the end)
 
```Java		
// Set response content type
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
```
		
**Note: the resp_code variable will be using the response code from the post request you are sending.**

The Final code will be:
e.g. 
```Java
protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			resp_code = sendPost(
					Universal.TOKEN_URL,
					request.getHeader("User-Agent"),
					"type=web_server&client_id=" + Universal.CLIENT_ID
							+ "&redirect_uri="
							+ URLEncoder
									.encode(Universal.REDIRECT_URL, "UTF-8")
							+ "&client_secret=" + Universal.CLIENT_SECRET
							+ "&code=" + request.getParameter("code"));
		} catch (Exception e) {
			// TODO: handle exception
			out.println(e);
		}
		// redirect according to request
		// Set response content type
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
	private int sendPost(String url, String User_agent, String urlParameters)
			throws Exception {
			//your code
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
			return responseCode;
}
```

**Note you maybe using another method for posting data to your server to get the auth token please look into that method and return the response code you might be getting and plugin that value into resp_code variable**
