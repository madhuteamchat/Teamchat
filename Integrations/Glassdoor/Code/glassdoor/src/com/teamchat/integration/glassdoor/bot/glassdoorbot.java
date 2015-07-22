package com.teamchat.integration.glassdoor.bot;
/*
 * *@author : Anuj Arora
 * 
 */
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

	int pn,ttp,ttc,flag;
    String key;
    String loc;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h4><b>Hi, I'm Glassdoor Bot.</b></h4>"
								+ "<img src=\"http://recruitingwebinars.com/wp-content/uploads/sites/3/2014/06/GD_forEmployers_Logo_600x200Pixels11.png\" height=\"90\" width=\"230\" />"
								+ "<br />"
								+ "<br /><b>You can use me to search any company and find its rating and reviews, using following keywords:</b>"
								+ "<br />"
								+ "<ul type=\"square\"; style=\"color:#359FD8\"; ><li><a1 style=\"color:black\";><b>search - </b></a1><a2 style=\"color:#359FD8\"; align=\"justify\";>"
								+ "Type this command to search any company"
								+ "</a2></li><li><a3 style=\"color:black\";><b>next - </b></a3><a4 style=\"color:#359FD8\"; align=\"justify\";>"
								+ "Type this command to check the next result page for your searched keyword"
								+ "</a4></li></ul>")));

		// list of keywords
		// 1.)search
		// 2.)next

	}
	//Use this keyword to search any company
	@OnKeyword("search")
	public void login(TeamchatAPI api) {
flag=0;
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Query phrase to search for:").name("keyword"));
		f.addField(api.objects().input().label("Location:").name("Location"));

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml(
				"<h2><b>Get Hired. Love Your Job.</b></h2>"
				+ "<a1 style=\"color:#359FD8\";><b>powered by </b></a1><img src='http://www.glassdoor.com/static/img/api/glassdoor_logo_80.png' style=\"padding-bottom: 0.35cm\"; /></a>"
				+ "<br />"
				+ "<h5><b>Enter any occupation or job title with the location you want to search for in the reply option.</b></h5>"
				+ "<br /><b>Note: </b><a1  style=\"color:#359FD8\";>One field is required.</a1>")
				.setReplyScreen(f).setReplyLabel("Reply").alias("getdata");
		api.perform(api.context().currentRoom().post(prime));
	}
	//getting the data from glassdoor server
	@OnAlias("getdata")
	public void getdata(TeamchatAPI api) throws IOException {
		
		String keyword = api.context().currentReply().getField("keyword");
		String Location = api.context().currentReply().getField("Location");
		//pn : pagenumber (global variable)
		pn =1;
		String resp;
		int l,l1;
		
        //concatenating strings with a "+" sign
		String temp="";
		String temp1="";
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
		key=temp;
		loc=temp1;
		glassdoorintegrator ob1 = new glassdoorintegrator();
		//getting companies from the glassdoor server.
		resp = ob1.getcompanies(temp, temp1,pn);
		
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
			//If there are some results to show.
			if(ttc>0)
			{
				//traversing through json object.
				for (Employer emdata : r.getEmployers()) {
					
					FeaturedReview fr;
					Ceo c;
					Image i;
					//If review details is there in the json.
					try {
						fr = emdata.getFeaturedReview();
						c = emdata.getCeo();
						i = c.getImage();
						PrimaryChatlet prime1 = new PrimaryChatlet();
						PrimaryChatlet prime2 = new PrimaryChatlet();
						prime1.setQuestionHtml("<h2><b>"+emdata.getName()
								+ "</b></h2>"
								+ "<img src=\""+emdata.getSquareLogo()+"\" height=\"120\" width=\"120\" />"
								+ "<br />"
								+ "<h3><b>Company Details:</b></h3>"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Industry - </b></a1><a2 style=\"color:#359FD8\";>"
								+ emdata.getIndustry()
								+ "</a2></li><li><a3 style=\"color:black\";><b>Website - </b></a3><a href=\"https://"
								+ emdata.getWebsite()
								+ "\" title=\"Go to this link\"; target=\"_blank\"; >"
								+ emdata.getWebsite()
								+ "</a></li><li><a7 style=\"color:black\";><b>Number of Ratings - </b></a7><a8 style=\"color:#359FD8\";>"
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
								+ "</ul>");
						
						api.perform(api.context().currentRoom().post(prime1));
						
						prime2.setQuestionHtml("<h3><b>Review Details:</b></h3>"
						+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Job Title - </b></a1><a2 style=\"color:#359FD8\";>"
						+ fr.getJobTitle()
						+ "</a2></li>"
						+"<li><a9 style=\"color:black\";><b>Location - </b></a9><a10 style=\"color:#359FD8\";>"
						+ fr.getLocation() + "</a10></li>"
						+"<li><a9 style=\"color:black\";><b>Review DateTime - </b></a9><a10 style=\"color:#359FD8\";>"
						+ fr.getReviewDateTime() + "</a10></li></ul>"
						+ "<center><h4><b>Review</b></h4></center>"
						+ "<center><h4><a1 style=\"color:#484848\";>\""+fr.getHeadline()+"\"</a1></h4></center>"
						+"<ul type=\"square\"; style=\"color:#359FD8\";><li><a9 style=\"color:black\";><b>Pros - </b></a9><a10 style=\"color:#359FD8\"; align=\"justify\";>"
						+ fr.getPros() + "</a10></li>"
						+"<li><a9 style=\"color:black\";><b>Cons - </b></a9><a10 style=\"color:#359FD8\"; align=\"justify\";>"
						+ fr.getCons() + "</a10></li>"
						+ "</ul>");
						api.perform(api.context().currentRoom().post(prime2));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//If review details and ceo details are not in the json.
						PrimaryChatlet prime1 = new PrimaryChatlet();
						prime1.setQuestionHtml("<h2><b>"+emdata.getName()
								+ "</b></h2>"
								+ "<img src=\""+emdata.getSquareLogo()+"\" height=\"120\" width=\"120\" />"
								+ "<br />"
								+ "<h3><b>Company Details:</b></h3>"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Industry - </b></a1><a2 style=\"color:#359FD8\";>"
								+ emdata.getIndustry()
								+ "</a2></li><li><a3 style=\"color:black\";><b>Website - </b></a3><a href=\"https://"
								+ emdata.getWebsite()
								+ "\" title=\"Go to this link\"; target=\"_blank\"; >"
								+ emdata.getWebsite()
								+ "</a></li><li><a7 style=\"color:black\";><b>Number of Ratings - </b></a7><a8 style=\"color:#359FD8\";>"
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
								+ "</ul>");
						api.perform(api.context().currentRoom().post(prime1));
					}
			
				}
			flag=1;	//To indicate that user has searched for a query.
			}
			//If no records found.
			else
			{
				PrimaryChatlet prime2 = new PrimaryChatlet();
				prime2.setQuestionHtml("<h4><b>Sorry, No records to display!!!</b></h4>");
				api.perform(api.context().currentRoom().post(prime2));
			}
	
		}
	}
	//Use this keyword for going to the next result.
	@OnKeyword("next")
	public void next(TeamchatAPI api) throws IOException {
		
		//user had searched for a query and current page number should be less than or equal to the total page count.
		if(pn<=ttp && flag==1)
		{
			//increasing the page number by 1. 
			pn++;
		glassdoorintegrator ob1 = new glassdoorintegrator();
		//getting companies from the glassdoor server.
		String resp = ob1.getcompanies(key, loc,pn);
	// Error if no json returned.
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
		//If returned
		else
		{

			Gson gson = new Gson();
			//main class for getters and setters : Glassdoormain
			Glassdoormain data = gson.fromJson(resp, Glassdoormain.class);
			Response r = data.getResponse();
				//For traversing through the json object.
					for (Employer emdata : r.getEmployers()) {
					FeaturedReview fr;
					Ceo c;
					Image i;
					try {
						//If review details and ceo details are in the json.
						fr = emdata.getFeaturedReview();
						c = emdata.getCeo();
						i = c.getImage();
						
						PrimaryChatlet prime1 = new PrimaryChatlet();
						PrimaryChatlet prime2 = new PrimaryChatlet();

						prime1.setQuestionHtml("<h2><b>"+emdata.getName()
								+ "</b></h2>"
								+ "<img src=\""+emdata.getSquareLogo()+"\" height=\"120\" width=\"120\" />"
								+ "<br />"
								+ "<h3><b>Company Details:</b></h3>"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Industry - </b></a1><a2 style=\"color:#359FD8\";>"
								+ emdata.getIndustry()
								+ "</a2></li><li><a3 style=\"color:black\";><b>Website - </b></a3><a href=\"https://"
								+ emdata.getWebsite()
								+ "\" title=\"Go to this link\"; target=\"_blank\"; >"
								+ emdata.getWebsite()
								+ "</a></li><li><a7 style=\"color:black\";><b>Number of Ratings - </b></a7><a8 style=\"color:#359FD8\";>"
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
								+ "</ul>");
						
						prime2.setQuestionHtml("<h3><b>Review Details:</b></h3>"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Job Title - </b></a1><a2 style=\"color:#359FD8\";>"
								+ fr.getJobTitle()
								+ "</a2></li>"
								+"<li><a9 style=\"color:black\";><b>Location - </b></a9><a10 style=\"color:#359FD8\";>"
								+ fr.getLocation() + "</a10></li>"
								+"<li><a9 style=\"color:black\";><b>Review DateTime - </b></a9><a10 style=\"color:#359FD8\";>"
								+ fr.getReviewDateTime() + "</a10></li></ul>"
								+ "<center><h4><b>Review</b></h4></center>"
								+ "<center><h4><a1 style=\"color:#484848\";>\""+fr.getHeadline()+"\"</a1></h4></center>"
								+"<ul type=\"square\"; style=\"color:#359FD8\";><li><a9 style=\"color:black\";><b>Pros - </b></a9><a10 style=\"color:#359FD8\"; align=\"justify\";>"
								+ fr.getPros() + "</a10></li>"
								+"<li><a9 style=\"color:black\";><b>Cons - </b></a9><a10 style=\"color:#359FD8\"; align=\"justify\";>"
								+ fr.getCons() + "</a10></li>"
								+ "</ul>");
								api.perform(api.context().currentRoom().post(prime2));
						
						api.perform(api.context().currentRoom().post(prime1));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//If review details and ceo details are not in the json.
						PrimaryChatlet prime1 = new PrimaryChatlet();
						prime1.setQuestionHtml("<h2><b>"+emdata.getName()
								+ "</b></h2>"
								+ "<img src=\""+emdata.getSquareLogo()+"\" height=\"120\" width=\"120\" />"
								+ "<br />"
								+ "<h3><b>Company Details:</b></h3>"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Industry - </b></a1><a2 style=\"color:#359FD8\";>"
								+ emdata.getIndustry()
								+ "</a2></li><li><a3 style=\"color:black\";><b>Website - </b></a3><a href=\"https://"
								+ emdata.getWebsite()
								+ "\" title=\"Go to this link\"; target=\"_blank\"; >"
								+ emdata.getWebsite()
								+ "</a></li><li><a7 style=\"color:black\";><b>Number of Ratings - </b></a7><a8 style=\"color:#359FD8\";>"
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
								+ "</ul>");
						
						api.perform(api.context().currentRoom().post(prime1));
						
					}
			}
			}
  }
	else{
		  //no records to display
			PrimaryChatlet prime2 = new PrimaryChatlet();
			prime2.setQuestionHtml("</h4><b>Sorry, No records to display!!!</b></h4>");
			api.perform(api.context().currentRoom().post(prime2));
		}

}
	
}
