package com.syrus.AMFICOM.analysis.dadara;

import java.io.*;
import java.util.LinkedList;

public class Threshold
{
	public static final int THRESHOLD_SIZE = 128;

	public static final int UP1 = 0;
	public static final int UP2 = 1;
	public static final int DOWN1 = 2;
	public static final int DOWN2 = 3;

	double[] dA = new double[] {.2, .4, -.2, -.4};
	double[] dC = new double[] {0, 0, 0, 0};
	double[] dX = new double[] {3, 6, -3, -6};
	double[] dL = new double[] {0, 0, 0, 0};

	ReflectogramEvent ep;

	public Threshold(ReflectogramEvent ep)
	{
		setReflectogramEvent(ep);
	}

	public void setReflectogramEvent(ReflectogramEvent ep)
	{
		this.ep = ep;
	}

	public Threshold(byte[] bar)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try
		{
			for (int i = 0; i < 4; i++)
				this.dA[i] = dis.readDouble();
			for (int i = 0; i < 4; i++)
				this.dC[i] = dis.readDouble();
			for (int i = 0; i < 4; i++)
				this.dX[i] = dis.readDouble();
			for (int i = 0; i < 4; i++)
				this.dL[i] = dis.readDouble();
			dis.close();
		}
		catch (IOException e)
		{
			System.out.println("Exception while converting byte array to Threshold: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void changeThreshold(double da, double dc, double dx, double dl, int key)
	{
		switch (key)
		{
			case UP1:
				if (dX[0]+dx > 0) dX[0] += dx;
				if (dL[0]+dl > 0) dL[0] += dl;
				if (dA[0]+da > 0) dA[0] += da;

				if (dX[0] > dX[1]) dX[1] = dX[0];
				if (dL[0] > dL[1]) dL[1] = dL[0];
				if (dA[0] > dA[1]) dA[1] = dA[0];
				break;

			case UP2:
				if (dX[1]+dx > 0) dX[1] += dx;
				if (dL[1]+dl > 0) dL[1] += dl;
				if (dA[1]+da > 0)	dA[1] += da;

				if (dX[1] < dX[0]) dX[0] = dX[1];
				if (dL[1] < dL[0]) dL[0] = dL[1];
				if (dA[1] < dA[0]) dA[0] = dA[1];
				break;

			case DOWN1:
				if ((dX[2]+dx < 0) && (dX[2]+dx > - ep.width_connector / 2)) dX[2] += dx;
				if ((dL[2]+dl < 0) && (dL[2]+dl > - ep.aLet_connector)) dL[2] += dl;
				if (dA[2]+da < 0) dA[2] += da;

				if (dX[2] < dX[3]) dX[3] = dX[2];
				if (dL[2] < dL[3]) dL[3] = dL[2];
				if (dA[2] < dA[3]) dA[3] = dA[2];
				break;

			case DOWN2:
				if ((dX[3]+dx < 0) && (dX[3]+dx > - ep.width_connector / 2)) dX[3] += dx;
				if ((dL[3]+dl < 0) && (dL[3]+dl > - ep.aLet_connector)) dL[3] += dl;
				if (dA[3]+da < 0) dA[3] += da;

				if (dX[3] > dX[2]) dX[2] = dX[3];
				if (dL[3] > dL[2]) dL[2] = dL[3];
				if (dA[3] > dA[2]) dA[2] = dA[3];
				break;
		}
		dC[0] -= dc;
		dC[1] -= dc;
		dC[2] -= dc;
		dC[3] -= dc;
	}

	public void setThresholdValue (double val, int col, int key)
	{
		switch (col)
		{
			case 1: changeThreshold (val - dA[key], 0, 0, 0, key);
				break;
			case 2: changeThreshold (0, - val + dC[key], 0, 0, key);
				break;
			case 3: changeThreshold (0, 0, val - dX[key], 0, key);
				break;
			case 4: changeThreshold (0, 0, 0, val - dL[key], key);
				break;
		}
	}

	public Object clone()
	{
		Threshold th = new Threshold(ep);
		th.dA = new double[] {dA[0], dA[1], dA[2], dA[3]};
		th.dC = new double[] {dC[0], dC[1], dC[2], dC[3]};
		th.dX = new double[] {dX[0], dX[1], dX[2], dX[3]};
		th.dL = new double[] {dL[0], dL[1], dL[2], dL[3]};
		return th;
	}

	public double[] getThresholds (int key)
	{
		return new double[]
		{
			MathRef.round_3(dA[key]),
			MathRef.round_3(dC[key]),
			MathRef.round_3(dX[key]),
			MathRef.round_3(dL[key])
		};
	}

	public Double[] getThresholdsObject (int key)
	{
		return new Double[]
		{
			new Double(MathRef.round_3(dA[key])),
			new Double(MathRef.round_3(dC[key])),
			new Double(MathRef.round_3(dX[key])),
			new Double(MathRef.round_3(dL[key]))
		};
	}

	public double getThresholdValue(int x, int key)
	{
		if(ep.getType() == ReflectogramEvent.CONNECTOR)
			return ep.connectorThresholdF(x, dL[key], dA[key], dX[key], dC[key]);
		else if(ep.getType() == ReflectogramEvent.LINEAR)
			return ep.linearThresholdF(x, dA[key]);
		else
			return ep.weldThresholdF(x, dA[key], dX[key]);
	}

	public byte[] getByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dA[i]);
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dC[i]);
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dX[i]);
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dL[i]);
			dos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(Threshold[] ts)
	{
		byte[] bar = new byte[THRESHOLD_SIZE * ts.length];
		byte[] bar1;
		for (int i = 0; i < ts.length; i++)
		{
			bar1 = ts[i].getByteArray();
			for (int j = 0; j < THRESHOLD_SIZE; j++)
				bar[i * THRESHOLD_SIZE + j] = bar1[j];
		}
		return bar;
	}

	public static Threshold[] fromByteArray(byte[] bar)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		byte[] buf = new byte[THRESHOLD_SIZE];
		LinkedList ll = new LinkedList();
		try
		{
			while (dis.read(buf) == THRESHOLD_SIZE)
				ll.add(new Threshold(buf));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return (Threshold[])ll.toArray(new Threshold[ll.size()]);
	}
}