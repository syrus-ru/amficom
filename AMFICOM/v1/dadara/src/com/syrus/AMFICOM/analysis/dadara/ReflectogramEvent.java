/**
 * ReflectogramEvent.java
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/28 09:07:15 $
 * @author $Author: saa $
 * @module general_v1
 */

/*
 * ��� �������� �������� ��������:
 * 1. Native-��� AnalysisManager'�
 * 2. static ����� fromByteArray() 
 */

// TODO: ����������� ������ �������� ���� �� �������

package com.syrus.AMFICOM.analysis.dadara;

import java.util.Map;
import java.util.Iterator;
import com.syrus.io.BellcoreStructure;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReflectogramEvent
{
    //private static final long serialVersionUID = 8468909716459300200L;
    private static final long SIGNATURE = 8468920041210182200L;
    
    // event types
	public static final int RESERVED_VALUE = -1; // �������� �� �������������� ��������
	public static final int LINEAR = 1;
	public static final int WELD = 2;
	public static final int CONNECTOR = 3;
	public static final int SINGULARITY = 4;

	// store flags for int vars
	private static final int STORE_MASK_INT		= 0xff;
	private static final int STORE_LINK_FLAGS	= 0x01;

	// store flags for long vars
	private static final int STORE_MASK_LONG	= 0xff00;

	// store flags for double vars
	private static final int STORE_MASK_DOUBLE	= 0xff0000;
	private static final int STORE_YB			= 0x010000;
	private static final int STORE_YE			= 0x020000;
	private static final int STORE_A_LET		= 0x040000;
	private static final int STORE_ASYMP_Y0		= 0x080000;
	private static final int STORE_ASYMP_Y1		= 0x100000;
	private static final int STORE_MLOSS		= 0x200000;

	private static final int STORE_FLAGS
		= STORE_YB
		| STORE_YE
		| STORE_A_LET
		| STORE_ASYMP_Y0
		| STORE_ASYMP_Y1
		| STORE_MLOSS
		| STORE_LINK_FLAGS;

	private static final int LINK_FIXLEFT = ModelFunction.LINK_FIXLEFT;

	private int begin; // ��������� �����
	private int end; // ��������� ����� ������������: ����� = end - begin + 1
	private int eventType; // ��� ������� (��� ����������)
	private int thresholdType; // ��� ��� ����� (��� ������)
	private ModelFunction mf;
	private int linkFlags;
	private double deltaX = 1.; // XXX

	private FittingParameters fittingParameters;

	private Threshold threshold;

	// XXX
	// "������� ��������"
	// ����������� �� ����� doFit()
	// ��� ���. ������� - �� ������ ���. �������.
	// ��� ���. ������� - �� ������ �������� �/� � �������� ������
	private double yB; // Y � ����� begin
	private double yE; // Y � ����� end

	// XXX
	// ������������� � ������ ������� ������� �� ������ calcMutualParameters
	// ��� ���. ������� - �� ��� ������. �������.,
	// ��� ���. � ���. ������� - �� ������,
	// ��� ���. � ���. ������� (��� ��� ������) - �� �������� �/� � �����
	private double asympY0; // �����
	private double asympY1; // ������

	//////////////////////
	// loss, reflectance -- ������������ � ���������,
	// ��� �� ����������� �� ������.
	// TODO: �������� � ���� ������������ ������
	// (������� ���� ����� �� getter/setter-�������)
	// mloss ������ �������� - ������ ��� �����. ������� ������ ��
	// ���� �������� �����. ���������, � �� ������
	// (FIXME - ��� ������) ������ �������� �/� �� ����� �������
	private double mloss; // ��� ���. ������� - ��������, ��� ���. - (-1) * ����������
	private double aLet; // �������� �������� ����� (��� ����������� ���������)
	
	// cache part

	private ReflectogramEvent[] cacheThresholdData; // 4 elements
	private long cacheThresholdSerialNumber;
	private double cacheRefAmplitude[]; // end + 1 - begin elements
	
	// left-right linking
	
	private ReflectogramEvent eventAtLeft;
	private ReflectogramEvent eventAtRight;
	
	// methods
	
	private ModelFunction getThresholdBaseMF()
	{
		ModelFunction ret;
		if (thresholdType == ReflectogramEvent.LINEAR && false) // true �������� � ����, ��� ������ �������� ��� ��
		{
			ret = ModelFunction.createLinear();
			ret.setAsLinear(begin, yB, end, yE); // NB: needs yB, yE
		}
		else
			ret = mf.copy();
		return ret;
	}
	
	public int getBegin()
	{
		return begin;
	}

	public int getEnd()
	{
		return end;
	}

	public void setBegin(int x)
	{
		// XXX: need to refit mf?
		begin = x;
		updated();
	}

	public void setEnd(int x)
	{
		// XXX: need to refit mf?
		updated();
		end = x;
	}

	public int getEventType()
	{
		return eventType;
	}

	public void setEventType(int newType)
	{
		eventType = newType; // XXX, it is used but not possible!
		thresholdType = newType;
		updated();
		// todo: recalc yB, yEp?
	}

	public int getThresholdType()
	{
		return thresholdType;
	}

	public void shiftY(double dy)
	{ // seems to be used in comparison
		mf.shiftY(dy);
		updated();
	}

	//  seems to be used only in event-based comparer (to get nearest event)
	// XXX: i guess the use of getMiddle in such a way is not a best idea //saa
	public int getMiddle()
	{
		return (begin + end) / 2;
	}

	public Threshold getThreshold()
	{
		return threshold;
	}

	public void setThreshold(Threshold threshold)
	{
		this.threshold = threshold;
		cacheThresholdInvalidate();
	}

	/*
	 * public int steal_shapeID() { return mf.steal_shapeID(); }
	 */

	public void setDeltaX(double delta_x)
	{
		this.deltaX = delta_x;
		updated();
	}

	public double getDeltaX()
	{
		return this.deltaX;
	}

	// ����� �������� ������ ����� doFit()
	private double getAsympYB()
	{
		return yB;
	}
	private double getAsympYE()
	{
		return yE;
	}
	
	// ������ ����� doFit() + calcMutialParameters()
	public double getAsympY0()
	{
		return asympY0;
	}

	public double getAsympY1()
	{
		return asympY1;
	}

	public double getWidth0()
	{
		return (end - begin) * deltaX;
	}

	/*
	 * public double getWidth() { if (getType() == LINEAR) return end - begin;
	 * else return mf.getWidth(); }
	 */

	// XXX: �� ����� ������� ������ ����������� ��������� ����������
	// XXX: �������������� �������� - ������������� ��������������� ���������
	// XXX: ������������ ��������� ����������
	private double getYMax1()
	{
		double yMax = refAmplitude(begin);
		for (int i = begin + 1; i <= end; i++)
		{
			if (yMax < refAmplitude(i))
				yMax = refAmplitude(i);
		}
		return yMax;
	}

	// ������ ����������
	// ����� ��������������� doFit()
	public double getMLoss()
	{
		return mloss;
	}
	public double getALet()
	{
		return aLet;
	}

	public static void createLeftRightLinks(ReflectogramEvent[] re)
	{
		int len = re.length;
		re[0].eventAtLeft = null;
		for (int i = 1; i < len; i++)
			re[i].eventAtLeft = re[i - 1];
		re[len - 1].eventAtRight = null;
		for (int i = 0; i < len - 1; i++)
			re[i].eventAtRight = re[i + 1];
	}

	/**
	 * ��������� ���� mloss � reflectance, ���������� ���� ������ �������.
	 * (������ ����-������)
	 * 
	 * TODO: ������� ��� �� ����� ������ � �����������, ���� ���-�� ���
	 * (��������, � IA)
	 * 
	 * @param re
	 *            ������ �������
	 * @param y
	 *            ��������������, �� ������� �� �������
	 */
	public static void calcMutualParameters(ReflectogramEvent[] re, double[] y)
	{
		for (int i = 0; i < re.length; i++)
		{
			// ���������� "���������������" �������� ����� � ������
			// ���� ��� ������� ����������,
			// � ��������������� ������� ������� ��������,
			// �� ����� �� ��������, ����� - �� ������.
			// TODO: ����������?

			double asympB = re[i].getAsympYB();
			double asympE = re[i].getAsympYE();
			if (re[i].getEventType() != ReflectogramEvent.LINEAR)
			if (i > 0 && re[i - 1].getEventType() == ReflectogramEvent.LINEAR)
				asympB = re[i - 1].getAsympYE();
			if (i < re.length - 1
					&& re[i + 1].getEventType() == ReflectogramEvent.LINEAR)
				asympE = re[i + 1].getAsympYB();

			re[i].mloss = asympE - asympB;

			double yMax = y[re[i].getBegin()];
			for (int j = re[i].getBegin(); j < re[i].getEnd(); j++)
			{
				if (yMax < y[j])
					yMax = y[j];
			}

			// XXX: ������ asympB ����� ����� ����������� �������� � �����
			// ����������� �������
			re[i].aLet = yMax - asympB;
			re[i].asympY0 = asympB;
			re[i].asympY1 = asympE;
		}
		
		// ������, ����� � ����� ������� ������ createLeftRightLinks,
		// ������ ���� ����� ������� �� calcMutualMarametes - TODO

		createLeftRightLinks(re);
	}

	// ������������ ������ �������� �� ���� ����� �������
	private void refAmplitudeFillCache()
	{
		cacheRefAmplitude = mf.funFillArray(begin, 1, end - begin + 1);
	}

	public double refAmplitude(int x)
	{
		// �������� �� [begin .. end] ���� � ����,
		// ��� ������������� ��������� ���
		if (x >= begin && x <= end)
		{
			if (!cacheRefAmplitudeIsValid())
				refAmplitudeFillCache();

			return cacheRefAmplitude[x - begin];
		} else
		{
			//System.out.println("O: "+this+" [ "+x+" !in
			// ["+begin+";"+end+"]");
			//if (x != 0)
			//	throw new Error();
			return mf.fun(x);
		}
	}

	/*
	 * public void doFit(double[] y, int fitMode) { doFit(y, fitMode, 0, 0.0,
	 * 0.0); }
	 */

	public static class FittingParameters
	{
		protected double[] y;
		protected double[] noise;
		protected double error1;
		protected double error2;
		public FittingParameters(double[] y, double error1, double error2)
		{
			this.y = y;
			this.noise = null;
			this.error1 = error1;
			this.error2 = error2;
		}
		public FittingParameters(double[] y, double noise[])
		{
			this.y = y;
			this.noise = noise;
			this.error1 = 0;
			this.error2 = 0;
		}
	}

	// errorMode:
	//	0: default mode (best fit)
	//  2: rgdB noise level is supplied

	public void setFittingParameters(FittingParameters fPars)
	{
		this.fittingParameters = fPars;
	}
	public void doFit()
	{
		int maxpoints = eventType == CONNECTOR
							? 25 // FIXIT
							: 5; // FIXIT
		FittingParameters fP = fittingParameters;
		if (fP.noise == null)
			mf.fit(fP.y, begin, end,
				activeLinkFlags(), activeLinkData0(),
				fP.error1, fP.error2, maxpoints);
		else
			mf.fit(fP.y, begin, end,
				activeLinkFlags(), activeLinkData0(),
				fP.noise);
		
		updated();

		if (eventType == LINEAR)
		{
			//System.out.println("lin fit");
			// ���. ������������� ��� ���. �������
			// (this.mf �� ����������� �������, ��� � � �������)
			ModelFunction lin = ModelFunction.createLinear();
			lin.fit(fP.y, begin, end, 0, 0.0);
			yB = lin.fun(begin);
			yE = lin.fun(end);
		} else
		{
			// ������ ������ - �� ����� �/�
			yB = fP.y[begin];
			yE = fP.y[end];
		}
	}

	public double getRMSDeviation(double[] y)
	{
		return mf.calcRMS(y, begin, end);
	}
	
	// ��������� ���������� ���� ������� �� ���
	private double[] getMaxDeviationPM(ModelFunction that)
	{
		double[] maxDev = new double[2];
		for (int i = begin; i <= end; i++)
		{
			double val = refAmplitude(i) - that.fun(i);
			if (val > maxDev[0])
				maxDev[0] = val;
			if (val < maxDev[1])
				maxDev[1] = val;
		}
		return maxDev;
	}

	public double getMaxDeviation(double[] y)
	{
		double maxDev = 0;
		for (int i = begin; i <= end; i++)
		{
			double val = Math.abs(refAmplitude(i) - y[i]);
			if (val > maxDev)
				maxDev = val;
		}
		return maxDev;
	}
	
	private ReflectogramEvent copyWithoutMFAndTh()
	{
		// �� �������� �� mf, �� �������, �� left-right ������
		ReflectogramEvent ret = new ReflectogramEvent();
		ret.threshold = null;
		ret.begin = begin;
		ret.end = end;
		ret.mf = null;
		ret.deltaX = deltaX;
		ret.mloss = mloss;
		ret.aLet = aLet;
		ret.asympY0 = asympY0;
		ret.asympY1 = asympY1;
		ret.eventType = eventType;
		ret.thresholdType = thresholdType;
		ret.yB = yB;
		ret.yE = yE;
		return ret;
	}

	/**
	 * NOTE: the copy is partially swallow, it do not copy the threshold, only
	 * its reference XXX: I don't know if it matters. //saa It is also partially
	 * deep since it does copy mf, and this does matter.
	 * 
	 * @return the partially swallow copy of this
	 */
	public ReflectogramEvent copy()
	{
		ReflectogramEvent ret = copyWithoutMFAndTh();
		ret.threshold = threshold;
		ret.mf = mf.copy();
		// ����������, ��� ���� � left-right �������
		return ret;
	}

	/**
	 * ������� ����� ������� �������, � ������������� � ����� ������������
	 * ����� left-right, ����������� ��� ������������� leftLink.
	 * ������ ����� ������� ��������� �� ������ ������ �������.
	 * @param re ������� ������ �������
	 * @return ����� �������� �������.
	 */
	public static ReflectogramEvent[] copyArray(ReflectogramEvent[] re)
	{
		int len = re.length;
		ReflectogramEvent[] ret = new ReflectogramEvent[len];
		for (int i = 0; i < len; i++)
			ret[i] = re[i].copy();
		createLeftRightLinks(ret);
		return ret;
	}

	// byte array presentation functions are untested
	public void writeToDOS(DataOutputStream dos) throws IOException
	{
	    dos.writeLong(SIGNATURE);
		dos.writeInt(begin);
		dos.writeInt(end);
		dos.writeInt(eventType);
		dos.writeInt(thresholdType);
		dos.writeDouble(deltaX);
		mf.writeToDOS(dos);
		dos.writeInt(STORE_FLAGS);
		int i;
		for (i = 1; i != 0; i = i * 2)
		{
			if ((STORE_FLAGS & i) == 0)
			    continue;

			switch (i)
			{
			case STORE_YB:
			    dos.writeDouble(yB);
				break;
			case STORE_YE:
			    dos.writeDouble(yE);
				break;
			case STORE_A_LET:
			    dos.writeDouble(aLet);
				break;
			case STORE_MLOSS:
			    dos.writeDouble(mloss);
				break;
			case STORE_ASYMP_Y0:
			    dos.writeDouble(asympY0);
				break;
			case STORE_ASYMP_Y1:
			    dos.writeDouble(asympY1);
				break;
			case STORE_LINK_FLAGS:
			    dos.writeInt(linkFlags);
				break;
			}
		}
		// we do not write thresholds
	}

	/**
	 * ��������� ��� ��������� ������� � ������.
	 * ����� ������ private �� ������ ������ �� �������,
	 * �����, ��������, ����� ����������� �� ��� ���������
	 * � �������� ��������� �� � �������������� ������� �������.
	 * @param dis ������� �����
	 * @throws IOException, SignatureMismatchException
	 */
	private void readFromDIS(DataInputStream dis) throws IOException, SignatureMismatchException
	{
	    long tsig = dis.readLong();
	    if (tsig != SIGNATURE)
	    {
	        throw new SignatureMismatchException();
	    }
		begin = dis.readInt();
		end = dis.readInt();
		eventType = dis.readInt();
		thresholdType = dis.readInt();
		deltaX = dis.readDouble();
		mf = ModelFunction.createFromDIS(dis);
		int flags = dis.readInt();
		int i;
		for (i = 1; i != 0; i = i * 2)
		{
			if ((flags & i) != 0)
			{
			    int ti = 0;
			    long tl = 0; // unused
			    double td = 0;
			    if ((i & STORE_MASK_INT) != 0)
			        ti = dis.readInt();
			    if ((i & STORE_MASK_LONG) != 0)
			        tl = dis.readLong();
			    if ((i & STORE_MASK_DOUBLE) != 0)
			        td = dis.readDouble();
				switch (i)
				{
				case STORE_YB:
					yB = td;
					break;
				case STORE_YE:
					yE = td;
					break;
				case STORE_A_LET:
					aLet = td;
					break;
				case STORE_MLOSS:
					mloss = td;
					break;
				case STORE_ASYMP_Y0:
					asympY0 = td;
					break;
				case STORE_ASYMP_Y1:
					asympY1 = td;
					break;
				case STORE_LINK_FLAGS:
					linkFlags = ti;
				}
			}
		}
		// do not read thresholds

		updated();
	}

	// xtodo: change name: toByteArray -> arrayToBytes
	// xtodo: use stream operations instead of byte arrays - for simplicity
	public static byte[] toByteArray(ReflectogramEvent[] revents)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeInt(revents.length);
			for (int i = 0; i < revents.length; i++)
				revents[i].writeToDOS(dos);
			return baos.toByteArray();
		} catch (IOException e)
		{
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return new byte[0]; //null // XXX
		}
	}

	public static ReflectogramEvent[] fromByteArray(byte[] bar)
	{
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(bar);
			DataInputStream dis = new DataInputStream(bais);
			int len = dis.readInt();
			ReflectogramEvent[] ret = new ReflectogramEvent[len];
			for (int i = 0; i < len; i++)
			{
				ret[i] = new ReflectogramEvent();
				ret[i].readFromDIS(dis);
			}
			// ������������� left-right �����
			createLeftRightLinks(ret);
			return ret;
		}
		catch (IOException e)
		{
			System.out.println("IOException caught: " + e);
			e.printStackTrace();
			return new ReflectogramEvent[0]; //null // XXX
		}
		catch (SignatureMismatchException e)
		{
			System.out.println("SignatureMismatchException caught: " + e);
			e.printStackTrace();
			return new ReflectogramEvent[0]; //null // XXX
		}
	}

	private ReflectogramEvent makeThresholdEvent(Threshold th,
			int thresholdNumeral)
	{
		ReflectogramEvent ret = this.getThresholdBaseRE();
		double[] thresholdValues = th.getThresholds(thresholdNumeral);
		switch (thresholdType)
		{
		case LINEAR:
			// fall through
		case WELD:
			thresholdValues[1] = 0;
			thresholdValues[2] = 0;
			// fall through
		case SINGULARITY:
			// fall through
		case CONNECTOR:
			break;
		}

		ret.mf.changeByACXLThreshold(thresholdValues);
		return ret;
	}

	public ReflectogramEvent getThresholdReflectogramEvent(int thresholdNumeral)
	{
		//����������� ���������� makeThresholdEvent � thresholdsCache
		if (thresholdNumeral < 4)
		{
			if (!cacheThresholdIsValid())
			{
				cacheThresholdData = new ReflectogramEvent[4];
				cacheThresholdSerialNumber = threshold.getSerialNumber();
			}

			if (cacheThresholdData[thresholdNumeral] == null)
			{
				cacheThresholdData[thresholdNumeral] = makeThresholdEvent(
						threshold, thresholdNumeral);
			}

			return cacheThresholdData[thresholdNumeral];
		}
		return makeThresholdEvent(threshold, thresholdNumeral);
	}

	public void changeThresholdType(int newType, double[] y)
	{
		thresholdType = newType;
		System.out.println("changeThresholdType: y = " + y);
		if (y != null)
			setDefaultThreshold(y);
		cacheThresholdInvalidate();
	}

	private int activeLinkFlags()
	{
		return
			eventAtLeft != null
			&& ((linkFlags & LINK_FIXLEFT) != 0)
			&& eventAtLeft.end == begin
						? LINK_FIXLEFT
						: 0;
	}

	// XXX:
	// ����� ���������� ���������� �������� ������ ����� ����
	// ��� ����� ������� �������������.
	// ��������� ����� ������������ � ���������, �� ������
	// ���������� �� ��, ��� ��������� ����� ����������� ����� �������.
	private double activeLinkData0()
	{
		return ((activeLinkFlags() & LINK_FIXLEFT) != 0)
		? eventAtLeft.mf.fun(begin) // �������� ������ ������� �� ����� ������� 
		: 0;
	}

	// ���������� �����, ������������� ��� ��������� �������,
	// ���������� � ��������� � �������� ������.
	// ���� ������� primaryTrace � bellcoreTraces,
	// � ����� bellcoreTraces ���� ���� �� ��� ����������� �� ����������
	// � primaryTrace, �� ��������� ����� ������
	// ���������� �� �������� �/�
	// ���� ������ primaryTrace, �� ������ ��������� � ������ y
	// <br>
	// NB: ��� ������� ������ ���������� ����� calcMutualParameters
	// �.�. ��� ���������� ���� leftLink � ������� ����� �������� ��
	// ������� �������. TODO(?): ���� ������ �� ������� ������� ���,
	// �� �� ������, � ������ ������������ leftLink.
	protected void setDefaultThreshold(double[] y, BellcoreStructure primaryTrace, Map bellcoreTraces)
	{
		//System.out.println("setDefaultThreshold: pTrace " + primaryTrace + " bellcoreTraces " + bellcoreTraces);
		//if (bellcoreTraces != null)
		//    System.out.println("bellcoreTraces.size() " + (bellcoreTraces.size()));

		// ������������, ��������������� ������ ����
		// �������� �� ������� ��������� ������� ������� � �/�
		double noiseDev = Math.max(getMaxDeviation(y) * 1.0, // XXX
				getRMSDeviation(y) * 5.0); // XXX

		// ��������� ������ ���������� ������ ���������� ��� �����������
		double noiseSL = mf.getEstimatedNoiseSuppressionLength();
		double typicalAverageLength = 10; // NetTest Specific (8..30) // XXX
		
		// ������ ���������� ���������� ������
		double noiseModel =
			noiseSL > typicalAverageLength
					? noiseDev / Math.sqrt(noiseSL / typicalAverageLength)
					: noiseDev;
					
		// ���������� ����. � ���. ������ ���������� � ��������� ������
		double maxDevP = 0;
		double maxDevM = 0;
		//int noiseSetCount = 0;
		BellcoreStructure pTrace = primaryTrace;
		if (pTrace != null && bellcoreTraces != null)
		{
			int count = 0;
			for (Iterator it = bellcoreTraces.keySet().iterator(); it.hasNext();)
			{
				// ����� ��������� �/� �� ������
				Object key = it.next();
				BellcoreStructure bs = (BellcoreStructure )bellcoreTraces.get(key);
				
				// ���������� ��������������, ������������� �� �������� ��������� 
				final boolean strictMode = true;
				if (bs.getRange() != pTrace.getRange()
						|| bs.getResolution() != pTrace.getResolution()
						|| strictMode
						&& (bs.getWavelength() != pTrace.getWavelength() || bs.getPulsewidth() != pTrace.getPulsewidth()))
				{
					//System.out.println("ReflectogramEvent: setDefaultThresholds: reflectogram ignored: " + key.toString()); // ������� �������� ����� - ������� ������ �� ������ �������
					continue;
				}
				//System.out.println("ReflectogramEvent: setDefaultThresholds: reflectogram accepted: " + key.toString());

				// ���������� ��������������, ������������� �� �����
				double[] yt = bs.getTraceData();
				if (yt == null || yt.length != y.length)
					continue;

				// ������� maxDev ����� ��������� �������
				ModelFunction temp = mf.copy();
				temp.fitLinearOnly(yt, begin, end, activeLinkFlags(), activeLinkData0());
				double[] maxDev = getMaxDeviationPM(temp);
				//System.out.println("ReflectogramEvent: setDefaultThresholds: maxDev = (" + maxDev[0] + ", " + maxDev[1] + ")");

				count++;

				// ��� ���������� ���� ���������� ��� ������� �� ����,
				// � getMaxDeviationPM ��������� ���������� ���� �� ���.
				// ����� �� ����� ���, ������� maxDevP � maxDevM ������� � �������
				if (-maxDev[1] > maxDevP)
					maxDevP = -maxDev[1];
				if (-maxDev[0] < maxDevM)
					maxDevM = -maxDev[0];
			}
			//System.out.println("setDefaultThreshold: count " + count);
		}

		// ������������� ������

		double maxDiff = maxDevP - maxDevM; // �������
		double width0 = noiseModel * 1.0 + maxDiff * 0.1; // XXX

		//System.out.println("ReflectogramEvent: setDefaultThresholds: noiseModel " + noiseModel + " maxDevP " + maxDevP + " maxDevM " + maxDevM);

		threshold.initFromDY(
			maxDevM - width0 * 2,
			maxDevM - width0,
			maxDevP + width0,
			maxDevP + width0 * 2);
	}

	public void setDefaultThreshold(double[] y)
	{
		setDefaultThreshold(y, null, null);
	}

	public void setDefaultThreshold(BellcoreStructure primaryTrace, Map allTraces)
	{
		double y[] = primaryTrace.getTraceData();
		setDefaultThreshold(y, primaryTrace, allTraces);
	}

	private boolean allowsLeftLinking()
	{
		return mf.allowsLeftLinking();
	}

	// returns true if event has changes
	// needs fitting parameters already initialized (FIXME?)
	public boolean setLeftLink(boolean leftLink)
	{
		int newLinkFlags = linkFlags & ~LINK_FIXLEFT;
		if (leftLink && allowsLeftLinking())
			newLinkFlags |= LINK_FIXLEFT;
		if (newLinkFlags == linkFlags)
			return false;
		linkFlags = newLinkFlags;
		doFit();
		updated();
		return true;
	}

	public boolean getLeftLink()
	{
		return (linkFlags & LINK_FIXLEFT) != 0;
	}

	/**
	 * ���������, ��� ��������� ������� ������� �������
	 * ����� � �������� ����� �������,
	 * ������� ������ ����� ������ �� ������.
	 * ������ � ����� ������� ������ ���������.
	 * @param that ������� ��������� �������
	 * @param softAlarms ���������, ��� ���� ���������� � soft-��������
	 *  (����� ������������ � hard-��������)
	 * @return -1 ���� � �������� �������;
	 *  ����� [begin..end] - ���������� ������ �� ������. 
	 */
	public int isThatWithinMyThresholds(ReflectogramEvent that, boolean softAlarms)
	{
		ReflectogramEvent up = getThresholdReflectogramEvent(
			softAlarms ? Threshold.UP1 : Threshold.UP2);
		ReflectogramEvent down = getThresholdReflectogramEvent(
			softAlarms ? Threshold.DOWN1 : Threshold.DOWN2);

		for (int i = begin; i < end; i++)
		{
			double thatVal = that.refAmplitude(i);
			if (thatVal > up.refAmplitude(i)
					|| thatVal < down.refAmplitude(i))
				return i;
		}
		return -1;
	}

	/**
	 * ������� ������� � ��������� ��������, ����������
	 * �� this �������� ���������� �� ������� �������.
	 * @param y ��������������, �� ������� �������� ���. ���.
	 * @return ������� � ������� ��������� ��������
	 */
	public ReflectogramEvent createLinearlyFitted(double[] y)
	{
		ReflectogramEvent ret = copyWithoutMFAndTh();
		ModelFunction temp = mf.copy();
		ret.mf = temp;
		temp.fitLinearOnly(y, begin, end,
			activeLinkFlags(), activeLinkData0());
		return ret;
	}

	// cache methods

	private void cacheThresholdInvalidate()
	{
		cacheThresholdData = null;
	}

	private boolean cacheThresholdIsValid()
	{
		return cacheThresholdData != null
				&& cacheThresholdSerialNumber == threshold.getSerialNumber();
	}

	private void cacheRefAmplitudeInvalidate()
	{
		cacheRefAmplitude = null;
	}

	private boolean cacheRefAmplitudeIsValid()
	{
		return cacheRefAmplitude != null;
	}

	private void updated()
	{
		cacheRefAmplitudeInvalidate();
		cacheThresholdInvalidate();
	}

	// end of cache methods

	// XXX: ����� ����������?
	// �����, ����� ������ ���������� �� ������������� RE � ������� � ������ �������?
	private ReflectogramEvent getThresholdBaseRE()
	{
		ReflectogramEvent ret = this.copyWithoutMFAndTh();
		ret.threshold = null; // undefined -- XXX
		ret.mf = getThresholdBaseMF();
		return ret;
	}
   
	private ReflectogramEvent()
	{
		threshold = new Threshold(); // ��� �������� ����� �������� �� ���������
	} // FIXIT

}

