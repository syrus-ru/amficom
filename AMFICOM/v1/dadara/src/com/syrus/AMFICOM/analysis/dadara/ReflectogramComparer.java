package com.syrus.AMFICOM.analysis.dadara;


//*

/**
 * ReflectogramComparer
 * 
 * ���������� ���������� �������.
 * ������� �� ���� ������.
 * 
 * (1) ������������� ����� - ������������ ������� ������
 * ������ ReflectogramComparer, ��� �� ����� ��� �������
 * ComplexReflectogramEvent[] - ���� - ������� �������, ������ - ���������.
 * (���� �������������� ���� �� ������������).
 * ��������� ������ ������� ������������ �������, � ����� �����
 * ����� �������� �� ������� �� ��������� ������� � ���������� �������.
 * 
 * (2) ����������� ����� - ���������� ��������� ������, �����������
 * �� ModelTrace - MaxDeviation � ��.
 * 
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/02/11 14:05:07 $
 * @module analysis_v1
 */
public class ReflectogramComparer
{
	//private ReflectogramEvent[] hardAlarms;
	//private ReflectogramEvent[] softAlarms;
	//double[] probeTrace;
	private ComplexReflectogramEvent[] probeEvents = null;
	private ComplexReflectogramEvent[] etalonEvents;

	// ����������� ������� ������� �/� �� ������� ���������
	// � ��������.
	// �������� -1 - �����. ������� �� �������.
	// �� ���� ���������, ����������� �������� ������������� �����������
	// probe2etalon � etalon2probe.
	// NB: ������� ������ ������� ������ ���� ������������� �������.
	private int[] probe2etalon = null;
	private int[] etalon2probe = null;

	//public static long COMPARE_ANALYSE = 0x1;
	//public static long COMPARE_EVALUATE = 0x2;

	public ReflectogramComparer(
			//double[] _probeTrace, // may be null if evaluation not desires
			ComplexReflectogramEvent[] _probeEvents, // may be null if analysis not performed
			ComplexReflectogramEvent[] _etalonEvents // not null
			//Threshold[] etalonThresholds // may be null if evaluation not desired
			)
	{
		probeEvents = _probeEvents;
		//probeTrace = _probeTrace;

		// ��������� ������ � ��������� ��������.
		// XXX: ������� ���� ���. ����� �������� - ��������� ��� ��� ���� ����� ���������.
		// ���� �����, ����� ������� ��� ������������ - ���� - ���� � evaluation, ������ - ���� ��� ����.
		/*
		if (etalonThresholds != null)
		{
			etalonEvents = new ComplexReflectogramEvent[_etalonEvents.length];
	        for (int i = 0; i < _etalonEvents.length; i++)
	        {
	            etalonEvents[i] = _etalonEvents[i].copy();
	            etalonEvents[i].setThreshold(etalonThresholds[i]);
	        }
		}
		else
		*/
		etalonEvents = _etalonEvents;

		// ��������� ������ - ���� ������������ ������� ������� � �����
		if (probeEvents != null)
		{
			probe2etalon = findNearestOverlappingEvent(probeEvents, etalonEvents);
			etalon2probe = findNearestOverlappingEvent(etalonEvents, probeEvents);
			removeNonPaired(probe2etalon, etalon2probe);
			removeNonPaired(etalon2probe, probe2etalon);
		}
	}
	
	public int getEtalonIdByProbeId(int probeId)
	{
	    return probe2etalon[probeId];
	}
	
	public int getProbeIdByEtalonId(int etalonId)
	{
	    return etalon2probe[etalonId];
	}

	public boolean isProbeEventNew(int probeId)
	{
		int etalonId = probe2etalon[probeId];
		return etalonId == -1;
	}

	public boolean isEtalonEventLost(int etalonId)
	{
	    int probeId = etalon2probe[etalonId];
	    return probeId == -1;
	}

	public int[] getNewEventsList()
	{
		// ������� ����� ����� �������
		int count = 0;
		for (int i = 0; i < probe2etalon.length; i++)
			if (isProbeEventNew(i))
				count++;
		// ������� � ��������� ������ ����� �������
		int[] ret = new int[count];
		count = 0;
		for (int i = 0; i < probe2etalon.length; i++)
			if (isEtalonEventLost(i))
				ret[count++] = i;

		return ret;
	}
	
	public static final int CHANGETYPE_AMPL = 0x1;
	public static final int CHANGETYPE_LOSS = 0x2;
	public static final int CHANGETYPE_TYPE = 0x4;
	public static final int CHANGETYPE_NEW_OR_LOST = 0x10;
	
	public static boolean eventsAreDifferent(
			ComplexReflectogramEvent a, // not null
			ComplexReflectogramEvent b, // not null
			int changeType, // one of CHANGETYPE...
			double changeThreshold) // may be zero
	{
		switch(changeType)
		{
		case CHANGETYPE_AMPL:
			return Math.abs(a.getALet() - b.getALet()) > changeThreshold;

		case CHANGETYPE_LOSS:
			return Math.abs(a.getMLoss() - b.getMLoss()) > changeThreshold;

		case CHANGETYPE_TYPE:
			// ��� changeThreshold == 0 � 1 �������� ������ ��������� ������
			return a.getEventType() != b.getEventType();

		case CHANGETYPE_NEW_OR_LOST:
			return false; // ���� ������� ��� ���� - ������ ��������� ���
		}
		// unknown criterion
		return false;
	}

