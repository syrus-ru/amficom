package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public class Evaluation extends Action {
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
		super(new Identifier(et.id),
					new Date(et.created),
					new Date(et.modified),
					new Identifier(et.creator_id),
					new Identifier(et.modifier_id),
					(EvaluationType)MeasurementObjectTypePool.getObjectType(new Identifier(et.type_id)),
					new Identifier(et.monitored_element_id));

		try {
			this.thresholdSet = new Set(new Identifier(et.threshold_set_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		catch (ObjectNotFoundException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Evaluation(Identifier id,
										 Identifier creatorId,
										 EvaluationType type,
										 Identifier monitoredElementId,
										 Set thresholdSet) throws CreateObjectException {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.type = type;
		super.monitoredElementId = monitoredElementId;

		this.thresholdSet = thresholdSet;

		super.currentVersion = super.getNextVersion();

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Evaluation_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			 super.created.getTime(),
																			 super.modified.getTime(),
																			 (Identifier_Transferable)super.creatorId.getTransferable(),
																			 (Identifier_Transferable)super.modifierId.getTransferable(),
																			 (Identifier_Transferable)super.type.getId().getTransferable(),
																			 (Identifier_Transferable)super.monitoredElementId.getTransferable(),
																			 new String(super.type.getCodename()),
																			 (Identifier_Transferable)this.thresholdSet.getId().getTransferable());
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

	public Result createResult(Identifier id,
														 Identifier creatorId,
														 Measurement measurement,
														 AlarmLevel alarmLevel,
														 SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(id,
																 creatorId,
																 measurement,
																 this,
																 ResultSort.RESULT_SORT_EVALUATION,
																 alarmLevel,
																 parameters);
	}

	public static Evaluation createInstance(Identifier id,
																					Identifier creatorId,
																					EvaluationType type,
																					Identifier monitoredElementId,
																					Set thresholdSet) throws CreateObjectException {
		return new Evaluation(id,
													creatorId,
													type,
													monitoredElementId,
													thresholdSet);
	}
}
