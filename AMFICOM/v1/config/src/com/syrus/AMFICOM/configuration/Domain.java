/*
 * $Id: Domain.java,v 1.8 2004/08/31 15:33:35 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

/**
 * @version $Revision: 1.8 $, $Date: 2004/08/31 15:33:35 $
 * @author $Author: bob $
 * @module configuration_v1
 */

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;

public class Domain extends DomainMember implements Characterized {
	private String name;
	private String description;
	private List characteristics;

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

	private Domain(Domain_Transferable dt) throws CreateObjectException {
		super(new Identifier(dt.id),
					new Date(dt.created),
					new Date(dt.modified),
					new Identifier(dt.creator_id),
					new Identifier(dt.modifier_id),
					(dt.domain_id.identifier_string.length() != 0) ? (new Identifier(dt.domain_id)) : null);
		this.name = new String(dt.name);
		this.description = new String(dt.description);
		
		try {
			this.characteristics = new ArrayList(dt.characteristic_ids.length);
			for (int i = 0; i < dt.characteristic_ids.length; i++)
				this.characteristics.add(ConfigurationStorableObjectPool.getStorableObject(new Identifier(dt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected Domain(Identifier id,
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

		this.characteristics = new ArrayList();
		
		this.domainDatabase = ConfigurationDatabaseContext.domainDatabase;
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Domain_Transferable((Identifier_Transferable)super.id.getTransferable(),
																	 super.created.getTime(),
																	 super.modified.getTime(),
																	 (Identifier_Transferable)super.creatorId.getTransferable(),
																	 (Identifier_Transferable)super.modifierId.getTransferable(),
																	 (super.domainId != null) ? (Identifier_Transferable)super.domainId.getTransferable() : (new Identifier_Transferable("")),
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

	public List getCharacteristics() {
		return this.characteristics;
	}

	public void setCharacteristics(List characteristics) {
		this.characteristics = characteristics;
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @return
	 */
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
	
	public static Domain getInstance(Domain_Transferable dt) throws CreateObjectException {
		Domain domain = new Domain(dt);
		
		domain.domainDatabase = ConfigurationDatabaseContext.domainDatabase;
		try {
			if (domain.domainDatabase != null)
				domain.domainDatabase.insert(domain);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return domain;
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
