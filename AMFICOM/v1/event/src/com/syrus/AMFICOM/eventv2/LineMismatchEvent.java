/*-
 * $Id: LineMismatchEvent.java,v 1.29 2006/06/16 14:51:46 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.logging.Level.SEVERE;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.SortedSet;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlAlarmStatus;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlTransferableObject;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * Generation of this one may be triggered upon receipt of a
 * {@link ReflectogramMismatchEvent}.
 * 
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.29 $, $Date: 2006/06/16 14:51:46 $
 * @module event
 */
public interface LineMismatchEvent
		extends Event<IdlLineMismatchEvent>, Identifiable,
		Comparable<LineMismatchEvent> {
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
	 * @throws ApplicationException
	 */
	ReflectogramMismatchEvent getReflectogramMismatchEvent()
	throws ApplicationException;

	/**
	 * <p>Updates <em>alarm&nbsp;status</em> of the event itself or its
	 * <em>event&nbsp;group</em> (if applicable).</p>
	 *  
	 * <p>If the event doesn&apos;t belong to any <em>event&nbsp;group</em>
	 * (i.&nbsp;e. doesn&apos;t reference any event as a parent and
	 * isn&apos;t referenced as a parent by any event) or is a leader of an
	 * <em>event&nbsp;group</em> (i.&nbsp;e. doesn&apos;t reference any
	 * event as a parent, but is referenced as a parent by at least one
	 * event), then its own <em>alarm&nbsp;status</em> is updated to {@code
	 * alarmStatus}.</p>
	 *
	 * <p>If the event belongs to an <em>event&nbsp;group</em>, but
	 * isn&apos;t a group leader (i.&nbsp;e. references some other event as
	 * a parent and isn&apos;t referenced as a parent by any event), then
	 * its group leader&apos;s <em>alarm&nbsp;status</em> is updated to
	 * {@code alarmStatus}.</p>
	 *
	 * <p>No event can be both a leader of an <em>event&nbsp;group</em> and
	 * a non-leader member of another <em>event&nbsp;group</em>; in other
	 * words, no event can belong to more than one group.</p>
	 *
	 * <p>No event can be both a leader of an <em>event&nbsp;group</em> and
	 * a non-leader member of the same <em>event&nbsp;group</em>; i.&nbsp;e.
	 * no event can reference itself as a parent.</p>
	 *
	 * <p>All newly-created events have &quot;{@link AlarmStatus#PENDING
	 * pending}&quot; <em>alarm&nbsp;status</em> and no parent (i.&nbsp;e.
	 * any such event is a leader of the <em>event&nbsp;group</em>
	 * consisting only of itself).</p>
	 *
	 * <p>Note that not any new <em>alarm&nbsp;status</em> is an allowed
	 * successor of the current event&apos;s status. If transition from
	 * current status to {@code alarmStatus} is disallowed, an {@link
	 * IllegalArgumentException} is thrown. To find out whether such
	 * transition is possible, {@link
	 * AlarmStatus#isAllowedPredecessorOf(LineMismatchEvent.AlarmStatus)}
	 * and {@link AlarmStatus#getAllowedSuccessors()} methods can be used.</p>
	 *
	 * @param alarmStatus the new <em>alarm&nbsp;status</em> to be set.
	 * @throws ApplicationException if this event itself is not a group
	 *         leader, and it tries to fish its group leader out of the
	 *         object pool in order to update the leader&apos;s alarm status,
	 *         and the pool throws an {@code ApplicationException}.
	 * @throws IllegalArgumentException if transition from current status to
	 *         {@code alarmStatus} is disallowed.
	 * @see AlarmStatus#isAllowedPredecessorOf(LineMismatchEvent.AlarmStatus)
	 * @see AlarmStatus#getAllowedSuccessors()
	 * @see #getAlarmStatus()
	 * @see #setParentLineMismatchEvent(LineMismatchEvent)
	 * @see #getParentLineMismatchEvent()
	 * @see #getChildLineMismatchEvents()
	 */
	void setAlarmStatus(final AlarmStatus alarmStatus) throws ApplicationException;

	/**
	 * @throws ApplicationException
	 * @see #setAlarmStatus(LineMismatchEvent.AlarmStatus)
	 * @see #setParentLineMismatchEvent(LineMismatchEvent)
	 * @see #getParentLineMismatchEvent()
	 * @see #getChildLineMismatchEvents()
	 */
	AlarmStatus getAlarmStatus() throws ApplicationException;

	/**
	 * <p>Updates this event&apos;s parent.</p>
	 *
	 * @param parentLineMismatchEventId
	 * @throws ApplicationException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @see #setParentLineMismatchEvent(LineMismatchEvent)
	 */
	void setParentLineMismatchEventId(final Identifier parentLineMismatchEventId)
	throws ApplicationException;

	/**
	 * <p>Updates this event&apos;s parent.</p>
	 *
	 * <p>Once this event becomes a non-leader member of an event group,
	 * invocation of this method will result in an {@code
	 * IllegalStateException}. In other words, it&apos;s impossible to
	 * &quot;detach&quot; nor &quot;reattach&quot; child events.</p>
	 *
	 * <p>If this event itself is a leader of a non-empty group, invocation
	 * of this method will also fail with an {@code IllegalStateException}.
	 * In other words, it&apos;is also impossible to &quot;attach&quot;
	 * group leaders.</p>
	 *
	 * <p>Once this event gets added to a group (as a child), its own
	 * alarm status information is lost, and further calls to {@link
	 * #getAlarmStatus()} will return the status of its parent. Also,
	 * parent&apos;s history log gets updated and a closed parent gets
	 * reopened again.</p>
	 *
	 * <p><strong>Note:</strong> this operation must be executed at most
	 * once per event. That means that if it can be executed on a client
	 * machine, it must be executed on at most one client machine
	 * (particularly the one running &quot;Observer&quot; module).</p>
	 *
	 * <p>On order to get a better idea of how this method behaves and what
	 * its contract is, see {@link EventHierarchyTestCase}.</p>
	 *
	 * @param parentLineMismatchEvent
	 * @throws ApplicationException
	 * @throws IllegalStateException if {@code this} is not a valid
	 *         subordinate for {@code parentLineMismatchEvent}.
	 * @throws IllegalArgumentException if {@code parentLineMismatchEvent}
	 *         is not a valid leader for {@code this}.
	 * @see #setAlarmStatus(LineMismatchEvent.AlarmStatus)
	 * @see #getAlarmStatus()
	 * @see #getParentLineMismatchEvent()
	 * @see #getChildLineMismatchEvents()
	 * @see EventHierarchyTestCase
	 */
	void setParentLineMismatchEvent(final LineMismatchEvent parentLineMismatchEvent)
	throws ApplicationException;

	/**
	 * @return identifier of the <em>group&nbsp;leader</em>, or {@link
	 *         Identifier#VOID_IDENTIFIER void identifier} if this event
	 *         itself is a <em>group&nbsp;leader</em>.
	 */
	Identifier getParentLineMismatchEventId();

	/**
	 * @return this event&apos;s <em>group&nbsp;leader</em>, or {@code null}
	 *         if this event itself is a <em>group&nbsp;leader</em>.
	 * @throws ApplicationException
	 * @see #setAlarmStatus(LineMismatchEvent.AlarmStatus)
	 * @see #getAlarmStatus()
	 * @see #setParentLineMismatchEvent(LineMismatchEvent)
	 * @see #getChildLineMismatchEvents()
	 */
	LineMismatchEvent getParentLineMismatchEvent() throws ApplicationException;

	/**
	 * <p>If this event is a group leader, returns all events in the group
	 * (excluding this one), otherwise returns an {@link
	 * java.util.Collections#emptySet() empty set}. If there&apos;re any
	 * child events, they will be returned as an {@link
	 * java.util.Collections#unmodifiableSet(Set) unmodifiable set}.</p>
	 *
	 * <p>If the underlying invocation of {@link
	 * com.syrus.AMFICOM.general.ObjectLoader#loadStorableObjectsButIdsByCondition(Set,
	 * com.syrus.AMFICOM.general.StorableObjectCondition)} throws an {@link
	 * ApplicationException}, the exception is rethrown by this method.</p>
	 *
	 * @return all events in this event&apos;s group (excluding the event
	 *         itself) if it&apos;s a group leader, or an {@link
	 *         java.util.Collections#emptySet() empty set} otherwise.
	 * @throws ApplicationException if object loader throws an {@code
	 *         ApplicationException}.
	 * @see java.util.Collections#emptySet()
	 * @see java.util.Collections#unmodifiableSet(Set)
	 * @see com.syrus.AMFICOM.general.ObjectLoader#loadStorableObjectsButIdsByCondition(Set, com.syrus.AMFICOM.general.StorableObjectCondition)
	 * @see #setAlarmStatus(LineMismatchEvent.AlarmStatus)
	 * @see #getAlarmStatus()
	 * @see #setParentLineMismatchEvent(LineMismatchEvent)
	 * @see #getParentLineMismatchEvent()
	 * @see #getChildLineMismatchEvents()
	 */
	SortedSet<LineMismatchEvent> getChildLineMismatchEvents()
	throws ApplicationException;

	/**
	 * Those alarm statii that aren&apos;t present in the
	 * <em>Consultronics&nbsp;NQMS</em> Alarm Model, have &laquo;{@code
	 * XTRA_}&raquo; prefix.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.29 $, $Date: 2006/06/16 14:51:46 $
	 * @module event
	 */
	enum AlarmStatus implements IdlTransferableObject<IdlAlarmStatus> {
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
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
		XTRA_ASSIGNED,

		/**
		 * <p>Ignored (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #XTRA_ASSIGNED assigned}.</p>
		 *
		 * <p>Next: {@link #XTRA_VERIFIED verified}.</p>
		 */
		@AllowedPredecessors(XTRA_ASSIGNED)
		@AllowedSuccessors({})
		IGNORED,

		/**
		 * <p> Acknowledged (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #XTRA_ASSIGNED assigned}.</p>
		 *
		 * <p>Next: {@link #IN_PROGRESS in progress}.</p>
		 */
		@AllowedPredecessors(XTRA_ASSIGNED)
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
		RESOLVED,

		/**
		 * <p>Abandoned (NQMS-standard).</p>
		 *
		 * <p>Previous: {@link #IN_PROGRESS in progress}.</p>
		 *
		 * <p>Next: {@link #XTRA_VERIFIED verified}.</p>
		 */
		@AllowedPredecessors(IN_PROGRESS)
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
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
		@AllowedSuccessors({})
		XTRA_CLOSED;

		private static final AlarmStatus VALUES[] = values();

		/**
		 * @param ordinal
		 * @throws IllegalArgumentException
		 */
		public static AlarmStatus valueOf(final int ordinal) {
			try {
				return VALUES[ordinal];
			} catch (final ArrayIndexOutOfBoundsException aioobe) {
				throw new IllegalArgumentException(String.valueOf(ordinal));
			}
		}

		/**
		 * @param alarmStatus
		 * @throws IllegalArgumentException
		 */
		public static AlarmStatus valueOf(final IdlAlarmStatus alarmStatus) {
			return valueOf(alarmStatus.value());
		}

		/**
		 * @param orb
		 * @throws IllegalArgumentException
		 * @see IdlTransferableObject#getIdlTransferable(ORB)
		 */
		public IdlAlarmStatus getIdlTransferable(final ORB orb) {
			try {
				return IdlAlarmStatus.from_int(this.ordinal());
			} catch (final BAD_PARAM bp) {
				throw new IllegalArgumentException(String.valueOf(this.ordinal()), bp);
			}
		}

		/**
		 * <p>For any two different alarm statii, <em>A</em> and <em>B</em>,
		 * {@code A.isAllowedPredecessorOf(B) ^ B.isAllowedPredecessorOf(A)}
		 * is {@code true} <em>only in case</em> <strong>state rollbacks
		 * aren&apos;t enabled</strong>.</p>
		 *
		 * <p>{@code A.isAllowedPredecessorOf(A)} is always {@code false}.</p>
		 *
		 * @param successor
		 * @see #getAllowedSuccessors()
		 */
		public boolean isAllowedPredecessorOf(final AlarmStatus successor) {
			if (successor == null) {
				throw new NullPointerException();
			}
			if (successor == this) {
				return false;
			}

			final Class<AlarmStatus> clazz = AlarmStatus.class;

			boolean possiblyAllowedPredecessor1;
			try {
				final Field field = clazz.getField(this.name());
				if (field.isAnnotationPresent(AllowedSuccessors.class)) {
					possiblyAllowedPredecessor1 = Arrays.asList(field.getAnnotation(AllowedSuccessors.class).value()).contains(successor);
				} else {
					possiblyAllowedPredecessor1 = false;
				}
			} catch (final SecurityException se) {
				Log.debugMessage(se, SEVERE);
				possiblyAllowedPredecessor1 = false;
			} catch (final NoSuchFieldException nsfe) {
				/*
				 * Never.
				 */
				assert false;
				possiblyAllowedPredecessor1 = false;
			}

			boolean possiblyAllowedPredecessor2;
			try {
				final Field field = clazz.getField(successor.name());
				if (field.isAnnotationPresent(AllowedPredecessors.class)) {
					possiblyAllowedPredecessor2 = Arrays.asList(field.getAnnotation(AllowedPredecessors.class).value()).contains(this);
				} else {
					possiblyAllowedPredecessor2 = false;
				}
			} catch (final SecurityException se) {
				Log.debugMessage(se, SEVERE);
				possiblyAllowedPredecessor2 = false;
			} catch (final NoSuchFieldException nsfe) {
				/*
				 * Never.
				 */
				assert false;
				possiblyAllowedPredecessor2 = false;
			}

			assert !(possiblyAllowedPredecessor1 && possiblyAllowedPredecessor2) : this + "; " + successor + "; possiblyAllowedPredecessor1 = " + possiblyAllowedPredecessor1  + "; possiblyAllowedPredecessor2 = " + possiblyAllowedPredecessor2;
			
			return possiblyAllowedPredecessor1 || possiblyAllowedPredecessor2;
		}

		/**
		 * @return a new mutable {@code Set} upon every invocation.
		 * @see #isAllowedPredecessorOf(LineMismatchEvent.AlarmStatus) 
		 */
		public EnumSet<AlarmStatus> getAllowedSuccessors() {
			final Class<AlarmStatus> clazz = AlarmStatus.class;

			final EnumSet<AlarmStatus> allowedSuccessors = EnumSet.noneOf(clazz);

			try {
				final Field field = clazz.getField(this.name());
				if (field.isAnnotationPresent(AllowedSuccessors.class)) {
					allowedSuccessors.addAll(Arrays.asList(field.getAnnotation(AllowedSuccessors.class).value()));
				}
			} catch (final SecurityException se) {
				Log.debugMessage(se, SEVERE);
			} catch (final NoSuchFieldException nsfe) {
				/*
				 * Never.
				 */
				assert false;
			}

			for (final AlarmStatus alarmStatus :  EnumSet.complementOf(EnumSet.of(this))) {
				final Field field;
				try {
					field = clazz.getField(alarmStatus.name());
				} catch (final SecurityException se) {
					Log.debugMessage(se, SEVERE);
					continue;
				} catch (final NoSuchFieldException nsfe) {
					/*
					 * Never.
					 */
					assert false;
					continue;
				}
				if (field.isAnnotationPresent(AllowedPredecessors.class)) {
					if (Arrays.asList(field.getAnnotation(AllowedPredecessors.class).value()).contains(this)) {
						allowedSuccessors.add(alarmStatus);
					}
				}
			}

			return allowedSuccessors;
		}

		/**
		 * Status <em>A</em> is an &laquo;allowed&nbsp;predecessor&raquo;
		 * of status <em>B</em> if an alarm with status <em>A</em> can be
		 * assigned status <em>B</em> directly, omitting any other
		 * intermediate status. The statement saying that <em>B</em> is
		 * an &laquo;allowed&nbsp;successor&raquo; of <em>A</em> has the
		 * same meaning. Logically, there&apos;s no need to introduce an
		 * additional {@link AllowedSuccessors} annotation, but semantically,
		 * there is one: fields can&apos;t be referenced prior they&apos;re
		 * defined. Thus, given the way enum constants are declared, one
		 * can&apos;t write:<pre style = "background: #fff7e9;">
		 * &#64;AllowedSuccessors(XTRA_CLOSED)
		 * XTRA_VERIFIED,
		 *
		 * XTRA_CLOSED;</pre>
		 * but can:
		 * <pre style = "background: #fff7e9;">
		 * XTRA_VERIFIED,
		 *
		 * &#64;AllowedPredecessors(XTRA_VERIFIED)
		 * XTRA_CLOSED;</pre>
		 *
		 * @author Andrew ``Bass'' Shcheglov
		 * @author $Author: bass $
		 * @version $Revision: 1.29 $, $Date: 2006/06/16 14:51:46 $
		 * @see AllowedSuccessors
		 * @module event
		 */
		@Retention(RUNTIME)
		@Target(FIELD)
		private @interface AllowedPredecessors {
			AlarmStatus[] value();
		}

		/**
		 * @author Andrew ``Bass'' Shcheglov
		 * @author $Author: bass $
		 * @version $Revision: 1.29 $, $Date: 2006/06/16 14:51:46 $
		 * @see AllowedPredecessors
		 * @module event
		 */
		@Retention(RUNTIME)
		@Target(FIELD)
		private @interface AllowedSuccessors {
			AlarmStatus[] value();
		}

		/**
		 * A mutable holder for immutable enum instances.
		 *
		 * @author Andrew ``Bass'' Shcheglov
		 * @author $Author: bass $
		 * @version $Revision: 1.29 $, $Date: 2006/06/16 14:51:46 $
		 * @module event
		 */
		static final class Proxy
				implements IdlTransferableObjectExt<IdlAlarmStatus>,
				Serializable {
			private static final long serialVersionUID = -6406170104542231355L;

			private AlarmStatus value;

			/**
			 * Creates a new uninitialized instance.
			 */
			public Proxy() {
				// empty
			}

			public Proxy(final AlarmStatus value) {
				this.value = value;
			}

			public AlarmStatus getValue() {
				return this.value;
			}

			public void setValue(final AlarmStatus value) {
				this.value = value;
			}

			/**
			 * @param orb
			 * @throws IllegalArgumentException
			 * @see IdlTransferableObject#getIdlTransferable(ORB)
			 */
			public IdlAlarmStatus getIdlTransferable(final ORB orb) {
				return this.value.getIdlTransferable(orb);
			}

			/**
			 * @param alarmStatus
			 * @throws IllegalArgumentException
			 * @see IdlTransferableObjectExt#fromIdlTransferable(org.omg.CORBA.portable.IDLEntity)
			 */
			public void fromIdlTransferable(final IdlAlarmStatus alarmStatus) {
				this.value = valueOf(alarmStatus);
			}
		}
	}
}
