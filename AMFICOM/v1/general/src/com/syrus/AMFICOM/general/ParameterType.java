/*
 * $Id: ParameterType.java,v 1.9 2005/02/14 09:09:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2005/02/14 09:09:46 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class ParameterType extends StorableObjectType {
	private static final long serialVersionUID = 4050767108738528569L;

	private String name;
	private int dataType;

	private StorableObjectDatabase parameterTypeDatabase;

	public ParameterType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.parameterTypeDatabase = GeneralDatabaseContext.parameterTypeDatabase;
		try {
			this.parameterTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public ParameterType(ParameterType_Transferable ptt) {
		super(ptt.header,
			  new String(ptt.codename),
			  new String(ptt.description));
		this.name = new String(ptt.name);
		this.dataType = ptt.data_type.value();
	}

	protected ParameterType(Identifier id,
							Identifier creatorId,
							long version,
							String codename,
							String description,
							String name,
							int dataType) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.dataType = dataType;

		this.parameterTypeDatabase = GeneralDatabaseContext.parameterTypeDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param dataType {@link DataType}
	 * @throws CreateObjectException
	 */
	public static ParameterType createInstance(Identifier creatorId,
											   String codename,
											   String description,
											   String name,
											   DataType dataType) throws CreateObjectException {
		if (creatorId == null
				|| codename == null
				|| codename.length() == 0
				|| description == null
				|| name == null
				|| name.length() == 0
				|| dataType == null)
			throw new IllegalArgumentException("Argument is 'null'");		

		try {
			ParameterType parameterType = new ParameterType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					name,
					dataType.value());
			parameterType.changed = true;
			return parameterType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("ParameterType.createInstance | cannot generate identifier ", e);
		}
	}


  public Object getTransferable() {
		return new ParameterType_Transferable(super.getHeaderTransferable(),
											  new String(super.codename),
											  (super.description != null) ? (new String(super.description)) : "",
											  new String(this.name),
											  DataType.from_int(this.dataType));
	}

	public String getName() {
		return this.name;
	}
	
	protected void setName0(String name) {
		this.name = name;
	}

	/**
	 * client setter for name 
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.setName0(name);
		super.changed = true;
	}

	public DataType getDataType() {
		return DataType.from_int(this.dataType);
	}

	protected void setDataType0(DataType dataType) {
		this.dataType = dataType.value();
	}

	public void setDataType(DataType dataType) {
		this.setDataType0(dataType);
		super.changed = true;
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  String codename,
											  String description,
											  String name,
											  int dataType) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
		this.name = name;
		this.dataType = dataType;
	}

	public List getDependencies() {
		return Collections.EMPTY_LIST;
	}
}
