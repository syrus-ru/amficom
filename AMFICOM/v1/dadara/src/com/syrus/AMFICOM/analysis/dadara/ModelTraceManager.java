/*
 * $Id: ModelTraceManager.java,v 1.4 2005/02/08 11:46:27 saa Exp $
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
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author $Author: saa $
 * @version $Revision: 1.4 $, $Date: 2005/02/08 11:46:27 $
 * @module
 */
public class ModelTraceManager
{
	private static final long SIGNATURE_EVENTS = 3353520050119193101L;
	private static final long SIGNATURE_THRESH = 3353620050119193101L;

	private ReflectogramEvent[] re; // not null
	private SimpleReflectogramEvent[] se; // not null; must be kept in sync with re
	private ModelFunction mf;
	private int traceLength;
	private ModelTrace[] thMTCache = null;

	protected void invalidateThMTCache()
	{
		thMTCache = null;
	}

	protected void createVoidThMTCacheEntry(int key)
	{
		if (thMTCache == null)
			thMTCache = new ModelTrace[] { null, null, null, null };
		thMTCache[key] = null;
	}

	protected boolean isThMFCacheValid(int key)
	{
		return thMTCache != null && thMTCache[key] != null;
	}

	private void createSEandTH()
	{
		SimpleReflectogramEventImpl[] srei = new SimpleReflectogramEventImpl[re.length];
		for (int i = 0; i < re.length; i++)
			srei[i] = new SimpleReflectogramEventImpl(re[i].getBegin(), re[i].getEnd(), re[i].getEventType());
		this.se = srei;

		LinkedList thresholds = new LinkedList();
		Thresh last; // далее будет всегда указывать на текущий порог "линейного" типа
		thresholds.add(last = new Thresh(-1, 0, 0, 0)); // "C" coding style
		for (int i = 0; i < se.length; i++)
		{
			int evBegin = se[i].getBegin();
			int evEnd = se[i].getEnd();
			switch(se[i].getEventType())
			{
			case SimpleReflectogramEvent.LINEAR:
				last.xMax = evEnd;
				break;
			case SimpleReflectogramEvent.SPLICE:
				last.xMax = evBegin;
				thresholds.add(last = new Thresh(i, 0, evEnd, evEnd));
				break;
			case SimpleReflectogramEvent.CONNECTOR:
				last.xMax = evBegin;
				//int evCenter = (evBegin + evEnd) / 2;
				// сохраняем в объект Thresh начало и конец события;
				// положение его максимума будет уточнено *потом*, в native-коде
				thresholds.add(last = new Thresh(i, 1, evBegin, evEnd));
				thresholds.add(last = new Thresh(i, 0, evEnd, evEnd));
				break;
			}
		}
		tl = (Thresh[] )thresholds.toArray(new Thresh[thresholds.size()]);
		mf.fixThresh(tl);
	}

	private ModelTrace reMT;

	public static final String CODENAME = "ModelTraceManager";

	public ModelTraceManager(ReflectogramEvent[] re, ModelFunction mf)
	{
		this.re = re;
		this.mf = mf;
		createSEandTH();
		this.traceLength = calcTraceLength();
		this.reMT = new ModelTraceImplMF(this.mf, this.traceLength);
		
		// @todo: remove this profiler code
//		long dt1 = 0;
//		long dt2 = 0;
//		long dt3 = 0;
//		for (int j = 0; j < 10; j++)
//		{
//			long t0 = System.currentTimeMillis();
//			for (int i = 0; i < traceLength; i++)
//				reMT.getY(i);
//			long t1 = System.currentTimeMillis();
//			for (int i = 0; i < 100; i++)
//				reMT.getYArrayZeroPad(0, traceLength);
//			long t2 = System.currentTimeMillis();
//			for (int i = 0; i < 5000; i++)
//				reMT.getYArrayZeroPad(0, traceLength / 50);
//			long t3 = System.currentTimeMillis();
//			dt1 += t1 - t0;
//			dt2 += t2 - t1;
//			dt3 += t3 - t2;
//		}
//		System.err.println("MTM: profiler: dt1 = " + dt1 + "; dt2 = " + (dt2 / 100.0) + "; dt3 = " + (dt3 / 100.0));
	}

	private int calcTraceLength()
	{
		if (se.length == 0)
			return 0;
		else
			return se[se.length - 1].getEnd() + 1;
	}

	public ModelTrace getModelTrace()
	{
		return reMT;
	}

