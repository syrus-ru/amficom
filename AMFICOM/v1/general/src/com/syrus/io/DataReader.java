/*
 * $Id: DataReader.java,v 1.1 2004/10/20 07:11:44 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/20 07:11:44 $
 * @author $Author: bass $
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
