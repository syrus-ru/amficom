/*
 * $Id: ParameterType.java,v 1.14 2005/04/04 16:04:41 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/04/04 16:04:41 $
 * @author $Author: bass $
 * @module general_v1
 */

public class ParameterType extends StorableObjectType {
	private static final long serialVersionUID = 4050767108738528569L;

	private String name;
	private int dataType;

	private StorableObjectDatabase parameterTypeDatabase;

	public ParameterType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();
		try {
			this.parameterTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public ParameterType(ParameterType_Transferable ptt) {
		try {
			this.fromTransferable(ptt);
		} catch (CreateObjectException e) {
			Log.debugException(e, Log.WARNING);
		}
		this.parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();
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

		this.parameterTypeDatabase = GeneralDatabaseContext.getParameterTypeDatabase();
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

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		ParameterType_Transferable ptt = (ParameterType_Transferable) transferable;
		super.fromTransferable(ptt.header, ptt.codename, ptt.description);
		this.name = ptt.name;
		this.dataType = ptt.data_type.value();
	}
	
  public IDLEntity getTransferable() {
		return new ParameterType_Transferable(super.getHeaderTransferable(),
											  super.codename,
											  super.description != null ? super.description : "",
											  this.name,
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

	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}
}
