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
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;

public class EvaluationType extends ActionType {
	private ArrayList in_parameter_types;
	private ArrayList threshold_parameter_types;
	private ArrayList etalon_parameter_types;
	private ArrayList out_parameter_types;

	private StorableObject_Database evaluationTypeDatabase;

	public EvaluationType(Identifier id) throws RetrieveObjectException {
		super(id);

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			this.evaluationTypeDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public EvaluationType(EvaluationType_Transferable ett) throws CreateObjectException {
		super(new Identifier(ett.id),
					new Date(ett.created),
					new Date(ett.modified),
					new Identifier(ett.creator_id),
					new Identifier(ett.modifier_id),
					new String(ett.codename),
					new String(ett.description));

		this.in_parameter_types = new ArrayList(ett.in_parameter_types.length);
		for (int i = 0; i < ett.in_parameter_types.length; i++)
			this.in_parameter_types.add(new Identifier(ett.in_parameter_types[i]));

		this.threshold_parameter_types = new ArrayList(ett.threshold_parameter_types.length);
		for (int i = 0; i < ett.threshold_parameter_types.length; i++)
			this.threshold_parameter_types.add(new Identifier(ett.threshold_parameter_types[i]));

		this.etalon_parameter_types = new ArrayList(ett.etalon_parameter_types.length);
		for (int i = 0; i < ett.etalon_parameter_types.length; i++)
			this.etalon_parameter_types.add(new Identifier(ett.etalon_parameter_types[i]));

		this.out_parameter_types = new ArrayList(ett.out_parameter_types.length);
		for (int i = 0; i < ett.out_parameter_types.length; i++)
			this.out_parameter_types.add(new Identifier(ett.out_parameter_types[i]));

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			this.evaluationTypeDatabase.insert(this);
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

		Identifier_Transferable[] threshold_par_types = new Identifier_Transferable[this.threshold_parameter_types.size()];
		i = 0;
		for (Iterator iterator = this.threshold_parameter_types.iterator(); iterator.hasNext();)
			threshold_par_types[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] etalon_par_types = new Identifier_Transferable[this.etalon_parameter_types.size()];
		i = 0;
		for (Iterator iterator = this.etalon_parameter_types.iterator(); iterator.hasNext();)
			etalon_par_types[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] out_par_types = new Identifier_Transferable[this.out_parameter_types.size()];
		i = 0;
		for (Iterator iterator = this.in_parameter_types.iterator(); iterator.hasNext();)
			in_par_types[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new EvaluationType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																					 super.created.getTime(),
																					 super.modified.getTime(),
																					 (Identifier_Transferable)super.creator_id.getTransferable(),
																					 (Identifier_Transferable)super.modifier_id.getTransferable(),
																					 new String(super.codename),
																					 new String(super.description),
																					 in_par_types,
																					 threshold_par_types,
																					 etalon_par_types,
																					 out_par_types);
	}

	public ArrayList getInParameterTypes() {
		return this.in_parameter_types;
	}

	public ArrayList getThresholdParameterTypes() {
		return this.threshold_parameter_types;
	}

	public ArrayList getEtalonParameterTypes() {
		return this.etalon_parameter_types;
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
																								ArrayList threshold_parameter_types,
																								ArrayList etalon_parameter_types,
																	 ArrayList out_parameter_types) {
		this.in_parameter_types = in_parameter_types;
		this.threshold_parameter_types = threshold_parameter_types;
		this.etalon_parameter_types = etalon_parameter_types;
		this.out_parameter_types = out_parameter_types;
	}
}