/*
 * $Id: ModelTraceManager.java,v 1.62 2005/04/30 09:43:36 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;

/**
 * Объект этого класса заведует хранением
 * эталонной пары {события + модельная кривая},
 * порогов к событиями (пока нет) и модельной кривой (есть),
 * генерацией пороговых кривых и сохранением/восстановлением порогов.
 *
 * @author $Author: saa $
 * @version $Revision: 1.62 $, $Date: 2005/04/30 09:43:36 $
 * @module
 */
public class ModelTraceManager
implements DataStreamable, Cloneable
{
	protected static final long SIGNATURE_THRESH = 3353620050119193102L;
	public static final String CODENAME = "ModelTraceManager";

	protected ModelTraceAndEventsImpl mtae;
	private ModelTrace[] thMTCache = null;
	protected Thresh[] tL; // полный список порогов
	protected ThreshDX[] tDX; // список DX-порогов
	protected ThreshDY[] tDY; // список DY-порогов

	private static DataStreamable.Reader dsReader;

    public Object clone()
    throws CloneNotSupportedException
    {
        ModelTraceManager ret = (ModelTraceManager)super.clone();
        // remove cache data
        ret.thMTCache = null;
        // copy thresholds
        Thresh[] tLout = (Thresh[]) this.tL.clone(); // clone the holder
        for (int i = 0; i < tLout.length; i++)
            tLout[i] = (Thresh)tLout[i].clone(); // clone each threshold in array
        ret.setTL(tLout);
        // no need to clone mtae
        return ret;
    }

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
		Thresh last = null; // далее будет всегда указывать на текущий порог A-типа, либо null, если разрыв (в нач. р/г либо после н/ид соб)
		for (int i = 0; i < getSE().length; i++)
		{
			int evBegin = getSE()[i].getBegin();
			int evEnd = getSE()[i].getEnd();
			if (last == null)
				thresholds.add(last = new ThreshDY(i, false, evBegin, evEnd)); // "C" coding style

			switch(getSE()[i].getEventType())
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
			case SimpleReflectogramEvent.NOTIDENTIFIED:
				if (last.xMax > last.xMin)
					last.xMax = evBegin - 1;
				if (evEnd < evBegin)
					evEnd--;
				thresholds.add(new ThreshDY(i, false, evBegin, evEnd));
				last = null;
				break;
			case SimpleReflectogramEvent.DEADZONE:
				// fall through
			case SimpleReflectogramEvent.ENDOFTRACE:
				// fall through
			case SimpleReflectogramEvent.CONNECTOR:
				int[] pos = CoreAnalysisManager.getConnectorMinMaxMin(getMF(), evBegin, evEnd);
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

	private ModelFunction getMF()
	{
		return mtae.getMF();
	}

    /**
     * @return etalon's simple events
     */
	protected SimpleReflectogramEventImpl[] getSE()
	{
		return mtae.getRSE();
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

	/** создает MTM на основе эталонной пары {события + м.ф.} ModelTraceAndEvents
	 * 
	 * @param mtae опорная кривая
	 */
	public ModelTraceManager(ModelTraceAndEventsImpl mtae)
	{
		this.mtae = mtae;
		createTH();
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
				return ((ThreshDX )th).getDX(key) * mtae.getDeltaX(); // samples to meters
			else
				return ((ThreshDY )th).getDY(key);
		}
		public void setValue(int key, double value)
		{
			ModelTraceManager.this.invalidateThMTByKey(key);
			if (type == TYPE_DXF || type == TYPE_DXT)
			{
                // convert meters to samples
                if (mtae.getDeltaX() > 0)
                    value /= mtae.getDeltaX();
                // round in some (user-convenient?) way
                if (value > 0 && value < 1)
                    value = 1;
                if (value < 0 && value > -1)
                    value = -1;
                value = Math.round(value);
				// convert to int, limit and set
                int val = (int)value;
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

	public ThreshEditor[] getThreshEditors(int nEvent)
	{
		//return re[nEvents].getThreshold();

		Thresh[] tlist = getAllThreshByNEvent(nEvent);
		ArrayList ret = new ArrayList();

		for (int i = 0; i < tlist.length; i++)
		{
			Thresh th = tlist[i];
			if (th instanceof ThreshDX)
			{
				ret.add(new ThreshEditor(
					((ThreshDX )th).isRise()
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
			ModelFunction tmp = getMF().copy();
			tmp.changeByThresh(tDX, tDY, key);
			thMt = new ModelTraceImplMF(tmp, getTraceLength());
			emptyThMTEntry(key);
			thMTCache[key] = thMt;
		}
		return thMt;
	}

	private int getTraceLength()
	{
		return mtae.getTraceLength();
	}

	/**
	 * Выдает модельную кривую указанного порога на участке указанного события,
	 * такую, как будто бы на соседних событиях порога нет совсем.
	 * Обеспечивает также мнимальное кэширование (в кэше хранятся результаты для
	 * одного nEvent).
	 * Производительность - не самая лучшая.
	 * FIXME: сейчас игнорирует nEvent и вызывает getThresholdMT(key)
	 * @param key код порога (Threshold)
	 * @param nEvent номер события, для которого одного нужно сгенерировать порог
	 * @return модельная кривая запрошенного порога, определенная на длине всей р/г
	 */
	public ModelTrace getEventThresholdMT(int key, int nEvent)
	{
		return getThresholdMT(key);
	}

	/**
	 * Расширяет пороги так, чтобы покрыть указанные кривые с указанным
	 * запасом. Верхняя кривая должна быть нигде не ниже нижней.
	 * Запас должен быть строго положителен, иначе из-за ошибок округления
	 * покрытие может оказаться неточным (и, как следствие, пойдут алармы).
	 * @param yTop верхняя покрываемая кривая
	 * @param yBottom нижняя покрываемая кривая
	 * @param dyMargin строго положительная амплитуда запас
	 * @param dyFactor множитель запаса (не менее 1.0)
	 */
	public void updateThreshToContain(double[] yTop, double[] yBottom, double dyMargin, double dyFactor)
	{
		for (int i = 0; i < tDY.length; i++)
			tDY[i].changeAllBy(-dyMargin);
		CoreAnalysisManager.extendThreshToCoverCurve(
			mtae.getModelTrace().getYArray(), yTop,
			tDX, tDY, Thresh.SOFT_UP, Thresh.HARD_UP,
			dyFactor);
		CoreAnalysisManager.extendThreshToCoverCurve(
			mtae.getModelTrace().getYArray(), yBottom,
			tDX, tDY, Thresh.SOFT_DOWN, Thresh.HARD_DOWN,
			dyFactor);
		for (int i = 0; i < tDY.length; i++)
			tDY[i].changeAllBy(dyMargin);
	}

	/**
	 * Интерфейс для управления порогами мышью
	 */
	public interface ThresholdHandle {

		final int	VERTICAL_UP_TYPE		= 1;
		final int	VERTICAL_DOWN_TYPE		= 2;
		final int	HORIZONTAL_LEFT_TYPE	= 3;
		final int	HORIZONTAL_RIGHT_TYPE	= 4;

		void moveBy(double dx,
					double dy);

		int getX();
		double getY();
		void release();
		int getType();
		boolean isRelevantToNEvent(int nEvent);
	}
	
	protected abstract class AbstractThresholdHandle implements ThresholdHandle {

		protected Thresh	th;
		protected int		key;
		protected int		posX;
		protected double	posY;
		protected int		type;

		protected AbstractThresholdHandle(int thId, Thresh[] thresh, int key, int posX, double posY, int type) {
			this.th = thresh[thId];
			this.key = key;
			this.posX = posX;
			this.posY = posY;
			this.type = type;
		}

		public int getX() {
			return this.posX;
		}

		public double getY() {
			return this.posY;
		}

		public void release() {
			this.th.arrangeLimits(this.key);
			invalidateThMTCache(); // сбрасываем кэш всех кривых
		}

		public boolean isRelevantToNEvent(int nEvent) {
			return this.th.isRelevantToNEvent(nEvent);
		}

		public int getType() {
			return this.type;
		}

	}

	protected class ThresholdHandleDX
	extends AbstractThresholdHandle 
	{
		private double dxFrac = 0; // сохраняем дробную часть dx 

		protected ThresholdHandleDX(int thId, int key, int posX, double posY) {
			super(thId, tDX, key, posX, posY, Thresh.IS_KEY_UPPER[key] ? HORIZONTAL_LEFT_TYPE : HORIZONTAL_RIGHT_TYPE);
			this.type = ((ThreshDX)this.th).isRise() ? (Thresh.IS_KEY_UPPER[key] ? HORIZONTAL_RIGHT_TYPE : HORIZONTAL_LEFT_TYPE) : this.type;
		}
		public void moveBy(double dx, double dy) // dy is ignored
		{
			dx += this.dxFrac;
			ThreshDX thx = (ThreshDX)this.th;
			double desiredValue = thx.getDX(this.key) + dx;
			thx.setDX(this.key, desiredValue);
			this.dxFrac = desiredValue - thx.getDX(this.key);
			this.posX += dx - this.dxFrac;
			if (this.dxFrac != dx)
				invalidateThMTByKey(this.key);
		}		
	}

	protected class ThresholdHandleDY
	extends AbstractThresholdHandle 
	{
		private double dyFrac = 0; // сохраненная дробная часть порога, не ложащаяся на допускаемую порогом сетку

		protected ThresholdHandleDY(int thId, int key, int posX, double posY)
		{
			super(thId, tDY, key, posX, posY, Thresh.IS_KEY_UPPER[key] ? VERTICAL_UP_TYPE :  VERTICAL_DOWN_TYPE);
			int posMin = this.th.xMin;
			int posMax = this.th.xMax;
			if (((ThreshDY)this.th).getTypeL() && thId > 0 && thId < tDY.length - 1)
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
						posMin = this.th.xMin;
					if (yRVal >= yMax)
						posMax = this.th.xMax;
				}
			}
			if (posX < posMin)
				posX = posMin;
			if (posX > posMax)
				posX = posMax;
		}
		public void moveBy(double dx, double dy) // dx is ignored
		{
			dy += this.dyFrac;
			ThreshDY thy = (ThreshDY)this.th;
			double desiredValue = thy.getDY(this.key) + dy;
			thy.setDY(this.key, desiredValue);
			this.dyFrac = desiredValue - thy.getDY(this.key);
			this.posY += dy - this.dyFrac; // привязка к сетке значения самого порога
			if (dy != this.dyFrac)
				invalidateThMTByKey(this.key);
		}
		
	}

	private Thresh[] getAllThreshByNEvent(int nEvent)
	{
		ArrayList al = new ArrayList();
		for (int i = 0; i < this.tL.length; i++)
		{
			if (this.tL[i].isRelevantToNEvent(nEvent))
				al.add(this.tL[i]);
		}
		return (Thresh[] )al.toArray(new Thresh[al.size()]);
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
	 * @param button номер кнопки мыши, 0=LMB, 1=RMB.
	 * @param nEvent номер события, которым надо ограничить захват,
	 *    либо -1, если ограничивать не надо 
	 * @return handle либо null
	 */
	public ThresholdHandle getThresholdHandle(double x0, double y0,
			double xCapture, double yCapture, double prioFactor, int button,
			int nEvent)
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

		// проверяем соответствие точки захвата заданному событию на
		// определенной пороговой кривой
		if (nEvent >= 0)
		{
			SimpleReflectogramEvent range
				= getEventRangeOnThresholdCurve(nEvent, bestKey);
			if (bestX < range.getBegin() || bestX > range.getEnd())
				return null;
		}

		double bestY = getThresholdY(bestKey, bestX);

		if (button == 0)
		{
			int thId = getMF().findResponsibleThreshDYID(tDX, tDY, bestKey, bestX);
			if (thId == -1)
				return null;
			ThresholdHandleDY handle = new ThresholdHandleDY(thId, bestKey, bestX, bestY);
			handle.posY = getThresholdY(bestKey, handle.posX);
			return handle;
		}
		else
		{
			int thId = getMF().findResponsibleThreshDXID(tDX, tDY, bestKey, bestX);
			if (thId == -1)
				return null;
			ThresholdHandleDX handle = new ThresholdHandleDX(thId, bestKey, bestX, bestY);
			handle.posY = getThresholdY(bestKey, handle.posX);
			return handle;
		}
	}

	// XXX: хитрый метод для определения участка, соответствующего данному событию
	public SimpleReflectogramEvent getEventRangeOnThresholdCurve(int nEvent, int key)
	{
		int begin = -1;
		int end = -1;
		int[] aX = getMF().findResponsibleThreshDXArray(tDX, tDY, key, 0, getTraceLength() - 1);
		for (int i = 0; i < getTraceLength(); i++)
		{
			boolean belongs = i >= getSE()[nEvent].getBegin() && i <= getSE()[nEvent].getEnd();
			if (aX[i] >= 0)
				belongs = tDX[aX[i]].isRelevantToNEvent(nEvent);
			if (belongs)
			{
				end = i;
				if (begin < 0)
					begin = i;
			}
		}
		if (begin < 0)
			return null;
		return new SimpleReflectogramEventImpl(begin, end, getSE()[nEvent].getEventType());
	}

	/**
	 * Создает MTM по эталонной паре события+м.ф., считанной из ByteArray
	 * @param bar
	 * @return MTM с неопределенными порогами 
	 */
	public static ModelTraceManager eventsAndTraceFromByteArray(byte[] bar)
	{
		ModelTraceAndEventsImpl mtae = (ModelTraceAndEventsImpl)DataStreamableUtil.readDataStreamableFromBA(bar, ModelTraceAndEventsImpl.getReader()); 
		return new ModelTraceManager(mtae);
	}

	public byte[] eventsAndTraceToByteArray()
	{
		return DataStreamableUtil.writeDataStreamableToBA(mtae);
	}

	/**
	 * Возвращает эталонные модельную кривую и список событий.
	 * @return ModelTraceAndEvents эталона
	 */
	public ModelTraceAndEventsImpl getMTAE()
	{
        // мы знаем, что MTAEI - неизменяемый (unmodifiable)
		return mtae;
	}

	/**
	 * @deprecated use getMTAE().getNEvents()
	 */
	public int getNEvents()
	{
		return mtae.getNEvents();
	}
	/**
	 * @deprecated use getMTAE().getSimpleEvent()
	 */
	public SimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return mtae.getSimpleEvent(nEvent);
	}

	public void writeToDOS(DataOutputStream dos) throws IOException
	{
		mtae.writeToDOS(dos);
		dos.writeLong(SIGNATURE_THRESH);
		Thresh.writeArrayToDOS(tL, dos);
	}

	private static class DSReader implements DataStreamable.Reader
	{
		// FIXME: very poor technique: init with defaults to oeverride a moment later, isn't it?
		public DataStreamable readFromDIS(DataInputStream dis) throws IOException, SignatureMismatchException
		{
			ModelTraceAndEventsImpl mtae = (ModelTraceAndEventsImpl)ModelTraceAndEventsImpl.getReader().readFromDIS(dis);
			ModelTraceManager mtm = new ModelTraceManager(mtae);
			mtm.invalidateThMTCache();
			long signature = dis.readLong();
			if (signature != SIGNATURE_THRESH)
				throw new SignatureMismatchException();
			Thresh[] tl2 = Thresh.readArrayFromDIS(dis);
			if (mtm.tL.length != tl2.length) 
				throw new SignatureMismatchException();
			mtm.setTL(tl2);
			return mtm;
		}
	}

	public static DataStreamable.Reader getReader()
	{
		if (dsReader == null)
			dsReader = new DSReader();
		return dsReader;
	}
}
