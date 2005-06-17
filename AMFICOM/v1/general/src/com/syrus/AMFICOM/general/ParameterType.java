/*
 * $Id: ParameterType.java,v 1.33 2005/06/17 11:00:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.33 $, $Date: 2005/06/17 11:00:57 $
 * @author $Author: bass $
 * @module general_v1
 */

public final class ParameterType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = 4050767108738528569L;

	private String name;
	private int dataType;
	
	private Set characteristics;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ParameterType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.characteristics = new HashSet();
		
		ParameterTypeDatabase database = (ParameterTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PARAMETER_TYPE_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ParameterType(final ParameterType_Transferable ptt) throws CreateObjectException {
		try {
			this.fromTransferable(ptt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ParameterType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final String name,
			final int dataType) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.dataType = dataType;
		
		this.characteristics = new HashSet();
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param dataType {@link DataType}
	 * @throws CreateObjectException
	 */
	public static ParameterType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final DataType dataType) throws CreateObjectException {
		try {
			ParameterType parameterType = new ParameterType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETER_TYPE_CODE),
					creatorId,
					0L,
					codename,
					description,
					name,
					dataType.value());

			assert parameterType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			parameterType.markAsChanged();

			return parameterType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.characteristics != null && this.characteristics != Collections.EMPTY_SET;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		ParameterType_Transferable ptt = (ParameterType_Transferable) transferable;
		try {
			super.fromTransferable(ptt.header, ptt.codename, ptt.description);
		}
		catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.name = ptt.name;
		this.dataType = ptt.data_type.value();
		
		Set characteristicIds = Identifier.fromTransferables(ptt.characteristic_ids);
		this.characteristics = new HashSet(ptt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);

		return new ParameterType_Transferable(super.getHeaderTransferable(),
				super.codename,
				super.description != null ? super.description : "",
				this.name,
				DataType.from_int(this.dataType),
				charIds);
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

	/**
	 * client setter for name
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}

	public DataType getDataType() {
		return DataType.from_int(this.dataType);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDataType0(final DataType dataType) {
		this.dataType = dataType.value();
	}

	public void setDataType(final DataType dataType) {
		this.setDataType0(dataType);
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
			final String codename,
			final String description,
			final String name,
			final int dataType) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
		this.name = name;
		this.dataType = dataType;
	}

	public void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.markAsChanged();
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}
}
