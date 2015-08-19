package com.teamchat.integration.dnsandipresolution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class DNSAndIPResolutionBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().select().label("Select method").addOption("Get Hostname for IP address").addOption("Get IP address for DNS address").addOption("Check if port is open or not")
				.name("selectMethod").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(Utility.help).setReplyScreen(f).setReplyLabel("Method").showDetails(true).setDetailsLabel("replies").allowComments(true)
						.alias("methodCheck")));
	}

	@OnAlias("methodCheck")
	public void methodCheck(TeamchatAPI api) throws Exception
	{
		String method = api.context().currentReply().getField("selectMethod");
		if (method.equals("Get Hostname for IP address"))
		{
			Form f = api.objects().form();
			f.addField(api.objects().input().label("IP address (eg. 54.209.199.175)").name("ipaddress").addRegexValidation(".+", "field cannot be left blank"));

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(Utility.image + Utility.paraStart + "Please enter IP address" + Utility.paraEnd).setReplyScreen(f).setReplyLabel("Enter")
							.showDetails(true).setDetailsLabel("replies").allowComments(true).alias("ipaddress")));
		}
		else if (method.equals("Get IP address for DNS address"))
		{
			Form f = api.objects().form();
			f.addField(api.objects().input().label("DNS address (eg. google.com)").name("dnsaddress").addRegexValidation(".+", "field cannot be left blank"));

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(Utility.image + Utility.paraStart + "Please enter DNS address" + Utility.paraEnd).setReplyScreen(f).setReplyLabel("Enter")
							.showDetails(true).setDetailsLabel("replies").allowComments(true).alias("dnsaddress")));
		}
		else if (method.equals("Check if port is open or not"))
		{
			Form f = api.objects().form();
			f.addField(api.objects().input().label("Host Name (eg. google.com)").name("hostname").addRegexValidation(".+", "field cannot be left blank"));
			f.addField(api.objects().input().label("Port (eg. 80, 8080)").name("givenPort").addRegexValidation(".+", "field cannot be left blank"));

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(Utility.image + Utility.paraStart + "Please enter Host name and port you want to check if open" + Utility.paraEnd).setReplyScreen(f)
							.setReplyLabel("Enter").showDetails(true).setDetailsLabel("replies").allowComments(true).alias("checkport")));
		}
	}

	@OnKeyword("IP2DNS")
	public void IP2DNS(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().label("IP address (eg. 10.0.0.1)").name("ipaddress").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(Utility.image + Utility.paraStart + "Please enter IP address" + Utility.paraEnd).setReplyScreen(f).setReplyLabel("Enter").showDetails(true)
						.setDetailsLabel("replies").allowComments(true).alias("ipaddress")));
	}

	@OnKeyword("DNS2IP")
	public void DNS2IP(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().label("DNS address (eg. google.com)").name("dnsaddress").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(Utility.image + Utility.paraStart + "Please enter DNS address" + Utility.paraEnd).setReplyScreen(f).setReplyLabel("Enter").showDetails(true)
						.setDetailsLabel("replies").allowComments(true).alias("dnsaddress")));
	}

	@OnKeyword("CHKPRT")
	public void CHKPRT(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Host Name (eg. google.com)").name("hostname").addRegexValidation(".+", "field cannot be left blank"));
		f.addField(api.objects().input().label("Port (eg. 80)").name("givenPort").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(Utility.image + Utility.paraStart + "Please enter Host name and Port you want to check if open" + Utility.paraEnd).setReplyScreen(f)
						.setReplyLabel("Enter").showDetails(true).setDetailsLabel("replies").allowComments(true).alias("checkport")));
	}

	@OnAlias("ipaddress")
	public void ipAddr(TeamchatAPI api) throws Exception
	{
		String ip = api.context().currentReply().getField("ipaddress");
		String url = Utility.apiUrlIPToDNS.replace("__IPAddress", ip);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", Utility.apiKey);
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

			String host = "";

			JSONObject json = new JSONObject(response.toString());
			if (json.has("host") && !json.isNull("host")) host = json.getString("host");

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.resp.replace("__AA", "Host").replace("__BB", host))));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}

	@OnAlias("dnsaddress")
	public void dnsAddr(TeamchatAPI api) throws Exception
	{
		String dns = api.context().currentReply().getField("dnsaddress");
		dns = URLEncoder.encode(dns, "UTF-8");
		String url = Utility.apiUrlDNSToIp.replace("__DNSServer", dns);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", Utility.apiKey);
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

			String ip = "";

			JSONObject json = new JSONObject(response.toString());
			if (json.has("ip") && !json.isNull("ip")) ip = json.getString("ip");

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.resp.replace("__AA", "IP").replace("__BB", ip))));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}

	@OnAlias("checkport")
	public void checkPort(TeamchatAPI api) throws Exception
	{
		String host = api.context().currentReply().getField("hostname");
		String port = api.context().currentReply().getField("givenPort");
		String url = Utility.apiUrlCheckPort.replace("__Host", host).replace("__Port", port);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", Utility.apiKey);
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

			boolean result = false;
			String status = "";
			JSONObject json = new JSONObject(response.toString());
			if (json.has("result") && !json.isNull("result"))
			{
				result = json.getBoolean("result");
				status = result ? "open" : "close";
			}

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.resp.replace("__AA", "Port").replace("__BB", status))));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}
}