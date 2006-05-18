/*-
 * $Id: ReflectogramMismatchEventProcessor.java,v 1.24 2006/05/18 19:47:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.configuration.EquipmentTypeCodename.MUFF;
import static com.syrus.AMFICOM.eventv2.EventType.REFLECTORGAM_MISMATCH;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
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
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.scheme.AbstractSchemeElement;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.24 $, $Date: 2006/05/18 19:47:18 $
 * @module leserver
 */
final class ReflectogramMismatchEventProcessor extends AbstractEventProcessor {
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

	ReflectogramMismatchEventProcessor(final int capacity) {
		super(capacity);
	}

	ReflectogramMismatchEventProcessor() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return REFLECTORGAM_MISMATCH;
	}

	/**
	 * @param event
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event<?> event) {
		final long t0 = System.nanoTime();

		@SuppressWarnings("unchecked")
		final ReflectogramMismatchEvent reflectogramMismatchEvent = (ReflectogramMismatchEvent) event;
		Log.debugMessage("ReflectogramMismatchEvent: "
				+ reflectogramMismatchEvent + " started being processed",
				FINEST);

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
				Log.debugMessage("For MeasurementPort: "
						+ measurementPort.getId()
						+ " Port is null",
						SEVERE);
				return;
			}
			final Set<TransmissionPath> transmissionPaths =
					StorableObjectPool.getStorableObjectsByCondition(
							new LinkedIdsCondition(portId, TRANSMISSIONPATH_CODE),
							true);
			final int transmissionPathsSize = transmissionPaths.size();
			if (transmissionPathsSize != 1) {
				Log.debugMessage("For Port: " + portId
						+ ", actual TransmissionPath count: "
						+ transmissionPathsSize
						+ "; expected: 1",
						SEVERE);
				return;
			}
			final Identifier transmissionPathId = transmissionPaths.iterator().next().getId();
			final Set<SchemePath> schemePaths =
					StorableObjectPool.getStorableObjectsByCondition(
							new LinkedIdsCondition(transmissionPathId, SCHEMEPATH_CODE),
							true);
			final int schemePathsSize = schemePaths.size();
			if (schemePathsSize != 1) {
				Log.debugMessage("For TransmissionPath: "
						+ transmissionPathId
						+ ", actual SchemePath count: "
						+ schemePathsSize
						+ "; expected: 1",
						SEVERE);
				return;
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
				Log.debugMessage("For SchemePath: " + schemePathId
						+ ", eventOpticalDistance: "
						+ eventOpticalDistance
						+ " is greater than totalOpticalLength: "
						+ totalOpticalLength,
						SEVERE);
				return;
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
				Log.debugMessage("SchemePath: "
						+ schemePathId + " is empty",
						SEVERE);
				return;
			}

			final String plainTextMessage = createPlainTextMessage(
					reflectogramMismatchEvent,
					test,
					kis,
					monitoredElement,
					affectedPathElement,
					physicalDistanceFromStart,
					physicalDistanceFromEnd);
			final String richTextMessage = toRichTextMessage(plainTextMessage);

			final LineMismatchEvent lineMismatchEvent = DefaultLineMismatchEvent.newInstance(
					LoginManager.getUserId(),
					affectedPathElement.getId(),
					affectedPathElement.isSpacious(),
					physicalDistanceFromStart,
					physicalDistanceFromEnd,
					eventOpticalDistance,
					eventPhysicalDistance,
					plainTextMessage,
					richTextMessage,
					reflectogramMismatchEvent.getId());
			final LEServerServantManager servantManager = LEServerSessionEnvironment.getInstance().getLEServerServantManager();
			servantManager.getEventServerReference().receiveEvents(new IdlEvent[] {
					lineMismatchEvent.getIdlTransferable(servantManager.getCORBAServer().getOrb())});
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}

		final long t1 = System.nanoTime();
		Log.debugMessage(((t1 - t0) / 1e9) + " second(s)", FINEST);
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
	private static String createPlainTextMessage(
			final ReflectogramMismatchEvent reflectogramMismatchEvent,
			final Test test,
			final KIS kis,
			final MonitoredElement monitoredElement,
			final PathElement affectedPathElement,
			final double physicalDistanceToStart,
			final double physicalDistanceToEnd) {
		String leftNonSpaciousName = null;
		String rightNonSpaciousName = null;

		if (affectedPathElement.isSpacious()) {
			if (false) {
				printDebugInfo(affectedPathElement);
			}

			try {
				final SchemePath parentPathOwner = affectedPathElement.getParentPathOwner();
				final ArrayList<PathElement> pathMembers = new ArrayList<PathElement>(parentPathOwner.getPathMembers());
				final int sequentialNumber = affectedPathElement.getSequentialNumber();


				for (int i = sequentialNumber - 1, n = 0; i >= n; i--) {
					final PathElement pathElement = pathMembers.get(i);
					if (pathElement.isSpacious()) {
						/**
						 * @bug The distance reported
						 * will be incorrect if there're
						 * two adjacent spacious path
						 * elements.
						 */
						continue;
					}
					leftNonSpaciousName = getExtendedName(pathElement.getId());
					break;
				}

				
				for (int i = sequentialNumber + 1, n = pathMembers.size(); i < n; i++) {
					final PathElement pathElement = pathMembers.get(i);
					if (pathElement.isSpacious()) {
						/**
						 * @bug The distance reported
						 * will be incorrect if there're
						 * two adjacent spacious path
						 * elements.
						 */
						continue;
					}
					rightNonSpaciousName = getExtendedName(pathElement.getId());
					break;
				}
			} catch (final ApplicationException ae) {
				Log.debugMessage(ae, SEVERE);
			}
		}
		return MISMATCH_CREATED + COLON_TAB + EasyDateFormatter.formatDate(reflectogramMismatchEvent.getCreated()) + NEWLINE
