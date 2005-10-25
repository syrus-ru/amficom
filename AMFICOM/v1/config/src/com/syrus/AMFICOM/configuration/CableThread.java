/*
 * $Id: CableThread.java,v 1.44 2005/10/25 19:53:09 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_CODE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.IdlCableThread;
import com.syrus.AMFICOM.configuration.corba.IdlCableThreadHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.44 $, $Date: 2005/10/25 19:53:09 $
 * @author $Author: bass $
 * @module config
 */
public final class CableThread extends DomainMember<CableThread>
		implements TypedObject<CableThreadType> {

	private static final long serialVersionUID = 3258415027823063600L;

	private String name;
	private String description;
	private CableThreadType type;

	public CableThread(final IdlCableThread ctt) throws CreateObjectException {
		try {
			this.fromTransferable(ctt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected CableThread(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
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
			final CableThread cableThread = new CableThread(IdentifierPool.getGeneratedIdentifier(CABLETHREAD_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description,
					type);

			assert cableThread.isValid() : OBJECT_STATE_ILLEGAL;

			cableThread.markAsChanged();

			return cableThread;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlCableThread ctt = (IdlCableThread) transferable;
		super.fromTransferable(ctt, new Identifier(ctt.domainId));

		this.name = ctt.name;
		this.description = ctt.description;
		this.type = (CableThreadType) StorableObjectPool.getStorableObject(new Identifier(ctt._typeId), true);
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableThread getTransferable(final ORB orb) {
		return IdlCableThreadHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.getDomainId().getTransferable(),
				this.name,
				this.description,
				this.type.getId().getTransferable());
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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

	public CableThreadType getType() {
		return this.type;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(1);
		dependencies.add(this.type);
		return dependencies;
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

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CableThreadWrapper getWrapper() {
		return CableThreadWrapper.getInstance();
	}
}
