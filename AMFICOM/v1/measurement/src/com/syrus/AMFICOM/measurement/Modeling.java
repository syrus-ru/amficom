/*
 * $Id: Modeling.java,v 1.7 2004/10/13 09:49:50 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.7 $, $Date: 2004/10/13 09:49:50 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Modeling extends Action {
	
	private String name;
	private String schemePathId;
	private MeasurementType measurementType;
	private Set argumentSet;

	private StorableObjectDatabase	modelingDatabase;

	public Modeling(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.modelingDatabase = MeasurementDatabaseContext.modelingDatabase;
		try {
			this.modelingDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
	
	protected Modeling(Identifier id,
							 Identifier creatorId,
							 String schemePathId,
							 String name,
							 Set argumentSet){
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.schemePathId = schemePathId;
		this.name = name;
		this.argumentSet = argumentSet;
		
		this.modelingDatabase = MeasurementDatabaseContext.testDatabase;

		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * create new instance for client
	 */

	public static Modeling createInstance(Identifier id,
																		Identifier creatorId,
																		String schemePathId,
																		String name,
																		Set argumentSet){
		return new Modeling(id,
										creatorId,
										schemePathId,
										name,
										argumentSet);
		
	}
	
	public Modeling(Modeling_Transferable mt) throws CreateObjectException {
			super(new Identifier(mt.id),
					new Date(mt.created),
					new Date(mt.modified),
					new Identifier(mt.creator_id),
					new Identifier(mt.modifier_id),
					null,
					null
					);
		this.schemePathId = mt.scheme_path_id;
		this.name = mt.name;
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		try {
			this.measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.measurement_type_id), true);
			/**
			 * @todo when change DB Identifier model ,change identifier_string to
			 *       identifier_code
			 */
			this.argumentSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.argument_set_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}	
	}
	
	public static Modeling getInstance(Modeling_Transferable mt) throws CreateObjectException {
		Modeling test = new Modeling(mt);
		
		test.modelingDatabase = MeasurementDatabaseContext.testDatabase;		
		try {
			if (test.modelingDatabase != null)
				test.modelingDatabase.insert(test);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		
		return test;
	}
	
	/** 
	 * @deprecated as unsupport method
	 */
	public Result createResult(	Identifier id,
								Identifier creatorId,
								Measurement measurement,
								AlarmLevel alarmLevel,
								SetParameter[] parameters) throws CreateObjectException {
		throw new UnsupportedOperationException("method isn't support");
	}

	public Result createResult(Identifier id,
								 Identifier creatorId,		
								 SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(id,
												 creatorId,
												 this,
												 ResultSort.RESULT_SORT_MODELING,
												 parameters);
	}

	public MeasurementType getMeasurementType() {
		return this.measurementType;
	}

	public Object getTransferable() {
		return new Modeling_Transferable((Identifier_Transferable)this.id.getTransferable(),
																 super.created.getTime(),
																 super.modified.getTime(),
																 (Identifier_Transferable)super.creatorId.getTransferable(),
																 (Identifier_Transferable)super.modifierId.getTransferable(),
																 new String(this.name),																 
																 this.schemePathId,
																 (Identifier_Transferable)this.measurementType.getId().getTransferable(),
																 (Identifier_Transferable)this.argumentSet.getId().getTransferable()
																 );
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String name,
																						String schemePathId,
																						MeasurementType measurementType,
																						Set argumentSet) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.name = name;
		this.schemePathId = schemePathId;
		this.measurementType = measurementType;
		this.argumentSet = argumentSet;
	}


	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.created);
		hashCodeGenerator.addObject(this.creatorId);
		hashCodeGenerator.addObject(this.modified);
		hashCodeGenerator.addObject(this.modifierId);
		hashCodeGenerator.addObject(this.name);
		hashCodeGenerator.addObject(this.schemePathId);
		hashCodeGenerator.addObject(this.measurementType);
		hashCodeGenerator.addObject(this.argumentSet);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;
	}
	
	public boolean equals(Object obj) {
		boolean equals = (this == obj);		
		if ((!equals) && (obj instanceof Modeling)){
			Modeling modeling = (Modeling)obj;
			if (	(modeling.id.equals(this.id)) &&
					HashCodeGenerator.equalsDate(this.created,modeling.created) &&
					(this.creatorId.equals(modeling.creatorId))&&
					HashCodeGenerator.equalsDate(this.modified,modeling.modified) &&
					(this.modifierId.equals(modeling.modifierId))&&					
					( ((modeling.name == null) && (this.name == null) ) 
							|| (modeling.name.equals(this.name)) ) &&					
					(modeling.getMeasurementType().equals(getMeasurementType())) &&
					(modeling.schemePathId.equals(this.schemePathId)) &&					 
					(modeling.argumentSet.equals(modeling.argumentSet)
					))
					equals = true;
		}
		return equals;
	}

    public short getEntityCode() {
        return ObjectEntities.MODELING_ENTITY_CODE;
    }
    
    public Set getArgumentSet() {
		return this.argumentSet;
	}
	public String getSchemePathId() {
		return this.schemePathId;
	}
	public String getName() {
		return this.name;
	}
	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}
}
