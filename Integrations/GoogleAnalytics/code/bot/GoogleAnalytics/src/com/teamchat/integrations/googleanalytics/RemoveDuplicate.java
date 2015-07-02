package com.teamchat.integrations.googleanalytics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveDuplicate {
	public static String[] remove(String[] array) {
		List<String> list = Arrays.asList(array);
		Set<String> set = new HashSet<String>(list);

		System.out.print("Remove duplicate result: ");

		//
		// Create an array to convert the Set back to array. The Set.toArray()
		// method copy the value in the set to the defined array.
		//
		String[] result = new String[set.size()];
		set.toArray(result);
		for (String s : result) {
			System.out.print(s + ", ");
		}
		return result;
	}
}
