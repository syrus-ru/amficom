/*
 * $Id: SimpleStringTokenizer.java,v 1.4 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.util.StringTokenizer;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
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
