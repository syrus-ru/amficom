package com.syrus.AMFICOM.analysis.dadara;

import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity.SEVERITY_NONE;
import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType.TYPE_UNDEFINED;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.SOAnchorImpl;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.io.DataFormatException;
import com.syrus.io.SignatureMismatchException;

/**
 * ��������� ��� �������� �������������� �������������� �������.
 * ��������� ��. {@link ReflectogramMismatch}
 * @see ReflectogramMismatch
 * 
 * @author $Author: saa $
 * @version $Revision: 1.9 $, $Date: 2005/10/14 07:59:13 $
 * @module dadara
 */
public class ReflectogramMismatchImpl
implements ReflectogramMismatch, Cloneable {
	private static final long SIGNATURE = 5490879050929171200L;

	private Severity severity = SEVERITY_NONE;
	// ���������� ��������� (� ������) ������� ������� ��� ����� ��
	// ������� �������, � �������/������� ��������� �����.
	// ����� ���������� �� ����������� ����� ������ �� ������� �������.
	private int coord = 0;
	private int endCoord = 0; // ��� �����������
	private AlarmType alarmType = TYPE_UNDEFINED;
	private double deltaX = 0.0;

	// ���������� � (�������� ����) ��������� ����������� ��������
	private SOAnchorImpl ref1Id = null; // null, ���� �������� #1 �� ����������
	private int ref1Coord = 0; // ���������� ��������� �������� #1, �� ����������, ���� ref1Id == null
	private SOAnchorImpl ref2Id = null; // null, ���� �������� #2 �� ����������
	private int ref2Coord = 0; // ���������� ��������� �������� #2, �� ����������, ���� ref2Id == null

	// ������ ������� ���������� �������. ������ �� ��������� � ���������
	// ��������� "�� ����������" - ���� min > max
	// ��������� ��������� - "�� ����������"
	private double minMismatch = 1.0; // ������ ����� (��������� �� ����������)
	private double maxMismatch = 0.0; // ������ ������ (��������� �� ����������)

	@Override
	protected ReflectogramMismatchImpl clone() {
		try {
			return (ReflectogramMismatchImpl) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/**
	 * Makes a copy
	 * @return a copy of this
	 */
	public ReflectogramMismatchImpl copy() {
		return this.clone();
	}

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

	public boolean hasMismatch() {
		return this.minMismatch <= this.maxMismatch;
	}

	public double getMinMismatch() {
		if (hasMismatch())
			return this.minMismatch;
		else
			throw new IllegalStateException();
	}
	public double getMaxMismatch() {
		if (hasMismatch())
			return this.maxMismatch;
		else
			throw new IllegalStateException();
	}

	public Severity getSeverity() {
		return this.severity;
	}
	public void setSeverity(final Severity severity) {
		this.severity = severity;
	}
	public double getDistance() {
		return getDeltaX() * getCoord();
	}
	public void setAnchors(SOAnchorImpl anchor1, int coord1,
			SOAnchorImpl anchor2, int coord2) {
		if (anchor1 == null || anchor2 == null) {
			throw new IllegalArgumentException("null anchor in setAnchor");
		}
		this.ref1Id = anchor1;
		this.ref1Coord = coord1;
		this.ref2Id = anchor2;
		this.ref2Coord = coord2;
	}
	public void unSetAnchors() {
		this.ref1Id = null;
		this.ref1Coord = 0;
		this.ref2Id = null;
		this.ref2Coord = 0;
	}
	public boolean hasAnchors() {
		return this.ref1Id != null && this.ref2Id != null;
	}
	public SOAnchorImpl getAnchor1Id() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref1Id;
	}
	public SOAnchorImpl getAnchor2Id() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref2Id;
	}
	public int getAnchor1Coord() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref1Coord;
	}
	public int getAnchor2Coord() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref2Coord;
	}

	/**
	 * Creates an empty 'no alarm' alarm
	 * with level = LEVEL_NONE and type = TYPE_UNDEFINED.
	 */
	public ReflectogramMismatchImpl()
	{ // all initialization is already done
	}

	protected static ReflectogramMismatchImpl createFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		ReflectogramMismatchImpl ret = new ReflectogramMismatchImpl();
		ret.severity = Severity.valueOf(dis.readInt());
		ret.setCoord(dis.readInt());
		ret.setEndCoord(dis.readInt());
		ret.setAlarmType(AlarmType.valueOf(dis.readInt()));
		ret.setDeltaX(dis.readDouble());
		if (dis.readBoolean()) {
			ret.minMismatch = dis.readDouble(); // XXX: ������� �� ������������
			ret.maxMismatch = dis.readDouble();
		} else {
			ret.minMismatch = 1.0;
			ret.maxMismatch = 0.0;
		}
		if(dis.readBoolean()) {
			ret.ref1Id = SOAnchorImpl.createFromDIS(dis);
			ret.ref2Id = SOAnchorImpl.createFromDIS(dis);
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
 
	protected void writeToDOS(DataOutputStream dos)
	throws IOException
	{
		// ��������������, �������� �������� �� 34 �� 74 ����
		dos.writeLong(SIGNATURE);
		dos.writeInt(this.severity.ordinal());
		dos.writeInt(this.getCoord());
		dos.writeInt(this.getEndCoord());
		dos.writeInt(this.getAlarmType().ordinal());
		dos.writeDouble(this.getDeltaX());
		if (hasMismatch()) {
			dos.writeBoolean(true);
			dos.writeDouble(this.minMismatch);
			dos.writeDouble(this.maxMismatch);
		} else {
			dos.writeBoolean(false);
		}
		if (this.ref1Id != null && this.ref2Id != null) {
			dos.writeBoolean(true);
			this.ref1Id.writeToDOS(dos);
			this.ref2Id.writeToDOS(dos);
			dos.writeInt(this.ref1Coord);
			dos.writeInt(this.ref2Coord);
		} else {
			dos.writeBoolean(false);
		}
	}

	@Deprecated
	protected static ReflectogramMismatch createFromByteArray(byte[] bar) throws DataFormatException
	{
			try {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bar));
				ReflectogramMismatch ret;
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

	public static byte[] alarmsToByteArray(ReflectogramMismatchImpl[] ralarms) {
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

	public static ReflectogramMismatchImpl[] alarmsFromByteArray(byte[] bar)
	throws DataFormatException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try
		{
			int count = dis.readInt();
			ReflectogramMismatchImpl[] ret = new ReflectogramMismatchImpl[count]; // exception possible when input data is malformed
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
	public void toHardest(ReflectogramMismatchImpl that)
	{
		if (that.severity.compareTo(this.severity) > 0
				|| that.severity == this.severity && that.getCoord() < this.getCoord())
		{
			this.severity = that.severity;
			this.setCoord(that.getCoord());
			this.setEndCoord(that.getEndCoord());
			this.setAlarmType(that.getAlarmType());
			this.setDeltaX(that.getDeltaX());
		}
	}
	
	@Override
	public String toString()
	{
		return "ReflectogramMismatch(level=" + this.severity
		+ ",type=" + getAlarmType()
		+ ",begin=" + getCoord()
		+ ",end=" + getEndCoord()
		+ ",distance=" + getDistance()
		+ (hasMismatch() ? ",mismatch=" + getMinMismatch() + "-" + getMaxMismatch() : "")
		+ (this.ref1Id != null ?  ",anc1=" + this.ref1Id + "@" + this.ref1Coord : "")
		+ (this.ref2Id != null ?  ",anc2=" + this.ref2Id + "@" + this.ref2Coord : "")
		+ ")";
	}

	public void setCoord(int coord) {
		this.coord = coord;
	}

	public int getCoord() {
		return this.coord;
	}

	public void setEndCoord(int endCoord) {
		this.endCoord = endCoord;
	}

	public int getEndCoord() {
		return this.endCoord;
	}

	public void setAlarmType(final AlarmType alarmType) {
		this.alarmType = alarmType;
	}

	public AlarmType getAlarmType() {
		return this.alarmType;
	}

	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	public double getDeltaX() {
		return this.deltaX;
	}
}
