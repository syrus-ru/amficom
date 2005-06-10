/*
 * $Id: Identifier.java,v 1.37 2005/06/10 11:02:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * <code>Identifier</code>s, alike {@link String}s, are immutable. Hence, when
 * one is cloning a <code>StorableObject</code>, there&apos;s no need to clone
 * its respective <code>creatorId</code> and <code>modifierId</code>. But
 * there&apos;s a particular task of <code>id</code> handling.
 *
 * @version $Revision: 1.37 $, $Date: 2005/06/10 11:02:49 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class Identifier implements Comparable, TransferableObject, Serializable, Identifiable {
	public static final char SEPARATOR = '_';

	public static final Identifier VOID_IDENTIFIER = new Identifier(ObjectEntities.UPDIKE_ENTITY_CODE, 0);

	private static final long serialVersionUID = 1721559813677093072L;

	private String identifierString;

	private short major;
	private long minor;

	public Identifier(Identifier_Transferable id_t) {
		this(id_t.identifier_string);
	}

	public Identifier(String identifierString) {
		this.major = ObjectEntities.stringToCode(identifierString.substring(0, identifierString.indexOf(SEPARATOR)));
		this.minor = Long.parseLong(identifierString.substring(identifierString.indexOf(SEPARATOR) + 1));
		this.identifierString = identifierString;
	}

	/*	Only for IdentifierGenerator	*/
	protected Identifier(short major, long minor) {
		this.major = major;
		this.minor = minor;
		this.identifierString = ObjectEntities.codeToString(this.major) + SEPARATOR + Long.toString(this.minor);
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

	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj instanceof Identifier){
			Identifier that = (Identifier) obj;
			ret = ((that.major == this.major) && (that.minor == this.minor));
		}
		return ret;
	}

	public String getIdentifierString() {
		return this.identifierString;
	}

	public short getMajor() {
		return this.major;
	}

	public long getMinor() {
		return this.minor;
	}

	public IDLEntity getTransferable() {
		return new Identifier_Transferable(this.identifierString);
	}

	public int hashCode() {
		int ret = 17;
		ret = 37 * ret + this.major;
		ret = 37 * ret + (int)(this.minor ^ (this.minor >>> 32));
		ret = 37 * ret + this.identifierString.hashCode();
		return ret;
	}

	public String toHexString() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return this.identifierString;
	}

	public static final Set createStrings(final Collection identifiables) {
		assert identifiables != null: ErrorMessages.NON_NULL_EXPECTED;

		Set idStrings = new HashSet(identifiables.size());
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();)
			idStrings.add(((Identifiable) identifiableIterator.next()).getId().toString());
		return idStrings;
	}

	/**
	 * @param identifiables <code>Collection&lt;Identifiable&gt;</code>
	 * @return a newly created <code>Identifier_Transferable[]</code> with
	 *         elements ordered in the same way as returned by the iterator.
	 * @see #fromTransferables(Identifier_Transferable[])
	 */
	public static final Identifier_Transferable[] createTransferables(final Collection identifiables) {
		assert identifiables != null: ErrorMessages.NON_NULL_EXPECTED;

		int i = 0;
		final Identifier_Transferable ids[] = new Identifier_Transferable[identifiables.size()];
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext(); i++)
			ids[i] = (Identifier_Transferable) ((Identifiable) identifiableIterator.next()).getId().getTransferable();
		return ids;
	}

	/**
	 * @param identifiables
	 * @see #createTransferables(Collection)
	 */
	public static final Identifier_Transferable[] createTransferables(final Identifiable identifiables[]) {
		assert identifiables != null: ErrorMessages.NON_NULL_EXPECTED;
		
		final int length = identifiables.length;
		final Identifier_Transferable ids[] = new Identifier_Transferable[length];
		for (int i = 0; i < length; i++)
			ids[i] = (Identifier_Transferable) identifiables[i].getId().getTransferable();
		return ids;
	}

	/**
	 * Creates new set of identifiers from the given set of identifiables
	 * @param identifiables
	 * @return Set of identifiers
	 */
	public static final Set createIdentifiers(final Set identifiables) {
		assert identifiables != null: ErrorMessages.NON_NULL_EXPECTED;

		final Set identifiers = new HashSet(identifiables.size());
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
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
	public static final Set createSumIdentifiers(final Set identifiables1, final Set identifiables2) {
		assert identifiables1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert identifiables2 != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set identifiers = new HashSet(identifiables1.size() + identifiables2.size());
		for (final Iterator it = identifiables1.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			identifiers.add(identifiable.getId());
		}
		for (final Iterator it = identifiables2.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			identifiers.add(identifiable.getId());
		}
		return identifiers;
	}

	/**
	 * Creates new set of identifiers, containing values from set <code>identifiables1</code>
	 * with exception to those, containing in set <code>identifiables2</code>
	 * @param identifiables1
	 * @param identifiables2
	 * @return
	 */
	public static final Set createSubstractionIdentifiers(final Set identifiables1, final Set identifiables2) {
		assert identifiables1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert identifiables2 != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set identifiers = Identifier.createIdentifiers(identifiables1);
		for (final Iterator it = identifiables2.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
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
	public static final void addToIdentifiers(final Set identifiers, final Set identifiables) {
		assert identifiers != null : ErrorMessages.NON_NULL_EXPECTED;
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		for (final Iterator it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
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
	public static final void substractFromIdentifiers(final Set identifiers, final Set identifiables) {
		assert identifiers != null : ErrorMessages.NON_NULL_EXPECTED;
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;

		for (final Iterator it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			identifiers.remove(identifiable.getId());
		}
	}

	/**
	 * @param transferables <code>Identifier_Transferable[]</code>
	 * @return a newly created <code>Set&lt;Identifier&gt;</code>.
	 * @see #createTransferables(Collection)
	 */
	public static final Set fromTransferables(final Identifier_Transferable[] transferables) {
		final Set set = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++)
			set.add(new Identifier(transferables[i]));
		return set;
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
