/*
 * $Id: CableThread.java,v 1.8 2005/01/25 12:13:08 bob Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/01/25 12:13:08 $
 * @author $Author: bob $
 * @module config_v1
 */
public class CableThread extends DomainMember implements TypedObject {

	private static final long	serialVersionUID	= 3258415027823063600L;

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";

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
						Identifier domainId,
						String name,
						String description,
						CableThreadType type) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					domainId);
		this.name = name;
		this.description = description;
		this.type = type;
	
		super.currentVersion = super.getNextVersion();
	
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
			return new CableThread(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLE_THREAD_ENTITY_CODE),
											creatorId,
											domainId,
											name,
											description,
											type);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CableThread.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.cableThreadDatabase != null)
				this.cableThreadDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
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
					Identifier domainId,
					String name,
					String description,
					CableThreadType type) {
		super.setAttributes(created, modified, creatorId, modifierId, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
		super.currentVersion = super.getNextVersion();
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
}
