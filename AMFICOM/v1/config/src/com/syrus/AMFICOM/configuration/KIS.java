/*
 * $Id: KIS.java,v 1.22 2004/11/02 12:42:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;

/**
 * @version $Revision: 1.22 $, $Date: 2004/11/02 12:42:43 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class KIS extends DomainMember implements TypedObject {
	
    protected static final int RETRIEVE_MONITORED_ELEMENTS = 1;

	private KISType type;
    private Identifier equipmentId;
	private Identifier mcmId;
	private String name;
	private String description;
	
	private List measurementPortIds;	//List <MeasurementPort>

	private StorableObjectDatabase kisDatabase;

	public KIS(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public KIS(KIS_Transferable kt) throws CreateObjectException {
		super(new Identifier(kt.id),
					new Date(kt.created),
					new Date(kt.modified),
					new Identifier(kt.creator_id),
					new Identifier(kt.modifier_id),
					new Identifier(kt.domain_id));
		this.equipmentId = new Identifier(kt.equipment_id);
		this.mcmId = new Identifier(kt.mcm_id);
		this.name = kt.name;
		this.description = kt.description;

		this.measurementPortIds = new ArrayList(kt.measurement_port_ids.length);
		for (int i = 0; i < kt.measurement_port_ids.length; i++)
			this.measurementPortIds.add(new Identifier(kt.measurement_port_ids[i]));
        
        try {
            this.type = (KISType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(kt.type_id), true);
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
                            KISType type,
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
		this.equipmentId = equipmentId;
        this.type = type;
		this.mcmId = mcmId;
		this.measurementPortIds = new ArrayList();
		
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
                                                                     KISType type,
																	 Identifier equipmentId,
																	 Identifier mcmId){
		return new KIS(id,
									 creatorId,
									 domainId,
									 name,
									 description,
                                     type,
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
		
		return new KIS_Transferable((Identifier_Transferable)super.id.getTransferable(),
																super.created.getTime(),
																super.modified.getTime(),
																(Identifier_Transferable)super.creatorId.getTransferable(),
																(Identifier_Transferable)super.modifierId.getTransferable(),
																(Identifier_Transferable)super.domainId.getTransferable(),
																new String(this.name),
																new String(this.description),
                                                                (Identifier_Transferable)this.type.getId().getTransferable(),
																(Identifier_Transferable)this.equipmentId.getTransferable(),
																(Identifier_Transferable)this.mcmId.getTransferable(),
																mportIds);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
    
    public StorableObjectType getType() {       
        return this.type;
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
                                                                                        KISType type,
																						Identifier equipmentId,
																						Identifier mcmId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
		this.name = name;
		this.description = description;
        this.type = type;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
	}

	protected synchronized void setMeasurementPortIds(List measurementPortIds) {
		this.measurementPortIds = measurementPortIds;
	}
}
