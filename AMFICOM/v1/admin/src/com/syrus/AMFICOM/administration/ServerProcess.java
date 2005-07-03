/*
 * $Id: ServerProcess.java,v 1.17 2005/06/25 17:07:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.IdlServerProcess;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2005/06/25 17:07:53 $
 * @author $Author: bass $
 * @module admin_v1
 */
public final class ServerProcess extends StorableObject {
	private static final long serialVersionUID = 2216890579914405388L;

	private String codename;
	private Identifier serverId;
	private Identifier userId;
	private String description;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	ServerProcess(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		final ServerProcessDatabase database = (ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	ServerProcess(final IdlServerProcess spt) {
		this.fromTransferable(spt);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param id
	 * @param creatorId
	 * @param version
	 * @param codename
	 * @param serverId
	 * @param userId
	 * @param description
	 */
	ServerProcess(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final Identifier serverId,
			final Identifier userId,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.codename = codename;
		this.serverId = serverId;
		this.userId = userId;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IDLEntity transferable) {
		final IdlServerProcess spt = (IdlServerProcess) transferable;
		try {
			super.fromTransferable(spt.header);
		}
		catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.codename = spt.codename;
		this.serverId = new Identifier(spt.serverId);
		this.userId = new Identifier(spt.userId);
		this.description = spt.description;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlServerProcess getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return new IdlServerProcess(super.getHeaderTransferable(orb),
				this.codename,
				this.serverId.getTransferable(),
				this.userId.getTransferable(),
				this.description);
	}

	public static ServerProcess createInstance(final Identifier creatorId,
			final String codename,
			final Identifier serverId,
			final Identifier userId,
			final String description) throws CreateObjectException {
		try {
			ServerProcess serverProcess = new ServerProcess(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SERVERPROCESS_CODE),
					creatorId,
					0L,
					codename,
					serverId,
					userId,
					description);

			assert serverProcess.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			serverProcess.markAsChanged();

			return serverProcess;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final Identifier serverId,
			final Identifier userId,
			final String description) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.codename = codename;
		this.serverId = serverId;
		this.userId = userId;
		this.description = description;
	}

	public String getCodename() {
		return this.codename;
	}

	public Identifier getServerId() {
		return this.serverId;
	}

	public Identifier getUserId() {
		return this.userId;
	}

	public String getDescription() {
		return this.description;
	}

	protected void setCodename(final String codename) {
		assert codename != null : ErrorMessages.NON_NULL_EXPECTED;
		this.codename = codename;
		super.markAsChanged();
	}

	public void setDescription(final String description) {
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.codename != null && this.codename.length() != 0
				&& this.serverId != null
				&& this.userId != null;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.serverId);
		dependencies.add(this.userId);
		return dependencies;
	}

}
