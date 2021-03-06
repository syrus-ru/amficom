/*-
 * $Id: IntelStreamReader.java,v 1.5 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/08 13:49:06 $
 * @module util
 */
public final class IntelStreamReader extends InputStreamReader {
	public IntelStreamReader(InputStream is) {
		super(is);
	}

	public IntelStreamReader(InputStream is, String enc) throws UnsupportedEncodingException {
		super(is, enc);
	}


	public String readASCIIString() throws IOException {
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
