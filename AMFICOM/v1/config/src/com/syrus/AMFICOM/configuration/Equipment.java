/*
 * $Id: Equipment.java,v 1.41 2004/12/09 12:01:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;

/**
 * @version $Revision: 1.41 $, $Date: 2004/12/09 12:01:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class Equipment extends MonitoredDomainMember implements Characterized, TypedObject {
	
	static final long serialVersionUID = -6115401698444070841L;

	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;
	
	private EquipmentType type;
	private String name;
	private String description;
	private Identifier imageId;
    private double longitude;
    private double latitude;
    private String supplier;

	private List portIds;

	private List characteristics;

	private StorableObjectDatabase equipmentDatabase;

	public Equipment(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.portIds = new LinkedList();
		this.characteristics = new LinkedList();
		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
		try {
			this.equipmentDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Equipment(Equipment_Transferable et) throws CreateObjectException {
		super(et.header,
			  new Identifier(et.domain_id));

		super.monitoredElementIds = new ArrayList(et.monitored_element_ids.length);
		for (int i = 0; i < et.monitored_element_ids.length; i++)
			super.monitoredElementIds.add(new Identifier(et.monitored_element_ids[i]));

		try {
			this.type = (EquipmentType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.name = new String(et.name);
		this.description = new String(et.description);
		this.imageId = new Identifier(et.image_id);
        this.supplier = new String(et.supplier);
        this.longitude = et.longitude;
        this.latitude = et.latitude;

		this.portIds = new ArrayList(et.port_ids.length);
		for (int i = 0; i < et.port_ids.length; i++)
			this.portIds.add(new Identifier(et.port_ids[i]));		

		try {
			this.characteristics = new ArrayList(et.characteristic_ids.length);
			for (int i = 0; i < et.characteristic_ids.length; i++)
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(et.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}
	
	protected Equipment(Identifier id,
										Identifier creatorId,
										Identifier domainId,
										EquipmentType type,
										String name,
										String description,
										Identifier imageId,
                                        String supplier,
                                        double longitude,
                                        double latitude) {
				super(id,
							new Date(System.currentTimeMillis()),
							new Date(System.currentTimeMillis()),
							creatorId,
							creatorId,
							domainId);

				super.monitoredElementIds = new LinkedList();

				this.type = type;
				this.name = name;
				this.description = description;
				this.imageId = imageId;
                this.supplier = supplier;
                this.longitude = longitude;
                this.latitude = latitude;

				this.portIds = new LinkedList();

				this.characteristics = new LinkedList();
				
				this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
	}
				
	/**
	 * create new instance for client 
	 * @param creatorId
	 * @param domainId
	 * @param type
	 * @param name
	 * @param description
	 * @param imageId
	 */
	public static Equipment createInstance(Identifier creatorId,
										   Identifier domainId,
										   EquipmentType type,
										   String name,
										   String description,
										   Identifier imageId,
                                           String supplier,
                                           double longitude,
                                           double latitude) {
		if (creatorId == null || domainId == null || type == null || name == null || 
				description == null || imageId == null || supplier == null)
			throw new IllegalArgumentException("Argument is 'null'");
		return new Equipment(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENT_ENTITY_CODE),
							creatorId,
							domainId,
							type,
							name,
							description,
							imageId,
                            supplier,
                            longitude,
                            latitude);
	}
	
	public static Equipment getInstance(Equipment_Transferable et) throws CreateObjectException{
		Equipment equipment = new Equipment(et);	
		
		equipment.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
		try {
			if (equipment.equipmentDatabase != null)
				equipment.equipmentDatabase.insert(equipment);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return equipment;
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] meIds = new Identifier_Transferable[super.monitoredElementIds.size()];
		for (Iterator iterator = super.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		i = 0;		
		Identifier_Transferable[] pIds = new Identifier_Transferable[this.portIds.size()];
		for (Iterator iterator = this.portIds.iterator(); iterator.hasNext();)
			pIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new Equipment_Transferable(super.getHeaderTransferable(),
										  (Identifier_Transferable)super.domainId.getTransferable(),
										  meIds,
										  (Identifier_Transferable)this.type.getId().getTransferable(),
										  new String(this.name),
										  new String(this.description),
                                          new String(this.supplier),
                                          this.longitude,
                                          this.latitude,
										  (Identifier_Transferable)this.imageId.getTransferable(),
										  pIds,
										  charIds);
	}

	public StorableObjectType getType() {
		return this.type;
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

	public Identifier getImageId(){
		return this.imageId;
	}

	public List getCharacteristics() {
		return this.characteristics;
	}

	public List getPortIds() {
		return this.portIds;
	}

	public void setCharacteristics(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
				this.characteristics.addAll(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  Identifier domainId,
											  EquipmentType type,
											  String name,
											  String description,
											  Identifier imageId) {
		super.setAttributes(created,												
							modified,
							creatorId,
							modifierId,
							domainId);
		this.type = type;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
	}

	protected synchronized void setPortIds(final List portIds) {
		this.portIds.clear();
		if (portIds != null)
				this.portIds.addAll(portIds);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.monitoredElementIds);
		dependencies.add(this.portIds);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}
	
	public String getSupplier() {
		return this.supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public double getLatitude() {
		return this.latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return this.longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
