package com.teamchat.integration.giphy.bot;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.giphy.classes.Datum;
import com.teamchat.integration.giphy.classes.Giphyd;
import com.teamchat.integration.giphy.classes.Images;
import com.teamchat.integration.giphy.classes.Original;
import com.teamchat.integration.giphy.classes.Pagination;

public class botgiphy {

	int sc, nextc, nexta;
	int ttl = 0;
	int count = 0;
	String[] urlpge;
	String[] chatletid;
	String[] urlpge_s;
	String storedstr, temp;
	int offset, limit;
	String chtid, emailid, tag;

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
								+ "Type this command to search any gif image"
								+ "</a2></li><li><a3 style=\"color:black\";><b>next - </b></a3><a4 style=\"color:#359FD8\";>"
								+ "Type this command to check the next result page for your searched keyword"
								+ "</a4></li>"
								+ "</a2></li><li><a3 style=\"color:black\";><b>rename - </b></a3><a4 style=\"color:#359FD8\";>"
								+ "Type this command to change the name of the tags currently stored"
								+ "</a4></li></ul>")));

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

	@OnKeyword("rename")
	public void rename(TeamchatAPI api) throws SQLException {
		
		String emailid = api.context().currentSender().getEmail();
		DBHandler rty = new DBHandler();
		
		if(rty.emailchk(emailid)==true)
		{
		this.emailid = emailid;
		DBHandler gt = new DBHandler();
		String[] tags = gt.gettags(emailid);
		System.err.println(tags[0] + tags[1] + tags[2] + tags[3] + tags[4]);
		Form fo = api.objects().form();
		fo.addField(api.objects().select().name("tag").label("Select one tag:")
				.addOption(tags[0]).addOption(tags[1]).addOption(tags[2])
				.addOption(tags[3]).addOption(tags[4]));
		fo.addField(api.objects().input().name("rename").label("New Name"));

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml(
				"<h4><b>Selct a tag from the drop-down list in the reply below to rename it.</b></h4>")
				.setReplyScreen(fo).setReplyLabel("Reply").alias("renametag");
		api.perform(api.context().currentRoom().post(prime));
		}
		else
		{
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml(
					"<h4><b>No records to display!!!</b></h4>");
				
			api.perform(api.context().currentRoom().post(prime));
		}
	}

	@OnAlias("renametag")
	public void renametag(TeamchatAPI api) throws IOException, SQLException {
		String rename = api.context().currentReply().getField("rename");
		String tag = api.context().currentReply().getField("tag");
		DBHandler gt = new DBHandler();
		String[] tags = gt.gettags(emailid);
		int flag=0;
		for(int i=0;i<tags.length;i++)
		{
			if(tag.equals(tags[i]))
			{
				flag=1;
			}
			
		}
		if(flag==0)
		{
		DBHandler ui = new DBHandler();
		ui.stmt.executeUpdate("UPDATE Bot.giphy_auth"
				+ " SET tag ='" + rename + "'"
				+ " where emailid='" + this.emailid + "' and tag='"
				+ tag + "'");
		ui.conn.close();
		PrimaryChatlet prime1 = new PrimaryChatlet();
		prime1.setQuestionHtml("<h4><b>TAG successfully renamed!!!</b></h4>");
		api.perform(api.context().currentRoom().post(prime1));
		}
		else
		{
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml(
					"<h4><b>TAG already in use!!</b></h4>"
					+ "<br /><b>Type \"<a1 style=\"color:#359FD8\";><b>rename</b></a1>\" again to change the name.</b>");
					
			api.perform(api.context().currentRoom().post(prime));
		}
	}
	
	@OnAlias("getdata")
	public void getdata(TeamchatAPI api) throws IOException {

		String keyword = api.context().currentReply().getField("keyword");
		temp = "";
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
		limit = 5;
		offset = 0;
		giphyintegrator ob1 = new giphyintegrator();
		resp = ob1.getimages(temp, limit, offset);
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

			if (ttl >= 5) {
				urlpge = new String[5];
				chatletid = new String[5];
				// count = 0;
				// sc = 1;
				int k = 1;
				int i = 0;
				Form fo = api.objects().form();
				fo.addField(api.objects().input().label("Tag").name("tag"));
				count = 5;
				for (Datum datas : data.getData()) {

					Images img = datas.getImages();
					Original or = img.getOriginal();
					// OriginalStill ors = img.getOriginalStill();
					PrimaryChatlet prime = new PrimaryChatlet();

					urlpge[i] = or.getUrl();

					System.out.println(urlpge[i]);
					prime.setQuestionHtml(
							"<h6><b>Image " + (k) + "</b></h6>"
									+ "<img src=\"" + or.getUrl()
									+ "\" height=\"140\" width=\"220\"/>")
							.setReplyScreen(fo).setReplyLabel("TAG")
							.alias("gettag");
					api.perform(api.context().currentRoom().post(prime));
					// urlpge[k] = or.getUrl();
					// urlpge_s[k] = ors.getUrl();
					// k++;
					chatletid[i] = prime.getFormId();
					System.err.println(chatletid[i]);
					i++;
					k++;
				}
			} else if (ttl < 5 && ttl != 0) {
				urlpge = new String[ttl];
				chatletid = new String[ttl];
				int k = 1;
				int i = 0;
				count = ttl;
				Form fo = api.objects().form();
				fo.addField(api.objects().input().label("Tag").name("tag"));

				for (Datum datas : data.getData()) {

					Images img = datas.getImages();
					Original or = img.getOriginal();
					// OriginalStill ors = img.getOriginalStill();
					PrimaryChatlet prime = new PrimaryChatlet();

					urlpge[i] = or.getUrl();

					System.out.println(urlpge[i]);
					prime.setQuestionHtml(
							"<h6><b>Image " + (k) + "</b></h6>"
									+ "<img src=\"" + or.getUrl()
									+ "\" height=\"140\" width=\"220\" />")
							.setReplyScreen(fo).setReplyLabel("TAG")
							.alias("gettag");
					api.perform(api.context().currentRoom().post(prime));
					// urlpge[k] = or.getUrl();
					// urlpge_s[k] = ors.getUrl();
					// k++;
					chatletid[i] = prime.getFormId();
					System.err.println(chatletid[i]);
					i++;
					k++;
				}
			} else if (ttl == 0) {
				count = 0;
				PrimaryChatlet prime = new PrimaryChatlet();
				prime.setQuestionHtml("<h4><b>Sorry, No results found!!!</b></h4>");
				api.perform(api.context().currentRoom().post(prime));

			}

		}

	}

	@OnAlias("gettag")
	public void gettag(TeamchatAPI api) throws IOException, SQLException {

		String emailid = api.context().currentReply().senderEmail();
		String tag = api.context().currentReply().getField("tag");

		DBHandler ob3 = new DBHandler();
		boolean fl = ob3.countchk(emailid);
		// System.err.println(tag);
		// System.err.println(tag);
		// System.err.println(tag);
		// System.err.println(tag);
		String chtid = api.context().currentReply().getFormId();
		// System.err.println(chtid);
		// System.err.println(chtid);
		// System.err.println(chtid);
		// System.err.println(chtid);

		if (fl == false) {
			for (int i = 0; i < count; i++) {
				if (chtid.equals(chatletid[i])) {
					DBHandler ob5 = new DBHandler();
					if (ob5.dchk(emailid, tag) == true) {
						DBHandler datacng = new DBHandler();
						datacng.stmt.executeUpdate("UPDATE Bot.giphy_auth"
								+ " SET url ='" + urlpge[i] + "'"
								+ " where emailid='" + emailid + "' and tag='"
								+ tag + "'");
						datacng.conn.close();
						PrimaryChatlet prime1 = new PrimaryChatlet();
						prime1.setQuestionHtml("<h4><b>TAG successfully replaced!!!</b></h4>");
						api.perform(api.context().currentRoom().post(prime1));
					} else {
						DBHandler ob1 = new DBHandler();
						ob1.setData(emailid, tag, urlpge[i]);
						PrimaryChatlet prime1 = new PrimaryChatlet();
						prime1.setQuestionHtml("<h4><b>TAG successfully added!!!</b></h4>");
						api.perform(api.context().currentRoom().post(prime1));

					}
				}
			}
		} else {
			this.emailid = emailid;
			this.tag = tag;
			this.chtid = chtid;
			DBHandler ob2 = new DBHandler();
			String[] tags = ob2.gettags(emailid);
			System.err.println(tags[0] + tags[1] + tags[2] + tags[3] + tags[4]);
			Form fo = api.objects().form();
			fo.addField(api.objects().select().name("tag")
					.label("Select one tag:").addOption(tags[0])
					.addOption(tags[1]).addOption(tags[2]).addOption(tags[3])
					.addOption(tags[4]));

			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml(
					"<h4><b>Sorry, Your storage capacity is full!!!</b></h4>"
							+ "<br /><b>Selct a tag from the drop-down list in the reply to make some free space.</b>")
					.setReplyScreen(fo).setReplyLabel("Reply")
					.alias("getfulltag");
			api.perform(api.context().currentRoom().post(prime));
		}
	}

	@OnAlias("getfulltag")
	public void getfulltag(TeamchatAPI api) throws IOException, SQLException {

		String tag = api.context().currentReply().getField("tag");
		DBHandler datacng = new DBHandler();
		datacng.stmt.executeUpdate("DELETE FROM Bot.giphy_auth"
				+ " WHERE emailid='" + emailid + "' AND tag='" + tag + "'");
		datacng.conn.close();

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml("<h4><b>Deletion Successful!!!</b></h4>");
		api.perform(api.context().currentRoom().post(prime));

		for (int i = 0; i < count; i++) {
			if (chtid.equals(chatletid[i])) {
				DBHandler ob5 = new DBHandler();
				if (ob5.dchk(emailid, this.tag) == true) {
					DBHandler ui = new DBHandler();
					ui.stmt.executeUpdate("UPDATE Bot.giphy_auth"
							+ " SET url ='" + urlpge[i] + "'"
							+ " where emailid='" + this.emailid + "' and tag='"
							+ this.tag + "'");
					ui.conn.close();
					PrimaryChatlet prime1 = new PrimaryChatlet();
					prime1.setQuestionHtml("<h4><b>TAG successfully replaced!!!</b></h4>");
					api.perform(api.context().currentRoom().post(prime1));

				} else {
					DBHandler ob1 = new DBHandler();
					ob1.setData(this.emailid, this.tag, urlpge[i]);
					PrimaryChatlet prime1 = new PrimaryChatlet();
					prime1.setQuestionHtml("<h4><b>TAG successfully added!!!</b></h4>");
					api.perform(api.context().currentRoom().post(prime1));
				}
			}
		}
	}

	@OnKeyword("next")
	public void next(TeamchatAPI api) throws IOException {

		if (ttl > 5 && (offset + limit) < ttl) {
			if ((ttl - (offset + limit)) >= 5) {
				offset = offset + 5;

				giphyintegrator ob1 = new giphyintegrator();
				String resp = ob1.getimages(temp, limit, offset);
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
					Gson gson = new Gson();
					Giphyd data = gson.fromJson(resp, Giphyd.class);
					urlpge = new String[5];
					chatletid = new String[5];
					count = 5;
					// count = 0;
					// sc = 1;
					int k = 1;
					int i = 0;
					Form fo = api.objects().form();
					fo.addField(api.objects().input().label("Tag").name("tag"));

					for (Datum datas : data.getData()) {

						Images img = datas.getImages();
						Original or = img.getOriginal();
						// OriginalStill ors = img.getOriginalStill();
						PrimaryChatlet prime = new PrimaryChatlet();

						urlpge[i] = or.getUrl();

						System.out.println(urlpge[i]);
						prime.setQuestionHtml(
								"<h6><b>Image " + (k) + "</b></h6>"
										+ "<img src=\"" + or.getUrl()
										+ "\" height=\"140\" width=\"220\" />")
								.setReplyScreen(fo).setReplyLabel("TAG")
								.alias("gettag");
						api.perform(api.context().currentRoom().post(prime));
						// urlpge[k] = or.getUrl();
						// urlpge_s[k] = ors.getUrl();
						// k++;
						chatletid[i] = prime.getFormId();
						System.err.println(chatletid[i]);
						i++;
						k++;
					}

					//
				}
			}

			else if ((ttl - (offset + limit)) < 5
					&& (ttl - (offset + limit)) != 0) {

				limit = ttl - (offset + limit);
				offset = offset + 5;

				giphyintegrator ob1 = new giphyintegrator();
				String resp = ob1.getimages(temp, limit, offset);
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

					Gson gson = new Gson();
					Giphyd data = gson.fromJson(resp, Giphyd.class);

					urlpge = new String[limit];
					chatletid = new String[limit];
					count = limit;
					int k = 1;
					int i = 0;
					Form fo = api.objects().form();
					fo.addField(api.objects().input().label("Tag").name("tag"));

					for (Datum datas : data.getData()) {

						Images img = datas.getImages();
						Original or = img.getOriginal();
						// OriginalStill ors = img.getOriginalStill();
						PrimaryChatlet prime = new PrimaryChatlet();

						urlpge[i] = or.getUrl();

						System.out.println(urlpge[i]);
						prime.setQuestionHtml(
								"<h6><b>Image " + (k) + "</b></h6>"
										+ "<img src=\"" + or.getUrl()
										+ "\" height=\"140\" width=\"220\" />")
								.setReplyScreen(fo).setReplyLabel("TAG")
								.alias("gettag");
						api.perform(api.context().currentRoom().post(prime));
						// urlpge[k] = or.getUrl();
						// urlpge_s[k] = ors.getUrl();
						// k++;
						chatletid[i] = prime.getFormId();
						System.err.println(chatletid[i]);
						i++;
						k++;
					}

				}
			}

		} else if ((ttl - (offset + limit)) == 0) {
			count = 0;
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml("<h4><b>Sorry, No images to show!!!</b></h4>");

			api.perform(api.context().currentRoom().post(prime));

		}

	}

	@OnMsg
	public void Mess(TeamchatAPI api) throws SQLException {
		String mess = api.context().currentChatlet().raw();
		String emailid = api.context().currentSender().getEmail();
		DBHandler ob2 = new DBHandler();

		if (ob2.dchk(emailid, mess) == true) {
			DBHandler ob3 = new DBHandler();
			String url = ob3.geturl(emailid, mess);
			PrimaryChatlet prime = new PrimaryChatlet();

			prime.setQuestionHtml("<img src=\"" + url
					+ "\" height=\"140\" width=\"220\" />");
			api.perform(api.context().currentRoom().post(prime));
		}
	}

}
