/*
 * $Id: Equipment.java,v 1.18 2004/08/11 16:45:02 arseniy Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentSort;

/**
 * @version $Revision: 1.18 $, $Date: 2004/08/11 16:45:02 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class Equipment extends MonitoredDomainMember implements Characterized, TypedObject {
	
	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;
	
	private EquipmentType type;
	private String name;
	private String description;
	private Identifier imageId;

	private List characteristicIds;
	private List portIds;
	private List cablePortIds;
	private List specialPortIds;
	
	private int sort;
	private Identifier kisId;

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

		this.type = (EquipmentType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
		this.name = new String(et.name);
		this.description = new String(et.description);
		this.imageId = new Identifier(et.image_id);

		this.characteristicIds = new ArrayList(et.characteristic_ids.length);
		for (int i = 0; i < et.characteristic_ids.length; i++)
			this.characteristicIds.add(new Identifier(et.characteristic_ids[i]));

		this.portIds = new ArrayList(et.port_ids.length);
		for (int i = 0; i < et.port_ids.length; i++)
			this.portIds.add(new Identifier(et.port_ids[i]));

		this.cablePortIds = new ArrayList(et.cable_port_ids.length);
		for (int i = 0; i < et.cable_port_ids.length; i++)
			this.cablePortIds.add(new Identifier(et.cable_port_ids[i]));

		this.specialPortIds = new ArrayList(et.special_port_ids.length);
		for (int i = 0; i < et.special_port_ids.length; i++)
			this.specialPortIds.add(new Identifier(et.special_port_ids[i]));
		
		this.sort = et.sort.value();
		if (this.sort == EquipmentSort._EQUIPMENT_SORT_KIS)
			this.kisId = new Identifier(et.kis_id);
		
		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
		try {
			this.equipmentDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		int i = 0;
		
		Identifier_Transferable[] meIds = new Identifier_Transferable[super.monitoredElementIds.size()];
		for (Iterator iterator = super.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
		
		i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristicIds.size()];
		for (Iterator iterator = this.characteristicIds.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;		
		Identifier_Transferable[] pIds = new Identifier_Transferable[this.portIds.size()];
		for (Iterator iterator = this.portIds.iterator(); iterator.hasNext();)
			pIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] cportIds = new Identifier_Transferable[this.cablePortIds.size()];
		for (Iterator iterator = this.cablePortIds.iterator(); iterator.hasNext();)
			cportIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] sportIds = new Identifier_Transferable[this.specialPortIds.size()];
		for (Iterator iterator = this.specialPortIds.iterator(); iterator.hasNext();)
			sportIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

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
																			charIds,
																			pIds,
																			cportIds,
																			sportIds,
																			EquipmentSort.from_int(this.sort),
																			(this.sort == EquipmentSort._EQUIPMENT_SORT_KIS) ? (Identifier_Transferable)this.kisId.getTransferable() : (new Identifier_Transferable("")));
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

	public List getCharacteristicIds() {
		return this.characteristicIds;
	}

	public List getPortIds() {
		return this.portIds;
	}

	public List getCablePortIds() {
		return this.cablePortIds;
	}

	public List getSpecialPortIds() {
		return this.specialPortIds;
	}

	public int getSort(){
		return this.sort;
	}

	public Identifier getKISId() {
		return this.kisId;
	}

	public void setCharacteristicIds(List characteristicIds) {
		this.characteristicIds = characteristicIds;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						EquipmentType type,
																						String name,
																						String description,
																						Identifier imageId,
																						int sort,
																						Identifier kisId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
		this.type = type;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.sort = sort;
		switch (this.sort) {
			case EquipmentSort._EQUIPMENT_SORT_KIS:
				this.kisId = kisId;
				break;
			default:
		}
	}
}
