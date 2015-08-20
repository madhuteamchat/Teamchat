package com.teamchat.integration.texttospeech;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.sample.CountryCodeUtil;

public class TextToSpeechBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		Form f = api.objects().form();
		Field fd = api.objects().select().label("Language").addRegexValidation(".+", "Please select a language").name("languageEntered");
		
		ArrayList<String> langs = CountryCodeUtil.getLanguagesList();
		Collections.sort(langs);
		for (int i=0;i<langs.size();i++)
		{
			fd.addOption(langs.get(i));
		}
		
		f.addField(fd);
		f.addField(api.objects().input().label("Text").addRegexValidation(".+", "Please enter some text").name("textToConvert"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(Utility.help).setReplyScreen(f).setReplyLabel("Convert").showDetails(true).setDetailsLabel("replies").allowComments(true)
						.alias("textToSpeech")));
	}

	@OnAlias("textToSpeech")
	public void textToSpeech(TeamchatAPI api) throws Exception
	{
		String lang = api.context().currentReply().getField("languageEntered");
		String langCode = CountryCodeUtil.getLangCode(lang);
		
		String text = api.context().currentReply().getField("textToConvert");
		System.out.println(text);
		String textN = URLEncoder.encode(text, "UTF-8");
		System.out.println(textN);
		
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.resp
														.replace("__Lang", langCode).replace("__EncodedText", textN).replace("__Text", text))));
	}
}