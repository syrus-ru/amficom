package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class ReflectogramAlarm {
	public static final int ALARM_SIZE = 36;//old - 32
	public static final int LEVEL_SOFT = 0;
	public static final int LEVEL_HARD = 1;

	public int level;

	public int alarmPointCoord = 0;
	public int alarmEndPointCoord = 0;
	public double refAmplChangeValue = 0.;

	public int nearestReflectoEventDistance = 0;
	public int leftReflectoEventCoord = 0;
	public int rightReflectoEventCoord = 0;

	private int eventType = 0; // 0 -linear; 3 -weld; 4 -connector.

	public ReflectogramAlarm(int alarmPointCoord, int level)	{
		this.alarmPointCoord = alarmPointCoord;
		this.level = level;
	}

	public ReflectogramAlarm(int alarmPointCoord, int level, int alarmEndPointCoord)	{
		this.alarmPointCoord = alarmPointCoord;
		this.level = level;
		this.alarmEndPointCoord = alarmEndPointCoord;
	}

	public ReflectogramAlarm(byte[] bar) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try {
			this.level = dis.readInt();

			this.alarmPointCoord = dis.readInt();
			this.alarmEndPointCoord = dis.readInt();
			this.refAmplChangeValue = dis.readDouble();

			this.nearestReflectoEventDistance = dis.readInt();
			this.leftReflectoEventCoord = dis.readInt();
			this.rightReflectoEventCoord = dis.readInt();

			this.eventType = dis.readInt();

			dis.close();
		}
		catch (IOException e) {
			System.out.println("Exception while converting byte array to AlarmParams: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public int getEventType()
	{
		return eventType;
	}

	public void setEventType(int eventType)
	{
		this.eventType = eventType;
	}

	public byte[] getByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(ALARM_SIZE);
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(this.level);

			dos.writeInt(this.alarmPointCoord);
			dos.writeInt(this.alarmEndPointCoord);
			dos.writeDouble(this.refAmplChangeValue);

			dos.writeInt(this.nearestReflectoEventDistance);
			dos.writeInt(this.leftReflectoEventCoord);
			dos.writeInt(this.rightReflectoEventCoord);

			dos.writeInt(this.eventType);

			dos.close();
		}
		catch (IOException ioe) {
			System.out.println("Exception while getting byte array from alarm: " + ioe.getMessage());
			ioe.printStackTrace();
			return new byte[0]; //null
		}
		return baos.toByteArray();
	}

	public static byte[] toByteArray(ReflectogramAlarm[] ralarms) {
		byte[] bar = new byte[ALARM_SIZE * ralarms.length];
		byte[] bar1;
		for (int i = 0; i < ralarms.length; i++) {
			bar1 = ralarms[i].getByteArray();
			for (int j = 0; j < ALARM_SIZE; j++)
				bar[i*ALARM_SIZE + j] = bar1[j];
		}
		return bar;
	}

	public static ReflectogramAlarm[] fromByteArray(byte[] bar) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		byte[] buf = new byte[ALARM_SIZE];
		LinkedList ll = new LinkedList();
		try {
			while (dis.read(buf) == ALARM_SIZE) {
				ll.add(new ReflectogramAlarm(buf));
			}
			dis.close();
		}
		catch (IOException ioe) {
			System.out.println("Exception while converting byte array to array of alarms: " + ioe.getMessage());
			ioe.printStackTrace();
			return new ReflectogramAlarm[0]; //null
		}
		return (ReflectogramAlarm[])ll.toArray(new ReflectogramAlarm[ll.size()]);
	}
}

