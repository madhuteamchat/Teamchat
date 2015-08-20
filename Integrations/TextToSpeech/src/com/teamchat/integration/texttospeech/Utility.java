package com.teamchat.integration.texttospeech;

public class Utility
{
	public static final String help = "<center><img src='http://integration.teamchat.com/sol/bot-images/texttospeech.png' width='150' /></center><h3 style='color:#159ceb'>Hi, I'm Text-to-Speech Bot </h3><div></div><div><p>I'll convert entered text to speech</p><p>Please click <b>Convert</b> button to start</div>";
	public static final String image = "<center><img src='http://integration.teamchat.com/sol/bot-images/texttospeech.png' width='150' /></center>";
	public static final String sorry = "<center><img src='http://integration.teamchat.com/sol/bot-images/texttospeech.png' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>";
	
	public static final String paraStart = "<div><p style='padding-right:5px; padding-left:5px; margin-top:5px;'><b>Click below link to listen:</b></p><p style='padding-right:5px; padding-left:5px; margin-top:5px;'>";
	public static final String paraEnd = "</p></div>";
	public static final String serverLocal = "http://localhost:8080/TextToSpeech/audio.html";
	public static final String server = "http://integration.teamchat.com....";
	public static final String resp = image + paraStart + "<a href='" + serverLocal + "?lang=__Lang&text=__EncodedText' target='_blank'>__Text</a>" + paraEnd;
}