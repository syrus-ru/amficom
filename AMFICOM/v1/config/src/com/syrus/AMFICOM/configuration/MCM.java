/*
 * $Id: MCM.java,v 1.19 2004/09/01 15:08:01 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;

/**
 * @version $Revision: 1.19 $, $Date: 2004/09/01 15:08:01 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MCM extends DomainMember implements Characterized {
	private String name;
	private String description;
	private Identifier userId;
	private Identifier serverId;

	private List kisIds;

	private List characteristics;

	private StorableObjectDatabase mcmDatabase;

	public MCM(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.mcmDatabase = ConfigurationDatabaseContext.mcmDatabase;
		try {
			this.mcmDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MCM(MCM_Transferable mt) throws CreateObjectException {
		super(new Identifier(mt.id),
					new Date(mt.created),
					new Date(mt.modified),
					new Identifier(mt.creator_id),
					new Identifier(mt.modifier_id),
					new Identifier(mt.domain_id));
		this.name = new String(mt.name);
		this.description = new String(mt.description);
		this.userId = new Identifier(mt.user_id);
		this.serverId = new Identifier(mt.server_id);

		this.kisIds = new ArrayList(mt.kis_ids.length);
		for (int i = 0; i < mt.kis_ids.length; i++)
			this.kisIds.add(new Identifier(mt.kis_ids[i]));
		
		try {
			this.characteristics = new ArrayList(mt.characteristic_ids.length);
			for (int i = 0; i < mt.characteristic_ids.length; i++)
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(mt.characteristic_ids[i]), true));
			}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected MCM(Identifier id,
							Identifier creatorId,
							Identifier domainId,
							String name,
							String description,
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
		this.userId = userId;
		this.serverId = serverId;

		this.characteristics = new ArrayList();

		this.kisIds = new ArrayList();
		
		this.mcmDatabase = ConfigurationDatabaseContext.mcmDatabase;
	}
	
	public static MCM getInstance(MCM_Transferable mt) throws CreateObjectException {
		MCM mcm = new MCM(mt);
		
		mcm.mcmDatabase = ConfigurationDatabaseContext.mcmDatabase;
		try {
			if (mcm.mcmDatabase != null)
				mcm.mcmDatabase.insert(mcm);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}

		return mcm;
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] kisIdsT = new Identifier_Transferable[this.kisIds.size()];
		for (Iterator iterator = this.kisIds.iterator(); iterator.hasNext();)
			kisIdsT[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new MCM_Transferable((Identifier_Transferable)super.id.getTransferable(),
																super.created.getTime(),
																super.modified.getTime(),
																(Identifier_Transferable)super.creatorId.getTransferable(),
																(Identifier_Transferable)super.modifierId.getTransferable(),
																(Identifier_Transferable)super.domainId.getTransferable(),
																new String(this.name),
																new String(this.description),
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

	public Identifier getUserId() {
		return this.userId;
	}

	public Identifier getServerId() {
		return this.serverId;
	}


	public List getCharacteristics() {
		return this.characteristics;
	}

	public void setCharacteristics(List characteristics) {
		this.characteristics = characteristics;
	}

	public List getKISIds() {
		return this.kisIds;
	}

	public static MCM createInstance(Identifier id,
																	 Identifier creatorId,
																	 Identifier domainId,
																	 String name,
																	 String description,
																	 Identifier userId,
																	 Identifier serverId) {
		return new MCM(id,
									 creatorId,
									 domainId,
									 name,
									 description,
									 userId,
									 serverId);
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						String name,
																						String description,
																						Identifier userId,
																						Identifier serverId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.serverId = serverId;		
	}

	protected synchronized void setKISIds(List kisIds) {
		this.kisIds = kisIds;
	}
}
