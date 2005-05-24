/*
 * $Id: Analysis.java,v 1.59 2005/05/24 13:25:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;

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
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.59 $, $Date: 2005/05/24 13:25:00 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class Analysis extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3979266967062721849L;

	private String name;
	private Set criteriaSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Analysis(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		AnalysisDatabase database = (AnalysisDatabase) DatabaseContext.getDatabase(ObjectEntities.ANALYSIS_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Analysis(Analysis_Transferable at) throws CreateObjectException {
		try {
			this.fromTransferable(at);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Analysis(Identifier id,
					   Identifier creatorId,
					   long version,
					   AnalysisType type,
					   Identifier monitoredElementId,
					   Measurement measurement,
					   String name,
					   Set criteriaSet) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					version,
					type,
					monitoredElementId,
					measurement);

		this.name = name;
		this.criteriaSet = criteriaSet;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Analysis_Transferable at = (Analysis_Transferable) transferable;
		super.fromTransferable(at.header, null, new Identifier(at.monitored_element_id), null);

		super.type = (AnalysisType) StorableObjectPool.getStorableObject(new Identifier(at.type_id), true);
		super.parentAction = (at.measurement_id.identifier_string.length() != 0)
				? (Measurement) StorableObjectPool.getStorableObject(new Identifier(at.measurement_id), true) : null;

		this.criteriaSet = (Set) StorableObjectPool.getStorableObject(new Identifier(at.criteria_set_id), true);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new Analysis_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) super.type.getId().getTransferable(),
				(Identifier_Transferable) super.monitoredElementId.getTransferable(),
				(super.parentAction != null) ? (Identifier_Transferable) super.parentAction.getId().getTransferable()
						: new Identifier_Transferable(""),
				this.name != null ? this.name : "",
				(Identifier_Transferable) this.criteriaSet.getId().getTransferable());
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.criteriaSet != null;
	}

	public short getEntityCode() {
		return ObjectEntities.ANALYSIS_ENTITY_CODE;
	}

	public Measurement getMeasurement() {
		return (Measurement)super.parentAction;
	}
	
	public void setMeasurement(final Measurement measurement) {
		super.parentAction = measurement;
		super.changed = true;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.changed = true;
	}

	public void setCriteriaSet(final Set criteriaSet) {
		this.criteriaSet = criteriaSet;
		super.changed = true;
	}
	
	public Set getCriteriaSet() {
		return this.criteriaSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			AnalysisType type,
			Identifier monitoredElementId,
			Measurement measurement,
			String name,
			Set criteriaSet) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				type,
				monitoredElementId,
				measurement);
		this.name = name;
		this.criteriaSet = criteriaSet;
	}

	/**
	 * Create a new instance for client
	 *
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param measurement
	 * @param criteriaSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Analysis createInstance(Identifier creatorId,
										  AnalysisType type,
										  Identifier monitoredElementId,
										  Measurement measurement,
										  String name,
										  Set criteriaSet) throws CreateObjectException {
		try {
			Analysis analysis = new Analysis(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_ENTITY_CODE),
				creatorId,
				0L,
				type,
				monitoredElementId,
				measurement,
				name,
				criteriaSet);
			
			assert analysis.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			
			analysis.changed = true;
			return analysis;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public Result createResult(Identifier resultCreatorId, SetParameter[] resultParameters) throws CreateObjectException {
		return Result.createInstance(resultCreatorId,
				this,
				ResultSort.RESULT_SORT_ANALYSIS,
				resultParameters);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();
		//	Measurement, if exists
		if (super.parentAction != null)
			dependencies.add(super.parentAction);
		dependencies.add(this.criteriaSet);
		return dependencies;
	}
}
