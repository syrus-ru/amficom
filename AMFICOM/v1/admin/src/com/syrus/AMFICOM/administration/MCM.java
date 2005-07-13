/*
 * $Id: MCM.java,v 1.40 2005/07/13 17:36:16 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlMCM;
import com.syrus.AMFICOM.administration.corba.IdlMCMHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.40 $, $Date: 2005/07/13 17:36:16 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public final class MCM extends DomainMember implements Characterizable {
	private static final long serialVersionUID = 4622885259080741046L;

	private String name;
	private String description;
	private String hostname;
	private Identifier userId;
	private Identifier serverId;

	private Set<Characteristic> characteristics;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public MCM(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();

		final MCMDatabase database = (MCMDatabase) DatabaseContext.getDatabase(ObjectEntities.MCM_CODE);
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
	public MCM(final IdlMCM mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	MCM(final Identifier id,
			final Identifier creatorId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final Identifier userId,
			final Identifier serverId) {
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
		this.userId = userId;
		this.serverId = serverId;

		this.characteristics = new HashSet<Characteristic>();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMCM mt = (IdlMCM)transferable;
		super.fromTransferable(mt, new Identifier(mt.domainId));
		this.name = mt.name;
		this.description = mt.description;
		this.hostname = mt.hostname;
		this.userId = new Identifier(mt.userId);
		this.serverId = new Identifier(mt.serverId);

		final Set<Identifier> characteristicIds = Identifier.fromTransferables(mt.characteristicIds);
		this.characteristics = new HashSet<Characteristic>(mt.characteristicIds.length);
		final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjects(characteristicIds, true);
		this.setCharacteristics0(characteristics0);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMCM getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return IdlMCMHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version,
				super.domainId.getTransferable(),
				this.name,
				this.description,
				this.hostname,
				this.userId.getTransferable(),
				this.serverId.getTransferable(),
				charIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null && this.name.length() != 0
				&& this.description != null
				&& this.hostname != null
				&& this.userId != null
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

	public Identifier getUserId() {
		return this.userId;
	}

	public Identifier getServerId() {
		return this.serverId;
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

	public Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public void setCharacteristics0(final Set<Characteristic> characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set<Characteristic> characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	public static MCM createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final Identifier userId,
			final Identifier serverId) throws CreateObjectException {
		try {
			MCM mcm = new MCM(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MCM_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description,
					hostname,
					userId,
					serverId);
			
			assert mcm.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			mcm.markAsChanged();

			return mcm;
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
			final String hostname,
			final Identifier userId,
			final Identifier serverId) {
		super.setAttributes(created,												
				modified,
				creatorId,
				modifierId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.userId = userId;
		this.serverId = serverId;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.userId);
		dependencies.add(this.serverId);
		return dependencies;
	}
	
	public void setHostName(final String hostname) {
		this.hostname = hostname;
		super.markAsChanged();
	}
	
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	
	public void setServerId(final Identifier serverId) {
		this.serverId = serverId;
		super.markAsChanged();
	}
	
	public void setUserId(final Identifier userId) {
		this.userId = userId;
		super.markAsChanged();
	}
}
