/*-
 * $Id: LineMismatchEvent.java,v 1.12 2005/11/22 19:33:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import java.util.Date;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * Generation of this one may be triggered upon receipt of a
 * {@link ReflectogramMismatchEvent}.
 * 
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/11/22 19:33:13 $
 * @module event
 */
public interface LineMismatchEvent extends Event<IdlLineMismatchEvent> {
	/**
	 * @return mismatch type; see {@link AlarmType AlarmType},
	 *         {@link com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAlarmType()}.
	 * @see AlarmType AlarmType
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getAlarmType()
	 */
	AlarmType getAlarmType();
	
	/**
	 * @return problem severity; see {@link Severity Severity},
	 *         {@link com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getSeverity()}.
	 * @see Severity Severity
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getSeverity()
	 */
	Severity getSeverity();

	/**
	 * @return {@code true} if threshold excess factor is defined; see
	 *         {@link com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#hasMismatch()}.
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#hasMismatch()
	 */
	boolean hasMismatch();

	/**
	 * @return lower bound of threshold excess factor if
	 *         {@code mismatch} is defined, i.&nbsp;e.
	 *         {@link #hasMismatch()}<code>&nbsp;==&nbsp;true</code>.
	 *         In this case, it&apos;s guaranteed that
	 *         <code>0.0&nbsp;&lt;=&nbsp;</code>{@link #getMinMismatch()
	 *         minMismatch}<code>&nbsp;&lt;=&nbsp;</code>{@link
	 *         #getMaxMismatch() maxMismatch}<code>&nbsp;&lt;=&nbsp;1.0</code>.
	 * @throws IllegalStateException if threshold excess factor is
	 *         undefined, i.&nbsp;e. {@link #hasMismatch()}<code>&nbsp;==&nbsp;false</code>.
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getMinMismatch()
	 */
	double getMinMismatch();

	/**
	 * @return upper bound of threshold excess factor if
	 *         {@code mismatch} is defined, i.&nbsp;e.
	 *         {@link #hasMismatch()}<code>&nbsp;==&nbsp;true</code>.
	 *         In this case, it&apos;s guaranteed that
	 *         <code>0.0&nbsp;&lt;=&nbsp;</code>{@link #getMinMismatch()
	 *         minMismatch}<code>&nbsp;&lt;=&nbsp;</code>{@link
	 *         #getMaxMismatch() maxMismatch}<code>&nbsp;&lt;=&nbsp;1.0</code>.
	 * @throws IllegalStateException if threshold excess factor is
	 *         undefined, i.&nbsp;e. {@link #hasMismatch()}<code>&nbsp;==&nbsp;false</code>.
	 * @see com.syrus.AMFICOM.reflectometry.ReflectogramMismatch#getMaxMismatch()
	 */
	double getMaxMismatch();

	/**
	 * @return identifier of the {@code PathElement} affected. It&apos;s
	 *         guaranteed to be both non-{@code null} and non-void.
	 */
	Identifier getAffectedPathElementId();

	/**
	 * @return {@code true} if the {@code PathElement} pointed to by
	 *         {@link #getAffectedPathElementId() affectedPathElementId}
	 *         has a {@code length} (i.&nbsp;e. {@code physicalLength},
	 *         {@code opticalLength}) attribute. Currently, this is true for
	 *         {@code PathElement}s that enclose either a {@code SchemeLink}
	 *         or a {@code SchemeCableLink}.
	 */
	boolean isAffectedPathElementSpacious();

	/**
	 * <p>Returns physical distance in meters from the point of occurence of
	 * this event to the <em>starting point</em> of the {@code PathElement}
	 * affected, <em>in case it is spacious</em>, i&#46;&nbsp;e&#46; {@link
	 * #isAffectedPathElementSpacious()
	 * affectedPathElementSpacious}<code>&nbsp;==&nbsp;true</code>.</p>
	 *
	 * <p><em>Starting point</em> is a point where the affected {@code
	 * PathElement} (with <code>sequentialNumber&nbsp;==&nbsp;N</code>, a
	 * <em>spacious</em> one) and its predecessor, a <em>non-spacious</em>
	 * {@code PathElement} with
	 * <code>sequentialNumber&nbsp;==&nbsp;(N&nbsp;-&nbsp;1)</code>, join
	 * together.</p>
	 *
	 * <p>Since a <em>spacious</em> {@code PathElement} can be neither the
	 * first nor the last one in its parent {@code SchemePath}, its predecessor
	 * and successor are guaranteed to exist <font color = "White">and be
	 * non-{@code null} ;-). Even if every <strike>mousebastard</strike>
	 * mousebuster forgets home his own head.</font></p>
	 *
	 * @return physical distance in meters from the point of occurence of
	 *         this event to the <em>starting point</em> of the
	 *         {@code PathElement} affected.
	 * @throws IllegalStateException if {@code PathElement} encloses a
	 *         {@code SchemeElement} and thus is <em>not&nbsp;spacious</em>,
	 *         i.&nbsp;e. {@link #isAffectedPathElementSpacious()
	 *         affectedPathElementSpacious}<code>&nbsp;==&nbsp;false</code>.
	 */
	double getPhysicalDistanceToStart();

	/**
	 * <p>Returns physical distance in meters from the point of occurence of
	 * this event to the <em>ending point</em> of the {@code PathElement}
	 * affected, <em>in case it is spacious</em>, i&#46;&nbsp;e&#46; {@link
	 * #isAffectedPathElementSpacious()
	 * affectedPathElementSpacious}<code>&nbsp;==&nbsp;true</code>.</p>
	 *
	 * <p><em>Ending point</em> is a point where the affected {@code
	 * PathElement} (with <code>sequentialNumber&nbsp;==&nbsp;N</code>, a
	 * <em>spacious</em> one) and its successor, a <em>non-spacious</em>
	 * {@code PathElement} with
	 * <code>sequentialNumber&nbsp;==&nbsp;(N&nbsp;+&nbsp;1)</code>, join
	 * together.</p>
	 *
	 * <p>Since a <em>spacious</em> {@code PathElement} can be neither the
	 * first nor the last one in its parent {@code SchemePath}, its predecessor
	 * and successor are guaranteed to exist <font color = "White">and be
	 * non-{@code null} ;-). Even if every <strike>mousebastard</strike>
	 * mousebuster forgets home his own head.</font></p>
	 *
	 * @return physical distance in meters from the point of occurence of
	 *         this event to the <em>starting point</em> of the
	 *         {@code PathElement} affected.
	 * @throws IllegalStateException if {@code PathElement} encloses a
	 *         {@code SchemeElement} and thus is <em>not&nbsp;spacious</em>,
	 *         i.&nbsp;e. {@link #isAffectedPathElementSpacious()
	 *         affectedPathElementSpacious}<code>&nbsp;==&nbsp;false</code>.
	 */
	double getPhysicalDistanceToEnd();

	/**
	 * @see PopupNotificationEvent#getResultId()
	 */
	Identifier getResultId();

	/**
	 * @see PopupNotificationEvent#getMismatchOpticalDistance()
	 */
	double getMismatchOpticalDistance();

	/**
	 * @see PopupNotificationEvent#getMismatchPhysicalDistance()
	 */
	double getMismatchPhysicalDistance();

	/**
	 * @see PopupNotificationEvent#getMismatchCreated()
	 */
	Date getMismatchCreated();
}
