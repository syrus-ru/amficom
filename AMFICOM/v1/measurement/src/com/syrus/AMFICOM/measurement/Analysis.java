package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public class Analysis extends Action {
	private Set criteriaSet;

	private StorableObjectDatabase analysisDatabase;

	public Analysis(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.retrieve(this);
		}
		catch (IllegalDataException ide){
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Analysis(Analysis_Transferable at) throws CreateObjectException {
		super(new Identifier(at.id),
					new Date(at.created),
					new Date(at.modified),
					new Identifier(at.creator_id),
					new Identifier(at.modifier_id),
					new Identifier(at.type_id),
					new Identifier(at.monitored_element_id));
		try {
			this.criteriaSet = new Set(new Identifier(at.criteria_set_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		catch (ObjectNotFoundException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		this.analysisDatabase = MeasurementDatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Analysis(Identifier id,
									 Identifier creatorId,
									 Identifier typeId,
									 Identifier monitoredElementId,
									 Set criteriaSet) throws CreateObjectException {
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
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Analysis_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creatorId.getTransferable(),
																			(Identifier_Transferable)super.modifierId.getTransferable(),
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
																Identifier monitoredElementId,
																Set criteriaSet) throws CreateObjectException {
		return new Analysis(id,
												creatorId,
												typeId,
												monitoredElementId,
												criteriaSet);
	}
}