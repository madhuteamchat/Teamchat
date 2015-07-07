package com.teamchat.integration.giphy.bot;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.giphy.classes.Datum;
import com.teamchat.integration.giphy.classes.Giphyd;
import com.teamchat.integration.giphy.classes.Images;
import com.teamchat.integration.giphy.classes.Original;
import com.teamchat.integration.giphy.classes.OriginalStill;
import com.teamchat.integration.giphy.classes.Pagination;

public class giphybot {

	int count, ttl, sc;
	String[] urlpge;
	String[] urlpge_s;
	String storedstr;

	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h4><b>Hi, I'm Giphy Bot.</b></h4>"
								+ "<img src=\"http://api.giphy.com/img/api_giphy_logo.png\" height=\"140\" width=\"95\">"
								+ "<br />"
								+ "<br /><b>You can use me to search any gif image, using following keywords:</b>"
								+ "<br />"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>search - </b></a1><a2 style=\"color:#359FD8\";>"
								+ "Type this command to search any gif image and share with other people, available or unavailable on Teamchat."
								+ "</a2></li><li><a3 style=\"color:black\";><b>addcheck - </b></a3><a4 style=\"color:#359FD8\";>"
								+ "Type this command to create any check i.e to add any website in your pingdom account."
								+ "</a4></li><li><a5 style=\"color:black\";><b>deletecheck - </b></a5><a6 style=\"color:#359FD8\";>"
								+ "Type this command to delete any check i.e to delete any website in your pingdom account."
								+ "</a6></li><li><a7 style=\"color:black\";><b>login - </b></a5><a6 style=\"color:#359FD8\";>"
								+ "Type this command if you are logging for the first time from this room or you want to log in from other account."
								+ "</a6></li><li><a7 style=\"color:black\";><b>logout - </b></a7><a8 style=\"color:#359FD8\";>"
								+ "Type this command to log out from your existing account."
								+ "</a8></li></ul>")));

		// list of keywords
		// 1.)search

	}

	@OnKeyword("search")
	public void login(TeamchatAPI api) {

		Form f = api.objects().form();
		f.addField(api.objects().input().label("Keyword").name("keyword"));

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml(
				"<h4><b>Enter any keyword or paraphrase to search an image:</b></h4>")
				.setReplyScreen(f).setReplyLabel("Enter").alias("getdata");
		api.perform(api.context().currentRoom().post(prime));

	}

	@OnAlias("getdata")
	public void getdata(TeamchatAPI api) throws IOException {

		String keyword = api.context().currentReply().getField("keyword");

		String temp = "";
		String resp;
		int l;

		keyword = keyword.trim();
		l = keyword.length();
		for (int i = 0; i < l; i++) {
			if (keyword.charAt(i) == ' ') {
				temp = temp + '+';
			} else
				temp = temp + keyword.charAt(i);
		}
		// System.out.println(temp);

		giphyintegrator ob1 = new giphyintegrator();
		resp = ob1.getimages(temp);
		// System.out.println(resp);

		if (resp.equals("Error")) {
			PrimaryChatlet prime = new PrimaryChatlet();
			api.perform(api
					.context()
					.currentRoom()
					.post(prime.setQuestionHtml("<br /><b>Error :</b>"
							+ "<br /><b>Status Code: 403</b>"
							+ "<br /><b>StatusDesc: Forbidden</b>"
							+ "<br /><b>Something went wrong!</b>")));

			// System.out.println("loggg");
		}

		else {

			// parsing a json like this

			// "data": [
			// {
			// "type": "gif",
			// "id": "v37chFjA54wHC",
			// "url":
			// "http:\\/\\/giphy.com\\/gifs\\/windows-microsoft-95-v37chFjA54wHC",
			// "bitly_gif_url": "http:\\/\\/gph.is\\/1AVsFVP",
			// "bitly_url": "http:\\/\\/gph.is\\/1AVsFVP",
			// "embed_url": "http:\\/\\/giphy.com\\/embed\\/v37chFjA54wHC",
			// "username": "",
			// "source":
			// "http:\\/\\/mckinney-n-sons.tumblr.com\\/post\\/107542006579",
			// "rating": "g",
			// "caption": "",
			// "content_url": "",
			// "import_datetime": "2015-01-08 23:20:16",
			// "trending_datetime": "1970-01-01 00:00:00",
			// "images": {
			// "fixed_height": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200.gif",
			// "width": "267",
			// "height": "200",
			// "size": "0",
			// "mp4":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200.mp4",
			// "mp4_size": "33989",
			// "webp":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200.webp",
			// "webp_size": "38972"
			// },
			// "fixed_height_still": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200_s.gif",
			// "width": "267",
			// "height": "200"
			// },
			// "fixed_height_downsampled": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200_d.gif",
			// "width": "267",
			// "height": "200",
			// "size": "39773",
			// "webp":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200_d.webp",
			// "webp_size": "15008"
			// },
			// "fixed_width": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200w.gif",
			// "width": "200",
			// "height": "150",
			// "size": "0",
			// "mp4":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200w.mp4",
			// "mp4_size": "37096",
			// "webp":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200w.webp",
			// "webp_size": "28568"
			// },
			// "fixed_width_still": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200w_s.gif",
			// "width": "200",
			// "height": "150"
			// },
			// "fixed_width_downsampled": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200w_d.gif",
			// "width": "200",
			// "height": "150",
			// "size": "61978",
			// "webp":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/200w_d.webp",
			// "webp_size": "11274"
			// },
			// "fixed_height_small": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100.gif",
			// "width": "133",
			// "height": "100",
			// "size": "0",
			// "mp4":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100.mp4",
			// "mp4_size": "32102",
			// "webp":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100.webp",
			// "webp_size": "18432"
			// },
			// "fixed_height_small_still": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100_s.gif",
			// "width": "133",
			// "height": "100"
			// },
			// "fixed_width_small": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100w.gif",
			// "width": "100",
			// "height": "75",
			// "size": "0",
			// "mp4":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100w.mp4",
			// "mp4_size": "24257",
			// "webp":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100w.webp",
			// "webp_size": "13456"
			// },
			// "fixed_width_small_still": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/100w_s.gif",
			// "width": "100",
			// "height": "75"
			// },
			// "downsized": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/giphy.gif",
			// "width": "384",
			// "height": "288",
			// "size": "146850"
			// },
			// "downsized_still": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/giphy_s.gif",
			// "width": "384",
			// "height": "288"
			// },
			// "downsized_large": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/giphy.gif",
			// "width": "384",
			// "height": "288",
			// "size": "146850"
			// },
			// "original": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/giphy.gif",
			// "width": "384",
			// "height": "288",
			// "size": "146850",
			// "frames": "16",
			// "mp4":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/giphy.mp4",
			// "mp4_size": "109307",
			// "webp":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/giphy.webp",
			// "webp_size": "60548"
			// },
			// "original_still": {
			// "url":
			// "http:\\/\\/media1.giphy.com\\/media\\/v37chFjA54wHC\\/giphy_s.gif",
			// "width": "384",
			// "height": "288"
			// }
			// }
			// }
			// ],
			// "meta": {
			// "status": 200,
			// "msg": "OK"
			// },
			// "pagination": {
			// "total_count": 1,
			// "count": 1,
			// "offset": 0
			// }
			// }

			Gson gson = new Gson();
			Giphyd data = gson.fromJson(resp, Giphyd.class);
			Pagination cnt = data.getPagination();
			ttl = cnt.getTotalCount();
			urlpge = new String[ttl];
			urlpge_s = new String[ttl];
			count = 0;
			sc = 1;
			int k = 0;
			Form fo = api.objects().form();

			for (Datum datas : data.getData()) {

				Images img = datas.getImages();
				Original or = img.getOriginal();
				OriginalStill ors = img.getOriginalStill();
				urlpge[k] = or.getUrl();
				urlpge_s[k] = ors.getUrl();
				k++;

			}
			if(ttl==0)
			{
				PrimaryChatlet prime = new PrimaryChatlet();
				prime.setQuestionHtml(
						"<h4><b>Sorry, No results found!!!</b></h6>");
					 
				api.perform(api.context().currentRoom().post(prime));
				
			}
			if (ttl > 4) {
				for (int c = 0; c < 5; c++) {
					fo.addField(api.objects().select()
							.name("img" + String.valueOf(sc))
							.label("Image " + String.valueOf(sc) + ":")
							.addOption("yes").addOption("no"));
					sc++;
				}
				count = 5;
				PrimaryChatlet prime = new PrimaryChatlet();
				prime.setQuestionHtml(
						"<h6><b>Image 1:</b></h6>" + "<img src=\"" + urlpge[0]+ "\" height=\"160\" width=\"280\">"
					  + "<h6><b>Image 2:</b></h6>" + "<img src=\"" + urlpge[1]+"\" height=\"160\" width=\"280\">"
					  + "<h6><b>Image 3:</b></h6>" + "<img src=\"" + urlpge[2]+"\" height=\"160\" width=\"280\">"
					  + "<h6><b>Image 4:</b></h6>" + "<img src=\"" + urlpge[3]+"\" height=\"160\" width=\"280\">"
					  + "<h6><b>Image 5:</b></h6>" + "<img src=\"" + urlpge[4]+"\" height=\"160\" width=\"280\">")
					  .setReplyScreen(fo).setReplyLabel("Enter")
						.alias("getdata2");
				api.perform(api.context().currentRoom().post(prime));

			}
			if(ttl==1)
			{
				for (int c = 0; c < ttl; c++) {
					fo.addField(api.objects().select()
							.name("img" + String.valueOf(sc))
							.label("Image " + String.valueOf(sc) + ":")
							.addOption("yes").addOption("no"));
					sc++;
				}
				count = 1;
				PrimaryChatlet prime = new PrimaryChatlet();
				prime.setQuestionHtml(
						"<h6><b>Image 1:</b></h6>" + "<img src=\"" + urlpge[0]+ "\" height=\"160\" width=\"280\">")
					  .setReplyScreen(fo).setReplyLabel("Enter")
						.alias("getdata2");
				api.perform(api.context().currentRoom().post(prime));
			}
			if(ttl==2)
			{
				for (int c = 0; c < ttl; c++) {
					fo.addField(api.objects().select()
							.name("img" + String.valueOf(sc))
							.label("Image " + String.valueOf(sc) + ":")
							.addOption("yes").addOption("no"));
					sc++;
				}
				count = 2;
				PrimaryChatlet prime = new PrimaryChatlet();
				prime.setQuestionHtml(
						"<h6><b>Image 1:</b></h6>" + "<img src=\"" + urlpge[0]+ "\" height=\"160\" width=\"280\">"
								+"<h6><b>Image 2:</b></h6>" + "<img src=\"" + urlpge[1]+ "\" height=\"160\" width=\"280\">")
					  .setReplyScreen(fo).setReplyLabel("Enter")
						.alias("getdata2");
				api.perform(api.context().currentRoom().post(prime));
			}

			if(ttl==3)
			{
				for (int c = 0; c < ttl; c++) {
					fo.addField(api.objects().select()
							.name("img" + String.valueOf(sc))
							.label("Image " + String.valueOf(sc) + ":")
							.addOption("yes").addOption("no"));
					sc++;
				}
				count = 3;
				PrimaryChatlet prime = new PrimaryChatlet();
				prime.setQuestionHtml(
						"<h6><b>Image 1:</b></h6>" + "<img src=\"" + urlpge[0]+ "\" height=\"160\" width=\"280\">"
								+"<h6><b>Image 2:</b></h6>" + "<img src=\"" + urlpge[1]+ "\" height=\"160\" width=\"280\">"
										+ "<h6><b>Image 3:</b></h6>" + "<img src=\"" + urlpge[2]+ "\" height=\"160\" width=\"280\">")
					  .setReplyScreen(fo).setReplyLabel("Enter")
						.alias("getdata2");
				api.perform(api.context().currentRoom().post(prime));
			}
			if(ttl==4)
			{
				for (int c = 0; c < ttl; c++) {
					fo.addField(api.objects().select()
							.name("img" + String.valueOf(sc))
							.label("Image " + String.valueOf(sc) + ":")
							.addOption("yes").addOption("no"));
					sc++;
				}
				count = 4;
				PrimaryChatlet prime = new PrimaryChatlet();
				prime.setQuestionHtml(
						"<h6><b>Image 1:</b></h6>" + "<img src=\"" + urlpge[0]+ "\" height=\"160\" width=\"280\">"
								+"<h6><b>Image 2:</b></h6>" + "<img src=\"" + urlpge[1]+ "\" height=\"160\" width=\"280\">"
										+ "<h6><b>Image 3:</b></h6>" + "<img src=\"" + urlpge[2]+ "\" height=\"160\" width=\"280\">"
												+ "<h6><b>Image 4:</b></h6>" + "<img src=\"" + urlpge[3]+ "\" height=\"160\" width=\"280\">")
					  .setReplyScreen(fo).setReplyLabel("Enter")
						.alias("getdata2");
				api.perform(api.context().currentRoom().post(prime));
			}
			
		}

	}

	@OnAlias("getdata2")
	public void getdata2(TeamchatAPI api) throws IOException {
		storedstr="";
		int sc=1;
		for(int i=0;i<count;i++)
		{
			if(api.context().currentReply().getField("img"+sc).equalsIgnoreCase("yes"))
			{
			storedstr=storedstr+ String.valueOf(sc);
			storedstr=storedstr+',';
			}
			sc++;
		}
		System.out.println(storedstr);

	}

}
