/*
 * $Id: SystemUser.java,v 1.14 2005/07/05 15:23:17 bass Exp $
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

import com.syrus.AMFICOM.administration.corba.IdlSystemUser;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserHelper;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/07/05 15:23:17 $
 * @author $Author: bass $
 * @module administration_v1
 */

public final class SystemUser extends StorableObject implements Characterizable, Namable {
	private static final long serialVersionUID = 7173419705878464356L;
	
	private String login;
	private int sort;
	private String name;
	private String description;

	private Set<Characteristic> characteristics;
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public SystemUser(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();

		final SystemUserDatabase database = (SystemUserDatabase) DatabaseContext.getDatabase(ObjectEntities.SYSTEMUSER_CODE);
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
	 * @throws CreateObjectException 
	 */
	public SystemUser(final IdlSystemUser ut) throws CreateObjectException {
		try {
			this.fromTransferable(ut);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	SystemUser(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String login,
			final int sort,
			final String name,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
		
		this.characteristics = new HashSet<Characteristic>();
	}

	/**
	 * client constructor
	 * @param creatorId
	 * @param login
	 * @param sort
	 * @param name
	 * @param description
	 * @throws CreateObjectException
	 */
	public static SystemUser createInstance(final Identifier creatorId,
			final String login,
			final SystemUserSort sort,
			final String name,
			final String description) throws CreateObjectException {
		try {
			final Identifier generatedIdentifier = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SYSTEMUSER_CODE);
			final SystemUser user = new SystemUser(generatedIdentifier,
					creatorId != null ? creatorId : generatedIdentifier,
					0L,
					login,
					sort.value(),
					name,
					description);

			assert user.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			user.markAsChanged();

			return user;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @throws ApplicationException 
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) 
	throws ApplicationException {
		final IdlSystemUser ut = (IdlSystemUser) transferable;
		try {
			super.fromTransferable(ut);
		}
		catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.login = ut.login;
		this.sort = ut.sort.value();
		this.name = ut.name;
		this.description = ut.description;
		
		final Set<Identifier> characteristicIds = Identifier.fromTransferables(ut.characteristicIds);
		this.characteristics = new HashSet<Characteristic>(ut.characteristicIds.length);
		final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjects(characteristicIds, true);
		this.setCharacteristics0(characteristics0);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlSystemUser getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		final IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);
		
		return IdlSystemUserHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version,
				this.login,
				SystemUserSort.from_int(this.sort),
				this.name,
				this.description,
				charIds);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.login != null && this.login.length() != 0
				&& this.name != null && this.name.length() != 0
				&& this.description != null
				&& this.characteristics != null && this.characteristics != Collections.EMPTY_SET;
	}
	
	public String getLogin() {
		return this.login;
	}

	public SystemUserSort getSort() {
		return SystemUserSort.from_int(this.sort);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
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

	public Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set<Characteristic> characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set<Characteristic> characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
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
			final String login,
			final int sort,
			final String name,
			final String description) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return Collections.emptySet();
	}
	
	public void setLogin(final String login) {
		this.login = login;
		super.markAsChanged();
	}
	
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	
	public void setSort(final SystemUserSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}
}
