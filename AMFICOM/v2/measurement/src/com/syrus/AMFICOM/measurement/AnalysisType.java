package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

public class AnalysisType extends ActionType {
	private ArrayList in_parameter_types;
	private ArrayList criteria_parameter_types;
	private ArrayList out_parameter_types;

	private StorableObject_Database analysisTypeDatabase;

	public AnalysisType(Identifier id) throws RetrieveObjectException {
		super(id);

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public AnalysisType(AnalysisType_Transferable att) throws CreateObjectException {
		super(new Identifier(att.id),
					new Date(att.created),
					new Date(att.modified),
					new Identifier(att.creator_id),
					new Identifier(att.modifier_id),
					new String(att.codename),
					new String(att.description));

		this.in_parameter_types = new ArrayList(att.in_parameter_types.length);
		for (int i = 0; i < att.in_parameter_types.length; i++)
			this.in_parameter_types.add(new Identifier(att.in_parameter_types[i]));

		this.criteria_parameter_types = new ArrayList(att.criteria_parameter_types.length);
		for (int i = 0; i < att.criteria_parameter_types.length; i++)
			this.criteria_parameter_types.add(new Identifier(att.criteria_parameter_types[i]));

		this.out_parameter_types = new ArrayList(att.out_parameter_types.length);
		for (int i = 0; i < att.out_parameter_types.length; i++)
			this.out_parameter_types.add(new Identifier(att.out_parameter_types[i]));

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		Identifier_Transferable[] in_par_types = new Identifier_Transferable[this.in_parameter_types.size()];
		int i = 0;
		for (Iterator iterator = this.in_parameter_types.iterator(); iterator.hasNext();)
			in_par_types[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] criteria_par_types = new Identifier_Transferable[this.criteria_parameter_types.size()];
		i = 0;
		for (Iterator iterator = this.criteria_parameter_types.iterator(); iterator.hasNext();)
			criteria_par_types[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] out_par_types = new Identifier_Transferable[this.out_parameter_types.size()];
		i = 0;
		for (Iterator iterator = this.in_parameter_types.iterator(); iterator.hasNext();)
			in_par_types[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new AnalysisType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																				 super.created.getTime(),
																				 super.modified.getTime(),
																				 (Identifier_Transferable)super.creator_id.getTransferable(),
																				 (Identifier_Transferable)super.modifier_id.getTransferable(),
																				 new String(super.codename),
																				 new String(super.description),
																				 in_par_types,
																				 criteria_par_types,
																				 out_par_types);
	}

	public ArrayList getInParameterTypes() {
		return this.in_parameter_types;
	}

	public ArrayList getCriteriaParameterTypes() {
		return this.criteria_parameter_types;
	}

	public ArrayList getOutParameterTypes() {
		return this.out_parameter_types;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						String codename,
																						String description) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												codename,
												description);
	}

	protected synchronized void setParameterTypes(ArrayList in_parameter_types,
																								ArrayList criteria_parameter_types,
																								ArrayList out_parameter_types) {
		this.in_parameter_types = in_parameter_types;
		this.criteria_parameter_types = criteria_parameter_types;
		this.out_parameter_types = out_parameter_types;
	}
}