package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

public class ResourceUtil
{
	public ResourceUtil()
	{
	}

	public static int parseNumber(String str) {
		StringBuffer s = new StringBuffer(str);
		boolean key = true;
		while (key) {
			key = false;
			for (int i = 0; i < s.length(); i++)
				if (!Character.isDigit(s.charAt(i))) {
					key = true;
					s = s.deleteCharAt(i);
					break;
				}
		}
		int n = -1;
		try {
			n = Integer.parseInt(s.toString());
		}
		catch (NumberFormatException ex) {
		}
		return n;
	}

	public static List parseStrings(String s)
	{
		List ids = new LinkedList();
		int i = 0;
		while(i < s.length())
		{
			int space = s.indexOf(" ", i);
			String s1 = s.substring(i, space);
			if(s1.length() != 0)
				ids.add(s1);
			i = space + 1;
		}
		return ids;
	}

}
