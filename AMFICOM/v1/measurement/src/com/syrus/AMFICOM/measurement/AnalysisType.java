package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

public class AnalysisType extends ActionType {

	private List					inParameterTypes;
	private List					criteriaParameterTypes;
	private List					outParameterTypes;

	private StorableObjectDatabase	analysisTypeDatabase;

	public AnalysisType(Identifier id) throws RetrieveObjectException {
		super(id);

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.retrieve(this);
		} catch (Exception e) {
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

		this.inParameterTypes = new ArrayList(att.in_parameter_types.length);
		for (int i = 0; i < att.in_parameter_types.length; i++)
			this.inParameterTypes.add(new Identifier(att.in_parameter_types[i]));

		this.criteriaParameterTypes = new ArrayList(att.criteria_parameter_types.length);
		for (int i = 0; i < att.criteria_parameter_types.length; i++)
			this.criteriaParameterTypes.add(new Identifier(att.criteria_parameter_types[i]));

		this.outParameterTypes = new ArrayList(att.out_parameter_types.length);
		for (int i = 0; i < att.out_parameter_types.length; i++)
			this.outParameterTypes.add(new Identifier(att.out_parameter_types[i]));

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.insert(this);
		} catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		Identifier_Transferable[] inParTypes = new Identifier_Transferable[this.inParameterTypes.size()];
		int i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] criteriaParTypes = new Identifier_Transferable[this.criteriaParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.criteriaParameterTypes.iterator(); iterator.hasNext();)
			criteriaParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] outParTypes = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new AnalysisType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																				 super.created.getTime(),
																				 super.modified.getTime(),
																				 (Identifier_Transferable)super.creatorId.getTransferable(),
																				 (Identifier_Transferable)super.modifierId.getTransferable(),
																				 new String(super.codename),
																				 new String(super.description),
																				 inParTypes,
																				 criteriaParTypes,
																				 outParTypes);
	}

	public List getInParameterTypes() {
		return this.inParameterTypes;
	}

	public List getCriteriaParameterTypes() {
		return this.criteriaParameterTypes;
	}

	public List getOutParameterTypes() {
		return this.outParameterTypes;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
	}

	protected synchronized void setParameterTypes(List inParameterTypes,
																								List criteriaParameterTypes,
																								List outParameterTypes) {
		this.inParameterTypes = inParameterTypes;
		this.criteriaParameterTypes = criteriaParameterTypes;
		this.outParameterTypes = outParameterTypes;
	}
}