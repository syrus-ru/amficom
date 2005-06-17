/*
 * $Id: CableThread.java,v 1.28 2005/06/17 12:32:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.CableThread_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.28 $, $Date: 2005/06/17 12:32:20 $
 * @author $Author: bass $
 * @module config_v1
 */
public class CableThread extends DomainMember implements TypedObject {

	private static final long serialVersionUID = 3258415027823063600L;

	private String name;
	private String description;
	private CableThreadType type;

	public CableThread(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		CableThreadDatabase database = (CableThreadDatabase) DatabaseContext.getDatabase(ObjectEntities.CABLETHREAD_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CableThread(final CableThread_Transferable ctt) throws CreateObjectException {
		try {
			this.fromTransferable(ctt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected CableThread(final Identifier id,
			final Identifier creatorId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final CableThreadType type) {
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
	}

	public static CableThread createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final CableThreadType type) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null || type == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			CableThread cableThread = new CableThread(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CABLETHREAD_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description,
					type);

			assert cableThread.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			cableThread.markAsChanged();

			return cableThread;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		CableThread_Transferable ctt = (CableThread_Transferable) transferable;
		super.fromTransferable(ctt.header, new Identifier(ctt.domain_id));

		this.name = ctt.name;
		this.description = ctt.description;
		this.type = (CableThreadType) StorableObjectPool.getStorableObject(new Identifier(ctt.type_id), true);
	}

	public IDLEntity getTransferable() {
		return new CableThread_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				this.name,
				this.description,
				(Identifier_Transferable) this.type.getId().getTransferable());
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final CableThreadType type) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public Set getDependencies() {
		return Collections.singleton(this.type);
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(final CableThreadType type) {
		this.type = type;
		super.markAsChanged();
	}
}
