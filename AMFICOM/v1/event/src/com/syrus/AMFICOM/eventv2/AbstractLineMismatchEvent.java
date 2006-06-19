/*-
 * $Id: AbstractLineMismatchEvent.java,v 1.16 2006/06/19 16:27:14 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.bugs.Crutch577;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2006/06/19 16:27:14 $
 * @module event
 */
public abstract class AbstractLineMismatchEvent extends StorableObject
		implements LineMismatchEvent {
	static DateFormat DATE_FORMAT = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss.SSS zzz yyyy",
			Locale.US);

	AbstractLineMismatchEvent(/*IdlLineMismatchEvent*/) {
		// super();
	}

	AbstractLineMismatchEvent(final Identifier id,
			final Identifier creatorId,
			final Date created,
			final StorableObjectVersion version) {
		super(id, created, created, creatorId, creatorId, version);
	}

	public final EventType getType() {
		return LINE_MISMATCH;
	}

	/**
	 * <p>If both {@code this} and {@code that} belong to the same
	 * <em>event&nbsp;group</em>, compares reflectogram mismatch events
	 * they&apos;re referencing via {@link
	 * LineMismatchEvent#getReflectogramMismatchEventId()}, otherwise throws
	 * an {@link IllegalArgumentException}.</p>
	 *
	 * <p>Obviously, a <em>group&nbsp;leader</em> is <em>less&nbsp;than</em>
	 * any other event in its group.</p>
	 *
	 * @param that
	 * @see Comparable#compareTo(Object)
	 * @throws IllegalArgumentException if {@code this} and {@code that}
	 *         belong to different <em>event&nbsp;groups</em>.
	 */
	public final int compareTo(final LineMismatchEvent that) {
		/*
		 * The same algorithm implemented by Saa:
		 */
//		final Identifier pThis = this.getParentLineMismatchEventId().isVoid()
//				? this.getId()
//				: this.getParentLineMismatchEventId();
//		final Identifier pThat = that.getParentLineMismatchEventId().isVoid()
//				? that.getId()
//				: that.getParentLineMismatchEventId();
//		if (!pThis.equals(pThat)) {
//			throw new IllegalArgumentException();
//		}
//		if (this.equals(that)) {
//			return 0;
//		}
//		if (this.equals(pThat)) {
//			return -1;
//		}
//		if (that.equals(pThis)) {
//			return 1;
//		}
//		try {
//			return this.getReflectogramMismatchEvent().compareTo(that.getReflectogramMismatchEvent());
//		} catch (final ApplicationException ae) {
//			throw new Error(ae);
//		}
		final Identifier thisParentId = this.getParentLineMismatchEventId();
		final Identifier thatParentId = that.getParentLineMismatchEventId();
		if (thisParentId.isVoid()) {
			/*-
			 * 1.
			 *
			 * "this" references no event.
			 */
			if (thatParentId.isVoid()) {
				/*-
				 * a.
				 *
				 * Unless "this" and "that" are the same event,
				 * they are leaders of different groups and
				 * hence should not be compared.
				 */
				if (this.equals(that)) {
					return 0;
				}
				throw new IllegalArgumentException("Group leaders "
						+ this.getId() + " and " + that.getId()
						+ " should not be compared.");
			} else if (this.equals(thatParentId)) {
				/*-
				 * b.
				 *
				 * "that" references "this".
				 * Ensure "this" < "that" and return -1.
				 */
				try {
					assert this.getReflectogramMismatchEvent().compareTo(that.getReflectogramMismatchEvent()) < 0 :
							this.getReflectogramMismatchEventId()
							+ " (" + DATE_FORMAT.format(this.getReflectogramMismatchEvent().getCreated()) + ')'
							+ " < "
							+ that.getReflectogramMismatchEventId()
							+ " (" + DATE_FORMAT.format(that.getReflectogramMismatchEvent().getCreated()) + ')';
					return -1;
				} catch (final ApplicationException ae) {
					throw new Error(ae);
				}
			} else {
				/*-
				 * c.
				 *
				 * "that" references some other event. Ensure it
				 * doesn't reference itself and, if not, throw
				 * an IAE.
				 */
				assert !thatParentId.equals(that) : that.getId();
				throw new IllegalArgumentException(that.getId()
						+ " doesn't belong to the group lead by "
						+ this.getId());
			}
		} else if (thisParentId.equals(that)) {
			/*-
			 * 2.
			 *
			 * "this" references "that".
			 * See 1.b.: return -that.compareTo(this)
			 */
			return -that.compareTo(this);
		} else {
			/*-
			 * 3.
			 *
			 * "this" references some other event.
			 * Ensure it doesn't reference itself.
			 */
			assert !this.equals(thisParentId);
			if (thatParentId.isVoid()) {
				/*-
				 * a.
				 *
				 * "that" references no event.
				 * See 1.c.: return -that.compareTo(this)
				 */
				return -that.compareTo(this);
			} else if (thatParentId.equals(thisParentId)) {
				/*-
				 * b.
				 *
				 * "that" references the same event as "this".
				 * Check whether they're the same,
				 * perform the comparison
				 * and return the result.
				 */
				if (this.equals(that)) {
					return 0;
				}
				try {
					final int returnValue = this.getReflectogramMismatchEvent().compareTo(that.getReflectogramMismatchEvent());
					assert returnValue != 0 :
							this.getReflectogramMismatchEventId()
							+ " (" + DATE_FORMAT.format(this.getReflectogramMismatchEvent().getCreated()) + ')'
							+ " != "
							+ that.getReflectogramMismatchEventId()
							+ " (" + DATE_FORMAT.format(that.getReflectogramMismatchEvent().getCreated()) + ')';
					return returnValue;
				} catch (final ApplicationException ae) {
					throw new Error(ae);
				}
			} else {
				/*-
				 * c.
				 *
				 * "that" references some other event.
				 */
				assert !thatParentId.equals(that) : that.getId();
				assert !thatParentId.equals(this) : "Bad hierarchy: "
						+ that.getId() + " --> " + this.id + " --> " + thisParentId;
				throw new IllegalArgumentException(
						"Child events from different groups should not be compared: "
						+ this.id + " --> " + thisParentId + "; "
						+ that.getId() + " --> " + thatParentId);
			}
		}
	}

	/**
	 * @see LineMismatchEvent#getReflectogramMismatchEvent()
	 */
	public final ReflectogramMismatchEvent getReflectogramMismatchEvent()
	throws ApplicationException {
		return StorableObjectPool.<AbstractReflectogramMismatchEvent>getStorableObject(
				this.getReflectogramMismatchEventId(),
				true);
	}

	/**
	 * @see LineMismatchEvent#setParentLineMismatchEventId(Identifier)
	 */
	public final void setParentLineMismatchEventId(
			final Identifier parentLineMismatchEvent)
	throws ApplicationException {
		this.setParentLineMismatchEvent(
				StorableObjectPool.<AbstractLineMismatchEvent> getStorableObject(
						parentLineMismatchEvent,
						true));
	}

	/**
	 * @see LineMismatchEvent#getParentLineMismatchEvent()
	 */
	public final LineMismatchEvent getParentLineMismatchEvent()
	throws ApplicationException {
		return StorableObjectPool.<AbstractLineMismatchEvent>getStorableObject(
				this.getParentLineMismatchEventId(),
				true);
	}

	private transient LinkedIdsCondition childLineMismatchEventsCondition
			= new LinkedIdsCondition(this.id, LINEMISMATCHEVENT_CODE);

	/**
	 * If there&apos;s a parent, returns an empty sorted set at once,
	 * without pool querying.
	 *
	 * @see LineMismatchEvent#getChildLineMismatchEvents()
	 */
	@Crutch577(notes = "Remove #setLinkedIdentifiable(...) invocation.")
	public final SortedSet<LineMismatchEvent> getChildLineMismatchEvents()
	throws ApplicationException {
		this.childLineMismatchEventsCondition.setLinkedIdentifiable(this);
		final Set<AbstractLineMismatchEvent> unsortedEvents = this.getParentLineMismatchEventId().isVoid()
				? StorableObjectPool.<AbstractLineMismatchEvent> getStorableObjectsByCondition(
						this.childLineMismatchEventsCondition,
						true)
				: Collections.<AbstractLineMismatchEvent> emptySet();
		final SortedSet<LineMismatchEvent> childLineMismatchEvents
				= new TreeSet<LineMismatchEvent>(unsortedEvents);
		return Collections.unmodifiableSortedSet(childLineMismatchEvents);
	}

	protected String paramString() {
		return "id = " + this.id + "; "
				+ "parentLineMismatchEvent = " + this.getParentLineMismatchEventId() + "; "
				+ "reflectogramMismatchEventId = " + this.getReflectogramMismatchEventId();
	}

	@Override
	public final String toString() {
		return this.getClass().getName() + "[" + this.paramString() + "]";
	}

	/**
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	@Override
	public abstract IdlLineMismatchEvent getIdlTransferable(final ORB orb);

	/**
	 * <em>Note:</em> {@link LineMismatchEvent#getParentLineMismatchEventId()}
	 * may return a void identifier.
	 *
	 * @see StorableObject#getDependenciesTmpl()
	 */
	@Override
	protected final Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.getAffectedPathElementId());
		dependencies.add(this.getReflectogramMismatchEventId());
		dependencies.add(this.getParentLineMismatchEventId());
		return dependencies;
	}

	/**
	 * @see StorableObject#getReverseDependencies(boolean)
	 */
	@Override
	protected final Set<Identifiable> getReverseDependencies(boolean usePool)
	throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.addAll(super.getReverseDependencies(usePool));
		final ReflectogramMismatchEvent reflectogramMismatchEvent = this.getReflectogramMismatchEvent();
		if (reflectogramMismatchEvent instanceof StorableObject) {
			reverseDependencies.addAll(getReverseDependencies(
					(StorableObject) reflectogramMismatchEvent,
					usePool));
		}
		for (final LineMismatchEvent lineMismatchEvent : this.getChildLineMismatchEvents()) {
			if (lineMismatchEvent instanceof StorableObject) {
				reverseDependencies.addAll(getReverseDependencies(
						(StorableObject) lineMismatchEvent,
						usePool));
			}
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	@Override
	protected final AbstractLineMismatchEventWrapper getWrapper() {
		return AbstractLineMismatchEventWrapper.getInstance();
	}

	private void readObject(final ObjectInputStream in)
	throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.childLineMismatchEventsCondition = new LinkedIdsCondition(this.id, LINEMISMATCHEVENT_CODE);
	}
}
