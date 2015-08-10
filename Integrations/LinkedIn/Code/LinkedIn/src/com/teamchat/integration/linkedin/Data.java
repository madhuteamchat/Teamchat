package com.teamchat.integration.linkedin;

public class Data {
	public static final String help = "<div align='center'><img width='90px' height='90px' src='https://yt3.ggpht.com/-CepHHHB3l1Y/AAAAAAAAAAI/AAAAAAAAAAA/Z8MftqWbEqA/s900-c-k-no/photo.jpg'/></div><br/>"
			+ "<div><b>Hi, I am the LinkedIn Bot and here is what all I can do for you</b><br/>"
			+ "<ol>"
			+ "<li>Fetch all the feed from your Linkedin account to this group</li>"
			+ "<li>Share content on your Linkedin</li>"
			+ "<li>Manage company pages (Coming Soon)</li>"
			+ "</ol>"
			+ "</div><br/>"
			+ "<div><b>Here is a list of keywords to integrate and manage LinkedIn-</b><br/></div>"
			+ "<div style='box-shadow:0px 0px 3px DodgerBlue;'> <div align='center'><b style='color:grey; font-size:13px '>LinkedIn</b></div> <table width='100%' border='0px' bordercolor='DodgerBlue ' style='font-size:11px;border-collapse: collapse;'> <!--headrer --> <tr style=' border-top:5px; border-top-color:DodgerBlue; border-top-style:solid;border-bottom:5px; border-bottom-color:DodgerBlue; border-bottom-style:solid'> <td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px; border-right:thin white solid;'>Keywords</td> <td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px;'>What it does</td> </tr> <!--headrer ends --> <!--content --> <tr style=''> <td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> <b>Connect</b> </td> <td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> To authenticate your Linkedin account, click on button</td> </tr> <tr style=''> <td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> <b>Profile</b> </td> <td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> To view your profile info </td> </tr> <tr style=''> <td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> <b>Post</b> </td> <td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> To post on linked in </td> </tr> <tr style=''> <td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> <b>Reset</b> </td> <td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> Remove your account from teamchat </td> </tr> </table> </div>";
	
	public static final String auth = "<a href='"
									+ "__auth"
									+ "' target='_blank'>"
									+ "<img src=\"http://www.deepspacemarketing.com/wp-content/uploads/2015/02/connect-On-Linkedin.png\" "
									+ "style=\"width:100%;height:50px;border:0\">"
									+ "</a>";
	
	public static final String update = "successfully posted on linkedin, check: <a href='" + "__update" + "' target='_blank'>'" + "__title" + "'</a>";

	public static final String people1 = "https://api.linkedin.com/v1/people/~?format=json";
	public static final String people2 = "https://api.linkedin.com/v1/people/~:(id,num-connections,picture-url)?format=json";
	public static final String share = "https://api.linkedin.com/v1/people/~/shares?format=json";
}
