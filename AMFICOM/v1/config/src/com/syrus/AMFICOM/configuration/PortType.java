/*
 * $Id: PortType.java,v 1.12 2004/11/15 14:02:55 bob Exp $
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
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;

/**
 * @version $Revision: 1.12 $, $Date: 2004/11/15 14:02:55 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class PortType extends StorableObjectType {
	static final long serialVersionUID = -115251480084275101L;

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
		super(ptt.header,
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
		return new PortType_Transferable(super.getHeaderTransferable(),
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

	public List getDependencies() {		
		return Collections.EMPTY_LIST;
	}
}
