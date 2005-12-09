/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.38 2005/12/09 15:04:00 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERMATTR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.38 $, $Date: 2005/12/09 15:04:00 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	private static final long serialVersionUID = -5632280454863134902L;

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Identifier domainId = domainMember.getDomainId();
			if (domainId.isVoid()) {
				return false;
			}
			final Domain dmDomain = StorableObjectPool.getStorableObject(domainId, true);
			for (final Identifiable identifiable : this.linkedIdentifiables) {
				final Identifier id = identifiable.getId();
				if (id.getMajor() == DOMAIN_CODE) {
					final Domain domain = StorableObjectPool.getStorableObject(id, true);
					if (dmDomain.equals(domain) || dmDomain.isChild(domain)) {
						condition = true;
					}
				}
			}
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
		}
		return condition;
	}

	private boolean checkDomain(final PermissionAttributes permissionAttributes) {
		boolean condition = false;
		try {
			final Identifier domainId = permissionAttributes.getDomainId();
			if (domainId.isVoid()) {
				// In case of Role
				return false;
			}
			final Domain dmDomain = StorableObjectPool.getStorableObject(domainId, true);
			for (final Identifiable identifiable : this.linkedIdentifiables) {
				final Identifier id = identifiable.getId();
				if (id.getMajor() == DOMAIN_CODE) {
					final Domain domain = StorableObjectPool.getStorableObject(id, true);
					if (dmDomain.equals(domain) || dmDomain.isChild(domain))
						condition = true;
				}
			}
		}
		catch (final ApplicationException ae) {
			Log.errorMessage(ae);
		}
		return condition;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case SYSTEMUSER_CODE:
				final SystemUser systemUser = (SystemUser) storableObject;
				switch (this.linkedEntityCode) {
					case ROLE_CODE:
						condition = super.conditionTest(systemUser.getRoleIds());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case DOMAIN_CODE:
				final Domain domain = (Domain) storableObject;
				condition = this.checkDomain(domain);
				break;
			case SERVER_CODE:
				final Server server = (Server) storableObject;
				condition = this.checkDomain(server);
				break;
			case MCM_CODE:
				final MCM mcm = (MCM) storableObject;
				switch (this.linkedEntityCode) {
					case SERVER_CODE:
						condition = super.conditionTest(mcm.getServerId());
						break;
					case DOMAIN_CODE:
						condition = this.checkDomain(mcm);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case PERMATTR_CODE:
				final PermissionAttributes permissionAttributes = (PermissionAttributes) storableObject;
				switch (this.linkedEntityCode) {
					case DOMAIN_CODE:
						condition = this.checkDomain(permissionAttributes);
						break;
					case ROLE_CODE:
					case SYSTEMUSER_CODE:
						condition = super.conditionTest(permissionAttributes.getParentId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
							+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
						
				}			
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

		return condition;
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case SYSTEMUSER_CODE:
			case DOMAIN_CODE:
			case SERVER_CODE:
			case MCM_CODE:
			case PERMATTR_CODE:
			case ROLE_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}
}
