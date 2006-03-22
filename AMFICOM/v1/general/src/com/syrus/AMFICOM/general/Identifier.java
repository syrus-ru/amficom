/*-
 * $Id: Identifier.java,v 1.93.2.4 2006/03/22 08:21:42 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_GENERATE_NEW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * <code>Identifier</code>s, alike {@link String}s, are immutable. Hence, when
 * one is cloning a <code>StorableObject</code>, there&apos;s no need to clone
 * its respective <code>creatorId</code> and <code>modifierId</code>. But
 * there&apos;s a particular task of <code>id</code> handling.
 *
 * @version $Revision: 1.93.2.4 $, $Date: 2006/03/22 08:21:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class Identifier implements Comparable<Identifier>,
		IdlTransferableObject<IdlIdentifier>, Identifiable, Serializable {
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

	private final short major;
	private final long minor;
	
	private transient int hashCode; 

	private transient long identifierCode;
	private transient String identifierString;

	/*
	 * TODO valueOf method
	 */
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	public IdlIdentifier getIdlTransferable(@SuppressWarnings("unused") final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlIdentifier getIdlTransferable() {
		return new IdlIdentifier(this.getIdentifierCode());
	}

	public void getXmlTransferable(final XmlIdentifier xmlId, final String importType) {
		assert !this.isVoid() : NON_VOID_EXPECTED;

		LocalXmlIdentifierPool.prefetch(importType);

		final String xmlStringValue; //  = xmlId.getStringValue()
		if (LocalXmlIdentifierPool.contains(this, importType)) {
			xmlStringValue = LocalXmlIdentifierPool.get(this, importType);
			xmlId.setStringValue(xmlStringValue);
		} else {
			xmlStringValue = this.getIdentifierString();
			xmlId.setStringValue(xmlStringValue);
			assert !LocalXmlIdentifierPool.contains(xmlStringValue, importType);
			LocalXmlIdentifierPool.put(this, xmlStringValue, importType);
		}
		
		assert LocalXmlIdentifierPool.contains(xmlStringValue, importType);
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

	public static Identifier valueOf(final long identifierCode) {
		return new Identifier(identifierCode);
	}

	public static Identifier valueOf(final IdlIdentifier id) {
		return new Identifier(id);
	}

	/**
	 * Create <code>Set</code> of <code>String</code> representations of identifiers.
	 * NOTE: Method is not synchronized on argument.
	 * @param identifiables
	 * @return A newly created <code>Set</code> of <code>String</code> representations of identifiers.
	 */
	public static Set<String> createStrings(final Collection<? extends Identifiable> identifiables) {
		assert identifiables != null : NON_NULL_EXPECTED;

		final Set<String> idStrings = new HashSet<String>(identifiables.size());
		for (final Identifiable identifiable : identifiables) {
			idStrings.add(identifiable.getId().toString());
		}
		return idStrings;
	}

	/**
	 * Create string representation of set of identifiers.
	 * NOTE: Method is not synchronized on argument.
	 * @param identifiables
	 * @return String representation of set of identifiers.
	 */
	public static String toString(final Collection<? extends Identifiable> identifiables) {
		assert identifiables != null : NON_NULL_EXPECTED;
		return createStrings(identifiables).toString();
	}

	/**
	 * Create array if {@link IdlIdentifier} obtained from given <code>Set</code> of {@link Identifiable}
	 * NOTE: Method is not synchronized on argument.
	 * @param identifiables <code>Collection&lt;Identifiable&gt;</code>
	 * @return a newly created <code>IdlIdentifier[]</code> with
	 *         elements ordered in the same way as returned by the iterator.
	 * @see #fromTransferables(IdlIdentifier[])
	 */
	public static IdlIdentifier[] createTransferables(final Collection<? extends Identifiable> identifiables) {
		assert identifiables != null : NON_NULL_EXPECTED;

		final IdlIdentifier[] ids = new IdlIdentifier[identifiables.size()];
		int i = 0;
		for (final Identifiable identifiable : identifiables) {
			ids[i++] = identifiable.getId().getIdlTransferable();
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
		for (int i = 0; i < length; i++) {
			ids[i] = identifiables[i].getId().getIdlTransferable();
		}
		return ids;
	}

	/**
	 * Creates new <code>Set</code> of {@link Identifier} from the given <code>Set</code> of {@link Identifiable}.
	 * NOTE: Method is not synchronized on argument.
	 * @param identifiables
	 * @return <code>Set</code> (modifiable) of {@link Identifier}
	 */
	public static Set<Identifier> createIdentifiers(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null : NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = new HashSet<Identifier>(identifiables.size());
		for (final Identifiable identifiable : identifiables) {
			identifiers.add(identifiable.getId());
		}
		return identifiers;
	}

	public static <T extends Identifiable> Map<Identifier, T> createIdsMap(final Set<T> identifiables) {
		assert identifiables != null : NON_NULL_EXPECTED;

		final Map<Identifier, T> idsMap = new HashMap<Identifier, T>();
		for (final T identifiable : identifiables) {
			idsMap.put(identifiable.getId(), identifiable);
		}
		return idsMap;
	}

	/**
	 * Creates <code>Map<Short entityCode, Set<Identifier> ids></code>.
	 * NOTE: Method is not synchronized on argument.
	 * @param identifiables
	 */
	public static Map<Short, Set<Identifier>> createEntityIdsMap(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null : NON_NULL_EXPECTED;

		final Map<Short, Set<Identifier>> entityIdsMap = new HashMap<Short, Set<Identifier>>();
		for (final Identifiable identifiable : identifiables) {
			final Identifier id = identifiable.getId();
			final Short entityKey = Short.valueOf(id.getMajor());
			Set<Identifier> entityIds = entityIdsMap.get(entityKey);
			if (entityIds == null) {
				entityIds = new HashSet<Identifier>();
				entityIdsMap.put(entityKey, entityIds);
			}
			entityIds.add(id);
		}
		return entityIdsMap;
	}

	/**
	 * Creates new <code>Set</code>of {@link Identifiable}, containing values from both supplied sets of identifiables
	 * NOTE: Method is not synchronized on arguments.
	 * @param identifiables1
	 * @param identifiables2
	 * @return Set of identifiables
	 */
	public static Set<Identifiable> createSumIdentifiables(final Set<? extends Identifiable> identifiables1,
			final Set<? extends Identifiable> identifiables2) {
		assert identifiables1 != null : NON_NULL_EXPECTED;
		assert identifiables2 != null : NON_NULL_EXPECTED;

		final Set<Identifiable> identifiables = new HashSet<Identifiable>(identifiables1.size() + identifiables2.size());
		identifiables.addAll(identifiables1);
		identifiables.addAll(identifiables2);
		return identifiables;
	}

	/**
	 * Creates new <code>Set</code>of {@link Identifier}, containing values from both supplied sets of identifiables
	 * NOTE: Method is not synchronized on arguments.
	 * @param identifiables1
	 * @param identifiables2
	 * @return Set of identifiers
	 */
	public static Set<Identifier> createSumIdentifiers(final Set<? extends Identifiable> identifiables1,
			final Set<? extends Identifiable> identifiables2) {
		assert identifiables1 != null : NON_NULL_EXPECTED;
		assert identifiables2 != null : NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = new HashSet<Identifier>(identifiables1.size() + identifiables2.size());
		for (final Identifiable identifiable : identifiables1) {
			identifiers.add(identifiable.getId());
		}
		for (final Identifiable identifiable : identifiables2) {
			identifiers.add(identifiable.getId());
		}
		return identifiers;
	}

	/**
	 * Creates new set of identifiers, containing values from set
	 * <code>minuendi</code> with exception to those, containing in set
	 * <code>subtrahendi</code>
	 * NOTE: Method is not synchronized on arguments. Need synchronization on <code>subtrahendi</code>.
	 * 
	 * @param minuendi
	 *        see <a href = "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=minuend">Merriam-Webster Dictionary</a>.
	 * @param subtrahendi
	 *        <a href = "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=subtrahend">ditto</a>.
	 * @return Set of identifiers
	 */
	public static Set<Identifier> createSubtractionIdentifiers(final Set<? extends Identifiable> minuendi,
			final Set<? extends Identifiable> subtrahendi) {
		assert minuendi != null : NON_NULL_EXPECTED;
		assert subtrahendi != null : NON_NULL_EXPECTED;

		final Set<Identifier> identifiers = createIdentifiers(minuendi);

		for (final Identifiable subtrahendum : subtrahendi) {
			identifiers.remove(subtrahendum.getId());
		}
		return identifiers;
	}

	/**
	 * Adds to set of identifiers <code>identifiers</code>
	 * identifiers from set of identifiables <code>identifiables</code>.
	 * (I. e., parameter <code>identifiers</code> is passed as &quot;inout&quot; argument.)
	 * NOTE: Method is not synchronized on arguments. Need synchronization on <code>identifiables</code>.
	 * @param identifiers
	 * @param identifiables
	 */
	public static void addToIdentifiers(final Set<Identifier> identifiers, final Set<? extends Identifiable> identifiables) {
		assert identifiers != null : NON_NULL_EXPECTED;
		assert identifiables != null : NON_NULL_EXPECTED;

		for (final Identifiable identifiable : identifiables) {
			identifiers.add(identifiable.getId());
		}
	}

	/**
	 * Removes from set of identifiers <code>minuendi</code> those, which
	 * contained in set of identifiables <code>subtrahendi</code>. (I. e.,
	 * parameter <code>minuendi</code> is passed as &quot;inout&quot;
	 * argument.)
	 * NOTE: Method is not synchronized on arguments. Need synchronization on <code>subtrahendi</code>.
	 * 
	 * @param minuendi
	 *        see <a href =
	 *        "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=minuend">Merriam-Webster
	 *        Dictionary</a>.
	 * @param subtrahendi
	 *        <a href =
	 *        "http://www.m-w.com/cgi-bin/dictionary?book=Dictionary&va=subtrahend">ditto</a>.
	 */
	public static void subtractFromIdentifiers(final Set<Identifier> minuendi, final Set<? extends Identifiable> subtrahendi) {
		assert minuendi != null : NON_NULL_EXPECTED;
		assert subtrahendi != null : NON_NULL_EXPECTED;

		for (final Identifiable subtrahendum : subtrahendi) {
			minuendi.remove(subtrahendum.getId());
		}
	}

	/**
	 * @param transferables
	 *        <code>IdlIdentifier[]</code>
	 * @return a newly created <code>Set&lt;Identifier&gt;</code>.
	 * @see #createTransferables(Collection)
	 */
	public static Set<Identifier> fromTransferables(final IdlIdentifier[] transferables) {
		final Set<Identifier> ids = new HashSet<Identifier>(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			ids.add(valueOf(transferables[i]));
		}
		return ids;
	}

	/**
	 * @param xmlId
	 * @param importType
	 * @param entityCode
	 * @throws IdentifierGenerationException
	 */
	static Identifier fromXmlTransferable(final XmlIdentifier xmlId,
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
			if (xmlConversionMode == MODE_GENERATE_NEW_IF_ABSENT) {
				throw new IllegalArgumentException(
						"MODE_GENERATE_NEW_IF_ABSENT is used internally and cannot be specified here");
			}
			return fromXmlTransferable(xmlId, UPDIKE_CODE, importType, xmlConversionMode);
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
		LocalXmlIdentifierPool.prefetch(importType);

		final String xmlStringValue = xmlId.getStringValue();
		if (LocalXmlIdentifierPool.contains(xmlStringValue, importType)) {
			final Identifier id = LocalXmlIdentifierPool.get(xmlStringValue, importType);

			assert !id.isVoid() : NON_VOID_EXPECTED;
			assert entityCode == UPDIKE_CODE || entityCode == id.getMajor();

			return id;
		}

		switch (xmlConversionMode) {
		case MODE_GENERATE_NEW_IF_ABSENT:
			final Identifier id = IdentifierPool.getGeneratedIdentifier(entityCode);

			assert !LocalXmlIdentifierPool.contains(id, importType);

			LocalXmlIdentifierPool.put(id, xmlStringValue, importType);

			return id;
		case MODE_RETURN_VOID_IF_ABSENT:
			return VOID_IDENTIFIER;
		case MODE_THROW_IF_ABSENT:
		default:
			throw new ObjectNotFoundException("Mapping not found");
		}
	}

	/**
	 * <p>This method is of no use for <code>Identifier</code>s, but the
	 * formal parameter is still of type <code>Identifiable</code>, since
	 * we want to be able to pass both {@code StorableObject}s
	 * and {@code Characterizable}s (but, of course, not {@code Identifier}s).</p>
	 *
	 * <p><em>Shouldn&apos;t be invoked by clients &amp; mousebusters as
	 * they should never mess with <code>Identifier</code>s directly.</em></p>
	 *
	 * @param identifiable an <code>Identifiable</code> whose
	 *        identifier is to be determined; can be <code>null</code>.
	 * @return an <code>Identifier</code> of the object supplied, or
	 *         {@link #VOID_IDENTIFIER} if the object is <code>null</code>.
	 */
	public static Identifier possiblyVoid(final Identifiable identifiable) {
		return identifiable == null ? VOID_IDENTIFIER : identifiable.getId();
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
