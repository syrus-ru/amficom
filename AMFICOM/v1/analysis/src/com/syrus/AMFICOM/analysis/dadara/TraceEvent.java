package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TraceEvent
{
	public static final int LINEAR = 0;
	public static final int NON_IDENTIFIED = 1;
	public static final int INITIATE = 2;
	public static final int WELD = 3;
	public static final int CONNECTOR = 4;
	public static final int TERMINATE = 5;
	//public static final int CONCAVITY = 7;
	public static final int OVERALL_STATS = 100;

	public static final int DENOISED = 101;
	public static final int NOISE = 102;

	public int type;
	public int first_point;
	public int last_point;
	public double[] data;

	public TraceEvent (int type, int first_point, int last_point)
	{
		this.type = type;
		this.first_point = first_point;
		this.last_point = last_point;
	}

	public TraceEvent (byte[] bytearray)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bytearray);
		DataInputStream dis = new DataInputStream(bais);

		try
		{
			type = dis.readInt();
			first_point = dis.readInt();
			last_point = dis.readInt();
			int data_length = dis.readInt();
			data = new double[data_length];
			for (int i = 0; i < data_length; i++)
				data[i] = dis.readDouble();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public int getType ()
	{
		return type;
	}

	public byte[] toByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try
		{
			dos.writeInt(type);
			dos.writeInt(first_point);
			dos.writeInt(last_point);
			dos.writeInt(data.length);
			for (int i = 0; i < data.length; i++)
				dos.writeDouble(data[i]);
			dos.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return (baos.toByteArray());
	}
}