/*
 * $Id: Modeling.java,v 1.19 2004/12/09 12:47:20 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingSort;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.19 $, $Date: 2004/12/09 12:47:20 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Modeling extends Action {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3544387028533065264L;
	private String name;
	private Identifier schemePathId;
	private MeasurementType measurementType;
	private Set argumentSet;
	
	private int sort;

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
					   Identifier schemePathId,
					   Identifier monitoredElementId,
					   String name,
					   Set argumentSet,
					   int sort){
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.schemePathId = schemePathId;
		this.monitoredElementId = monitoredElementId;
		this.name = name;
		this.argumentSet = argumentSet;
		this.sort = sort;		
		
		this.modelingDatabase = MeasurementDatabaseContext.testDatabase;

		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */

	public static Modeling createInstance(Identifier creatorId,
										  Identifier schemePathId,
										  Identifier monitoredElementId,
										  String name,
										  Set argumentSet,
										  ModelingSort sort) throws CreateObjectException{
		if (creatorId == null || schemePathId == null || monitoredElementId == null || 
				name == null || argumentSet == null || sort == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new Modeling(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_ENTITY_CODE),
				creatorId,
				schemePathId,
				monitoredElementId,
				name,
				argumentSet,
				sort.value());
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Modeling.createInstance | cannot generate identifier ", e);
		}
		
	}
	
	public Modeling(Modeling_Transferable mt) throws CreateObjectException {
			super(mt.header,
				  null,
				  null);
		this.name = mt.name;
		this.sort = mt.sort.value();
		
		try {
			switch(this.sort){
				case ModelingSort._MODELINGSORT_MODELING:
					this.schemePathId = new Identifier(mt.scheme_path_id);
					break;
				case ModelingSort._MODELINGSORT_PREDICTION:
					this.monitoredElementId = new Identifier(mt.monitored_element_id);
					break;
				default:
					throw new CreateObjectException("unsupported ModelingSort = " + this.sort);
			}
			
			this.measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.measurement_type_id), true);
			
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
	public Result createResult(Identifier creatorId,
								Measurement measurement,
								AlarmLevel alarmLevel,
								SetParameter[] parameters) throws CreateObjectException {
		throw new UnsupportedOperationException("method isn't support");
	}

	public Result createResult(Identifier creatorId,		
							   SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(creatorId,
			this,
			ResultSort.RESULT_SORT_MODELING,
			parameters);
	}

	public MeasurementType getMeasurementType() {
		return this.measurementType;
	}

	public Object getTransferable() {
		return new Modeling_Transferable(super.getHeaderTransferable(),
										 new String(this.name),
										 (this.schemePathId != null) ? (Identifier_Transferable)this.schemePathId.getTransferable() : (new Identifier_Transferable("")),
										 (this.monitoredElementId != null) ? (Identifier_Transferable)this.monitoredElementId.getTransferable() : (new Identifier_Transferable("")),
										 (Identifier_Transferable)this.measurementType.getId().getTransferable(),
										 (Identifier_Transferable)this.argumentSet.getId().getTransferable(),
										 ModelingSort.from_int(this.sort));
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  String name,
											  Identifier monitoredElementId,
											  Identifier schemePathId,
											  MeasurementType measurementType,
											  Set argumentSet,
											  int sort) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId);
		this.name = name;
		this.monitoredElementId = monitoredElementId;
		this.schemePathId = schemePathId;
		this.measurementType = measurementType;
		this.argumentSet = argumentSet;
		this.sort = sort;
	}


	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.created);
		hashCodeGenerator.addObject(this.creatorId);
		hashCodeGenerator.addObject(this.modified);
		hashCodeGenerator.addObject(this.modifierId);
		hashCodeGenerator.addObject(this.name);
		hashCodeGenerator.addObject(this.monitoredElementId);
		hashCodeGenerator.addObject(this.schemePathId);
		hashCodeGenerator.addObject(this.measurementType);
		hashCodeGenerator.addObject(this.argumentSet);
		hashCodeGenerator.addInt(this.sort);
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
					( ((modeling.monitoredElementId == null) && (this.monitoredElementId == null) ) 
							|| (modeling.monitoredElementId.equals(this.monitoredElementId)) ) &&
					(modeling.schemePathId.equals(this.schemePathId)) &&					 
					(modeling.argumentSet.equals(this.argumentSet) &&
					(modeling.sort == this.sort)
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
    
	public Identifier getSchemePathId() {
		return this.schemePathId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}
	
	public ModelingSort getSort() {
		return ModelingSort.from_int(this.sort);
	}	
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		
        if (this.argumentSet != null)
			dependencies.add(this.argumentSet);
        
		dependencies.add(this.measurementType);
		return dependencies;
	}
}
