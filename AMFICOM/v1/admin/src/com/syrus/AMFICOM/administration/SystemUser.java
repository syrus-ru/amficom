/*-
 * $Id: SystemUser.java,v 1.46 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

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
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;


/**
 * @version $Revision: 1.46 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */

public final class SystemUser extends StorableObject implements Characterizable, Namable, IdlTransferableObjectExt<IdlSystemUser> {
	private static final long serialVersionUID = -8933797697074976263L;

	private String login;
	private int sort;
	private String name;
	private String description;

	private transient StorableObjectCondition roleCondition;

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 * 
	 * @throws CreateObjectException
	 */
	public SystemUser(final IdlSystemUser ut) throws CreateObjectException {
		try {
			this.fromIdlTransferable(ut);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	SystemUser(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String login,
			final int sort,
			final String name,
			final String description) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
	}

	/**
	 * Create System Administrator This method is only need on system
	 * installation to create the first system administrator. Normally, only
	 * installation procedure should call it. Other users must be created using
	 * {@link SystemUser#createInstance(Identifier, String, SystemUserSort, String, String)}.
	 * 
	 * @param login
	 * @param name
	 * @param description
	 * @return instance of SystemUser, SystemUserSort._USER_SORT_SYSADMIN
	 * @throws CreateObjectException
	 */
	protected static SystemUser createSysAdminInstance(final String login, final String name, final String description)
			throws CreateObjectException {
		try {
			final Identifier generatedIdentifier = IdentifierPool.getGeneratedIdentifier(SYSTEMUSER_CODE);
			final SystemUser systemUser = new SystemUser(generatedIdentifier,
					generatedIdentifier,
					INITIAL_VERSION,
					login,
					SystemUserSort._USER_SORT_SYSADMIN,
					name,
					description);
			assert systemUser.isValid() : OBJECT_STATE_ILLEGAL;
			return systemUser;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * client constructor
	 * 
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
		if (creatorId == null || login == null || sort == null || name == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final SystemUser systemUser = new SystemUser(IdentifierPool.getGeneratedIdentifier(SYSTEMUSER_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					login,
					sort.value(),
					name,
					description);

			assert systemUser.isValid() : OBJECT_STATE_ILLEGAL;

			systemUser.markAsChanged();

			return systemUser;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 * 
	 * @throws IdlConversionException
	 */
	public synchronized void fromIdlTransferable(final IdlSystemUser idlSystemUser) throws IdlConversionException {
		super.fromIdlTransferable(idlSystemUser);
		this.login = idlSystemUser.login;
		this.sort = idlSystemUser.sort.value();
		this.name = idlSystemUser.name;
		this.description = idlSystemUser.description;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	public IdlSystemUser getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlSystemUserHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.login,
				SystemUserSort.from_int(this.sort),
				this.name,
				this.description);
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
				&& this.description != null;
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

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String login,
			final int sort,
			final String name,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.login = login;
		this.sort = sort;
		this.name = name;
		this.description = description;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
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

	public void addRole(final Role role) {
		assert role != null : NON_NULL_EXPECTED;

		role.addSystemUserId(this.id);
	}

	public void removeRole(final Role role) {
		assert role != null : NON_NULL_EXPECTED;

		role.removeSystemUserId(this.id);
	}

	public Set<Identifier> getRoleIds() throws ApplicationException {
		if (this.roleCondition == null) {
			this.roleCondition = new LinkedIdsCondition(this, ROLE_CODE);
		}

		return StorableObjectPool.getIdentifiersByCondition(this.roleCondition, true);
	}

	public Set<Role> getRoles() throws ApplicationException {
		if (this.roleCondition == null) {
			this.roleCondition = new LinkedIdsCondition(this, ROLE_CODE);
		}

		return StorableObjectPool.getStorableObjectsByCondition(this.roleCondition, true);
	}

	public void setRoleIds(final Set<Identifier> roleIds) throws ApplicationException {
		assert roleIds != null : NON_NULL_EXPECTED;
		if (roleIds.isEmpty()) {
			return;
		}
		assert StorableObject.getEntityCodeOfIdentifiables(roleIds) == ROLE_CODE : ILLEGAL_ENTITY_CODE;

		final Set<Role> roles = StorableObjectPool.getStorableObjects(roleIds, true);
		for (final Role role : roles) {
			role.addSystemUserId(this.id);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SystemUserWrapper getWrapper() {
		return SystemUserWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this,
						CHARACTERISTIC_CODE)
					: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic,
	 *      boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic, final boolean usePool) throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic,
	 *      boolean)
	 */
	public void removeCharacteristic(final Characteristic characteristic, final boolean usePool) throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool) throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool) throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set,
	 *      boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics, final boolean usePool) throws ApplicationException {
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
