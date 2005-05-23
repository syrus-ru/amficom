/*
 * $Id: ModelingType.java,v 1.27 2005/05/23 18:45:15 bass Exp $
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

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;

/**
 * @version $Revision: 1.27 $, $Date: 2005/05/23 18:45:15 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class ModelingType extends ActionType {
/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3904963062178854712L;

	public static final String CODENAME_DADARA = "dadara";

	private java.util.Set inParameterTypeIds;
	private java.util.Set outParameterTypeIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ModelingType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypeIds = new HashSet();
		this.outParameterTypeIds = new HashSet();

		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MODELINGTYPE_ENTITY_CODE);
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
	public ModelingType(ModelingType_Transferable mtt) throws CreateObjectException {
		try {
			this.fromTransferable(mtt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected ModelingType(Identifier id,
							 Identifier creatorId,
							 long version,
							 String codename,
							 String description,
							 java.util.Set inParameterTypeIds,
							 java.util.Set outParameterTypeIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypeIds = new HashSet();
		this.setInParameterTypeIds0(inParameterTypeIds);

		this.outParameterTypeIds = new HashSet();
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
	public static ModelingType createInstance(Identifier creatorId,
												String codename,
												String description,
												java.util.Set inParameterTypeIds,
												java.util.Set outParameterTypeIds) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			ModelingType modelingType = new ModelingType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELINGTYPE_ENTITY_CODE),
										creatorId,
										0L,
										codename,
										description,
										inParameterTypeIds,
										outParameterTypeIds);
			assert modelingType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			
			modelingType.changed = true;
			return modelingType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		ModelingType_Transferable mtt = (ModelingType_Transferable) transferable;
		super.fromTransferable(mtt.header, mtt.codename, mtt.description);

		this.inParameterTypeIds = Identifier.fromTransferables(mtt.in_parameter_type_ids);
		this.outParameterTypeIds = Identifier.fromTransferables(mtt.out_parameter_type_ids);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		Identifier_Transferable[] inParTypeIds = Identifier.createTransferables(this.inParameterTypeIds);
		Identifier_Transferable[] outParTypeIds = Identifier.createTransferables(this.outParameterTypeIds);

		return new ModelingType_Transferable(super.getHeaderTransferable(),
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
	protected boolean isValid() {
		return super.isValid() && this.inParameterTypeIds != null && this.inParameterTypeIds != Collections.EMPTY_SET
			&& this.outParameterTypeIds != null && this.outParameterTypeIds != Collections.EMPTY_SET;
	}
	
	public java.util.Set getInParameterTypeIds() {
		return Collections.unmodifiableSet(this.inParameterTypeIds);
	}

	public java.util.Set getOutParameterTypeIds() {
		return Collections.unmodifiableSet(this.outParameterTypeIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  String codename,
											  String description) {
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
	protected synchronized void setParameterTypeIds(Map parameterTypeIdsModeMap) {
		this.setInParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(ModelingTypeWrapper.MODE_IN));
		this.setOutParameterTypeIds0((java.util.Set) parameterTypeIdsModeMap.get(ModelingTypeWrapper.MODE_OUT));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Map getParameterTypeIdsModeMap() {
		Map parameterTypeIdsModeMap = new HashMap(2);
		parameterTypeIdsModeMap.put(ModelingTypeWrapper.MODE_IN, this.inParameterTypeIds);
		parameterTypeIdsModeMap.put(ModelingTypeWrapper.MODE_OUT, this.outParameterTypeIds);
		return parameterTypeIdsModeMap;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypeIds0(java.util.Set inParameterTypeIds) {
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
	public void setInParameterTypeIds(java.util.Set inParameterTypeIds) {
		this.setInParameterTypeIds0(inParameterTypeIds);
		super.changed = true;		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypeIds0(java.util.Set outParameterTypeIds) {
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
	public void setOutParameterTypeIds(java.util.Set outParameterTypeIds) {
		this.setOutParameterTypeIds0(outParameterTypeIds);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();
		if (this.inParameterTypeIds != null)
			dependencies.addAll(this.inParameterTypeIds);
				
		if (this.outParameterTypeIds != null)
			dependencies.addAll(this.outParameterTypeIds);
				
		return dependencies;
	}
}
