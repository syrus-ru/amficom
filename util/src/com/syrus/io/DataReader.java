/*
 * $Id: DataReader.java,v 1.5 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public abstract class DataReader extends BellcoreStructure
{
	public abstract BellcoreStructure getData(byte[] b);

	public BellcoreStructure getData(File f)
	{
		return getData (readFromFile(f));
	}

	protected byte[] readFromFile(File f)
	{
		byte[] data = new byte[(int)f.length()];
		try
		{
			FileInputStream fis = new FileInputStream(f);
			fis.read(data);
		}
		catch (IOException ex)
		{
			// empty
		}
		return data;
	}
}
