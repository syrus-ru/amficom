/*
 * $Id: MeasurementPort.java,v 1.17 2004/11/25 08:37:39 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
 * @version $Revision: 1.17 $, $Date: 2004/11/25 08:37:39 $
 * @author $Author: max $
 * @module configuration_v1
 */
public class MeasurementPort extends StorableObject implements TypedObject{
	static final long serialVersionUID = -5100885507408715167L;

	private MeasurementPortType type;
	
	private String name;
	private String description;
	
	private Identifier kisId;
	private Identifier portId;
	private List characteristics;
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
		super(mpt.header);

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
		try {
            this.characteristics = new ArrayList(mpt.characteristic_ids.length);
            for (int i = 0; i < mpt.characteristic_ids.length; i++)
                this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(mpt.characteristic_ids[i]), true));
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae);
        }
	}
	
	protected MeasurementPort(Identifier id,
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
		this.characteristics = new ArrayList();
		this.measurementPortDatabase = ConfigurationDatabaseContext.measurementPortDatabase;
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

	public static MeasurementPort getInstance(MeasurementPort_Transferable mpt) throws CreateObjectException {
		MeasurementPort measurementPort = new MeasurementPort(mpt);
		
		measurementPort.measurementPortDatabase = ConfigurationDatabaseContext.measurementPortDatabase;
		try {
			if (measurementPort.measurementPortDatabase != null)
				measurementPort.measurementPortDatabase.insert(measurementPort);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return measurementPort;
	}
	
	public Object getTransferable() {
		int i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        
		return new MeasurementPort_Transferable(super.getHeaderTransferable(),
												(Identifier_Transferable)this.type.getId().getTransferable(),
												new String(this.name),
												new String(this.description),
												(Identifier_Transferable)this.kisId.getTransferable(),
												(Identifier_Transferable)this.portId.getTransferable(),
                                                charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}


	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
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
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.add(this.kisId);
		dependencies.add(this.portId);
        dependencies.addAll(this.characteristics);
		return dependencies;
	}
	
	public void setName(String name) {
		this.currentVersion = super.getNextVersion();
		this.name = name;
	}
	
	public void setType(MeasurementPortType type) {
		this.currentVersion = super.getNextVersion();
		this.type = type;
	}
    
    public List getCharacteristics() {
        return this.characteristics;
    }
    
    public void setCharacteristics(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
        super.currentVersion = super.getNextVersion();
    }
}
