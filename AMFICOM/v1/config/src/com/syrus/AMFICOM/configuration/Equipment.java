/*
 * $Id: Equipment.java,v 1.30 2004/11/04 09:05:13 bob Exp $
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
 * @version $Revision: 1.30 $, $Date: 2004/11/04 09:05:13 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class Equipment extends MonitoredDomainMember implements Characterized, TypedObject {
	
	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;
	
	private EquipmentType type;
	private String name;
	private String description;
	private Identifier imageId;

	private List portIds;

	private List characteristics;

	private StorableObjectDatabase equipmentDatabase;

	public Equipment(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
		try {
			this.equipmentDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Equipment(Equipment_Transferable et) throws CreateObjectException {
		super(new Identifier(et.id),
					new Date(et.created),
					new Date(et.modified),
					new Identifier(et.creator_id),
					new Identifier(et.modifier_id),
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
										Identifier imageId) {
				super(id,
							new Date(System.currentTimeMillis()),
							new Date(System.currentTimeMillis()),
							creatorId,
							creatorId,
							domainId);

				super.monitoredElementIds = new ArrayList();

				this.type = type;
				this.name = name;
				this.description = description;
				this.imageId = imageId;

				this.portIds = new ArrayList();

				this.characteristics = new ArrayList();
				
				this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
	}
				
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param domainId
	 * @param type
	 * @param name
	 * @param description
	 * @param imageId
	 * @param sort
	 * @param kisId
	 * @return
	 */
	public static Equipment createInstance(Identifier id,
																				 Identifier creatorId,
																				 Identifier domainId,
																				 EquipmentType type,
																				 String name,
																				 String description,
																				 Identifier imageId) {
		return new Equipment(id,
												 creatorId,
												 domainId,
												 type,
												 name,
												 description,
												 imageId);
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

		return new Equipment_Transferable((Identifier_Transferable)super.id.getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creatorId.getTransferable(),
																			(Identifier_Transferable)super.modifierId.getTransferable(),
																			(Identifier_Transferable)super.domainId.getTransferable(),
																			meIds,
																			(Identifier_Transferable)this.type.getId().getTransferable(),
																			new String(this.name),
																			new String(this.description),
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

	public Identifier getImageId(){
		return this.imageId;
	}

	public List getCharacteristics() {
		return this.characteristics;
	}

	public List getPortIds() {
		return this.portIds;
	}

	public void setCharacteristics(List characteristics) {
		this.characteristics = characteristics;
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

	protected synchronized void setPortIds(List portIds) {
		this.portIds = portIds;
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.monitoredElementIds);
		dependencies.add(this.portIds);
		for (Iterator it = this.characteristics.iterator(); it.hasNext();) {
			Characteristic characteristic = (Characteristic) it.next();
			dependencies.add(characteristic.getId());			
		}
		return dependencies;
	}
}
