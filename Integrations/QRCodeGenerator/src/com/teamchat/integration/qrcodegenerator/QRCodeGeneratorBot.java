package com.teamchat.integration.qrcodegenerator;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class QRCodeGeneratorBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Enter text/url").name("enteredtext").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help).setReplyScreen(f).setReplyLabel("Enter").alias("generate")));
	}

	@OnKeyword("qrcode")
	public void convert(TeamchatAPI api) throws Exception
	{
		Form f = api.objects().form();
		f.addField(api.objects().select().label("Select sentiment anlytics type").addOption("Text").addOption("Url").addRegexValidation(".+", "please select one option").name("sentiment_type"));
		f.addField(api.objects().input().label("Enter text/url (add http:// or https:// before url)").name("enteredtext").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Please click 'enter' button to generate").setReplyScreen(f).setReplyLabel("Analyse").alias("analysis")));
	}

	@OnAlias("generate")
	public void converted(TeamchatAPI api) throws Exception
	{
		String text = api.context().currentReply().getField("enteredtext");
		String encodedText = URLEncoder.encode(text, "UTF-8");
		String fileName = encodedText.substring(0,10) + ".png";

		String url = Utility.apiUrl.replace("__text", encodedText);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", Utility.apiKey);

		int responseCode = con.getResponseCode();

		if (responseCode == 200)
		{
			InputStream input = con.getInputStream();
			byte[] buffer = new byte[4096];
			int n = - 1;

			OutputStream output = new FileOutputStream(Utility.path + fileName);
			while ( (n = input.read(buffer)) != -1) 
			{
			    output.write(buffer, 0, n);
			}
			output.close();
			
			String fileUrl = Utility.server + fileName;
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.resp.replace("__fileUrl", fileUrl))));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}
}