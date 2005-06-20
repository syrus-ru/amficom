/*-
 * $Id: Identifier.java,v 1.46 2005/06/20 20:55:07 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * <code>Identifier</code>s, alike {@link String}s, are immutable. Hence, when
 * one is cloning a <code>StorableObject</code>, there&apos;s no need to clone
 * its respective <code>creatorId</code> and <code>modifierId</code>. But
 * there&apos;s a particular task of <code>id</code> handling.
 *
 * @version $Revision: 1.46 $, $Date: 2005/06/20 20:55:07 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class Identifier implements Comparable, TransferableObject, Serializable, Identifiable {
	private static final long serialVersionUID = 1721559813677093072L;

	public static final char SEPARATOR = '_';
	private static final int MINOR_SIZE_BITS = 48;
	private static final long MAJOR_MASK = 0xffffL << MINOR_SIZE_BITS;
	private static final long MINOR_MASK = ~MAJOR_MASK;

	public static final Identifier VOID_IDENTIFIER = new Identifier(ObjectEntities.UPDIKE_CODE, 0);

	private short major;
	private long minor;

	private transient long identifierCode;
	private transient String identifierString;

	public Identifier(final IdlIdentifier idlId) {
		this(idlId.identifierCode);
	}

	public Identifier(final String identifierString) {
		this(ObjectEntities.stringToCode(identifierString.substring(0, identifierString.indexOf(SEPARATOR))),
				Long.parseLong(identifierString.substring(identifierString.indexOf(SEPARATOR) + 1)));
	}

	Identifier(final long identifierCode) {
		this((short) ((identifierCode & MAJOR_MASK) >> MINOR_SIZE_BITS),
				identifierCode & MINOR_MASK);
	}

	/*	Only for IdentifierGenerator	*/
	Identifier(final short major, final long minor) {
		assert (minor & MAJOR_MASK) == 0 : OBJECT_STATE_ILLEGAL;

		this.major = major;
		this.minor = minor;
	}

	/**
	 * @param anotherIdentifier
	 */
	public int compareTo(final Identifier anotherIdentifier) {
		final short thatMajor = anotherIdentifier.getMajor();
		final long thatMinor = anotherIdentifier.getMinor();
		return this.major == thatMajor
			? this.minor <= thatMinor ? this.minor < thatMinor ? -1 : 0 : 1
			: this.major - thatMajor;
	}

	/**
	 * @param o
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(final Object o) {
		return compareTo((Identifier) o);
	}

	@Override
	public boolean equals(final Object obj) {
		boolean ret = false;
		if (obj instanceof Identifier) {
			Identifier that = (Identifier) obj;
			ret = ((that.major == this.major) && (that.minor == this.minor));
		}
		return ret;
	}

	public long getIdentifierCode() {
		if (this.identifierCode == 0)
			this.identifierCode = (((long) this.major) << MINOR_SIZE_BITS) | this.minor;
		return this.identifierCode;
	}

	public String getIdentifierString() {
		if (this.identifierString == null)
			this.identifierString = ObjectEntities.codeToString(this.major) + SEPARATOR + Long.toString(this.minor);
		return this.identifierString;
	}

	public short getMajor() {
		return this.major;
	}

	public long getMinor() {
		return this.minor;
	}

	public IdlIdentifier getTransferable() {
		return new IdlIdentifier(this.getIdentifierCode());
	}

	@Override
	public int hashCode() {
		int ret = 17;
		ret = 37 * ret + this.major;
		ret = 37 * ret + (int)(this.minor ^ (this.minor >>> 32));
		return ret;
	}

	public String toHexString() {
		return Long.toHexString(this.getIdentifierCode());
	}

	@Override
	public String toString() {
		return this.getIdentifierString();
	}

	public static final Set<String> createStrings(final Collection<? extends Identifiable> identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;

		Set<String> idStrings = new HashSet<String>(identifiables.size());
		for (final Iterator<? extends Identifiable> identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();)
			idStrings.add(identifiableIterator.next().getId().toString());
		return idStrings;
	}

	/**
	 * @param identifiables <code>Collection&lt;Identifiable&gt;</code>
	 * @return a newly created <code>IdlIdentifier[]</code> with
	 *         elements ordered in the same way as returned by the iterator.
	 * @see #fromTransferables(IdlIdentifier[])
	 */
	public static final IdlIdentifier[] createTransferables(final Collection<? extends Identifiable> identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;

		int i = 0;
		final IdlIdentifier ids[] = new IdlIdentifier[identifiables.size()];
		for (final Iterator<? extends Identifiable> identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext(); i++)
			ids[i] = identifiableIterator.next().getId().getTransferable();
		return ids;
	}

	/**
	 * @param identifiables
	 * @see #createTransferables(Collection)
	 */
	public static final IdlIdentifier[] createTransferables(final Identifiable[] identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;
		
		final int length = identifiables.length;
		final IdlIdentifier ids[] = new IdlIdentifier[length];
		for (int i = 0; i < length; i++)
			ids[i] = identifiables[i].getId().getTransferable();
		return ids;
	}

	/**
	 * Creates new set of identifiers from the given set of identifiables
	 * @param identifiables
	 * @return Set of identifiers
	 */
	public static final Set<Identifier> createIdentifiers(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = new HashSet<Identifier>(identifiables.size());
		for (Iterator<? extends Identifiable> it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = it.next();
			identifiers.add(identifiable.getId());
		}
		return identifiers;
	}

	/**
	 * Creates new set of identifiers, containing values from both supplied sets of identifiables
	 * @param identifiables1
	 * @param identifiables2
	 * @return Set of identifiers
	 */
	public static final Set<Identifier> createSumIdentifiers(final Set<? extends Identifiable> identifiables1, final Set<? extends Identifiable> identifiables2) {
		assert identifiables1 != null : NON_NULL_EXPECTED;
		assert identifiables2 != null : NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = new HashSet<Identifier>(identifiables1.size() + identifiables2.size());
		for (final Iterator<? extends Identifiable> it = identifiables1.iterator(); it.hasNext();) {
			final Identifiable identifiable = it.next();
			identifiers.add(identifiable.getId());
		}
		for (final Iterator<? extends Identifiable> it = identifiables2.iterator(); it.hasNext();) {
			final Identifiable identifiable = it.next();
			identifiers.add(identifiable.getId());
		}
		return identifiers;
	}

	/**
	 * Creates new set of identifiers, containing values from set <code>identifiables1</code>
	 * with exception to those, containing in set <code>identifiables2</code>
	 * @param identifiables1
	 * @param identifiables2
	 * @return Set of identifiers
	 */
	public static final Set<Identifier> createSubtractionIdentifiers(final Set<? extends Identifiable> identifiables1, final Set<? extends Identifiable> identifiables2) {
		assert identifiables1 != null : NON_NULL_EXPECTED;
		assert identifiables2 != null : NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = Identifier.createIdentifiers(identifiables1);
		for (final Iterator<? extends Identifiable> it = identifiables2.iterator(); it.hasNext();) {
			final Identifiable identifiable = it.next();
			identifiers.remove(identifiable.getId());
		}
		return identifiers;
	}

	/**
	 * Adds to set of identifiers <code>identifiers</code>
	 * identifiers from set of identifiables <code>identifiables</code>.
	 * (I. e., parameter <code>identifiers</code> is passed as &quot;inout&quot; argument.)
	 * @param identifiers
	 * @param identifiables
	 */
	public static final void addToIdentifiers(final Set<Identifier> identifiers, final Set<? extends Identifiable> identifiables) {
		assert identifiers != null : NON_NULL_EXPECTED;
		assert identifiables != null : NON_NULL_EXPECTED;

		for (final Iterator<? extends Identifiable> it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = it.next();
			identifiers.add(identifiable.getId());
		}
	}

	/**
	 * Removes from set of identifiers <code>identifiers</code> those,
	 * which contained in set of identifiables <code>identifiables</code>.
	 * (I. e., parameter <code>identifiers</code> is passed as &quot;inout&quot; argument.)
	 * @param identifiers
	 * @param identifiables
	 */
	public static final void subtractFromIdentifiers(final Set<Identifier> identifiers, final Set<? extends Identifiable> identifiables) {
		assert identifiers != null : NON_NULL_EXPECTED;
		assert identifiables != null : NON_NULL_EXPECTED;

		for (final Iterator<? extends Identifiable> it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = it.next();
			identifiers.remove(identifiable.getId());
		}
	}

	/**
	 * @param transferables <code>IdlIdentifier[]</code>
	 * @return a newly created <code>Set&lt;Identifier&gt;</code>.
	 * @see #createTransferables(Collection)
	 */
	public static final Set<Identifier> fromTransferables(final IdlIdentifier[] transferables) {
		final Set<Identifier> ids = new HashSet<Identifier>(transferables.length);
		for (int i = 0; i < transferables.length; i++)
			ids.add(new Identifier(transferables[i]));
		return ids;
	}

	/**
	 * <p>This method is of no use for <code>Identifier</code>s, so formal
	 * parameter of type <code>StorableObject</code> is ok.</p>
	 *
	 * <p><em>Shouldn&apos;t be invoked by clients &amp; mousebusters as
	 * they should never mess with <code>Identifier</code>s directly.</em></p>
	 *
	 * @param storableObject a <code>StorableObject</code> whose
	 *        identifier is to be determined; can be <code>null</code>.
	 * @return an <code>Identifier</code> of the object supplied, or
	 *         {@link #VOID_IDENTIFIER} if the object is <code>null</code>.
	 */
	public static final Identifier possiblyVoid(final StorableObject storableObject) {
		return storableObject == null ? VOID_IDENTIFIER : storableObject.getId();
	}

	/**
	 * @see Identifiable#getId()
	 */
	public final Identifier getId() {
		return this;
	}

	public final boolean isVoid() {
		return equals(VOID_IDENTIFIER);
	}
}
