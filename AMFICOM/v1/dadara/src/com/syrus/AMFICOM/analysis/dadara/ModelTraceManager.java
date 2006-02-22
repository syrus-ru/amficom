/*
 * $Id: ModelTraceManager.java,v 1.110 2006/02/22 10:32:02 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import static java.util.logging.Level.FINEST;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.io.DataFormatException;
import com.syrus.io.SignatureMismatchException;
import com.syrus.util.Log;

/**
 * ������ ����� ������ �������� ���������
 * ��������� ���� {������� + ��������� ������},
 * ������� � ��������� (���� ���) � ��������� ������ (����),
 * ���������� ��������� ������ � �����������/��������������� �������.
 *
 * @author $Author: saa $
 * @version $Revision: 1.110 $, $Date: 2006/02/22 10:32:02 $
 * @module
 */
public class ModelTraceManager
implements DataStreamable, Cloneable
{
	protected static final long SIGNATURE_MTM = 3353620050929164200L;
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

	@Override
	public Object clone()
	throws CloneNotSupportedException
	{
		ModelTraceManager ret = (ModelTraceManager)super.clone();
		// remove cache data
		ret.thMTCache = null;
		// copy thresholds
		Thresh[] tLout = this.tL.clone(); // clone the holder
		for (int i = 0; i < tLout.length; i++)
			tLout[i] = (Thresh)tLout[i].clone(); // clone each threshold in array
		ret.setTL(tLout);
		// no need to clone mtae
		return ret;
	}

	// ������� ��� ������ ����
	protected void invalidateCache()
	{
		this.thMTCache = null;
		this.thSingleMTRCache = null;
		this.thSRECache = null;
	}

	// ������� ������ ����, ��������� �� ����� key,
	// � ����� �������������� ��� ��������� ������
	protected void emptyCacheEntryByKey(int key)
	{
		// ��� ���� ����������� �������
		if (this.thMTCache == null)
			this.thMTCache = new ModelTrace[] { null, null, null, null };
		this.thMTCache[key] = null;

		// ��� ��������� �� ����������� (���������� ������ ��������)
		if (this.thSRECache != null)
			this.thSRECache[key] = null;

		// � ��� ����� ����� ������ ������� - ������� ���� ������
		this.thSingleMTRCache = null;
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
		return this.thMTCache != null && this.thMTCache[key] != null;
	}

	// ������� ������
	private Thresh[] createTH()
	{
		List<Thresh> thresholds = new ArrayList<Thresh>();
		Thresh last = null; // ����� ����� ������ ��������� �� ������� ����� A-����, ���� null, ���� ������ (� ���. �/� ���� ����� �/�� ���)
		for (int i = 0; i < getSE().length; i++)
		{
			int evBegin = getSE()[i].getBegin();
			int evEnd = getSE()[i].getEnd();
			if (last == null)
				thresholds.add(last = new ThreshDY(i, ThreshDY.Type.dA, evBegin, evEnd)); // "C" coding style

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
				thresholds.add(new ThreshDX(i, evBegin - DELTA, evEnd + DELTA, true, false));
				thresholds.add(last = new ThreshDY(i, ThreshDY.Type.dA, evEnd, evEnd));
				break;
			case SimpleReflectogramEvent.LOSS:
				last.xMax = evBegin;
				last.eventId1 = i;
				thresholds.add(new ThreshDX(i, evBegin - DELTA, evEnd + DELTA, false, false));
				thresholds.add(last = new ThreshDY(i, ThreshDY.Type.dA, evEnd, evEnd));
				break;
			case SimpleReflectogramEvent.NOTIDENTIFIED:
				if (last.xMax > last.xMin)
					last.xMax = evBegin - 1;
				if (evEnd < evBegin)
					evEnd--;
				thresholds.add(new ThreshDY(i, ThreshDY.Type.nI, evBegin, evEnd));
				last = null;
				break;
			case SimpleReflectogramEvent.DEADZONE:
				// fall through
			case SimpleReflectogramEvent.ENDOFTRACE:
				// fall through
			case SimpleReflectogramEvent.CONNECTOR:
				int[] pos = getConnectorMinMaxMin(getMF(), evBegin, evEnd);
				evBegin = pos[0];
				int evCenter = pos[1];
				evEnd = pos[2];
				last.xMax = evBegin;
				last.eventId1 = i;
				thresholds.add(new ThreshDX(i, evBegin - DELTA, evCenter, true, true));
				thresholds.add(new ThreshDY(i, ThreshDY.Type.dL, evCenter, evCenter));
				thresholds.add(new ThreshDX(i, evCenter, evEnd + DELTA, false, false));
				thresholds.add(last = new ThreshDY(i, ThreshDY.Type.dA, evEnd, evEnd));
//				Log.debugMessage("REFLECTIVE: event #" + i + " begin=" + evBegin + " center=" + evCenter + " end=" + evEnd,
//						FINEST);
				break;
			}
		}
		return thresholds.toArray(new Thresh[thresholds.size()]);
	}

	protected ModelFunction getMF()
	{
		return this.mtae.getMF();
	}

	/**
	 * @return etalon's simple events
	 */
	protected SimpleReflectogramEventImpl[] getSE()
	{
		return this.mtae.getRSE();
	}

	// is 'protected' to be accessible from DSReader
	protected void setTL(Thresh tl[])
	{
		this.tL = tl;
		// ��������� �������� ������ tDX � tDY
		List<Thresh> thresholds = new ArrayList<Thresh>();
		for (int i = 0; i < this.tL.length; i++)
		{
			if (this.tL[i] instanceof ThreshDX)
				thresholds.add(this.tL[i]);
		}
		this.tDX = thresholds.toArray(new ThreshDX[thresholds.size()]);

		thresholds = new ArrayList<Thresh>();
		for (int i = 0; i < this.tL.length; i++)
		{
			if (this.tL[i] instanceof ThreshDY)
				thresholds.add(this.tL[i]);
		}
		this.tDY = thresholds.toArray(new ThreshDY[thresholds.size()]);
	}

	/**
	 * ������� MTM �� ������ ��������� ���� {������� + �.�.}
	 * ModelTraceAndEvents.
	 * ����������� DY-������ ������� � ������� ����������� ����������
	 * ����� {@link #updateThreshToContain}
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
		public static final int TYPE_A = 1; // XXX: both dA and nI
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
			return this.type;
		}
		public double getValue(int key)
		{
			if (this.th instanceof ThreshDX)
				return ((ThreshDX )this.th).getDX(key) * ModelTraceManager.this.mtae.getDeltaX(); // samples to meters
			return ((ThreshDY )this.th).getDY(key);
		}
		public void setValue(int key, double value)
		{
			ModelTraceManager.this.invalidateCacheByKey(key);
			if (this.type == TYPE_DXF || this.type == TYPE_DXT)
			{
				// convert meters to samples
				if (ModelTraceManager.this.mtae.getDeltaX() > 0)
					value /= ModelTraceManager.this.mtae.getDeltaX();
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
				((ThreshDX )this.th).setDX(key, val);
			}
			else
				((ThreshDY )this.th).setDY(key, value);
			this.th.arrangeLimits(key);
			invalidateCache(); // ���������� ��� ���� ������
		}
		/**
		 * increase thresholds;
		 * for dx uses +1, for dy uses the default value
		 */
		public void increaseValues()
		{
			if (this.th instanceof ThreshDX)
				((ThreshDX)this.th).changeAllBy(1);
			else
				((ThreshDY)this.th).changeAllBy(0.1);
			invalidateCache();
		}
		/**
		 * increase thresholds;
		 * for dx uses -1, for dy uses the default value
		 */
		public void decreaseValues()
		{
			if (this.th instanceof ThreshDX)
				((ThreshDX)this.th).changeAllBy(-1);
			else
				((ThreshDY)this.th).changeAllBy(-0.1);
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
		ArrayList<ThreshEditorWithDefaultMark> ret = new ArrayList<ThreshEditorWithDefaultMark>();
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
					((ThreshDY )th).getType() == ThreshDY.Type.dL
						? ThreshEditor.TYPE_L
						: ThreshEditor.TYPE_A,
					th,
					th == defaultTh)); // mark if thesh object is same
			}
		}
		return ret.toArray(new ThreshEditorWithDefaultMark[ret.size()]);
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
			thMt = this.thMTCache[key];
		}
		else
		{
			ModelFunction tmp = getMF().copy();
			tmp.changeByThresh(this.tDX, this.tDY, key);
			thMt = new ModelTraceImplMF(tmp, getTraceLength());
			emptyCacheEntryByKey(key);
			this.thMTCache[key] = thMt;
		}
		return thMt;
	}

	/**
	 * ������ ��������� ������ ������ ��������� ������.
	 * �� ��������.
	 * @param isUpper true - ����� ������� �����, false - ������
	 * @param level ������� ������, �� 0.0 �� 1.0 ������������
	 * @return ����������� ��������� ������
	 */
	private ModelTrace getThresholdMTByLevel(boolean isUpper, double level) {

		// ������� ����� �������
		ThreshDX[] effX = new ThreshDX[this.tDX.length];
		for (int i = 0; i < this.tDX.length; i++)
			effX[i] = this.tDX[i].makeWeightedThresholds(level);
		ThreshDY[] effY = new ThreshDY[this.tDY.length];
		for (int i = 0; i < this.tDY.length; i++)
			effY[i] = this.tDY[i].makeWeightedThresholds(level);

		// ���������� ��������� ������
		ModelFunction tmp = getMF().copy();

		// timing: this call takes 60% CPU time of the whole method CPU time
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
		return this.mtae.getTraceLength();
	}

	/**
	 * ������� ����� DX-�������, � ������� "��� ������ ������� ����� �������
	 * ��������� �������"
	 * @param nEvent ����� ��������� �������
	 * @return ����� DY-�������
	 */
	private ThreshDX[] getSingleEventThreshDX(int nEvent) {
		ThreshDX[] tmpTDX = new ThreshDX[this.tDX.length];
		for (int i = 0; i < tmpTDX.length; i++) {
			tmpTDX[i] = this.tDX[i].isRelevantToNEvent(nEvent)
				? this.tDX[i]
				: this.tDX[i].makeZeroedCopy();
		}
		return tmpTDX;
	}
	/**
	 * ������� ����� DY-������� ����������
	 * {@link #getSingleEventThreshDX(int)}
	 */
	private ThreshDY[] getSingleEventThreshDY(int nEvent) {
		ThreshDY[] tmpTDY = new ThreshDY[this.tDY.length];
		for (int i = 0; i < tmpTDY.length; i++) {
			tmpTDY[i] = this.tDY[i].isRelevantToNEvent(nEvent)
				? this.tDY[i]
				: this.tDY[i].makeZeroedCopy();
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
		if (this.thSingleMTRCacheEventId == nEvent
				&& this.thSingleMTRCache != null)
		{
			Log.debugMessage("getEventThresholdMTR: nEvent " + nEvent + " cache hit",
					FINEST);
			return this.thSingleMTRCache;
		}

		// make thresholds for 'this event only'
		ThreshDX[] tmpTDX = getSingleEventThreshDX(nEvent);
		ThreshDY[] tmpTDY = getSingleEventThreshDY(nEvent);

		// init cache
		this.thSingleMTRCacheEventId = nEvent;
		this.thSingleMTRCache = new ModelTraceRange[] { null, null, null, null };

		// find ranges and curves, fill cache
		for (int key = 0; key < 4; key++)
		{
			SimpleReflectogramEvent sre =
				getEventRangeOnThresholdCurve(nEvent, key, tmpTDX, tmpTDY);
			if (sre != null) {
				ModelFunction tmp = getMF().copy();
				tmp.changeByThresh(tmpTDX, tmpTDY, key);
				this.thSingleMTRCache[key] = new ModelTraceRangeImplMF(tmp,
						sre.getBegin(),
						sre.getEnd());
			} else {
				this.thSingleMTRCache[key] = null;
			}
		}

		// make a copy of resulting array for client
		Log.debugMessage("getEventThresholdMTR: nEvent " + nEvent + " cache miss",
				FINEST);
		return this.thSingleMTRCache.clone();
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
		// ��������� ������
		for (int i = 0; i < this.tDY.length; i++)
			this.tDY[i].changeAllBy(-dyMargin);
		CoreAnalysisManager.extendThreshToCoverCurve(
				this.mtae.getModelTrace().getYArray(), yTop,
				this.tDX, this.tDY, Thresh.SOFT_UP, Thresh.HARD_UP,
				dyFactor);
		CoreAnalysisManager.extendThreshToCoverCurve(
				this.mtae.getModelTrace().getYArray(), yBottom,
				this.tDX, this.tDY, Thresh.SOFT_DOWN, Thresh.HARD_DOWN,
				dyFactor);
		for (int i = 0; i < this.tDY.length; i++)
			this.tDY[i].changeAllBy(dyMargin);
		// ������������ ������
		postProcessThresholds();
		// ��������� ������ ���������� - ���������� ���
		invalidateCache();
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
			super(thId, ModelTraceManager.this.tDX, key, posX, posY, Thresh.IS_KEY_UPPER[key] ? HORIZONTAL_LEFT_TYPE : HORIZONTAL_RIGHT_TYPE);
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
			super(thId, ModelTraceManager.this.tDY, key, posX, posY, Thresh.IS_KEY_UPPER[key] ? VERTICAL_UP_TYPE :  VERTICAL_DOWN_TYPE);
			int posMin = this.th.xMin;
			int posMax = this.th.xMax;
			if (((ThreshDY)this.th).getType() == ThreshDY.Type.dL
					&& thId > 0 && thId < ModelTraceManager.this.tDY.length - 1)
			{
				// �������� ��������� ����� �������� �� ������ 98% ��������� ������
				posMin = ModelTraceManager.this.tDY[thId - 1].xMax;
				posMax = ModelTraceManager.this.tDY[thId + 1].xMin;
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
		ArrayList<Thresh> al = new ArrayList<Thresh>();
		for (int i = 0; i < this.tL.length; i++)
		{
			if (this.tL[i].isRelevantToNEvent(nEvent))
				al.add(this.tL[i]);
		}
		return al.toArray(new Thresh[al.size()]);
	}
	// ���������� '����� �� ���������' ��� ������� �������
	// may return null
	private Thresh getDefaultThreshByNEvent(int nEvent)
	{
		// �������� ������� DL-����� -- ��� �����������
		for (int i = 0; i < this.tDY.length; i++) {
			if (this.tDY[i].isRelevantToNEvent(nEvent)
					&& this.tDY[i].getType() == ThreshDY.Type.dL)
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
			: this.tDX;
		ThreshDY[] tmpTDY = singleEventCurveMode
			? getSingleEventThreshDY(nEvent)
			: this.tDY;
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
		int thId = getMF().
			findResponsibleThreshDXID(tmpTDX, tmpTDY, bestKey, bestX);
		if (thId == -1)
			return null;
		ThresholdHandleDX handle =
			new ThresholdHandleDX(thId, bestKey, bestX, bestY);
		handle.posY = bestY; //getThresholdY(bestKey, handle.posX);
		return handle;
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
					prevRelevant = this.tDX[prevNT].isRelevantToNEvent(nEvent);
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
		if (this.thSRECacheEventId == nEvent && this.thSRECache != null
				&& this.thSRECache[key] != null)
		{
			Log.debugMessage("getEventRangeOnThresholdCurve: nEvent " + nEvent + " cache hit",
					FINEST);
			return this.thSRECache[key];
		}
		
		SimpleReflectogramEvent sre =
			getEventRangeOnThresholdCurve(nEvent, key, this.tDX, this.tDY);

		// ������ � ���
		if (this.thSRECacheEventId != nEvent)
			this.thSRECache = null;
		if (this.thSRECache == null)
			this.thSRECache = new SimpleReflectogramEvent[] {null, null, null, null};
		this.thSRECacheEventId = nEvent;
		this.thSRECache[key] = sre;
		Log.debugMessage("getEventRangeOnThresholdCurve: nEvent " + nEvent + " cache miss",
				FINEST);
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

		int addOne = oneMorePoint ? 1 : 0;

		// ������� ��������� ��� DX-������ � ������ ����� ���� �������,
		// � ����� - ������ ��� ������� (�� ������ ���������� DX-�������).
		for (int i = 0; i < this.tDX.length; i++) {
			int keyU = Thresh.HARD_UP;
			int keyD = Thresh.HARD_DOWN;
			int dxMin = Math.min(this.tDX[i].getDX(keyU), this.tDX[i].getDX(keyD)) - addOne; // negative
			int dxMax = Math.max(this.tDX[i].getDX(keyU), this.tDX[i].getDX(keyD)) + addOne; // positive
			if (this.tDX[i].xMin + dxMin <= x && this.tDX[i].xMax + dxMax >= x) {
				// eventId0 � eventId1 ��� DX-������� �����, ����� eventId0 (?)
				int nEv = this.tDX[i].eventId0;
				Log.debugMessage("findSupposedAlarmEventByPos: tDX: x " + x + ", nEv " + nEv,
						FINEST);
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
				Log.debugMessage("findSupposedAlarmEventByPos: nEv: x " + x + ", nEv " + nEv,
						FINEST);
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
	 * ����� ������ ����� ������, ���� ��������������� ����� ������
	 * �� ��� ������, ���� ��� �������� ������ ����������������� ���������.
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
		return DataStreamableUtil.writeDataStreamableToBA(this.mtae);
	}

	/**
	 * ���������� ��������� ��������� ������ � ������ �������.
	 * @return ModelTraceAndEvents �������
	 */
	public ModelTraceAndEventsImpl getMTAE()
	{
		// �� �����, ��� MTAEI - ������������ (unmodifiable)
		return this.mtae;
	}

	/**
	 * @deprecated use getMTAE().getNEvents()
	 */
	@Deprecated
	public int getNEvents()
	{
		return this.mtae.getNEvents();
	}
	/**
	 * @deprecated use getMTAE().getSimpleEvent()
	 */
	@Deprecated
	public SimpleReflectogramEvent getSimpleEvent(int nEvent)
	{
		return this.mtae.getSimpleEvent(nEvent);
	}

	public void writeToDOS(DataOutputStream dos) throws IOException
	{
		dos.writeLong(SIGNATURE_MTM);
		this.mtae.writeToDOS(dos);
		Thresh.writeArrayToDOS(this.tL, dos);
	}

	private static class DSReader implements DataStreamable.Reader
	{
		public DataStreamable readFromDIS(DataInputStream dis)
		throws IOException, SignatureMismatchException
		{
			if (dis.readLong() != SIGNATURE_MTM)
				throw new SignatureMismatchException();

			ModelTraceAndEventsImpl mtae =
				(ModelTraceAndEventsImpl)ModelTraceAndEventsImpl.
					getReader().readFromDIS(dis);

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

	/**
	 * �������� ��������� ����������. ����� ��������� �������� �������� ���������
	 * L-��������������� �� �����, � ����� ���� �����. ���. � ��������� ���������
	 * ����������, ����� ����� ���� �������� ����� �� �����.
	 * @param mf ��������� ������
	 * @param evBegin ��������� ������ �������
	 * @param evEnd ��������� ����� �������
	 * @return int[3] { ������ ������� (���������� ������), ��������, ��������� ������� (���������� �����) } 
	 */
	private static int[] getConnectorMinMaxMin(ModelFunction mf, int evBegin, int evEnd)
	{
		final int X0 = evBegin;
		final int N = evEnd - evBegin + 1;
		double[] arr = mf.funFillArray(X0, 1.0, N);
		int iMax = ReflectogramMath.getArrayMaxIndex(arr, 0, N - 1);
		int iLMin = ReflectogramMath.getArrayMinIndex(arr, 0, iMax);
		int iRMin = ReflectogramMath.getArrayMinIndex(arr, iMax, N - 1);
		return new int[] {evBegin + iLMin, evBegin + iMax, evBegin + iRMin};
	}

	/**
	 * ���������� ������� DY-������� �� ������ ������� �������.
	 * FIXME: ��������������
	 * @param nEvent ����� �������
	 * @return ��������� ����������������� DY-������� �� ������ ���������� �������
	 */
	public double getDYScaleForEventBeginning(int nEvent) {
		for (int i = 0; i < this.tDY.length; i++) {
			if (this.tDY[i].isRelevantToNEvent(nEvent)) {
				return (Math.abs(this.tDY[i].getDY(Thresh.SOFT_UP))
						+ Math.abs(this.tDY[i].getDY(Thresh.SOFT_DOWN))) / 2.0;
			}
		}
		return 0.0; // ������� �� ������� �������� (� ��������, ��� ���� �� ������)
	}
	/**
	 * ���������� ������� DY-������� �� ����� ������� �������.
	 * FIXME: ��������������
	 * @param nEvent ����� �������
	 * @return ��������� ����������������� DY-������� �� ����� ���������� �������
	 */
	public double getDYScaleForEventEnd(int nEvent) {
		double ret = 0.0;
		for (int i = 0; i < this.tDY.length; i++) {
			if (this.tDY[i].isRelevantToNEvent(nEvent)) {
				ret = (Math.abs(this.tDY[i].getDY(Thresh.SOFT_UP))
						+ Math.abs(this.tDY[i].getDY(Thresh.SOFT_DOWN))) / 2.0;
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		return "MTM(mtae=" + this.mtae.toString()
			+ ",tDX[" + this.tDX.length + "]=" + getObjectArrayHash(this.tDX)
			+ ",tDY[" + this.tDY.length + "]=" + getObjectArrayHash(this.tDY)
			+ ")";
	}

	private static int getObjectArrayHash(Object[] arr) {
		int result = 17;
		for (int i = 0; i < arr.length; i++) {
			result = 37 * result + arr[i].hashCode();
		}
		return result;
	}

	/**
	 * ������ ����-��������� ��������������� �������
	 * <ul>
	 * <li> ������������, ����� ��������� DY-������ ���� �� ����� 0.5 ��
	 * <li> ������������, ����� DA-������ �� ������� (� ������, �����
	 * ������ DY-����� ��� �� ������, ��� ����� DA-����� ����� �� ����,
	 * ����� DA-������ � ����� ������ �/�)
	 * <li> ��������� DA �� ������ DZ � ����� EOT �� +10..15 / -50..75 �� ��  
	 * </ul>
	 */
	private void postProcessThresholds() {
		final double minHardAlarm = 0.5;

		/*
		 * ������� ����������� ����������� �������� �������
		 * (������ ThreshDY ������������ ������ ��������� ��������)
		 */
		ThreshDY acc = new ThreshDY(0, ThreshDY.Type.dA, 0, 0);
		acc.setDY(Thresh.HARD_UP, minHardAlarm);
		acc.setDY(Thresh.HARD_DOWN, -minHardAlarm);

		/*
		 * ��������� ������. ������������, ��� ������ � tDY ���� ����� �������
		 */
		for(ThreshDY th: this.tDY) {
			// ��������� ������ th
			th.extendUpto(acc);
			// ��������� acc ��� ���� th dA-����, ����� ������� (������� �� ��)
			if (th != this.tDY[0] && th.getType() == ThreshDY.Type.dA) {
				acc.extendUpto(th);
			}
		}

		/*
		 * ��������� ������ �� ������ DZ � ����� EOT
		 */
		acc.setToZero();
		acc.setDY(Thresh.SOFT_UP, 10.0);
		acc.setDY(Thresh.HARD_UP, 15.0);
		acc.setDY(Thresh.SOFT_DOWN, -50.0);
		acc.setDY(Thresh.HARD_DOWN, -75.0);
		if (this.tDY.length > 0) {
			this.tDY[0].extendUpto(acc);
			this.tDY[this.tDY.length - 1].extendUpto(acc);
		}
	}

	public void increaseAllDyThresholds() {
		for (ThreshDY th: this.tDY) {
			th.changeAllBy(0.1); // XXX: move this constant outside
		}
		invalidateCache();
	}

	public void decreaseAllDyThresholds() {
		for (ThreshDY th: this.tDY) {
			th.changeAllBy(-0.1); // XXX: move this constant outside
		}
		invalidateCache();
	}
}
