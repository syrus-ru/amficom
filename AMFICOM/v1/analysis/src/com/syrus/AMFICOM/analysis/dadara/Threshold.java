package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class Threshold {
	public static final int THRESHOLD_SIZE = 128;

	public static final int UP1 = 0;

	public static final int UP2 = 1;

	public static final int DOWN1 = 2;

	public static final int DOWN2 = 3;

	private double[] dA = new double[] { .2, .4, -.2, -.4 };

	private double[] dC = new double[] { 0, 0, 0, 0 };

	private double[] dX = new double[] { 1, 2, -1, -2 };

	private double[] dL = new double[] { 0, 0, 0, 0 };
	
	private long serialNumber = 0; // для обнуления кэшей

	public Threshold() {
	}

	public Threshold(byte[] bar) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try {
			for (int i = 0; i < 4; i++)
				this.dA[i] = dis.readDouble();
			for (int i = 0; i < 4; i++)
				this.dC[i] = dis.readDouble();
			for (int i = 0; i < 4; i++)
				this.dX[i] = dis.readDouble();
			for (int i = 0; i < 4; i++)
				this.dL[i] = dis.readDouble();
			dis.close();
		} catch (IOException e) {
			System.out
					.println("Exception while converting byte array to Threshold: "
							+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	public long getSerialNumber()
	{
		return serialNumber;
	}

	public void changeThresholdBy(double da, double dc, double dx, double dl,
			ReflectogramEvent ep, int key)
	{
		serialNumber++;
		
		// TODO: rewrite thresholds
		int kID = key;
		int kIDconj;
		int sign;
		int sigd;
		switch (key) {
		case UP1: // upper yellow
			kIDconj = UP2;
			sign = 1;
			sigd = 1;
			break;
		case UP2: // upper red
			kIDconj = UP1;
			sign = 1;
			sigd = -1;
			break;
		case DOWN1: // lower yellow
			kIDconj = DOWN2;
			sign = -1;
			sigd = -1;
			break;
		case DOWN2: // lower red
			kIDconj = DOWN1;
			sign = -1;
			sigd = 1;
			break;
		default: // bad key given
			return;
		}

		dX[kID] += dx;
		if (dX[kID] * sign < 0)
			dX[kID] = 0;
		if (dX[kID] * sigd > dX[kIDconj] * sigd)
			dX[kIDconj] = dX[kID];

		dA[kID] += da;
		if (dA[kID] * sign < 0)
			dA[kID] = 0;
		if (dA[kID] * sigd > dA[kIDconj] * sigd)
			dA[kIDconj] = dA[kID];

		dL[kID] += dl;
		if (dL[kID] * sign < 0)
			dL[kID] = 0;
		if (dL[kID] * sigd > dL[kIDconj] * sigd)
			dL[kIDconj] = dL[kID];

		dC[0] -= dc;
		dC[1] -= dc;
		dC[2] -= dc;
		dC[3] -= dc;
	}

	public void setThresholdValue(double val, int col, ReflectogramEvent ep,
			int key) {
		switch (col) {
		case 1:
			changeThresholdBy(val - dA[key], 0, 0, 0, ep, key);
			break;
		case 2:
			changeThresholdBy(0, -val + dC[key], 0, 0, ep, key);
			break;
		case 3:
			changeThresholdBy(0, 0, val - dX[key], 0, ep, key);
			break;
		case 4:
			changeThresholdBy(0, 0, 0, val - dL[key], ep, key);
			break;
		}
	}
	
	public void initFromDY(double dymA, double dymW, double dypW, double dypA)
	{
		serialNumber++;

//		System.out.println("iniFromDY: dymA =" + dymA);
//		System.out.println("iniFromDY: dymW =" + dymW);
//		System.out.println("iniFromDY: dypW =" + dypW);
//		System.out.println("iniFromDY: dypA =" + dypA);

		dA[0] = dypW;
		dA[1] = dypA;
		dA[2] = dymW;
		dA[3] = dymA;
		dC[0] = dC[1] = dC[2] = dC[3] = 0;
		dL[0] = dL[1] = dL[2] = dL[3] = 0;
		dX[0] = 1;
		dX[1] = 2;
		dX[2] = -1;
		dX[3] = -2;
	}

	public Threshold copy() {
		Threshold th = new Threshold();
		th.dA = new double[] { dA[0], dA[1], dA[2], dA[3] };
		th.dC = new double[] { dC[0], dC[1], dC[2], dC[3] };
		th.dX = new double[] { dX[0], dX[1], dX[2], dX[3] };
		th.dL = new double[] { dL[0], dL[1], dL[2], dL[3] };
		return th;
	}

	public double[] getThresholds(int key) {
		return new double[] {
				MathRef.round_3(dA[key]),
				MathRef.round_3(dC[key]),
				MathRef.round_3(dX[key]),
				MathRef.round_3(dL[key]) };
	}

	public Double[] getThresholdsObject(int key) {
		return new Double[] {
				new Double(MathRef.round_3(dA[key])),
				new Double(MathRef.round_3(dC[key])),
				new Double(MathRef.round_3(dX[key])),
				new Double(MathRef.round_3(dL[key])) };
	}

	/*public double getThresholdValue(int x, ReflectogramEvent ep, int key)
	 {
	 return ep.thresholdAmplitude(x, this, key);
	 / *
	 if(ep.getType() == ReflectogramEvent.CONNECTOR)
	 return ep.connectorThresholdF(x, dL[key], dA[key], dX[key], dC[key]);
	 else if(ep.getType() == ReflectogramEvent.LINEAR)
	 return ep.linearThresholdF(x, dA[key]);
	 else
	 return ep.weldThresholdF(x, dA[key], dX[key]);
	 * /
	 }*/

	public byte[] getByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dA[i]);
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dC[i]);
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dX[i]);
			for (int i = 0; i < 4; i++)
				dos.writeDouble(dL[i]);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(Threshold[] ts) {
		byte[] bar = new byte[THRESHOLD_SIZE * ts.length];
		byte[] bar1;
		for (int i = 0; i < ts.length; i++) {
			bar1 = ts[i].getByteArray();
			for (int j = 0; j < THRESHOLD_SIZE; j++)
				bar[i * THRESHOLD_SIZE + j] = bar1[j];
		}
		return bar;
	}

	public static Threshold[] fromByteArray(byte[] bar) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		byte[] buf = new byte[THRESHOLD_SIZE];
		LinkedList ll = new LinkedList();
		try {
			while (dis.read(buf) == THRESHOLD_SIZE)
				ll.add(new Threshold(buf));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (Threshold[]) ll.toArray(new Threshold[ll.size()]);
	}
}