package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public class Analysis extends Action {
	private Set criteriaSet;

	private StorableObjectDatabase analysisDatabase;

	public Analysis(Identifier id) throws RetrieveObjectException {
		super(id);

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Analysis(Analysis_Transferable at) throws CreateObjectException, RetrieveObjectException {
		super(new Identifier(at.id),
					new Date(at.created),
					new Date(at.modified),
					new Identifier(at.creator_id),
					new Identifier(at.modifier_id),
					new Identifier(at.type_id),
					new Identifier(at.monitored_element_id));
		this.criteriaSet = new Set(new Identifier(at.criteria_set_id));

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Analysis(Identifier id,
									 Identifier creatorId,
									 Identifier typeId,
									 Set criteriaSet,
									 Identifier monitoredElementId) throws CreateObjectException {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					typeId,
					monitoredElementId);
		this.criteriaSet = criteriaSet;

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Analysis_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creator_id.getTransferable(),
																			(Identifier_Transferable)super.modifier_id.getTransferable(),
																			(Identifier_Transferable)super.typeId.getTransferable(),
																			(Identifier_Transferable)super.monitoredElementId.getTransferable(),
																			(Identifier_Transferable)this.criteriaSet.getId().getTransferable());
	}

	public Set getCriteriaSet() {
		return this.criteriaSet;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier typeId,
																						Identifier monitoredElementId,
																						Set criteriaSet) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												typeId,
												monitoredElementId);
		this.criteriaSet = criteriaSet;
	}

	public Result createResult(Identifier id,
														 Identifier creatorId,
														 Measurement measurement,
														 AlarmLevel alarmLevel,
														 Identifier[] parameterIds,
														 Identifier[] parameterTypeIds,
														 byte[][] parameterValues) throws CreateObjectException {
		return Result.create(id,
												 creatorId,
												 measurement,
												 this,
												 ResultSort.RESULT_SORT_ANALYSIS,
												 alarmLevel,
												 parameterIds,
												 parameterTypeIds,
												 parameterValues);
	}

	public static Analysis create(Identifier id,
																Identifier creatorId,
																Identifier typeId,
																Set criteriaSet,
																Identifier monitoredElementId) throws CreateObjectException {
		return new Analysis(id,
												creatorId,
												typeId,
												criteriaSet,
												monitoredElementId);
	}
}