package com.syrus.AMFICOM.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
