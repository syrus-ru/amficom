package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class ReflectogramAlarm {
	// Alarm levels. Must be comparable with >; >=
	public static final int LEVEL_NONE = 0; // just a convenience level, not a real alarm
	public static final int LEVEL_SOFT = 1; // soft alarm ('warning')
	public static final int LEVEL_HARD = 2; // hard alarm ('alarm')

	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_LINEBREAK = 1;
	public static final int TYPE_OUTOFMASK = 2;
	public static final int TYPE_EVENTLISTCHANGED = 3;

	public int level = LEVEL_NONE;
	public int pointCoord = 0;
	public int endPointCoord = 0; // ?
	public int alarmType = TYPE_UNDEFINED;
	/*
	public int getLevel()
	{
		return level;
	}
	public int getPoint()
	{
		return pointCoord;
	}
	public int getAlarmType()
	{
		return alarmType;
	}
*/
	// create 'no alarm' alarm
	public ReflectogramAlarm()
	{
	}

	public static ReflectogramAlarm createFromDIS(DataInputStream dis)
	throws IOException
	{
		ReflectogramAlarm ret = new ReflectogramAlarm();
		ret.level = dis.readInt();
		ret.pointCoord = dis.readInt();
		ret.endPointCoord = dis.readInt();
		ret.alarmType = dis.readInt();
		return ret;
	}
 
	public void writeToDOS(DataOutputStream dos)
	throws IOException
	{
		dos.writeInt(this.level);
		dos.writeInt(this.pointCoord);
		dos.writeInt(this.endPointCoord);
		dos.writeInt(this.alarmType);
	}

	public static ReflectogramAlarm createFromByteArray(byte[] bar)
	throws IOException
	// @todo: catch IOException and throw some other exception: "format mismatch"
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bar));
		ReflectogramAlarm ret = createFromDIS(dis);
		dis.close();
		return ret;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			writeToDOS(dos);
			dos.close();
			return baos.toByteArray();
		}
		catch (IOException ioe) {
			// XXX
			System.out.println("Something very unexpected while getting byte array from alarm: " + ioe.getMessage());
			ioe.printStackTrace();
			return new byte[0]; //null
		}
	}

	public static byte[] alarmsToByteArray(ReflectogramAlarm[] ralarms) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeInt(ralarms.length);
			for (int i = 0; i < ralarms.length; i++)
			{
				ralarms[i].writeToDOS(dos);
			}
		}
		catch (IOException ioe) {
			// XXX
			System.out.println("Something very unexpected while getting byte array from alarms: " + ioe.getMessage());
			ioe.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static ReflectogramAlarm[] alarmsFromByteArray(byte[] bar)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try
		{
			int count = dis.readInt();
			ReflectogramAlarm[] ret = new ReflectogramAlarm[count]; // XXX: exception possible when input data is malformed
			for (int i = 0; i < count; i++)
				ret[i] = createFromDIS(dis);
			dis.close();
			return ret;
		}
		catch(IOException ioe)
		{
			// XXX
			System.out.println("Something very unexpected in alarmsFromByteArray: " + ioe.getMessage());
			ioe.printStackTrace();
			return null;
		}
	}
}

