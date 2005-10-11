/*-
 * $Id: SimpleReflectogramEventComparer.java,v 1.8 2005/10/11 14:41:20 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEventUtil;

/**
 * ��������� �������.
 * <p>
 * ������������ ������� ������-����������
 * ����� ������, ��� �� ����� ��� ������� �������
 * SimpleReflectogramEvent[] - ���� - ������� �������, ������ - ���������.
 * ��������� ������ ������� ������������ �������, � ����� �����
 * ����� �������� �� ������� � ������������ ����� �������� �������
 * � �� ��������� ������� � ���������� �������.
 * <p>
 * ������������ ������� ����� ����������� � ���� �������:
 * NonStrict, ����������, MostStrict.
 * XXX: �������� ������� ��� ����� ���������, �������� �������, ���� ����������.
 * FIXME: �������� MostStrict ��������� {@link #removeNonUnique} �� ��������.
 * <p>
 * ��� ��������� ��������� ����������� �������:
 * ��������� ������� ��������� ��� �����
 * SimpleReflectogramEvent,
 * ��������� - ������ ��� ComplexReflectogramEvent,
 * � ��������� - ������ ��� ReliabilitySimpleReflectogramEvent
 * <p>
 * NB: ��������� ������������� ����� �������� �� ����, �������� �� �������
 * ������� instanceof {@link ReliabilitySimpleReflectogramEvent}.
 * <p>
 * ���� � ����������� ������, ������� ���� ������� ������-����������,
 * � �������� �� ���� ������ ������������ �������.
 * <p>
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.8 $, $Date: 2005/10/11 14:41:20 $
 * @module dadara
 */
public class SimpleReflectogramEventComparer {
	private SimpleReflectogramEvent[] probeEvents;
	private SimpleReflectogramEvent[] etalonEvents;
	
	private static final int UNPAIRED = -1;
	private static final int RELIABLY_UNPAIRED = -2;

	// ����������� ������� ������� �/� �� ������� ���������
	// � ��������.
	// �������� < 0 - �����. ������� �� �������.
	// ������ UNPAIRED: ���� �� �������, � RELIABLY_UNPAIRED: ������� ���������� �����
	// �� ���� ���������, ����������� �������� ������������� �����������
	// probe2etalon � etalon2probe.
	// NB: ������� ������ ������� ������ ���� ������������� �������.
	private int[] probe2etalonNonStrict = null;
	private int[] etalon2probeNonStrict = null;
	private int[] probe2etalon = null;
	private int[] etalon2probe = null;
	private int[] probe2etalonMostStrict = null;
	private int[] etalon2probeMostStrict = null;

	//public static long COMPARE_ANALYSE = 0x1;
	//public static long COMPARE_EVALUATE = 0x2;

	// This constructor was added just for slight performance reasons:
	// this way we need not copy 'events' arrays as it would happen
	// if the caller used 'mtaeProbe.getSimpleEvents()'
	public SimpleReflectogramEventComparer(
			ModelTraceAndEventsImpl mtaeProbe,
			ModelTraceAndEventsImpl mtaeEtalon)
	{
		this(mtaeProbe != null ? mtaeProbe.getRSE() : null,
				mtaeEtalon.getRSE());
	}

	public SimpleReflectogramEventComparer(
			SimpleReflectogramEvent[] _probeEvents,
			SimpleReflectogramEvent[] _etalonEvents) {
		this.probeEvents = _probeEvents;
		this.etalonEvents = _etalonEvents;

		// ��������� ������ - ���� ������������ ������� ������� � �����
		if (this.probeEvents != null)
		{
			this.probe2etalon = findNearestOverlappingEvent(this.probeEvents, this.etalonEvents);
			this.etalon2probe = findNearestOverlappingEvent(this.etalonEvents, this.probeEvents);

			this.probe2etalonMostStrict = this.probe2etalon.clone();
			this.etalon2probeMostStrict = this.etalon2probe.clone();

			this.probe2etalonNonStrict = this.probe2etalon.clone();
			this.etalon2probeNonStrict = this.etalon2probe.clone();

			// ��������� ��������� ������

			// ������� ����������� ���������
			removeNonPaired(this.probe2etalon, this.etalon2probe);
			removeNonPaired(this.etalon2probe, this.probe2etalon);

			// ������� �������� ������� ���������
			removeNonUnique(this.probe2etalonNonStrict, this.etalon2probeMostStrict);
			removeNonUnique(this.etalon2probeNonStrict, this.probe2etalonMostStrict);
		}
	}

