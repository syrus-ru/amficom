/*
 * $Id: KIS.java,v 1.35 2004/11/25 15:41:11 bob Exp $
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

import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.35 $, $Date: 2004/11/25 15:41:11 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class KIS extends DomainMember {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257281422661466166L;

	protected static final int RETRIEVE_MONITORED_ELEMENTS = 1;

	private Identifier equipmentId;
	private Identifier mcmId;
	private String name;
	private String description;
	private String hostname;
	private short tcpPort;
	private List measurementPortIds;	//List <MeasurementPort>
	private List characteristics;
	private StorableObjectDatabase kisDatabase;

	public KIS(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.measurementPortIds = new LinkedList();
		this.characteristics = new LinkedList();
		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public KIS(KIS_Transferable kt) throws CreateObjectException {
		super(kt.header, new Identifier(kt.domain_id));

		this.equipmentId = new Identifier(kt.equipment_id);
		this.mcmId = new Identifier(kt.mcm_id);
		this.name = kt.name;
		this.description = kt.description;
		this.hostname = kt.hostname;
		this.tcpPort = kt.tcp_port;

		this.measurementPortIds = new ArrayList(kt.measurement_port_ids.length);
		for (int i = 0; i < kt.measurement_port_ids.length; i++)
			this.measurementPortIds.add(new Identifier(kt.measurement_port_ids[i]));
        
        try {
            this.characteristics = new ArrayList(kt.characteristic_ids.length);
            for (int i = 0; i < kt.characteristic_ids.length; i++)
                this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(kt.characteristic_ids[i]), true));
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae);
        }

	}

	protected KIS(Identifier id,
								Identifier creatorId,
								Identifier domainId,
								String name,
								String description,
								String hostname,
								short tcpPort,
								Identifier equipmentId,
								Identifier mcmId) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
		this.measurementPortIds = new LinkedList();
		this.characteristics = new LinkedList();
		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
	}

	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param mcmId
	 * @return
	 */
	public static KIS createInstance(Identifier id,
																	 Identifier creatorId,
																	 Identifier domainId,
																	 String name,
																	 String description,
																	 String hostname,
																	 short tcpPort,
																	 Identifier equipmentId,
																	 Identifier mcmId) {
		return new KIS(id,
									 creatorId,
									 domainId,
									 name,
									 description,
									 hostname,
									 tcpPort,
									 equipmentId,
									 mcmId);
	}

	public static KIS getInstance(KIS_Transferable kt) throws CreateObjectException{
		
		KIS kis = new KIS(kt);
		
		kis.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			if (kis.kisDatabase != null)
				kis.kisDatabase.insert(kis);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return kis;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] mportIds = new Identifier_Transferable[this.measurementPortIds.size()];
		for (Iterator iterator = this.measurementPortIds.iterator(); iterator.hasNext();)
			mportIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
		
        i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        
		return new KIS_Transferable(super.getHeaderTransferable(),
																(Identifier_Transferable)super.domainId.getTransferable(),
																new String(this.name),
																new String(this.description),
																this.hostname,
																this.tcpPort,
																(Identifier_Transferable)this.equipmentId.getTransferable(),
																(Identifier_Transferable)this.mcmId.getTransferable(),
                                                                charIds,
																mportIds);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public String getHostName(){
		return this.hostname;
	}

	public short getTCPPort(){
		return this.tcpPort;
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	public Identifier getMCMId() {
		return this.mcmId;
	}

	public List getMeasurementPortIds() {
		return this.measurementPortIds;
	}

	public List retrieveMonitoredElements() throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (List)this.kisDatabase.retrieveObject(this, RETRIEVE_MONITORED_ELEMENTS, null);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						String name,
																						String description,
																						String hostname,
																						short tcpPort,
																						Identifier equipmentId,
																						Identifier mcmId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
	}

	protected synchronized void setMeasurementPortIds(List measurementPortIds) {
		this.measurementPortIds.clear();
        if (measurementPortIds != null)
                this.measurementPortIds.addAll(measurementPortIds);
        super.currentVersion = super.getNextVersion();
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.equipmentId);
		dependencies.add(this.mcmId);
		dependencies.addAll(this.measurementPortIds);
        dependencies.addAll(this.characteristics);
		return dependencies;
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
