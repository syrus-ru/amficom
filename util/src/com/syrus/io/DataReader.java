/*
 * $Id: DataReader.java,v 1.8 2005/03/17 10:12:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.8 $, $Date: 2005/03/17 10:12:49 $
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
		}
		catch (IOException ex) {
			System.err.println("Exception -- " + ex.getMessage()); //$NON-NLS-1$
			ex.printStackTrace();
		}
		return data;
	}
}
