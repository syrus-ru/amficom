/*
 * $Id: Server.java,v 1.3 2005/02/01 11:36:51 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/01 11:36:51 $
 * @author $Author: bob $
 * @module administration_v1
 */

public class Server extends DomainMember implements Characterized {
	private static final long serialVersionUID = 1988410957632317660L;

	protected static final int RETRIEVE_MCM_IDS	= 1;

	private String name;
	private String description;
	private String hostname;
	private Identifier userId;
	private List characteristics;

	private StorableObjectDatabase serverDatabase;

	public Server(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.characteristics = new ArrayList();

		this.serverDatabase = AdministrationDatabaseContext.serverDatabase;
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
		this.hostname = new String(st.hostname);
		this.userId = new Identifier(st.user_id);

		try {
			this.characteristics = new ArrayList(st.characteristic_ids.length);
			for (int i = 0; i < st.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(st.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.serverDatabase = AdministrationDatabaseContext.serverDatabase;
	}

	protected Server(Identifier id,
								 Identifier creatorId,
								 Identifier domainId,
								 String name,
								 String description,
								 String hostname,
								 Identifier userId) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;

		this.characteristics = new ArrayList();

		super.currentVersion = super.getNextVersion();

		this.serverDatabase = AdministrationDatabaseContext.serverDatabase;
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.serverDatabase != null)
				this.serverDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
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
										 new String(this.hostname),
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

	public String getHostName() {
		return this.hostname;
	}

	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public Identifier getUserId() {
		return this.userId;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public List getCharacteristics() {
		return this.characteristics;
	}

	protected void setCharacteristics0(List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	public static Server createInstance(Identifier creatorId,
										Identifier domainId,
										String name,
										String description,
										String hostname,
										Identifier userId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null || hostname == null || 
				userId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Server(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SERVER_ENTITY_CODE),
						creatorId,
						domainId,
						name,
						description,
						hostname,
						userId);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Server.createInstance | cannot generate identifier ", e);
		}
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  Identifier domainId,
											  String name,
											  String description,
												String hostname,
											  Identifier userId) {
		super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.userId);
		dependencies.addAll(this.characteristics);			
		return dependencies;
	}
	
	public void setHostName(String hostname) {
		this.hostname = hostname;
		super.currentVersion = super.getNextVersion();
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	public void setUserId(Identifier userId) {
		this.userId = userId;
		super.currentVersion = super.getNextVersion();
	}
}
