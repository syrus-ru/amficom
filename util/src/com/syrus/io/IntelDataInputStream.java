/*
 * $Id: IntelDataInputStream.java,v 1.6 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.*;

/**
 * @version $Revision: 1.6 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class IntelDataInputStream extends DataInputStream
{
	public IntelDataInputStream (InputStream is)
	{
		super (is);
	}

	public final short readIShort() throws IOException
	{
		byte b[] = new byte[2];
		read(b);
		return (short)((b[1] << 8) | (b[0] & 0xff));
	}

	public final int readIUnsignedShort() throws IOException
	{
		byte b[] = new byte[2];
		read(b);
		return (b[1] & 0xff) << 8 | b[0] & 0xff;
	}

	public final int readIInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return (((b[3] & 0xff) << 24) | ((b[2] & 0xff) << 16) |
			((b[1] & 0xff) << 8) | (b[0] & 0xff));
	}

	public final long readILong() throws IOException
	{
		byte b[] = new byte[8];
		read(b);
		long returnValue = 0;
		for (int i = 0; i < b.length; i++)
			returnValue |= ((long) (b[i] & 0xff)) << (8 * i);
		return returnValue;
	}
	
	public final long readIUnsignedInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return ((long)b[3] & 0xff) << 24 | ((long)b[2] & 0xff) << 16 |
			((long)b[1] & 0xff) << 8 | (long)b[0] & 0xff;
	}

	public final long readUnsignedInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return ((long)b[0] & 0xff) << 24 | ((long)b[1] & 0xff) << 16 |
			((long)b[2] & 0xff) << 8 | (long)b[3] & 0xff;
	}

	public final String readIString() throws IOException
	{
		byte b[] = new byte[1];
		String s = ""; //$NON-NLS-1$

		while (true)
		{
			read(b);
			if (b[0] == 0x00)
				break;
			s += new String(b);
		}
		return  s;
	}

	public final String readASCIIString() throws IOException
	{
		int res;
		int nRead = 0;
		byte b[] = new byte[1];
		String s = ""; //$NON-NLS-1$

		while (true)
		{
			res = read(b);
			if (res == -1)
				break;
			nRead++;
			if (b[0] == '\n')
				break;
			else if (b[0] != '\r')
				s += new String(b);
		}
		if (nRead == 0)
			return null;
		return s;
	}

	public final char readIChar() throws IOException
	{
		byte b[] = new byte[1];
		read(b);
		return new String(b).charAt(0);
	}
}
