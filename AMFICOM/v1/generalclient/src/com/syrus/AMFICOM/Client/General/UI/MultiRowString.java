package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.io.SimpleStringTokenizer;

public class MultiRowString 
{
	int rows;
	String[] strings;

	public MultiRowString(String s)
	{
		parseString(s);
	}

	public void parseString(String s)
	{
		SimpleStringTokenizer tokenizer = new SimpleStringTokenizer(s, "\n");
		rows = tokenizer.countTokens();
		strings = new String[rows];
		for(int i = 0; i < rows; i++)
			strings[i] = tokenizer.nextToken();
	}

	public int getRowCount()
	{
		return rows;
	}
	
	public String get(int i)
	{
		return strings[i];
	}
}
