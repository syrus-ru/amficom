/*
 * $Id: EventParameter.java,v 1.4 2005/02/02 15:09:47 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import com.syrus.AMFICOM.event.corba.EventParameter_Transferable;
import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterSort;
import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterValue;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/02 15:09:47 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventParameter implements TransferableObject, TypedObject, Identified {
	private Identifier id;
	private ParameterType type;
	private int sort;
	private int valueNumber;
	private String valueString;
	private byte[] valueRaw;

	public EventParameter(EventParameter_Transferable ept) throws DatabaseException, CommunicationException {
		this.id = new Identifier(ept.id);
		this.type = (ParameterType) GeneralStorableObjectPool.getStorableObject(new Identifier(ept.type_id), true);
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
	}

	protected EventParameter(Identifier id,
								ParameterType type,
								int value) {
		this.id = id;
		this.type = type;
		this.sort = EventParameterSort._PARAMETER_SORT_NUMBER;
		this.valueNumber = value;
	}

	protected EventParameter(Identifier id,
								ParameterType type,
								String value) {
		this.id = id;
		this.type = type;
		this.sort = EventParameterSort._PARAMETER_SORT_STRING;
		this.valueString = value;
	}

	protected EventParameter(Identifier id,
								ParameterType type,
								byte[] value) {
		this.id = id;
		this.type = type;
		this.sort = EventParameterSort._PARAMETER_SORT_RAW;
		this.valueRaw = value;
	}

	public static EventParameter createInstance(ParameterType type, int value) throws CreateObjectException {
		try {
			return new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_ENTITY_CODE),
					type,
					value);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EventParameter.createInstance | Cannot generate identifier", ioee);
		}
	}

	public static EventParameter createInstance(ParameterType type, String value) throws CreateObjectException {
		try {
			return new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_ENTITY_CODE),
					type,
					value);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EventParameter.createInstance | Cannot generate identifier", ioee);
		}
	}

	public static EventParameter createInstance(ParameterType type, byte[] value) throws CreateObjectException {
		try {
			return new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_ENTITY_CODE),
					type,
					value);
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
		return new EventParameter_Transferable((Identifier_Transferable) this.id.getTransferable(),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				EventParameterSort.from_int(this.sort),
				epv);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public Identifier getId() {
		return this.id;
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
}
