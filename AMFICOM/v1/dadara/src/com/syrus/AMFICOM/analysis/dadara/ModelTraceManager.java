/*
 * $Id: ModelTraceManager.java,v 1.11 2005/02/24 08:54:01 saa Exp $
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

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;

/**
 * @author $Author: saa $
 * @version $Revision: 1.11 $, $Date: 2005/02/24 08:54:01 $
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

	// очищает записи кэша, зависящие от ключа key в порогах
	// (это key и Thresh.CONJ_KEY[key])
	protected void invalidateThMTByKey(int key)
	{
		if (thMTCache == null)
			thMTCache = new ModelTrace[] { null, null, null, null };
		thMTCache[key] = null;
		thMTCache[Thresh.CONJ_KEY[key]] = null;
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
		thresholds.add(last = new ThreshDY(-1, false, 0, 0)); // "C" coding style
		for (int i = 0; i < se.length; i++)
		{
			int evBegin = se[i].getBegin();
			int evEnd = se[i].getEnd();
			switch(se[i].getEventType())
			{
			case SimpleReflectogramEvent.LINEAR:
				last.xMax = evEnd;
				last.eventId1 = i;
				break;
			case SimpleReflectogramEvent.GAIN:
				last.xMax = evBegin;
				last.eventId1 = i;
				thresholds.add(new ThreshDX(i, evBegin, evEnd, true));
				thresholds.add(last = new ThreshDY(i, false, evEnd, evEnd));
				break;
			case SimpleReflectogramEvent.LOSS:
				last.xMax = evBegin;
				last.eventId1 = i;
				thresholds.add(new ThreshDX(i, evBegin, evEnd, false));
				thresholds.add(last = new ThreshDY(i, false, evEnd, evEnd));
				break;
			case SimpleReflectogramEvent.REFLECTIVE:
				int[] pos = CoreAnalysisManager.getConnectorMinMaxMin(mf, evBegin, evEnd);
				evBegin = pos[0];
				int evCenter = pos[1];
				evEnd = pos[2];
				last.xMax = evBegin;
				last.eventId1 = i;
				thresholds.add(new ThreshDX(i, evBegin, evCenter, true));
				thresholds.add(new ThreshDY(i, true, evCenter, evCenter));
				thresholds.add(new ThreshDX(i, evCenter, evEnd, false));
				thresholds.add(last = new ThreshDY(i, false, evEnd, evEnd));
				//System.err.println("REFLECTIVE: event #" + i + " begin=" + evBegin + " center=" + evCenter + " end=" + evEnd);
				break;
			}
		}
		Thresh[] tl = (Thresh[] )thresholds.toArray(new Thresh[thresholds.size()]);
		setTL(tl);
	}

	private void setTL(Thresh tl[])
	{
		tL = tl;
		// формируем отдельно списки tDX и tDY
		LinkedList thresholds = new LinkedList();
		for (int i = 0; i < tL.length; i++)
		{
			if (tL[i] instanceof ThreshDX)
				thresholds.add(tL[i]);
		}
		tDX = (ThreshDX[] )thresholds.toArray(new ThreshDX[thresholds.size()]);

		thresholds = new LinkedList();
		for (int i = 0; i < tL.length; i++)
		{
			if (tL[i] instanceof ThreshDY)
				thresholds.add(tL[i]);
		}
		tDY = (ThreshDY[] )thresholds.toArray(new ThreshDY[thresholds.size()]);
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
		throw new UnsupportedOperationException();
//		invalidateThMTByKey(key);
//		ArrayList tlist = getAllThreshByNEvent(nEvent);
//		for (int i = 0; i < tlist.size(); i++)
//		{
//			throw new UnsupportedOperationException();
//			//((Thresh )tlist.get(i)).dX[key] += dW; // FIXME
//			//((Thresh )tlist.get(i)).values[key] += dH;
//		}
	}

	/**
	 * Возвращает номер события, соответствующего данному
	 * иксу. Если x попадает на границу двух событий,
	 * то выбор левого или правого зависит от реализации.
	 * Если x не попадает ни на одно событие,
	 * то возвращает -1.
	 * <p> Относительно медленный метод и не очень удобный. 
	 */
	public int getEventByCoord(int x)
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
		private static final int MIN_DX = -1000;
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
			if (type == TYPE_DXF || type == TYPE_DXT)
				return ((ThreshDX )th).dX[key];
			else
				return ((ThreshDY )th).values[key];
		}
		public void setValue(int key, double value)
		{
			ModelTraceManager.this.invalidateThMTByKey(key);
			if (type == TYPE_DXF || type == TYPE_DXT)
			{
				int val = (int )value;
				if (val > MAX_DX)
					val = MAX_DX;
				if (val < MIN_DX)
					val = MIN_DX;
				((ThreshDX )th).dX[key] = val;
			}
			else
				((ThreshDY )th).values[key] = value;
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
			if (th instanceof ThreshDX)
			{
				ret.add(new ThreshEditor(
					((ThreshDX )th).isRise
						? ThreshEditor.TYPE_DXF
						: ThreshEditor.TYPE_DXF,
					th));
			}
			if (th instanceof ThreshDY)
			{
				if (((ThreshDY )th).typeL)
					ret.add(new ThreshEditor(ThreshEditor.TYPE_L, th));
				else
					ret.add(new ThreshEditor(ThreshEditor.TYPE_A, th));
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
			tmp.changeByThresh(tDX, tDY, key);
			thMt = new ModelTraceImplMF(tmp, traceLength);
			invalidateThMTByKey(key);
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
			Thresh.writeArrayToDOS(tL, dos);
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
			if (this.tL.length != tl2.length) 
				throw new SignatureMismatchException();
			setTL(tl2); 
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

	protected Thresh[] tL;
	protected ThreshDX[] tDX;
	protected ThreshDY[] tDY;

	public interface ThresholdHandle
	{
		public void moveBy(double dx, double dy);
		public int getX();
		public double getY();
	}

	protected class ThresholdHandleDX
	implements ThresholdHandle 
	{
		private ThreshDX th;
		private int key;
		private double dxFrac = 0; // сохраняем дробную часть dx, еще не доставленную до th 
		protected int posX;
		protected double posY;
		protected ThresholdHandleDX(int thId, int key, int posX, double posY)
		{
			this.th = tDX[thId];
			this.key = key;
			this.posX = posX;
			this.posY = posY;
		}
		public void moveBy(double dx, double dy) // dy is ignored
		{
			dxFrac += dx;
			dx = Math.round(dxFrac);
			dxFrac -= dx;
			//System.err.println("THDX: moveBy: dx=" + dx + "; dy=" + dy);
			invalidateThMTByKey(key);
			posX += dx;
			th.dX[key] += dx;
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

	protected class ThresholdHandleDY
	implements ThresholdHandle 
	{
		private ThreshDY th;
		private int key;
		private double dyGrid; // точность представления dy
		private double dyFrac; // сохраненная дробная часть порога, не ложащаяся на сетку  1/1000 дБ
		protected int posX;
		protected double posY;
		protected ThresholdHandleDY(int thId, int key, int posX, double posY, double dyGrid)
		{
			this.th = tDY[thId];
			this.key = key;
			this.dyGrid = dyGrid;
			int posMin = th.xMin;
			int posMax = th.xMax;
			if (th.typeL && thId > 0 && thId < tDY.length - 1)
			{
				// уточняем положение точки привязки по ширине 98% максимума кривой
				posMin = tDY[thId - 1].xMax;
				posMax = tDY[thId + 1].xMin;
				if (posMin < posMax)
				{
					// получаем интересующий нас участок кривой
					int x0 = posMin;
					double[] yArr = getThresholdMT(key).getYArray(x0, posMax - posMin + 1);
					// выбираем пороговый уровень
					double level = 0.995; // XXX - подстроечный параметр для UI
					double yMax = ReflectogramMath.getArrayMax(yArr);
					double yLVal = getThresholdY(key, posMin);
					double yRVal = getThresholdY(key, posMax);
					double yLCut = yLVal + (yMax - yLVal) * level;
					double yRCut = yRVal + (yMax - yRVal) * level;
					// определяем начало и конец максимума по выбранному уровню
					int i;
					for (i = posMin; i <= posMax; i++)
						if (yArr[i - x0] > yLCut)
							break;
					posMin = i;
					for (i = posMax; i > posMin; i--)
						if (yArr[i - x0] > yRCut)
							break;
					posMax = i;
					// на случай, если кривая опущена ниже смежных порогов,
					// отключаем ограничение координаты
					// XXX: срабатывание не гарантировано, но вроде обычно срабатывает.
					// Если не сработает - это коснется только удобства GUI, но не функциональности.
					if (yLVal >= yMax)
						posMin = th.xMin;
					if (yRVal >= yMax)
						posMax = th.xMax;
				}
			}
			if (posX < posMin)
				posX = posMin;
			if (posX > posMax)
				posX = posMax;
			this.posX = posX;
			this.posY = posY;
		}
		public void moveBy(double dx, double dy) // dx is ignored
		{
			// привязка к сетке указанного шага (тип., 0.001 дБ)
			if (dyGrid > 0)
			{
				dyFrac += dy;
				dy = Math.round(dyFrac / dyGrid) * dyGrid;
				dyFrac -= dy;
			}
			invalidateThMTByKey(key);
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

	// определяем, к какому DY-порогу лучше относится данная точка
	// при выборе между A и A граница порога - посередине между порогами,
	// при выборе между A и L - по уровню Y = (Y_A + Y_L)/2
	private int getNearestThreshDYByX(int key, int x)
	{
		for (int i = 0; i < tDY.length - 1; i++)
		{
			int thisEnd = tDY[i].xMax;
			int nextBegin = tDY[i + 1].xMin;
			if (x > nextBegin)
				continue;
			int separator;
			boolean isAtoL = tDY[i].typeL || tDY[i + 1].typeL;
			if (isAtoL == false)
			{
				// между порогами A-A или DX-DX
				// устанавливаем границу раздела посередине
				separator = (thisEnd + nextBegin) / 2;
			}
			else
			{
				// между порогами, A-L или L-A
				// ищем границу раздела по уровню 1/2 высоты
				int x0 = thisEnd;
				int N = nextBegin - thisEnd;
				double[] yArr = getThresholdMT(key).getYArray(x0, N);
				double median = (yArr[0] + yArr[N - 1]) / 2.0;
				separator = x0;
				for (int k = 0; k < N; k++)
					if (yArr[k] <= median ^ median <= yArr[0])
						separator++;
			}
			if (x < separator)
			{
				return i;
			}
		}
		// note: empty tDY would cause return of -1
		return tDY.length - 1;
	}

	private ArrayList getAllThreshByNEvent(int nEvent)
	{
		ArrayList ret = new ArrayList();
		for (int i = 0; i < tL.length; i++)
		{
			if (tL[i].eventId0 <= nEvent && tL[i].eventId1 >= nEvent)
				ret.add(tL[i]);
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
		if (xCapture <= 0.1)
			xCapture = 0.1;  // XXX
		if (xCapture > 5000)
			xCapture = 5000; // XXX
		if (yCapture <= 1e-4)
			yCapture = 1e-4; // XXX: специфичный для рефлектометрии параметр

		int xRange = (int )(xCapture + 1);

		// Определяем ближайшую пороговую кривую и ближайшую точку на этой кривой
		// UP2/DOWN2 takes precedence over UP1/DOWN1
		double bestDR = 2; // наибольшее расстояние=1; 2 => еще не найдено  
		int bestKey = 0;
		int bestX = 0;
		int[] keys = new int[] { Threshold.UP1, Threshold.DOWN1, Threshold.UP2, Threshold.DOWN2 }; 
		for (int k = 0; k < 4; k++)
		{
			int xL = (int )x0 - xRange;
			int W = xRange * 2;
			double[] yArr = getThresholdMT(keys[k]).getYArray(xL, W + 1);

			double xScale = xCapture;
			double yScale = yCapture;
			double curBestDR = 2; // >1 => еще не найдено
			int curBestX = 0;
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

				if (dR <= 1 && dR < curBestDR)
				{
					curBestDR = dR;
					// определяем вершину, ближайшую к точке отрезка, ближайшей к месту клика
					curBestX = xL + i;
					if (scalar > 0.5)
						curBestX++;
				}
			}
			if (curBestDR <= 1 && curBestDR <= bestDR + prioFactor)
			{
				bestKey = keys[k];
				bestDR = curBestDR;
				bestX = curBestX;
			}
		}

		if (bestDR > 1)
			return null;

		double bestY = getThresholdY(bestKey, bestX);

		if (button == 0)
		{
			int thId = getNearestThreshDYByX(bestKey, bestX);
			ThresholdHandleDY handle = new ThresholdHandleDY(thId, bestKey, bestX, bestY, 0.001);
			handle.posY = getThresholdY(bestKey, handle.posX);
			return handle;
		}
		else
		{
			int thId = mf.findResponsibleThreshDXID(tDX, tDY, bestKey, bestX);
			if (thId == -1)
				return null;
			ThresholdHandleDX handle = new ThresholdHandleDX(thId, bestKey, bestX, bestY);
			handle.posY = getThresholdY(bestKey, handle.posX);
			return handle;
		}
	}
}
