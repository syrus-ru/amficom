/*
 * $Id: Server.java,v 1.9 2004/08/11 14:48:33 arseniy Exp $
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
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;

/**
 * @version $Revision: 1.9 $, $Date: 2004/08/11 14:48:33 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class Server extends DomainMember implements Characterized {
	protected static final int RETRIEVE_MCM_IDS	= 1;

	private String name;
	private String description;
	private Identifier userId;
	private List characteristicIds;

	private StorableObjectDatabase serverDatabase;

	public Server(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.serverDatabase = ConfigurationDatabaseContext.serverDatabase;
		try {
			this.serverDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Server(Server_Transferable st) throws CreateObjectException {
		super(new Identifier(st.id),
					new Date(st.created),
					new Date(st.modified),
					new Identifier(st.creator_id),
					new Identifier(st.modifier_id),
					new Identifier(st.domain_id));
		this.name = new String(st.name);
		this.description = new String(st.description);
		this.userId = new Identifier(st.user_id);

		this.characteristicIds = new ArrayList(st.characteristic_ids.length);
		for (int i = 0; i < st.characteristic_ids.length; i++)
			this.characteristicIds.add(new Identifier(st.characteristic_ids[i]));

		this.serverDatabase = ConfigurationDatabaseContext.serverDatabase;
		try {
			this.serverDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	private Server(Identifier id,
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

		this.characteristicIds = new ArrayList();
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristicIds.size()];
		for (Iterator iterator = this.characteristicIds.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new Server_Transferable((Identifier_Transferable)super.id.getTransferable(),
																	 super.created.getTime(),
																	 super.modified.getTime(),
																	 (Identifier_Transferable)super.creatorId.getTransferable(),
																	 (Identifier_Transferable)super.modifierId.getTransferable(),
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

	public Identifier getUserId() {
		return this.userId;
	}

	public List getCharacteristicIds() {
		return this.characteristicIds;
	}

	public void setCharacteristicIds(List characteristicIds) {
		this.characteristicIds = characteristicIds;
	}
	
	public static Server createInstance(Identifier id,
																			Identifier creatorId,
																			Identifier domainId,
																			String name,
																			String description,
																			Identifier userId) {
		return new Server(id,
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
}
