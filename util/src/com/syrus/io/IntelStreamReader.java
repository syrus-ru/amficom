package com.syrus.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class IntelStreamReader extends InputStreamReader
{
	public IntelStreamReader(InputStream is)
	{
		super(is);
	}

	public IntelStreamReader(InputStream is, String enc) throws UnsupportedEncodingException
	{
		super(is, enc);
	}


public String readASCIIString() throws IOException
	{
		int res;
		int nRead = 0;
		char ch[] = new char[1];

		String s = "";

		while (true)
		{
			res = read(ch);
			if (res == -1)
				break;
			nRead++;
			if (ch[0] == '\n')
				break;
			else if (ch[0] != '\r')
				s += new String(ch);
		}
		if (nRead == 0)
			return null;
		return s;
	}
}
