/*-
 * $Id: AbstractLineMismatchEvent.java,v 1.8 2006/06/13 07:55:32 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.EventType.LINE_MISMATCH;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEvent;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventHelper;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialData;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlAffectedPathElementSpacious;
import com.syrus.AMFICOM.eventv2.corba.IdlLineMismatchEventPackage.IdlSpacialDataPackage.IdlPhysicalDistancePair;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/06/13 07:55:32 $
 * @module event
 */
public abstract class AbstractLineMismatchEvent extends StorableObject
		implements LineMismatchEvent {
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
			} else if (thatParentId.equals(this)) {
				/*-
				 * b.
				 *
				 * "that" references "this".
				 * Ensure "this" < "that" and return -1.
				 */
				final Identifier thisReflectogramMismatchEventId = this.getReflectogramMismatchEventId();
				final Identifier thatReflectogramMismatchEventId = that.getReflectogramMismatchEventId();
				try {
					final ReflectogramMismatchEvent thisReflectogramMismatchEvent = this.getReflectogramMismatchEvent();
					final ReflectogramMismatchEvent thatReflectogramMismatchEvent = that.getReflectogramMismatchEvent();
					assert thisReflectogramMismatchEvent.compareTo(thatReflectogramMismatchEvent) < 0 :
							thisReflectogramMismatchEventId
							+ " (" + thisReflectogramMismatchEvent.getCreated() + ')'
							+ " < "
							+ thatReflectogramMismatchEventId
							+ " (" + thatReflectogramMismatchEvent.getCreated() + ')';
				} catch (final ApplicationException ae) {
					Log.debugMessage(ae, SEVERE);
					assert thisReflectogramMismatchEventId.compareTo(thatReflectogramMismatchEventId) < 0 :
							thisReflectogramMismatchEventId
							+ " < "
							+ thatReflectogramMismatchEventId;
				}
				return -1;
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
			 * 2. "this" references "that".
			 */
			throw new UnsupportedOperationException();
		} else {
			/*-
			 * 3. "this" references some other event.
			 */
			throw new UnsupportedOperationException();
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
	 * @see LineMismatchEvent#setParentLineMismatchEvent(LineMismatchEvent)
	 */
	public final void setParentLineMismatchEvent(
			final LineMismatchEvent parentLineMismatchEvent) {
		this.setParentLineMismatchEventId(Identifier.possiblyVoid(parentLineMismatchEvent));
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
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(ORB)
	 */
	@Override
	public final IdlLineMismatchEvent getIdlTransferable(final ORB orb) {
		final IdlSpacialData spacialData = new IdlSpacialData();
		if (this.isAffectedPathElementSpacious()) {
			spacialData.physicalDistancePair(
					IdlAffectedPathElementSpacious._TRUE,
					new IdlPhysicalDistancePair(
							this.getPhysicalDistanceToStart(),
							this.getPhysicalDistanceToEnd()));
		} else {
			spacialData._default(IdlAffectedPathElementSpacious._FALSE);
		}

		return IdlLineMismatchEventHelper.init(orb,
				this.id.getIdlTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(orb),
				this.modifierId.getIdlTransferable(orb),
				this.version.longValue(),
				this.getAffectedPathElementId().getIdlTransferable(orb),
				spacialData,
				this.getMismatchOpticalDistance(),
				this.getMismatchPhysicalDistance(),
				this.getPlainTextMessage(),
				this.getRichTextMessage(),
				this.getReflectogramMismatchEventId().getIdlTransferable(orb));
	}

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
	protected Set<Identifiable> getReverseDependencies(boolean usePool)
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
}
