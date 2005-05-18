/*
 * $Id: TraceDataReader.java,v 1.4 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

public class TraceDataReader {
	static {
		System.loadLibrary("TraceData");
	}

	public native byte[] getBellcoreData(String fileName);

	public native byte[] getBellcoreData(byte[] t5data);
}
