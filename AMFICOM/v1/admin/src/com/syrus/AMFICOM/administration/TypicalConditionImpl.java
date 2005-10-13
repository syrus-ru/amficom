/*-
 * $Id: TypicalConditionImpl.java,v 1.24 2005/10/13 15:15:43 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort;
import com.syrus.util.Shitlet;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.24 $, $Date: 2005/10/13 15:15:43 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */
final class TypicalConditionImpl extends TypicalCondition {
	private static final long serialVersionUID = -182818603007872200L;

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final Enum e,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = e;
		this.type = TypicalSort._TYPE_ENUM;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}
	
	@SuppressWarnings("unused")
	private TypicalConditionImpl(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstInt = firstInt;
		this.secondInt = secondInt;
		this.type = TypicalSort._TYPE_NUMBER_INT;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstLong = firstLong;
		this.secondLong = secondLong;
		this.type = TypicalSort._TYPE_NUMBER_LONG;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstDouble = firstDouble;
		this.secondDouble = secondDouble;
		this.type = TypicalSort._TYPE_NUMBER_DOUBLE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final String value,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = value;
		this.type = TypicalSort._TYPE_STRING;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = firstDate;
		this.otherValue = secondDate;
		this.type = TypicalSort._TYPE_DATE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final Boolean value,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = value;
		this.type = TypicalSort._TYPE_BOOLEAN;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return !(this.type == TypicalSort._TYPE_STRING || this.type == TypicalSort._TYPE_BOOLEAN)
				|| this.operation != OperationSort._OPERATION_EQUALS
				|| identifiables == null
				|| identifiables.isEmpty();
	}

	@Override
	@Shitlet
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		if (storableObject instanceof SystemUser) {
			final SystemUser systemUser = (SystemUser) storableObject;
			final Wrapper<SystemUser> wrapper = SystemUserWrapper.getInstance();
			return super.parseCondition(wrapper.getValue(systemUser, this.key));
		} else if (storableObject instanceof Domain) {
			final Domain domain = (Domain) storableObject;
			final Wrapper<Domain> wrapper = DomainWrapper.getInstance();
			return super.parseCondition(wrapper.getValue(domain, this.key));
		} else if (storableObject instanceof Server) {
			final Server server = (Server) storableObject;
			final Wrapper<Server> wrapper = ServerWrapper.getInstance();
			return super.parseCondition(wrapper.getValue(server, this.key));
		} else if (storableObject instanceof MCM) {
			final MCM mcm = (MCM) storableObject;
			final Wrapper<MCM> wrapper = MCMWrapper.getInstance();
			return super.parseCondition(wrapper.getValue(mcm, this.key));
		} else if (storableObject instanceof ServerProcess) {
			final ServerProcess serverProcess = (ServerProcess) storableObject;
			final Wrapper<ServerProcess> wrapper = ServerProcessWrapper.getInstance();
			return super.parseCondition(wrapper.getValue(serverProcess, this.key));
		} else if (storableObject instanceof PermissionAttributes) {
			final PermissionAttributes permissionAttributes = (PermissionAttributes) storableObject;
			final Wrapper<PermissionAttributes> wrapper = PermissionAttributesWrapper.getInstance();
			return super.parseCondition(wrapper.getValue(permissionAttributes, this.key));
		} else if (storableObject instanceof Role) {
			final Role role = (Role) storableObject;
			final Wrapper<Role> wrapper = RoleWrapper.getInstance();
			return super.parseCondition(wrapper.getValue(role, this.key));
		} else {
			throw new IllegalObjectEntityException(ERROR_ENTITY_NOT_REGISTERED + storableObject.getClass().getName(),
					IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}
}
