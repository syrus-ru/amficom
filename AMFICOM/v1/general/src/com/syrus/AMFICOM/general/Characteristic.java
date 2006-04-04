/*-
 * $Id: Characteristic.java,v 1.92.2.1 2006/04/04 08:49:10 arseniy Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlCharacteristic;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicHelper;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * @version $Revision: 1.92.2.1 $, $Date: 2006/04/04 08:49:10 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class Characteristic extends AbstractCloneableStorableObject
		implements TypedObject<CharacteristicType>,
		ReverseDependencyContainer,
		XmlTransferableObject<XmlCharacteristic>,
		IdlTransferableObjectExt<IdlCharacteristic> {
	private static final long serialVersionUID = -2746555753961778403L;

	/**
	 * Can&apos;t be {@code null}.
	 * 
	 * @serial include
	 */
	private CharacteristicType type;

	/**
	 * Can be neither {@code null} nor empty. Minimum length for {@code name} is 1 character.
	 * Maximum length for {@code name} is 128 characters.
	 *
	 * @serial include
	 */
	private String name;

	/**
	 * Can&apos;t be {@code null}; instead, for an empty {@code description}, empty string
	 * should be supplied. Maximum length for {@code description} is 256 characters.
	 *
	 * @serial include
	 */
	private String description;

	/**
	 * Can&apos;t be {@code null}; instead, for an empty {@code value}, empty string should be
	 * supplied. Maximum length for {@code value} is 256 characters.
	 *
	 * @serial include
	 */
	private String value;

	/**
	 * Can be neither {@code null} nor {@link Identifier#VOID_IDENTIFIER void}.
	 *
	 * @serial include
	 */
	private Identifier parentCharacterizableId;

	/**
	 * @serial include
	 */
	private boolean editable;

	/**
	 * @serial include
	 */
	private boolean visible;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param ct
	 * @throws CreateObjectException
	 */
	public Characteristic(final IdlCharacteristic ct) throws CreateObjectException {
		try {
			this.fromIdlTransferable(ct);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Characteristic(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final CharacteristicType type,
			final String name,
			final String description,
			final String value,
			final Identifier characterizableId,
			final boolean editable,
			final boolean visible) {
		super(id,
				new Date(),
				new Date(),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.value = value;
		this.parentCharacterizableId = characterizableId;

		this.editable = editable;
		this.visible = visible;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private Characteristic(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, CHARACTERISTIC_CODE, created, creatorId);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.isTypeValid()
				&& this.isNameValid()
				&& this.isDescriptionValid()
				&& this.isValueValid()
				&& this.isParentCaharacterizableIdValid();
	}

	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 * @param type
	 *        see {@link CharacteristicType}
	 * @param name
	 * @param description
	 * @param value
	 * @param parentCharacterizable
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = { "final boolean usePool" })
	public static Characteristic createInstance(final Identifier creatorId,
			final CharacteristicType type,
			final String name,
			final String description,
			final String value,
			final Characterizable parentCharacterizable,
			final boolean editable,
			final boolean visible) throws CreateObjectException {
		final boolean usePool = false;

		final Identifier parentCharacterizableId = Identifier.possiblyVoid(parentCharacterizable);

		checkTypeValid(type);
		checkNameValid(name);
		checkDescriptionValid(description);
		checkValueValid(value);
		checkParentCaharacterizableIdValid(parentCharacterizableId);

		try {
			final Characteristic characteristic = new Characteristic(IdentifierPool.getGeneratedIdentifier(CHARACTERISTIC_CODE),
					creatorId,
					INITIAL_VERSION,
					type,
					name,
					description,
					value,
					parentCharacterizableId,
					editable,
					visible);
			if (parentCharacterizable != null) {
				parentCharacterizable.getCharacteristicContainerWrappee().addToCache(characteristic, usePool);
			}

			assert characteristic.isValid() : OBJECT_STATE_ILLEGAL;

			characteristic.markAsChanged();
			return characteristic;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("Characteristic.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlCharacteristic
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static Characteristic createInstance(final Identifier creatorId,
			final XmlCharacteristic xmlCharacteristic,
			final String importType) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlCharacteristic.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			Characteristic characteristic;
			if (id.isVoid()) {
				characteristic = new Characteristic(xmlId,
						importType,
						created,
						creatorId);
			} else {
				characteristic = StorableObjectPool.getStorableObject(id, true);
				if (characteristic == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					characteristic = new Characteristic(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			characteristic.fromXmlTransferable(xmlCharacteristic, importType);
			assert characteristic.isValid() : OBJECT_BADLY_INITIALIZED;
			characteristic.markAsChanged();
			return characteristic;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void fromIdlTransferable(final IdlCharacteristic ct)
	throws IdlConversionException {
		try {
			super.fromIdlTransferable(ct);

			this.type = StorableObjectPool.getStorableObject(new Identifier(ct._typeId), true);
			this.name = ct.name;
			this.description = ct.description;
			this.value = ct.value;
			this.parentCharacterizableId = new Identifier(ct.characterizableId);
			this.editable = ct.editable;
			this.visible = ct.visible;

			assert this.isValid() : OBJECT_STATE_ILLEGAL;
		} catch (final ApplicationException ae) {
			throw new IdlConversionException(ae);
		}
	}

	/**
	 * @param characteristic
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlCharacteristic characteristic, final String importType)
			throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(characteristic, CHARACTERISTIC_CODE, importType, PRE_IMPORT);

			this.type = StorableObjectPool.getStorableObject(
					Identifier.fromXmlTransferable(
							characteristic.getTypeId(),
							importType,
							MODE_THROW_IF_ABSENT),
					true);
			this.name = characteristic.getName();
			this.description = characteristic.isSetDescription()
					? characteristic.getDescription()
					: "";
			this.value = characteristic.isSetValue()
					? characteristic.getValue()
					: "";
			this.editable = characteristic.getEditable();
			this.visible = characteristic.getVisible();
			this.parentCharacterizableId = Identifier.fromXmlTransferable(
					characteristic.getParentCharacterizableId(),
					importType,
					MODE_THROW_IF_ABSENT);

			XmlComplementorRegistry.complementStorableObject(characteristic, CHARACTERISTIC_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlCharacteristic getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
		
		return IdlCharacteristicHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.type.getId().getIdlTransferable(),
				this.name,
				this.description,
				(this.value == null) ? "" : this.value,
				this.parentCharacterizableId.getIdlTransferable(),
				this.editable,
				this.visible);
	}

	/**
	 * @param characteristic
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlCharacteristic characteristic,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		try {
			this.id.getXmlTransferable(characteristic.addNewId(), importType);
			this.type.getId().getXmlTransferable(characteristic.addNewTypeId(), importType);
			characteristic.setName(this.name);

			if (characteristic.isSetDescription()) {
				characteristic.unsetDescription();
			}
			if (this.description.length() != 0) {
				characteristic.setDescription(this.description);
			}

			if (characteristic.isSetValue()) {
				characteristic.unsetValue();
			}
			if (this.value.length() != 0) {
				characteristic.setValue(this.value);
			}

			characteristic.setEditable(this.editable);
			characteristic.setVisible(this.visible);
			this.parentCharacterizableId.getXmlTransferable(characteristic.addNewParentCharacterizableId(), importType);

			XmlComplementorRegistry.complementStorableObject(characteristic, CHARACTERISTIC_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	public boolean isEditable() {
		return this.editable;
	}

	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setEditable0(final boolean editable) {
		this.editable = editable;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setVisible0(final boolean visible) {
		this.visible = visible;
	}

	public void setEditable(final boolean editable) {
		this.setEditable0(editable);
		super.markAsChanged();
	}

	public void setVisible(final boolean visible) {
		this.setVisible0(visible);
		super.markAsChanged();
	}

	public Identifier getTypeId() {
		return this.getType().getId();
	}

	public CharacteristicType getType() {
		assert this.isTypeValid();
		return this.type;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setType0(final CharacteristicType type) {
		checkTypeValid(type);
		this.type = type;
	}

	public void setType(final CharacteristicType type) {
		this.setType0(type);
		super.markAsChanged();
	}

	public String getName() {
		assert this.isNameValid();
		return this.name;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setName0(final String name) {
		checkNameValid(name);
		this.name = name;
	}

	protected void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}

	public String getDescription() {
		assert this.isDescriptionValid();
		return this.description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDescription0(final String description) {
		checkDescriptionValid(description);
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}

	/**
	 * This method returns the data this characteristic holds. It's in no
	 * way connected with {@link StorableObject#getValue(String)}. 
	 */
	public String getValue() {
		assert this.isValueValid();
		return this.value;
	}

	private void setValue0(final String value) {
		checkValueValid(value);
		this.value = value;
	}

	public void setValue(final String value) {
		if (this.value.intern() != value.intern()) {
			this.setValue0(value);
			this.markAsChanged();
		}
	}

	/**
	 * @todo add check whether parentCharacterizableId is non-void.
	 */
	public Identifier getParentCharacterizableId() {
		assert this.isParentCaharacterizableIdValid();

		return this.parentCharacterizableId;
	}

	/**
	 * A wrapper around {@link #getParentCharacterizableId()}.
	 *
	 * @throws ApplicationException
	 * @todo add check whether parentCharacterizable is non-null.
	 */
	public Characterizable getParentCharacterizable() throws ApplicationException {
		final StorableObject storableObject = StorableObjectPool.getStorableObject(this.getParentCharacterizableId(), true);
		if (storableObject == null || storableObject instanceof Characterizable) {
			return (Characterizable) storableObject;
		}
		throw new ClassCastException();
	}

	/**
	 * A wrapper around {@link #setParentCharacterizable(Characterizable, boolean)}.
	 *
	 * @param parentCharacterizableId
	 * @param usePool
	 * @throws ApplicationException
	 * @bug current code permits orphan characteristics.
	 */
	public void setParentCharacterizableId(
			final Identifier parentCharacterizableId,
			final boolean usePool)
	throws ApplicationException {
		checkParentCaharacterizableIdValid(parentCharacterizableId);
		/*
		 * Further check for identifier validity (e. g.: major corresponds to some specific
		 * entity code) cannot be performed since multiple StorableObject descendants may
		 * implement Characterizable (in other words, we don't have a special
		 * CHARACTERIZABLE_CODE).
		 */

		if (this.parentCharacterizableId.equals(parentCharacterizableId)) {
			return;
		}

		final StorableObject storableObject = StorableObjectPool.getStorableObject(parentCharacterizableId, true);
		/*
		 * The situation when null StorableObject is returned is unusual, and must be
		 * handled somehow. However, currently it is not.
		 */
		if (storableObject == null || storableObject instanceof Characterizable) {
			this.setParentCharacterizable((Characterizable) storableObject, usePool);
		} else {
			throw new ClassCastException();
		}
	}

	/**
	 * @param parentCharacterizable
	 * @param usePool
	 * @throws ApplicationException
	 * @bug current code permits orphan characteristics.
	 */
	public void setParentCharacterizable(
			final Characterizable parentCharacterizable,
			final boolean usePool)
	throws ApplicationException {
		final Identifier newParentCharacterizableId = Identifier.possiblyVoid(parentCharacterizable);
		if (this.parentCharacterizableId.equals(newParentCharacterizableId)) {
			return;
		}

		final Characterizable oldParentCharacterizable = this.getParentCharacterizable();
		if (oldParentCharacterizable != null) {
			oldParentCharacterizable.getCharacteristicContainerWrappee().removeFromCache(this, usePool);
		}
		if (parentCharacterizable != null) {
			parentCharacterizable.getCharacteristicContainerWrappee().addToCache(this, usePool);
		}

		this.parentCharacterizableId = newParentCharacterizableId;
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final CharacteristicType type,
			final String name,
			final String description,
			final String value,
			final Identifier parentCharacterizableId,
			final boolean editable,
			final boolean visible) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.value = value;
		this.parentCharacterizableId = parentCharacterizableId;
		this.editable = editable;
		this.visible = visible;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.parentCharacterizableId);
		dependencies.add(this.type);
		return dependencies;
	}

	public Set<Identifiable> getReverseDependencies(final boolean usePool) {
		return Collections.<Identifiable> singleton(super.id);
	}

	/**
	 * @see Object#clone()
	 */
	@Override
	public Characteristic clone() throws CloneNotSupportedException {
		/*-
		 * Since this method is usually invoked when a parent
		 * of this characteristic clones itself,
		 * characterizableId is updated from within that code,
		 * and not here. 
		 */
		final Characteristic clone = (Characteristic) super.clone();

		if (clone.clonedIdMap == null) {
			clone.clonedIdMap = new HashMap<Identifier, Identifier>();
		}

		clone.clonedIdMap.put(this.id, clone.id);

		return clone;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CharacteristicWrapper getWrapper() {
		return CharacteristicWrapper.getInstance();
	}

	/*-****************************************************************************************
	 * Contract.                                                                              *
	 ******************************************************************************************/

	/**
	 * Should be used in property modifiers (public API).
	 *
	 * @param type
	 * @throws RuntimeException if type doesn&apos;t meet its contract.
	 */
	private static void checkTypeValid(final CharacteristicType type) {
		if (type == null) {
			throw new NullPointerException("type is null");
		}
	}

	/**
	 * Self-consistency check that should be used in assertion chaining. This method itself
	 * always returns {@code true}, but throws an {@link AssertionError} (with a meaningful
	 * message), if (a) assertions are enabled and (b) {@link #type} doesn&apos;t meet its
	 * contract.
	 *
	 * @return true
	 * @throws AssertionError if assertions are enabled and {@link #type} doesn&apos;t meet its
	 *         contract.
	 */
	private boolean isTypeValid() {
		try {
			checkTypeValid(this.type);
		} catch (final RuntimeException re) {
			assert false : re.getMessage();
		}
		return true;
	}

	private static void checkNameValid(final String name) {
		if (name == null) {
			throw new NullPointerException("name is null");
		}
		final int length = name.length();
		if (length < 1 || length > 128) {
			throw new IllegalArgumentException("expected name length: 1..128; actual: " + length);
		}
	}

	private boolean isNameValid() {
		try {
			checkNameValid(this.name);
		} catch (final RuntimeException re) {
			assert false : re.getMessage();
		}
		return true;
	}

	private static void checkDescriptionValid(final String description) {
		if (description == null) {
			throw new NullPointerException("description is null");
		}
		final int length = description.length();
		if (length > 256) {
			throw new IllegalArgumentException("expected description length: 0..256; actual: " + length);
		}
	}

	private boolean isDescriptionValid() {
		try {
			checkDescriptionValid(this.description);
		} catch (final RuntimeException re) {
			assert false : re.getMessage(); 
		}
		return true;
	}

	private static void checkValueValid(final String value) {
		if (value == null) {
			throw new NullPointerException("value is null");
		}
		final int length = value.length();
		if (length > 256) {
			throw new IllegalArgumentException("expected value length: 0..256; actual: " + length);
		}
	}

	private boolean isValueValid() {
		try {
			checkValueValid(this.value);
		} catch (final RuntimeException re) {
			assert false : re.getMessage(); 
		}
		return true;
	}

	/**
	 * Probably, this method should also check whether class of the {@code StorableObject}
	 * identified by {@code parentCharacterizableId} is really assignable from
	 * {@code Characterizable} interface. However, this can only be done via Reflection API, and
	 * is not currently implemented.
	 *
	 * @param parentCharacterizableId
	 */
	private static void checkParentCaharacterizableIdValid(final Identifier parentCharacterizableId) {
		if (parentCharacterizableId == null) {
			throw new NullPointerException("parentCharacterizableId is null");
		}
		if (parentCharacterizableId.isVoid()) {
			throw new IllegalArgumentException("parentCharacterizableId is void");
		}
	}

	private boolean isParentCaharacterizableIdValid() {
		try {
			checkParentCaharacterizableIdValid(this.parentCharacterizableId);
		} catch (final RuntimeException re) {
			assert false : re.getMessage();
		}
		return true;
	}
}
