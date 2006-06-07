/*
 * $Id: EventType.java,v 1.57.2.5 2006/06/07 09:25:21 arseniy Exp $
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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.event.corba.IdlEventType;
import com.syrus.AMFICOM.event.corba.IdlEventTypeHelper;
import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.IdlUserAlertKinds;
import com.syrus.AMFICOM.eventv2.DeliveryMethod;
import com.syrus.AMFICOM.eventv2.corba.IdlNotificationEventPackage.IdlDeliveryMethod;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.57.2.5 $, $Date: 2006/06/07 09:25:21 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventType extends StorableObjectType implements IdlTransferableObjectExt<IdlEventType> {
	private static final long serialVersionUID = -8660055955879452510L;

	public static final String CODENAME_MEASUREMENT_ALARM = "measurement_alarm";

	/**
	 * @serial include
	 */
	private Set<Identifier> parameterTypeIds;
	/**
	 * @serial include
	 */
	private Map<Identifier, Set<DeliveryMethod>> userAlertKindsMap;	//Map <Identifier userId, Set <DeliveryMethod> alertKinds>

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
			final Map<Identifier, Set<DeliveryMethod>> userAlertKindsMap) {
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

		this.userAlertKindsMap = new HashMap<Identifier, Set<DeliveryMethod>>();
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
			final Map<Identifier, Set<DeliveryMethod>> userAlertKindsMap) throws CreateObjectException {
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

	public synchronized void fromIdlTransferable(final IdlEventType eventType) throws IdlConversionException {
		super.fromIdlTransferable(eventType, eventType.codename, eventType.description);

		this.setParameterTypes0(Identifier.fromTransferables(eventType.parameterTypeIds));

		this.userAlertKindsMap = new HashMap<Identifier, Set<DeliveryMethod>>(eventType.userAlertKinds.length);
		for (final IdlUserAlertKinds userAlertKinds : eventType.userAlertKinds) {
			final Set<DeliveryMethod> deliveryMethods = EnumSet.noneOf(DeliveryMethod.class);
			for (final IdlDeliveryMethod deliveryMethod : userAlertKinds.alertKinds) {
				deliveryMethods.add(DeliveryMethod.valueOf(deliveryMethod));
			}
			this.userAlertKindsMap.put(Identifier.valueOf(userAlertKinds.userId), deliveryMethods);
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

		final IdlUserAlertKinds[] userAlertKinds = new IdlUserAlertKinds[this.userAlertKindsMap.size()];
		int i = 0;
		for (final Entry<Identifier, Set<DeliveryMethod>> entry : this.userAlertKindsMap.entrySet()) {
			final Set<DeliveryMethod> deliveryMethods = entry.getValue();
			final IdlDeliveryMethod[] idlDeliveryMethods = new IdlDeliveryMethod[deliveryMethods.size()];
			int j = 0;
			for (final DeliveryMethod deliveryMethod : deliveryMethods) {
				idlDeliveryMethods[j++] = (new DeliveryMethod.Proxy(deliveryMethod)).getIdlTransferable(orb);
			}
			userAlertKinds[i++] = new IdlUserAlertKinds(entry.getKey().getIdlTransferable(orb), idlDeliveryMethods);
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
				userAlertKinds);
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

	public Set<DeliveryMethod> getUserAlertKinds(final Identifier userId) {
		final Set<DeliveryMethod> userAlertKinds = this.userAlertKindsMap.get(userId);
		return userAlertKinds == null
				? Collections.<DeliveryMethod>emptySet()
				: userAlertKinds;
	}

	protected Map<Identifier, Set<DeliveryMethod>> getUserAlertKindsMap() {
		return Collections.unmodifiableMap(this.userAlertKindsMap);
	}

	protected void setUserAlertKindsMap0(final Map<Identifier, Set<DeliveryMethod>> userAlertKindsMap) {
		this.userAlertKindsMap.clear();
		if (userAlertKindsMap != null) {
			this.userAlertKindsMap.putAll(userAlertKindsMap);
		}
	}

	public void addAlertKindToUser(final Identifier userId, final DeliveryMethod deliveryMethod) {
		assert (userId != null) : "User id is NULL";
		assert (deliveryMethod != null) : "Alert kind is NULL";
		
		Set<DeliveryMethod> deliveryMethods = this.userAlertKindsMap.get(userId);
		if (deliveryMethods == null) {
			deliveryMethods = EnumSet.noneOf(DeliveryMethod.class);
			this.userAlertKindsMap.put(userId, deliveryMethods);
		}
		deliveryMethods.add(deliveryMethod);
		super.markAsChanged();
	}

	public void removeAlertKindFromUser(final Identifier userId, final DeliveryMethod deliveryMethod) {
		assert (userId != null) : "User id is NULL";
		assert (deliveryMethod != null) : "Alert kind is NULL";
		
		final Set<DeliveryMethod> deliveryMethods = this.userAlertKindsMap.get(userId);
		if (deliveryMethods != null) {
			deliveryMethods.remove(deliveryMethod);
			if (deliveryMethods.isEmpty())
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
			for (final DeliveryMethod deliveryMethod : this.userAlertKindsMap.get(userId)) {
				System.out.println("	alert kind: " + deliveryMethod.getCodename());
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
