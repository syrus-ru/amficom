/*
 * $Id: FileToDirectory.java,v 1.4 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;
import java.util.*;

/**
 * @version $Revision: 1.4 $, $Date: 2005/03/04 08:05:49 $
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
		"@characteristics", //$NON-NLS-1$
		"@threads", //$NON-NLS-1$
		"@test_type_ids", //$NON-NLS-1$
		"@devices", //$NON-NLS-1$
		"@links", //$NON-NLS-1$
		"@protoelements", //$NON-NLS-1$
		"@attributes", //$NON-NLS-1$
		"@cabletypes", //$NON-NLS-1$
		"@pathtypes", //$NON-NLS-1$
		"@arguments", //$NON-NLS-1$
		"@parameters", //$NON-NLS-1$
		"@criterias", //$NON-NLS-1$
		"@thresholds", //$NON-NLS-1$
		"@eventtypes", //$NON-NLS-1$
		"@values", //$NON-NLS-1$
		"@analysis_types", //$NON-NLS-1$
		"@evaluation_types", //$NON-NLS-1$
		"@pathelements", //$NON-NLS-1$
		"@paths", //$NON-NLS-1$
		"@elements", //$NON-NLS-1$
		"@mapprotos", //$NON-NLS-1$
		"@groups", //$NON-NLS-1$
		"@cablelinks" //$NON-NLS-1$
	};

	public FileToDirectory (File file) throws IOException
	{
		this.fis = new FileInputStream(file);
		this.isr = new IntelStreamReader(this.fis, "UTF-16"); //$NON-NLS-1$
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
					while (!s[0].startsWith("@end")) //$NON-NLS-1$
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
				if (s[0].startsWith("@")) //$NON-NLS-1$
					h.put(s[0], s[1]);
				else
				if (s[0].equals("")) //$NON-NLS-1$
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
		if (!this.isr.readASCIIString().equals("AMFICOM component description file")) //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		String[] s = analyseString (this.isr.readASCIIString());
		if (s[0].equals("@type")) //$NON-NLS-1$
			return s[1];
		return ""; //$NON-NLS-1$
	}

	protected String[] analyseString (String s)
	{
		int n = s.indexOf(" "); //$NON-NLS-1$
		if (n == -1)
			return new String[] {s, ""}; //$NON-NLS-1$
		String s1 = s.substring(0, n);
		String s2 = s.substring(n + 1, s.length());
		return new String[] {s1, s2};
	}
}
