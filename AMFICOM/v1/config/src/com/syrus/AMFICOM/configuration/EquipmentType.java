/*
 * $Id: EquipmentType.java,v 1.12 2004/11/04 08:51:05 bob Exp $
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;

/**
 * @version $Revision: 1.12 $, $Date: 2004/11/04 08:51:05 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class EquipmentType extends StorableObjectType {
	
	private String name;

	private StorableObjectDatabase equipmentTypeDatabase;

	public EquipmentType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			this.equipmentTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public EquipmentType(EquipmentType_Transferable ett) throws CreateObjectException {
		super(new Identifier(ett.id),
					new Date(ett.created),
					new Date(ett.modified),
					new Identifier(ett.creator_id),
					new Identifier(ett.modifier_id),
					new String(ett.codename),
					new String(ett.description));	
		this.name = ett.name;
	}
	
	protected EquipmentType(Identifier id,
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
		
		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
	}


	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @return
	 */
	public static EquipmentType createInstance(Identifier id,
											 Identifier creatorId,
											 String codename,
											 String description,
											 String name){
		return new EquipmentType(id,
							creatorId,
							codename,
							description,
							name);		
	}
	
	public static EquipmentType getInstance(EquipmentType_Transferable ett) throws CreateObjectException {
		EquipmentType equipmentType = new EquipmentType(ett);
		
		equipmentType.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			if (equipmentType.equipmentTypeDatabase != null)
				equipmentType.equipmentTypeDatabase.insert(equipmentType);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return equipmentType;
	}
	
	public Object getTransferable() {
		return new EquipmentType_Transferable((Identifier_Transferable)super.id.getTransferable(),
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
	
	protected List getDependencies() {		
		return Collections.EMPTY_LIST;
	}
}
