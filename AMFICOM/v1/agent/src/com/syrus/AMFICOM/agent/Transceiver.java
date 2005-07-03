/*
 * $Id: Transceiver.java,v 1.4 2004/07/21 08:18:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/21 08:18:10 $
 * @author $Author: arseniy $
 * @module agent_v1
 */

public class Transceiver {
	private static String measurementId;
	private static String[] parNames;
	private static byte[][] parValues;

	private Transceiver() {
	}

	static {
		System.loadLibrary("agenttransceiver");
	}

  public static native int call(String fileName);
  public static native boolean read1(int fileHandle, String fileName);
  public static native boolean push1(int fileHandle, String fileName,
                                      String measurementId,
                                      String measurementTypeId,
                                      String localAddress,
                                      String[] parNames,
                                      byte[][] parValues);

  public static String getMeasurementId() {
    return measurementId;
  }

  public static String[] getParameterNames() {
    return parNames;
  }

	public static byte[][] getParameterValues() {
		return parValues;
	}
}
