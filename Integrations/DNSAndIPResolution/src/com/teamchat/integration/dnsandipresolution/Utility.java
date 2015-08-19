package com.teamchat.integration.dnsandipresolution;

public class Utility
{
	public static final String start = "<div style='box-shadow:0px 0px 3px DodgerBlue;'> ";
	public static final String div = "<div align='center'><b style='color:grey; font-size:13px '>DNS Tools</b></div>";
	public static final String startTable = "<table width='100%' border='0px' bordercolor='DodgerBlue ' style='font-size:11px;border-collapse: collapse;'> <!--headrer --> ";
	public static final String head = "<tr style=' border-top:5px; border-top-color:DodgerBlue; border-top-style:solid;border-bottom:5px; border-bottom-color:DodgerBlue; border-bottom-style:solid'> <td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px; border-right:thin white solid;'>Keyword</td><td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px;'>What it does</td> </tr>";
	public static final String rowWhite = "<tr style=''> <td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> <b>__aa</b> </td><td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> <b>__bb</b> </td> </tr> ";
	public static final String rowSmoke = "<tr style=''> <td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> <b>__aa</b> </td><td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> <b>__bb</b> </td> </tr> ";
	public static final String end = "</table> </div>";
	
	public static final String help = "<center><img src='http://integration.teamchat.com/sol/bot-images/dnstools.png' width='150' /></center><h3 style='color:#159ceb'>Hi, I'm DNS Tools Bot </h3><div></div><div><p>I can do the following:</p></div>"
										+ start + div + startTable + head + rowSmoke.replace("__aa", "IP2DNS").replace("__bb", "It returns the hostname of a given IP address.")
										+ rowWhite.replace("__aa", "DNS2IP").replace("__bb", "It returns the IP address of a given DNS address.")
										+ rowSmoke.replace("__aa", "CHKPRT").replace("__bb", "Check if a port is open on a remote host.")
										+ end
										+ "<div><p style='padding-right:5px; margin-top:8px'>Please click 'Method' button to start</p></div>";
	public static final String apiUrlIPToDNS = "https://jack-dns-tools.p.mashape.com/dnstools.php?_method=IP2DNS&ip=__IPAddress";
	public static final String apiUrlDNSToIp = "https://jack-dns-tools.p.mashape.com/dnstools.php?_method=DNS2IP&dns=__DNSServer";
	public static final String apiUrlCheckPort = "https://jack-dns-tools.p.mashape.com/dnstools.php?_method=CHKPRT&host=__Host&port=__Port";
	public static final String apiKey = "";
	public static final String image = "<center><img src='http://integration.teamchat.com/sol/bot-images/dnstools.png' width='150' /></center>";
	public static final String sorry = "<center><img src='http://integration.teamchat.com/sol/bot-images/dnstools.png' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>";
	
	public static final String paraStart = "<div><p style='padding-right:5px; padding-left:5px; margin-top:5px;'>";
	public static final String paraEnd = "</p></div>";
	
	public static final String resp = "<center><img src='http://integration.teamchat.com/sol/bot-images/dnstools.png' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>__AA: </b>__BB</p></div>";
}
