package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;

public class ParameterType extends StorableObjectType {
	private String name;

	private StorableObjectDatabase parameterTypeDatabase;

	public ParameterType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.parameterTypeDatabase = MeasurementDatabaseContext.parameterTypeDatabase;
		try {
			this.parameterTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public ParameterType(ParameterType_Transferable ptt) throws CreateObjectException {
		super(new Identifier(ptt.id));
		this.codename = new String(ptt.codename);
		this.name = new String(ptt.name);
		this.description = new String(ptt.description);

		this.parameterTypeDatabase = MeasurementDatabaseContext.parameterTypeDatabase;
		try {
			this.parameterTypeDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private ParameterType(Identifier id,
												String codename,
												String name,
												String description) throws CreateObjectException {
		super(id);
		this.codename = codename;
		this.name = name;
		this.description = description;

		this.parameterTypeDatabase = MeasurementDatabaseContext.parameterTypeDatabase;
		try {
			this.parameterTypeDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}
	
	private ParameterType(Identifier id,
						 String name){
		//super(PoolId.getId(ObjectEntities.PARAMETERTYPE_ENTITY));
		super(id);
		setName(name);
	}
	
	/**
	 * create new instance for client
	 * @param id
	 * @param name
	 * @return
	 */
	public static ParameterType createInstance(Identifier id,
						 String name){
		return new ParameterType(id,name);
	}

	public Object getTransferable() {
		return new ParameterType_Transferable((Identifier_Transferable)this.id.getTransferable(),
																					new String(this.codename),
																					new String(this.name),
																					new String(this.description));
	}
	

	public String getName() {
		return this.name;
	}
	
	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description,
																						String name) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
		this.name = name;
	}
	/**
	 * client setter for name 
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.currentVersion = super.getNextVersion();
		this.name = name;
	}
}