	private void removeNonUnique(int[] a, int[] b) {
		for (int i = 0; i < a.length; i++) {
			int j = a[i];
			if (j >= 0) {
				if (b[j] != i) {
					b[j] = -1;
				}
			}
		}
	}

	/**
	 * Find corresponding etalon event for the specified probe event
	 * @param probeId probe event #
	 * @return >=0: etalon event #; -1: no etalon event for this probe
	 */
	public int getEtalonIdByProbeId(int probeId)
	{
		return this.probe2etalon[probeId] >= 0 ? this.probe2etalon[probeId] : UNPAIRED;
	}

	/**
	 * Find non-strict corresponding probe event for the specified etalon event
	 * @param etalonId etalon event #
	 * @return >=0: probe event #; -1: no probe event for this etalon
	 */
	public int getProbeIdByEtalonIdNonStrict(int etalonId) {
		return this.etalon2probeNonStrict[etalonId] >= 0
				? this.etalon2probeNonStrict[etalonId]
				: UNPAIRED;
	}

	/**
	 * Find most-strict corresponding etalon event for the specified probe event
	 * @param probeId probe event #
	 * @return >=0: etalon event #; -1: no etalon event for this probe
	 */
	public int getEtalonIdByProbeIdMostStrict(int probeId) {
		return this.probe2etalonMostStrict[probeId] >= 0
				? this.probe2etalonMostStrict[probeId]
				: UNPAIRED;
	}

	/**
	 * Find most-strict corresponding probe event for the specified etalon event
	 * @param etalonId etalon event #
	 * @return >=0: probe event #; -1: no probe event for this etalon
	 */
	public int getProbeIdByEtalonIdMostStrict(int etalonId) {
		return this.etalon2probeMostStrict[etalonId] >= 0
				? this.etalon2probeMostStrict[etalonId]
				: UNPAIRED;
	}

	/**
	 * Find non-strict corresponding etalon event for the specified probe event
	 * @param probeId probe event #
	 * @return >=0: etalon event #; -1: no etalon event for this probe
	 */
	public int getEtalonIdByProbeIdNonStrict(int probeId) {
		return this.probe2etalonNonStrict[probeId] >= 0
				? this.probe2etalonNonStrict[probeId]
				: UNPAIRED;
	}

	/**
	 * Find corresponding probe event for the specified etalon event
	 * @param etalonId etalon event #
	 * @return >=0: probe event #; -1: no probe event for this etalon
	 */
	public int getProbeIdByEtalonId(int etalonId)
	{
		return this.etalon2probe[etalonId] >= 0 ? this.etalon2probe[etalonId] : UNPAIRED;
	}

	public boolean isProbeEventNew(int probeId)
	{
		int etalonId = this.probe2etalon[probeId];
		return etalonId < 0;
	}

	public boolean isEtalonEventLost(int etalonId)
	{
		int probeId = this.etalon2probe[etalonId];
		return probeId < 0;
	}

	public boolean isProbeEventReliablyNew(int probeId)
	{
		int etalonId = this.probe2etalon[probeId];
		return etalonId == RELIABLY_UNPAIRED;
	}

	public boolean isEtalonEventReliablyLost(int etalonId)
	{
		int probeId = this.etalon2probe[etalonId];
		return probeId == RELIABLY_UNPAIRED;
	}

