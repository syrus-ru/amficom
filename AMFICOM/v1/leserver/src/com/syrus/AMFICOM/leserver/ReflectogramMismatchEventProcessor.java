/*-
 * $Id: ReflectogramMismatchEventProcessor.java,v 1.18 2006/03/30 12:11:12 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.REFLECTORGAM_MISMATCH;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Set;
import java.util.regex.Pattern;

import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.eventv2.DefaultLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2006/03/30 12:11:12 $
 * @module leserver
 */
final class ReflectogramMismatchEventProcessor
		implements EventProcessor {
	/*-********************************************************************
	 * String constants & i18n.                                           *
	 **********************************************************************/

	private static final char NEWLINE = '\n';

	private static final char SPACE = ' ';

	private static final String COLON_TAB = ":\t";

	private static final String RANGE = " .. ";

	private static final String PHYSICAL_DISTANCE_TO = I18N.getString("NotificationEvent.PhysicalDistanceTo");

	private static final String END_OF = I18N.getString("NotificationEvent.EndOf");

	private static final String START_OF = I18N.getString("NotificationEvent.StartOf");

	private static final String PATH_ELEMENT_GENITIVE = I18N.getString("NotificationEvent.PathElementGenitive");

	private static final String PATH_ELEMENT = I18N.getString("NotificationEvent.PathElement");

	private static final String AFFECTED = I18N.getString("NotificationEvent.Affected");

	private static final String MISMATCH_LEVEL = I18N.getString("NotificationEvent.MismatchLevel");

	private static final String MISMATCH_CREATED = I18N.getString("NotificationEvent.MismatchCreated");


	private static final String METER_SINGULAR_NOMINATIVE = I18N.getString("NotificationEvent.MeterSingularNominative");

	private static final String METER_SINGULAR_GENITIVE = I18N.getString("NotificationEvent.MeterSingularGenitive");

	private static final String METER_PLURAL_GENITIVE = I18N.getString("NotificationEvent.MeterPluralGenitive");


	private static final Pattern METER_SINGULAR_NOMINATIVE_REGEXP = Pattern.compile("([0-9]*[^1])?1");

	private static final Pattern METER_SINGULAR_GENITIVE_REGEXP = Pattern.compile("([0-9]*[^1])?[2-4]");

	private static final Pattern METER_PLURAL_GENITIVE_REGEXP = Pattern.compile("(([0-9]*[^1])?[0,5-9]|[0-9]*1[0-9])");

	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return REFLECTORGAM_MISMATCH;
	}

	/**
	 * @param event
	 * @throws EventProcessingException
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) throws EventProcessingException {
		@SuppressWarnings("unchecked")
		final ReflectogramMismatchEvent reflectogramMismatchEvent = (ReflectogramMismatchEvent) event;
		Log.debugMessage("ReflectogramMismatchEvent: "
				+ reflectogramMismatchEvent + " started being processed",
				SEVERE);

		try {
			final Identifier measurementId = reflectogramMismatchEvent.getMeasurementId();
			if (measurementId.isVoid()) {
				throw new NullPointerException("Measurement is null");
			}
			final Measurement measurement = StorableObjectPool.getStorableObject(measurementId, true);
			final Test test = measurement.getTest();
			final MonitoredElement monitoredElement = test.getMonitoredElement();
			final KIS kis = test.getKIS();
			final MeasurementPort measurementPort = monitoredElement.getMeasurementPort();
			final Identifier portId = measurementPort.getPortId();
			if (portId.isVoid()) {
				throw new EventProcessingException("For MeasurementPort: "
						+ measurementPort.getId()
						+ " Port is null");
			}
			final Set<TransmissionPath> transmissionPaths =
					StorableObjectPool.getStorableObjectsByCondition(
							new LinkedIdsCondition(portId, TRANSMISSIONPATH_CODE),
							true);
			final int transmissionPathsSize = transmissionPaths.size();
			if (transmissionPathsSize != 1) {
				throw new EventProcessingException(
						"For Port: " + portId
						+ ", actual TransmissionPath count: "
						+ transmissionPathsSize
						+ "; expected: 1");
			}
			final Identifier transmissionPathId = transmissionPaths.iterator().next().getId();
			final Set<SchemePath> schemePaths =
					StorableObjectPool.getStorableObjectsByCondition(
							new LinkedIdsCondition(transmissionPathId, SCHEMEPATH_CODE),
							true);
			final int schemePathsSize = schemePaths.size();
			if (schemePathsSize != 1) {
				throw new EventProcessingException(
						"For TransmissionPath: "
						+ transmissionPathId
						+ ", actual SchemePath count: "
						+ schemePathsSize
						+ "; expected: 1");
			}
			final SchemePath schemePath = schemePaths.iterator().next();
			final Identifier schemePathId = schemePath.getId();
			final double totalOpticalLength = schemePath.getOpticalLength();
			final double deltaX = reflectogramMismatchEvent.getDeltaX();
			final double eventOpticalDistance = reflectogramMismatchEvent.getCoord() * deltaX;			

			final double epsilon = deltaX / 2.0;

			/*
			 * Throw an exception if eventOpticalDistance, in points,
			 * is greater than totalOpticalLength, in points.
			 */
			if (eventOpticalDistance > totalOpticalLength + epsilon) {
				throw new EventProcessingException(
						"For SchemePath: " + schemePathId
						+ ", eventOpticalDistance: "
						+ eventOpticalDistance
						+ " is greater than totalOpticalLength: "
						+ totalOpticalLength);
			}

			double minimumMetric = Double.POSITIVE_INFINITY;
			PathElement affectedPathElement = null;
			double physicalDistanceFromStart = 0.0;
			double physicalDistanceFromEnd = 0.0;
			double eventPhysicalDistance = 0.0;

			double currentOpticalDistance = 0.0;
			double currentPhysicalDistance = 0.0;
			for (final PathElement pathElement : schemePath.getPathMembers()) {
				final double previousOpticalDistance = currentOpticalDistance;
				final double previousPhysicalDistance = currentPhysicalDistance;

				final double opticalLength = pathElement.getOpticalLength();
				final double physicalLength = pathElement.getPhysicalLength();

				currentOpticalDistance += opticalLength;
				currentPhysicalDistance += physicalLength;

				final boolean pathElementSpacious = pathElement.isSpacious();
				final double currentMetric = calculateMetric(pathElementSpacious,
						previousOpticalDistance,
						currentOpticalDistance,
						eventOpticalDistance,
						epsilon);
				if (currentMetric < minimumMetric) {
					minimumMetric = currentMetric;
					affectedPathElement = pathElement;
					physicalDistanceFromStart = pathElementSpacious
							? physicalLength * (eventOpticalDistance - previousOpticalDistance)
									/ (currentOpticalDistance - previousOpticalDistance)
							: 0.0;
					physicalDistanceFromEnd = physicalLength - physicalDistanceFromStart;
					eventPhysicalDistance = previousPhysicalDistance + physicalDistanceFromStart;
				}
			}
			if (affectedPathElement == null) {
				throw new EventProcessingException("SchemePath: "
						+ schemePathId + " is empty");
			}

			final LineMismatchEvent lineMismatchEvent = DefaultLineMismatchEvent.newInstance(
					LoginManager.getUserId(),
					affectedPathElement.getId(),
					affectedPathElement.isSpacious(),
					physicalDistanceFromStart,
					physicalDistanceFromEnd,
					eventOpticalDistance,
					eventPhysicalDistance,
					createMessage(reflectogramMismatchEvent,
							test,
							kis,
							monitoredElement,
							affectedPathElement,
							physicalDistanceFromStart,
							physicalDistanceFromEnd),
					reflectogramMismatchEvent.getId());
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			servantManager.getEventServerReference().receiveEvents(new IdlEvent[] {
					lineMismatchEvent.getIdlTransferable(servantManager.getCORBAServer().getOrb())});
		} catch (final ApplicationException ae) {
			throw new EventProcessingException(ae);
		} catch (final IdlEventProcessingException epe) {
			throw new EventProcessingException(epe.message, epe);
		}
	}

	/**
	 * <p>Calculates a metric for a given {@link PathElement}.</p>
	 *
	 * <p>For a <em>spacious</em> {@link PathElement}, returns
	 * &#949;&nbsp;(epsilon) if it is crossed with the event (specified by
	 * {@code eventCoord}); otherwise returns
	 * {@linkplain Double#POSITIVE_INFINITY
	 * +&#8734;&nbsp;(positive&nbsp;infinity)}.</p>
	 *
	 * <p>For a <em>non-spacious</em> {@link PathElement}, returns optical
	 * distance from it to the event.</p>
	 *
	 * @param pathElementSpacious
	 * @param pathElementStartOpticalDistance optical distance from the
	 *        start of the path/reflectogram to {@link PathElement} start,
	 *        in meters.
	 * @param pathElementEndOpticalDistance optical distance from the
	 *        start of the path/reflectogram to {@link PathElement} end,
	 *        in meters.
	 * @param eventOpticalDistance optical distance from the start of the
	 *        path/reflectogram to the event, in meters.
	 * @param epsilon reflectometer inaccuracy halved, in meters.
	 */
	private static double calculateMetric(final boolean pathElementSpacious,
			final double pathElementStartOpticalDistance,
			final double pathElementEndOpticalDistance,
			final double eventOpticalDistance,
			final double epsilon) {
		return pathElementSpacious
				? pathElementStartOpticalDistance <= eventOpticalDistance && eventOpticalDistance <= pathElementEndOpticalDistance
						? epsilon
						: Double.POSITIVE_INFINITY
				: Math.abs(pathElementStartOpticalDistance - eventOpticalDistance);
	}

	/**
	 * @return a plain-text, human-readable, localized representaion of the
	 *         {@link com.syrus.AMFICOM.reflectometry.ReflectogramMismatch mismatch}
	 *         that triggerred this event's generation.
	 */
	private static String createMessage(
			final ReflectogramMismatchEvent reflectogramMismatchEvent,
			final Test test,
			final KIS kis,
			final MonitoredElement monitoredElement,
			final PathElement affectedPathElement,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd) {
		return MISMATCH_CREATED + COLON_TAB + EasyDateFormatter.formatDate(reflectogramMismatchEvent.getCreated()) + NEWLINE
				+ "Discovered by Test" + COLON_TAB + test.getDescription() + NEWLINE
				+ "Reflectometer" + COLON_TAB + kis.getName() + NEWLINE
				+ "Monitored Element" + COLON_TAB + monitoredElement.getDisplayedName() + NEWLINE
				+ NEWLINE
				+ reflectogramMismatchEvent.getSeverity().getLocalizedDescription() + NEWLINE
				+ reflectogramMismatchEvent.getAlarmType().getLocalizedDescription() + NEWLINE
				+ NEWLINE
				+ AFFECTED + SPACE + PATH_ELEMENT + COLON_TAB + getExtendedName(affectedPathElement.getId()) + NEWLINE
				+ (affectedPathElement.isSpacious()
						? PHYSICAL_DISTANCE_TO + SPACE + START_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + getLocalizedDistance((int) physicalDistanceToStart) + NEWLINE
						+ PHYSICAL_DISTANCE_TO + SPACE + END_OF + SPACE + PATH_ELEMENT_GENITIVE + COLON_TAB + getLocalizedDistance((int) physicalDistanceToEnd) + NEWLINE
						: "")
				+ NEWLINE
				+ (reflectogramMismatchEvent.hasMismatch()
						? MISMATCH_LEVEL + COLON_TAB + reflectogramMismatchEvent.getMinMismatch() + RANGE + reflectogramMismatchEvent.getMaxMismatch() + NEWLINE
						: "");
	}

	private static String getLocalizedDistance(final int distance) {
		assert distance >= 0 : distance;

		final String stringDistance = Integer.toString(distance);
		if (METER_SINGULAR_NOMINATIVE_REGEXP.matcher(stringDistance).matches()) {
			return stringDistance + SPACE + METER_SINGULAR_NOMINATIVE;
		} else if (METER_SINGULAR_GENITIVE_REGEXP.matcher(stringDistance).matches()) {
			return stringDistance + SPACE + METER_SINGULAR_GENITIVE;
		} else if (METER_PLURAL_GENITIVE_REGEXP.matcher(stringDistance).matches()) {
			return stringDistance + SPACE + METER_PLURAL_GENITIVE;
		} else {
			/*
			 * Never.
			 */
			assert false : stringDistance;
			return null;
		}
	}

	/**
	 * @param pathElementId
	 * @return the name of the PathElement in the following form:
	 *         &quot;&lt;name of the path element&gt; (&lt;name of the scheme
	 *         that contains it&gt;)&quot;
	 */
	private static String getExtendedName(final Identifier pathElementId) {
		try {
			final PathElement pathElement = StorableObjectPool
					.getStorableObject(pathElementId, true);
			if (pathElement == null) {
				/*
				 * We don't check all database data for integrity;
				 * however incoming data (i.e. pathElementId) may be
				 * invalid. 
				 */
				Log.debugMessage("PathElement identified by "
						+ pathElementId + " is null",
						SEVERE);
				return "";
			}

			return pathElement.getName() + SPACE + '('
					+ pathElement.getAbstractSchemeElement()
					.getNearestParentScheme().getName() + ')';
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return "";
		}
	}
}
