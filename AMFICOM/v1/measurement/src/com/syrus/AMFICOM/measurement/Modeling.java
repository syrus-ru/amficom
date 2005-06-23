/*
 * $Id: Modeling.java,v 1.49 2005/06/23 18:45:09 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.IdlModeling;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;

/**
 * @version $Revision: 1.49 $, $Date: 2005/06/23 18:45:09 $
 * @author $Author: bass $
 * @author arseniy
 * @module measurement_v1
 */
public final class Modeling extends Action {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7265091663685378139L;

	private String name;
	private ParameterSet argumentSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Modeling(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		ModelingDatabase database = (ModelingDatabase) DatabaseContext.getDatabase(ObjectEntities.MODELING_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Modeling(final IdlModeling mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Modeling(final Identifier id,
			final Identifier creatorId,
			final long version,
			final ModelingType type,
			final Identifier monitoredElementId,
			final String name,
			final ParameterSet argumentSet) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version,
			type,
			monitoredElementId,
			null);
		this.name = name;
		this.argumentSet = argumentSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlModeling mt = (IdlModeling) transferable;
		super.fromTransferable(mt.header, null, new Identifier(mt.monitoredElementId), null);

		super.type = (ModelingType) StorableObjectPool.getStorableObject(new Identifier(mt._typeId), true);

		this.argumentSet = (ParameterSet) StorableObjectPool.getStorableObject(new Identifier(mt.argumentSetId), true);

		this.name = mt.name;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IdlModeling getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new IdlModeling(super.getHeaderTransferable(),
				super.type.getId().getTransferable(),
				super.monitoredElementId.getTransferable(),
				this.name,
				this.argumentSet.getId().getTransferable());
	}

	public short getEntityCode() {
		return ObjectEntities.MODELING_CODE;
	}

	public String getName() {
		return this.name;
	}

	public ParameterSet getArgumentSet() {
		return this.argumentSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final ModelingType type,
			final Identifier monitoredElementId,
			final String name,
			final ParameterSet argumentSet) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				type,
				monitoredElementId,
				null);
		this.name = name;
		this.argumentSet = argumentSet;
	}

	/**
	 * Create a new instance for client
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param name
	 * @param argumentSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Modeling createInstance(final Identifier creatorId,
			final ModelingType type,
			final Identifier monitoredElementId,
			final String name,
			final ParameterSet argumentSet) throws CreateObjectException{
		try {
			Modeling modeling = new Modeling(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_CODE),
					creatorId,
					0L,
					type,
					monitoredElementId,
					name,
					argumentSet);

			assert modeling.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			modeling.markAsChanged();

			return modeling;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public Result createResult(final Identifier resultCreatorId, final Parameter[] resultParameters)
			throws CreateObjectException {
		return Result.createInstance(resultCreatorId, this, ResultSort.RESULT_SORT_MODELING, resultParameters);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	public java.util.Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.singleton(this.argumentSet);
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.measurement.Action#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.argumentSet != null;
	}
	
	
	/**
	 * @param argumentSet The argumentSet to set.
	 */
	public void setArgumentSet(final ParameterSet argumentSet) {
		this.argumentSet = argumentSet;
		super.markAsChanged();
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
}
