/*
 * $Id: Evaluation.java,v 1.38 2004/12/27 21:00:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.38 $, $Date: 2004/12/27 21:00:01 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class Evaluation extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3617570505297703480L;

	private Set thresholdSet;

	private StorableObjectDatabase evaluationDatabase;

	public Evaluation(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Evaluation(Evaluation_Transferable et) throws CreateObjectException {
		super(et.header,
			  null,
			  new Identifier(et.monitored_element_id));

		try {
			super.type = (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(et.type_id), true);

			this.thresholdSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(et.threshold_set_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
	}

	protected Evaluation(Identifier id,
						 Identifier creatorId,
						 EvaluationType type,
						 Identifier monitoredElementId,
						 Set thresholdSet) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					type,
					monitoredElementId);

		this.thresholdSet = thresholdSet;

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.evaluationDatabase != null)
				this.evaluationDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		return new Evaluation_Transferable(super.getHeaderTransferable(),
										   (Identifier_Transferable)super.type.getId().getTransferable(),
										   (Identifier_Transferable)super.monitoredElementId.getTransferable(),
										   (Identifier_Transferable)this.thresholdSet.getId().getTransferable());
	}

	public short getEntityCode() {
        return ObjectEntities.EVALUATION_ENTITY_CODE;
    }
    
    public Set getThresholdSet() {
		return this.thresholdSet;
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  EvaluationType type,
											  Identifier monitoredElementId,
											  Set thresholdSet) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			type,
			monitoredElementId);
		this.thresholdSet = thresholdSet;
	}

	public Result createResult(Identifier creatorId,
							   Measurement measurement,
							   AlarmLevel alarmLevel,
							   SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(creatorId,
			measurement,
			this,
			ResultSort.RESULT_SORT_EVALUATION,
			alarmLevel,
			parameters);
	}
	
	
	
	/** 
	 * @deprecated as unsupport method
	 */
	public Result createResult(Identifier creatorId, SetParameter[] parameters)
			throws CreateObjectException {
		throw new UnsupportedOperationException("method isn't support");
	}

	public static Evaluation createInstance(Identifier creatorId,
											EvaluationType type,
											Identifier monitoredElementId,
											Set thresholdSet) throws CreateObjectException {
		if (creatorId == null || type == null || monitoredElementId == null || thresholdSet == null)
			throw new IllegalArgumentException("Argument is 'null'");		

		try {
			return new Evaluation(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATION_ENTITY_CODE),
				creatorId,
				type,
				monitoredElementId,
				thresholdSet);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Evaluation.createInstance | cannot generate identifier ", e);
		}
	}
	
	public List getDependencies() {		
		return Collections.singletonList(this.thresholdSet);
	}
}
