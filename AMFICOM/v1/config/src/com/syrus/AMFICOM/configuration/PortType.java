/*
 * $Id: PortType.java,v 1.15 2004/11/25 15:59:50 max Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;

/**
 * @version $Revision: 1.15 $, $Date: 2004/11/25 15:59:50 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class PortType extends StorableObjectType implements Characterized {
	static final long serialVersionUID = -115251480084275101L;

	private String name;
	private List characteristics;
	private StorableObjectDatabase portTypeDatabase;

	public PortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		
		this.characteristics = new LinkedList();
		this.portTypeDatabase = ConfigurationDatabaseContext.portTypeDatabase;
		try {
			this.portTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public PortType(PortType_Transferable ptt) throws CreateObjectException {
		super(ptt.header,
			  new String(ptt.codename),
			  new String(ptt.description));
		this.name = ptt.name;
        try {
            this.characteristics = new ArrayList(ptt.characteristic_ids.length);
            for (int i = 0; i < ptt.characteristic_ids.length; i++)
                this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(ptt.characteristic_ids[i]), true));
        }
        catch (ApplicationException ae) {
            throw new CreateObjectException(ae);
        }
	}
	
	protected PortType(Identifier id,
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
		this.characteristics = new LinkedList();
		this.portTypeDatabase = ConfigurationDatabaseContext.portTypeDatabase;
	}
	
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @return
	 */
	public static PortType createInstance(Identifier id,
											 Identifier creatorId,
											 String codename,
											 String description,
											 String name){
		return new PortType(id,
							creatorId,
							codename,
							description,
							name);
	}
	
	public static PortType getInstance(PortType_Transferable ptt) throws CreateObjectException {
		PortType portType = new PortType(ptt);
		
		portType.portTypeDatabase = ConfigurationDatabaseContext.portTypeDatabase;
		try {
			if (portType.portTypeDatabase != null)
				portType.portTypeDatabase.insert(portType);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return portType;
	}
	
	public Object getTransferable() {
		int i = 0;
        Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
        for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
            charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
        
        return new PortType_Transferable(super.getHeaderTransferable(),
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
        return this.characteristics;
    }
    
    public void setCharacteristics(final List characteristics) {
        this.characteristics.clear();
        if (characteristics != null)
                this.characteristics.addAll(characteristics);
        super.currentVersion = super.getNextVersion();
    }
}
