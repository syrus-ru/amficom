/*
 * $Id: Modeling.java,v 1.4 2004/10/06 15:45:16 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.4 $, $Date: 2004/10/06 15:45:16 $
 * @author $Author: max $
 * @module measurement_v1
 */

public class Modeling extends StorableObject {
	
	private String name;
	private Identifier meId;
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
							 Identifier meId,
							 String name,
							 Set argumentSet){
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.meId = meId;
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
																		Identifier meId,
																		String name,
																		Set argumentSet){
		return new Modeling(id,
										creatorId,
										meId,
										name,
										argumentSet);
		
	}


	public Modeling(Modeling_Transferable mt) throws CreateObjectException {
			super(new Identifier(mt.id),
					new Date(mt.created),
					new Date(mt.modified),
					new Identifier(mt.creator_id),
					new Identifier(mt.modifier_id));
		this.name = mt.name;
		this.meId = new Identifier(mt.monitored_element_id);
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
																 (Identifier_Transferable)this.meId.getTransferable(),
																 (Identifier_Transferable)this.measurementType.getId().getTransferable(),
																 (Identifier_Transferable)this.argumentSet.getId().getTransferable()
																 );
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String name,
																						Identifier domainId,
																						MeasurementType measurementType,
																						Set argumentSet) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.name = name;
		this.meId = domainId;
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
		hashCodeGenerator.addObject(this.meId);
		hashCodeGenerator.addObject(this.measurementType);
		hashCodeGenerator.addObject(this.argumentSet);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;
	}
	
	public boolean equals(Object obj) {
		boolean equals = (this == obj);		
		if ((!equals) && (obj instanceof Modeling)){
			Modeling test = (Modeling)obj;
			if (	(test.id.equals(this.id)) &&
					HashCodeGenerator.equalsDate(this.created,test.created) &&
					(this.creatorId.equals(test.creatorId))&&
					HashCodeGenerator.equalsDate(this.modified,test.modified) &&
					(this.modifierId.equals(test.modifierId))&&					
					( ((test.name == null) && (this.name == null) ) 
							|| (test.name.equals(this.name)) ) &&					
					(test.getMeasurementType().equals(getMeasurementType())) &&
					(test.meId.equals(this.meId)) &&					 
					(test.argumentSet.equals(test.argumentSet)
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
	public Identifier getMonitoredElementId() {
		return this.meId;
	}
	public String getName() {
		return this.name;
	}
	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}
}
