/*
 * $Id: Server.java,v 1.24 2004/12/06 12:54:20 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;

/**
 * @version $Revision: 1.24 $, $Date: 2004/12/06 12:54:20 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class Server extends DomainMember implements Characterized {
	static final long serialVersionUID = 1988410957632317660L;

	protected static final int RETRIEVE_MCM_IDS	= 1;

	private String name;
	private String description;
	private Identifier userId;
	private List characteristics;

	private StorableObjectDatabase serverDatabase;

	public Server(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.characteristics = new LinkedList();

		this.serverDatabase = ConfigurationDatabaseContext.serverDatabase;
		try {
			this.serverDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Server(Server_Transferable st) throws CreateObjectException {
		super(st.header,
			  new Identifier(st.domain_id));
		this.name = new String(st.name);
		this.description = new String(st.description);
		this.userId = new Identifier(st.user_id);
		
		try {
			this.characteristics = new ArrayList(st.characteristic_ids.length);
			for (int i = 0; i < st.characteristic_ids.length; i++)
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(st.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Server(Identifier id,
								 Identifier creatorId,
								 Identifier domainId,
								 String name,
								 String description,
								 Identifier userId) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;
		this.userId = userId;

		this.characteristics = new LinkedList();
		this.serverDatabase = ConfigurationDatabaseContext.serverDatabase;
	}
	
	public static Server getInstance(Server_Transferable st) throws CreateObjectException {
		Server server = new Server(st);
		
		server.serverDatabase = ConfigurationDatabaseContext.serverDatabase;
		try {
			if (server.serverDatabase != null)
				server.serverDatabase.insert(server);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}

		return server;
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Server_Transferable(super.getHeaderTransferable(),
									   (Identifier_Transferable)super.domainId.getTransferable(),
									   new String(this.name),
									   new String(this.description),
									   (Identifier_Transferable)this.userId.getTransferable(),
									   charIds);
	}

	public List retrieveMCMIds() throws ObjectNotFoundException, RetrieveObjectException {
		try {
			return (List)this.serverDatabase.retrieveObject(this, RETRIEVE_MCM_IDS, null);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
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

	public Identifier getUserId() {
		return this.userId;
	}

	public List getCharacteristics() {
		return this.characteristics;
	}

	public void setCharacteristics(List characteristics) {
		this.characteristics.clear();
	     if (characteristics != null)
	     	this.characteristics.addAll(characteristics);
	     super.currentVersion = super.getNextVersion();
	}
	
	public static Server createInstance(Identifier creatorId,
										Identifier domainId,
										String name,
										String description,
										Identifier userId) {
		if (creatorId == null || domainId == null || name == null || description == null || 
				userId == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return new Server(IdentifierPool.generateId(ObjectEntities.SERVER_ENTITY_CODE),
					creatorId,
					domainId,
					name,
					description,
					userId);
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  Identifier domainId,
											  String name,
											  String description,
											  Identifier userId) {
		super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					domainId);
		this.name = name;
		this.description = description;
		this.userId = userId;
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.userId);
		dependencies.addAll(this.characteristics);			
		return dependencies;
	}
}