	// ����� ������, ��� ������� ����������,
	// ���� ������� ��������� isEtalonEventLost.
	// � ��������� ������ ����� ������� �� ����������
	public boolean isEtalonEventChanged(int etalonId, int changeType, double changeThreshold)
	{
		int probeId = etalon2probe[etalonId];
		if (probeId == -1) // ������� �������
			return true;

		return eventsAreDifferent(
			etalonEvents[etalonId],
			probeEvents[probeId],
			changeType,
			changeThreshold);
	}

	// ����� ������, ��� ������� ����������,
	// ���� ������� ��������� isProbeEventNew.
	// � ��������� ������ - ������ (��� �������)
	public boolean isProbeEventChanged(int probeId, int changeType, double changeThreshold)
	{
		int etalonId = probe2etalon[probeId];
		if (etalonId == -1) // ������� ���������
			return true;

		return eventsAreDifferent(
			etalonEvents[etalonId],
			probeEvents[probeId],
			changeType,
			changeThreshold);
	}

	// internal events comparison

	private boolean eventsOverlaps(SimpleReflectogramEvent x, SimpleReflectogramEvent y)
	{
		return Math.max(x.getBegin(), y.getBegin()) <= Math.min(x.getEnd(), y.getEnd());
	}

	private int calcEventsDistance(SimpleReflectogramEvent x, SimpleReflectogramEvent y)
	{
		return Math.abs(x.getBegin() - y.getBegin()) + Math.abs(x.getEnd() - y.getEnd());
	}

	private void removeNonPaired(int[] fwd, int[] backwd)
	{
		for (int i = 0; i < fwd.length; i++)
		{
			int j = fwd[i];
			if (j >= 0 && backwd[j] != i)
				fwd[i] = -1;
		}
	}

	private int[] findNearestOverlappingEvent(SimpleReflectogramEvent[] X, SimpleReflectogramEvent[] Y)
	{
		int[] ret = new int[X.length];
		for (int i = 0; i < X.length; i++)
		{
			double bestDistance = -1.0; // Stands for +inf
			int bestJ = -1;
			for (int j = 0; j < Y.length; j++)
			{
				if (eventsOverlaps(X[i], Y[j]))
				{
	                double distance = calcEventsDistance(X[i], Y[j]); 
	                if (bestDistance < 0 || distance < bestDistance)
	                {
	                    bestJ = j;
	                    bestDistance = distance;
	                }
				}
			}
			ret[i] = bestJ;
		}
		return ret;
	}

	//

	private static int[] getChangedProbeEventsList(ComplexReflectogramEvent[] data,
			ComplexReflectogramEvent[] etalon,
			int changeType,
			double changeThreshold)
	{
		if (data == null || etalon == null)
			return new int[0];
		ReflectogramComparer comparer = new ReflectogramComparer(data, etalon);
		int count = 0;
		for (int i = 0; i < data.length; i++)
			if (comparer.isProbeEventChanged(i, changeType, changeThreshold))
				count++;
		int[] ret = new int[count];
		count = 0;
		for (int i = 0; i < data.length; i++)
			if (comparer.isProbeEventChanged(i, changeType, changeThreshold))
				ret[count++] = i;
		return ret;
	}

	// NEW IMPLEMENTATION OF OLD CONTRACT (as far as I guess it)
	public static int[] getNewEventsList(
			ComplexReflectogramEvent[] data,
			ComplexReflectogramEvent[] etalon)
	{
		return getChangedProbeEventsList(data, etalon, CHANGETYPE_NEW_OR_LOST, 0);
	}

	public static int[] getChangedAmplitudeEventsList(
			ComplexReflectogramEvent[] data,
			ComplexReflectogramEvent[] etalon,
			double threshold)
	{
		return getChangedProbeEventsList(data, etalon, CHANGETYPE_AMPL, threshold);
	}
	
	public static int[] getChangedLossEventsList(
			ComplexReflectogramEvent[] data,
			ComplexReflectogramEvent[] etalon,
			double threshold)
	{
		return getChangedProbeEventsList(data, etalon, CHANGETYPE_LOSS, threshold);
	}

