package com.teamchat.integration.numberfact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Random;

import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class NumberFactBot
{
	Properties configProps;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnKeyword("fact")
	public void fact(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().select().addOption("Date").addOption("Math").addOption("Random").addOption("Trivia").addOption("Year").label("Choose Option").name("fact"));

		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("What do you want to get facts for?").setReplyScreen(f).setReplyLabel("Enter").alias("getfact")));
	}

	@OnAlias("getfact")
	public void getFact(TeamchatAPI api) throws Exception
	{
		try
		{
			configProps = loadPropertyFromClasspath("numberfact-config.properties", NumberFactBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String fact = api.context().currentReply().getField("fact");

		if (fact.equals("Date"))
		{
			Form f = api.objects().form();

			Field fd = api.objects().select().label("Day").name("day");
			for (int i = 1; i < 32; i++)
				fd.addOption(i + "");

			Field fm = api.objects().select().label("Month").name("month");
			for (int i = 1; i < 13; i++)
				fm.addOption(i + "");

			f.addField(fd);
			f.addField(fm);

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter day and month").setReplyScreen(f).setReplyLabel("Enter").alias("datefact")));
		}

		if (fact.equals("Math"))
		{
			Form f = api.objects().form();
			Field fd = api.objects().input().label("Number").name("num");
			f.addField(fd);

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter integer of interest").setReplyScreen(f).setReplyLabel("Enter").alias("numberfact")));
		}

		if (fact.equals("Random"))
		{
			String[] type = {"date", "math", "trivia", "year"};
			int random  = randInt(1, 4);

			String url = "https://numbersapi.p.mashape.com/random/" + type[random];

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("X-Mashape-Key", configProps.getProperty("apikey"));
			con.setRequestProperty("Accept", "application/json");

			int responseCode = con.getResponseCode();

			if (responseCode == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null)
				{
					response.append(inputLine);
				}
				in.close();
				
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>Fact: </b>" + response.toString() + "</p></div>")));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>")));
			}
		}

		if (fact.equals("Trivia"))
		{
			Form f = api.objects().form();
			Field fd = api.objects().input().label("Number").name("num");
			f.addField(fd);

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter integer of interest").setReplyScreen(f).setReplyLabel("Enter").alias("numberfact")));
		}

		if (fact.equals("Year"))
		{
			Form f = api.objects().form();
			Field fd = api.objects().input().label("Number").name("num");
			f.addField(fd);

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter year of interest").setReplyScreen(f).setReplyLabel("Enter").alias("numberfact")));
		}
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	@OnAlias("datefact")
	public void dateFact(TeamchatAPI api) throws Exception
	{
		try
		{
			configProps = loadPropertyFromClasspath("numberfact-config.properties", NumberFactBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String day = api.context().currentReply().getField("day");
		String month = api.context().currentReply().getField("month");

		String url = "https://numbersapi.p.mashape.com/" + month + "/" + day + "/date?fragment=true&json=true";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", configProps.getProperty("apikey"));
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();

		if (responseCode == 200)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			JSONObject json = new JSONObject(response.toString());
			
			String fact = json.getString("text");
			int year = json.getInt("year");
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>Fact: </b>" + fact + "<br/><br/><b>Year: </b>" + year +  "</p></div>")));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>")));
		}
	}
	
	@OnAlias("numberfact")
	public void numberFact(TeamchatAPI api) throws Exception
	{
		try
		{
			configProps = loadPropertyFromClasspath("numberfact-config.properties", NumberFactBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String number = api.context().currentReply().getField("num");

		String url = "https://numbersapi.p.mashape.com/" + number + "/math";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", configProps.getProperty("apikey"));
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();

		if (responseCode == 200)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			JSONObject json = new JSONObject(response.toString());
			
			String fact  = json.getString("text");
			String num = json.getString("number");
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>Number: </b>" + num + "<br/><br/><b>Fact: </b>" + fact +  "</p></div>")));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>")));
		}
	}
	
	@OnAlias("randomfact")
	public void randomFact(TeamchatAPI api) throws Exception
	{
		try
		{
			configProps = loadPropertyFromClasspath("numberfact-config.properties", NumberFactBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String type = api.context().currentReply().getField("random");
		type = type.toLowerCase();

		String url = "https://numbersapi.p.mashape.com/random/" + type;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", configProps.getProperty("apikey"));
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();

		if (responseCode == 200)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			JSONObject json = new JSONObject(response.toString());
			
			String fact  = json.getString("text");
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>Fact: </b>" + fact + "</p></div>")));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>")));
		}
	}
	
	@OnAlias("triviafact")
	public void triviaFact(TeamchatAPI api) throws Exception
	{
		try
		{
			configProps = loadPropertyFromClasspath("numberfact-config.properties", NumberFactBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String number = api.context().currentReply().getField("num");

		String url = "https://numbersapi.p.mashape.com/" + number + "/trivia";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", configProps.getProperty("apikey"));
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();

		if (responseCode == 200)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			JSONObject json = new JSONObject(response.toString());
			
			String fact  = json.getString("text");
			String num = json.getString("number");
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>Number: </b>" + num + "<br/><br/><b>Fact: </b>" + fact +  "</p></div>")));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p>.</div>")));
		}
	}
	
	@OnAlias("yearfact")
	public void yearFact(TeamchatAPI api) throws Exception
	{
		try
		{
			configProps = loadPropertyFromClasspath("numberfact-config.properties", NumberFactBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String year = api.context().currentReply().getField("num");

		String url = "https://numbersapi.p.mashape.com/" + year + "/year";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", configProps.getProperty("apikey"));
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();

		if (responseCode == 200)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			JSONObject json = new JSONObject(response.toString());
			
			String fact  = json.getString("text");
			String num = json.getString("number");
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'><b>Year: </b>" + num + "<br/><br/><b>Fact: </b>" + fact +  "</p></div>")));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/numbers.jpg' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>")));
		}
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
	{
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}