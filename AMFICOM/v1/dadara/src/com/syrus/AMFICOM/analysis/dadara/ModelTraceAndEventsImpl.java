/*-
 * $Id: ModelTraceAndEventsImpl.java,v 1.6 2005/04/30 09:12:11 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/04/30 09:12:11 $
 * @module
 */
public class ModelTraceAndEventsImpl
implements ReliabilityModelTraceAndEvents, DataStreamable
{
	protected static final long SIGNATURE_EVENTS = 3353520050119193102L;

	private ReliabilitySimpleReflectogramEventImpl[] rse; // not null
	private ModelFunction mf; // not null
	private int traceLength;
	private double deltaX = 1; // XXX
	private ModelTrace mt; // will just contain mt

	private static DataStreamable.Reader dsReader = null; // DIS reader singleton-style object

	public ModelTraceAndEventsImpl(ReliabilitySimpleReflectogramEventImpl[] rse,
			ModelFunction mf, double deltaX)
	{
		this.rse = rse;
		this.mf = mf;
		this.deltaX = deltaX;
		this.setTraceLength(calcTraceLength());
		mt = new ModelTraceImplMF(this.getMF(), this.getTraceLength());
	}

	public double getDeltaX()
	{
		return deltaX;
	}
	public ModelTrace getModelTrace()
	{
		return mt;
	}

    /**
     * protected because hopes that caller will not modify the array returned
     * @return internal array of reliability events.
     */
	protected ReliabilitySimpleReflectogramEventImpl[] getRSE()
	{
		return rse;
	}
	protected ModelFunction getMF()
	{
		return mf;
	}
	protected int getTraceLength()
	{
		return traceLength;
	}

	private int calcTraceLength()
	{
		if (getRSE().length == 0)
			return 0;
		else
			return getRSE()[getRSE().length - 1].getEnd() + 1;
	}
	private void setTraceLength(int traceLength)
	{
		this.traceLength = traceLength;
	}

	public SimpleReflectogramEvent[] getSimpleEvents()
	{
        // Copy an array and all its references to protect se array.
        // Array elements are unmodifiable, so no need to clone them.
        return (ReliabilitySimpleReflectogramEvent[] )getRSE().clone();
	}

	public ComplexReflectogramEvent[] getComplexEvents()
	{
		return ComplexReflectogramEvent.createEvents(getRSE(), mt);
	}

	/**
	 * ���������� ����� �������, ���������������� �������
	 * ����. ���� x �������� �� ������� ���� �������,
	 * �� ����� ������ ��� ������� ������� �� ����������.
	 * ���� x �� �������� �� �� ���� �������,
	 * �� ���������� -1.
	 * <p> ������������ ��������� ����� � �� ����� �������. 
	 */
	public int getEventByCoord(int x)
	{
		int ret = -1;
		for (int i = 0; i < getRSE().length; i++)
		{
			if (x >= getRSE()[i].getBegin() && x <= getRSE()[i].getEnd())
				ret = i;
		}
		return ret;
	}

	public int getNEvents()
	{
		return getRSE().length;
	}

	public SimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return getRSE()[nEvent];
	}

	public void writeToDOS(DataOutputStream dos) throws IOException
	{
		dos.writeLong(SIGNATURE_EVENTS);
		getMF().writeToDOS(dos);
		dos.writeDouble(getDeltaX());
		dos.writeInt(rse.length);
		for (int i = 0; i < rse.length; i++)
			rse[i].writeToDOS(dos);
	}

	public static DataStreamable.Reader getReader()
	{
		if (dsReader == null)
			dsReader = new DataStreamable.Reader() {
            public DataStreamable readFromDIS(DataInputStream dis)
            throws IOException, SignatureMismatchException
            {
                long signature = dis.readLong();
                if (signature != SIGNATURE_EVENTS)
                    throw new SignatureMismatchException();
                ModelFunction mf = ModelFunction.createFromDIS(dis);
                double deltaX = dis.readDouble();
                int len = dis.readInt();
                ReliabilitySimpleReflectogramEventImpl[] se = new ReliabilitySimpleReflectogramEventImpl[len];
                for (int i = 0; i < len; i++)
                    se[i] = new ReliabilitySimpleReflectogramEventImpl(dis);
                return new ModelTraceAndEventsImpl(se, mf, deltaX);
            }
        };
		return dsReader;
	}
}
