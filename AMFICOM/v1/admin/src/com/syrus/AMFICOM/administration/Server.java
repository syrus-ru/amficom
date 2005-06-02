/*
 * $Id: Server.java,v 1.30 2005/06/02 14:26:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.30 $, $Date: 2005/06/02 14:26:53 $
 * @author $Author: arseniy $
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
	public Server(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);
		this.characteristics = new LinkedHashSet();

		ServerDatabase database = (ServerDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVER_ENTITY_CODE);
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
	public Server(Server_Transferable st) throws CreateObjectException {
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
	Server(Identifier id,
								 Identifier creatorId,
								 long version,
								 Identifier domainId,
								 String name,
								 String description,
								 String hostname) {
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
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
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

		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Server_Transferable(super.getHeaderTransferable(),
									   (Identifier_Transferable)super.domainId.getTransferable(),
									   this.name,
									   this.description,
										 this.hostname,
									   charIds);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
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

	public void setDescription(String description) {
		this.description = description;
		super.changed = true;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.changed = true;
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public void setCharacteristics0(Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.changed = true;
	}

	public static Server createInstance(Identifier creatorId,
										Identifier domainId,
										String name,
										String description,
										String hostname) throws CreateObjectException {
		try {
			Server server = new Server(IdentifierPool.getGeneratedIdentifier(ObjectEntities.SERVER_ENTITY_CODE),
						creatorId,
						0L,
						domainId,
						name,
						description,
						hostname);

			assert server.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			server.changed = true;
			try {
				StorableObjectPool.putStorableObject(server);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

			return server;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  Identifier domainId,
											  String name,
											  String description,
											  String hostname) {
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

	public void setHostName(String hostname) {
		this.hostname = hostname;
		super.changed = true;
	}

	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
}