	// ����������� ������ - ��� ������ ��������� ������, �� �������� �
	// ������� ������������ �������.
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	//---------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	private static double getMaxDeviation(ModelTrace mt1,
			ModelTrace mt2, int iFrom, int iToEx)
	{
		int N = iToEx - iFrom;
		double[] y1 = mt1.getYArrayZeroPad(iFrom, N);
		double[] y2 = mt2.getYArrayZeroPad(iFrom, N);

		double ret = 0.;
		for (int i = 0; i < N; i++)
		{
			double diff = Math.abs(y1[i] - y2[i]);
			if (diff > ret)
				ret = diff;
		}
		return ret;
	}

	private static double getMeanDeviation(ModelTrace mt1,
			ModelTrace mt2, int iFrom, int iToEx)
	{
		int N = iToEx - iFrom;
		double[] y1 = mt1.getYArrayZeroPad(iFrom, N);
		double[] y2 = mt2.getYArrayZeroPad(iFrom, N);
		double sum = 0.;
		for (int i = 0; i < N; i++)
		{
				sum += Math.abs(y1[i] - y2[i]);
		}
		return N > 0 ? sum / N : 0.0;
	}

	private static double getRMSDeviation(ModelTrace mt1,
			ModelTrace mt2, int iFrom, int iToEx)
	{
		int N = iToEx - iFrom;
		double[] y1 = mt1.getYArrayZeroPad(iFrom, N);
		double[] y2 = mt2.getYArrayZeroPad(iFrom, N);
		double sum = 0.;
		for (int i = 0; i < N; i++)
		{
				double diff = y1[i] - y2[i];
				sum += diff * diff;
		}
		return N > 0 ? Math.sqrt(sum / N) : 0.0;
	}

	public static double getMaxDeviation(ModelTraceManager data,
			ModelTrace etalon, int nEvent)
	{
		SimpleReflectogramEvent dataSE = data.getSimpleEvent(nEvent);
		int iFrom = dataSE.getBegin();
		int iToEx = Math.min(dataSE.getEnd() + 1, etalon.getLength());
		ModelTrace dataMT = data.getModelTrace();
		return getMaxDeviation(dataMT, etalon, iFrom, iToEx);
	}

	public static double getMeanDeviation(ModelTraceManager data,
			ModelTrace etalon, int nEvent)
	{
		SimpleReflectogramEvent dataSE = data.getSimpleEvent(nEvent);
		int iFrom = dataSE.getBegin();
		int iToEx = Math.min(dataSE.getEnd() + 1, etalon.getLength());
		ModelTrace dataMT = data.getModelTrace();
		return getMeanDeviation(dataMT, etalon, iFrom, iToEx);
	}

	public static double getMaxDeviation(ModelTrace data, ModelTrace etalon)
	{
		int iFrom = 0;
		int iToEx = Math.min(data.getLength(), etalon.getLength());
		return getMaxDeviation(data, etalon, iFrom, iToEx);
	}

	public static double getMeanDeviation(ModelTrace data, ModelTrace etalon)
	{
		int iFrom = 0;
		int iToEx = Math.min(data.getLength(), etalon.getLength());
		return getMeanDeviation(data, etalon, iFrom, iToEx);
	}

	public static double getLossDifference(ModelTraceManager etalon,
			ModelTraceManager data)
	{
		SimpleReflectogramEvent[] dataSRE = data.getSimpleEvents();
		SimpleReflectogramEvent[] etalonSRE = etalon.getSimpleEvents();

		int length1 = ReflectogramMath.getLastConnectorBegin(dataSRE);
		int length2 = ReflectogramMath.getLastConnectorBegin(etalonSRE);

		int c = etalonSRE[0].getEnd();

		ModelTrace dataMT = data.getModelTrace();
		ModelTrace etalonMT = etalon.getModelTrace();
		double a1 = dataMT.getY(c);
		double a2 = dataMT.getY(length1);
		double b1 = etalonMT.getY(c);
		double b2 = etalonMT.getY(length2);

		return (a1 - a2) - (b1 - b2);
	}

	public static double getRMSDeviation(ModelTrace mt1, ModelTrace mt2, SimpleReflectogramEvent range)
	{
		int iFrom = range.getBegin();
		int iToEx = range.getEnd() + 1;
		return getRMSDeviation(mt1, mt2, iFrom, iToEx);
	}

	public static double getMaxDeviation(ModelTrace mt1, ModelTrace mt2, SimpleReflectogramEvent range)
	{
		int iFrom = range.getBegin();
		int iToEx = range.getEnd() + 1;
		return getMaxDeviation(mt1, mt2, iFrom, iToEx);
	}

