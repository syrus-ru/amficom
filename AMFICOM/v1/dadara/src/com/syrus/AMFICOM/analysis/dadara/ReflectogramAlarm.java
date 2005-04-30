package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	/**
	 * Creates an empty 'no alarm' alarm
	 * with level = LEVEL_NONE and type = TYPE_UNDEFINED.
	 */
	public ReflectogramAlarm()
	{ // all initialization is already done
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

	public static ReflectogramAlarm createFromByteArray(byte[] bar) throws DataFormatException
	{
            try {
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bar));
                ReflectogramAlarm ret;
                ret = createFromDIS(dis);
                dis.close();
                return ret;
            } catch (IOException e) {
                throw new DataFormatException(e.toString());
            }
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
            throw new InternalError("Unexpected exception" + ioe.toString());
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
            throw new InternalError("Unexpected exception" + ioe.toString());
		}
		return baos.toByteArray();
	}

    // seem to be unused
	public static ReflectogramAlarm[] alarmsFromByteArray(byte[] bar)
    throws DataFormatException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try
		{
			int count = dis.readInt();
			ReflectogramAlarm[] ret = new ReflectogramAlarm[count]; // exception possible when input data is malformed
			for (int i = 0; i < count; i++)
				ret[i] = createFromDIS(dis);
			dis.close();
			return ret;
		}
		catch(IOException ioe)
		{
            throw new DataFormatException(ioe.toString());
		}
	}

    /**
     * Если аларм that более приоритетен, чем this,
     * загружает параметры that в this.
     * <p>Приоритет определяется в таком порядке:
     * <ol>
     * <li> бОльший level
     * <li> меньший pointCoord
     * <li> сравнение остальных параметров пока не определено 
     * </ol>
     * 
     * @param that
     */
    public void toHardest(ReflectogramAlarm that)
    {
        if (that.level > this.level
                || that.level == this.level && that.pointCoord < this.pointCoord)
        {
            this.level = that.level;
            this.pointCoord = that.pointCoord;
            this.endPointCoord = that.endPointCoord;
            this.alarmType = that.alarmType;
        }
    }
    
    public String toString()
    {
        return "ReflectogramAlarm(level=" + level + ",type=" + alarmType + ",begin=" + pointCoord + ",end=" + endPointCoord + ")";
    }
}
