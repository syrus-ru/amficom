/*
 * $Id: FileToDirectory.java,v 1.5 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;
import java.util.*;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @module util
 */
public class FileToDirectory
{
	FileInputStream fis;
	IntelStreamReader isr;

	String type;
	Vector elements;
	private int counter = 0;
	Hashtable currentHash;

	static String[] hashArguments = new String[]
	{
		"@characteristics",
		"@threads",
		"@test_type_ids",
		"@devices",
		"@links",
		"@protoelements",
		"@attributes",
		"@cabletypes",
		"@pathtypes",
		"@arguments",
		"@parameters",
		"@criterias",
		"@thresholds",
		"@eventtypes",
		"@values",
		"@analysis_types",
		"@evaluation_types",
		"@pathelements",
		"@paths",
		"@elements",
		"@mapprotos",
		"@groups",
		"@cablelinks"
	};

	public FileToDirectory (File file) throws IOException
	{
		this.fis = new FileInputStream(file);
		this.isr = new IntelStreamReader(this.fis, "UTF-16");
		this.type = getType();
		this.elements = readFromFile();
	}

	public Object read(String key)
	{
		return this.currentHash.get(key);
	}

	public boolean hasMoreElements()
	{
		return this.counter < this.elements.size();
	}

	public void nextElement()
	{
		this.currentHash = (Hashtable)this.elements.get(this.counter);
		this.counter++;
	}

	public String getTyp()
	{
		return this.type;
	}

	protected Vector readFromFile() throws IOException
	{
		boolean continueAnalyse = true;
		Vector vec = new Vector();
		Hashtable h = new Hashtable();
		while (this.isr.ready())
		{
			continueAnalyse = true;
			String[] s = analyseString(this.isr.readASCIIString());
			for(int i = 0; i < hashArguments.length; i++)
			{
				if (s[0].startsWith(hashArguments[i]))
				{
					s = analyseString(this.isr.readASCIIString());
					Hashtable ch = new Hashtable();
					while (!s[0].startsWith("@end"))
					{
						ch.put(s[0], s[1]);
						s = analyseString(this.isr.readASCIIString());
					}
					h.put(hashArguments[i], ch);
					continueAnalyse = false;
					break;
				}
			}
			if(continueAnalyse)
			{
				if (s[0].startsWith("@"))
					h.put(s[0], s[1]);
				else
				if (s[0].equals(""))
				{
					if (!h.isEmpty())
					{
						vec.add(h);
						h = new Hashtable();
					}
				}
			}
		}
		return vec;
	}

	protected String getType() throws IOException
	{
		if (!this.isr.readASCIIString().equals("AMFICOM component description file"))
			return "";
		String[] s = analyseString (this.isr.readASCIIString());
		if (s[0].equals("@type"))
			return s[1];
		return "";
	}

	protected String[] analyseString (String s)
	{
		int n = s.indexOf(" ");
		if (n == -1)
			return new String[] {s, ""};
		String s1 = s.substring(0, n);
		String s2 = s.substring(n + 1, s.length());
		return new String[] {s1, s2};
	}
}
