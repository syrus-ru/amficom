/*
 * $Id: MCM.java,v 1.3 2005/02/01 11:36:51 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
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
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/01 11:36:51 $
 * @author $Author: bob $
 * @module administration_v1
 */

public class MCM extends DomainMember implements Characterized {
	private static final long serialVersionUID = 4622885259080741046L;

	private String name;
	private String description;
	private String hostname;
	private Identifier userId;
	private Identifier serverId;

	private List kisIds;

	private List characteristics;

	private StorableObjectDatabase mcmDatabase;

	public MCM(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.kisIds = new LinkedList();
		this.characteristics = new ArrayList();
		this.mcmDatabase = AdministrationDatabaseContext.mcmDatabase;
		try {
			this.mcmDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MCM(MCM_Transferable mt) throws CreateObjectException {
		super(mt.header, new Identifier(mt.domain_id));

		this.name = new String(mt.name);
		this.description = new String(mt.description);
		this.hostname = new String(mt.hostname);
		this.userId = new Identifier(mt.user_id);
		this.serverId = new Identifier(mt.server_id);

		try {
			this.kisIds = new ArrayList(mt.kis_ids.length);
			for (int i = 0; i < mt.kis_ids.length; i++)
				this.kisIds.add(new Identifier(mt.kis_ids[i]));

			this.characteristics = new ArrayList(mt.characteristic_ids.length);
			for (int i = 0; i < mt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(mt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.mcmDatabase = AdministrationDatabaseContext.mcmDatabase;
	}

	protected MCM(Identifier id,
								Identifier creatorId,
								Identifier domainId,
								String name,
								String description,
								String hostname,
								Identifier userId,
								Identifier serverId) {
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
		this.serverId = serverId;

		this.characteristics = new ArrayList();

		this.kisIds = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.mcmDatabase = AdministrationDatabaseContext.mcmDatabase;
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.mcmDatabase != null)
				this.mcmDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator it = this.characteristics.iterator(); it.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)it.next()).getId().getTransferable();

		i = 0;
		Identifier_Transferable[] kisIdsT = new Identifier_Transferable[this.kisIds.size()];
		for (Iterator it = this.kisIds.iterator(); it.hasNext();)
			kisIdsT[i++] = (Identifier_Transferable)((Identifier)it.next()).getTransferable();

		return new MCM_Transferable(super.getHeaderTransferable(),
									(Identifier_Transferable)super.domainId.getTransferable(),
									new String(this.name),
									new String(this.description),
									new String(this.hostname),
									(Identifier_Transferable)this.userId.getTransferable(),
									(Identifier_Transferable)this.serverId.getTransferable(),
									charIds,
									kisIdsT);
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

	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public Identifier getUserId() {
		return this.userId;
	}

	public Identifier getServerId() {
		return this.serverId;
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

	public List getKISIds() {
		return Collections.unmodifiableList(this.kisIds);
	}	

	public static MCM createInstance(Identifier creatorId,
									 Identifier domainId,
									 String name,
									 String description,
									 String hostname,
									 Identifier userId,
									 Identifier serverId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null || hostname == null || 
				userId == null || serverId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new MCM(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MCM_ENTITY_CODE),
								creatorId,
								domainId,
								name,
								description,
								hostname,
								userId,
								serverId);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MCM.createInstance | cannot generate identifier ", e);
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
											  Identifier userId,
											  Identifier serverId) {
		super.setAttributes(created,												
				modified,
				creatorId,
				modifierId,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;
		this.serverId = serverId;
	}

	protected synchronized void setKISIds0(List kisIds) {
		this.kisIds.clear();
		if (kisIds != null)
			this.kisIds.addAll(kisIds);		
	}

	public void setKISIds(List kisIds) {
		this.setKISIds0(kisIds);
		super.currentVersion = super.getNextVersion();
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.addAll(this.kisIds);
		dependencies.add(this.userId);
		dependencies.add(this.serverId);
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
	
	public void setServerId(Identifier serverId) {
		this.serverId = serverId;
		super.currentVersion = super.getNextVersion();
	}
	
	public void setUserId(Identifier userId) {
		this.userId = userId;
		super.currentVersion = super.getNextVersion();
	}
}
