//package com.integration.teamchat.main.onenote;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.List;
//
//import com.google.gson.Gson;
//import com.integration.teamchat.onenote.page.Pages;
//import com.integration.teamchat.onenote.page.Value;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//
//public class Getpage
//{
//
//	public void requestpage() throws Exception
//	{
//		OkHttpClient client = new OkHttpClient();
//
//		Request request = new Request.Builder()
//				.url("https://www.onenote.com/api/v1.0/me/notes/pages")
//				.get()
//				.addHeader(
//						"authorization",
//						"Bearer EwBYAq1DBAAUGCCXc8wU/zFu9QnLdZXy+YnElFkAAale0elSqwRjUTIZnCr5EZp2YRgbbCuFoSWN8DHyobv7FNThO7guCM4g7hw3WbknF8NbV4gI2brCPDRAxvUE7GtILvXINob2M5ApNENWNFZNFPtkoUM0tN/Vp3TvQvcNfUsqHke+A053C7cBZUDoq8NRVdBLrt9WKbSCzTCrxXJ9EbNRoORW7a0H/3qyJtSWdVMr/MW/QY45tAghW1P8k7c5ZWVnfphW7TM3mnVE0OkKFtJBhiIualJU5eWFbAFxcvdpRG+lTTknuhQYvrksB1u+DyA3sjDBg2VyVyLKI5eqfLetdIjZCHRon0i4xC3FL+QlUhAlTheIwYLrHM4+TqsDZgAACJtpbplpUdSKKAH4qNtYmK+OO7oO6PzFnRB056IGv9CECN6l/ZxPdOSnOZAH9DuajZJVT1ZLla39e7aqzTu6X1lq+0cABHJ6p+XQ3rQEAQ2ugBvWdw9Qw5ge3dO1RqPuXlIw3G2sPTH7VUlbZQbiyAnRft0Kt5JcPvqxhYGjBBPhBzMj9zC4mGkOPhLK7r5WFribX2gJ57Cod6h4w558F3e5TtcqTkq7Maa0qgngBGtgL4WoWsC6x5catS3gFQcx5zhFNGMRdx0zolunlIT2Lje0dZQEA2vraCltC1zHEyFe8fAb+HkF1pdrZGixoSiLmcopFpBRs+APTay4luyhVpUS/DPwVrRkVXNz3Ny/RJKBrX4XrAW923gSn7B5u0UJ9Ik6ptmiQhUt8tUEeRC7GwhEelAB")
//				.build();
//
//		Response response = client.newCall(request).execute();
//		String body =response.body().string();
//		 System.out.println(body);
//		System.out.println("It is here00");
//		Gson gson = new Gson();
//	
//		Pages pg = gson.fromJson(body, Pages.class);
//		System.out.println("It is here11");
//		// List<values> v = pg.getValue();
//		List<Value> v=pg.getValue();
//		
//		for (Value a : v)
//		{
//			System.out.println("Its here 22");
//			System.out.println(a.getTitle());
//
//		}
//
//	}
//
//}
