/*
 * $Id: Domain.java,v 1.1 2004/08/10 19:01:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/10 19:01:08 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;

public class Domain extends DomainMember implements Characterized {
	private String name;
	private String description;
	private List characteristicIds;

	private StorableObjectDatabase domainDatabase;

	public Domain(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.domainDatabase = ConfigurationDatabaseContext.domainDatabase;
		try {
			this.domainDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Domain(Domain_Transferable dt) throws CreateObjectException {
		super(new Identifier(dt.id),
					new Date(dt.created),
					new Date(dt.modified),
					new Identifier(dt.creator_id),
					new Identifier(dt.modifier_id),
					(dt.domain_id.identifier_string != "") ? (new Identifier(dt.domain_id)) : null);
		this.name = new String(dt.name);
		this.description = new String(dt.description);

		this.characteristicIds = new ArrayList(dt.characteristic_ids.length);
		for (int i = 0; i < dt.characteristic_ids.length; i++)
			this.characteristicIds.add(new Identifier(dt.characteristic_ids[i]));

		this.domainDatabase = ConfigurationDatabaseContext.domainDatabase;
		try {
			this.domainDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	private Domain(Identifier id,
								 Identifier creatorId,
								 Identifier domainId,
								 String name,
								 String description) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;

		this.characteristicIds = new ArrayList();
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristicIds.size()];
		for (Iterator iterator = this.characteristicIds.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new Domain_Transferable((Identifier_Transferable)super.id.getTransferable(),
																	 super.created.getTime(),
																	 super.modified.getTime(),
																	 (Identifier_Transferable)super.creatorId.getTransferable(),
																	 (Identifier_Transferable)super.modifierId.getTransferable(),
																	 (super.domainId != null) ? (Identifier_Transferable)super.domainId.getTransferable() : new Identifier_Transferable(""),
																	 new String(this.name),
																	 new String(this.description),
																	 charIds);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public List getCharacteristicIds() {
		return this.characteristicIds;
	}

	public void setCharacteristicIds(List characteristicIds) {
		this.characteristicIds = characteristicIds;
	}

	public static Domain createInstance(Identifier id,
																			Identifier creatorId,
																			Identifier domainId,
																			String name,
																			String description) {
		return new Domain(id,
											creatorId,
											domainId,
											name,
											description);
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						String name,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
		this.name = name;
		this.description = description;
	}
}
