/*
 * $Id: CableThread.java,v 1.13 2005/02/14 09:15:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.CableThread_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.administration.DomainMember;

/**
 * @version $Revision: 1.13 $, $Date: 2005/02/14 09:15:45 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class CableThread extends DomainMember implements TypedObject {

	private static final long	serialVersionUID	= 3258415027823063600L;

	private String name;
	private String description;
	private CableThreadType type;

	private StorableObjectDatabase cableThreadDatabase;

	public CableThread(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.cableThreadDatabase = ConfigurationDatabaseContext.cableThreadDatabase;
		try {
			this.cableThreadDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableThread(CableThread_Transferable ctt) throws CreateObjectException {
		super(ctt.header,
					new Identifier(ctt.domain_id));

		this.name = ctt.name;
		this.description = ctt.description;
		try {
			this.type = (CableThreadType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(ctt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.cableThreadDatabase = ConfigurationDatabaseContext.cableThreadDatabase;
	}

	protected CableThread(Identifier id,
						Identifier creatorId,
						long version,
						Identifier domainId,
						String name,
						String description,
						CableThreadType type) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version,
			domainId);
		this.name = name;
		this.description = description;
		this.type = type;
	
		this.cableThreadDatabase = ConfigurationDatabaseContext.cableThreadDatabase;
	}

	public static CableThread createInstance(Identifier creatorId,
							Identifier domainId,
							String name,
							String description,
							CableThreadType type) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null || type == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			CableThread cableThread = new CableThread(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLETHREAD_ENTITY_CODE),
											creatorId,
											0L,
											domainId,
											name,
											description,
											type);
			cableThread.changed = true;
			return cableThread;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CableThread.createInstance | cannot generate identifier ", e);
		}
	}

	public Object getTransferable() {
		return new CableThread_Transferable(super.getHeaderTransferable(),
																 (Identifier_Transferable)this.getDomainId().getTransferable(),
																 new String(this.name),
																 new String(this.description),
																 (Identifier_Transferable)this.type.getId().getTransferable());
	}

	protected synchronized void setAttributes(Date created,
					Date modified,
					Identifier creatorId,
					Identifier modifierId,
					long version,
					Identifier domainId,
					String name,
					String description,
					CableThreadType type) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
		super.changed = true;
	}

	public String getName() {
		return this.name;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public List getDependencies() {
		return Collections.singletonList(this.type);
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(CableThreadType type) {
		this.type = type;
		super.changed = true;
	}
}
