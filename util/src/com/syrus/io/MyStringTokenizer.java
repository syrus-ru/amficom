package com.syrus.io;

import java.util.StringTokenizer;

public class MyStringTokenizer extends StringTokenizer
{

	public MyStringTokenizer(String str)
	{
		super(str);
	}

	public MyStringTokenizer(String str, String delim)
	{
		super(str, delim);
	}

	public MyStringTokenizer(String str, String delim, boolean returnTokens)
	{
		super(str, delim, returnTokens);
	}

	public String finalToken()
	{
		String s = "";
		if(hasMoreTokens())
			s = s + nextToken();
		while(hasMoreTokens())
			s = s + " " + nextToken();
		return s;
	}
}
