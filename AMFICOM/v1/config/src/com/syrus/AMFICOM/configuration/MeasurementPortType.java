/*
 * $Id: MeasurementPortType.java,v 1.26 2004/12/28 12:45:28 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;

/**
 * @version $Revision: 1.26 $, $Date: 2004/12/28 12:45:28 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class MeasurementPortType extends StorableObjectType implements Characterized {
	private static final long serialVersionUID = 7733425194674608181L;

	private String name;
	private List characteristics;
	private StorableObjectDatabase measurementPortTypeDatabase;

	public MeasurementPortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new LinkedList();
		this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			this.measurementPortTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPortType(MeasurementPortType_Transferable mptt) throws CreateObjectException {
		super(mptt.header,
			  new String(mptt.codename),
			  new String(mptt.description));		
		this.name = mptt.name;
		try {
			this.characteristics = new ArrayList(mptt.characteristic_ids.length);
			for (int i = 0; i < mptt.characteristic_ids.length; i++)
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(mptt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
	}

	protected MeasurementPortType(Identifier id,
								Identifier creatorId,
								String codename,
								String description,
								String name) {
			super(id,
				  new Date(System.currentTimeMillis()),
				  new Date(System.currentTimeMillis()),
				  creatorId,
				  creatorId,
				  codename,
				  description);				
			this.name = name;
			this.characteristics = new LinkedList();

			super.currentVersion = super.getNextVersion();

			this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
	}
	
	/**
	 * create new instance for client 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static MeasurementPortType createInstance(Identifier creatorId,
													 String codename,
													 String description,
													 String name) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new MeasurementPortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE),
										   creatorId,
										   codename,
										   description,
										   name);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MeasurementPortType.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.measurementPortTypeDatabase != null)
				this.measurementPortTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

//	public static MeasurementPortType getInstance(MeasurementPortType_Transferable mptt) throws CreateObjectException {
//		MeasurementPortType measurementPortType = new MeasurementPortType(mptt);
//		
//		measurementPortType.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
//		try {
//			if (measurementPortType.measurementPortTypeDatabase != null)
//				measurementPortType.measurementPortTypeDatabase.insert(measurementPortType);
//		}
//		catch (IllegalDataException ide) {
//			throw new CreateObjectException(ide.getMessage(), ide);
//		}
//		
//		return measurementPortType;
//	}
	
	public Object getTransferable() {
		int i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        return new MeasurementPortType_Transferable(super.getHeaderTransferable(),
													new String(super.codename),
													(super.description != null) ? (new String(super.description)) : "",
													(this.name != null) ? (new String(this.name)) : "",
                                                    charIds);
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
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.currentVersion = super.getNextVersion();
		this.name = name;
	}	

	public List getDependencies() {        
        return Collections.unmodifiableList(this.characteristics);
    }
    
	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null){
			this.characteristics.add(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}
	
	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null){
			this.characteristics.remove(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public List getCharacteristics() {
        return Collections.unmodifiableList(this.characteristics);
    }
    
    protected void setCharacteristics0(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
    }
    
    public void setCharacteristics(final List characteristics) {
        this.setCharacteristics0(characteristics);
        super.currentVersion = super.getNextVersion();
    }
}
