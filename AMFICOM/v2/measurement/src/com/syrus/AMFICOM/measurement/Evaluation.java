package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public class Evaluation extends Action {
	private Set threshold_set;
	private Set etalon;

	private StorableObject_Database evaluationDatabase;

	public Evaluation(Identifier id) throws RetrieveObjectException {
		super(id);

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Evaluation(Evaluation_Transferable et) throws CreateObjectException {
		super(new Identifier(et.id),
					new Date(et.created),
					new Date(et.modified),
					new Identifier(et.creator_id),
					new Identifier(et.modifier_id),
					new Identifier(et.type_id),
					new Identifier(et.monitored_element_id));
		try {
			this.threshold_set = new Set(new Identifier(et.threshold_set_id));
			this.etalon = new Set(new Identifier(et.etalon_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Evaluation(Identifier id,
										 Identifier creator_id,
										 Identifier type_id,
										 Set threshold_set,
										 Set etalon,
										 Identifier monitored_element_id) throws CreateObjectException {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creator_id,
					creator_id,
					type_id,
					monitored_element_id);
		this.threshold_set = threshold_set;
		this.etalon = etalon;

		this.evaluationDatabase = MeasurementDatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Evaluation_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			 super.created.getTime(),
																			 super.modified.getTime(),
																			 (Identifier_Transferable)super.creator_id.getTransferable(),
																			 (Identifier_Transferable)super.modifier_id.getTransferable(),
																			 (Identifier_Transferable)super.type_id.getTransferable(),
																			 (Identifier_Transferable)super.monitored_element_id.getTransferable(),
																			 (Identifier_Transferable)this.threshold_set.getId().getTransferable(),
																			 (Identifier_Transferable)this.etalon.getId().getTransferable());
	}

	public Set getThresholdSet() {
		return this.threshold_set;
	}

	public Set getEtalon() {
		return this.etalon;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier type_id,
																						Identifier monitored_element_id,
																						Set threshold_set,
																						Set etalon) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												type_id,
												monitored_element_id);
		this.threshold_set = threshold_set;
		this.etalon = etalon;
	}

	public Result createResult(Identifier id,
														 Identifier creator_id,
														 Measurement measurement,
														 AlarmLevel alarm_level,
														 Identifier[] parameter_ids,
														 Identifier[] parameter_type_ids,
														 byte[][] parameter_values) throws CreateObjectException {
		return Result.create(id,
												 creator_id,
												 measurement,
												 this,
												 ResultSort.RESULT_SORT_EVALUATION,
												 alarm_level,
												 parameter_ids,
												 parameter_type_ids,
												 parameter_values);
	}

	public static Evaluation create(Identifier id,
																	Identifier creator_id,
																	Identifier type_id,
																	Set threshold_set,
																	Set etalon,
																	Identifier monitored_element_id) throws CreateObjectException {
		return new Evaluation(id,
													creator_id,
													type_id,
													threshold_set,
													etalon,
													monitored_element_id);
	}
}