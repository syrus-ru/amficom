/*
 * $Id: EventType.java,v 1.22 2005/05/24 13:24:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.event.corba.EventType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.22 $, $Date: 2005/05/24 13:24:55 $
 * @author $Author: bass $
 * @module event_v1
 */

public final class EventType extends StorableObjectType {
	private static final long serialVersionUID = -8660055955879452510L;

	public static final String CODENAME_MEASUREMENT_ALARM = "measurement_alarm";

	private Set parameterTypes;
	private Set userIds;

	public EventType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.parameterTypes = new HashSet();
		this.userIds = new HashSet();

		EventTypeDatabase database = (EventTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.EVENTTYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public EventType(EventType_Transferable ett) throws CreateObjectException {
		try {
			this.fromTransferable(ett);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected EventType(Identifier id,
								Identifier creatorId,
								long version,
								String codename,
								String description,
								Set parameterTypes,
								Set userIds) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.parameterTypes = new HashSet();
		this.setParameterTypes0(parameterTypes);

		this.userIds = new HashSet();
		this.setUserIds0(userIds);
	}

	/**
	 * Create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param parameterTypes
	 * @return a newly generated object
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static EventType createInstance(Identifier creatorId,
															String codename,
															String description,
															Set parameterTypes,
															Set userIds) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null)
			throw new IllegalArgumentException("Argument is null'");

		try {
			EventType eventType = new EventType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTTYPE_ENTITY_CODE),
										creatorId,
										0L,
										codename,
										description,
										parameterTypes,
										userIds);

			assert eventType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			eventType.changed = true;
			return eventType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		EventType_Transferable ett = (EventType_Transferable) transferable;

		super.fromTransferable(ett.header, ett.codename, ett.description);

		Set ids = Identifier.fromTransferables(ett.parameter_type_ids);
		this.parameterTypes = new HashSet(ett.parameter_type_ids.length);
		this.setParameterTypes0(StorableObjectPool.getStorableObjects(ids, true));

		this.userIds = new HashSet(ett.user_ids.length);
		this.setUserIds0(Identifier.fromTransferables(ett.user_ids));

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		Identifier_Transferable[] parTypeIdsT = Identifier.createTransferables(this.parameterTypes);
		Identifier_Transferable[] userIdsT = Identifier.createTransferables(this.userIds);
		return new EventType_Transferable(super.getHeaderTransferable(),
										super.codename,
										super.description != null ? super.description : "",
										parTypeIdsT,
										userIdsT);
	}

  public Set getParameterTypes() {
		return Collections.unmodifiableSet(this.parameterTypes);
	}

  public Set getUserIds() {
		return Collections.unmodifiableSet(this.userIds);
	}

  /**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid()
				&& this.parameterTypes != null && this.parameterTypes != Collections.EMPTY_SET
				&& this.userIds != null && this.userIds != Collections.EMPTY_SET;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						long version,
																						String codename,
																						String description) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							codename,
							description);
	}

	protected void setParameterTypes0(Set parameterTypes) {
		this.parameterTypes.clear();
		if (parameterTypes != null)
	     	this.parameterTypes.addAll(parameterTypes);
	}

	/**
	 * client setter for parameterTypes
	 *
	 * @param parameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setParameterTypes(Set parameterTypes) {
		this.setParameterTypes0(parameterTypes);
		this.changed = true;
	}

	protected void setUserIds0(Set userIds) {
		this.userIds.clear();
		if (userIds != null)
	     	this.userIds.addAll(userIds);
	}

	/**
	 * client setter for userIds
	 *
	 * @param userIds
	 *            The userIds to set.
	 */
	public void setUserIds(Set userIds) {
		this.setUserIds0(userIds);
		this.changed = true;
	}

	public boolean hasUserId(Identifier userId) {
		return this.userIds.contains(userId);
	}

	public void addUserId(Identifier userId) {
		if (userId != null && !this.hasUserId(userId)) {
			this.userIds.add(userId);
			super.changed = true;
		}
	}

	public void removeUserId(Identifier userId) {
		if (userId != null && this.hasUserId(userId)) {
			this.userIds.remove(userId);
			super.changed = true;
		}
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.addAll(this.parameterTypes);
		dependencies.addAll(this.userIds);
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
}
