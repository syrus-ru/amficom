/*
 * $Id: ModelingType.java,v 1.23 2005/04/15 19:22:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;

/**
 * @version $Revision: 1.23 $, $Date: 2005/04/15 19:22:19 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class ModelingType extends ActionType {
/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3904963062178854712L;

	public static final String CODENAME_DADARA = "dadara";

	private java.util.Set inParameterTypes;
	private java.util.Set outParameterTypes;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public ModelingType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new HashSet();
		this.outParameterTypes = new HashSet();

		ModelingTypeDatabase database = MeasurementDatabaseContext.getModelingTypeDatabase();
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
							 java.util.Set inParameterTypes,
							 java.util.Set outParameterTypes) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypes = new HashSet();
		this.setInParameterTypes0(inParameterTypes);

		this.outParameterTypes = new HashSet();
		this.setOutParameterTypes0(outParameterTypes);
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param outParameterTypes
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static ModelingType createInstance(Identifier creatorId,
												String codename,
												String description,
												java.util.Set inParameterTypes,
												java.util.Set outParameterTypes) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			ModelingType modelingType = new ModelingType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELINGTYPE_ENTITY_CODE),
										creatorId,
										0L,
										codename,
										description,
										inParameterTypes,
										outParameterTypes);
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

		java.util.Set parTypeIds;

		parTypeIds = Identifier.fromTransferables(mtt.in_parameter_type_ids);
		this.inParameterTypes = new HashSet(mtt.in_parameter_type_ids.length);
		this.setInParameterTypes0(GeneralStorableObjectPool.getStorableObjects(parTypeIds, true));

		parTypeIds = Identifier.fromTransferables(mtt.out_parameter_type_ids);
		this.outParameterTypes = new HashSet(mtt.out_parameter_type_ids.length);
		this.setOutParameterTypes0(GeneralStorableObjectPool.getStorableObjects(parTypeIds, true));
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		int i;

		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypes.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

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
		return super.isValid() && this.inParameterTypes != null && this.inParameterTypes != Collections.EMPTY_SET
			&& this.outParameterTypes != null && this.outParameterTypes != Collections.EMPTY_SET;
	}
	
	public java.util.Set getInParameterTypes() {
		return Collections.unmodifiableSet(this.inParameterTypes);
	}

	public java.util.Set getOutParameterTypes() {
		return Collections.unmodifiableSet(this.outParameterTypes);
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
	protected synchronized void setParameterTypes(java.util.Set inParameterTypes,
			java.util.Set outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setInParameterTypes0(java.util.Set inParameterTypes) {
		this.inParameterTypes.clear();
		if (inParameterTypes != null)
			this.inParameterTypes.addAll(inParameterTypes);
	}

	/**
	 * client setter for inParameterTypes
	 * 
	 * @param inParameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setInParameterTypes(java.util.Set inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.changed = true;		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setOutParameterTypes0(java.util.Set outParameterTypes) {
		this.outParameterTypes.clear();
		if (outParameterTypes != null)
			this.outParameterTypes.addAll(outParameterTypes);
	}

	/**
	 * client setter for outParameterTypes
	 * 
	 * @param outParameterTypes
	 *            The outParameterTypes to set.
	 */
	public void setOutParameterTypes(java.util.Set outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();
		if (this.inParameterTypes != null)
			dependencies.addAll(this.inParameterTypes);
				
		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);
				
		return dependencies;
	}
}