/*
	//-----------------------------------------------------------------------------
	public static double getLossChange(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data, int nEvent)
	{
		if (data == null || etalon == null)
			return 0.;

		ReflectogramEvent d = data[nEvent];
		int coord = d.getMiddle();
		ReflectogramEvent e = ReflectogramMath.getEvent(coord, etalon);
		if (e == null)
			return 0.;

		int type = d.getEventType();
		if (e.getEventType() != type)
			return 0.;

		double a1 = d.getMLoss();
		double a2 = e.getMLoss();
		return a1 - a2;
	}

	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	public static double getLocationDifference(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data, int nEvent)
	{
		int coord1 = data[nEvent].getMiddle();
		ReflectogramEvent e = ReflectogramMath.getEvent(coord1, etalon);
		if (e == null)
			return 0.;

		int coord2 = e.getMiddle();

		return coord1 - coord2;
	}

	//-----------------------------------------------------------------------------
	public static double getWidthDifference(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data, int nEvent)
	{
		int coord = data[nEvent].getMiddle();
		ReflectogramEvent e = ReflectogramMath.getEvent(coord, etalon);
		if (e == null)
			return 0.;

		int type = data[nEvent].getEventType();
		if (type != e.getEventType())
			return 0.;

		// XXX
		//return 0.; // 
		return data[nEvent].getWidth0() - e.getWidth0();
	}

	//-----------------------------------------------------------------------------
	public static double getMaximalDeviation(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data)
	{
		if (data == null || etalon == null)
			return 0.;

		double ret = 0.;
		double a1;
		double a2;

		for (int i = 0; i < data[data.length - 1].getBegin()
				&& i < etalon[etalon.length - 1].getBegin(); i++)
		{
			a1 = ReflectogramMath.getEventAmplitudeAt(i, data);
			a2 = ReflectogramMath.getEventAmplitudeAt(i, etalon);
			if (Math.abs(ret) < Math.abs(a1 - a2))
			{
				ret = a1 - a2;
			}
		}
		return Math.abs(ret);
	}

	//-----------------------------------------------------------------------------
	public static double getLossDifference(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data)
	{
		double a1, a2;
		double b1, b2;

		int length1 = ReflectogramMath.getLastSplash(data);
		int length2 = ReflectogramMath.getLastSplash(etalon);

		int c = data[0].getEnd();
		a1 = ReflectogramMath.getEventAmplitudeAt(c, data);
		a2 = ReflectogramMath.getEventAmplitudeAt(length1, data);
		b1 = ReflectogramMath.getEventAmplitudeAt(c, etalon);
		b2 = ReflectogramMath.getEventAmplitudeAt(length2, etalon);

		double ret = (a1 - a2) - (b1 - b2);

		return ret;
	}

	//-----------------------------------------------------------------------------
	public static double getMeanDeviation(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data)
	{
		if (data == null || etalon == null)
			return 0.;

		double ret = 0.;
		double a1;
		double a2;
		int norma = 0;

		for (int i = 0; i < data[data.length - 1].getBegin()
				&& i < etalon[etalon.length - 1].getBegin(); i++)
		{
			a1 = ReflectogramMath.getEventAmplitudeAt(i, data);
			a2 = ReflectogramMath.getEventAmplitudeAt(i, etalon);
			ret += Math.abs(a1 - a2);
			norma++;
		}

		if (norma > 0)
			ret /= norma;

		return ret;
	}
	*/
}

