/*
 * $Id: IntelRandomAccessFile.java,v 1.3 2004/12/08 13:39:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/08 13:39:55 $
 * @author $Author: bass $
 * @module util
 */
public class IntelRandomAccessFile extends RandomAccessFile
{
	public IntelRandomAccessFile(File file, String mode) throws IOException
	{
		super(file, mode);
	}

	public IntelRandomAccessFile(String fileName, String mode) throws IOException
	{
		super(fileName, mode);
	}

	public final byte readIByte() throws IOException
	{
		byte b[] = new byte[1];
		read(b);
		return b[0];
	}

	public final short readIUnsignedByte() throws IOException
	{
		byte b[] = new byte[1];
		read(b);
		return (short)(b[0]& 0xff);
	}

	public final short readIShort() throws IOException
	{
		byte b[] = new byte[2];
		read(b);
		return (short)((b[1] << 8) | (b[0] & 0xff));
	}

	public final void writeIShort(short v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
	}

	public final int readIUnsignedShort() throws IOException
	{
		byte b[] = new byte[2];
		read(b);
		return (b[1] & 0xff) << 8 | b[0] & 0xff;
	}

	public final void writeIUnsignedShort(int v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
	}

	public final int readIInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return (((b[3] & 0xff) << 24) | ((b[2] & 0xff) << 16) |
						((b[1] & 0xff) << 8) | (b[0] & 0xff));
	}

	public final void writeIInt(int v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
		write ((byte)(0xff & (v >> 16)));
		write ((byte)(0xff & (v >> 24)));
	}

	public final long readIUnsignedInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return (long)(b[3] & 0xff) << 24 | (long)(b[2] & 0xff) << 16 |
			(long)(b[1] & 0xff) << 8 | b[0] & 0xff;
	}

	public final long readUnsignedInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return (long)(b[0] & 0xff) << 24 | (long)(b[1] & 0xff) << 16 |
			(long)(b[2] & 0xff) << 8 | b[3] & 0xff;
	}

	public final long readILong() throws IOException
	{
		byte b[] = new byte[8];
		read(b);
		return ((long)b[7] << 56) + ((long)b[6] << 48) +
			((long)b[5] << 40) + ((long)b[4] << 32) +
			((long)b[3] << 24) + ((long)b[2] << 16) +
			((long)b[1] << 8) + b[0];
	}

	public final void writeIUnsignedInt(long v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
		write ((byte)(0xff & (v >> 16)));
		write ((byte)(0xff & (v >> 24)));
	}

	public final String readIString2space() throws IOException
	{
		byte b[] = new byte[1];
		String s = "";

		while (true)
		{
				read(b);
				if (b[0] == 0x20)
					break;
				s += new String(b);
		}
		return s;
	}

	public final String readIString() throws IOException
	{
		return  readLine();
	}

	public final void writeIString(String s) throws IOException
	{
		for (int i = 0; i < s.length(); i++)
			write ((byte)s.charAt(i));
		write ((byte)0x00);
	}

	public final void writeASCIIString(String s) throws IOException
	{
		for (int i = 0; i < s.length(); i++)
			write ((byte)s.charAt(i));
		write ((byte)'\n');
	}

	public final char readIChar() throws IOException
	{
		byte b[] = new byte[1];
		byte nul = 0x00;
		read(b);
		return (char)((nul << 8) | (b[0] & 0xff));
	}

	public final void writeIChar(char ch) throws IOException
	{
		write ((byte)ch);
	}
}
