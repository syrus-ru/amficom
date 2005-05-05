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
	private double[] data;

	private double loss = 0; // свойство определено для лин.уч., коннекторов, сварок и н/ид; не определено для начала и конца волокна // FIXME: save/restore 

	public double getLoss() {
		return loss;
	}
	public void setLoss(double v) {
		loss = v;
	}

	public TraceEvent(int type, int first_point, int last_point) {
		this.type = type;
		this.first_point = first_point;
		this.last_point = last_point;
	}

	public TraceEvent(byte[] bytearray) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytearray);
		DataInputStream dis = new DataInputStream(bais);

		try
		{
			type = dis.readInt();
			first_point = dis.readInt();
			last_point = dis.readInt();
			loss = dis.readDouble();
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

	public int getType() {
		return type;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try
		{
			dos.writeInt(type);
			dos.writeInt(first_point);
			dos.writeInt(last_point);
			dos.writeDouble(loss);
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
	
	// overallStats methods
	public double overallStatsNoiseLevel() { // yMax - yNoise
		return data[2];
	}
	public double overallStatsLoss() {
		return Math.abs(data[0] -  data[1]);
	}
	public double overallStatsY0() {
		return data[0];
	}
	public double overallStatsY1() {
		return data[1];
	}
	public double overallStatsDD() {
		return data[2] - data[3];
	}
	public int overallStatsEvNum() {
		return (int) data[4];
	}
	
	// linear methods
	public double linearData0() {
		return data[0];
	}
	public double linearData1() {
		return data[1];
	}
	public double linearAsympLoss() {
		return data[2];
	}
	public double linearData3() {
		return data[3];
	}
	public double linearData4() {
		return data[4];
	}
	
	public double spliceData0() {
		return data[0];
	}
	public double spliceData1() {
		return data[1];
	}

	// initial event methods
	public double initialData0() {
		return data[0];
	}
	public double initialData1() {
		return data[1];
	}
	public double initialData2() {
		return data[2];
	}
	public double initialData3() {
		return data[3];
	}

	// connector methods
	public double connectorData0() {
		return data[0];
	}
	public double connectorData1() {
		return data[1];
	}
	public double connectorData2() {
		return data[2];
	}
	public double connectorPeak() {
		return data[0] - data[2];
	}

	// terminate event smethods
	public double terminateData0() {
		return data[0];
	}
	public double terminateData1() {
		return data[1];
	}
	public double terminateReflection() {
		return data[0] - data[1];
	}

	// non-identified event methods
	public double nonidData0() {
		return data[0];
	}
	public double nonidData1() {
		return data[1];
	}
	public double nonidData2() {
		return data[2];
	}
	/**
	 * sets all data
	 * @param data2
	 */
	public void setData(double[] data2)
	{
		data = (double[])data2.clone();
	}
}
