package com.syrus.AMFICOM.Client.General.UI;

import java.util.*;
import com.syrus.io.*;

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
		Vector vec = new Vector();
		MyStringTokenizer tokenizer = new MyStringTokenizer(s, "\n");
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
