/*
 * $Id: PortType.java,v 1.21 2004/12/09 12:24:00 bob Exp $
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

import com.syrus.AMFICOM.configuration.corba.PortTypeSort;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;

/**
 * @version $Revision: 1.21 $, $Date: 2004/12/09 12:24:00 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class PortType extends StorableObjectType implements Characterized {
	static final long serialVersionUID = -115251480084275101L;

	private String name;
	private List characteristics;
	private StorableObjectDatabase portTypeDatabase;
	private int sort;
    
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
        this.sort = ptt.sort.value();
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
						 String name,
                         int sort){
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				codename,
				description);
		this.name = name;
        this.sort = sort;
		this.characteristics = new LinkedList();
		this.portTypeDatabase = ConfigurationDatabaseContext.portTypeDatabase;
	}
	
	
	/**
	 * create new instance for client 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static PortType createInstance(Identifier creatorId,
										  String codename,
										  String description,
										  String name,
										  PortTypeSort sort) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null || 
				sort == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new PortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PORTTYPE_ENTITY_CODE),
								creatorId,
								codename,
								description,
								name,
			                    sort.value());
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("PortType.createInstance | cannot generate identifier ", e);
		}
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
                                         PortTypeSort.from_int(this.sort),
                                         charIds);
	}
	
	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description,
																						String name,
                                                                                        int sort) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
		this.name = name;
        this.sort = sort;
	}
	
	
	public String getName(){
		return this.name;
	}
    
    public PortTypeSort getSort() {
        return PortTypeSort.from_int(this.sort);
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
