package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;

public class MeasurementType extends ActionType {

	private List					inParameterTypes;
	private List					outParameterTypes;

	private StorableObjectDatabase	measurementTypeDatabase;

	public MeasurementType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
		try {
			this.measurementTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public MeasurementType(MeasurementType_Transferable mtt) throws CreateObjectException {
		super(new Identifier(mtt.id),
					new Date(mtt.created),
					new Date(mtt.modified),
					new Identifier(mtt.creator_id),
					new Identifier(mtt.modifier_id),
					new String(mtt.codename),
					new String(mtt.description));

		this.inParameterTypes = new ArrayList(mtt.in_parameter_types.length);
		for (int i = 0; i < mtt.in_parameter_types.length; i++)
			this.inParameterTypes.add(new Identifier(mtt.in_parameter_types[i]));

		this.outParameterTypes = new ArrayList(mtt.out_parameter_types.length);
		for (int i = 0; i < mtt.out_parameter_types.length; i++)
			this.outParameterTypes.add(new Identifier(mtt.out_parameter_types[i]));

		this.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
		try {
			this.measurementTypeDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}
	
	/**
	 * client constructor
	 * @param id
	 * @param inParameterTypes
	 * @param outParameterTypes
	 */
	public MeasurementType(Identifier id,
						   List inParameterTypes,
						   List	outParameterTypes){
		//super(PoolId.getId(ObjectEntities.MEASUREMENTTYPE_ENTITY));
		super(id);
		setInParameterTypes(inParameterTypes);
		setOutParameterTypes(outParameterTypes);
	}

	public Object getTransferable() {
		Identifier_Transferable[] inParTypes = new Identifier_Transferable[this.inParameterTypes.size()];
		int i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] outParTypes = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypes[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new MeasurementType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																						super.created.getTime(),
																						super.modified.getTime(),
																						(Identifier_Transferable)super.creatorId.getTransferable(),
																						(Identifier_Transferable)super.modifierId.getTransferable(),
																						new String(super.codename),
																						new String(super.description),
																						inParTypes,
																						outParTypes);
	}

	public List getInParameterTypes() {
		return this.inParameterTypes;
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
																								List outParameterTypes) {
		this.inParameterTypes = inParameterTypes;
		this.outParameterTypes = outParameterTypes;
	}
	/**
	 * client setter for inParameterTypes
	 * @param inParameterTypes The inParameterTypes to set.
	 */
	public void setInParameterTypes(List inParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.inParameterTypes = inParameterTypes;
	}
	/**
	 * client setter for outParameterTypes
	 * @param outParameterTypes The outParameterTypes to set.
	 */
	public void setOutParameterTypes(List outParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.outParameterTypes = outParameterTypes;
	}
}