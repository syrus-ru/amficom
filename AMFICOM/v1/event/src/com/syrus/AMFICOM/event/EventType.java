/*
 * $Id: EventType.java,v 1.46 2005/08/19 16:34:56 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.event.corba.IdlEventType;
import com.syrus.AMFICOM.event.corba.IdlEventTypeHelper;
import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.AlertKind;
import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.IdlUserAlertKinds;
import com.syrus.AMFICOM.general.ApplicationException;
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
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlParameterType;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.46 $, $Date: 2005/08/19 16:34:56 $
 * @author $Author: arseniy $
 * @module event
 */

public final class EventType extends StorableObjectType {
	private static final long serialVersionUID = -8660055955879452510L;

	public static final String CODENAME_MEASUREMENT_ALARM = "measurement_alarm";

	private Set<ParameterType> parameterTypes;
	private Map<Identifier, Set<AlertKind>> userAlertKindsMap;	//Map <Identifier userId, Set <AlertKind> alertKinds>

	EventType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.parameterTypes = new HashSet<ParameterType>();
		this.userAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();

		try {
			DatabaseContext.getDatabase(ObjectEntities.EVENT_TYPE_CODE).retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public EventType(final IdlEventType ett) throws CreateObjectException {
		try {
			this.fromTransferable(ett);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	EventType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final Set<ParameterType> parameterTypeIds,
			final Map<Identifier, Set<AlertKind>> userAlertKindsMap) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.parameterTypes = new HashSet<ParameterType>();
		this.setParameterTypes0(parameterTypeIds);

		this.userAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
		this.setUserAlertKindsMap0(userAlertKindsMap);
	}

	/**
	 * Create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param parameterTypes
	 * @param userAlertKindsMap
	 * @return a newly generated object
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static EventType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<ParameterType> parameterTypes,
			final Map<Identifier, Set<AlertKind>> userAlertKindsMap) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null)
			throw new IllegalArgumentException("Argument is null'");

		try {
			final EventType eventType = new EventType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					parameterTypes,
					userAlertKindsMap);

			assert eventType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			eventType.markAsChanged();

			return eventType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlEventType ett = (IdlEventType) transferable;

		super.fromTransferable(ett, ett.codename, ett.description);

		this.parameterTypes = ParameterType.fromTransferables(ett.parameterTypes);

		this.userAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>(ett.userAlertKinds.length);
		for (int i = 0; i < ett.userAlertKinds.length; i++) {
			final IdlUserAlertKinds userAlertKindsT = ett.userAlertKinds[i];
			final Identifier userId = new Identifier(userAlertKindsT.userId);
			final Set<AlertKind> userAlertKinds = new HashSet<AlertKind>(userAlertKindsT.alertKinds.length);
			for (int j = 0; j < userAlertKindsT.alertKinds.length; j++) {
				userAlertKinds.add(userAlertKindsT.alertKinds[j]);
			}
			this.userAlertKindsMap.put(userId, userAlertKinds);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEventType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlParameterType[] parTypesT = ParameterType.createTransferables(this.parameterTypes, orb);

		final IdlUserAlertKinds[] userAlertKindsT = new IdlUserAlertKinds[this.userAlertKindsMap.size()];
		int i, j;
		i = 0;
		for (final Identifier userId : this.userAlertKindsMap.keySet()) {
			final Set<AlertKind> userAlertKinds = this.userAlertKindsMap.get(userId);
			final AlertKind[] alertKindsT = new AlertKind[userAlertKinds.size()];
			j = 0;
			for (final AlertKind alertKind : userAlertKinds) {
				alertKindsT[j] = alertKind;
			}
			userAlertKindsT[i] = new IdlUserAlertKinds(userId.getTransferable(), alertKindsT);
		}

		return IdlEventTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				parTypesT,
				userAlertKindsT);
	}

  /**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.parameterTypes != null && this.parameterTypes != Collections.EMPTY_SET
				&& this.userAlertKindsMap != null && this.userAlertKindsMap != Collections.EMPTY_MAP;
	}

	@Override
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
	}

	public Set<ParameterType> getParameterTypes() {
		return Collections.unmodifiableSet(this.parameterTypes);
	}

	protected void setParameterTypes0(final Set<ParameterType> parameterTypes) {
		this.parameterTypes.clear();
		if (parameterTypes != null) {
			this.parameterTypes.addAll(parameterTypes);
		}
	}

	/**
	 * client setter for parameterTypeIds
	 * 
	 * @param parameterTypes
	 *        The inParameterTypeIds to set.
	 */
	public void setParameterTypes(final Set<ParameterType> parameterTypes) {
		this.setParameterTypes0(parameterTypes);
		this.markAsChanged();
	}

	public Set<Identifier> getAlertedUserIds() {
		return this.userAlertKindsMap.keySet();
	}

	public Set<AlertKind> getUserAlertKinds(final Identifier userId) {
		final Set<AlertKind> userAlertKinds = this.userAlertKindsMap.get(userId);
		if (userAlertKinds != null) {
			return userAlertKinds;
		}
		return Collections.emptySet();
	}

	protected Map<Identifier, Set<AlertKind>> getUserAlertKindsMap() {
		return Collections.unmodifiableMap(this.userAlertKindsMap);
	}

	protected void setUserAlertKindsMap0(final Map<Identifier, Set<AlertKind>> userAlertKindsMap) {
		this.userAlertKindsMap.clear();
		if (userAlertKindsMap != null) {
			this.userAlertKindsMap.putAll(userAlertKindsMap);
		}
	}

	public void addAlertKindToUser(final Identifier userId, final AlertKind alertKind) {
		assert (userId != null) : "User id is NULL";
		assert (alertKind != null) : "Alert kind is NULL";
		
		Set<AlertKind> userAlertKinds = this.userAlertKindsMap.get(userId);
		if (userAlertKinds == null) {
			userAlertKinds = new HashSet<AlertKind>();
			this.userAlertKindsMap.put(userId, userAlertKinds);
		}
		userAlertKinds.add(alertKind);
		super.markAsChanged();
	}

	public void removeAlertKindFromUser(final Identifier userId, final AlertKind alertKind) {
		assert (userId != null) : "User id is NULL";
		assert (alertKind != null) : "Alert kind is NULL";
		
		final Set<AlertKind> userAlertKinds = this.userAlertKindsMap.get(userId);
		if (userAlertKinds != null) {
			userAlertKinds.remove(alertKind);
			if (userAlertKinds.isEmpty())
				this.userAlertKindsMap.remove(userId);
			super.markAsChanged();
		}
	}

	public void removeAlertKindsFromUser(final Identifier userId) {
		assert (userId != null) : "User id is NULL";

		if (this.userAlertKindsMap.remove(userId) != null) {
			super.markAsChanged();
		}
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.userAlertKindsMap.keySet());
		return dependencies;
	}

	@Override
	public String toString() {
		final String str = getClass().getName() + EOSL
		+ ID + this.id + EOSL
		+ ID_CREATED + this.created.toString() + EOSL
		+ ID_CREATOR_ID + this.creatorId.toString() + EOSL
		+ ID_MODIFIED + this.modified.toString() + EOSL
		+ ID_MODIFIER_ID + this.modifierId.toString() + EOSL
		+ TypedObject.ID_CODENAME + this.codename+ EOSL
		+ TypedObject.ID_DESCRIPTION + this.description + EOSL;
		return str;
	}

	protected void printUserAlertKinds() {
		for (final Identifier userId : this.userAlertKindsMap.keySet()) {
			System.out.println("User '" + userId + "'");
			final Set<AlertKind> userAlertKinds = this.userAlertKindsMap.get(userId);
			for (final AlertKind alertKind : userAlertKinds) {
				System.out.println("	alert kind: " + alertKind.value());
			}
		}
	}
}
