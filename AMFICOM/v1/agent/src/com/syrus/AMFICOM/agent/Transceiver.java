/*
 * $Id: Transceiver.java,v 1.2 2004/06/21 14:56:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/21 14:56:29 $
 * @author $Author: bass $
 * @module agent_v1
 */
public class Transceiver {
	private static String measurement_id;
	private static String[] par_names;
	private static byte[][] par_values;

	private Transceiver() {
	}

	static {
		System.loadLibrary("agenttransceiver");
	}

  public static native int call(String fileName);
  public static native boolean read1(int fileHandle, String fileName);
  public static native boolean push1(int fileHandle, String fileName,
                                      String measurement_id,
                                      String measurement_type_id,
                                      String local_address,
                                      String[] par_names,
                                      byte[][] par_values);

  public static String getMeasurementId() {
    return measurement_id;
  }

  public static String[] getParameterNames() {
    return par_names;
  }

	public static byte[][] getParameterValues() {
		return par_values;
	}
}
