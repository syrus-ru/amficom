package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;

public class EvaluationType extends ActionType {

	private List					inParameterTypes;
	private List					thresholdParameterTypes;
	private List					etalonParameterTypes;
	private List					outParameterTypes;

	private StorableObjectDatabase	evaluationTypeDatabase;

	public EvaluationType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			this.evaluationTypeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public EvaluationType(EvaluationType_Transferable ett) throws CreateObjectException {
		super(new Identifier(ett.id), new Date(ett.created), new Date(ett.modified), new Identifier(ett.creator_id),
				new Identifier(ett.modifier_id), new String(ett.codename), new String(ett.description));

		this.inParameterTypes = new ArrayList(ett.in_parameter_types.length);
		for (int i = 0; i < ett.in_parameter_types.length; i++)
			this.inParameterTypes.add(new Identifier(ett.in_parameter_types[i]));

		this.thresholdParameterTypes = new ArrayList(ett.threshold_parameter_types.length);
		for (int i = 0; i < ett.threshold_parameter_types.length; i++)
			this.thresholdParameterTypes.add(new Identifier(ett.threshold_parameter_types[i]));

		this.etalonParameterTypes = new ArrayList(ett.etalon_parameter_types.length);
		for (int i = 0; i < ett.etalon_parameter_types.length; i++)
			this.etalonParameterTypes.add(new Identifier(ett.etalon_parameter_types[i]));

		this.outParameterTypes = new ArrayList(ett.out_parameter_types.length);
		for (int i = 0; i < ett.out_parameter_types.length; i++)
			this.outParameterTypes.add(new Identifier(ett.out_parameter_types[i]));

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			this.evaluationTypeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}
	
	/**
	 * client constructor
	 * @param id
	 * @param inParameterTypes
	 * @param thresholdParameterTypes
	 * @param etalonParameterTypes
	 * @param outParameterTypes
	 */
	public EvaluationType(Identifier id,
						  List etalonParameterTypes,
						  List inParameterTypes,
						  List outParameterTypes,
						  List thresholdParameterTypes) {
		super(id);
		//super(PoolId.getId(ObjectEntities.EVALUATIONTYPE_ENTITY));
		setInParameterTypes(inParameterTypes);
		this.thresholdParameterTypes = thresholdParameterTypes;
		this.etalonParameterTypes = etalonParameterTypes;
		this.outParameterTypes = outParameterTypes;
	}

	public Object getTransferable() {
		Identifier_Transferable[] inParTypes = new Identifier_Transferable[this.inParameterTypes.size()];
		int i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] thresholdParTypes = new Identifier_Transferable[this.thresholdParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.thresholdParameterTypes.iterator(); iterator.hasNext();)
			thresholdParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] etalonParTypes = new Identifier_Transferable[this.etalonParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.etalonParameterTypes.iterator(); iterator.hasNext();)
			etalonParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] outParTypes = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new EvaluationType_Transferable((Identifier_Transferable) super.id.getTransferable(), super.created
				.getTime(), super.modified.getTime(), (Identifier_Transferable) super.creatorId.getTransferable(),
												(Identifier_Transferable) super.modifierId.getTransferable(),
												new String(super.codename), new String(super.description), inParTypes,
												thresholdParTypes, etalonParTypes, outParTypes);
	}

	public List getInParameterTypes() {
		return this.inParameterTypes;
	}

	public List getThresholdParameterTypes() {
		return this.thresholdParameterTypes;
	}

	public List getEtalonParameterTypes() {
		return this.etalonParameterTypes;
	}

	public List getOutParameterTypes() {
		return this.outParameterTypes;
	}

	protected synchronized void setAttributes(	Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												String codename,
												String description) {
		super.setAttributes(created, modified, creatorId, modifierId, codename, description);
	}

	protected synchronized void setParameterTypes(	List inParameterTypes,
													List thresholdParameterTypes,
													List etalonParameterTypes,
													List outParameterTypes) {
		this.inParameterTypes = inParameterTypes;
		this.thresholdParameterTypes = thresholdParameterTypes;
		this.etalonParameterTypes = etalonParameterTypes;
		this.outParameterTypes = outParameterTypes;
	}

	/**
	 * client setter for etalonParameterTypes
	 * 
	 * @param etalonParameterTypes
	 *            The etalonParameterTypes to set.
	 */
	public void setEtalonParameterTypes(List etalonParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.etalonParameterTypes = etalonParameterTypes;
	}

	/**
	 * client setter for inParameterTypes
	 * 
	 * @param inParameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setInParameterTypes(List inParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.inParameterTypes = inParameterTypes;
	}

	/**
	 * client setter for outParameterTypes
	 * 
	 * @param outParameterTypes
	 *            The outParameterTypes to set.
	 */
	public void setOutParameterTypes(List outParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.outParameterTypes = outParameterTypes;
	}

	/**
	 * client setter for thresholdParameterTypes
	 * 
	 * @param thresholdParameterTypes
	 *            The thresholdParameterTypes to set.
	 */
	public void setThresholdParameterTypes(List thresholdParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.thresholdParameterTypes = thresholdParameterTypes;
	}
}