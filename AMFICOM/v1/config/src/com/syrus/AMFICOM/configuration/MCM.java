/*
 * $Id: MCM.java,v 1.10 2004/08/10 10:15:41 bob Exp $
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;

/**
 * @version $Revision: 1.10 $, $Date: 2004/08/10 10:15:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MCM extends DomainMember implements Characterized {
	private String name;
	private String description;
	private Identifier userId;
	private Identifier serverId;
	private List characteristicIds;

	private List kiss;

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

		this.characteristicIds = new ArrayList(mt.characteristic_ids.length);
		for (int i = 0; i < mt.characteristic_ids.length; i++)
			this.characteristicIds.add(new Identifier(mt.characteristic_ids[i]));

		this.mcmDatabase = ConfigurationDatabaseContext.mcmDatabase;
		try {
			this.mcmDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristicIds.size()];
		for (Iterator iterator = this.characteristicIds.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
		
		i = 0;
		Identifier_Transferable[] kisIds = new Identifier_Transferable[this.kiss.size()];
		for (Iterator iterator = this.kiss.iterator(); iterator.hasNext();)
			kisIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new MCM_Transferable((Identifier_Transferable)super.getId().getTransferable(),
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
																kisIds);
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


	public List getCharacteristicIds() {
		return this.characteristicIds;
	}

	public void setCharacteristicIds(List characteristicIds) {
		this.characteristicIds = characteristicIds;
	}

	public List getKISs() {
		return this.kiss;
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

	protected synchronized void setKISs(List kiss) {
		this.kiss = kiss;
	}
}
