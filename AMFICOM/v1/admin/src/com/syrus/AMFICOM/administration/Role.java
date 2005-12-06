/*-
 * $Id: Role.java,v 1.12 2005/12/06 09:41:12 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlRole;
import com.syrus.AMFICOM.administration.corba.IdlRoleHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/12/06 09:41:12 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module administration
 */

public final class Role extends StorableObject<Role>
		implements Describable {
	private static final long serialVersionUID = 1530119194975831896L;

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

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @throws CreateObjectException 
	 */
	public Role(final IdlRole rt) throws CreateObjectException {
		try {
			this.fromTransferable(rt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Role(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.codename = codename;
		this.description = description;
	}

	/**
	 * client constructor
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static Role createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws CreateObjectException {
		try {
			final Identifier generatedIdentifier = IdentifierPool.getGeneratedIdentifier(ObjectEntities.ROLE_CODE);
			final Role role = new Role(generatedIdentifier,
					creatorId != null ? creatorId : generatedIdentifier,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					description);

			assert role.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			role.markAsChanged();

			return role;
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
		final IdlRole rt = (IdlRole) transferable;
		try {
			super.fromTransferable(rt);
		}
		catch (final ApplicationException ae) {
			// Never
			Log.errorMessage(ae);
		}
		this.codename = rt.codename;
		this.description = rt.description;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlRole getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlRoleHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.codename,
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
				&& this.codename != null && this.codename.length() != 0
				&& this.description != null;
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
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return Collections.emptySet();
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


	private Set<SystemUser> getSystemUsers0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(this.id, SYSTEMUSER_CODE),
				true);
	}

	/**
	 * @return a {@link Set} of {@link SystemUser}s associated with this
	 *         {@link Role}.
	 * @throws ApplicationException
	 * @todo caching
	 */
	public Set<SystemUser> getSystemUsers() throws ApplicationException {
		return Collections.unmodifiableSet(this.getSystemUsers0());
	}
}
