/*
 * $Id: SimpleStringTokenizer.java,v 1.2 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

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
		String s = ""; //$NON-NLS-1$
		if(hasMoreTokens())
			s = s + nextToken();
		while(hasMoreTokens())
			s = s + " " + nextToken(); //$NON-NLS-1$
		return s;
	}
}
