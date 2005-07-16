/*
 * $Id: DataReader.java,v 1.11 2005/07/16 21:40:20 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @version $Revision: 1.11 $, $Date: 2005/07/16 21:40:20 $
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
		} catch (IOException ex) {
			System.err.println("Exception -- " + ex.getMessage());
			ex.printStackTrace();
		}
		return data;
	}
}
