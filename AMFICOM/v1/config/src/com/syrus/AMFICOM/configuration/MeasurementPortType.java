/*
 * $Id: MeasurementPortType.java,v 1.8 2004/10/26 14:31:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;

/**
 * @version $Revision: 1.8 $, $Date: 2004/10/26 14:31:43 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MeasurementPortType extends StorableObjectType {

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
		super(new Identifier(ptt.id),
					new Date(ptt.created),
					new Date(ptt.modified),
					new Identifier(ptt.creator_id),
					new Identifier(ptt.modifier_id),
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
		return new MeasurementPortType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																								super.created.getTime(),
																								super.modified.getTime(),
																								(Identifier_Transferable)super.creatorId.getTransferable(),
																								(Identifier_Transferable)super.modifierId.getTransferable(),
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
}
