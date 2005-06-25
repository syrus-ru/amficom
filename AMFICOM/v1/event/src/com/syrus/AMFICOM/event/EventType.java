/*
 * $Id: EventType.java,v 1.34 2005/06/25 17:50:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.event.corba.AlertKind;
import com.syrus.AMFICOM.event.corba.UserAlertKinds;
import com.syrus.AMFICOM.event.corba.IdlEventType;
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
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.34 $, $Date: 2005/06/25 17:50:49 $
 * @author $Author: bass $
 * @module event_v1
 */

public final class EventType extends StorableObjectType {
	private static final long serialVersionUID = -8660055955879452510L;

	public static final String CODENAME_MEASUREMENT_ALARM = "measurement_alarm";

	private Set<Identifier> parameterTypeIds;
	private Map<Identifier, Set<AlertKind>> userAlertKindsMap;	//Map <Identifier userId, Set <AlertKind> alertKinds>
//	private Set userIds;

	EventType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.parameterTypeIds = new HashSet();
		this.userAlertKindsMap = new HashMap();

		final EventTypeDatabase database = (EventTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.EVENT_TYPE_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	EventType(final IdlEventType ett) throws CreateObjectException {
		try {
			this.fromTransferable(ett);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	EventType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final Set parameterTypeIds,
			final Map userAlertKindsMap) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.parameterTypeIds = new HashSet();
		this.setParameterTypeIds0(parameterTypeIds);

		this.userAlertKindsMap = new HashMap();
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
			final Set parameterTypeIds,
			final Map userAlertKindsMap) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null)
			throw new IllegalArgumentException("Argument is null'");

		try {
			EventType eventType = new EventType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENT_TYPE_CODE),
					creatorId,
					0L,
					codename,
					description,
					parameterTypeIds,
					userAlertKindsMap);

			assert eventType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			eventType.markAsChanged();

			return eventType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlEventType ett = (IdlEventType) transferable;

		super.fromTransferable(ett.header, ett.codename, ett.description);

		this.parameterTypeIds = Identifier.fromTransferables(ett.parameterTypeIds);

		this.userAlertKindsMap = new HashMap(ett.userAlertKinds.length);
		for (int i = 0; i < ett.userAlertKinds.length; i++) {
			final UserAlertKinds userAlertKindsT = ett.userAlertKinds[i];
			final Identifier userId = new Identifier(userAlertKindsT.userId);
			final Set userAlertKinds = new HashSet(userAlertKindsT.alertKinds.length);
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

		final IdlIdentifier[] parTypeIdsT = Identifier.createTransferables(this.parameterTypeIds);

		final UserAlertKinds[] userAlertKindsT = new UserAlertKinds[this.userAlertKindsMap.size()];
		int i, j;
		i = 0;
		for (final Iterator<Identifier> it1 = this.userAlertKindsMap.keySet().iterator(); it1.hasNext(); i++) {
			final Identifier userId = it1.next();
			final Set<AlertKind> userAlertKinds = this.userAlertKindsMap.get(userId);
			final AlertKind[] alertKindsT = new AlertKind[userAlertKinds.size()];
			j = 0;
			for (final Iterator<AlertKind> it2 = userAlertKinds.iterator(); it2.hasNext(); j++) {
				alertKindsT[j] = it2.next();
			}
			userAlertKindsT[i] = new UserAlertKinds(userId.getTransferable(), alertKindsT);
		}

		return new IdlEventType(
				super.getHeaderTransferable(orb),
				super.codename,
				super.description != null ? super.description : "",
				parTypeIdsT,
				userAlertKindsT);
	}

  /**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid()
				&& this.parameterTypeIds != null && this.parameterTypeIds != Collections.EMPTY_SET
				&& this.userAlertKindsMap != null && this.userAlertKindsMap != Collections.EMPTY_MAP;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
	}

  public Set getParameterTypeIds() {
		return Collections.unmodifiableSet(this.parameterTypeIds);
	}

	protected void setParameterTypeIds0(final Set parameterTypeIds) {
		this.parameterTypeIds.clear();
		if (parameterTypeIds != null)
	     	this.parameterTypeIds.addAll(parameterTypeIds);
	}

	/**
	 * client setter for parameterTypeIds
	 *
	 * @param parameterTypeIds
	 *            The inParameterTypeIds to set.
	 */
	public void setParameterTypeIds(final Set parameterTypeIds) {
		this.setParameterTypeIds0(parameterTypeIds);
		this.markAsChanged();
	}

	public Set getAlertedUserIds() {
		return this.userAlertKindsMap.keySet();
	}

	public Set getUserAlertKinds(final Identifier userId) {
		final Set userAlertKinds = (Set) this.userAlertKindsMap.get(userId);
		return (userAlertKinds != null) ? Collections.unmodifiableSet(userAlertKinds) : Collections.EMPTY_SET;
	}

	protected Map getUserAlertKindsMap() {
		return Collections.unmodifiableMap(this.userAlertKindsMap);
	}

	protected void setUserAlertKindsMap0(final Map userAlertKindsMap) {
		this.userAlertKindsMap.clear();
		if (userAlertKindsMap != null)
			this.userAlertKindsMap.putAll(userAlertKindsMap);
	}

	public void addAlertKindToUser(final Identifier userId, final AlertKind alertKind) {
		assert (userId != null) : "User id is NULL";
		assert (alertKind != null) : "Alert kind is NULL";
		
		Set userAlertKinds = (Set) this.userAlertKindsMap.get(userId);
		if (userAlertKinds == null) {
			userAlertKinds = new HashSet();
			this.userAlertKindsMap.put(userId, userAlertKinds);
		}
		userAlertKinds.add(alertKind);
		super.markAsChanged();
	}

	public void removeAlertKindFromUser(final Identifier userId, final AlertKind alertKind) {
		assert (userId != null) : "User id is NULL";
		assert (alertKind != null) : "Alert kind is NULL";
		
		final Set userAlertKinds = (Set) this.userAlertKindsMap.get(userId);
		if (userAlertKinds != null) {
			userAlertKinds.remove(alertKind);
			if (userAlertKinds.isEmpty())
				this.userAlertKindsMap.remove(userId);
			super.markAsChanged();
		}
	}

	public void removeAlertKindsFromUser(final Identifier userId) {
		assert (userId != null) : "User id is NULL";

		if (this.userAlertKindsMap.remove(userId) != null)
			super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.parameterTypeIds);
		dependencies.addAll(this.userAlertKindsMap.keySet());
		return dependencies;
	}

	public String toString() {
		String str = getClass().getName() + EOSL
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
		for (final Iterator it1 = this.userAlertKindsMap.keySet().iterator(); it1.hasNext();) {
			final Identifier userId = (Identifier) it1.next();
			System.out.println("User '" + userId + "'");
			final Set userAlertKinds = (Set) this.userAlertKindsMap.get(userId);
			for (final Iterator it2 = userAlertKinds.iterator(); it2.hasNext();) {
				final AlertKind alertKind = (AlertKind) it2.next();
				System.out.println("	alert kind: " + alertKind.value());
			}
		}
	}
}
