package com.syrus.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class ByteArrayCollector
{
	ByteArrayOutputStream baos;
	ByteArrayInputStream bais;
	DataOutputStream dos;
	DataInputStream dis;

	public ByteArrayCollector()
	{
		baos = new ByteArrayOutputStream();
		dos = new DataOutputStream(baos);
	}

	public void add (byte[] b)
	{
		try
		{
			dos.writeInt(b.length);
			dos.write(b);
		}
		catch (IOException io)
		{
			io.printStackTrace();
		}
	}

	public byte[] encode ()
	{
		try
		{
			dos.flush();
		}
		catch (IOException io)
		{
			io.printStackTrace();
		}
		return baos.toByteArray();
	}

	public byte[][] decode(byte[] b)
	{
		bais = new ByteArrayInputStream(b);
		dis = new DataInputStream(bais);

		int size = 0;

		try
		{
			while(dis.available() != 0)
			{
				int s = dis.readInt();
				dis.skipBytes(s);
				size++;
			}
		}
		catch (EOFException eof)
		{
			;
		}
		catch (IOException io)
		{
			io.printStackTrace();
		}

		byte[][] data = new byte[size][];
		try
		{
			dis.reset();
			for (int i = 0; i < size; i++)
			{
				int s = dis.readInt();
				data[i] = new byte[s];
				dis.read(data[i]);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return data;
	}
}
