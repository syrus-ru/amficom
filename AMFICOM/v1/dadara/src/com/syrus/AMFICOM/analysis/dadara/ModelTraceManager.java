/*
 * $Id: ModelTraceManager.java,v 1.43 2005/04/11 10:35:31 saa Exp $
 * 
 * Copyright � Syrus Systems.
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
 * @version $Revision: 1.43 $, $Date: 2005/04/11 10:35:31 $
 * @module
 */
public class ModelTraceManager
{
	protected static final long SIGNATURE_THRESH = 3353620050119193102L;
	public static final String CODENAME = "ModelTraceManager";

	private ModelTraceAndEventsImpl mtae;
	private ModelTrace[] thMTCache = null;
	protected Thresh[] tL; // ������ ������ �������
	protected ThreshDX[] tDX; // ������ DX-�������
	protected ThreshDY[] tDY; // ������ DY-�������

	protected void invalidateThMTCache()
	{
		thMTCache = null;
	}

	// ������� ������ ���� ��������� ������ key
	protected void emptyThMTEntry(int key)
	{
		if (thMTCache == null)
			thMTCache = new ModelTrace[] { null, null, null, null };
		thMTCache[key] = null;
	}

	// ������� ������ ����, ��������� �� ����� key � �������
	// (��� key � Thresh.CONJ_KEY[key])
	protected void invalidateThMTByKey(int key)
	{
		emptyThMTEntry(key);
		emptyThMTEntry(Thresh.CONJ_KEY[key]);
	}

	protected boolean isThMFCacheValid(int key)
	{
		return thMTCache != null && thMTCache[key] != null;
	}

