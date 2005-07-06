/*
 * $Id: ModelTraceManager.java,v 1.85 2005/07/06 06:26:29 saa Exp $
 * 
 * Copyright � Syrus Systems.
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
 * ������ ����� ������ �������� ���������
 * ��������� ���� {������� + ��������� ������},
 * ������� � ��������� (���� ���) � ��������� ������ (����),
 * ���������� ��������� ������ � �����������/��������������� �������.
 *
 * @author $Author: saa $
 * @version $Revision: 1.85 $, $Date: 2005/07/06 06:26:29 $
 * @module
 */
public class ModelTraceManager
implements DataStreamable, Cloneable
{
	protected static final long SIGNATURE_THRESH = 3353620050119193102L;
	public static final String CODENAME = "ModelTraceManager";

	protected ModelTraceAndEventsImpl mtae;
	protected Thresh[] tL; // ������ ������ �������
	protected ThreshDX[] tDX; // ������ DX-�������
	protected ThreshDY[] tDY; // ������ DY-�������

    // threshold curves cache
    // cache for thresholds curves, cached objects are arary elements
    private ModelTrace[] thMTCache = null;

    // threshold event cache for getEventRangeOnThresholdCurve
    // cached objects are array elements
    protected int thSRECacheEventId;
    protected SimpleReflectogramEvent[] thSRECache = null;

    // 'single threshold mode' curve cache for getEventThresholdMTR
    // cached object is whole array
    protected int thSingleMTRCacheEventId;
    protected ModelTraceRange[] thSingleMTRCache = null; 

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

    // ������� ��� ������ ����
	protected void invalidateCache()
	{
		thMTCache = null;
        thSingleMTRCache = null;
        thSRECache = null;
	}

    // ������� ������ ����, ��������� �� ����� key,
    // � ����� �������������� ��� ��������� ������
	protected void emptyCacheEntryByKey(int key)
	{
        // ��� ���� ����������� �������
		if (thMTCache == null)
			thMTCache = new ModelTrace[] { null, null, null, null };
		thMTCache[key] = null;

        // ��� ��������� �� ����������� (���������� ������ ��������)
        if (thSRECache != null)
            thSRECache[key] = null;

        // � ��� ����� ����� ������ ������� - ������� ���� ������
        thSingleMTRCache = null;
	}

	// ������� ������ ����, ��������� �� ����� key � �������
	// (��� key � Thresh.CONJ_KEY[key])
	protected void invalidateCacheByKey(int key)
	{
		emptyCacheEntryByKey(key);
		emptyCacheEntryByKey(Thresh.CONJ_KEY[key]);
	}

	protected boolean isThMFCacheValid(int key)
	{
		return thMTCache != null && thMTCache[key] != null;
	}

	// ������� ������
	private Thresh[] createTH()
	{
		LinkedList thresholds = new LinkedList();
		Thresh last = null; // ����� ����� ������ ��������� �� ������� ����� A-����, ���� null, ���� ������ (� ���. �/� ���� ����� �/�� ���)
		for (int i = 0; i < getSE().length; i++)
		{
			int evBegin = getSE()[i].getBegin();
			int evEnd = getSE()[i].getEnd();
			if (last == null)
				thresholds.add(last = new ThreshDY(i, false, evBegin, evEnd)); // "C" coding style

			// ������������� ��������� ������� �������� DX-�������
			// �� DELTA=1 �����.
			// ������� ���� ������� � ���, ��� ���� ����������� �������
			// �������� DX-������ ������������� (�������� �������
			// "������� �����"), � ���� ���������
			// �� �������� �������� ���������� ���������� ����� �����������.
			//
			// �������� ��������� ���������� (��� DELTA=0) - �� ������
			// ��������� ����������� (����., rg0065 @23.9km),
			// � ������� ��������� ����� ��� ��������� ���� ������
			// ��������������� ���. ������� � ������� ����� ���. �������.
			// � ����� ������� ����� ����������� ���������� ������(!) ��� ��
			// ���� �����(!) �������� � ����, ��� ���. ����� ����������
			// (�� �������� �� ����) ��������� ���� �� ������ ���. �������
			// � ������ �� ������� DY-������� ��� ���� (�����) �����.
			// �� ����, ����� ���������� ������������� � ���������� ��������
			// ������, � ������������ ������ ������ �� ���������, �����
			// �������� ���������� DY-������� ����� ���. �������.
			//
			// �������, ��� DELTA=1 ����������.
			final int DELTA = 1;

			switch(getSE()[i].getEventType())
			{
			case SimpleReflectogramEvent.LINEAR:
				last.xMax = evEnd;
				last.eventId1 = i;
				break;
			case SimpleReflectogramEvent.GAIN:
				last.xMax = evBegin;
				last.eventId1 = i;
				thresholds.add(new ThreshDX(i, evBegin - DELTA, evEnd + DELTA, true));
				thresholds.add(last = new ThreshDY(i, false, evEnd, evEnd));
				break;
			case SimpleReflectogramEvent.LOSS:
				last.xMax = evBegin;
				last.eventId1 = i;
				thresholds.add(new ThreshDX(i, evBegin - DELTA, evEnd + DELTA, false));
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
				thresholds.add(new ThreshDX(i, evBegin - DELTA, evCenter, true));
				thresholds.add(new ThreshDY(i, true, evCenter, evCenter));
				thresholds.add(new ThreshDX(i, evCenter, evEnd + DELTA, false));
				thresholds.add(last = new ThreshDY(i, false, evEnd, evEnd));
				//System.err.println("REFLECTIVE: event #" + i + " begin=" + evBegin + " center=" + evCenter + " end=" + evEnd);
				break;
			}
		}
		return (Thresh[] )thresholds.toArray(new Thresh[thresholds.size()]);
	}

	protected ModelFunction getMF()
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

    // is 'protected' to be accessible from DSReader
    protected void setTL(Thresh tl[])
	{
		tL = tl;
		// ��������� �������� ������ tDX � tDY
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

	/** ������� MTM �� ������ ��������� ���� {������� + �.�.} ModelTraceAndEvents
	 * 
	 * @param mtae ������� ������
	 */
	public ModelTraceManager(ModelTraceAndEventsImpl mtae)
	{
		this.mtae = mtae;
        setTL(createTH());
	}

    protected ModelTraceManager(ModelTraceAndEventsImpl mtae, Thresh[] tl)
    {
        this.mtae = mtae;
        setTL(tl);
    }

	/**
	 * ���������� �������� ������ � ������ �����.
	 * � ����� ��������� ������������������,
	 * ������������� ����� ����� getThresholdMT  
	 * @param key ����� ��������� ������
	 * @param x x-���������� (������)
	 * @return y-�������� (��)
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
			ModelTraceManager.this.invalidateCacheByKey(key);
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
			invalidateCache(); // ���������� ��� ���� ������
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
			invalidateCache();
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
			invalidateCache();
		}
	}

    public class ThreshEditorWithDefaultMark
    extends ThreshEditor {
        public boolean isMarked;
        protected ThreshEditorWithDefaultMark(int type, Thresh th,
                boolean isMarked) {
            super(type, th);
            this.isMarked = isMarked;
        }
    }

    /**
     * @param nEvent ����� �������
     * @return ������ ���������� ���� ������� ��� ������� �������
     */
	public ThreshEditorWithDefaultMark[] getThreshEditors(int nEvent)
	{
		//return re[nEvents].getThreshold();

		Thresh[] tlist = getAllThreshByNEvent(nEvent);
		ArrayList ret = new ArrayList();
        Thresh defaultTh = getDefaultThreshByNEvent(nEvent);

		for (int i = 0; i < tlist.length; i++)
		{
			Thresh th = tlist[i];
			if (th instanceof ThreshDX)
			{
				ret.add(new ThreshEditorWithDefaultMark(
					((ThreshDX )th).isRise()
						? ThreshEditor.TYPE_DXF
						: ThreshEditor.TYPE_DXT,
					th,
                    th == defaultTh)); // mark if thesh object is same
			}
			if (th instanceof ThreshDY)
			{
                ret.add(new ThreshEditorWithDefaultMark(
				    ((ThreshDY )th).getTypeL()
                        ? ThreshEditor.TYPE_L
                        : ThreshEditor.TYPE_A,
                    th,
                    th == defaultTh)); // mark if thesh object is same
			}
		}
        return (ThreshEditorWithDefaultMark[])ret.toArray(
                new ThreshEditorWithDefaultMark[ret.size()]);
	}

    // may return -1
    public static int getDefaultThreshEditorIndex(ThreshEditorWithDefaultMark[] teds) {
        for (int i = 0; i < teds.length; i++)
            if (teds[i].isMarked)
                return i;
        return -1;
    }

	/**
	 * ������ ��������� ������ ���������� ������.
	 * ������������ ����������� �����������.
	 * @param key ��� ������ (Threshold)
	 * @return ��������� ������ ������������ ������ 
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
			emptyCacheEntryByKey(key);
			thMTCache[key] = thMt;
		}
		return thMt;
	}

	/**
	 * ������ ��������� ������ ������ ��������� ������.
	 * XXX: �� ��������.
	 * @param isUpper true - ����� ������� �����, false - ������
	 * @param level ������� ������, �� 0.0 �� 1.0 ������������
	 * @return ����������� ��������� ������
	 */
	private ModelTrace getThresholdMTByLevel(boolean isUpper, double level) {
		// ������� ����� �������
		ThreshDX[] effX = new ThreshDX[tDX.length];
		for (int i = 0; i < tDX.length; i++)
			effX[i] = tDX[i].makeWeightedThresholds(level);
		ThreshDY[] effY = new ThreshDY[tDY.length];
		for (int i = 0; i < tDY.length; i++)
			effY[i] = tDY[i].makeWeightedThresholds(level);
		// ���������� ��������� ������
		ModelFunction tmp = getMF().copy();
		tmp.changeByThresh(effX,
				effY,
				isUpper ? Thresh.SOFT_UP : Thresh.SOFT_DOWN);
		return new ModelTraceImplMF(tmp, getTraceLength());
	}

	/**
	 * ������ ������� ��������� ������ ������ ��������� ������
	 * @param level ������� ������, �� 0.0 �� 1.0 ������������
	 * @return ����������� ��������� ������
	 */
	public ModelTrace getThresholdMTUpperByLevel(double level) {
		return getThresholdMTByLevel(true, level);
	}

	/**
	 * ������ ������ ��������� ������ ������ ��������� ������
	 * @param level ������� ������, �� 0.0 �� 1.0 ������������
	 * @return ����������� ��������� ������
	 */
	public ModelTrace getThresholdMTLowerByLevel(double level) {
		return getThresholdMTByLevel(false, level);
	}

	private int getTraceLength()
	{
		return mtae.getTraceLength();
	}

    /**
     * ������� ����� DX-�������, � ������� "��� ������ ������� ����� �������
     * ��������� �������"
     * @param nEvent ����� ��������� �������
     * @return ����� DY-�������
     */
    private ThreshDX[] getSingleEventThreshDX(int nEvent) {
        ThreshDX[] tmpTDX = new ThreshDX[tDX.length];
        for (int i = 0; i < tmpTDX.length; i++) {
            tmpTDX[i] = tDX[i].isRelevantToNEvent(nEvent)
                ? tDX[i]
                : tDX[i].makeZeroedCopy();
        }
        return tmpTDX;
    }
    /**
     * ������� ����� DY-������� ����������
     * {@link #getSingleEventThreshDX(int)}
     */
    private ThreshDY[] getSingleEventThreshDY(int nEvent) {
        ThreshDY[] tmpTDY = new ThreshDY[tDY.length];
        for (int i = 0; i < tmpTDY.length; i++) {
            tmpTDY[i] = tDY[i].isRelevantToNEvent(nEvent)
                ? tDY[i]
                : tDY[i].makeZeroedCopy();
        }
        return tmpTDY;
    }

    /**
     * ���������� ��� ������ ��������� ������ ������� �������
     *   "��� ���� �� �� �� ���� ������ �������".
     * <p>
     * Note: ����������� �������� �����, ���� �������� ���������
     *   � �������� ������ �������.
     * @param nEvent ����� �������
     * @return ������ ��������� ������ � ������� ModelTraceRange,
     *   � ���� �������. � ������� �������� �������� null, ���� �����. �����
     *   �� �����������.
     */
    public ModelTraceRange[] getEventThresholdMTR(int nEvent) {
        // check if the answer is already present
        if (thSingleMTRCacheEventId == nEvent
                && thSingleMTRCache != null)
        {
            //System.err.println("getEventThresholdMTR: nEvent " + nEvent + " cache hit");
            return thSingleMTRCache;
        }

        // make thresholds for 'this event only'
        ThreshDX[] tmpTDX = getSingleEventThreshDX(nEvent);
        ThreshDY[] tmpTDY = getSingleEventThreshDY(nEvent);

        // init cache
        thSingleMTRCacheEventId = nEvent;
        thSingleMTRCache = new ModelTraceRange[] { null, null, null, null };

        // find ranges and curves, fill cache
        for (int key = 0; key < 4; key++)
        {
            SimpleReflectogramEvent sre =
                getEventRangeOnThresholdCurve(nEvent, key, tmpTDX, tmpTDY);
            if (sre != null) {
	            ModelFunction tmp = getMF().copy();
	            tmp.changeByThresh(tmpTDX, tmpTDY, key);
	            thSingleMTRCache[key] = new ModelTraceRangeImplMF(tmp,
	                    sre.getBegin(),
	                    sre.getEnd());
            } else {
            	thSingleMTRCache[key] = null;
            }
        }

        // make a copy of resulting array for client
        //System.err.println("getEventThresholdMTR: nEvent " + nEvent + " cache miss");
        return (ModelTraceRange[])thSingleMTRCache.clone();
    }

	/**
	 * ��������� ������ ���, ����� ������� ��������� ������ � ���������
	 * �������. ������� ������ ������ ���� ����� �� ���� ������.
	 * ����� ������ ���� ������ �����������, ����� ��-�� ������ ����������
	 * �������� ����� ��������� �������� (�, ��� ���������, ������ ������).
	 * @param yTop ������� ����������� ������
	 * @param yBottom ������ ����������� ������
	 * @param dyMargin ������ ������������� ��������� �����
	 * @param dyFactor ��������� ������ (�� ����� 1.0)
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
	 * ��������� ��� ���������� �������� �����
	 */
	public interface ThresholdHandle {

		int	VERTICAL_UP_TYPE		= 1;
		int	VERTICAL_DOWN_TYPE		= 2;
		int	HORIZONTAL_LEFT_TYPE	= 3;
		int	HORIZONTAL_RIGHT_TYPE	= 4;

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
			invalidateCache(); // ���������� ��� ���� ������
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
		private double dxFrac = 0; // ��������� ������� ����� dx 

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
				invalidateCacheByKey(this.key);
		}		
	}

	protected class ThresholdHandleDY
	extends AbstractThresholdHandle 
	{
		private double dyFrac = 0; // ����������� ������� ����� ������, �� ��������� �� ����������� ������� �����

		protected ThresholdHandleDY(int thId, int key, int posX, double posY)
		{
			super(thId, tDY, key, posX, posY, Thresh.IS_KEY_UPPER[key] ? VERTICAL_UP_TYPE :  VERTICAL_DOWN_TYPE);
			int posMin = this.th.xMin;
			int posMax = this.th.xMax;
			if (((ThreshDY)this.th).getTypeL() && thId > 0 && thId < tDY.length - 1)
			{
				// �������� ��������� ����� �������� �� ������ 98% ��������� ������
				posMin = tDY[thId - 1].xMax;
				posMax = tDY[thId + 1].xMin;
				if (posMin < posMax)
				{
					// �������� ������������ ��� ������� ������
					int x0 = posMin;
					double[] yArr = getThresholdMT(key).getYArray(x0, posMax - posMin + 1);
					// �������� ��������� �������
					double level = 0.995; // XXX - ������������ �������� ��� UI
					double yMax = ReflectogramMath.getArrayMax(yArr);
					double yLVal = getThresholdY(key, posMin);
					double yRVal = getThresholdY(key, posMax);
					double yLCut = yLVal + (yMax - yLVal) * level;
					double yRCut = yRVal + (yMax - yRVal) * level;
					// ���������� ������ � ����� ��������� �� ���������� ������
					int i;
					for (i = posMin; i <= posMax; i++)
						if (yArr[i - x0] > yLCut)
							break;
					posMin = i;
					for (i = posMax; i > posMin; i--)
						if (yArr[i - x0] > yRCut)
							break;
					posMax = i;
					// �� ������, ���� ������ ������� ���� ������� �������,
					// ��������� ����������� ����������
					// XXX: ������������ �� �������������, �� ����� ������ �����������.
					// ���� �� ��������� - ��� �������� ������ �������� GUI, �� �� ����������������.
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
			this.posY += dy - this.dyFrac; // �������� � ����� �������� ������ ������
			if (dy != this.dyFrac)
				invalidateCacheByKey(this.key);
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
    // ���������� '����� �� ���������' ��� ������� �������
    // may return null
    private Thresh getDefaultThreshByNEvent(int nEvent)
    {
        // �������� ������� DL-����� -- ��� �����������
        for (int i = 0; i < this.tDY.length; i++) {
            if (this.tDY[i].isRelevantToNEvent(nEvent) && this.tDY[i].getTypeL())
                return this.tDY[i];
        }
        // ���������� ����� DY-����� (����� �� ������) -- ��� ��-�����������  
        for (int i = 0; i < this.tDY.length; i++) {
            if (this.tDY[i].isRelevantToNEvent(nEvent))
                return this.tDY[i];
        }
        // DY-������� ��� ����� ������� ��� ������
        return null;
    }

	/**
	 * ������ handle ��� ��������� �������� ������� ����� �� ������ ����������
     * �����, ���� "�����" ������������.
	 * ���� ����������, ����� ����� ������ ������� ����� ����������.
	 * ������ ���������� �������� ��������� ������������ ���������,
	 * ����������� ��� ��������������.
     * <p>
     * �������� ����������� handle:
     * <ul>
     * <li> ����������, ����� ������� ��������� ������ ��������� ��� �������
     * <li> ���������� ��������� � ���� ��������� ������ � ��������� � ����
     *   ����� �� ���� ������
     * <li> ���������� �����, ��������������� ��������� ����� ��������� ������
     * </ul>
	 * @param x0 ��������� x-���������� ���� (����������� �������, �� ���.)
	 * @param y0 ��������� y-���������� ���� (��)
	 * @param xCapture ������ ������� ������ ����� �� �����������
	 * @param yCapture ������ ������� ������ ����� �� ���������
	 * @param prioFactor �������� �� ��������� �������,
	 *     =0: ����������� ���; =1: 100% ��������� HARD-�������
	 * @param button ����� ������ ����, 0=LMB, 1=RMB.
	 * @param nEvent ����� �������, ������� ���� ���������� ������,
	 *    ���� -1, ���� ������������ �� ����
     * @param singleEventCurveMode true, ���� ������������� ������,
     *    ������������ "��� ���� �� ������ ������� �� ����" (������������
     *    ��� nEvent < 0).
	 * @return handle ���� null
	 */
	public ThresholdHandle getThresholdHandle(double x0, double y0,
			double xCapture, double yCapture, double prioFactor, int button,
			int nEvent, boolean singleEventCurveMode)
	{
        // ���� ������� ��� - ����� �������� � ������ ������� �������
        if (nEvent < 0)
            singleEventCurveMode = false;

		if (xCapture <= 0.1)
			xCapture = 0.1;  // XXX: xCapture range: min
		if (xCapture > 5000)
			xCapture = 5000; // XXX: xCapture range: max
		if (yCapture <= 1e-4)
			yCapture = 1e-4; // XXX: yCapture range: min (����������� ��� �������������� ��������)

		int xRange = (int )(xCapture + 1);

        // ���������� ��������� ������ � ������� �� ���, � ������� ����������
        // ����� ����� �������
        ModelTraceRange[] mtrs = null;
        if (singleEventCurveMode)
            mtrs = getEventThresholdMTR(nEvent);
        else
        {
            mtrs = new ModelTraceRange[] { null, null, null, null };
            for (int key = 0; key < mtrs.length; key++)
            {
                if (nEvent < 0)
                    mtrs[key] = getThresholdMT(key);
                else {
                    SimpleReflectogramEvent range =
                        getEventRangeOnThresholdCurve(nEvent, key);
                    mtrs[key] =
                        new ModelTraceRangeImplMTRSubrange(getThresholdMT(key),
                            range.getBegin(), range.getEnd(), false);
                }
            }
        }

        // ���������� ��������� ��������� ������ � ��������� ����� �� ���� ������
		// UP2/DOWN2 takes precedence over UP1/DOWN1
		double bestDR = 2; // ���������� ����������=1; 2 => ��� �� �������  
		int bestKey = 0;
		int bestX = 0;
        double bestY = 0;
		int[] keys = new int[] { // ������� �������� �������
		        Thresh.SOFT_UP, Thresh.SOFT_DOWN,
                Thresh.HARD_UP, Thresh.HARD_DOWN }; 
		for (int k = 0; k < 4; k++)
		{
            ModelTraceRange cmtr = mtrs[keys[k]];
            if (cmtr == null)
            	continue; // ���� ������ ��� (����� �� �����������)
            int xL = Math.max((int)x0 - xRange, cmtr.getBegin());
            int xR = Math.min((int)x0 + xRange, cmtr.getEnd());
            if (xR < xL)
                continue; // ����������� ������� �� ������������ � ���� ��������� ������
            double[] yArr = cmtr.getYArray(xL, xR - xL + 1);

			double xScale = xCapture;
			double yScale = yCapture;
			double curBestDR = 2; // >1 => ��� �� �������
			int curBestX = 0;
			for (int i = 0; i < yArr.length; i++)
			{
				double dX = (x0 - xL - i) / xScale;
				double dY = (y0 - yArr[i]) / yScale;
				double scalar;
				double dR;
				if (i == yArr.length - 1) // ��������� ����� - ������������� ���������� ������ �� ��� �����
				{
					dR = Math.sqrt(dX * dX + dY * dY);
					scalar = 0;
				}
				else
				{
					double aX = 1.0 / xScale;
					double aY = (yArr[i + 1] - yArr[i]) / yScale;
					double aSqr = aX * aX + aY * aY;
					scalar = (dX * aX + dY * aY) / aSqr;
					if (scalar <= 0) // ����� ����� �����
					{
						dR = Math.sqrt(dX * dX + dY * dY);
					}
					else if (scalar < 1) // ����� � ��������� ��� � �����
					{
						double vector = dX * aY - dY * aX;
						dR = Math.abs(vector) / Math.sqrt(aSqr);
					}
					else
						continue; // ������, ����� �� ����� � ������ ����� ����� ���������� �� ��������� ��������
				}

				if (dR <= 1 && dR < curBestDR)
				{
					curBestDR = dR;
					// ���������� �������, ��������� � ����� �������, ��������� � ����� �����
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
                bestY = yArr[curBestX - xL];
			}
		}

		if (bestDR > 1)
			return null;

        // ������ ���� ���������� �����, ��������������� ����� �������
        // ��� ����� ������� ���� ����������, � ����� ������� �������
        // �� �������� - � ������, ��� � "��� ���� �� ���� ������ ������
        // ������ �������".
        ThreshDX[] tmpTDX = singleEventCurveMode
            ? getSingleEventThreshDX(nEvent)
            : tDX;
        ThreshDY[] tmpTDY = singleEventCurveMode
            ? getSingleEventThreshDY(nEvent)
            : tDY;
		if (button == 0)
		{
			int thId = getMF().
                findResponsibleThreshDYID(tmpTDX, tmpTDY, bestKey, bestX);
			if (thId == -1)
				return null;
			ThresholdHandleDY handle =
                new ThresholdHandleDY(thId, bestKey, bestX, bestY);
			handle.posY = bestY; //getThresholdY(bestKey, handle.posX);
			return handle;
		}
		else
		{
			int thId = getMF().
                findResponsibleThreshDXID(tmpTDX, tmpTDY, bestKey, bestX);
			if (thId == -1)
				return null;
			ThresholdHandleDX handle =
                new ThresholdHandleDX(thId, bestKey, bestX, bestY);
			handle.posY = bestY; //getThresholdY(bestKey, handle.posX);
			return handle;
		}
	}

    // ��������� �������� ���� �������, ������ �������
	// ���������� null, ���� �������� ����
    private SimpleReflectogramEvent getEventRangeOnThresholdCurve(int nEvent,
            int key, ThreshDX[] threshDX, ThreshDY[] threshDY)
    {
        int begin = -1;
        int end = -1;
        int[] aX = getMF().findResponsibleThreshDXArray(threshDX, threshDY,
                key, 0, getTraceLength() - 1);
        int eventBegin = getSE()[nEvent].getBegin();
        int eventEnd = getSE()[nEvent].getEnd();

        // XXX: rather slow loop
        // so, we try to increase its performance with traceLength and cache
        int traceLength = getTraceLength();
        boolean prevRelevant = false; // caching
        int prevNT = -1; // caching
        for (int i = 0; i < traceLength; i++)
        {
            boolean belongs;
            if (aX[i] >= 0)
            {
                if (prevNT != aX[i]) // caching
                {
                    prevNT = aX[i];
                    prevRelevant = tDX[prevNT].isRelevantToNEvent(nEvent);
                }
                belongs = prevRelevant;
            }
            else
                belongs = i >= eventBegin && i <= eventEnd;
            if (belongs)
            {
                end = i;
                if (begin < 0)
                    begin = i;
            }
        }
        if (begin < 0)
            return null;
        return new SimpleReflectogramEventImpl(begin, end,
                getSE()[nEvent].getEventType());
    }

    /**
     * ������ �������� ����������� �������, ���������������� ������� �������.
     *   �������� ������������ ��������, ���� �������� ��������� � ��������
     *   ������ �������.
     * @param nEvent ����� �������
     * @param key ����� ��������� ������
     * @return ������� �/� � ���� SimpleReflectogramEvent
     *   � ������������� begin � end.
     */
    public SimpleReflectogramEvent getEventRangeOnThresholdCurve(int nEvent, int key)
	{
        // �������� ����� �� ����
        if (thSRECacheEventId == nEvent && thSRECache != null
                && thSRECache[key] != null)
        {
            //System.err.println("getEventRangeOnThresholdCurve: nEvent " + nEvent + " cache hit");
            return thSRECache[key];
        }
        
        SimpleReflectogramEvent sre =
            getEventRangeOnThresholdCurve(nEvent, key, tDX, tDY);

        // ������ � ���
        if (thSRECacheEventId != nEvent)
            thSRECache = null;
        if (thSRECache == null)
            thSRECache = new SimpleReflectogramEvent[] {null, null, null, null};
        thSRECacheEventId = nEvent;
        thSRECache[key] = sre;
        //System.err.println("getEventRangeOnThresholdCurve: nEvent " + nEvent + " cache miss");
        return sre;
    }

    private int getEventAlarmPref(int eventType) {
		switch (eventType) {
		case SimpleReflectogramEvent.DEADZONE:   // fall through
		case SimpleReflectogramEvent.ENDOFTRACE: // fall through
		case SimpleReflectogramEvent.CONNECTOR:
			return 3;
		case SimpleReflectogramEvent.GAIN: // fall through
		case SimpleReflectogramEvent.LOSS:
			return 2;
		case SimpleReflectogramEvent.NOTIDENTIFIED:
			return 1;
		default:
			return 0;
		}
    }

    /**
     * ����������, ������ ������� ������ ����� ������������� ������
     * ����� ������������� ������.
     * �� ���������� ������� ��������� ������, � ��������� ������ ��
     * ������� ������� � �������� ��� HARD DX-�������.
     * ������ ������������ ������������� �������� � �������, �����
     * ����., � ������ ����� ��� ��������.
     * @param x ���������� ����� ������
     * @param oneMorePoint ��������� �������� ������� ��� �� 1 �����.
     * �������, ����� ������� ������-������ ����� �� ������� DX-�����, �����
     * ��������� ����� ������ � ����� �������.
     * @return ����� �������, ���� -1, ���� �� �������.
     */
    private int findSupposedAlarmEventByPos(int x, boolean oneMorePoint) {
    	int pref = -1; // ������������������ (������ - ��� ����. � ������, ������ - ��� ���.)
    	int found = -1; // ����� ���������� �������

    	// ������� ��������� ��� DX-������ � ������ ����� ���� �������,
    	// � ����� - ������ ��� ������� (�� ������ ���������� DX-�������).
    	for (int i = 0; i < tDX.length; i++) {
    		int keyU = Thresh.HARD_UP;
    		int keyD = Thresh.HARD_DOWN;
    		int dxMin = Math.min(tDX[i].getDX(keyU), tDX[i].getDX(keyD));
    		int dxMax = Math.max(tDX[i].getDX(keyU), tDX[i].getDX(keyD));
    		if (tDX[i].xMin - dxMin <= x && tDX[i].xMax + dxMax >= x) {
    			// eventId0 � eventId1 ��� DX-������� �����, ����� eventId0 (?)
    			int nEv = tDX[i].eventId0;
    			System.out.println("findSupposedAlarmEventByPos: tDX: x " + x + ", nEv " + nEv); // FIXME: debug sysout
    			int eventType = getMTAE().getSimpleEvent(nEv).getEventType();
    			int curPref = getEventAlarmPref(eventType);
    			if (curPref > pref) {
    				pref = curPref;
    				found = nEv;
    			}
    		}
    	}

    	int nEvents = this.mtae.getNEvents();
    	for (int nEv = 0; nEv < nEvents; nEv++) {
    		SimpleReflectogramEvent ev = getMTAE().getSimpleEvent(nEv);
    		if (ev.getBegin() <= x && ev.getEnd() >= x) {
    			System.out.println("findSupposedAlarmEventByPos: nEv: x " + x + ", nEv " + nEv); // FIXME: debug sysout
    			int eventType = getMTAE().getSimpleEvent(nEv).getEventType();
    			int curPref = getEventAlarmPref(eventType);
    			if (curPref > pref) {
    				pref = curPref;
    				found = nEv;
    			}
    		}
    	}

    	return found;
    }

    /**
     * ������������ ��������� ������, ���������� ��� � ������ �������,
     * ���� ��� ������� - ������������� ��� ������.
     * ��� ���. � ����. ������� �������� �� ������������.
     * @param x ���������;
     * @param oneMorePoint ���������� ��������� ������� �� 1 ����� ����� �������
     *  (see {@link #findSupposedAlarmEventByPos}).
     * @return ����������������� ���������
     */
    public int fixAlarmPos(int x, boolean oneMorePoint) {
    	int nEv = findSupposedAlarmEventByPos(x, oneMorePoint);
    	if (nEv < 0)
    		return x;
    	SimpleReflectogramEvent ev = this.mtae.getSimpleEvent(nEv);
    	int eventType = ev.getEventType();
		switch (eventType) {
		case SimpleReflectogramEvent.DEADZONE:   // fall through
		case SimpleReflectogramEvent.ENDOFTRACE: // fall through
		case SimpleReflectogramEvent.CONNECTOR:  // fall through
		case SimpleReflectogramEvent.GAIN: // fall through
		case SimpleReflectogramEvent.LOSS:
			return ev.getBegin();
		default:
			return x;
		}
    }

	/**
	 * ������� MTM �� ��������� ���� �������+�.�., ��������� �� ByteArray
	 * @param bar
	 * @return MTM � ��������������� ��������
	 * @throws DataFormatException 
	 */
	public static ModelTraceManager eventsAndTraceFromByteArray(byte[] bar)
    throws DataFormatException
	{
		ModelTraceAndEventsImpl mtae =(ModelTraceAndEventsImpl)
            DataStreamableUtil.readDataStreamableFromBA(bar,
                ModelTraceAndEventsImpl.getReader()); 
		return new ModelTraceManager(mtae);
	}

	public byte[] eventsAndTraceToByteArray()
	{
		return DataStreamableUtil.writeDataStreamableToBA(mtae);
	}

	/**
	 * ���������� ��������� ��������� ������ � ������ �������.
	 * @return ModelTraceAndEvents �������
	 */
	public ModelTraceAndEventsImpl getMTAE()
	{
        // �� �����, ��� MTAEI - ������������ (unmodifiable)
		return mtae;
	}

	/**
	 * @deprecated use getMTAE().getNEvents()
	 */
	@Deprecated
	public int getNEvents()
	{
		return mtae.getNEvents();
	}
	/**
	 * @deprecated use getMTAE().getSimpleEvent()
	 */
	@Deprecated
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
		public DataStreamable readFromDIS(DataInputStream dis)
        throws IOException, SignatureMismatchException
		{
			ModelTraceAndEventsImpl mtae =
                (ModelTraceAndEventsImpl)ModelTraceAndEventsImpl.
                    getReader().readFromDIS(dis);

            long signature = dis.readLong();
			if (signature != SIGNATURE_THRESH)
				throw new SignatureMismatchException();

            return new ModelTraceManager(mtae,
                    Thresh.readArrayFromDIS(dis));
		}
	}

	public static DataStreamable.Reader getReader()
	{
		if (dsReader == null)
			dsReader = new DSReader();
		return dsReader;
	}
}
