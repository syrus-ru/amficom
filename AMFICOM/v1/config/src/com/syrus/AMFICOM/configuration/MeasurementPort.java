/*
 * $Id: MeasurementPort.java,v 1.4 2004/08/23 20:48:15 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;

import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/23 20:48:15 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */
public class MeasurementPort extends StorableObject implements TypedObject{

	private MeasurementPortType type;
	
	private String name;
	private String description;
	
	private Identifier kisId;
	private Identifier portId;
	
	private StorableObjectDatabase measurementPortDatabase;
	
	public MeasurementPort(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.measurementPortDatabase = ConfigurationDatabaseContext.measurementPortDatabase;
		try {
			this.measurementPortDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPort(MeasurementPort_Transferable mpt) throws CreateObjectException {
		super(new Identifier(mpt.id),
					new Date(mpt.created),
					new Date(mpt.modified),
					new Identifier(mpt.creator_id),
					new Identifier(mpt.modifier_id));

		try {
			this.type = (MeasurementPortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(mpt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		
		this.name = new String(mpt.name);
		this.description = new String(mpt.description);
		
		this.kisId = new Identifier(mpt.kis_id);
		this.portId = new Identifier(mpt.port_id);

		
		this.measurementPortDatabase = ConfigurationDatabaseContext.measurementPortDatabase;
		try {
			this.measurementPortDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}
	
	private MeasurementPort(Identifier id,
							Identifier creatorId,
							MeasurementPortType type,
							String name,
							String description,	
							Identifier kisId,
							Identifier portId){
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param type
	 * @param name
	 * @param description
	 * @param kisId
	 * @param portId
	 * @return
	 */
	public static MeasurementPort createInstance(Identifier id,
													Identifier creatorId,
													MeasurementPortType type,
													String name,
													String description,	
													Identifier kisId,
													Identifier portId){
		return new MeasurementPort(id,
								   creatorId,
								   type,
								   name,
								   description,
								   kisId,
								   portId);
	}

	public Object getTransferable() {
		    
		return new MeasurementPort_Transferable((Identifier_Transferable)super.id.getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creatorId.getTransferable(),
																			(Identifier_Transferable)super.modifierId.getTransferable(),
																			(Identifier_Transferable)this.type.getId().getTransferable(),
																			new String(this.name),
																			new String(this.description),
																			(Identifier_Transferable)this.kisId.getTransferable(),
																			(Identifier_Transferable)this.portId.getTransferable());
	}

	public StorableObjectType getType() {
		return this.type;
	}


	public String getDescription() {
		return this.description;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Identifier getKISId(){
		return this.kisId;
	}

	public Identifier getPortId(){
		return this.portId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						MeasurementPortType type,
																						String name,
																						String description,	
																						Identifier kisId,
																						Identifier portId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
	}

}
