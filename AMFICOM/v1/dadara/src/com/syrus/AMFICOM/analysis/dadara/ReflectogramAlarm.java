package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.SOAnchor;

/**
 * ��������� ��� �������� ����������������� ������.
 * ����� ���������� � ����, ������ � ��������� �������.
 * <p>
 * ��������� ������� ������� ���������.
 * ���� {@link #pointCoord} �������� ��������� ���������� ��������� (� ������)
 * ������� ��� �������, � ������� ������������� ����������. ��� ��������
 * ��������� ���������� ��������� � ������� ���������� ��������� ���� ��� �������:
 * <ol>
 * <li> ���������������� ����������� ��������� ���������� ��������� � ��������
 * ������� ���������� ���������. ��� ���� ��������� �����������, ������������
 * �������� ������� ���������� ��������� �� �������� ���������� ���������
 * (��-�� ����������� ����������� ������ ������� ��������), �
 * ����� �������� ��������� ���������� ��������� �� ��������.
 * ���� ��� ����������� ����� ����������� ������ ��� ������� ����� ����
 * ���������������, ��� ������ ��������� ������ �����������, ��������� ��
 * ������� � ������� ��������� ������� (����� � ��.) ��� � �������� � ���
 * ������.
 * <li> ������������� ��������
 *   {@link #ref1Id}/{@link #ref1Coord},
 *   {@link #ref2Id}/{@link #ref2Coord}.
 * ��� ���������� ��������� ��������� ���������� ��������� (���� � ������)
 * ��� 0, 1 ��� 2 ��������, ����������� �� ������ ������� �� ������.
 * ��� ��������� ������ ���������� ����� ������. � ������, ����
 * ��� ������� ������� ���� ������������� �������� ��� ������� �������,
 * �������� �������� �������, ���������������� ������, ���������� ����������,
 * � ����������� ��������� ������ � ��������, �������� ��� ���������
 * ���������� � ���������� ���������.
 * <p> ��� �������������� ������� ���������� ��� ���������� ���������,
 * ���� ������������ ���������
 * 
 * <pre> pointCoord-ref1Coord : ref2Coord-pointCoord = L1 : L2 </pre>
 * 
 * ��� L1 � L2 - ������� (���������� ��� ����������) ���������� �� ������
 * �� ������������� �������� �����.
 * <p> ���� ������� �������� ����������, �� ��� ����� �� ������ �������
 * �� ������ (�� ��� ���������).
 * <p> ���� ����� ���������� �� �������� ������� (������, ���������),
 * ��� ������� �������� � �����, �� ref1Id � ref2Id ���������,
 * � �����. ��������� �������.
 * <p> ���� ���� �� ���� �� ref1Id, ref2Id - null, �� ���������, ��� ��������
 * ��� � ����� ������������ ������ ������ ���������� � ������� ���������.
 * </ol>
 * 
 * @author $Author: saa $
 * @version $Revision: 1.17 $, $Date: 2005/06/30 14:19:57 $
 * @module
 */
public class ReflectogramAlarm {
	// Alarm levels. Must be comparable with >; >=
	public static final int LEVEL_NONE = 0; // just a convenience level, not a real alarm
	public static final int LEVEL_SOFT = 1; // soft alarm ('warning')
	public static final int LEVEL_HARD = 2; // hard alarm ('alarm')

	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_LINEBREAK = 1; // ����� �����
	public static final int TYPE_OUTOFMASK = 2; // ����� �� �����
	public static final int TYPE_EVENTLISTCHANGED = 3; // �����/���������� ������� � �������� �����

	public int level = LEVEL_NONE;
	// ���������� ��������� (� ������) ������� ������� ��� ����� ��
	// ������� �������, � �������/������� ��������� �����.
	// ����� ���������� �� ����������� ����� ������ �� ������� �������.
	public int pointCoord = 0;
	public int endPointCoord = 0; // ?
	public int alarmType = TYPE_UNDEFINED;
    public double deltaX = 0.0;

    // ���������� � (�������� ����) ��������� ����������� ��������
    public SOAnchor ref1Id = null; // null, ���� �������� #1 �� ����������
    public int ref1Coord = 0; // ���������� ��������� �������� #1, �� ����������, ���� ref1Id == null
    public SOAnchor ref2Id = null; // null, ���� �������� #2 �� ����������
    public int ref2Coord = 0; // ���������� ��������� �������� #2, �� ����������, ���� ref2Id == null

    // ������ ������� ���������� �������. ������ �� ��������� � ���������
    // ��������� "�� ����������" - ���� min > max
    // ��������� ��������� - "�� ����������"
    private double minMismatch = 1.0; // ������ ����� (��������� �� ����������)
    private double maxMismatch = 0.0; // ������ ������ (��������� �� ����������)

