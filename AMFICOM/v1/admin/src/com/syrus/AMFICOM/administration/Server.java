/*
 * $Id: Server.java,v 1.33 2005/06/17 13:06:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.33 $, $Date: 2005/06/17 13:06:55 $
 * @author $Author: bass $
 * @module administration_v1
 */

public class Server extends DomainMember implements Characterizable {
	private static final long serialVersionUID = 1988410957632317660L;

	private String name;
	private String description;
	private String hostname;

	private Set characteristics;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Server(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.characteristics = new LinkedHashSet();

		ServerDatabase database = (ServerDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVER_CODE);
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
	public Server(final Server_Transferable st) throws CreateObjectException {
		try {
			this.fromTransferable(st);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}	
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Server(final Identifier id,
			final Identifier creatorId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;

		this.characteristics = new HashSet();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		Server_Transferable st = (Server_Transferable) transferable;
		super.fromTransferable(st.header, new Identifier(st.domain_id));
		this.name = st.name;
		this.description = st.description;
		this.hostname = st.hostname;

		Set characteristicIds = Identifier.fromTransferables(st.characteristic_ids);
		this.characteristics = new HashSet(st.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return new Server_Transferable(super.getHeaderTransferable(),
				(IdlIdentifier) super.domainId.getTransferable(),
				this.name,
				this.description,
				this.hostname,
				charIds);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null && this.name.length() != 0
				&& this.description != null
				&& this.hostname != null
				&& this.characteristics != null && this.characteristics != Collections.EMPTY_SET;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getHostName() {
		return this.hostname;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.markAsChanged();
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	public static Server createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname) throws CreateObjectException {
		try {
			Server server = new Server(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SERVER_CODE),
						creatorId,
						0L,
						domainId,
						name,
						description,
						hostname);

			assert server.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			server.markAsChanged();

			return server;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname) {
		super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version,
					domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return Collections.EMPTY_SET;
	}

	public void setHostName(final String hostname) {
		this.hostname = hostname;
		super.markAsChanged();
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
}
