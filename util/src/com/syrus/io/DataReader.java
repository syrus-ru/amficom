/*
 * $Id: DataReader.java,v 1.4 2004/11/22 14:03:40 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.4 $, $Date: 2004/11/22 14:03:40 $
 * @author $Author: stas $
 * @module general_v1
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
			;
		}
		return data;
	}
}
