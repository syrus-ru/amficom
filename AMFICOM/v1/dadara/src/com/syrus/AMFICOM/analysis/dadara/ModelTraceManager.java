/*
 * $Id: ModelTraceManager.java,v 1.26 2005/03/24 14:27:11 saa Exp $
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
 * @version $Revision: 1.26 $, $Date: 2005/03/24 14:27:11 $
 * @module
 */
public class ModelTraceManager
{
	private static final long SIGNATURE_EVENTS = 3353520050119193102L;
	private static final long SIGNATURE_THRESH = 3353620050119193102L;
	public static final String CODENAME = "ModelTraceManager";

	private SimpleReflectogramEventImpl[] se; // not null
	private ModelFunction mf;
	private int traceLength;
	private ModelTrace[] thMTCache = null;
	private double deltaX = 1; // XXX

	private ModelTrace mt; // will just contain mt

	protected void invalidateThMTCache()
	{
		thMTCache = null;
	}

	// очищает записи кэша пороговых кривых key
	protected void emptyThMTEntry(int key)
	{
		if (thMTCache == null)
			thMTCache = new ModelTrace[] { null, null, null, null };
		thMTCache[key] = null;
	}

	// очищает записи кэша, зависящие от ключа key в порогах
	// (это key и Thresh.CONJ_KEY[key])
	protected void invalidateThMTByKey(int key)
	{
		emptyThMTEntry(key);
		emptyThMTEntry(Thresh.CONJ_KEY[key]);
	}

	protected boolean isThMFCacheValid(int key)
	{
		return thMTCache != null && thMTCache[key] != null;
	}

	// создать пороги
	private void createTH()
	{
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

	public ModelTraceManager(SimpleReflectogramEventImpl[] se, ModelFunction mf, double deltaX)
	{
		this.se = se;
		this.mf = mf;
		this.deltaX = deltaX;
		this.traceLength = calcTraceLength();
		this.mt = new ModelTraceImplMF(this.mf, this.traceLength);
		createTH();
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
		return mt;
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
		// Array elements are unmodifiable, so no need to clone them.
		return (SimpleReflectogramEvent[] )se.clone();
	}

	public ComplexReflectogramEvent[] getComplexEvents()
	{
		return ComplexReflectogramEvent.createEvents(se, mt);
	}

	public void setDeltaX(double deltaX)
	{
		this.deltaX = deltaX;
	}

	public double getDeltaX()
	{
		return deltaX;
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
			if (th instanceof ThreshDX)
				return ((ThreshDX )th).getDX(key);
			else
				return ((ThreshDY )th).getDY(key);
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
				((ThreshDX )th).setDX(key, val);
			}
			else
				((ThreshDY )th).setDY(key, value);
			th.arrangeLimits(key);
			invalidateThMTCache(); // сбрасываем кэш всех кривых
		}
		/**
		 * increase thresholds;
		 * for dx uses +1, for dy uses the default value
		 */
		public void increaseValues()
		{
			if (th instanceof ThreshDX)
				((ThreshDX)th).changeAllBy(1);
			else
				((ThreshDY)th).changeAllBy(0.1);
			invalidateThMTCache();
		}
		/**
		 * increase thresholds;
		 * for dx uses -1, for dy uses the default value
		 */
		public void decreaseValues()
		{
			if (th instanceof ThreshDX)
				((ThreshDX)th).changeAllBy(-1);
			else
				((ThreshDY)th).changeAllBy(-0.1);
			invalidateThMTCache();
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
					((ThreshDX )th).getRise()
						? ThreshEditor.TYPE_DXF
						: ThreshEditor.TYPE_DXF,
					th));
			}
			if (th instanceof ThreshDY)
			{
				if (((ThreshDY )th).getTypeL())
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
			// FIXME - remove
			//for (int i = 0; i < 500; i++)
			//	mf.copy().changeByThresh(tDX, tDY, key);

			ModelFunction tmp = mf.copy();
			tmp.changeByThresh(tDX, tDY, key);
			thMt = new ModelTraceImplMF(tmp, traceLength);
			emptyThMTEntry(key);
			thMTCache[key] = thMt;
		}
		return thMt;
	}

	public void updateUpperThreshToContain(double[] yTop)
	{
		CoreAnalysisManager.nExtendThreshToCoverCurve(mt.getYArray(), yTop,
			tDX, tDY, Thresh.SOFT_UP, Thresh.HARD_UP);
	}

	public void updateLowerThreshToContain(double[] yTop)
	{
		CoreAnalysisManager.nExtendThreshToCoverCurve(mt.getYArray(), yTop,
			tDX, tDY, Thresh.SOFT_DOWN, Thresh.HARD_DOWN);
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
			dos.writeDouble(deltaX);
			dos.writeInt(se.length);
			for (int i = 0; i < se.length; i++)
				se[i].writeToDOS(dos);
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
		public void release();
	}

	protected class ThresholdHandleDX
	implements ThresholdHandle 
	{
		private ThreshDX th;
		private int key;
		private double dxFrac = 0; // сохраняем дробную часть dx 
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
			dx += dxFrac;
			double desiredValue = th.getDX(key) + dx;
			th.setDX(key, desiredValue);
			dxFrac = desiredValue - th.getDX(key);
			posX += dx - dxFrac;
			if (dxFrac != dx)
				invalidateThMTByKey(key);
		}
		public int getX()
		{
			return posX;
		}
		public double getY()
		{
			return posY;
		}
		public void release()
		{
			th.arrangeLimits(key);
			invalidateThMTCache(); // сбрасываем кэш всех кривых
		}
	}

	protected class ThresholdHandleDY
	implements ThresholdHandle 
	{
		private ThreshDY th;
		private int key;
		private double dyFrac = 0; // сохраненная дробная часть порога, не ложащаяся на допускаемую порогом сетку
		protected int posX;
		protected double posY;

		protected ThresholdHandleDY(int thId, int key, int posX, double posY)
		{
			this.th = tDY[thId];
			this.key = key;
			int posMin = th.xMin;
			int posMax = th.xMax;
			if (th.getTypeL() && thId > 0 && thId < tDY.length - 1)
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
			dy += dyFrac;
			double desiredValue = th.getDY(key) + dy;
			th.setDY(key, desiredValue);
			dyFrac = desiredValue - th.getDY(key);
			posY += dy - dyFrac; // привязка к сетке значения самого порога
			if (dy != dyFrac)
				invalidateThMTByKey(key);
		}
		public int getX()
		{
			return posX;
		}
		public double getY()
		{
			return posY;
		}
		public void release()
		{
			th.arrangeLimits(key);
			invalidateThMTCache(); // сбрасываем кэш всех кривых
		}
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
		int[] keys = new int[] { Thresh.SOFT_UP, Thresh.SOFT_DOWN, Thresh.HARD_UP, Thresh.HARD_DOWN }; 
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
			int thId = mf.findResponsibleThreshDYID(tDX, tDY, bestKey, bestX);
			if (thId == -1)
				return null;
			ThresholdHandleDY handle = new ThresholdHandleDY(thId, bestKey, bestX, bestY);
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
