/*
al * $Id: Result.java,v 1.31 2005/01/12 13:34:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2005/01/12 13:34:13 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class Result extends StorableObject {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256999964965286967L;
	private Measurement measurement;
	private Action action;
	private int sort;
	private SetParameter[] parameters;

	private StorableObjectDatabase resultDatabase;

	public Result(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.resultDatabase = MeasurementDatabaseContext.resultDatabase;
		try {
			this.resultDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Result(Result_Transferable rt) throws CreateObjectException {
		super(rt.header);

		this.sort = rt.sort.value();
		switch (this.sort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				try {
					this.measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.measurement_id), true);
				}
				catch (ApplicationException ae) {
					throw new CreateObjectException("Cannot create result -- " + ae.getMessage(), ae);
				}
				this.action = this.measurement;			
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				try {
					this.measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.measurement_id), true);
					this.action = (Analysis)MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.analysis_id), true);
				}
				catch (ApplicationException ae) {
					throw new CreateObjectException("Cannot create result -- " + ae.getMessage(), ae);
				}
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				try {
					this.measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.measurement_id), true);
					this.action = (Evaluation)MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.evaluation_id), true);
				}
				catch (ApplicationException ae) {
					throw new CreateObjectException("Cannot create result -- " + ae.getMessage(), ae);
				}
				break;
			case ResultSort._RESULT_SORT_MODELING:
				try {
					this.action = (Modeling)MeasurementStorableObjectPool.getStorableObject(new Identifier(rt.evaluation_id), true);
				}
				catch (ApplicationException ae) {
					throw new CreateObjectException("Cannot create result -- " + ae.getMessage(), ae);
				}
				break;
				
			default:
				Log.errorMessage("Result.init | Illegal sort: " + this.sort + " of result '" + super.id.toString() + "'");
		}

		try {
			this.parameters = new SetParameter[rt.parameters.length];
			for (int i = 0; i < this.parameters.length; i++)
				this.parameters[i] = new SetParameter(rt.parameters[i]);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

	}

	protected Result(Identifier id,
								 Identifier creatorId,
								 Measurement measurement,
								 Action action,
								 int sort,
								 SetParameter[] parameters) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.measurement = measurement;
		this.action = action;
		this.sort = sort;
		this.parameters = parameters;

		super.currentVersion = super.getNextVersion();

		this.resultDatabase = MeasurementDatabaseContext.resultDatabase;
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.resultDatabase != null)
				this.resultDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable)this.parameters[i].getTransferable();
		return new Result_Transferable(super.getHeaderTransferable(),
									   (this.sort != ResultSort._RESULT_SORT_MODELING)?(Identifier_Transferable)this.measurement.getId().getTransferable():(new Identifier_Transferable("")),
									   (this.sort == ResultSort._RESULT_SORT_ANALYSIS)?(Identifier_Transferable)this.action.getId().getTransferable():(new Identifier_Transferable("")),
									   (this.sort == ResultSort._RESULT_SORT_EVALUATION)?(Identifier_Transferable)this.action.getId().getTransferable():(new Identifier_Transferable("")),
									   (this.sort == ResultSort._RESULT_SORT_MODELING)?(Identifier_Transferable)this.action.getId().getTransferable():(new Identifier_Transferable("")),
									   ResultSort.from_int(this.sort),
									   pts);
	}

    public short getEntityCode() {
        return ObjectEntities.RESULT_ENTITY_CODE;
    }
    
	public Measurement getMeasurement() {
		return this.measurement;
	}

	public Action getAction() {
		return this.action;
	}

	public ResultSort getSort() {
		return ResultSort.from_int(this.sort);
	}

	public SetParameter[] getParameters() {
		return this.parameters;
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  Measurement measurement,
											  Action action,
											  int sort) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId);
		this.measurement = measurement;
		this.action = action;
		this.sort = sort;
	}

	protected synchronized void setParameters(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	protected static Result createInstance(Identifier creatorId,
										   Measurement measurement,
										   Action action,
										   ResultSort sort,
										   SetParameter[] parameters) throws CreateObjectException {
		if (creatorId == null || measurement == null || action == null || sort == null ||
				parameters == null || parameters.length == 0)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new Result(IdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE),
				creatorId,
				measurement,
				action,
				sort.value(),
				parameters);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Result.createInstance | cannot generate identifier ", e);
		}
	}
	
	public List getDependencies() {		
		return Collections.singletonList(this.measurement);
	}
}
