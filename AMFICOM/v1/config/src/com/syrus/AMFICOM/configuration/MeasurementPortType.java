/*
 * $Id: MeasurementPortType.java,v 1.12 2004/11/15 14:02:55 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;

/**
 * @version $Revision: 1.12 $, $Date: 2004/11/15 14:02:55 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MeasurementPortType extends StorableObjectType {
	static final long serialVersionUID = 7733425194674608181L;

	private String name;

	private StorableObjectDatabase measurementPortTypeDatabase;

	public MeasurementPortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			this.measurementPortTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPortType(MeasurementPortType_Transferable ptt) throws CreateObjectException {
		super(ptt.header,
			  new String(ptt.codename),
			  new String(ptt.description));		
		this.name = ptt.name;
	}
	
	protected MeasurementPortType(Identifier id,
								Identifier creatorId,
								String codename,
								String description,
								String name){
			super(id,
				  new Date(System.currentTimeMillis()),
				  new Date(System.currentTimeMillis()),
				  creatorId,
				  creatorId,
				  codename,
				  description);				
			this.name = name;
			this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @return
	 */
	public static MeasurementPortType createInstance(Identifier id,
													 Identifier creatorId,
													 String codename,
													 String description,
													 String name){
		return new MeasurementPortType(id,
									   creatorId,
									   codename,
									   description,
									   name);
	}
	
	public static MeasurementPortType getInstance(MeasurementPortType_Transferable mptt) throws CreateObjectException {
		MeasurementPortType measurementPortType = new MeasurementPortType(mptt);
		
		measurementPortType.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			if (measurementPortType.measurementPortTypeDatabase != null)
				measurementPortType.measurementPortTypeDatabase.insert(measurementPortType);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return measurementPortType;
	}
	
	public Object getTransferable() {
		return new MeasurementPortType_Transferable(super.getHeaderTransferable(),
													new String(super.codename),
													(super.description != null) ? (new String(super.description)) : "",
													(this.name != null) ? (new String(this.name)) : "");
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
		return Collections.EMPTY_LIST;
	}
}
