/*
 * $Id: EventType.java,v 1.57.2.3 2006/03/15 15:45:33 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @version $Revision: 1.57.2.3 $, $Date: 2006/03/15 15:45:33 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */

public final class EventType extends StorableObjectType {
	private static final long serialVersionUID = -5205946777012405394L;

	public static final String CODENAME_MEASUREMENT_ALARM = "measurement_alarm";

	private Set<Identifier> parameterTypeIds;
	private Map<Identifier, Set<AlertKind>> userAlertKindsMap;	//Map <Identifier userId, Set <AlertKind> alertKinds>

	public EventType(final IdlEventType ett) throws CreateObjectException {
		try {
			this.fromIdlTransferable(ett);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	EventType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final Set<Identifier> parameterTypeIds,
			final Map<Identifier, Set<AlertKind>> userAlertKindsMap) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.parameterTypeIds = new HashSet<Identifier>();
		this.setParameterTypes0(parameterTypeIds);

		this.userAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
		this.setUserAlertKindsMap0(userAlertKindsMap);
	}

	/**
	 * Create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param parameterTypeIds
	 * @param userAlertKindsMap
	 * @return a newly generated object
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static EventType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final Set<Identifier> parameterTypeIds,
			final Map<Identifier, Set<AlertKind>> userAlertKindsMap) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null)
			throw new IllegalArgumentException("Argument is null'");

		try {
			final EventType eventType = new EventType(IdentifierPool.getGeneratedIdentifier(EVENT_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description,
					parameterTypeIds,
					userAlertKindsMap);

			assert eventType.isValid() : OBJECT_STATE_ILLEGAL;

			eventType.markAsChanged();

			return eventType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromIdlTransferable(final IdlStorableObject transferable) throws IdlConversionException {
		final IdlEventType ett = (IdlEventType) transferable;

		super.fromTransferable(ett, ett.codename, ett.description);

		this.setParameterTypes0(Identifier.fromTransferables(ett.parameterTypeIds));

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

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEventType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

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
			userAlertKindsT[i] = new IdlUserAlertKinds(userId.getIdlTransferable(), alertKindsT);
		}

		return IdlEventTypeHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				Identifier.createTransferables(this.parameterTypeIds),
				userAlertKindsT);
	}

  /**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.parameterTypeIds != null && this.parameterTypeIds != Collections.EMPTY_SET
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

	public Set<Identifier> getParameterTypeIds() {
		return Collections.unmodifiableSet(this.parameterTypeIds);
	}

	protected void setParameterTypes0(final Set<Identifier> parameterTypeIds) {
		this.parameterTypeIds.clear();
		if (parameterTypeIds != null) {
			this.parameterTypeIds.addAll(parameterTypeIds);
		}
	}

	/**
	 * client setter for parameterTypeIds
	 * 
	 * @param parameterTypes
	 *        The inParameterTypeIds to set.
	 */
	public void setParameterTypes(final Set<Identifier> parameterTypeIds) {
		this.setParameterTypes0(parameterTypeIds);
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
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.userAlertKindsMap.keySet());
		return dependencies;
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

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected EventTypeWrapper getWrapper() {
		return EventTypeWrapper.getInstance();
	}
}
