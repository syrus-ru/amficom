/*
 * $Id: TraceDataReader.java,v 1.3 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

public class TraceDataReader {
	static {
		System.loadLibrary("TraceData"); //$NON-NLS-1$
	}

	public native byte[] getBellcoreData(String fileName);

	public native byte[] getBellcoreData(byte[] t5data);
}
