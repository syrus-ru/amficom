package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
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

		this.evaluationDatabase = StorableObject_DatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Evaluation(Evaluation_Transferable et) throws CreateObjectException {
		super(new Identifier(et.id), new Identifier(et.type_id), new Identifier(et.monitored_element_id));
		try {
			this.threshold_set = new Set(new Identifier(et.threshold_set_id));
			this.etalon = new Set(new Identifier(et.etalon_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}

		this.evaluationDatabase = StorableObject_DatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Evaluation(Identifier id,
										 Identifier type_id,
										 Set threshold_set,
										 Set etalon,
										 Identifier monitored_element_id) throws CreateObjectException {
		super(id, type_id, monitored_element_id);
		this.threshold_set = threshold_set;
		this.etalon = etalon;

		this.evaluationDatabase = StorableObject_DatabaseContext.evaluationDatabase;
		try {
			this.evaluationDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Evaluation_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			 (Identifier_Transferable)super.type_id.getTransferable(),
																			 (Identifier_Transferable)this.threshold_set.getId().getTransferable(),
																			 (Identifier_Transferable)this.etalon.getId().getTransferable(),
																			 (Identifier_Transferable)super.monitored_element_id.getTransferable());
	}

	public Set getThresholdSet() {
		return this.threshold_set;
	}

	public Set getEtalon() {
		return this.etalon;
	}

	protected synchronized void setAttributes(Identifier type_id,
															 Set threshold_set,
															 Set etalon,
															 Identifier monitored_element_id) {
		super.type_id = type_id;
		this.threshold_set = threshold_set;
		this.etalon = etalon;
		super.monitored_element_id = monitored_element_id;
	}

	public Result createResult(Identifier id,
														 Measurement measurement,
														 AlarmLevel alarm_level,
														 Identifier[] parameter_ids,
														 Identifier[] parameter_type_ids,
														 byte[][] parameter_values) throws CreateObjectException {
		return new Result(id,
											measurement,
											this,
											ResultSort.RESULT_SORT_EVALUATION,
											alarm_level,
											parameter_ids,
											parameter_type_ids,
											parameter_values);						
	}

	public static Evaluation create(Identifier id,
																	Identifier type_id,
																	Set threshold_set,
																	Set etalon,
																	Identifier monitored_element_id) throws CreateObjectException {
		return new Evaluation(id,
													type_id,
													threshold_set,
													etalon,
													monitored_element_id);
	}
}