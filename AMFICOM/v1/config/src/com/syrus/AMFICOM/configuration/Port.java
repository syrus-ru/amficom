/*
 * $Id: Port.java,v 1.2 2004/08/11 12:24:13 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/11 12:24:13 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public class Port extends StorableObject implements Characterized, TypedObject {

	private PortType type;	
	private String description;
	private Identifier equipmentId;
	private int sort;
	
	private List characteristicIds;
	
	private StorableObjectDatabase portDatabase;
	
	public Port(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.portDatabase = ConfigurationDatabaseContext.portDatabase;
		try {
			this.portDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Port(Port_Transferable pt) throws CreateObjectException {
		super(new Identifier(pt.id),
					new Date(pt.created),
					new Date(pt.modified),
					new Identifier(pt.creator_id),
					new Identifier(pt.modifier_id));
		
		this.type = (PortType)ConfigurationObjectTypePool.getObjectType(new Identifier(pt.type_id));
		this.description = new String(pt.description);
		this.equipmentId = new Identifier(pt.equipment_id);

		this.characteristicIds = new ArrayList(pt.characteristic_ids.length);
		for (int i = 0; i < pt.characteristic_ids.length; i++)
			this.characteristicIds.add(new Identifier(pt.characteristic_ids[i]));
		
		this.sort = pt.sort.value();
		
		this.portDatabase = ConfigurationDatabaseContext.portDatabase;
		try {
			this.portDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		int i = 0;
		
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristicIds.size()];
		for (Iterator iterator = this.characteristicIds.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
		    
		return new Port_Transferable((Identifier_Transferable)super.id.getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creatorId.getTransferable(),
																			(Identifier_Transferable)super.modifierId.getTransferable(),																			
																			
																			(Identifier_Transferable)this.type.getId().getTransferable(),																			
																			new String(this.description),
																			(Identifier_Transferable)this.equipmentId.getTransferable(),																			
																			PortSort.from_int(this.sort),
																			charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}


	public String getDescription() {
		return this.description;
	}

	public List getCharacteristicIds() {
		return this.characteristicIds;
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}
	
	public int getSort(){
		return this.sort;
	}

	public void setCharacteristicIds(List characteristicIds) {
		this.characteristicIds = characteristicIds;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						PortType type,																						
																						String description,	
																						Identifier equipmentId,
																						int sort) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
		this.sort = sort;
	}

}
