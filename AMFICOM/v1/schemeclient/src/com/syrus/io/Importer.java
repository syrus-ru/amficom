/*-
 * $Id: Importer.java,v 1.1 2006/01/11 12:42:57 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.IOException;

public abstract class Importer {
	protected static String getType (IntelStreamReader isr) throws IOException {
		if (!isr.readASCIIString().equals("AMFICOM component description file")) {
			return "";
		}
		String[] s = analyseString (isr.readASCIIString());
		if (s[0].equals("@type")) {
			return s[1];
		}
		return "";
	}
	
	protected static String[] analyseString (String s) {
		int n = s.indexOf(" ");
		if (n == -1) {
			return new String[] {s, ""};
		}
		String s1 = s.substring(0, n);
		String s2 = s.substring(n + 1, s.length());
		return new String[] {s1, s2};
	}
}
