/*
 * $Id: EquipmentType.java,v 1.26 2004/12/10 12:13:50 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;

/**
 * @version $Revision: 1.26 $, $Date: 2004/12/10 12:13:50 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class EquipmentType extends StorableObjectType implements Characterized {
	static final long serialVersionUID = 9157517478787463967L;

	private String name;

	private StorableObjectDatabase equipmentTypeDatabase;
    
    private List characteristics;

	public EquipmentType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new LinkedList();
		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			this.equipmentTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public EquipmentType(EquipmentType_Transferable ett) throws CreateObjectException {
		super(ett.header,
			  new String(ett.codename),
			  new String(ett.description));	
		this.name = ett.name;
        try {
            this.characteristics = new ArrayList(ett.characteristic_ids.length);
            for (int i = 0; i < ett.characteristic_ids.length; i++)
                this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(ett.characteristic_ids[i]), true));
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae);
        }
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
        this.characteristics = new ArrayList();		
		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
	}


	/**
	 * create new instance for client 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static EquipmentType createInstance(Identifier creatorId,
											 String codename,
											 String description,
											 String name) throws CreateObjectException{
		if (creatorId == null || codename == null || description == null || name == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new EquipmentType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE),
								creatorId,
								codename,
								description,
								name);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("EquipmentType.createInstance | cannot generate identifier ", e);
		}		
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.equipmentTypeDatabase != null)
				this.equipmentTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

//	public static EquipmentType getInstance(EquipmentType_Transferable ett) throws CreateObjectException {
//		EquipmentType equipmentType = new EquipmentType(ett);
//		
//		equipmentType.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
//		try {
//			if (equipmentType.equipmentTypeDatabase != null)
//				equipmentType.equipmentTypeDatabase.insert(equipmentType);
//		}
//		catch (IllegalDataException ide) {
//			throw new CreateObjectException(ide.getMessage(), ide);
//		}
//		
//		return equipmentType;
//	}
	
	public Object getTransferable() {
        int i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        
		return new EquipmentType_Transferable(super.getHeaderTransferable(),
											  new String(super.codename),
											  (super.description != null) ? (new String(super.description)) : "",
											  (this.name != null) ? (new String(this.name)) : "",
                                              charIds);
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
	
	public List getDependencies() {		
		return this.characteristics;
	}
    
	public List getCharacteristics() {
        return Collections.unmodifiableList(this.characteristics);
    }
    
    protected void setCharacteristics0(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
    }
    
    public void setCharacteristics(final List characteristics) {
        this.setCharacteristics0(characteristics);
        super.currentVersion = super.getNextVersion();
    }
}
