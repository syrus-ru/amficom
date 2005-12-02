/*-
 * $Id: DeliveryAttributes.java,v 1.9 2005/12/02 11:24:21 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.DELIVERYATTRIBUTES_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.event.corba.IdlDeliveryAttributes;
import com.syrus.AMFICOM.event.corba.IdlDeliveryAttributesHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/12/02 11:24:21 $
 * @module event
 */
public final class DeliveryAttributes extends StorableObject<DeliveryAttributes> {
	private static final long serialVersionUID = -8861427452530992582L;

	private Severity severity;

	private Set<Identifier> systemUserIds;

	private Set<Identifier> roleIds;

	DeliveryAttributes(final Identifier id,
			final Identifier creatorId,
			final Date created,
			final StorableObjectVersion version,
			final Severity severity) {
		super(id, created, created, creatorId, creatorId, version);
		this.severity = severity;
	}

	/**
	 * @param deliveryAttributes
	 * @throws CreateObjectException
	 */
	public DeliveryAttributes(final IdlDeliveryAttributes deliveryAttributes)
	throws CreateObjectException {
		try {
			this.fromTransferable(deliveryAttributes);
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.getRoleIds0());
		dependencies.addAll(this.getSystemUserIds0());
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @param orb
	 * @see StorableObject#getTransferable(ORB)
	 */
	@Override
	public IdlStorableObject getTransferable(final ORB orb) {
		return IdlDeliveryAttributesHelper.init(orb,
				this.id.getTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(orb),
				this.modifierId.getTransferable(orb),
				this.version.longValue(),
				this.severity.getTransferable(orb),
				Identifier.createTransferables(this.getRoleIds0()),
				Identifier.createTransferables(this.getSystemUserIds0()));
	}

	/**
	 * @param transferable
	 * @throws ApplicationException
	 * @see StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable)
	throws ApplicationException {
		synchronized (this) {
			final IdlDeliveryAttributes deliveryAttributes = (IdlDeliveryAttributes) transferable;
			super.fromTransferable(deliveryAttributes);
			this.severity = Severity.valueOf(deliveryAttributes.severity);
			this.setRoleIds0(Identifier.fromTransferables(deliveryAttributes.roleIds));
			this.setSystemUserIds0(Identifier.fromTransferables(deliveryAttributes.systemUserIds));
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected DeliveryAttributesWrapper getWrapper() {
		return DeliveryAttributesWrapper.getInstance();
	}

	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Severity severity) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.severity = severity;
	}

	/**
	 * This method either returns an object previously saved in a database,
	 * if one found (invoking {@link StorableObjectPool#refresh(Set) on it}),
	 * or creates a new one otherwise (invoking
	 * {@link StorableObjectPool#flush(Identifiable, Identifier, boolean)}
	 * on it). Thus, a {@code StorableObject} returned by this method is,
	 * first, always unchanged and, second, has the most recent version.
	 *
	 * @param creatorId
	 * @param severity
	 * @throws CreateObjectException
	 */
	public static DeliveryAttributes getInstance(final Identifier creatorId,
			final Severity severity)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert severity != null : NON_NULL_EXPECTED;

		try {
			final StorableObjectCondition condition = new TypicalCondition(
					severity,
					OPERATION_EQUALS,
					DELIVERYATTRIBUTES_CODE,
					COLUMN_SEVERITY);
			final Set<DeliveryAttributes> deliveryAttributesSet =
						StorableObjectPool.getStorableObjectsByCondition(condition, true);
			final DeliveryAttributes deliveryAttributes;
			if (deliveryAttributesSet.isEmpty()) {
				deliveryAttributes = new DeliveryAttributes(
						IdentifierPool.getGeneratedIdentifier(DELIVERYATTRIBUTES_CODE),
						creatorId,
						new Date(),
						StorableObjectVersion.INITIAL_VERSION,
						severity);
				deliveryAttributes.markAsChanged();
				StorableObjectPool.flush(deliveryAttributes, creatorId, false);
			} else {
				final int size = deliveryAttributesSet.size();
				assert size == 1 : size;
				deliveryAttributes = deliveryAttributesSet.iterator().next();
				StorableObjectPool.refresh(Collections.singleton(deliveryAttributes.getId()));
			}
			return deliveryAttributes;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public Severity getSeverity() {
		return this.severity;
	}

	/*-********************************************************************
	 * Children manipulation: systemUserIds.                              *
	 **********************************************************************/

	public void addSystemUserId(final Identifier systemUserId) {
		if (systemUserId == null) {
			throw new NullPointerException();
		} else if (systemUserId.getMajor() != SYSTEMUSER_CODE || systemUserId.isVoid()) {
			throw new IllegalArgumentException(systemUserId.toString());
		}

		this.getSystemUserIds0().add(systemUserId);
		this.markAsChanged();
	}

	public void removeSystemUserId(final Identifier systemUserId) {
		if (systemUserId == null) {
			throw new NullPointerException();
		} else if (systemUserId.getMajor() != SYSTEMUSER_CODE || systemUserId.isVoid()) {
			throw new IllegalArgumentException(systemUserId.toString());
		}

		this.getSystemUserIds0().remove(systemUserId);
		this.markAsChanged();
	}

	private Set<Identifier> getSystemUserIds0() {
		return this.systemUserIds == null
				? this.systemUserIds = new HashSet<Identifier>()
				: this.systemUserIds;
	}

	public Set<Identifier> getSystemUserIds() {
		return Collections.unmodifiableSet(this.getSystemUserIds0());
	}

	/**
	 * @param systemUserIds
	 */
	void setSystemUserIds0(final Set<Identifier> systemUserIds) {
		final Set<Identifier> systemUserIds0 = this.getSystemUserIds0();
		systemUserIds0.clear();
		systemUserIds0.addAll(systemUserIds);
	}

	public void setSystemUserIds(final Set<Identifier> systemUserIds) {
		if (systemUserIds == null) {
			throw new NullPointerException();
		}

		final Set<Identifier> oldSystemUserIds = this.getSystemUserIds0();

		final Set<Identifier> toRemove = new HashSet<Identifier>(oldSystemUserIds);
		toRemove.removeAll(systemUserIds);
		for (final Identifier systemUserId : toRemove) {
			this.removeSystemUserId(systemUserId);
		}

		final Set<Identifier> toAdd = new HashSet<Identifier>(systemUserIds);
		toAdd.removeAll(oldSystemUserIds);
		for (final Identifier systemUserId : toAdd) {
			this.addSystemUserId(systemUserId);
		}
	}

	/*-********************************************************************
	 * Children manipulation: systemUsers.                                *
	 **********************************************************************/

	public void addSystemUser(final SystemUser systemUser) {
		if (systemUser == null) {
			throw new NullPointerException();
		}

		this.addSystemUserId(systemUser.getId());
	}

	public void removeSystemUser(final SystemUser systemUser) {
		if (systemUser == null) {
			throw new NullPointerException();
		}

		this.removeSystemUserId(systemUser.getId());
	}

	private Set<SystemUser> getSystemUsers0() throws ApplicationException {
		return new HashSet<SystemUser>(StorableObjectPool.<SystemUser>getStorableObjects(this.getSystemUserIds0(), true));
	}

	public Set<SystemUser> getSystemUsers() throws ApplicationException {
		return Collections.unmodifiableSet(this.getSystemUsers0());
	}

	public void setSystemUsers(final Set<SystemUser> systemUsers) {
		if (systemUsers == null) {
			throw new NullPointerException();
		}

		this.setSystemUserIds(Identifier.createIdentifiers(systemUsers));
	}

	/*-********************************************************************
	 * Children manipulation: roleIds.                                    *
	 **********************************************************************/

	public void addRoleId(final Identifier roleId) {
		if (roleId == null) {
			throw new NullPointerException();
		} else if (roleId.getMajor() != ROLE_CODE || roleId.isVoid()) {
			throw new IllegalArgumentException(roleId.toString());
		}

		this.getRoleIds0().add(roleId);
		this.markAsChanged();
	}

	public void removeRoleId(final Identifier roleId) {
		if (roleId == null) {
			throw new NullPointerException();
		} else if (roleId.getMajor() != ROLE_CODE || roleId.isVoid()) {
			throw new IllegalArgumentException(roleId.toString());
		}

		this.getRoleIds0().remove(roleId);
		this.markAsChanged();
	}

	private Set<Identifier> getRoleIds0() {
		return this.roleIds == null
				? this.roleIds = new HashSet<Identifier>()
				: this.roleIds;
	}

	public Set<Identifier> getRoleIds() {
		return Collections.unmodifiableSet(this.getRoleIds0());
	}

	/**
	 * @param roleIds
	 */
	void setRoleIds0(final Set<Identifier> roleIds) {
		final Set<Identifier> roleIds0 = this.getRoleIds0();
		roleIds0.clear();
		roleIds0.addAll(roleIds);
	}

	public void setRoleIds(final Set<Identifier> roleIds) {
		if (roleIds == null) {
			throw new NullPointerException();
		}

		final Set<Identifier> oldRoleIds = this.getRoleIds0();

		final Set<Identifier> toRemove = new HashSet<Identifier>(oldRoleIds);
		toRemove.removeAll(roleIds);
		for (final Identifier roleId : toRemove) {
			this.removeRoleId(roleId);
		}

		final Set<Identifier> toAdd = new HashSet<Identifier>(roleIds);
		toAdd.removeAll(oldRoleIds);
		for (final Identifier roleId : toAdd) {
			this.addRoleId(roleId);
		}
	}

	/*-********************************************************************
	 * Children manipulation: roles.                                      *
	 **********************************************************************/

	public void addRole(final Role role) {
		if (role == null) {
			throw new NullPointerException();
		}

		this.addRoleId(role.getId());
	}

	public void removeRole(final Role role) {
		if (role == null) {
			throw new NullPointerException();
		}

		this.removeRoleId(role.getId());
	}

	private Set<Role> getRoles0() throws ApplicationException {
		return new HashSet<Role>(StorableObjectPool.<Role>getStorableObjects(this.getRoleIds0(), true));
	}

	public Set<Role> getRoles() throws ApplicationException {
		return Collections.unmodifiableSet(this.getRoles0());
	}

	public void setRoles(final Set<Role> roles) {
		if (roles == null) {
			throw new NullPointerException();
		}

		this.setRoleIds(Identifier.createIdentifiers(roles));
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * @return a full {@link Set} of {@link SystemUser}s associated with
	 *         this {@link DeliveryAttributes}, either indirectly via
	 *         associated {@link Role}s, or directly.
	 * @throws ApplicationException
	 */
	public Set<SystemUser> getSystemUsersRecursively() throws ApplicationException {
		final Set<SystemUser> systemUsers = new HashSet<SystemUser>();
		systemUsers.addAll(this.getSystemUsers0());
		for (final Role role : this.getRoles0()) {
			systemUsers.addAll(role.getSystemUsers());
		}
		return Collections.unmodifiableSet(systemUsers);
	}
}