	public int getNEvents()
	{
		return se.length;
	}

	public SimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return se[nEvent];
	}

	public SimpleReflectogramEvent[] getSimpleEvents()
	{
		// Copy an array and all its references to protect se array.
		// se[i] are unmodifiable, so we need not clone them.
		return (SimpleReflectogramEvent[] )se.clone();
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

//	public void fixEventTypes(ModelTraceManager etalon, int delta)
//	{
//		throw new UnsupportedOperationException();
//		invalidateThMTCache();
//		// FIXME: just a copy-paste
//		// XXX: rude alg.: probable errors when end - begin < delta
//		// just a copy-paste from old analysis
//		if (re.length == etalon.re.length)
//		{
//			for (int i = 0; i < etalon.re.length; i++)
//			{
//				if (Math.abs(re[i].getBegin() - etalon.re[i].getBegin()) < delta
//						&& Math.abs(re[i].getEnd() - etalon.re[i].getEnd()) < delta)
//				{
//					re[i].setEventType(etalon.re[i].getEventType());
//				}
//			}
//		}
//	}

	/**
	 * Определяет значение порога в данной точке.
	 * В целях повышения производительности,
	 * рекомендуется также метод getThresholdMT  
	 * @param key номер пороговой кривой
	 * @param x x-координата (индекс)
	 * @return y-значение (дБ)
	 */
	public double getThresholdY(int key, int x)
	{
		ModelTrace th = getThresholdMT(key);
		return th.getY(x);
	}

	// для пользовательского интерфейса нужна команда изменения порогов для данного *события*
	// FIXME: нет ограничения на отрицательные значения порогов
	public void changeThresholdBy(int nEvent, int key, double dH, int dW)
	{
		createVoidThMTCacheEntry(key);
		ArrayList tlist = getAllThreshByNEvent(nEvent);
		for (int i = 0; i < tlist.size(); i++)
		{
			((Thresh )tlist.get(i)).dxL[key] += dW;
			((Thresh )tlist.get(i)).dxR[key] += dW;
			((Thresh )tlist.get(i)).values[key] += dH;
		}
	}

	// XXX: slow
	// XXX: inconvenient
	public int getEventByCoord(int x) // may be -1
	{
		int ret = -1;
		for (int i = 0; i < se.length; i++)
		{
			if (x >= se[i].getBegin() && x <= se[i].getEnd())
				ret = i;
		}
		return ret;
	}

	public class ThreshEditor
	{
		public static final int TYPE_A = 1;
		public static final int TYPE_L = 2;
		public static final int TYPE_DXF = 3;
		public static final int TYPE_DXT = 4;
		private static final int MAX_DX = 1000;
		private static final int MIN_DX = 0;
		private int type;
		private Thresh th;
		protected ThreshEditor(int type, Thresh th)
		{
			this.type = type;
			this.th = th;
		}
		public int getType()
		{
			return type;
		}
		public double getValue(int key)
		{
			if (type == TYPE_DXF)
				return th.dxL[key];
			if (type == TYPE_DXT)
				return th.dxR[key];
			return th.values[key];
		}
		public void setValue(int key, double value)
		{
			ModelTraceManager.this.createVoidThMTCacheEntry(key);
			if (type == TYPE_DXF)
			{
				th.dxL[key] = (int )value;
				if (th.dxL[key] > MAX_DX)
					th.dxL[key] = MAX_DX;
				if (th.dxL[key] < MIN_DX)
					th.dxL[key] = MIN_DX;
			}
			else if (type == TYPE_DXT)
			{
				System.err.println("TYPE_DXT change");
				th.dxR[key] = (int )value;
				if (th.dxR[key] > MAX_DX)
					th.dxR[key] = MAX_DX;
				if (th.dxR[key] < MIN_DX)
					th.dxR[key] = MIN_DX;
			}
			else
				th.values[key] = value;
		}
	}

	public ThreshEditor[] getThreshEditor(int nEvent)
	{
		//return re[nEvents].getThreshold();

		ArrayList tlist = getAllThreshByNEvent(nEvent);
		ArrayList ret = new ArrayList();

		for (int i = 0; i < tlist.size(); i++)
		{
			Thresh th = ((Thresh )tlist.get(i));
			if (th.typeId == 0)
			{
				ret.add(new ThreshEditor(ThreshEditor.TYPE_A, th));
			}
			else
			{
				ret.add(new ThreshEditor(ThreshEditor.TYPE_DXF, th));
				ret.add(new ThreshEditor(ThreshEditor.TYPE_DXT, th));
				ret.add(new ThreshEditor(ThreshEditor.TYPE_L, th));
			}
		}
		return (ThreshEditor[] )ret.toArray(new ThreshEditor[ret.size()]);
	}

	/**
	 * Выдает модельную кривую указанного порога.
	 * Обеспечивает необходимое кэширование.
	 * @param key код порога (Threshold)
	 * @return модельная кривая запрошенного порога 
	 */
	public ModelTrace getThresholdMT(int key)
	{
		ModelTrace thMt;
		if (isThMFCacheValid(key))
		{
			thMt = thMTCache[key];
		}
		else
		{
			ModelFunction tmp = mf.copy();
			tmp.changeByThresh(tl, key);
			thMt = new ModelTraceImplMF(tmp, traceLength);
			createVoidThMTCacheEntry(key);
			thMTCache[key] = thMt;
		}
		return thMt;
	}

	public void setThreshold(int nEvent, Threshold th) // FIXME -- remove
	{
		throw new UnsupportedOperationException();
		//invalidateThMFCache();
		//re[nEvent].setThreshold(th);
	}

	// FIXME - remove
	public void changeThresholdType(int nEvent, int thresholdType, double[] y)
	{
		throw new UnsupportedOperationException();
		//createVoidThMFCacheEntry(key);
		//re[nEvent].changeThresholdType(thresholdType, y);
	}

	public void setThresholds(Threshold[] th) // FIXME -- remove
	{
		throw new UnsupportedOperationException();
		//invalidateThMFCache();
//		for (int i = 0; i < re.length && i < th.length; i++)
//			re[i].setThreshold(th[i]);
	}

	public void setDefaultThreshold(int nEvent)
	{
		throw new UnsupportedOperationException(); // FIXME
		//invalidateThMFCache();
		//re[nEvent].setThreshold(new Threshold());
	}

	public byte[] eventsAndTraceToByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeLong(SIGNATURE_EVENTS);
			mf.writeToDOS(dos);
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

	//@todo: throw exceptions when there are errors
	public static ModelTraceManager eventsAndTraceFromByteArray(byte[] bar)
	{
		//throw new UnsupportedOperationException(); // FIXME
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			long signature = dis.readLong();
			if (signature != SIGNATURE_EVENTS)
				throw new SignatureMismatchException();
			ModelFunction mf = ModelFunction.createFromDIS(dis);
			int len = dis.readInt();
			ReflectogramEvent[] re = new ReflectogramEvent[len];
			for (int i = 0; i < len; i++)
				re[i] = ReflectogramEvent.readFromDIS2(dis);
			ReflectogramEvent.createLeftRightLinks(re);
			return new ModelTraceManager(re, mf);
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

	public byte[] toThresholdsByteArray()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeLong(SIGNATURE_THRESH);
			Thresh.writeArrayToDOS(tl, dos);
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
		// XXX: что происходит в MTM в случае замены tl посторонним tl?
		// XXX: exception handling
		invalidateThMTCache();
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			long signature = dis.readLong();
			if (signature != SIGNATURE_THRESH)
				throw new SignatureMismatchException();
			Thresh[] tl2 = Thresh.readArrayFromDIS(dis);
			if (this.tl.length != tl2.length) 
				throw new SignatureMismatchException();
			this.tl = tl2; 
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

	private Thresh[] tl;

	public class ThresholdHandle
	{
		private Thresh th;
		private int key;
		protected int posX;
		protected double posY;
		protected ThresholdHandle(Thresh th, int key, int posX, double posY)
		{
			this.th = th;
			this.key = key;
			System.err.println("ThresholdHandle(): type=" + th.typeId + " X=" + posX + " L=" + th.xMin + " R=" + th.xMax);
			if (posX < th.xMin)
				posX = th.xMin;
			if (posX > th.xMax)
				posX = th.xMax;
			this.posX = posX;
			this.posY = posY;
		}
		public void moveBy(int dx, double dy)
		{
			// dx is ignored now
			createVoidThMTCacheEntry(key);
			posY += dy;
			th.values[key] += dy;
		}
		public int getX()
		{
			return posX;
		}
		public double getY()
		{
			return posY;
		}
	}

	private Thresh getNearestThreshByX(int x0)
	{
		for (int i = 0; i < tl.length - 1; i++)
		{
			int thisEnd = tl[i].xMax;
			int nextBegin = tl[i + 1].xMin;
			if (x0 < (thisEnd + nextBegin) / 2)
			{
				System.err.println("getNearestThresholdByX: returning " + i);
				return tl[i];
			}
		}
		// FIXME: empty tl will cause NullPointerException
		return tl[tl.length - 1];
	}

	private ArrayList getAllThreshByNEvent(int nEvent)
	{
		ArrayList ret = new ArrayList();
		for (int i = 0; i < tl.length; i++)
		{
			if (tl[i].eventId == -1 && nEvent == 0
					|| tl[i].eventId == nEvent)
				ret.add(tl[i]);
		}
		// если это линейное событие без собственных порогов,
		// то надо выбрать покрывающий его порог
		if (ret.size() == 0)
		{
			for (int i = 1; i < tl.length; i++)
				if (tl[i].eventId > nEvent)
				{
					ret.add(tl[i - 1]);
					break;
				}
		}
		return ret;
	}

	/**
	 * Выдает handle для изменения значения порогов мышью.
	 * Само определяет, какой порог какого события будет изменяться.
	 * Данная реализации содержит некоторые подстроечные параметры,
	 * специфичные для рефлектометрии.
	 * @param x0 модельная x-координата (разверности индекса, но вещ.)
	 * @param y0 модельная y-координата (дБ)
	 * @param xCapture радиус захвата кривой мышью по горизонтали
	 * @param yCapture радиус захвата кривой мышью по вертикали
	 * @param prioFactor поправка на приоритет алармов,
	 *     =0: приоритетов нет; =1: 100% приоритет HARD-алармов
	 * @param button номер кнопки мыши, 0=LMB, 1=RMB, пока не используется. 
	 * @return handle либо null
	 */
	public ThresholdHandle getThresholdHandle(double x0, double y0, double xCapture, double yCapture, double prioFactor, int button)
	{
		Thresh th = getNearestThreshByX((int )x0);

		if (xCapture <= 0.1)
			xCapture = 0.1;
		if (yCapture <= 1e-4)
			yCapture = 1e-4; // XXX: специфичный для рефлектометрии параметр
		if (xCapture > 5000)
			xCapture = 5000; // XXX
		
		int xRange = (int )(xCapture + 1);

		// find nearest threshold curve; UP2/DOWN2 takes precedence over UP1/DOWN1
		int key = 0;
		double bestDR = -1;
		int bestX = 0;
		int[] keys = new int[] { Threshold.UP1, Threshold.DOWN1, Threshold.UP2, Threshold.DOWN2 }; 
		for (int k = 0; k < 4; k++)
		{
			int xL = (int )x0 - xRange;
			int W = xRange * 2;
			double[] yArr = getThresholdMT(keys[k]).getYArray(xL, W + 1);
			double xScale = xCapture;
			double yScale = yCapture;
			for (int i = 0; i <= W; i++)
			{
				double dX = (x0 - xL - i) / xScale;
				double dY = (y0 - yArr[i]) / yScale;
				double scalar;
				double dR;
				if (i == W) // последняя точка - рассматриваем расстояние только до нее самой
				{
					dR = Math.sqrt(dX * dX + dY * dY);
					scalar = 1;
				}
				else
				{
					double aX = 1.0 / xScale;
					double aY = (yArr[i + 1] - yArr[i]) / yScale;
					double aSqr = aX * aX + aY * aY;
					scalar = (dX * aX + dY * aY) / aSqr;
					if (scalar <= 0) // левая точка ближе
					{
						dR = Math.sqrt(dX * dX + dY * dY);
					}
					else if (scalar < 1) // ближе к интервалу чем к узлам
					{
						double vector = dX * aY - dY * aX;
						dR = Math.abs(vector) / Math.sqrt(aSqr);
					}
					else
						continue; // случай, когда мы ближе к правой точке будет рассмотрен на следующей итерации
				}

				if (dR <= 1 && (dR <= bestDR + prioFactor || bestDR < 0))
				{
					key = keys[k];
					bestDR = dR;
					// определяем вершину, ближайшую к ближайшей точек
					bestX = xL + i;
					if (scalar > 0.5)
						bestX++;
				}
			}
		}
		if (bestDR < 0)
			return null;

		double bestY = getThresholdY(key, bestX);
		ThresholdHandle handle = new ThresholdHandle(th, key, bestX, bestY);
		handle.posY = getThresholdY(key, handle.posX);
		return handle;
	}
}