	// XXX: ����� �� ����� �����?
	public int[] getNewEventsList()
	{
		// ������� ����� ����� �������
		int count = 0;
		for (int i = 0; i < this.probe2etalon.length; i++)
			if (isProbeEventNew(i))
				count++;
		// ������� � ��������� ������ ����� �������
		int[] ret = new int[count];
		count = 0;
		for (int i = 0; i < this.probe2etalon.length; i++)
			if (isProbeEventNew(i))
				ret[count++] = i;

		return ret;
	}

	/* Comparison results table ('+' is true, '-' is false)
	 * AMPL LOSS TYPE NOL REL_NOL Check type / change type
	 *   -    -    -   -     -       no significant change
	 *   +    -    -   -     -       changed amplitude
	 *   -    +    -   -     -       changed loss
	 *   -    -    +   -     -       changed type
	 *   +    +    +   +     -       non-reliably new or lost
	 *   +    +    +   +     +       reliably new or lost
	 */

	public static final int CHANGETYPE_AMPL = 0x1; // requires ComplexReflectogramEvents
	public static final int CHANGETYPE_LOSS = 0x2; // requires ComplexReflectogramEvents
	public static final int CHANGETYPE_TYPE = 0x4; // requires SimpleReflectogramEvents
	public static final int CHANGETYPE_NEW_OR_LOST = 0x8;  // requires ComplexReflectogramEvents
	public static final int CHANGETYPE_RELIABLY_NEW_OR_LOST = 0x10;  // requires ReliabilityReflectogramEvents
	
