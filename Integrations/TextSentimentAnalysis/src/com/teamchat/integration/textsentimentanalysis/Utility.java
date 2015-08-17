package com.teamchat.integration.textsentimentanalysis;

public class Utility
{
	public static final String help = "<center><img src='http://cdn.business2community.com/wp-content/uploads/2014/08/sentiment-analysis.jpg' width='150' /></center><h3 style='color:#159ceb'>Hi, i'm Sentiment Analytics Bot </h3><div></div><div><p>I do text analytics, also if you give me a website url I will do sentiment analysis of the website</p><br/><p>Please click analyse button to start</p></div>";
	public static final String apiUrl = "https://loudelement-free-natural-language-processing-service.p.mashape.com/nlp-text/?text=__text";
	public static final String apiKey = "";
	public static final String response = "<center><img src='http://cdn.business2community.com/wp-content/uploads/2014/08/sentiment-analysis.jpg' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>Entered __CopyHere: </b>__EnteredText<br/><b>Sentiment Text: </b>__SentiText"
								+ "<br/><b>Sentiment Score: </b>__SentiScore</p></div>";
	public static final String sorry = "<center><img src='http://cdn.business2community.com/wp-content/uploads/2014/08/sentiment-analysis.jpg' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>";
	public static final String resp = "<br/><div><p style='padding-left:5px; padding-right:5px;align:center;'>"
			+ "<b>Extracted Content: </b>__ExtractedContent</p></div>";
}
//http://integration.teamchat.com/sol/bot-images/sentiment.png
