package com.teamchat.integration.datarechargeplans;

public class Utility
{
	public static final String help = "<center><img src='https://lh3.ggpht.com/Ef12o-o6qqVDHa2P5iVKl8zaOdmGYo8k0RPYthBdvzrC0tsfNNtxqz3boI2CfCcWkrM=w300' width='150' /></center><h3 style='color:#159ceb'>Hi, I am Recharge Plans Info Bot </h3><div></div><div><p>I will give you current recharge plans info based on your circle and operator</p><p>Please click <b>select</b> button to start</p><p>Also you can send <b>plans</b> keyword to get information</p></div>";
	public static final String apiUrl = "https://sphirelabs-indian-telecom-data-recharge-plans-v1.p.mashape.com/telecomdata/v1/get/index.php?circle=__CCircle&opcode=__OOperator&type=__TType";
	public static final String apiKey = "";
	public static final String sorry = "<center><img src='http://integration.teamchat.com/sol/bot-images/recharge.png' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>";
	
	public static final String image = "<div><center><p style='margin-bottom:5px;'><img src='https://lh3.ggpht.com/Ef12o-o6qqVDHa2P5iVKl8zaOdmGYo8k0RPYthBdvzrC0tsfNNtxqz3boI2CfCcWkrM=w300' width='150' /></p></center></div>";
	public static final String start = "<div style='box-shadow:0px 0px 3px DodgerBlue;'> ";
	public static final String div = "<div align='center'><b style='color:grey; font-size:13px '>__aa</b></div>";
	public static final String startTable = "<table width='100%' border='0px' bordercolor='DodgerBlue ' style='font-size:11px;border-collapse: collapse;'> <!--headrer --> ";
	public static final String head = "<tr style=' border-top:5px; border-top-color:DodgerBlue; border-top-style:solid;border-bottom:5px; border-bottom-color:DodgerBlue; border-bottom-style:solid'> <td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px; border-right:thin white solid;'>__aa</td><td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px; border-right:thin white solid;'>__bb</td><td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px; border-right:thin white solid;'>__cc</td> <td width='25%' style='background-color:DodgerBlue ; color:white; padding:0px 5px 0px 5px;'>__dd</td> </tr>";
	public static final String rowWhite = "<tr style=''> <td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> <b>__aa</b> </td><td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> <b>__bb</b> </td><td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'> <b>__cc</b> </td> <td style='background-color:White ; color:black; padding:3px 5px 3px 5px;'><b>__dd</b></td> </tr> ";
	public static final String rowSmoke = "<tr style=''> <td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> <b>__aa</b> </td><td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> <b>__bb</b> </td><td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'> <b>__cc</b> </td> <td style='background-color:WhiteSmoke ; color:black; padding:3px 5px 3px 5px;'><b>__dd</b></td> </tr> ";
	public static final String end = "</table> </div>";
}
//http://integration.teamchat.com/sol/bot-images/recharge.png
