/*
 * $Id: Result.java,v 1.20 2004/11/04 08:51:52 bob Exp $
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.20 $, $Date: 2004/11/04 08:51:52 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Result extends StorableObject {
	private Measurement measurement;
	private Action action;
	private int sort;
	private int alarmLevel;
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
		super(new Identifier(rt.id),
					new Date(rt.created),
					new Date(rt.modified),
					new Identifier(rt.creator_id),
					new Identifier(rt.modifier_id));

		this.sort = rt.sort.value();
		this.alarmLevel = rt.alarm_level.value();
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
								 int alarmLevel,
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
		this.alarmLevel = alarmLevel;
		this.parameters = parameters;

		super.currentVersion = super.getNextVersion();

		this.resultDatabase = MeasurementDatabaseContext.resultDatabase;
	}
	
	public static Result getInstance(Result_Transferable rt) throws CreateObjectException {
		Result result = new Result(rt);
		
		result.resultDatabase = MeasurementDatabaseContext.resultDatabase;
		try {
			if (result.resultDatabase != null)
				result.resultDatabase.insert(result);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		
		return result;
	}

	public Object getTransferable() {
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable)this.parameters[i].getTransferable();
		return new Result_Transferable((Identifier_Transferable)this.id.getTransferable(),
																	 super.created.getTime(),
																	 super.modified.getTime(),
																	 (Identifier_Transferable)super.creatorId.getTransferable(),
																	 (Identifier_Transferable)super.modifierId.getTransferable(),
																	 (this.sort != ResultSort._RESULT_SORT_MODELING)?(Identifier_Transferable)this.measurement.getId().getTransferable():(new Identifier_Transferable("")),
																	 (this.sort == ResultSort._RESULT_SORT_ANALYSIS)?(Identifier_Transferable)this.action.getId().getTransferable():(new Identifier_Transferable("")),
																	 (this.sort == ResultSort._RESULT_SORT_EVALUATION)?(Identifier_Transferable)this.action.getId().getTransferable():(new Identifier_Transferable("")),
																	 (this.sort == ResultSort._RESULT_SORT_MODELING)?(Identifier_Transferable)this.action.getId().getTransferable():(new Identifier_Transferable("")),
																	 ResultSort.from_int(this.sort),
																	 pts,
																	 AlarmLevel.from_int(this.alarmLevel));
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

	public AlarmLevel getAlarmLevel() {
		return AlarmLevel.from_int(this.alarmLevel);
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Measurement measurement,
																						Action action,
																						int sort,
																						int alarmLevel) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.measurement = measurement;
		this.action = action;
		this.sort = sort;
		this.alarmLevel = alarmLevel;
	}

	protected synchronized void setParameters(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	protected static Result createInstance(Identifier id,
																				 Identifier creatorId,
																				 Measurement measurement,
																				 Action action,
																				 ResultSort sort,
																				 AlarmLevel alarmLevel,
																				 SetParameter[] parameters) throws CreateObjectException {
		return new Result(id,
											creatorId,
											measurement,
											action,
											sort.value(),
											alarmLevel.value(),
											parameters);
	}
	
	protected static Result createInstance(Identifier id,
											 Identifier creatorId,
											 Modeling modeling,
											 ResultSort sort,					
											 SetParameter[] parameters) throws CreateObjectException {

		return new Result(id,
				creatorId,
				null,
				modeling,
				sort.value(),
				AlarmLevel._ALARM_LEVEL_NONE,
				parameters);
	}
	
	protected List getDependencies() {		
		return Collections.singletonList(this.measurement.getId());
	}
}
