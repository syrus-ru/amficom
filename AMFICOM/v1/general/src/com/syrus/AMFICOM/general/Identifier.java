/*-
 * $Id: Identifier.java,v 1.74 2005/09/20 10:42:00 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_GENERATE_NEW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.UNKNOWN_CODE;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;

/**
 * <code>Identifier</code>s, alike {@link String}s, are immutable. Hence, when
 * one is cloning a <code>StorableObject</code>, there&apos;s no need to clone
 * its respective <code>creatorId</code> and <code>modifierId</code>. But
 * there&apos;s a particular task of <code>id</code> handling.
 *
 * @version $Revision: 1.74 $, $Date: 2005/09/20 10:42:00 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class Identifier implements Comparable<Identifier>, TransferableObject, Serializable, Identifiable {
	private static final long serialVersionUID = 1721559813677093072L;

	public static final char SEPARATOR = '_';
	private static final int MINOR_SIZE_BITS = 48;
	private static final long MAJOR_MASK = 0xffffL << MINOR_SIZE_BITS;
	private static final long MINOR_MASK = ~MAJOR_MASK;

	public static final Identifier VOID_IDENTIFIER = new Identifier(ObjectEntities.UPDIKE_CODE, 0);

	public static enum XmlConversionMode {
		MODE_GENERATE_NEW_IF_ABSENT,
		MODE_RETURN_VOID_IF_ABSENT,
		MODE_THROW_IF_ABSENT
	}

	private short major;
	private long minor;
	
	private transient int hashCode; 

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
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(final Identifier anotherIdentifier) {
		final short thatMajor = anotherIdentifier.getMajor();
		final long thatMinor = anotherIdentifier.getMinor();
		return this.major == thatMajor
			? this.minor <= thatMinor ? this.minor < thatMinor ? -1 : 0 : 1
			: this.major - thatMajor;
	}

	/**
	 * @param obj
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Identifiable) {
			final Identifier that = ((Identifiable) obj).getId();
			return this == that ||
				that.major == this.major && that.minor == this.minor;
		}
		return false;
	}

	public long getIdentifierCode() {
		if (this.identifierCode == 0) {
			this.identifierCode = (((long) this.major) << MINOR_SIZE_BITS) | this.minor;
		}
		return this.identifierCode;
	}

	public String getIdentifierString() {
		if (this.identifierString == null) {
			this.identifierString = ObjectEntities.codeToString(this.major) + SEPARATOR + Long.toString(this.minor);
		}
		return this.identifierString;
	}

	public short getMajor() {
		return this.major;
	}

	public long getMinor() {
		return this.minor;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlIdentifier getTransferable(@SuppressWarnings("unused") final ORB orb) {
		return this.getTransferable();
	}

	public IdlIdentifier getTransferable() {
		return new IdlIdentifier(this.getIdentifierCode());
	}

	public void getXmlTransferable(final XmlIdentifier xmlId, final String importType) {
		assert !this.isVoid() : NON_VOID_EXPECTED;

		if (LocalXmlIdentifierPool.contains(this, importType)) {
			xmlId.setStringValue(LocalXmlIdentifierPool.get(this, importType).getStringValue());
		} else {
			xmlId.setStringValue(this.getIdentifierString());
			assert !LocalXmlIdentifierPool.contains(xmlId, importType);
			LocalXmlIdentifierPool.put(this, xmlId, importType);
		}

		assert LocalXmlIdentifierPool.contains(xmlId, importType);
	}

	@Override
	public int hashCode() {
		if (this.hashCode == 0) {
			// hashCode can be cached due to immutability 
			this.hashCode = 17;
			this.hashCode = 37 * this.hashCode + this.major;
			this.hashCode = 37 * this.hashCode + (int)(this.minor ^ (this.minor >>> 32));
		}
		return this.hashCode;
	}

	public String toHexString() {
		return Long.toHexString(this.getIdentifierCode());
	}

	@Override
	public String toString() {
		return this.getIdentifierString();
	}

	public static Set<String> createStrings(final Collection<? extends Identifiable> identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;

		final Set<String> idStrings = new HashSet<String>(identifiables.size());
		synchronized (identifiables) {
			for (final Identifiable identifiable : identifiables) {
				idStrings.add(identifiable.getId().toString());
			}
		}
		return idStrings;
	}

	/**
	 * @param identifiables <code>Collection&lt;Identifiable&gt;</code>
	 * @return a newly created <code>IdlIdentifier[]</code> with
	 *         elements ordered in the same way as returned by the iterator.
	 * @see #fromTransferables(IdlIdentifier[])
	 */
	public static IdlIdentifier[] createTransferables(final Collection<? extends Identifiable> identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;

		final IdlIdentifier[] ids = new IdlIdentifier[identifiables.size()];
		int i = 0;
		synchronized (identifiables) {
			for (final Identifiable identifiable : identifiables) {
				ids[i++] = identifiable.getId().getTransferable();
			}
		}
		return ids;
	}

	/**
	 * @param identifiables
	 * @see #createTransferables(Collection)
	 */
	public static IdlIdentifier[] createTransferables(final Identifiable[] identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;
		
		final int length = identifiables.length;
		final IdlIdentifier[] ids = new IdlIdentifier[length];
		for (int i = 0; i < length; i++)
			ids[i] = identifiables[i].getId().getTransferable();
		return ids;
	}

	/**
	 * Creates new set of identifiers from the given set of identifiables
	 * @param identifiables
	 * @return Set of identifiers
	 */
	public static Set<Identifier> createIdentifiers(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = new HashSet<Identifier>(identifiables.size());
		synchronized (identifiables) {
			for (final Identifiable identifiable : identifiables) {
				identifiers.add(identifiable.getId());
			}
		}
		return identifiers;
	}

	/**
	 * Creates new set of identifiers, containing values from both supplied sets of identifiables
	 * @param identifiables1
	 * @param identifiables2
	 * @return Set of identifiers
	 */
	public static Set<Identifier> createSumIdentifiers(final Set<? extends Identifiable> identifiables1,
			final Set<? extends Identifiable> identifiables2) {
		assert identifiables1 != null : NON_NULL_EXPECTED;
		assert identifiables2 != null : NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = new HashSet<Identifier>(identifiables1.size() + identifiables2.size());
		synchronized (identifiables1) {
			for (final Identifiable identifiable : identifiables1) {
				identifiers.add(identifiable.getId());
			}
		}
		synchronized (identifiables2) {
			for (final Identifiable identifiable : identifiables2) {
				identifiers.add(identifiable.getId());
			}
		}
		return identifiers;
	}

	/**
	 * Creates new set of identifiers, containing values from set <code>identifiables1</code>
	 * with exception to those, containing in set <code>identifiables2</code>
	 *
	 * @param minuendi see <a href = "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=minuend">Merriam-Webster Dictionary</a>.
	 * @param subtrahendi <a href = "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=subtrahend">ditto</a>.
	 * @return Set of identifiers
	 */
	public static Set<Identifier> createSubtractionIdentifiers(final Set<? extends Identifiable> minuendi,
			final Set<? extends Identifiable> subtrahendi) {
		assert minuendi != null : NON_NULL_EXPECTED;
		assert subtrahendi != null : NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = createIdentifiers(minuendi);
		synchronized (subtrahendi) {
			for (final Identifiable subtrahendum : subtrahendi) {
				identifiers.remove(subtrahendum.getId());
			}
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
	public static void addToIdentifiers(final Set<Identifier> identifiers, final Set<? extends Identifiable> identifiables) {
		assert identifiers != null : NON_NULL_EXPECTED;
		assert identifiables != null : NON_NULL_EXPECTED;

		synchronized (identifiables) {
			for (final Identifiable identifiable : identifiables) {
				identifiers.add(identifiable.getId());
			}
		}
	}

	/**
	 * Removes from set of identifiers <code>identifiers</code> those,
	 * which contained in set of identifiables <code>identifiables</code>.
	 * (I. e., parameter <code>identifiers</code> is passed as &quot;inout&quot; argument.)
	 *
	 * @param minuendi see <a href = "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=minuend">Merriam-Webster Dictionary</a>.
	 * @param subtrahendi <a href = "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=subtrahend">ditto</a>.
	 */
	public static void subtractFromIdentifiers(final Set<Identifier> minuendi,
			final Set<? extends Identifiable> subtrahendi) {
		assert minuendi != null : NON_NULL_EXPECTED;
		assert subtrahendi != null : NON_NULL_EXPECTED;

		synchronized (subtrahendi) {
			for (final Identifiable subtrahendum : subtrahendi) {
				minuendi.remove(subtrahendum.getId());
			}
		}
	}

	/**
	 * @param transferables <code>IdlIdentifier[]</code>
	 * @return a newly created <code>Set&lt;Identifier&gt;</code>.
	 * @see #createTransferables(Collection)
	 */
	public static Set<Identifier> fromTransferables(final IdlIdentifier[] transferables) {
		final Set<Identifier> ids = new HashSet<Identifier>(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			ids.add(new Identifier(transferables[i]));
		}
		return ids;
	}

	/**
	 * @param xmlId
	 * @param importType
	 * @param entityCode
	 * @throws IdentifierGenerationException
	 */
	public static Identifier fromXmlTransferable(final XmlIdentifier xmlId,
			final String importType,
			final short entityCode)
	throws IdentifierGenerationException {
		try {
			return fromXmlTransferable(xmlId, entityCode, importType, MODE_GENERATE_NEW_IF_ABSENT);
		} catch (final ObjectNotFoundException onfe) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	/**
	 * @param xmlId
	 * @param importType
	 * @param xmlConversionMode
	 * @throws ObjectNotFoundException
	 */
	public static Identifier fromXmlTransferable(final XmlIdentifier xmlId,
			final String importType,
			final XmlConversionMode xmlConversionMode)
	throws ObjectNotFoundException {
		try {
			assert xmlConversionMode != MODE_GENERATE_NEW_IF_ABSENT;
			return fromXmlTransferable(xmlId, UNKNOWN_CODE, importType, xmlConversionMode);
		} catch (final IdentifierGenerationException ige) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	/**
	 * @param xmlId
	 * @param entityCode
	 * @param importType
	 * @param xmlConversionMode
	 * @throws IdentifierGenerationException
	 * @throws ObjectNotFoundException
	 */
	private static Identifier fromXmlTransferable(final XmlIdentifier xmlId,
			final short entityCode,
			final String importType,
			final XmlConversionMode xmlConversionMode)
	throws IdentifierGenerationException, ObjectNotFoundException {
		if (LocalXmlIdentifierPool.contains(xmlId, importType)) {
			final Identifier id = LocalXmlIdentifierPool.get(xmlId, importType);

			assert !id.isVoid() : NON_VOID_EXPECTED;
			assert entityCode == UNKNOWN_CODE || entityCode == id.getMajor();

			return id;
		}

		switch (xmlConversionMode) {
		case MODE_GENERATE_NEW_IF_ABSENT:
			final Identifier id = IdentifierPool.getGeneratedIdentifier(entityCode);

			assert !LocalXmlIdentifierPool.contains(id, importType);

			LocalXmlIdentifierPool.put(id, xmlId, importType);

			return id;
		case MODE_RETURN_VOID_IF_ABSENT:
			return VOID_IDENTIFIER;
		case MODE_THROW_IF_ABSENT:
		default:
			throw new ObjectNotFoundException("Mapping not found");
		}
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
	public static Identifier possiblyVoid(final StorableObject storableObject) {
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
