/*
 * $Id: DataReader.java,v 1.7 2005/03/16 16:29:26 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.7 $, $Date: 2005/03/16 16:29:26 $
 * @author $Author: arseniy $
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
		}
		catch (IOException ex) {
			System.err.println("Exception -- " + ex.getMessage());
			ex.printStackTrace();
		}
		return data;
	}
}
