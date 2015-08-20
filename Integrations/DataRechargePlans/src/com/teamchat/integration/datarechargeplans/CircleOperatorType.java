package com.teamchat.integration.datarechargeplans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CircleOperatorType
{
	private static HashMap<String, String> circle = new HashMap<String, String>();
	private static HashMap<String, String> operator = new HashMap<String, String>();
	private static String[] rechargeType = {"3G", "2G", "Other", "Topup", "Data/2G"};

	/**
	 * DataStore
	 */
	private static HashMap<String, String> loadCircle()
	{
		if (circle.size() < 1)
		{
			circle.put("Assam", "as");
			circle.put("Bihar", "br");
			circle.put("Chennai", "ch");
			circle.put("Delhi", "dl");
			circle.put("Himachal Pradesh", "hp");
			circle.put("Karnataka", "ka");
			circle.put("Kolkata", "kk");
			circle.put("Maharashtra", "mh");
			circle.put("Mumbai", "mb");
			circle.put("North East", "ne");
			circle.put("Orissa", "or");
			circle.put("Punjab", "pb");
			circle.put("Rajasthan", "rj");
			circle.put("Tamil Nadu", "tn");
			circle.put("Uttar Pradesh East", "upe");
			circle.put("Uttar Pradesh West", "upw");
			circle.put("West Bengal", "wb");
			circle.put("Jammu", "jk");
			circle.put("Andhra Pradesh", "ap");
			circle.put("Gujarat", "gj");
			circle.put("Haryana", "hr");
			circle.put("Kerala", "kl");
			circle.put("Madhya Pradesh", "mp");
		}
		return circle;
	}
	
	private static HashMap<String, String> loadOperator()
	{
		if (operator.size() < 1)
		{
			operator.put("Aircel", "aircel");
			operator.put("Airtel", "airtel");
			operator.put("BSNL", "bsnl");
			operator.put("Tata Docomo GSM", "tatadocomogsm");
			operator.put("Idea", "idea");
			operator.put("Loop Mobile", "loopmobile");
			operator.put("MTNL Delhi", "mtnldelhi");
			operator.put("Reliance CDMA", "reliancecdma");
			operator.put("Tata Indicom", "tataindicom");
			operator.put("Uninor", "uninor");
			operator.put("Vodafone", "Vodafone");
			operator.put("MTS", "mts");
			operator.put("Videocon Mobile", "videoconmobile");
			operator.put("Virgin GSM", "virgingsm");
			operator.put("Tata Docomo CDMA", "tatadocomocdma");
			operator.put("MTNL Mumbai", "mtnlmumbai");
		}
		return operator;
	}

	public static String getCircleCode(String circleName)
	{
		circle = loadCircle();
		String res = circle.get(circleName);
		return res;
	}
	
	public static String[] getCircles()
	{
		String[] circles = null;
		if (circle.size() < 1)
		{
			circle = loadCircle();
		}
		
		Set<String> keys = circle.keySet();
		circles = new String[keys.size()];
		
		int i=0;
		for (String lang: keys)
		{
			circles[i] = lang;
		}
		return circles;
	}
	
	public static ArrayList<String> getCircleList()
	{
		if (circle.size() < 1)
		{
			circle = loadCircle();
		}
		
		ArrayList<String> circles = new ArrayList<String>(circle.keySet());
		return circles;
	}
	
	public static String getOperatorCode(String operatorName)
	{
		operator = loadOperator();
		String res = operator.get(operatorName);
		return res;
	}
	
	public static String[] getOperators()
	{
		String[] operators = null;
		if (operator.size() < 1)
		{
			operator = loadOperator();
		}
		
		Set<String> keys = operator.keySet();
		operators = new String[keys.size()];
		
		int i=0;
		for (String lang: keys)
		{
			operators[i] = lang;
		}
		return operators;
	}
	
	public static ArrayList<String> getOperatorsList()
	{
		if (operator.size() < 1)
		{
			operator = loadOperator();
		}
		
		ArrayList<String> operators = new ArrayList<String>(operator.keySet());
		return operators;
	}
	
	public static String[] getRechargeType()
	{
		return rechargeType;
	}
}