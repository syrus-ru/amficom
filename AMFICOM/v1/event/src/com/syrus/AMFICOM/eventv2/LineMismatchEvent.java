/*-
 * $Id: LineMismatchEvent.java,v 1.16 2006/05/29 14:02:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;

/**
 * Generation of this one may be triggered upon receipt of a
 * {@link ReflectogramMismatchEvent}.
 * 
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2006/05/29 14:02:13 $
 * @module event
 */
public interface LineMismatchEvent
		extends Event<IdlLineMismatchEvent>, Identifiable {
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
	 * <p>See also the note on nullability of
	 * {@link #getReflectogramMismatchEventId() resultId} property.</p>
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

	double getMismatchOpticalDistance();

	double getMismatchPhysicalDistance();

	String getPlainTextMessage();

	String getRichTextMessage();

	/**
	 * <p>{@code reflectogramMismatchEventId} is guaranteed to be
	 * non-{@code null} and non-void during this event&apos;s transfer from
	 * an agent to the event server, but later on, if the
	 * {@link ReflectogramMismatchEvent} referenced by this
	 * {@code reflectogramMismatchEventId} is deleted, a void identifier
	 * will be returned by this method.</p>
	 *
	 * <p>See also the note on nullability of
	 * {@link #getAffectedPathElementId() affectedPathElementId} property.</p>
	 */
	Identifier getReflectogramMismatchEventId();

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.16 $, $Date: 2006/05/29 14:02:13 $
	 * @module event
	 */
	enum AlarmStatus {
		/**
		 * <p>Pending (NQMS-standard). Initial status an alarm is
		 * assigned after it is generated.</p>
		 *
		 * <p>Previous: none.</p>
		 *
		 * <p>Next: {@link #XTRA_ASSIGNED assigned} or {@link #TIMED_OUT
		 * timed out}.</p>
		 */
		@AllowedPredecessors({})
		PENDING,

		/**
		 * <p>Assigned (<em>Non-NQMS-standard</em>).</p>
		 *
		 * <p>Previous: {@link #PENDING pending}.</p>
		 *
		 * <p>Next: {@link #IGNORED ignored}, {@link #RESOLVED
		 * acknowledged and resolved} (with no <em>trouble&nbsp;ticket</em>)
		 * or {@link #ACKNOWLEDGED acknowledged, resolution pending}
		 * (with a <em>trouble&nbsp;ticket</em> having been assigned).</p>
		 */
		@AllowedPredecessors(PENDING)
		XTRA_ASSIGNED,

		/**
		 * <p>Ignored (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #XTRA_ASSIGNED assigned}.</p>
		 *
		 * <p>Next: {@link #XTRA_VERIFIED verified}.</p>
		 */
		@AllowedPredecessors(XTRA_ASSIGNED)
		IGNORED,

		/**
		 * <p> Acknowledged (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #XTRA_ASSIGNED assigned}.</p>
		 *
		 * <p>Next: {@link #IN_PROGRESS in progress}.</p>
		 */
		@AllowedPredecessors(XTRA_ASSIGNED)
		ACKNOWLEDGED,

		/**
		 * <p>In progress (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #ACKNOWLEDGED acknowledged}.</p>
		 *
		 * <p>Next: {@link #ABANDONED abandoned}, {@link
		 * #XTRA_TT_COMPLETED <em>trouble&nbsp;ticket</em> successfully
		 * completed} or {@link #TIMED_OUT timed out}.</p>
		 */
		@AllowedPredecessors(ACKNOWLEDGED)
		IN_PROGRESS,

		/**
		 * <p><em>Trouble&nbsp;ticket</em> successfully completed
		 * (<em>Non-NQMS-standard</em>).</p>
		 *
		 * <p>Previous: {@link #IN_PROGRESS in progress}.</p>
		 *
		 * <p>Next: {@link #XTRA_VTEST_IN_PROGRESS verification test in
		 * progress}.</p>
		 */
		@AllowedPredecessors(IN_PROGRESS)
		XTRA_TT_COMPLETED,

		/**
		 * <p>Verification test in progress (<em>Non-NQMS-standard</em>).</p>
		 *
		 * <p>Previous: {@link #XTRA_TT_COMPLETED <em>trouble&nbsp;ticket</em>
		 * successfully completed}.</p>
		 *
		 * <p>Next: {@link #RESOLVED resolved}.</p>
		 */
		@AllowedPredecessors(XTRA_TT_COMPLETED)
		XTRA_VTEST_IN_PROGRESS,

		/**
		 * <p>Resolved (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #XTRA_ASSIGNED assigned} or {@link
		 * #XTRA_VTEST_IN_PROGRESS verification test in progress}.</p>
		 *
		 * <p>Next: {@link #XTRA_VERIFIED verified}.</p>
		 */
		@AllowedPredecessors({XTRA_ASSIGNED, XTRA_VTEST_IN_PROGRESS})
		RESOLVED,

		/**
		 * <p>Abandoned (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #IN_PROGRESS in progress}.</p>
		 *
		 * <p>Next: {@link #XTRA_VERIFIED verified}.</p>
		 */
		@AllowedPredecessors(IN_PROGRESS)
		ABANDONED,

		/**
		 * <p>Timed out (NQMS-standard). Alarm automatically (i.&nbsp;e.
		 * without any human interference) gets assigned this status in
		 * either of the two cases:<ol>
		 *
		 * <li>it is a {@link #PENDING pending} alarm, and the operator
		 * doesn&apos;t change its status within the preset time-out
		 * period;</li>
		 * <li>it is an alarm with a <em>trouble&nbsp;ticket</em> in
		 * &quot;{@link #IN_PROGRESS in progress}&quot; state, and the
		 * <em>trouble&nbsp;ticket</em> gets neither {@link #ABANDONED
		 * abandoned} nor {@link #XTRA_TT_COMPLETED successfully
		 * completed} within the preset time-out period.</li>
		 * </ol></p>
		 *
		 * <p>Previous: {@link #PENDING pending} or {@link #IN_PROGRESS
		 * in progress}.</p>
		 *
		 * <p>Next: {@link #XTRA_VERIFIED verified}.</p>
		 */
		@AllowedPredecessors({PENDING, IN_PROGRESS})
		TIMED_OUT,

		/**
		 * <p>Verified (<em>Non-NQMS-standard</em>).</p>
		 *
		 * <p>Previous: {@link #RESOLVED resolved} (either with or
		 * without a <em>trouble&nbsp;ticket</em>), {@link #ABANDONED
		 * abandoned}, {@link #IGNORED ignored} or {@link #TIMED_OUT
		 * timed out}.</p>
		 *
		 * <p>Next: {@link #XTRA_CLOSED closed}.</p>
		 */
		@AllowedPredecessors({RESOLVED, ABANDONED, IGNORED, TIMED_OUT})
		XTRA_VERIFIED,

		/**
		 * <p>Closed (<em>Non-NQMS-standard</em>). This is the last
		 * state in the life cycle of an alarm.</p>
		 *
		 * <p>Previous: {@link #XTRA_VERIFIED verified}.</p>
		 *
		 * <p>Next: none.</p>
		 */
		@AllowedPredecessors(XTRA_VERIFIED)
		XTRA_CLOSED;

		/**
		 * @author Andrew ``Bass'' Shcheglov
		 * @author $Author: bass $
		 * @version $Revision: 1.16 $, $Date: 2006/05/29 14:02:13 $
		 * @module event
		 */
		@Retention(RUNTIME)
		@Target(FIELD)
		private @interface AllowedPredecessors {
			AlarmStatus[] value();
		}
	}
}
