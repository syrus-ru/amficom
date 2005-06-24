/*
 * $Id: ModelingType.java,v 1.37 2005/06/24 13:54:35 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;

/**
 * @version $Revision: 1.37 $, $Date: 2005/06/24 13:54:35 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class ModelingType extends ActionType {
/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3904963062178854712L;

	public static final String CODENAME_DADARA = "dadara";

	private Set<Identifier> inParameterTypeIds;
	private Set<Identifier> outParameterTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ModelingType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet<Identifier>();
		this.outParameterTypeIds = new HashSet<Identifier>();

		final ModelingTypeDatabase database = (ModelingTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.MODELING_TYPE_CODE);
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
	ModelingType(final IdlModelingType mtt) throws CreateObjectException {
		try {
			this.fromTransferable(mtt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ModelingType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> outParameterTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypeIds = new HashSet<Identifier>();
		this.setInParameterTypeIds0(inParameterTypeIds);

		this.outParameterTypeIds = new HashSet<Identifier>();
		this.setOutParameterTypeIds0(outParameterTypeIds);
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypeIds
	 * @param outParameterTypeIds
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static ModelingType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<Identifier> inParameterTypeIds,
			final Set<Identifier> outParameterTypeIds) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final ModelingType modelingType = new ModelingType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_TYPE_CODE),
					creatorId,
					0L,
					codename,
					description,
					inParameterTypeIds,
					outParameterTypeIds);

			assert modelingType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			modelingType.markAsChanged();

			return modelingType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlModelingType mtt = (IdlModelingType) transferable;
		super.fromTransferable(mtt.header, mtt.codename, mtt.description);

		this.inParameterTypeIds = Identifier.fromTransferables(mtt.inParameterTypeIds);
		this.outParameterTypeIds = Identifier.fromTransferables(mtt.outParameterTypeIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IdlModelingType getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final IdlIdentifier[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		final IdlIdentifier[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);

		return new IdlModelingType(super.getHeaderTransferable(),
				super.codename,
				super.description != null ? super.description : "",
				inParTypeIds,
				outParTypeIds);
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObjectType#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid() && this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET
			&& this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET;
	}
	
	public Set<Identifier> getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public Set<Identifier> getOutParameterTypeIds() {
		return Collections.unmodifiableSet(this.outParameterTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void setParameterTypeIds(final Map<String, Set<Identifier>> parameterTypeIdsModeMap) {
		this.setInParameterTypeIds0(parameterTypeIdsModeMap.get(ModelingTypeWrapper.MODE_IN));
		this.setOutParameterTypeIds0(parameterTypeIdsModeMap.get(ModelingTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Map<String, Set<Identifier>> getParameterTypeIdsModeMap() {
		final Map<String, Set<Identifier>> parameterTypeIdsModeMap = new HashMap<String, Set<Identifier>>(2);
		parameterTypeIdsModeMap.put(ModelingTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(ModelingTypeWrapper.MODE_OUT, this.outParameterTypeIds);
		return parameterTypeIdsModeMap;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypeIds0(final Set<Identifier> inParameterTypeIds) {
		this.inParameterTypeIds.clear();
		if (inParameterTypeIds != null)
			this.inParameterTypeIds.addAll(inParameterTypeIds);
	}

	/**
	 * client setter for inParameterTypeIds
	 *
	 * @param inParameterTypeIds
	 *            The inParameterTypeIds to set.
	 */
	public void setInParameterTypeIds(final Set<Identifier> inParameterTypeIds) {
		this.setInParameterTypeIds0(inParameterTypeIds);
		super.markAsChanged();		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypeIds0(final Set<Identifier> outParameterTypeIds) {
		this.outParameterTypeIds.clear();
		if (outParameterTypeIds != null)
			this.outParameterTypeIds.addAll(outParameterTypeIds);
	}

	/**
	 * client setter for outParameterTypeIds
	 *
	 * @param outParameterTypeIds
	 *            The outParameterTypeIds to set.
	 */
	public void setOutParameterTypeIds(final Set<Identifier> outParameterTypeIds) {
		this.setOutParameterTypeIds0(outParameterTypeIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		if (this.inParameterTypeIds != null)
			dependencies.addAll(this.inParameterTypeIds);
				
		if (this.outParameterTypeIds != null)
			dependencies.addAll(this.outParameterTypeIds);
				
		return dependencies;
	}
}
