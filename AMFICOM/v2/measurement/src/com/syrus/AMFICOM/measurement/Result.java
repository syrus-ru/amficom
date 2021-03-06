package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;

public class Result extends StorableObject {
	private Measurement measurement;
	private Action action;
	private int sort;
	private int alarm_level;
	private SetParameter[] parameters;

	private StorableObject_Database resultDatabase;

	public Result(Identifier id) throws RetrieveObjectException {
		super(id);

		this.resultDatabase = MeasurementDatabaseContext.resultDatabase;
		try {
			this.resultDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Result(Result_Transferable rt) throws CreateObjectException, RetrieveObjectException {
		super(new Identifier(rt.id),
					new Date(rt.created),
					new Date(rt.modified),
					new Identifier(rt.creator_id),
					new Identifier(rt.modifier_id));
		this.measurement = new Measurement(new Identifier(rt.measurement_id));
		this.sort = rt.sort.value();
		this.alarm_level = rt.alarm_level.value();
		switch (this.sort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				this.action = this.measurement;
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				this.action = new Analysis(new Identifier(rt.analysis_id));
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				this.action = new Evaluation(new Identifier(rt.evaluation_id));
				break;
		}

		this.parameters = new SetParameter[rt.parameters.length];
		for (int i = 0; i < this.parameters.length; i++)
			this.parameters[i] = new SetParameter(rt.parameters[i]);

		this.resultDatabase = MeasurementDatabaseContext.resultDatabase;
		try {
			this.resultDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Result(Identifier id,
								 Identifier creator_id,
								 Measurement measurement,
								 Action action,
								 ResultSort sort,
								 AlarmLevel alarm_level,
								 Identifier[] parameter_ids,
								 Identifier[] parameter_type_ids,
								 byte[][] parameter_values) throws CreateObjectException {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creator_id,
					creator_id);
		this.measurement = measurement;
		this.action = action;
		this.sort = sort.value();
		this.alarm_level = alarm_level.value();

		this.parameters = new SetParameter[Math.min(Math.min(parameter_ids.length, parameter_type_ids.length), parameter_values.length)];
		for (int i = 0; i < this.parameters.length; i++)
			this.parameters[i] = new SetParameter(parameter_ids[i],
																						parameter_type_ids[i],
																						parameter_values[i]);

		this.resultDatabase = MeasurementDatabaseContext.resultDatabase;
		try {
			this.resultDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		Parameter_Transferable[] pts = new Parameter_Transferable[this.parameters.length];
		for (int i = 0; i < pts.length; i++)
			pts[i] = (Parameter_Transferable)this.parameters[i].getTransferable();
		return new Result_Transferable((Identifier_Transferable)this.id.getTransferable(),
																	 super.created.getTime(),
																	 super.modified.getTime(),
																	 (Identifier_Transferable)super.creator_id.getTransferable(),
																	 (Identifier_Transferable)super.modifier_id.getTransferable(),
																	 (Identifier_Transferable)this.measurement.getId().getTransferable(),
																	 (this.sort == ResultSort._RESULT_SORT_ANALYSIS)?(Identifier_Transferable)this.action.getId().getTransferable():new Identifier_Transferable(0),
																	 (this.sort == ResultSort._RESULT_SORT_EVALUATION)?(Identifier_Transferable)this.action.getId().getTransferable():new Identifier_Transferable(0),
																	 ResultSort.from_int(this.sort),
																	 pts,
																	 AlarmLevel.from_int(this.alarm_level));
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
		return AlarmLevel.from_int(this.alarm_level);
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Measurement measurement,
																						Action action,
																						int sort,
																						int alarm_level) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.measurement = measurement;
		this.action = action;
		this.sort = sort;
		this.alarm_level = alarm_level;
	}

	protected synchronized void setParameters(SetParameter[] parameters) {
		this.parameters = parameters;
	}

	protected static Result create(Identifier id,
																 Identifier creator_id,
																 Measurement measurement,
																 Action action,
																 ResultSort sort,
																 AlarmLevel alarm_level,
																 Identifier[] parameter_ids,
																 Identifier[] parameter_type_ids,
																 byte[][] parameter_values) throws CreateObjectException {
		return new Result(id,
											creator_id,
											measurement,
											action,
											sort,
											alarm_level,
											parameter_ids,
											parameter_type_ids,
											parameter_values);
	}
}