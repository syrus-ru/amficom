package com.syrus.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class IntelDataOutputStream extends DataOutputStream
{

	public IntelDataOutputStream (OutputStream os)
	{
		super (os);
	}

	public final void writeIShort(short v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
	}

	public final void writeIUnsignedShort(int v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
	}

	public final void writeIInt(int v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
		write ((byte)(0xff & (v >> 16)));
		write ((byte)(0xff & (v >> 24)));
	}

	public final void writeIUnsignedInt(long v) throws IOException
	{
		write ((byte)(0xff & v));
		write ((byte)(0xff & (v >> 8)));
		write ((byte)(0xff & (v >> 16)));
		write ((byte)(0xff & (v >> 24)));
	}

	public final void writeIString(String s) throws IOException
	{
		for (int i = 0; i < s.length(); i++)
			write ((byte)s.charAt(i));
		write ((byte)0x00);
	}

	public final void writeIChar(char ch) throws IOException
	{
		write ((byte)ch);
	}
}
