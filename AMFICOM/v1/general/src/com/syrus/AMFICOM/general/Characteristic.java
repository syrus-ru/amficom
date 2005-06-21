/*
 * $Id: Characteristic.java,v 1.43 2005/06/21 12:43:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.IdlCharacteristic;

/**
 * @version $Revision: 1.43 $, $Date: 2005/06/21 12:43:47 $
 * @author $Author: bass $
 * @module general_v1
 */

public final class Characteristic extends StorableObject implements TypedObject {
	private static final long serialVersionUID = -2746555753961778403L;

	private CharacteristicType type;
	private String name;
	private String description;
	private String value;
	private Identifier characterizableId;
	private boolean editable;
	private boolean visible;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param id
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	Characteristic(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		CharacteristicDatabase database = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

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
			final long version,
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
		this.characterizableId = characterizableId;

		this.editable = editable;
		this.visible = visible;
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
				|| this.characterizableId != null;
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
	 * @param characterizable
	 * @throws CreateObjectException
	 */
	public static Characteristic createInstance(final Identifier creatorId,
			final CharacteristicType type,
			final String name,
			final String description,
			final String value,
			final Characterizable characterizable,
			final boolean editable,
			final boolean visible) throws CreateObjectException {
		assert characterizable != null : ErrorMessages.NON_NULL_EXPECTED;
		try {
			Characteristic characteristic = new Characteristic(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTIC_CODE),
					creatorId,
					0L,
					type,
					name,
					description,
					value,
					characterizable.getId(),
					editable,
					visible);

			assert characteristic.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			characteristic.markAsChanged();

			return characteristic;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("Characteristic.createInstance | cannot generate identifier ", ige);
		}
	}	

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlCharacteristic ct = (IdlCharacteristic) transferable;
		
		super.fromTransferable(ct.header);
		
		this.type = (CharacteristicType) StorableObjectPool.getStorableObject(new Identifier(ct._typeId), true);
		this.name = ct.name;
		this.description = ct.description;
		this.value = ct.value;
		this.characterizableId = new Identifier(ct.characterizableId);
		this.editable = ct.editable;
		this.visible = ct.visible;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IdlCharacteristic getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new IdlCharacteristic(super.getHeaderTransferable(),
				this.type.getId().getTransferable(),
				this.name,
				this.description,
				(this.value != null) ? this.value : "",
				this.characterizableId.getTransferable(),
				this.editable,
				this.visible);
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

	public StorableObjectType getType() {
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
		super.markAsChanged();
		this.value = value;
	}

	public Identifier getCharacterizableId() {
		return this.characterizableId;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setCharacterizableId0(final Identifier characterizableId) {
		this.characterizableId = characterizableId;
	}
	
	public void setCharacterizableId(final Identifier characterizableId) {
		this.setCharacterizableId0(characterizableId);
		super.markAsChanged();
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final CharacteristicType type,
			final String name,
			final String description,
			final String value,
			final Identifier characterizableId,
			final boolean editable,
			final boolean visible) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.value = value;
		this.characterizableId = characterizableId;
		this.editable = editable;
		this.visible = visible;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.characterizableId);
		dependencies.add(this.type);
		return dependencies;
	}	
}
