/*
 * $Id: Modeling.java,v 1.43 2005/06/03 20:38:04 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.43 $, $Date: 2005/06/03 20:38:04 $
 * @author $Author: arseniy $
 * @author arseniy
 * @module measurement_v1
 */
public class Modeling extends Action {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7265091663685378139L;

	private String name;
	private Set argumentSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Modeling(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		ModelingDatabase database = (ModelingDatabase) DatabaseContext.getDatabase(ObjectEntities.MODELING_ENTITY_CODE);
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
	Modeling(final Modeling_Transferable mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		}
		catch (ApplicationException ae) {
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
			final Set argumentSet) {
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
		Modeling_Transferable mt = (Modeling_Transferable) transferable;
		super.fromTransferable(mt.header, null, new Identifier(mt.monitored_element_id), null);

		super.type = (ModelingType) StorableObjectPool.getStorableObject(new Identifier(mt.type_id), true);

		this.argumentSet = (Set) StorableObjectPool.getStorableObject(new Identifier(mt.argument_set_id), true);

		this.name = mt.name;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new Modeling_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) super.type.getId().getTransferable(),
				(Identifier_Transferable) super.monitoredElementId.getTransferable(),
				this.name,
				(Identifier_Transferable) this.argumentSet.getId().getTransferable());
	}

	public short getEntityCode() {
		return ObjectEntities.MODELING_ENTITY_CODE;
	}

	public String getName() {
		return this.name;
	}

	public Set getArgumentSet() {
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
			final Set argumentSet) {
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
			final Set argumentSet) throws CreateObjectException{
		try {
			Modeling modeling = new Modeling(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_ENTITY_CODE),
					creatorId,
					0L,
					type,
					monitoredElementId,
					name,
					argumentSet);

			assert modeling.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			modeling.markAsChanged();

			return modeling;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public Result createResult(final Identifier resultCreatorId, final SetParameter[] resultParameters)
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
	public void setArgumentSet(final Set argumentSet) {
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
