package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public class Analysis extends Action {
	private Set criteria_set;

	private StorableObject_Database analysisDatabase;

	public Analysis(Identifier id) throws RetrieveObjectException {
		super(id);

		this.analysisDatabase = StorableObject_DatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Analysis(Analysis_Transferable at) throws CreateObjectException, RetrieveObjectException {
		super(new Identifier(at.id), new Identifier(at.type_id), new Identifier(at.monitored_element_id));
		this.criteria_set = new Set(new Identifier(at.criteria_set_id));

		this.analysisDatabase = StorableObject_DatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Analysis(Identifier id,
									 Identifier type_id,
									 Set criteria_set,
									 Identifier monitored_element_id) throws CreateObjectException {
		super(id, type_id, monitored_element_id);
		this.criteria_set = criteria_set;

		this.analysisDatabase = StorableObject_DatabaseContext.analysisDatabase;
		try {
			this.analysisDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Analysis_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																		 (Identifier_Transferable)super.type_id.getTransferable(),
																		 (Identifier_Transferable)this.criteria_set.getId().getTransferable(),
																		 (Identifier_Transferable)super.monitored_element_id.getTransferable());
	}

	public Set getCriteriaSet() {
		return this.criteria_set;
	}

	protected synchronized void setAttributes(Identifier type_id,
																						Set criteria_set,
																						Identifier monitored_element_id) {
		super.type_id = type_id;
		this.criteria_set = criteria_set;
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
											ResultSort.RESULT_SORT_ANALYSIS,
											alarm_level,
											parameter_ids,
											parameter_type_ids,
											parameter_values);						
	}

	public static Analysis create(Identifier id,
																Identifier type_id,
																Set criteria_set,
																Identifier monitored_element_id) throws CreateObjectException {
		return new Analysis(id,
												type_id,
												criteria_set,
												monitored_element_id);
	}
}