/*
 * $Id: Server.java,v 1.13 2005/04/01 10:31:51 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.13 $, $Date: 2005/04/01 10:31:51 $
 * @author $Author: bass $
 * @module administration_v1
 */

public class Server extends DomainMember implements Characterizable {
	private static final long serialVersionUID = 1988410957632317660L;

	protected static final int RETRIEVE_MCM_IDS	= 1;

	private String name;
	private String description;
	private String hostname;
	private Identifier userId;

	private Set characteristics;

	private StorableObjectDatabase serverDatabase;

	public Server(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.characteristics = new LinkedHashSet();

		this.serverDatabase = AdministrationDatabaseContext.getServerDatabase();
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
			this.characteristics = new HashSet(st.characteristic_ids.length);
			for (int i = 0; i < st.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(st.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.serverDatabase = AdministrationDatabaseContext.getServerDatabase();
	}

	protected Server(Identifier id,
								 Identifier creatorId,
								 long version,
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
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;

		this.characteristics = new HashSet();

		this.serverDatabase = AdministrationDatabaseContext.getServerDatabase();
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

	public Set retrieveMCMIds() throws ObjectNotFoundException, RetrieveObjectException {
		try {
			return (Set) this.serverDatabase.retrieveObject(this, RETRIEVE_MCM_IDS, null);
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
		super.changed = true;
	}

	public Identifier getUserId() {
		return this.userId;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.changed = true;
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.changed = true;
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
			Server server = new Server(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SERVER_ENTITY_CODE),
						creatorId,
						0L,
						domainId,
						name,
						description,
						hostname,
						userId);
			server.changed = true;
			return server;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Server.createInstance | cannot generate identifier ", e);
		}
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  Identifier domainId,
											  String name,
											  String description,
											  String hostname,
											  Identifier userId) {
		super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version,
					domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;
	}

	public Set getDependencies() {
		return Collections.singleton(this.userId);
	}
	
	public void setHostName(String hostname) {
		this.hostname = hostname;
		super.changed = true;
	}
	
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	
	public void setUserId(Identifier userId) {
		this.userId = userId;
		super.changed = true;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SERVER;
	}
}
