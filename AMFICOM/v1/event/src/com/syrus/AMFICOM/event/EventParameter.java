/*
 * $Id: EventParameter.java,v 1.6 2005/02/28 15:31:36 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.event.corba.EventParameter_Transferable;
import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterSort;
import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterValue;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/28 15:31:36 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventParameter extends StorableObject implements TypedObject {
	private static final long serialVersionUID = -8007306186355527479L;

	private ParameterType type;
	private Identifier eventId;
	private int sort;
	private int valueNumber;
	private String valueString;
	private byte[] valueRaw;

	private StorableObjectDatabase eventParameterDatabase;

	public EventParameter(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.eventParameterDatabase = EventDatabaseContext.eventParameterDatabase;
		try {
			this.eventParameterDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public EventParameter(EventParameter_Transferable ept) throws CreateObjectException {
		super(ept.header);
 
		try {
			this.type = (ParameterType) GeneralStorableObjectPool.getStorableObject(new Identifier(ept.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.eventId = new Identifier(ept.event_id);
		this.sort = ept.sort.value();
		switch (this.sort) {
			case EventParameterSort._PARAMETER_SORT_NUMBER:
				this.valueNumber = ept.value.value_number();
				break;
			case EventParameterSort._PARAMETER_SORT_STRING:
				this.valueString = new String(ept.value.value_string());
				break;
			case EventParameterSort._PARAMETER_SORT_RAW:
				byte[] ba = ept.value.value_raw();
				this.valueRaw = new byte[ba.length];
				for (int i = 0; i < this.valueRaw.length; i++)
					this.valueRaw[i] = ba[i];
				break;
			default:
				Log.errorMessage("EventParameter.<init> | Illegal event parameter sort: " + this.sort);
		}

		this.eventParameterDatabase = EventDatabaseContext.eventParameterDatabase;
	}

	protected EventParameter(Identifier id, Identifier creatorId, long version, ParameterType type, Identifier eventId, int value) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);

		this.type = type;
		this.eventId = eventId;
		this.sort = EventParameterSort._PARAMETER_SORT_NUMBER;
		this.valueNumber = value;

		this.eventParameterDatabase = EventDatabaseContext.eventParameterDatabase;
	}

	protected EventParameter(Identifier id, Identifier creatorId, long version, ParameterType type, Identifier eventId, String value) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);

		this.type = type;
		this.eventId = eventId;
		this.sort = EventParameterSort._PARAMETER_SORT_STRING;
		this.valueString = value;

		this.eventParameterDatabase = EventDatabaseContext.eventParameterDatabase;
	}

	protected EventParameter(Identifier id, Identifier creatorId, long version, ParameterType type, Identifier eventId, byte[] value) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version);

		this.type = type;
		this.eventId = eventId;
		this.sort = EventParameterSort._PARAMETER_SORT_RAW;
		this.valueRaw = value;

		this.eventParameterDatabase = EventDatabaseContext.eventParameterDatabase;
	}

	public static EventParameter createInstance(Identifier creatorId, ParameterType type, Identifier eventId, int value) throws CreateObjectException {
		if (creatorId == null || type == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			EventParameter eventParameter = new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_ENTITY_CODE),
					creatorId,
					0L,
					type,
					eventId,
					value);
			eventParameter.changed = true;
			return eventParameter;
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EventParameter.createInstance | Cannot generate identifier", ioee);
		}
	}

	public static EventParameter createInstance(Identifier creatorId, ParameterType type, Identifier eventId, String value) throws CreateObjectException {
		if (creatorId == null || type == null || value == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			EventParameter eventParameter = new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_ENTITY_CODE),
					creatorId,
					0L,
					type,
					eventId,
					value);
			eventParameter.changed = true;
			return eventParameter;
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EventParameter.createInstance | Cannot generate identifier", ioee);
		}
	}

	public static EventParameter createInstance(Identifier creatorId, ParameterType type, Identifier eventId, byte[] value) throws CreateObjectException {
		if (creatorId == null || type == null || value == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			EventParameter eventParameter = new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_ENTITY_CODE),
					creatorId,
					0L,
					type,
					eventId,
					value);
			eventParameter.changed = true;
			return eventParameter;
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EventParameter.createInstance | Cannot generate identifier", ioee);
		}
	}

	public Object getTransferable() {
		EventParameterValue epv = new EventParameterValue();
		switch (this.sort) {
			case EventParameterSort._PARAMETER_SORT_NUMBER:
				epv.value_number(this.valueNumber);
				break;
			case EventParameterSort._PARAMETER_SORT_STRING:
				epv.value_string(new String(this.valueString));
			case EventParameterSort._PARAMETER_SORT_RAW:
				byte[] ba = new byte[this.valueRaw.length];
				for (int i = 0; i < ba.length; i++)
					ba[i] = this.valueRaw[i];
				epv.value_raw(ba);
			default:
				Log.errorMessage("EventParameter.getTransferable | Illegal event parameter sort: " + this.sort);
		}
		return new EventParameter_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				(Identifier_Transferable) this.eventId.getTransferable(),
				EventParameterSort.from_int(this.sort),
				epv);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public Identifier getEventId() {
		return this.eventId;
	}

	public EventParameterSort getSort() {
		return EventParameterSort.from_int(this.sort);
	}

	public int getValueNumber() {
		return this.valueNumber;
	}

	public String getValueString() {
		return this.valueString;
	}

	public byte[] getValueRaw() {
		return this.valueRaw;
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			ParameterType type,
			Identifier eventId,
			int value) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.type = type;
		this.eventId = eventId;
		this.sort = EventParameterSort._PARAMETER_SORT_NUMBER;
		this.valueNumber = value;
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			ParameterType type,
			Identifier eventId,
			String value) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.type = type;
		this.eventId = eventId;
		this.sort = EventParameterSort._PARAMETER_SORT_STRING;
		this.valueString = value;
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			ParameterType type,
			Identifier eventId,
			byte[] value) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.type = type;
		this.eventId = eventId;
		this.sort = EventParameterSort._PARAMETER_SORT_RAW;
		this.valueRaw = value;
	}

	public List getDependencies() {
		return Collections.EMPTY_LIST;
	}
}