//				+ "Discovered by Test" + COLON_TAB + test.getDescription() + NEWLINE
				/**
				 * @todo localize me
				 */
				+ "\u041e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d\u043e \u0442\u0435\u0441\u0442\u043e\u043c" + COLON_TAB + test.getDescription() + NEWLINE
//				+ "Reflectometer" + COLON_TAB + kis.getName() + NEWLINE
				/**
				 * @todo localize me
				 */
				+ "\u0420\u0435\u0444\u043b\u0435\u043a\u0442\u043e\u043c\u0435\u0442\u0440" + COLON_TAB + kis.getName() + NEWLINE
//				+ "Monitored Element" + COLON_TAB + monitoredElement.getDisplayedName() + NEWLINE
				/**
				 * @todo localize me
				 */
				+ "\u041b\u0438\u043d\u0438\u044f \u0442\u0435\u0441\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f" + COLON_TAB + monitoredElement.getDisplayedName() + NEWLINE
				+ NEWLINE
				+ reflectogramMismatchEvent.getSeverity().getLocalizedDescription() + NEWLINE
				+ reflectogramMismatchEvent.getAlarmType().getLocalizedDescription() + NEWLINE
				+ NEWLINE
				+ AFFECTED + SPACE + PATH_ELEMENT + COLON_TAB + getExtendedName(affectedPathElement.getId()) + NEWLINE
				+ (affectedPathElement.isSpacious()
						? PHYSICAL_DISTANCE_TO + SPACE
								+ (leftNonSpaciousName == null
										/**
										 * @todo localize me
										 */
										? START_OF + SPACE + PATH_ELEMENT_GENITIVE + SPACE + "(\u043e\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u0443\u0437\u043b\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e)"
										: "\u0443\u0437\u043b\u0430" + SPACE + leftNonSpaciousName)
								+ COLON_TAB + getLocalizedDistance((int) physicalDistanceToStart) + NEWLINE
						+ PHYSICAL_DISTANCE_TO + SPACE
								+ (rightNonSpaciousName == null
										/**
										 * @todo localize me
										 */
										? END_OF + SPACE + PATH_ELEMENT_GENITIVE + SPACE + "(\u043e\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u0443\u0437\u043b\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e)"
										: "\u0443\u0437\u043b\u0430" + SPACE + rightNonSpaciousName)
								+ COLON_TAB + getLocalizedDistance((int) physicalDistanceToEnd) + NEWLINE
						: "")
				+ NEWLINE
				+ (reflectogramMismatchEvent.hasMismatch()
						? MISMATCH_LEVEL + COLON_TAB + reflectogramMismatchEvent.getMinMismatch() + RANGE + reflectogramMismatchEvent.getMaxMismatch() + NEWLINE
						: "");
	}

	private static String toRichTextMessage(final String plainTextMessage) {
		final StringBuilder builder = new StringBuilder();
		builder.append(plainTextMessage.replaceAll("&", "&amp;").
				replaceAll("\"", "&quot;").
				replaceAll("'", "&apos;").
				replaceAll("<", "&lt;").
				replaceAll(">", "&gt;").
				replaceAll("\n", "<br>\n"));			
		return builder.toString();
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

			final AbstractSchemeElement abstractSchemeElement = pathElement.getAbstractSchemeElement();
			final String schemeElementName = abstractSchemeElement.getName();
			final String schemeName = abstractSchemeElement.getNearestParentScheme().getName();
			if (pathElement.isSpacious()) {
				return schemeElementName + SPACE + '(' + schemeName + ')';
			}
			final SchemeElement schemeElement = (SchemeElement) abstractSchemeElement;
			final ProtoEquipment protoEquipment = schemeElement.getProtoEquipment();
			if (protoEquipment == null) {
				Log.debugMessage("For SchemeElement: "
						+ schemeElement.getId()
						+ ", ProtoEquipment is null",
						WARNING);
				return schemeElementName + SPACE + '(' + schemeName + ')';
			}
			final EquipmentType equipmentType = protoEquipment.getType();
			return equipmentType.getDescription() + ' ' + schemeElementName + SPACE + '(' + schemeName + ')';
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return "";
		}
	}

	/**
	 * Currently, just logs names of the nodes nearest to
	 * {@code affectedPathElement}. Later on, will be used to form a message
	 * to deliver to clients. Search mechanism to be transformed into a more
	 * intelligent one.
	 *  
	 * @param affectedPathElement
	 */
	private static void printDebugInfo(final PathElement affectedPathElement) {
		final long t0 = System.nanoTime();

		String leftNonSpaciosName = null;
		String leftNonMuffName = null;
		String leftMuffName = null;

		String rightNonSpaciosName = null;
		String rightNonMuffName = null;
		String rightMuffName = null;
		try {
			final SchemePath parentPathOwner = affectedPathElement.getParentPathOwner();
			final ArrayList<PathElement> pathMembers = new ArrayList<PathElement>(parentPathOwner.getPathMembers());
			final int sequentialNumber = affectedPathElement.getSequentialNumber();


			for (int i = sequentialNumber - 1, n = 0; i >= n; i--) {
				final PathElement pathElement = pathMembers.get(i);
				if (pathElement.isSpacious()) {
					continue;
				}
				final SchemeElement schemeElement = (SchemeElement) pathElement.getAbstractSchemeElement();
				if (leftNonSpaciosName == null) {
					leftNonSpaciosName = schemeElement.getName();
					assert leftNonSpaciosName != null;
				}
				final ProtoEquipment protoEquipment = schemeElement.getProtoEquipment();
				if (protoEquipment == null) {
					Log.debugMessage("For SchemeElement: "
							+ schemeElement.getId()
							+ ", ProtoEquipment is null",
							WARNING);
					continue;
				}
				final EquipmentType equipmentType = protoEquipment.getType();
				final String equipmentTypeCodename = equipmentType.getCodename();
				final String muffCodename = MUFF.stringValue();
				if (equipmentTypeCodename.equals(muffCodename) && leftMuffName == null) {
					leftMuffName = equipmentType.getDescription() + ' ' + schemeElement.getName();
					assert leftMuffName != null;
				} else if (!equipmentTypeCodename.equals(muffCodename) && leftNonMuffName == null) {
					leftNonMuffName = equipmentType.getDescription() + ' ' + schemeElement.getName();
					assert leftNonMuffName != null;
				}

				if (leftNonMuffName != null && leftMuffName != null) {
					break;
				}
			}


			for (int i = sequentialNumber + 1, n = pathMembers.size(); i < n; i++) {
				final PathElement pathElement = pathMembers.get(i);
				if (pathElement.isSpacious()) {
					continue;
				}
				final SchemeElement schemeElement = (SchemeElement) pathElement.getAbstractSchemeElement();
				if (rightNonSpaciosName == null) {
					rightNonSpaciosName = schemeElement.getName();
					assert rightNonSpaciosName != null;
				}
				final ProtoEquipment protoEquipment = schemeElement.getProtoEquipment();
				if (protoEquipment == null) {
					Log.debugMessage("For SchemeElement: "
							+ schemeElement.getId()
							+ ", ProtoEquipment is null",
							WARNING);
					continue;
				}
				final EquipmentType equipmentType = protoEquipment.getType();
				final String equipmentTypeCodename = equipmentType.getCodename();
				final String muffCodename = MUFF.stringValue();
				if (equipmentTypeCodename.equals(muffCodename) && rightMuffName == null) {
					rightMuffName = equipmentType.getDescription() + ' ' + schemeElement.getName();
					assert rightMuffName != null;
				} else if (!equipmentTypeCodename.equals(muffCodename) && rightNonMuffName == null) {
					rightNonMuffName = equipmentType.getDescription() + ' ' + schemeElement.getName();
					assert rightNonMuffName != null;
				}

				if (rightNonMuffName != null && rightMuffName != null) {
					break;
				}
			}
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			final String fallbackName = '<' + ae.getLocalizedMessage() + '>';

			leftNonSpaciosName = fallbackName;
			leftNonMuffName = fallbackName;
			leftMuffName = fallbackName;

			rightNonSpaciosName = fallbackName;
			rightNonMuffName = fallbackName;
			rightMuffName = fallbackName;
		}

		if (leftNonSpaciosName == null) {
			leftNonSpaciosName = "N/A";
		}
		if (leftNonMuffName == null) {
			leftNonMuffName = "N/A";
		}
		if (leftMuffName == null) {
			leftMuffName = "N/A";
		}

		if (rightNonSpaciosName == null) {
			rightNonSpaciosName = "N/A";
		}
		if (rightNonMuffName == null) {
			rightNonMuffName = "N/A";
		}
		if (rightMuffName == null) {
			rightMuffName = "N/A";
		}

		Log.debugMessage("*** Nearest left node: " + leftNonSpaciosName, INFO);
		Log.debugMessage("*** Nearest left non-muff node: " + leftNonMuffName, INFO);
		Log.debugMessage("*** Nearest left muff: " + leftMuffName, INFO);
		Log.debugMessage("*** Nearest right node: " + rightNonSpaciosName, INFO);
		Log.debugMessage("*** Nearest right non-muff node: " + rightNonMuffName, INFO);
		Log.debugMessage("*** Nearest right muff: " + rightMuffName, INFO);

		final long t1 = System.nanoTime();
		Log.debugMessage(((t1 - t0) / 1e9) + " second(s)", FINEST);
	}
}
