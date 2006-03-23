/*-
 * $Id: LineMismatchEvent.java,v 1.12.2.2 2006/03/23 07:58:01 bass Exp $
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
 * @version $Revision: 1.12.2.2 $, $Date: 2006/03/23 07:58:01 $
 * @module event
 */
public interface LineMismatchEvent extends Event<IdlLineMismatchEvent> {
	/**
	 * <p>Returns identifier of the {@code PathElement} affected. It&apos;s
	 * guaranteed to be both non-{@code null} and non-void, unless
	 * {@code PathElement} is deleted (which is unlikely for newly-created
	 * events, but still may happen in the future): in this latter case a
	 * void identifier will be returned by this method.</p>
	 *
	 * <p>Also, the original value of
	 * {@link #isAffectedPathElementSpacious() affectedPathElementSpacious}
	 * property is preserved if the {@code PathElement} is deleted.</p>
	 *
	 * <p>See also the note on nullability of {@link #getResultId() resultId}
	 * property.</p>
	 *
	 * @return identifier of the {@code PathElement} affected.
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
	 * @see PopupNotificationEvent#getMismatchOpticalDistance()
	 */
	double getMismatchOpticalDistance();

	/**
	 * @see PopupNotificationEvent#getMismatchPhysicalDistance()
	 */
	double getMismatchPhysicalDistance();

	Identifier getReflectogramMismatchEventId();
}
