package com.syrus.io;

import java.util.StringTokenizer;

public class SimpleStringTokenizer extends StringTokenizer
{

	public SimpleStringTokenizer(String str)
	{
		super(str);
	}

	public SimpleStringTokenizer(String str, String delim)
	{
		super(str, delim);
	}

	public SimpleStringTokenizer(String str, String delim, boolean returnTokens)
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