/*/

import java.util.ArrayList;

public class ReflectogramComparer
{
	boolean performShifting = false;

	private double[] data;

	private ReflectogramEvent[] reData;
	private ReflectogramEvent[] etalon;

	private double[] thUp1;
	private double[] thUp2;
	private double[] thDown1;
	private double[] thDown2;

	private int minimalEventSize;

	private int length; //minimal length among all arrays;

	private ReflectogramAlarm[] hardAlarms;
	private ReflectogramAlarm[] softAlarms;

	public ReflectogramComparer(double[] data, ReflectogramEvent[] etalon,
			Threshold[] thresholds, boolean performShifting)
	{
		this(data, etalon, performShifting);

		for (int i = 0; i < Math.min(etalon.length, thresholds.length); i++)
			etalon[i].setThreshold(thresholds[i]);

		doIt();
	}

	public ReflectogramComparer(double[] data, ReflectogramEvent[] etalon,
			boolean performShifting)
	{
		this.reData = null;
		this.data = MathRef.correctReflectogramm(data);
		this.etalon = etalon;
		this.performShifting = performShifting;

		doIt();
	}

	private void doIt()
	{
		// -->> NECESSARY, VERY IMPORTANT part of
		// comparing!!!
		shiftDataToEtalon();
		initializeThresholds();
		initializeMinimalEventSize();
		initializeAlarms();
		initializeAlarmsProperties();
	}

	private void shiftDataToEtalon()
	{
		if (data == null || etalon == null || etalon.length < 1)
			return;

		double maxEtalon = -1000.;
		double maxData = -1000.;

		for (int i = etalon[0].getBegin(); i <= etalon[0].getEnd()
				&& i < data.length; i++)
		{
			if (maxEtalon < etalon[0].refAmplitude(i))
				maxEtalon = etalon[0].refAmplitude(i);
			if (maxData < data[i])
				maxData = data[i];
		}

		//Here, we must shift data to the etalon;

		// *** the reason is that the previous analysis does make a shift
		// *** so that y_min becomes zero;
		// *** now we have to restore y level. (c) Stas //saa
		double dA = maxEtalon - maxData;

		for (int i = 0; i < data.length; i++) //shifting of the data array;
		{
			data[i] += dA;
		}

		if (reData != null) // shifting of the reflectogramm events;
			for (int i = 0; i < reData.length; i++)
				reData[i].shiftY(dA);
	}

	private void initializeAlarmsProperties()
	{
		double[] etalonArray = ReflectogramMath.getReflectogrammFromEvents(
			etalon, etalon[etalon.length - 1].getEnd());

		for (int i = 0; i < hardAlarms.length; i++) // iniciating of the hard
		// alarms properties;
		{
			ReflectogramAlarm ra = hardAlarms[i];
			ra.setEventType(ReflectogramMath.getEventType(ra.alarmPointCoord,
				etalon));
			ra.refAmplChangeValue = ReflectogramMath.getMaximalDifference(
				etalonArray, data, ra.alarmPointCoord, ra.alarmEndPointCoord);

			//      if(reData == null)
			//      {
			ra.leftReflectoEventCoord = 0;
			ra.rightReflectoEventCoord = etalon[etalon.length - 1].getBegin();

			int number = ReflectogramMath.getEventNumber(ra.alarmPointCoord,
				etalon);
			for (int j = number + 1; j < etalon.length; j++)
			{
				if (etalon[j].getEventType() == ReflectogramEvent.CONNECTOR)
				{
					ra.rightReflectoEventCoord = etalon[j].getBegin();
				}
			}
			for (int j = number - 1; j >= 0; j--)
			{
				if (etalon[j].getEventType() == ReflectogramEvent.CONNECTOR)
				{
					ra.leftReflectoEventCoord = etalon[j].getBegin();
				}
			}
			//      }
			//      else
			//      {
			//        ra.leftReflectoEventCoord = 0;
			//        ra.rightReflectoEventCoord = reData[reData.length-1].getBegin();
			//
			//        int number =
			// WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord,
			// reData);
			//        for(int j=number+1; j<reData.length; j++)
			//        {
			//          if(reData[j].connectorEvent == 1)
			//          {
			//            ra.rightReflectoEventCoord = reData[j].getBegin();
			//          }
			//        }
			//        for(int j=number-1; j>=0; j--)
			//        {
			//          if(reData[j].connectorEvent == 1)
			//          {
			//            ra.leftReflectoEventCoord = reData[j].getBegin();
			//          }
			//        }
			//      }

			if (Math.abs(ra.alarmPointCoord - ra.leftReflectoEventCoord) < Math.abs(ra.alarmPointCoord
					- ra.rightReflectoEventCoord))
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord
						- ra.leftReflectoEventCoord);
			} else
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord
						- ra.rightReflectoEventCoord);
			}
		}

		for (int i = 0; i < softAlarms.length; i++) // iniciating of the soft
		// alarms properties;
		{
			ReflectogramAlarm ra = softAlarms[i];
			ra.setEventType(ReflectogramMath.getEventType(ra.alarmPointCoord,
				etalon));
			ra.refAmplChangeValue = ReflectogramMath.getMaximalDifference(
				etalonArray, data, ra.alarmPointCoord, ra.alarmEndPointCoord);

			//      if(reData == null)
			//      {
			ra.leftReflectoEventCoord = 0;
			ra.rightReflectoEventCoord = etalon[etalon.length - 1].getBegin();

			int number = ReflectogramMath.getEventNumber(ra.alarmPointCoord,
				etalon);
			for (int j = number + 1; j < etalon.length; j++)
			{
				if (etalon[j].getEventType() == ReflectogramEvent.CONNECTOR)
				{
					ra.rightReflectoEventCoord = etalon[j].getBegin();
				}
			}
			for (int j = number - 1; j >= 0; j--)
			{
				if (etalon[j].getEventType() == ReflectogramEvent.CONNECTOR)
				{
					ra.leftReflectoEventCoord = etalon[j].getBegin();
				}
			}
			//      }
			//      else
			//      {
			//        ra.leftReflectoEventCoord = 0;
			//        ra.rightReflectoEventCoord = reData[reData.length-1].getBegin();
			//
			//        int number =
			// WorkWithReflectoEventsArray.getEventNumber(ra.alarmPointCoord,
			// reData);
			//        for(int j=number+1; j<reData.length; j++)
			//        {
			//          if(reData[j].connectorEvent == 1)
			//          {
			//            ra.rightReflectoEventCoord = reData[j].getBegin();
			//          }
			//        }
			//        for(int j=number-1; j>=0; j--)
			//        {
			//          if(reData[j].connectorEvent == 1)
			//          {
			//            ra.leftReflectoEventCoord = reData[j].getBegin();
			//          }
			//        }
			//      }
			//
			if (Math.abs(ra.alarmPointCoord - ra.leftReflectoEventCoord) < Math.abs(ra.alarmPointCoord
					- ra.rightReflectoEventCoord))
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord
						- ra.leftReflectoEventCoord);
			} else
			{
				ra.nearestReflectoEventDistance = Math.abs(ra.alarmPointCoord
						- ra.rightReflectoEventCoord);
			}
		}

	}

	private void initializeAlarms()
	{
		length = min(new int[] { data.length, thDown1.length, thDown2.length,
				thUp1.length, thUp2.length });
		ArrayList hard_Alarms = new ArrayList();
		ArrayList soft_Alarms = new ArrayList();

		int begin;
		int end;

		for (int i = 0; i < length; i++) // initializing hard alarms
		{
			begin = 0;
			end = 0;
			if (data[i] > thUp2[i])
			{
				begin = i;
				while (data[i] > thUp2[i] && i < length - 1)
				{
					i++;
				}
				end = i;
			} else if (data[i] < thDown2[i])
			{
				begin = i;
				while (data[i] < thDown2[i] && i < length - 1)
				{
					i++;
				}
				end = i;
			}

			if (end - begin > minimalEventSize)
			{
				ReflectogramEvent ev = ReflectogramMath.getEvent(begin, reData);
				if (ev != null
						&& (ev.getEventType() == ReflectogramEvent.CONNECTOR || ev.getEventType() == ReflectogramEvent.WELD))
					begin = ev.getBegin();

				hard_Alarms.add(new ReflectogramAlarm(begin,
						ReflectogramAlarm.LEVEL_HARD, end));
			}
		}

		for (int i = 0; i < length; i++) // initializing soft alarms
		{
			begin = 0;
			end = 0;
			if (data[i] > thUp1[i])
			{
				begin = i;
				while (data[i] > thUp1[i] && i < length - 1)
				{
					i++;
				}
				end = i;
			} else if (data[i] < thDown1[i])
			{
				begin = i;
				while (data[i] < thDown1[i] && i < length - 1)
				{
					i++;
				}
				end = i;
			}

			if (end - begin > minimalEventSize)
			{
				ReflectogramEvent ev = ReflectogramMath.getEvent(begin, reData);
				if (ev != null
						&& (ev.getEventType() == ReflectogramEvent.CONNECTOR || ev.getEventType() == ReflectogramEvent.WELD))
					begin = ev.getBegin();

				soft_Alarms.add(new ReflectogramAlarm(begin,
						ReflectogramAlarm.LEVEL_HARD, end));
			}
		}

		this.hardAlarms = (ReflectogramAlarm[] )hard_Alarms.toArray(new ReflectogramAlarm[hard_Alarms.size()]);
		this.softAlarms = (ReflectogramAlarm[] )soft_Alarms.toArray(new ReflectogramAlarm[soft_Alarms.size()]);
	}

	private void initializeMinimalEventSize()
	{
		if (etalon[0].getEventType() == ReflectogramEvent.CONNECTOR)
		{
			minimalEventSize = (int )(etalon[0].getWidth0() / 2);
		} else
		{
			minimalEventSize = ReflectogramMath.getReflectiveEventSize(data,
				0.5) / 2;
		}

		if (minimalEventSize < 3)
		{
			minimalEventSize = 3;
		}
	}

	private void initializeThresholds()
	{
		thUp1 = ReflectogramMath.getReflectogrammFromEvents(
			ReflectogramMath.getThreshold(etalon, Threshold.UP1),
			etalon[etalon.length - 1].getEnd());

		thUp2 = ReflectogramMath.getReflectogrammFromEvents(
			ReflectogramMath.getThreshold(etalon, Threshold.UP2),
			etalon[etalon.length - 1].getEnd());

		thDown1 = ReflectogramMath.getReflectogrammFromEvents(
			ReflectogramMath.getThreshold(etalon, Threshold.DOWN1),
			etalon[etalon.length - 1].getEnd());

		thDown2 = ReflectogramMath.getReflectogrammFromEvents(
			ReflectogramMath.getThreshold(etalon, Threshold.DOWN2),
			etalon[etalon.length - 1].getEnd());
	}

	private int min(int[] array)
	{
		int m = array[0];
		for (int i = 0; i < array.length; i++)
		{
			if (m > array[i])
				m = array[i];
		}
		return m;
	}

	public ReflectogramAlarm[] getHardAlarms()
	{
		return hardAlarms;
	}

	public ReflectogramAlarm[] getSoftAlarms()
	{
		return softAlarms;
	}

	public ReflectogramAlarm[] getAlarms()
	{
		ReflectogramAlarm[] res = new ReflectogramAlarm[hardAlarms.length
				+ softAlarms.length];
		int n = 0;
		for (int i = 0; i < hardAlarms.length; i++)
		{
			res[n] = hardAlarms[i];
			n++;
		}
		for (int i = 0; i < softAlarms.length; i++)
		{
			res[n] = softAlarms[i];
			n++;
		}
		return res;
	}

	// methods of comparing to etalon

	public ReflectogramComparer(ReflectogramEvent[] reData,
			ReflectogramEvent[] etalon, Threshold[] thresholds,
			boolean performShifting)
	{
		this(reData, etalon, performShifting);

		for (int i = 0; i < Math.min(etalon.length, thresholds.length); i++)
			etalon[i].setThreshold(thresholds[i]);

		doIt();
	}

	public ReflectogramComparer(ReflectogramEvent[] reData,
			ReflectogramEvent[] etalon, boolean performShifting)
	{
		this.reData = reData;
		this.data = ReflectogramMath.getReflectogrammFromEvents(reData,
			etalon[etalon.length - 1].getEnd());
		this.etalon = etalon;
		this.performShifting = performShifting;

		doIt();
	}

	// Static methods definition
	//-----------------------------------------------------------------------------
	public static int[] getNewEventsList(ReflectogramEvent[] data,
			ReflectogramEvent[] etalon)
	{
		if (data == null || etalon == null)
			return new int[0];

		int[] eventsList = null;
		ArrayList v = new ArrayList();

		for (int i = 0; i < data.length; i++)
		{
			int coord = data[i].getMiddle();
			int type = data[i].getEventType();
			if (ReflectogramMath.getEventType(coord, etalon) != type)
			{
				v.add(new Integer(i));
			}
		}

		eventsList = new int[v.size()];

		for (int i = 0; i < eventsList.length; i++)
		{
			eventsList[i] = ((Integer )v.get(i)).intValue();
		}
		return eventsList;
	}

	//-----------------------------------------------------------------------------
	public static int[] getLostEventsList(ReflectogramEvent[] data,
			ReflectogramEvent[] etalon)
	{
		return getNewEventsList(etalon, data);
	}

	//-----------------------------------------------------------------------------
	public static int[] getChangedLossEventsList(ReflectogramEvent[] data,
			ReflectogramEvent[] etalon, double changeThreshold)
	{
		if (data == null || etalon == null)
			return new int[0];

		int[] eventsList = null;
		ArrayList v = new ArrayList();

		for (int i = 0; i < data.length; i++)
		{
			int coord = data[i].getMiddle();
			int type = data[i].getEventType();
			ReflectogramEvent et = ReflectogramMath.getEvent(coord, etalon);
			if (et != null && et.getEventType() == type) // Event is the same!;
			{
				if (type == ReflectogramEvent.CONNECTOR)
				{
					if (i > 0
							&& i < data.length - 1
							&& Math.abs(data[i].getMLoss() - et.getMLoss()) > changeThreshold)
						v.add(new Integer(i));
				} else if (type == ReflectogramEvent.WELD)
				{
					//if(Math.abs(data[i].boost_weld -
					// et.boost_weld)>changeThreshold)
					if (Math.abs(data[i].getMLoss() - et.getMLoss()) > changeThreshold)
						v.add(new Integer(i));
				}
			}
		}

		eventsList = new int[v.size()];

		for (int i = 0; i < eventsList.length; i++)
		{
			eventsList[i] = ((Integer )v.get(i)).intValue();
		}
		return eventsList;
	}

	//-----------------------------------------------------------------------------
	public static int[] getChangedAmplitudeEventsList(ReflectogramEvent[] data,
			ReflectogramEvent[] etalon, double changeThreshold)
	{
		if (data == null || etalon == null)
			return new int[0];

		int[] eventsList = null;
		ArrayList v = new ArrayList();

		for (int i = 0; i < data.length; i++)
		{
			int coord = data[i].getMiddle();
			int type = data[i].getEventType();
			ReflectogramEvent et = ReflectogramMath.getEvent(coord, etalon);

			if (et != null && et.getEventType() == type) // Event is the same!;
			{
				if (type == ReflectogramEvent.CONNECTOR)
				{
					if (i > 0
							&& Math.abs(data[i].getAsympY0() - et.getAsympY0()) > changeThreshold
							|| i < data.length - 1
							&& Math.abs(data[i].getAsympY1() - et.getAsympY1()) > changeThreshold)
						v.add(new Integer(i));
				} else if (type == ReflectogramEvent.WELD)
				{
					//if(Math.abs(data[i].a_weld - et.a_weld)>changeThreshold)
					if (Math.abs(data[i].getMLoss() - et.getMLoss()) > changeThreshold)
						v.add(new Integer(i));
				} else if (type == ReflectogramEvent.LINEAR)
				{
					if (Math.abs(data[i].getAsympY0() - et.getAsympY0()) > changeThreshold)
						v.add(new Integer(i));
				}
			}
		}

		eventsList = new int[v.size()];

		for (int i = 0; i < eventsList.length; i++)
		{
			eventsList[i] = ((Integer )v.get(i)).intValue();
		}
		return eventsList;
	}

	//-----------------------------------------------------------------------------
	public static double getMaximalDeviation(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data)
	{
		if (data == null || etalon == null)
			return 0.;

		double ret = 0.;
		double a1;
		double a2;

		for (int i = 0; i < data[data.length - 1].getBegin()
				&& i < etalon[etalon.length - 1].getBegin(); i++)
		{
			a1 = ReflectogramMath.getEventAmplitudeAt(i, data);
			a2 = ReflectogramMath.getEventAmplitudeAt(i, etalon);
			if (Math.abs(ret) < Math.abs(a1 - a2))
			{
				ret = a1 - a2;
			}
		}
		return ret;
	}

	//-----------------------------------------------------------------------------
	public static double getLossDifference(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data)
	{
		double a1, a2;
		double b1, b2;

		int length1 = ReflectogramMath.getLastSplash(data);
		int length2 = ReflectogramMath.getLastSplash(etalon);

		int c = data[0].getEnd();
		a1 = ReflectogramMath.getEventAmplitudeAt(c, data);
		a2 = ReflectogramMath.getEventAmplitudeAt(length1, data);
		b1 = ReflectogramMath.getEventAmplitudeAt(c, etalon);
		b2 = ReflectogramMath.getEventAmplitudeAt(length2, etalon);

		double ret = (a1 - a2) - (b1 - b2);

		return ret;
	}

	//-----------------------------------------------------------------------------
	public static double getMeanDeviation(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data)
	{
		if (data == null || etalon == null)
			return 0.;

		double ret = 0.;
		double a1;
		double a2;
		int norma = 0;

		for (int i = 0; i < data[data.length - 1].getBegin()
				&& i < etalon[etalon.length - 1].getBegin(); i++)
		{
			a1 = ReflectogramMath.getEventAmplitudeAt(i, data);
			a2 = ReflectogramMath.getEventAmplitudeAt(i, etalon);
			ret += (a1 - a2);
			norma++;
		}

		if (norma > 0)
			ret /= norma;

		return ret;
	}

	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	public static double getDeviation(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data, int nEvent)
	{
		if (data == null || etalon == null)
			return 0.;

		double ret = 0.;

		for (int i = data[nEvent].getBegin(); i < data[nEvent].getEnd(); i++)
		{
			if (i < etalon[etalon.length - 1].getEnd())
			{
				double a1 = data[nEvent].refAmplitude(i);
				double a2 = ReflectogramMath.getEventAmplitudeAt(i, etalon);
				if (Math.abs(ret) < Math.abs(a1 - a2))
				{
					ret = a1 - a2;
				}
			}
		}

		return Math.abs(ret); // ?? - ������ ������������ �������� �� ������ - �����?
	}

	//-----------------------------------------------------------------------------
	public static double getMeanDeviation(ReflectogramEvent[] data,
			ReflectogramEvent[] etalon, int nEvent)
	{
		double ret = 0.;
		int norma = 0;
		for (int i = data[nEvent].getBegin(); i < data[nEvent].getEnd(); i++)
		{
			if (i < etalon[etalon.length - 1].getEnd())
			{
				double a1 = data[nEvent].refAmplitude(i);
				double a2 = ReflectogramMath.getEventAmplitudeAt(i, etalon);
				ret += Math.abs(a1 - a2); // there was no abs!
				norma++;
			}
		}
		if (norma > 0)
			ret = ret / norma;

		return ret;
	}

	//-----------------------------------------------------------------------------
	public static double getLoss(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data, int nEvent)
	{
		if (data == null || etalon == null)
			return 0.;

		ReflectogramEvent d = data[nEvent];
		int coord = d.getMiddle();
		ReflectogramEvent e = ReflectogramMath.getEvent(coord, etalon);
		if (e == null)
			return 0.;

		int type = d.getEventType();
		if (e.getEventType() != type)
			return 0.;

		double a1 = d.getMLoss();
		double a2 = e.getMLoss();
		return a1 - a2;
	}

	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------
	public static double getLocationDifference(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data, int nEvent)
	{
		int coord = data[nEvent].getMiddle();
		ReflectogramEvent e = ReflectogramMath.getEvent(coord, etalon);
		if (e == null)
			return 0.;

		int coord2 = e.getMiddle();

		return coord - coord2;
	}

	//-----------------------------------------------------------------------------
	public static double getWidthDifference(ReflectogramEvent[] etalon,
			ReflectogramEvent[] data, int nEvent)
	{
		int coord = data[nEvent].getMiddle();
		ReflectogramEvent e = ReflectogramMath.getEvent(coord, etalon);
		if (e == null)
			return 0.;

		int type = data[nEvent].getEventType();
		if (type != e.getEventType())
			return 0.;

		return 0.; // data[nEvent].getWidth() - e.getWidth(); // FIXME
	}

}
//*/
// DO NOT DELETE THIS COMMENT!!!

/*
 ReflectogramEvent linearEvent = null;

 for(int i=0; i<etalon.length; i++)
 {
 if(etalon[i].linearEvent == 1 && etalon[i].getEnd()-etalon[i].getBegin()>3) //at list, three points needed to get total shift;
 {
 linearEvent = etalon[i];
 break;
 }
 }

 double meanShift = 0.;
 int norma = 0;

 if(linearEvent!=null)
 {
 for(int i=linearEvent.getBegin(); i<=linearEvent.getEnd() ; i++)
 {
 meanShift = meanShift+(data[i]-linearEvent.refAmplitude(i));
 norma++;
 }
 }
 else
 {
 for(int i=minimalEventSize*3; i<=minimalEventSize*3+10; i++)
 {
 meanShift = meanShift+(data[i]-WorkWithReflectoEventsArray.getAmplitudeByCoord(etalon, i));
 norma++;
 }
 }

 meanShift = meanShift/norma;// So, <data> = <etalon>+meanShift and
 // <threshold>  --> <threshold>+meanShift;

 */

