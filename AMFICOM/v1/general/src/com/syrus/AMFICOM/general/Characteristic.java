/*-
 * $Id: Characteristic.java,v 1.76 2005/10/28 10:08:34 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlCharacteristic;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicHelper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.76 $, $Date: 2005/10/28 10:08:34 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class Characteristic extends AbstractCloneableStorableObject<Characteristic>
		implements TypedObject<CharacteristicType>,
		ReverseDependencyContainer,
		XmlBeansTransferable<XmlCharacteristic> {
	private static final long serialVersionUID = -2746555753961778403L;

	private CharacteristicType type;
	private String name;
	private String description;
	private String value;
	private Identifier parentCharacterizableId;
	private boolean editable;
	private boolean visible;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param ct
	 * @throws CreateObjectException
	 */
	public Characteristic(final IdlCharacteristic ct) throws CreateObjectException {
		try {
			this.fromTransferable(ct);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
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
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
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

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.type != null
				|| this.name != null
				|| this.value != null
				|| this.parentCharacterizableId != null;
	}

	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 * @param type
	 *          see {@link CharacteristicType}
	 * @param name
	 * @param description
	 * @param value
	 * @param parentCharacterizable
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static Characteristic createInstance(final Identifier creatorId,
			final CharacteristicType type,
			final String name,
			final String description,
			final String value,
			final Characterizable parentCharacterizable,
			final boolean editable,
			final boolean visible) throws CreateObjectException {
		final boolean usePool = false;

		assert parentCharacterizable != null : NON_NULL_EXPECTED;
		try {
			final Characteristic characteristic = new Characteristic(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTIC_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					type,
					name,
					description,
					value,
					parentCharacterizable.getId(),
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
	public static Characteristic createInstance(
			final Identifier creatorId,
			final XmlCharacteristic xmlCharacteristic,
			final String importType)
	throws CreateObjectException {
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
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlCharacteristic ct = (IdlCharacteristic) transferable;
		
		super.fromTransferable(ct);
		
		this.type = StorableObjectPool.getStorableObject(new Identifier(ct._typeId), true);
		this.name = ct.name;
		this.description = ct.description;
		this.value = ct.value;
		this.parentCharacterizableId = new Identifier(ct.characterizableId);
		this.editable = ct.editable;
		this.visible = ct.visible;
		
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param characteristic
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(final XmlCharacteristic characteristic,
			final String importType)
	throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlCharacteristic getTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
		
		return IdlCharacteristicHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.type.getId().getTransferable(),
				this.name,
				this.description,
				(this.value == null) ? "" : this.value,
				this.parentCharacterizableId.getTransferable(),
				this.editable,
				this.visible);
	}

	/**
	 * @param characteristic
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlCharacteristic characteristic,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		throw new UnsupportedOperationException();
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

	public CharacteristicType getType() {
		return this.type;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setType0(final CharacteristicType type) {
		this.type = type;
	}

	public void setType(final CharacteristicType type) {
		this.setType0(type);
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setName0(final String name) {
		this.name = name;
	}
	
	protected void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}


	public String getDescription() {
		return this.description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDescription0(final String description) {
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		if (this.value.intern() != value.intern()) {
			super.markAsChanged();
			this.value = value;
		}
	}

	/**
	 * @todo add check whether parentCharacterizableId is non-void.
	 */
	public Identifier getParentCharacterizableId() {
		assert this.parentCharacterizableId != null : NON_NULL_EXPECTED;

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
		assert parentCharacterizableId != null : NON_NULL_EXPECTED;
		/*
		 * Further check for identifier validity (e. g.: either void or
		 * major corresponds to some specific entity code) cannot be
		 * performed since multiple StorableObject descendants may
		 * implement Characterizable (in other words, we don't have a
		 * special CHARACTERIZABLE_CODE).
		 */

		if (this.parentCharacterizableId.equals(parentCharacterizableId)) {
			return;
		}

		final StorableObject storableObject = StorableObjectPool.getStorableObject(parentCharacterizableId, true);
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
	protected void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final CharacteristicType type,
			final String name,
			final String description,
			final String value,
			final Identifier characterizableId,
			final boolean editable,
			final boolean visible) {
		synchronized (this) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version);
			this.type = type;
			this.name = name;
			this.description = description;
			this.value = value;
			this.parentCharacterizableId = characterizableId;
			this.editable = editable;
			this.visible = visible;
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.parentCharacterizableId);
		dependencies.add(this.type);
		return dependencies;
	}

	public Set<Identifiable> getReverseDependencies(final boolean usePool) {
		return Collections.<Identifiable>singleton(super.id);
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
		final Characteristic clone = super.clone();

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
}