	/**
	 * @throws ClassCastException if comparison type is not supported by input events type
	 */
	public static boolean eventsAreDifferent(
			SimpleReflectogramEvent a, // not null
			SimpleReflectogramEvent b, // not null
			int changeType, // one of CHANGETYPE's
			double changeThreshold) // may be zero; may be unused
	{
		switch(changeType)
		{
		case CHANGETYPE_AMPL:
			if (a instanceof ComplexReflectogramEvent)
				return Math.abs(((ComplexReflectogramEvent)a).getALet() - ((ComplexReflectogramEvent)b).getALet()) > changeThreshold;
			else {
				try {
					return Math.abs(DetailedEventUtil.getAmplDiff(
								(DetailedEvent)a, (DetailedEvent)b))
							> changeThreshold;
				} catch (NoSuchFieldException e) {
					return false; // treat no difference if parameter is not present
				}
			}

		case CHANGETYPE_LOSS:
			if (a instanceof ComplexReflectogramEvent)
				return Math.abs(((ComplexReflectogramEvent)a).getMLoss() - ((ComplexReflectogramEvent)b).getMLoss()) > changeThreshold;
			else {
				try {
					return Math.abs(DetailedEventUtil.getLossDiff(
								(DetailedEvent) a, (DetailedEvent) b))
							> changeThreshold;
				} catch (NoSuchFieldException e) {
					return false; // treat no difference if parameter is not present
				}
			}

		case CHANGETYPE_TYPE:
			// ��� changeThreshold == 0 � 1 �������� ������ ��������� ������
			return a.getEventType() != b.getEventType();

		case CHANGETYPE_NEW_OR_LOST:
			// fall through
		case CHANGETYPE_RELIABLY_NEW_OR_LOST:
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
		int probeId = this.etalon2probe[etalonId];

		if (changeType == CHANGETYPE_RELIABLY_NEW_OR_LOST)
			return probeId == RELIABLY_UNPAIRED;

		if (probeId < 0) // ������� �������
			return true;

		return eventsAreDifferent(
			this.etalonEvents[etalonId],
			this.probeEvents[probeId],
			changeType,
			changeThreshold);
	}

	// ����� ������, ��� ������� ����������,
	// ���� ������� ��������� isProbeEventNew.
	// � ��������� ������ - ������ (��� �������)
	public boolean isProbeEventChanged(int probeId, int changeType, double changeThreshold)
	{
		int etalonId = this.probe2etalon[probeId];

		if (changeType == CHANGETYPE_RELIABLY_NEW_OR_LOST)
			return etalonId == RELIABLY_UNPAIRED;

		if (etalonId < 0) // ������� ���������
			return true;

		return eventsAreDifferent(
			this.etalonEvents[etalonId],
			this.probeEvents[probeId],
			changeType,
			changeThreshold);
	}

	// internal events comparison

	// degree of events overlapping
	private double eventsOverlap(SimpleReflectogramEvent x, SimpleReflectogramEvent y)
	{
		//return Math.max(x.getBegin(), y.getBegin()) <= Math.min(x.getEnd(), y.getEnd());
		// we write '<', not '<=' because we want to treat adjacent events as different
		//return Math.max(x.getBegin(), y.getBegin()) < Math.min(x.getEnd(), y.getEnd()) ? 1 : 0;
		double xb = x.getBegin();
		double xe = x.getEnd();
		double yb = y.getBegin();
		double ye = y.getEnd();
		double diff1 = Math.min(xe, ye) - Math.max(xb, yb);
		if (diff1 <= 0)
			return 0;
		double diff2 = Math.max(xe, ye) - Math.min(xb, yb); // diff2 >= diff1
		return diff1 / diff2;
	}

	private double calcEventsDistance(SimpleReflectogramEvent x, SimpleReflectogramEvent y)
	{
		double dist1 = Math.abs(x.getBegin() - y.getBegin()) + Math.abs(x.getEnd() - y.getEnd());
		double mult;
		// same type
		if (x.getEventType() == y.getEventType())
			mult = 1.0;
		// one event is linear, another is not
		else if (x.getEventType() == SimpleReflectogramEvent.LINEAR
				|| y.getEventType() == SimpleReflectogramEvent.LINEAR)
			mult = 3.0;
		// all other cases
		else
			mult = 2.0;
		return dist1 * mult;
	}

	private void removeNonPaired(int[] fwd, int[] backwd)
	{
		for (int i = 0; i < fwd.length; i++)
		{
			int j = fwd[i];
			if (j >= 0 && backwd[j] != i)
				fwd[i] = UNPAIRED;
		}
	}

	private int[] findNearestOverlappingEvent(SimpleReflectogramEvent[] X, SimpleReflectogramEvent[] Y)
	{
		int[] ret = new int[X.length];
		for (int i = 0; i < X.length; i++)
		{
			// ��������� �� ����� ������� ������ �������
			double bestDistance = -1; // Stands for +inf
			int bestJ = UNPAIRED;
			// �������� �����. ������� - ������������� ��������� � �����/����������
			boolean reliablyNewOrLost = X[i].getEventType() != SimpleReflectogramEvent.LINEAR
				&& (X[i] instanceof ReliabilitySimpleReflectogramEvent)
				&& ((ReliabilitySimpleReflectogramEvent)X[i]).hasReliability()
				&& ((ReliabilitySimpleReflectogramEvent)X[i]).getReliability() > ReliabilitySimpleReflectogramEvent.RELIABLE; 
			for (int j = 0; j < Y.length; j++)
			{
				double overlap = eventsOverlap(X[i], Y[j]);
				if (overlap > 0)
				{
					// ��������� ������� �������������� �������
					// (������������� �����)
					double distance = calcEventsDistance(X[i], Y[j])
						/ ( 1.0 + overlap);
					if (bestDistance < 0 || distance < bestDistance)
					{
						bestJ = j;
						bestDistance = distance;
					}
					// � ��������� ���. ���. ����� ���� �����-�� ���� -- ��� �� "������� �����/����������"
					if (Y[j].getEventType() != SimpleReflectogramEvent.LINEAR)
						reliablyNewOrLost = false;
				}
			}
			if (reliablyNewOrLost)
				ret[i] = RELIABLY_UNPAIRED;
			else
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
		SimpleReflectogramEventComparer comparer =
			new SimpleReflectogramEventComparer(data, etalon);
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
}
