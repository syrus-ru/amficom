/*
 * $Id: ParameterType.java,v 1.34 2004/12/09 15:52:53 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.34 $, $Date: 2004/12/09 15:52:53 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class ParameterType extends StorableObjectType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 4050767108738528569L;
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

	public ParameterType(ParameterType_Transferable ptt) {
		super(ptt.header,
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
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @throws CreateObjectException
	 */
	public static ParameterType createInstance(Identifier creatorId,
											   String codename,
											   String description,
											   String name) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null || name == null || name.length() == 0)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new ParameterType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
				creatorId,
				codename,
				description,
				name,
				DataType._DATA_TYPE_DATA);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("ParameterType.createInstance | cannot generate identifier ", e);
		}
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param sort {@link DataType}
	 * @throws CreateObjectException
	 */
	public static ParameterType createInstance(Identifier creatorId,
											   String codename,
											   String description,
											   String name,
											   DataType sort) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null || name == null || name.length() == 0 || sort == null)
			throw new IllegalArgumentException("Argument is 'null'");		

		try {
			return new ParameterType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
							 creatorId,
							 codename,
							 description,
							 name,
							 sort.value());
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("ParameterType.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.parameterTypeDatabase != null)
				this.parameterTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

//	public static ParameterType getInstance(ParameterType_Transferable ptt) throws CreateObjectException {
//		ParameterType parameterType = new ParameterType(ptt);
//		
//		parameterType.parameterTypeDatabase = MeasurementDatabaseContext.parameterTypeDatabase;
//		try {
//			parameterType.parameterTypeDatabase.insert(parameterType);
//		}
//		catch (IllegalDataException e) {
//			throw new CreateObjectException(e.getMessage(), e);
//		}
//		
//		return parameterType;
//	}

    public short getEntityCode() {
        return ObjectEntities.PARAMETERTYPE_ENTITY_CODE;
    }
    
    public Object getTransferable() {
		return new ParameterType_Transferable(super.getHeaderTransferable(),
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
	
	public List getDependencies() {
		return Collections.EMPTY_LIST;
	}
}
