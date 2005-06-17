/*
 * $Id: DataReader.java,v 1.10 2005/06/17 11:25:48 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/17 11:25:48 $
 * @author $Author: bass $
 * @module util
 */
public abstract class DataReader extends BellcoreStructure {
	public abstract BellcoreStructure getData(byte[] b);

	public BellcoreStructure getData(File f) {
		return getData(readFromFile(f));
	}

	protected byte[] readFromFile(File f) {
		byte[] data = new byte[(int) f.length()];
		try {
			FileInputStream fis = new FileInputStream(f);
			fis.read(data);
		} catch (IOException ex) {
			System.err.println("Exception -- " + ex.getMessage());
			ex.printStackTrace();
		}
		return data;
	}
}
