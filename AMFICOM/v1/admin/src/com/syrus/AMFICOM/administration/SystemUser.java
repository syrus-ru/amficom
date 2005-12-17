/*-
* $Id: SystemUser.java,v 1.39 2005/12/17 12:08:16 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
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

import com.syrus.AMFICOM.administration.corba.IdlSystemUser;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserHelper;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.39 $, $Date: 2005/12/17 12:08:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */

public final class SystemUser extends StorableObject<SystemUser>
		implements Characterizable, Namable {
	private static final long serialVersionUID = 7173419705878464356L;

	private String login;
	private int sort;
	private String name;
	private String description;
	private Set<Identifier> roleIds;  
	
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
			final StorableObjectVersion version,
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
		
		this.roleIds = new HashSet<Identifier>();
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
			final SystemUser systemUser = new SystemUser(generatedIdentifier,
					creatorId != null ? creatorId : generatedIdentifier,
					StorableObjectVersion.INITIAL_VERSION,
					login,
					sort.value(),
					name,
					description);

			assert systemUser.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			systemUser.markAsChanged();

			return systemUser;
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
	protected synchronized void fromTransferable(final IdlStorableObject transferable) 
	throws ApplicationException {
		final IdlSystemUser ut = (IdlSystemUser) transferable;
		try {
			super.fromTransferable(ut);
		}
		catch (final ApplicationException ae) {
			// Never
			Log.errorMessage(ae);
		}
		this.login = ut.login;
		this.sort = ut.sort.value();
		this.name = ut.name;
		this.description = ut.description;

		this.roleIds = Identifier.fromTransferables(ut.roleIds);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlSystemUser getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

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
				this.description,
				Identifier.createTransferables(this.roleIds));
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.roleIds != null 
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
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return new HashSet<Identifiable>(this.roleIds);
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
		assert role != null : ErrorMessages.NON_NULL_EXPECTED;
		this.roleIds.add(role.getId());
		super.markAsChanged();
	}

	public void removeRole(final Role role) {
		assert role != null : ErrorMessages.NON_NULL_EXPECTED;
		this.roleIds.remove(role.getId());
		super.markAsChanged();
	}

	public Set<Identifier> getRoleIds() {
		return Collections.unmodifiableSet(this.roleIds);
	}

	public void setRoleIds(final Set<Identifier> roleIds) {
		this.setRoleIds0(roleIds);
		super.markAsChanged();
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	void setRoleIds0(final Set<Identifier> roleIds) {
		this.roleIds.clear();
		if (roleIds != null) {
			this.roleIds.addAll(roleIds);
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
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
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
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
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
