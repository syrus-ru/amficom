/*-
 * $Id: ModelTraceAndEventsImpl.java,v 1.3 2005/04/14 16:01:28 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/04/14 16:01:28 $
 * @module
 */
public class ModelTraceAndEventsImpl
implements ModelTraceAndEvents, DataStreamable
{
	protected static final long SIGNATURE_EVENTS = 3353520050119193102L;

	private ReliabilitySimpleReflectogramEventImpl[] se; // not null
	private ModelFunction mf; // not null
	private int traceLength;
	private double deltaX = 1; // XXX
	private ModelTrace mt; // will just contain mt

	private static DataStreamable.Reader dsReader = null; // DIS reader singleton-style object

	public ModelTraceAndEventsImpl(ReliabilitySimpleReflectogramEventImpl[] se,
			ModelFunction mf, double deltaX)
	{
		this.se = se;
		this.mf = mf;
		this.deltaX = deltaX;
		this.setTraceLength(calcTraceLength());
		mt = new ModelTraceImplMF(this.getMF(), this.getTraceLength());
	}

	public void setDeltaX(double deltaX)
	{
		setDeltaX(deltaX);
	}
	public double getDeltaX()
	{
		return deltaX;
	}
	public ModelTrace getModelTrace()
	{
		return mt;
	}

	protected ReliabilitySimpleReflectogramEventImpl[] getSE()
	{
		return se;
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
		if (getSE().length == 0)
			return 0;
		else
			return getSE()[getSE().length - 1].getEnd() + 1;
	}
	private void setTraceLength(int traceLength)
	{
		this.traceLength = traceLength;
	}

	public SimpleReflectogramEvent[] getSimpleEvents()
	{
		// Copy an array and all its references to protect se array.
		// Array elements are unmodifiable, so no need to clone them.
		return (SimpleReflectogramEvent[] )getSE().clone();
	}

	public ComplexReflectogramEvent[] getComplexEvents()
	{
		return ComplexReflectogramEvent.createEvents(getSE(), mt);
	}

	/**
	 * ¬озвращает номер событи€, соответствующего данному
	 * иксу. ≈сли x попадает на границу двух событий,
	 * то выбор левого или правого зависит от реализации.
	 * ≈сли x не попадает ни на одно событие,
	 * то возвращает -1.
	 * <p> ќтносительно медленный метод и не очень удобный. 
	 */
	public int getEventByCoord(int x)
	{
		int ret = -1;
		for (int i = 0; i < getSE().length; i++)
		{
			if (x >= getSE()[i].getBegin() && x <= getSE()[i].getEnd())
				ret = i;
		}
		return ret;
	}

	public int getNEvents()
	{
		return getSE().length;
	}

	public SimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return getSE()[nEvent];
	}

	public void writeToDOS(DataOutputStream dos) throws IOException
	{
		dos.writeLong(SIGNATURE_EVENTS);
		getMF().writeToDOS(dos);
		dos.writeDouble(getDeltaX());
		dos.writeInt(se.length);
		for (int i = 0; i < se.length; i++)
			se[i].writeToDOS(dos);
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
