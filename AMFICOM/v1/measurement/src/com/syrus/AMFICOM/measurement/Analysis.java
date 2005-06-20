/*
 * $Id: Analysis.java,v 1.68 2005/06/20 17:29:55 bass Exp $
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
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.68 $, $Date: 2005/06/20 17:29:55 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class Analysis extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3979266967062721849L;

	private String name;
	private ParameterSet criteriaSet;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Analysis(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		AnalysisDatabase database = (AnalysisDatabase) DatabaseContext.getDatabase(ObjectEntities.ANALYSIS_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Analysis(final Analysis_Transferable at) throws CreateObjectException {
		try {
			this.fromTransferable(at);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Analysis(final Identifier id,
			final Identifier creatorId,
			final long version,
			final AnalysisType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final String name,
			final ParameterSet criteriaSet) {
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
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		Analysis_Transferable at = (Analysis_Transferable) transferable;
		super.fromTransferable(at.header, null, new Identifier(at.monitored_element_id), null);

		super.type = (AnalysisType) StorableObjectPool.getStorableObject(new Identifier(at.type_id), true);
		final Identifier parentActionId = new Identifier(at.measurement_id);
		super.parentAction = (!parentActionId.equals(Identifier.VOID_IDENTIFIER))
				? (Action) StorableObjectPool.getStorableObject(parentActionId, true) : null;

		this.criteriaSet = (ParameterSet) StorableObjectPool.getStorableObject(new Identifier(at.criteria_set_id), true);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Analysis_Transferable getTransferable() {

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return new Analysis_Transferable(super.getHeaderTransferable(),
				super.type.getId().getTransferable(),
				super.monitoredElementId.getTransferable(),
				(super.parentAction != null) ? super.parentAction.getId().getTransferable()
						: Identifier.VOID_IDENTIFIER.getTransferable(),
				this.name != null ? this.name : "",
				this.criteriaSet.getId().getTransferable());
	}
	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.criteriaSet != null;
	}

	public short getEntityCode() {
		return ObjectEntities.ANALYSIS_CODE;
	}

	public Measurement getMeasurement() {
		return (Measurement)super.parentAction;
	}
	
	public void setMeasurement(final Measurement measurement) {
		super.parentAction = measurement;
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public void setCriteriaSet(final ParameterSet criteriaSet) {
		this.criteriaSet = criteriaSet;
		super.markAsChanged();
	}
	
	public ParameterSet getCriteriaSet() {
		return this.criteriaSet;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final AnalysisType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final String name,
			final ParameterSet criteriaSet) {
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
	public static Analysis createInstance(final Identifier creatorId,
			final AnalysisType type,
			final Identifier monitoredElementId,
			final Measurement measurement,
			final String name,
			final ParameterSet criteriaSet) throws CreateObjectException {
		try {
			Analysis analysis = new Analysis(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_CODE),
				creatorId,
				0L,
				type,
				monitoredElementId,
				measurement,
				name,
				criteriaSet);

			assert analysis.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			analysis.markAsChanged();

			return analysis;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public Result createResult(final Identifier resultCreatorId, final Parameter[] resultParameters)
			throws CreateObjectException {
		return Result.createInstance(resultCreatorId, this, ResultSort.RESULT_SORT_ANALYSIS, resultParameters);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
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
