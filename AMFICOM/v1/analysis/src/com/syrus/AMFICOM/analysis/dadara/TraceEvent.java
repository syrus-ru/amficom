package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TraceEvent
{
	public static final int LINEAR = 0;
	public static final int NON_IDENTIFIED = 10;
	public static final int INITIATE = 20;
	public static final int GAIN = 31;
	public static final int LOSS = 32;
	public static final int CONNECTOR = 40;
	public static final int TERMINATE = 50;
	public static final int OVERALL_STATS = 100;

	public static final int DENOISED = 101;
	public static final int NOISE = 102;

	public int type;
	public int first_point;
	public int last_point;
	public double[] data;

	public double loss = 0; // свойство определено для лин.уч., коннекторов, сварок и н/ид; не определено для начала и конца волокна 

	public double getLoss()
	{
		return loss;
	}
	public void setLoss(double v)
	{
		loss = v;
	}

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