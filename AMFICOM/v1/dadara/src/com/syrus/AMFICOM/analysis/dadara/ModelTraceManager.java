/*
 * $Id: ModelTraceManager.java,v 1.2 2005/01/26 14:59:25 saa Exp $
 * 
 * Copyright © Syrus Systems.
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
 * @version $Revision: 1.2 $, $Date: 2005/01/26 14:59:25 $
 * @module
 */
public class ModelTraceManager
{
	private static final long SIGNATURE = 3353520050119193100L;

	// @todo: разобраться, м ли re б null
	private ReflectogramEvent[] re;

	private ModelTrace reMT = new ModelTrace()
	{
		public double getY(final int x)
		{
			return ModelTraceManager.this.getY(x);
		}

		public int getLength()
		{
			return ModelTraceManager.this.getTraceLength();
		}
	};

	public static final String CODENAME = "ModelTraceManager";

	public ModelTraceManager(ReflectogramEvent[] re)
	{
		this.re = re;
	}

	public int getTraceLength()
	{
		if (re == null || re.length == 0)
			return 0;
		else
			return re[re.length - 1].getEnd() + 1;
	}

	public double getY(int x) { return ReflectogramMath.getEventAmplitudeAt(x, re); }

	public double[] getYArray(int x0, int n)
	{
		// if x0 + n > trace length, fill with zero
		double[] ret = new double[n];
		for (int i = 0; i < n; i++)
			ret[i] = getY(x0 + i); // XXX: slow
		return ret;
	}

	public ModelTrace getModelTrace()
	{
		return reMT;
	}

	public int getNEvents()
	{
		if (re != null)
			return re.length;
		else return 0;
	}

