/*
 * $Id: ParameterType.java,v 1.23 2004/11/04 08:51:52 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.23 $, $Date: 2004/11/04 08:51:52 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class ParameterType extends StorableObjectType {
	private String name;
	private int sort;

	private StorableObjectDatabase parameterTypeDatabase;
	
	protected static final String ID_NAME = "name"+KEY_VALUE_SEPERATOR;
	protected static final String ID_SORT = "sort"+KEY_VALUE_SEPERATOR;

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
		super(new Identifier(ptt.id),
					new Date(ptt.created),
					new Date(ptt.modified),
					new Identifier(ptt.creator_id),
					new Identifier(ptt.modifier_id),
					new String(ptt.codename),
					new String(ptt.description));
		this.name = new String(ptt.name);
		this.sort = ptt.sort.value();
	}

	protected ParameterType(Identifier id,
												Identifier creatorId,
												String codename,
												String description,
												String name,
												int sort) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		super.description = description;
		this.name = name;
		this.sort = sort;
		super.currentVersion = super.getNextVersion();
		
		this.parameterTypeDatabase = MeasurementDatabaseContext.parameterTypeDatabase;
	}
		
	/**
	 * create new instance for client
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @return
	 */
	public static ParameterType createInstance(Identifier id,
																						 Identifier creatorId,
																						 String codename,
																						 String description,
																						 String name) {
		return new ParameterType(id,
														 creatorId,
														 codename,
														 description,
														 name,
														 DataType._DATA_TYPE_DATA);
	}
	
	/**
	 * create new instance for client
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param sort {@link ParameterTypeSort}
	 * @return
	 */
	public static ParameterType createInstance(Identifier id,
												 Identifier creatorId,
												 String codename,
												 String description,
												 String name,
												 DataType sort) {
		return new ParameterType(id,
						 creatorId,
						 codename,
						 description,
						 name,
						 sort.value());
	}

	
	public static ParameterType getInstance(ParameterType_Transferable ptt) throws CreateObjectException {
		ParameterType parameterType = new ParameterType(ptt);
		
		parameterType.parameterTypeDatabase = MeasurementDatabaseContext.parameterTypeDatabase;
		try {
			parameterType.parameterTypeDatabase.insert(parameterType);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		
		return parameterType;
	}

    public short getEntityCode() {
        return ObjectEntities.PARAMETERTYPE_ENTITY_CODE;
    }
    
    public Object getTransferable() {
		return new ParameterType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																					super.created.getTime(),
																					super.modified.getTime(),
																					(Identifier_Transferable)super.creatorId.getTransferable(),
																					(Identifier_Transferable)super.modifierId.getTransferable(),
																					new String(super.codename),
																					(super.description != null) ? (new String(super.description)) : "",
																					new String(this.name),
																					DataType.from_int(this.sort));
	}
	

	public String getName() {
		return this.name;
	}
	
	public DataType getSort(){
		return DataType.from_int(this.sort);
	}
	
	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description,
																						String name,
																						int sort) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
		this.name = name;
		this.sort = sort;
	}
	/**
	 * client setter for name 
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.currentVersion = super.getNextVersion();
		this.name = name;
	}
	
	
	public boolean equals(Object obj) {
		boolean equals = (obj==this);
		if ((!equals)&&(obj instanceof ParameterType)){
			ParameterType type = (ParameterType)obj;
			if ((this.id.equals(type.id))&&
				 HashCodeGenerator.equalsDate(this.created,type.created) &&
				 (this.creatorId.equals(type.creatorId))&&
				 HashCodeGenerator.equalsDate(this.modified,type.modified) &&
				 (this.modifierId.equals(type.modifierId))&&
				 (this.codename.equals(type.codename))&&
				 (this.description.equals(type.description))&&
				 (this.name.equals(type.name))&&
				 (this.sort ==  type.sort))
				 equals = true;
		}
		return equals;
	}
	
	
	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.created);
		hashCodeGenerator.addObject(this.creatorId);
		hashCodeGenerator.addObject(this.modified);
		hashCodeGenerator.addObject(this.modifierId);
		hashCodeGenerator.addObject(this.codename);
		hashCodeGenerator.addObject(this.description);
		hashCodeGenerator.addObject(this.name);
		hashCodeGenerator.addInt(this.sort);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;

	}
	
	
	public String toString() {
		String str = getClass().getName()+EOSL
					 + ID+this.id + EOSL
					 + ID_CREATED + this.created.toString()+ EOSL		
					 + ID_CREATOR_ID + this.creatorId.toString()+ EOSL
					 + ID_MODIFIED + this.modified.toString() + EOSL
					 + ID_MODIFIER_ID + this.modifierId.toString() + EOSL
					 + TypedObject.ID_CODENAME + this.codename+ EOSL
					 + TypedObject.ID_DESCRIPTION + this.description + EOSL
					 + ID_NAME + this.name + EOSL
					 + ID_SORT + this.sort;
					 
		return str;
	}
	
	protected List getDependencies() {
		return Collections.EMPTY_LIST;
	}
}
