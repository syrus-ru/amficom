/*-
 * $Id: ModelTraceAndEvents.java,v 1.1 2005/03/31 11:46:23 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/03/31 11:46:23 $
 * @module
 */
public class ModelTraceAndEvents
{
	protected static final long SIGNATURE_EVENTS = 3353520050119193102L;
	protected static final long SIGNATURE_THRESH = 3353620050119193102L;

	private SimpleReflectogramEventImpl[] se; // not null
	private ModelFunction mf; // not null
	private int traceLength;
	private double deltaX = 1; // XXX
	private ModelTrace mt; // will just contain mt

	// XXX: пока создаем MTM целиком, а не MTAE. Ќаверное, надо пользоватьс€ 'фабрикой'
	public static ModelTraceManager eventsAndTraceFromByteArray(byte[] bar)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			long signature = dis.readLong();
			if (signature != SIGNATURE_EVENTS)
				throw new SignatureMismatchException();
			ModelFunction mf = ModelFunction.createFromDIS(dis);
			double deltaX = dis.readDouble();
			int len = dis.readInt();
			SimpleReflectogramEventImpl[] se = new SimpleReflectogramEventImpl[len];
			for (int i = 0; i < len; i++)
				se[i] = SimpleReflectogramEventImpl.createFromDIS(dis);
			return new ModelTraceManager(se, mf, deltaX);
		}
		catch (IOException e)
		{
			// FIXME: what to do?
			// we should not catch exceptions here?
			System.out.println("IOException caught, wanna die: " + e);
			e.printStackTrace();
			return null; // FIXME
		}
		catch (SignatureMismatchException e)
		{
			System.out.println("SignatureMismatchException caught, wanna die: " + e);
			e.printStackTrace();
			return null; // FIXME
			//return new ModelTraceManager(new ReflectogramEvent[0]); // FIXME
		}
	}

	protected ModelTraceAndEvents(SimpleReflectogramEventImpl[] se,
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

	protected SimpleReflectogramEventImpl[] getSE()
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

	public byte[] eventsAndTraceToByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeLong(SIGNATURE_EVENTS);
			getMF().writeToDOS(dos);
			dos.writeDouble(getDeltaX());
			dos.writeInt(getSE().length);
			for (int i = 0; i < getSE().length; i++)
				getSE()[i].writeToDOS(dos);
			return baos.toByteArray();
		} catch (IOException e)
		{
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return new byte[0]; //null // XXX
		}
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
}
