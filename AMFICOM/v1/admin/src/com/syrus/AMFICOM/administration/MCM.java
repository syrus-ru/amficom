/*
 * $Id: MCM.java,v 1.53 2005/10/02 14:00:22 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;

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
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.53 $, $Date: 2005/10/02 14:00:22 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */

public final class MCM extends DomainMember implements Characterizable, Namable {
	private static final long serialVersionUID = 4622885259080741046L;

	private String name;
	private String description;
	private String hostname;
	private Identifier userId;
	private Identifier serverId;

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
			final StorableObjectVersion version,
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
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMCM mt = (IdlMCM)transferable;
		super.fromTransferable(mt, new Identifier(mt.domainId));
		this.name = mt.name;
		this.description = mt.description;
		this.hostname = mt.hostname;
		this.userId = new Identifier(mt.userId);
		this.serverId = new Identifier(mt.serverId);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMCM getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlMCMHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.domainId.getTransferable(),
				this.name,
				this.description,
				this.hostname,
				this.userId.getTransferable(),
				this.serverId.getTransferable());
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
				&& this.userId != null;
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

	public static MCM createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final Identifier userId,
			final Identifier serverId) throws CreateObjectException {
		try {
			final MCM mcm = new MCM(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MCM_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
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
			final StorableObjectVersion version,
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

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		if (this.characteristicContainerWrappee == null) {
			this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE);
		}
		return this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
