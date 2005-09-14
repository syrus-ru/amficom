/*
 * $Id: TraceDataReader.java,v 1.5 2005/09/14 18:28:26 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/14 18:28:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class TraceDataReader {
	static {
		System.loadLibrary("TraceData");
	}

	public native byte[] getBellcoreData(String fileName);

	public native byte[] getBellcoreData(byte[] t5data);
}