    /**
     * ������������� ������� ���������� �������. ������ � ���������
     * "����������" � ���������� �� min �� max.
     * min ������ ���� �� ����� max
     */
    public void setMismatch(double min, double max) {
    	if (min > max)
    		throw new IllegalArgumentException("minMismatch > maxMismatch");
    	this.minMismatch = min;
    	this.maxMismatch = max;
    }
    /**
     * ������������� ������� ���������� �������. ������ � ��������� "�� ����������" 
     */
    public void unsetMismatch() {
    	this.minMismatch = 1.0;
    	this.maxMismatch = 0.0;
    }

    /**
     * @return true, ���� ������� ���������� �������. ������ ����������
     */
    public boolean hasMismatch() {
    	return minMismatch <= maxMismatch;
    }
    /**
     * @return ������ ������ ������� ���������� ������������������ ������,
     *   ���� ������� ���������� ����������
     *   ({@link #hasMismatch() ���������� true})
     * @throws IllegalArgumentException, ���� ������� ���������� �� ����������
     */
    public double getMinMismatch() {
    	if (hasMismatch())
    		return minMismatch;
    	else
    		throw new IllegalArgumentException();
    }
    /**
     * @return ������� ������ ������� ���������� ������������������ ������,
     *   ���� ������� ���������� ����������
     *   ({@link #hasMismatch() ���������� true})
     * @throws IllegalArgumentException, ���� ������� ���������� �� ����������
     */
    public double getMaxMismatch() {
    	if (hasMismatch())
    		return maxMismatch;
    	else
    		throw new IllegalArgumentException();
    }

    /*
	public int getLevel()
	{
		return level;
	}
	public int getPoint()
	{
		return pointCoord;
	}
*/
    public int getSeverity() {
        return level;
    }
	public int getSpecificType() {
		return alarmType;
	}
    public double getDistance() {
        return deltaX * pointCoord;
    }
	/**
	 * Creates an empty 'no alarm' alarm
	 * with level = LEVEL_NONE and type = TYPE_UNDEFINED.
	 */
	public ReflectogramAlarm()
	{ // all initialization is already done
	}

	public static ReflectogramAlarm createFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException
	{
		ReflectogramAlarm ret = new ReflectogramAlarm();
		ret.level = dis.readInt();
		ret.pointCoord = dis.readInt();
		ret.endPointCoord = dis.readInt();
		ret.alarmType = dis.readInt();
        ret.deltaX = dis.readDouble();
        if (dis.readBoolean()) {
        	ret.minMismatch = dis.readDouble(); // XXX: ������� �� ������������
        	ret.maxMismatch = dis.readDouble();
        } else {
        	ret.minMismatch = 1.0;
        	ret.maxMismatch = 0.0;
        }
        if(dis.readBoolean()) {
        	ret.ref1Id = (SOAnchor) SOAnchor.getDSReader().readFromDIS(dis);
        	ret.ref2Id = (SOAnchor) SOAnchor.getDSReader().readFromDIS(dis);
        	ret.ref1Coord = dis.readInt();
        	ret.ref2Coord = dis.readInt();
        } else {
        	ret.ref1Id = null;
        	ret.ref1Id = null;
        	ret.ref1Coord = 0;
        	ret.ref2Coord = 0;
        }
		return ret;
	}
 
	public void writeToDOS(DataOutputStream dos)
	throws IOException
	{
		// ��������������, �������� �������� �� 26 �� 66 ����
		dos.writeInt(this.level);
		dos.writeInt(this.pointCoord);
		dos.writeInt(this.endPointCoord);
		dos.writeInt(this.alarmType);
        dos.writeDouble(this.deltaX);
        if (hasMismatch()) {
        	dos.writeBoolean(true);
        	dos.writeDouble(this.minMismatch);
        	dos.writeDouble(this.maxMismatch);
        } else {
        	dos.writeBoolean(false);
        }
        if (ref1Id != null && ref2Id != null) {
        	dos.writeBoolean(true);
        	ref1Id.writeToDOS(dos);
        	ref2Id.writeToDOS(dos);
        	dos.writeInt(ref1Coord);
        	dos.writeInt(ref2Coord);
        } else {
        	dos.writeBoolean(false);
        }
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
     * ���� ����� that ����� �����������, ��� this,
     * ��������� ��������� that � this.
     * <p>��������� ������������ � ����� �������:
     * <ol>
     * <li> ������� level
     * <li> ������� pointCoord
     * <li> ��������� ��������� ���������� ���� �� ���������� 
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
            this.deltaX = that.deltaX;
        }
    }
    
    public String toString()
    {
        return "ReflectogramAlarm(level=" + level
        + ",type=" + alarmType
        + ",begin=" + pointCoord
        + ",end=" + endPointCoord
        + ",distance=" + getDistance()
        + (hasMismatch() ? ",mismatch=" + getMinMismatch() + "-" + getMaxMismatch() : "")
        + ")";
    }
}