	public SimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return getComplexEvent(nEvent);
	}

	public SimpleReflectogramEvent[] getSimpleEvents()
	{
		return getComplexEvents();
	}

	public ComplexReflectogramEvent getComplexEvent(int nEvent)
	{
		return new ComplexReflectogramEvent(re[nEvent]);
	}

	public ComplexReflectogramEvent[] getComplexEvents()
	{
		ComplexReflectogramEvent[] ret = new ComplexReflectogramEvent[re.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = new ComplexReflectogramEvent(re[i]);
		return ret;
	}

	public void setDeltaX(double deltaX)
	{
		for(int i=0; i<re.length; i++)
			re[i].setDeltaX(deltaX);
	}

	public double getDeltaX()
	{
		if (re.length > 0)
			return re[0].getDeltaX();
		else
			return 1.0; // FIXME
	}

	public void fixEventTypes(ModelTraceManager etalon, int delta)
	{
		// XXX: rude alg.: probable errors when end - begin < delta
		// just a copy-paste from old analysis
		if (re.length == etalon.re.length)
		{
			for (int i = 0; i < etalon.re.length; i++)
			{
				if (Math.abs(re[i].getBegin() - etalon.re[i].getBegin()) < delta
						&& Math.abs(re[i].getEnd()
								- etalon.re[i].getEnd()) < delta)
				{
					re[i].setEventType(etalon.re[i].getEventType());
				}
			}
		}
	}

	public double getThresholdY(int key, int x, int nEvent)
	{
		return re[nEvent].getThresholdReflectogramEvent(key).refAmplitude(x);
	}
	public double getThresholdY(int key, int x)
	{
		int nEvent = getEventByCoord(x);
		return getThresholdY(key, x, nEvent);
	}
	public int getThresholdType(int nEvent)
	{
		return re[nEvent].getThresholdType();
	}
	public void changeThresholdBy(int nEvent, int key, double dA, double dC, double dX, double dL)
	{
		re[nEvent].getThreshold().changeThresholdBy(dA, dC, dX, dL, key);
	}

	// XXX: slow
	// XXX: inconvenient
	public int getEventByCoord(int x) // may be -1
	{
		int ret = -1;
		for (int i = 0; i < re.length; i++)
		{
			if (x >= re[i].getBegin() && x <= re[i].getEnd())
				ret = i;
		}
		return ret;
	}

	public boolean getLeftLink(int nEvent)
	{
		return re[nEvent].getLeftLink();
	}

	// Setter-метод должен возвращать void!
	public boolean setLeftLink(int nEvent, boolean value)
	{
		return re[nEvent].setLeftLink(value);
	}

	public Threshold getThresholdObject(int nEvents)
	{
		return re[nEvents].getThreshold();
	}

	public ModelTrace getThresholdMF(int key)
	{
		return new ModelTraceImplREOld(ReflectogramEvent.getThresholdReflectogramEvents(re, key)); //????
	}

	/**
	 * @deprecated
	 */
	public ReflectogramEvent getEvent(int i)
	{
		return re[i];
	}

	public int getEventBegin(int i)
	{
		return re[i].getBegin();
	}

	public int getEventEnd(int i)
	{
		return re[i].getEnd();
	}
	public int getEventType(int i)
	{
		return re[i].getEventType();
	}

	public void setThreshold(int nEvent, Threshold th) // disapproved
	{
		re[nEvent].setThreshold(th);
	}

	public void changeThresholdType(int nEvent, int thresholdType, double[] y)
	{
		re[nEvent].changeThresholdType(thresholdType, y);
	}

	public void setThresholds(Threshold[] th) // disapproved
	{
		for (int i = 0; i < re.length && i < th.length; i++)
			re[i].setThreshold(th[i]);
	}

	public void setDefaultThreshold(int nEvent)
	{
		re[nEvent].setThreshold(new Threshold());
	}

	public byte[] toEventsByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeLong(SIGNATURE);
			dos.writeInt(re.length);
			for (int i = 0; i < re.length; i++)
				re[i].writeToDOS(dos);
			return baos.toByteArray();
		} catch (IOException e)
		{
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return new byte[0]; //null // XXX
		}
	}

	//@todo: throw exceptions
	public static ModelTraceManager fromEventsByteArray(byte[] bar)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			long signature = dis.readLong();
			if (signature != SIGNATURE)
				throw new SignatureMismatchException();
			int len = dis.readInt();
			ReflectogramEvent[] re = new ReflectogramEvent[len];
			for (int i = 0; i < len; i++)
				re[i] = ReflectogramEvent.readFromDIS2(dis);
			ReflectogramEvent.createLeftRightLinks(re);
			return new ModelTraceManager(re);
		}
		catch (IOException e)
		{
			// FIXME: what to do?
			// we should not catch exceptions here?
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return new ModelTraceManager(new ReflectogramEvent[0]); // FIXME
		}
		catch (SignatureMismatchException e)
		{
			System.out.println("SignatureMismatchException caught: " + e);
			e.printStackTrace();
			return new ModelTraceManager(new ReflectogramEvent[0]); // FIXME
		}
	}

	public byte[] toThresholdsByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			Threshold[] th = ReflectogramEvent.getThresholds(re);

//			 на данный момент ReflectogramEvent.getThresholds нули не возвращает;
//			 если есть не все пороги, считаем что их и нет
//			for (int i = 0; i < re.length; i++)
//				if (th[i] == null)
//					return new byte[0];
			dos.writeLong(SIGNATURE);
			Threshold.writeArrayToDOS(th, dos);
			return baos.toByteArray();
		} catch (IOException e)
		{
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return new byte[0]; //null // XXX
		}
	}

	public void setThresholdsFromByteArray(byte[] bar)
	{
		// XXX: exception handling
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			long signature = dis.readLong();
			if (signature != SIGNATURE)
				throw new SignatureMismatchException();
			Threshold[] th = Threshold.readArrayFromDIS(dis);
			if (th.length != re.length)
				throw new SignatureMismatchException();
			setThresholds(th);
			//ReflectogramEventOld.createLeftRightLinks(re);
		}
		catch (IOException e)
		{
			// FIXME: what to do?
			// we should not catch exceptions here?
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
		}
		catch (SignatureMismatchException e)
		{
			System.out.println("SignatureMismatchException caught: " + e);
			e.printStackTrace();
		}
	}
}
