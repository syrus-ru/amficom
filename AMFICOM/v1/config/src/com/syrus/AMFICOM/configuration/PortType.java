/*
 * $Id: PortType.java,v 1.9 2004/11/04 08:51:05 bob Exp $
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
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2004/11/04 08:51:05 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class PortType extends StorableObjectType {

	private String name;
	
	private StorableObjectDatabase portTypeDatabase;

	public PortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.portTypeDatabase = ConfigurationDatabaseContext.portTypeDatabase;
		try {
			this.portTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public PortType(PortType_Transferable ptt) throws CreateObjectException {
		super(new Identifier(ptt.id),
					new Date(ptt.created),
					new Date(ptt.modified),
					new Identifier(ptt.creator_id),
					new Identifier(ptt.modifier_id),
					new String(ptt.codename),
					new String(ptt.description));
		this.name = ptt.name;	
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
		return new PortType_Transferable((Identifier_Transferable)super.id.getTransferable(),
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
