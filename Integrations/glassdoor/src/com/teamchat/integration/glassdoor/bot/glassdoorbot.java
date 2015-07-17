package com.teamchat.integration.glassdoor.bot;

import java.io.IOException;

import com.google.gson.Gson;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.glassdoor.classes.Ceo;
import com.teamchat.integration.glassdoor.classes.Employer;
import com.teamchat.integration.glassdoor.classes.FeaturedReview;
import com.teamchat.integration.glassdoor.classes.Glassdoormain;
import com.teamchat.integration.glassdoor.classes.Image;
import com.teamchat.integration.glassdoor.classes.Response;


public class glassdoorbot {

	int pn,ttp,ttc;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h4><b>Hi, I'm Glassdoor Bot.</b></h4>"
								+ "<img src=\"http://recruitingwebinars.com/wp-content/uploads/sites/3/2014/06/GD_forEmployers_Logo_600x200Pixels11.png\" height=\"90\" width=\"230\" />"
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
								+ "</a4></li></ul>"
								+ "<br />"
								+ "<br /><b>Note: </b><a1  style=\"color:#359FD8\";>To make your tags work , You have to make a P2P group with the bot present inside it.</a1>")));

		// list of keywords
		// 1.)search
		// 2.)rename
		// 3.)next

	}
	
	@OnKeyword("search")
	public void login(TeamchatAPI api) {

		Form f = api.objects().form();
		f.addField(api.objects().input().label("Query phrase to search for:").name("keyword"));
		f.addField(api.objects().input().label("Location:").name("Location"));

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml(
				"<h2><b>Get Hired. Love Your Job.</b></h2>"
				+ "<a1 style=\"color:#359FD8\";><b>powered by </b></a1><img src='http://www.glassdoor.com/static/img/api/glassdoor_logo_80.png' style=\"padding-bottom: 0.35cm\";/></a>"
				+ "<br />"
				+ "<h5><b>Enter any occupation or job title with the location you want to search for in the reply option.</b><h5>"
				+ "<br /><b>Note: </b><a1  style=\"color:#359FD8\";>Location and job title are not mandatory.</a1>")
				.setReplyScreen(f).setReplyLabel("Reply").alias("getdata");
		api.perform(api.context().currentRoom().post(prime));
	}
	
	@OnAlias("getdata")
	public void getdata(TeamchatAPI api) throws IOException {
		
		String keyword = api.context().currentReply().getField("keyword");
		String Location = api.context().currentReply().getField("Location");
		String temp = "";
		String temp1 = "";
		pn =1;
		String resp;
		int l,l1;

		keyword = keyword.trim();
		Location = Location.trim();
		l1 = Location.length();
		l = keyword.length();
		for (int i = 0; i < l; i++) {
			if (keyword.charAt(i) == ' ') {
				temp = temp + '+';
			} else
				temp = temp + keyword.charAt(i);
		}
		for (int i = 0; i < l1; i++) {
			if (Location.charAt(i) == ' ') {
				temp1 = temp1 + '+';
			} else
				temp1 = temp1 + Location.charAt(i);
		}
		
		glassdoorintegrator ob1 = new glassdoorintegrator();
		//getting images from the giphy server.
		resp = ob1.getcompanies(temp, temp1,pn);
		
		//System.out.println(resp);
		
		

		if (resp.equals("Error")) {
			PrimaryChatlet prime = new PrimaryChatlet();
			api.perform(api
					.context()
					.currentRoom()
					.post(prime.setQuestionHtml("<br /><b>Error :</b>"
							+ "<br /><b>Status Code: 403</b>"
							+ "<br /><b>StatusDesc: Forbidden</b>"
							+ "<br /><b>Something went wrong!</b>")));

		
		}
		else
		{
			// parsing a json like this
			
//******************************************************************************************************************************************			
//			{
//			    "success": true,
//			    "status": "OK",
//			    "jsessionid": "CDA86E81A7C6F534B5E9F69A550902DE",
//			    "response": {
//			        "attributionURL": "http://www.glassdoor.com/Reviews/new-delhi-graphic-designer-reviews-SRCH_IL.0,9_IM1083_KO10,26.htm",
//			        "currentPageNumber": 1,
//			        "totalNumberOfPages": 55,
//			        "totalRecordCount": 55,
//			        "locationString": "delhi location",
//			        "locationLashed": true,
//			        "lashedLocation": {
//			            "id": 1083,
//			            "shortName": "New Delhi",
//			            "longName": "New Delhi, India",
//			            "locationType": "M"
//			        },
//			        "employers": [
//			            {
//			                "id": 509893,
//			                "name": "Jabong.com",
//			                "website": "www.jabong.com",
//			                "isEEP": false,
//			                "exactMatch": false,
//			                "industry": "Computer Hardware & Software",
//			                "numberOfRatings": 46,
//			                "squareLogo": "https://media.glassdoor.com/sqll/509893/jabong-com-squarelogo.png",
//			                "overallRating": 3.5,
//			                "ratingDescription": "Satisfied",
//			                "cultureAndValuesRating": "0.0",
//			                "seniorLeadershipRating": "0.0",
//			                "compensationAndBenefitsRating": "0.0",
//			                "careerOpportunitiesRating": "0.0",
//			                "workLifeBalanceRating": "0.0",
//			                "recommendToFriendRating": "0.0",
//			                "featuredReview": {
//			                    "id": 4167058,
//			                    "currentJob": true,
//			                    "reviewDateTime": "2014-05-04 05:18:36.38",
//			                    "jobTitle": "Graphic Designer",
//			                    "location": "Gurgaon, Haryana (India)",
//			                    "jobTitleFromDb": "Graphic Designer",
//			                    "headline": "Great Working Environment",
//			                    "pros": "Good startup with strong leadership. Excellent working environment with good learning experience. Senior management is expert and helpful. I have been working at Jabong.com full-time for more than a year.",
//			                    "cons": "Policies and procedures are getting mature. HR Department needs to be improve on post appraisals.",
//			                    "overall": 5,
//			                    "overallNumeric": 5
//			                },
//			                "ceo": {
//			                    "name": "Praveen Sinha",
//			                    "title": "Managing Director",
//			                    "numberOfRatings": 3,
//			                    "pctApprove": 100,
//			                    "pctDisapprove": 0,
//			                    "image": {
//			                        "src": "https://media.glassdoor.com/people/sqll/509893/jabong-com-praveen-sinha.png",
//			                        "height": 200,
//			                        "width": 200
//			                    }
//			                }
//			            }
//			        ]
//			    }
//			}
//******************************************************************************************************************************************			
			
			Gson gson = new Gson();
			//main class for getters and setters : Glassdoormain
			Glassdoormain data = gson.fromJson(resp, Glassdoormain.class);
			Response r = data.getResponse();
			ttp=r.getTotalNumberOfPages();
			ttc=r.getTotalRecordCount();
			
			if(ttc>0)
			{
				for (Employer emdata : r.getEmployers()) {
					FeaturedReview fr = emdata.getFeaturedReview();
					Ceo c = emdata.getCeo();
					Image i = c.getImage();
					
					PrimaryChatlet prime1 = new PrimaryChatlet();
					prime1.setQuestionHtml("<h2><b>"+emdata.getName()
							+ "</b></h2>"
							+ "<img src=\""+emdata.getSquareLogo()+"\" height=\"120\" width=\"120\" style=\"padding-left: 1cm\";/>"
							+ "<br />"
							+ "<h3><b>Company Details:</b></h3>"
							+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Industry - </b></a1><a2 style=\"color:#359FD8\";>"
							+ emdata.getIndustry()
							+ "</a2></li><li><a3 style=\"color:black\";><b>Website - </b></a3><a4 style=\"color:#359FD8\";>"
							+ emdata.getWebsite()
							+ "</a4></li><li><a7 style=\"color:black\";><b>Number of Ratings - </b></a7><a8 style=\"color:#359FD8\";>"
							+ emdata.getNumberOfRatings()
							+ "</a8></li><li><a9 style=\"color:black\";><b>Overall Rating - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getOverallRating() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Rating Description - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getRatingDescription() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Culture and Values Rating - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getCultureAndValuesRating() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Senior Leadership Rating - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getSeniorLeadershipRating() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Compensation and Benefits Rating - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getCompensationAndBenefitsRating() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Career Opportunities Rating - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getCareerOpportunitiesRating() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Worklife Balance Rating - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getWorkLifeBalanceRating() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Recommend To Friend Rating - </b></a9><a10 style=\"color:#359FD8\";>"
							+ emdata.getRecommendToFriendRating() + "</a10></li>"
							+ "</ul>"
							+ "<br />"
							+ "<h3><b>Review Details:</b></h3>"
							+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Job Title - </b></a1><a2 style=\"color:#359FD8\";>"
							+ fr.getJobTitle()
							+ "</a2></li>"
							+"<li><a9 style=\"color:black\";><b>Location - </b></a9><a10 style=\"color:#359FD8\";>"
							+ fr.getLocation() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Review DateTime - </b></a9><a10 style=\"color:#359FD8\";>"
							+ fr.getReviewDateTime() + "</a10></li></ul>"
							+ "<center><h4><b>Review</b></h4></center>"
							+ "<center><h4><a1 style=\"color:#484848\";>\""+fr.getHeadline()+"\"</a1></h4></center>"
							+"<ul type=\"square\"; style=\"color:#359FD8\";><li><a9 style=\"color:black\";><b>Pros - </b></a9><a10 style=\"color:#359FD8\";>"
							+ fr.getPros() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Cons - </b></a9><a10 style=\"color:#359FD8\";>"
							+ fr.getCons() + "</a10></li>"
							+ "</ul>"
							+ "<h3><b>CEO Details:</b></h3>"
							+ "<img src=\""+i.getSrc()+"\" height=\"120\" width=\"120\" style=\"padding-left: 1cm\";/>"
							+ "<br />"
							+ "<br />"
							+"<ul type=\"square\"; style=\"color:#359FD8\";><li><a9 style=\"color:black\";><b>Name - </b></a9><a10 style=\"color:#359FD8\";>"
							+ c.getName() + "</a10></li>"
							+"<li><a9 style=\"color:black\";><b>Title - </b></a9><a10 style=\"color:#359FD8\";>"
							+ c.getTitle() + "</a10></li></ul>"
							);
					
					api.perform(api.context().currentRoom().post(prime1));
				}
				
			}
			
			
			
			
		}
	}

		
	
}
