package com.syrus.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

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
		return (int)(((b[1] & 0xff) << 8) | (b[0] & 0xff));
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
		return (((b[7] & 0xff) << 56) | ((b[6] & 0xff) << 48) |
						((b[5] & 0xff) << 40) | ((b[4] & 0xff) << 32) |
						((b[3] & 0xff) << 24) | ((b[2] & 0xff) << 16) |
						((b[1] & 0xff) << 8) | (b[0] & 0xff));
	}


	public final long readIUnsignedInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return (long)((((long)b[3] & 0xff) << 24) | (((long)b[2] & 0xff) << 16) |
									(((long)b[1] & 0xff) << 8) | ((long)b[0] & 0xff));
	}

	public final long readUnsignedInt() throws IOException
	{
		byte b[] = new byte[4];
		read(b);
		return (long)((((long)b[0] & 0xff) << 24) | (((long)b[1] & 0xff) << 16) |
									(((long)b[2] & 0xff) << 8) | ((long)b[3] & 0xff));
	}

	public final String readIString() throws IOException
	{
		byte b[] = new byte[1];
		String s = "";

		while (true)
		{
			read(b);
			if (b[0] == 0x00)
				break;
			else
				s += new String(b);
		}
		return  s;
	}

	public final String readASCIIString() throws IOException
	{
		int res;
		int n_read = 0;
		byte b[] = new byte[1];
		String s = "";

		while (true)
		{
			res = read(b);
			if (res == -1)
				break;
			n_read++;
			if (b[0] == '\n')
				break;
			else if (b[0] != '\r')
				s += new String(b);
		}
		if (n_read == 0)
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
