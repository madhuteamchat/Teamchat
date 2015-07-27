package com.teamchat.integration.linkedin.utils;

public class Utility {
	public static String shareDocHTML = "<div style=''> "
				+ "<center> "
					+ "<div style='margin-left:10px;'> "
						+ "<img src='__imgUrl' style='margin-top:10px;margin-bottom:10px;margin-right:10px;' />"
					+ "</div>"
					+ "<div style=' margin-top:5px; color :#159ceb;font-family: monospace;'>"
						+ "<div style='padding:2px;font-weight:bold'>__name</div>"
						+ "<div style='padding:2px;color :#9B9B9B;'>__headline</div>"
						+ "<div style='padding:2px;color :#9B9B9B;'>__cons</div>"
						+ "<a href='__link' target='_blank' style='text-decoration: none;'>"
							+ "<div style='background-color:rgba(21, 156, 235, 0.49);padding:2px;color :white;'>__profile</div>"
						+ "</a>"
					+ "</div>"
				+ "</center>"
			+ "</div>";
}