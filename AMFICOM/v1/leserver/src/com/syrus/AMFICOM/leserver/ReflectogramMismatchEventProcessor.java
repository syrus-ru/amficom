/*-
 * $Id: ReflectogramMismatchEventProcessor.java,v 1.6 2005/10/24 08:56:51 bass Exp $
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.leserver.corba.EventServerPackage.IdlEventProcessingException;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/10/24 08:56:51 $
 * @module leserver
 */
final class ReflectogramMismatchEventProcessor implements
		EventProcessor {
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
	public void processEvent(final Event event) throws EventProcessingException {
		final ReflectogramMismatchEvent reflectogramMismatchEvent = (ReflectogramMismatchEvent) event;
		Log.debugMessage("ReflectogramMismatchEventProcessor.processEvent() | ReflectogramMismatchEvent: "
				+ reflectogramMismatchEvent + " started being processed",
				SEVERE);

		try {
			final Identifier resultId = reflectogramMismatchEvent.getResultId();
			if (resultId.isVoid()) {
				throw new NullPointerException("Result is null");
			}
			final Identifier monitoredElementId = reflectogramMismatchEvent.getMonitoredElementId();
			if (monitoredElementId.isVoid()) {
				throw new NullPointerException("MonitoredElement is null");
			}
			final MonitoredElement monitoredElement = StorableObjectPool.getStorableObject(monitoredElementId, true);
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
			if (eventOpticalDistance > totalOpticalLength) {
				throw new EventProcessingException(
						"For SchemePath: " + schemePathId
						+ ", eventOpticalDistance: "
						+ eventOpticalDistance
						+ " is greater than totalOpticalLength: "
						+ totalOpticalLength);
			}

			final double epsilon = deltaX / 2.0;

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

			final boolean mismatch;
			double minMismatch = 0.0;
			double maxMismatch = 0.0;
			if (!!(mismatch = reflectogramMismatchEvent.hasMismatch())) {
				minMismatch = reflectogramMismatchEvent.getMinMismatch();
				maxMismatch = reflectogramMismatchEvent.getMaxMismatch();
			}

			final LineMismatchEvent lineMismatchEvent = DefaultLineMismatchEvent.valueOf(
					reflectogramMismatchEvent.getAlarmType(),
					reflectogramMismatchEvent.getSeverity(),
					mismatch, minMismatch, maxMismatch,
					affectedPathElement.getId(),
					affectedPathElement.isSpacious(),
					physicalDistanceFromStart,
					physicalDistanceFromEnd, resultId,
					eventOpticalDistance,
					eventPhysicalDistance,
					reflectogramMismatchEvent.getCreated());
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			servantManager.getEventServerReference().receiveEvents(new IdlEvent[] {
					lineMismatchEvent.getTransferable(servantManager.getCORBAServer().getOrb())});
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
}
