/*
 * $Id: ParameterType.java,v 1.49 2005/08/08 14:22:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlDataType;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlParameterTypeHelper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.49 $, $Date: 2005/08/08 14:22:49 $
 * @author $Author: arseniy $
 * @module general
 */

public final class ParameterType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = 4050767108738528569L;

	private String name;
	private DataType dataType;

	private transient CharacterizableDelegate characterizableDelegate;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ParameterType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		try {
			DatabaseContext.getDatabase(ObjectEntities.PARAMETER_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ParameterType(final IdlParameterType ptt) throws CreateObjectException {
		try {
			this.fromTransferable(ptt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ParameterType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final DataType dataType) {
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
			final ParameterType parameterType = new ParameterType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETER_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					dataType);

			assert parameterType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			parameterType.markAsChanged();

			return parameterType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid() && this.name != null;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlParameterType ptt = (IdlParameterType) transferable;
		try {
			super.fromTransferable(ptt, ptt.codename, ptt.description);
		} catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.name = ptt.name;
		this.dataType = DataType.fromTransferable(ptt.dataType);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlParameterType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlParameterTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name,
				(IdlDataType) this.dataType.getTransferable(orb));
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
		return this.dataType;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDataType0(final DataType dataType) {
		this.dataType = dataType;
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
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final DataType dataType) {
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

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}
}