	// ������� ������
	private void createTH()
	{
		LinkedList thresholds = new LinkedList();
		Thresh last = null; // ����� ����� ������ ��������� �� ������� ����� A-����, ���� null, ���� ������ (� ���. �/� ���� ����� �/�� ���)
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
				last.xMax = evBegin;
				if (evBegin < evEnd)
					evBegin++;
				if (evBegin < evEnd)
					evEnd--;
				thresholds.add(new ThreshDY(i, false, evBegin, evEnd));
				last = null;
				break;
			case SimpleReflectogramEvent.REFLECTIVE:
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

	protected SimpleReflectogramEventImpl[] getSE()
	{
		return mtae.getSE();
	}

	private void setTL(Thresh tl[])
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

	/** ������� MTM �� ������ ModelTraceAndEvents
	 * 
	 * @param mtae ������� ������
	 */
	public ModelTraceManager(ModelTraceAndEventsImpl mtae)
	{
		this.mtae = mtae;
		createTH();
	}
	/**
	 * ������� MTM �� ������ ��������� ������ mf � ������ ������� se.
	 * �� �������� ���������� mf, se; ����� ������������ ��� ���
	 * �� ����� ���������� �����.
	 * @param se ������ �������
	 * @param mf ��������� ������
	 * @param deltaX ���������� (����� �� ������)
	 */
	public ModelTraceManager(SimpleReflectogramEventImpl[] se, ModelFunction mf, double deltaX)
	{
		this(new ModelTraceAndEventsImpl(se, mf, deltaX));
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
			invalidateThMTCache(); // ���������� ��� ���� ������
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
			// FIXME - remove
			//for (int i = 0; i < 500; i++)
			//	mf.copy().changeByThresh(tDX, tDY, key);

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
	 * ������ ��������� ������ ���������� ������ �� ������� ���������� �������,
	 * �����, ��� ����� �� �� �������� �������� ������ ��� ������.
	 * ������������ ����� ���������� ����������� (� ���� �������� ���������� ���
	 * ������ nEvent).
	 * ������������������ - �� ����� ������.
	 * FIXME: ������ ���������� nEvent � �������� getThresholdMT(key)
	 * @param key ��� ������ (Threshold)
	 * @param nEvent ����� �������, ��� �������� ������ ����� ������������� �����
	 * @return ��������� ������ ������������ ������, ������������ �� ����� ���� �/�
	 */
	public ModelTrace getEventThresholdMT(int key, int nEvent)
	{
		return getThresholdMT(key); // FIXME
	}

	public void updateUpperThreshToContain(double[] yTop)
	{
		CoreAnalysisManager.nExtendThreshToCoverCurve(getModelTrace().getYArray(), yTop,
			tDX, tDY, Thresh.SOFT_UP, Thresh.HARD_UP);
	}

	public ModelTrace getModelTrace()
	{
		return mtae.getModelTrace();
	}

	public void updateLowerThreshToContain(double[] yTop)
	{
		CoreAnalysisManager.nExtendThreshToCoverCurve(getModelTrace().getYArray(), yTop,
			tDX, tDY, Thresh.SOFT_DOWN, Thresh.HARD_DOWN);
	}

	public void setDefaultThreshold(int nEvent)
	{
		throw new UnsupportedOperationException(); // FIXME
		//invalidateThMFCache();
		//re[nEvent].setThreshold(new Threshold());
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
		// XXX: ��� ���������� � MTM � ������ ������ tl ����������� tl?
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

	/**
	 * ��������� ��� ���������� �������� �����
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
			invalidateThMTCache(); // ���������� ��� ���� ������
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
				invalidateThMTByKey(this.key);
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
	 * ������ handle ��� ��������� �������� ������� �����.
	 * ���� ����������, ����� ����� ������ ������� ����� ����������.
	 * ������ ���������� �������� ��������� ������������ ���������,
	 * ����������� ��� ��������������.
	 * @param x0 ��������� x-���������� (����������� �������, �� ���.)
	 * @param y0 ��������� y-���������� (��)
	 * @param xCapture ������ ������� ������ ����� �� �����������
	 * @param yCapture ������ ������� ������ ����� �� ���������
	 * @param prioFactor �������� �� ��������� �������,
	 *     =0: ����������� ���; =1: 100% ��������� HARD-�������
	 * @param button ����� ������ ����, 0=LMB, 1=RMB. 
	 * @return handle ���� null
	 */
	public ThresholdHandle getThresholdHandle(double x0, double y0, double xCapture, double yCapture, double prioFactor, int button)
	{
		if (xCapture <= 0.1)
			xCapture = 0.1;  // XXX
		if (xCapture > 5000)
			xCapture = 5000; // XXX
		if (yCapture <= 1e-4)
			yCapture = 1e-4; // XXX: ����������� ��� �������������� ��������

		int xRange = (int )(xCapture + 1);

		// ���������� ��������� ��������� ������ � ��������� ����� �� ���� ������
		// UP2/DOWN2 takes precedence over UP1/DOWN1
		double bestDR = 2; // ���������� ����������=1; 2 => ��� �� �������  
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
			double curBestDR = 2; // >1 => ��� �� �������
			int curBestX = 0;
			for (int i = 0; i <= W; i++)
			{
				double dX = (x0 - xL - i) / xScale;
				double dY = (y0 - yArr[i]) / yScale;
				double scalar;
				double dR;
				if (i == W) // ��������� ����� - ������������� ���������� ������ �� ��� �����
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
			}
		}

		if (bestDR > 1)
			return null;

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

	// XXX: ������ ����� ��� ����������� �������, ���������������� ������� �������
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

	public static ModelTraceManager eventsAndTraceFromByteArray(byte[] bar)
	{
		ModelTraceAndEventsImpl mtae = ModelTraceAndEventsImpl.eventsAndTraceFromByteArray(bar);
		return new ModelTraceManager(mtae);
	}

	public byte[] eventsAndTraceToByteArray()
	{
		return mtae.eventsAndTraceToByteArray();
	}

	public ModelTraceAndEvents getMTAE()
	{
		return new UnmodifiableModelTraceAndEvents(mtae);
	}
}
