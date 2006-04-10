/*-
 * $Id: Role.java,v 1.18 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlRole;
import com.syrus.AMFICOM.administration.corba.IdlRoleHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.18 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module administration
 */

public final class Role extends StorableObject implements Describable, IdlTransferableObjectExt<IdlRole> {
	private static final long serialVersionUID = -2103470335943889067L;

	public enum RoleCodename {
		SUBSCRIBER("Subscriber"),
		SYSTEM_ADMINISTATOR("SystemAdministator"),
		MEDIA_MONITORING_ADMINISTATOR("MediaMonitoringAdministator"),
		ANALYST("Analyst"),
		OPERATOR("Operator"),
		PLANNER("Planner"),
		SPECIALIST("Specialist");
		
		private static final String KEY_ROOT = "Role.Description."; 
		
		private final String codename;
		
		private RoleCodename(final String codename) {
			this.codename = codename;
		}
		
		public final String getDescription() {
			return LangModelAdministation.getString(KEY_ROOT + this.codename);
		}
		
		public final String getCodename() {
			return this.codename;
		}
	}
	
	private String codename;
	private String description;

	private Set<Identifier> systemUserIds;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Role(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final Set<Identifier> systemUserIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.codename = codename;
		this.description = description;

		this.systemUserIds = new HashSet<Identifier>();
		this.setSystemUserIds0(systemUserIds);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 * 
	 * @throws CreateObjectException
	 */
	public Role(final IdlRole idlRole) throws CreateObjectException {
		this.systemUserIds = new HashSet<Identifier>();
		try {
			this.fromIdlTransferable(idlRole);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * client constructor
	 * 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static Role createInstance(final Identifier creatorId, final String codename, final String description)
			throws CreateObjectException {
		try {
			final Identifier generatedIdentifier = IdentifierPool.getGeneratedIdentifier(ROLE_CODE);
			final Role role = new Role(generatedIdentifier,
					creatorId != null ? creatorId : generatedIdentifier,
					INITIAL_VERSION,
					codename,
					description,
					Collections.<Identifier> emptySet());

			assert role.isValid() : OBJECT_STATE_ILLEGAL;

			role.markAsChanged();

			return role;
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
	public synchronized void fromIdlTransferable(final IdlRole idlRole) throws IdlConversionException {
		super.fromIdlTransferable(idlRole);
		this.codename = idlRole.codename;
		this.description = idlRole.description;

		this.setSystemUserIds0(Identifier.fromTransferables(idlRole.systemUserIds));

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlRole getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlRoleHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.codename,
				this.description,
				Identifier.createTransferables(this.systemUserIds));
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.codename != null && this.codename.length() != 0
				&& this.description != null
				&& this.systemUserIds != null
				&& (this.systemUserIds.isEmpty() || StorableObject.getEntityCodeOfIdentifiables(this.systemUserIds) == SYSTEMUSER_CODE);
	}

	public String getCodename() {
		return this.codename;
	}

	public String getName() {
		return this.description;
	}

	public void setName(final String name) {
		this.setDescription(name);
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Set<Identifier> getSystemUserIds() {
		return Collections.unmodifiableSet(this.systemUserIds);
	}

	public Set<SystemUser> getSystemUsers() throws ApplicationException {
		return StorableObjectPool.getStorableObjects(this.systemUserIds, true);
	}

	public void addSystemUserId(final Identifier systemUserId) {
		assert systemUserId != null : NON_NULL_EXPECTED;
		assert systemUserId.getMajor() == SYSTEMUSER_CODE : ILLEGAL_ENTITY_CODE;

		this.systemUserIds.add(systemUserId);
		super.markAsChanged();
	}

	public void addSystemUser(final SystemUser systemUser) {
		assert systemUser != null : NON_NULL_EXPECTED;

		this.addSystemUserId(systemUser.getId());
	}

	public void removeSystemUserId(final Identifier systemUserId) {
		assert systemUserId != null : NON_NULL_EXPECTED;
		assert systemUserId.getMajor() == SYSTEMUSER_CODE : ILLEGAL_ENTITY_CODE;

		if (this.systemUserIds.remove(systemUserId)) {
			super.markAsChanged();
		}
	}

	public void removeSystemUser(final SystemUser systemUser) {
		assert systemUser != null : NON_NULL_EXPECTED;

		this.removeSystemUserId(systemUser.getId());
	}

	protected synchronized void setSystemUserIds0(final Set<Identifier> systemUserIds) {
		this.systemUserIds.clear();
		if (systemUserIds != null) {
			this.systemUserIds.addAll(systemUserIds);
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
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.codename = codename;
		this.description = description;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies =  new HashSet<Identifiable>();
		dependencies.addAll(this.systemUserIds);
		return dependencies;
	}

	public void setCodename(final String codename) {
		this.codename = codename;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected RoleWrapper getWrapper() {
		return RoleWrapper.getInstance();
	}

}
