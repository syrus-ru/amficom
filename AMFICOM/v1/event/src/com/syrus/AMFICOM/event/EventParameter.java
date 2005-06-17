/*
 * $Id: EventParameter.java,v 1.14 2005/06/17 11:01:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.event.corba.EventParameter_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.14 $, $Date: 2005/06/17 11:01:03 $
 * @author $Author: bass $
 * @module event_v1
 */
public class EventParameter implements Identifiable, TransferableObject, TypedObject {

	private Identifier id;
	private ParameterType type;
	private String value;

	public EventParameter(EventParameter_Transferable ept) throws ApplicationException {
		this.id = new Identifier(ept.id);
		this.type = (ParameterType) StorableObjectPool.getStorableObject(new Identifier(ept.type_id), true);
		this.value = ept.value;
	}

	protected EventParameter(Identifier id, ParameterType type, String value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	public static EventParameter createInstance(ParameterType type, String value) throws CreateObjectException {
		if (type == null || value == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			EventParameter eventParameter = new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_CODE),
					type,
					value);
			return eventParameter;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public IDLEntity getTransferable() {
		return new EventParameter_Transferable((Identifier_Transferable) this.id.getTransferable(),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				this.value);
	}

	public Identifier getId() {
		return this.id;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.type);
		hashCodeGenerator.addObject(this.value);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;
	}

	public boolean equals(Object obj) {
		boolean equals = (obj == this);
		if ((!equals) && (obj instanceof EventParameter)) {
			EventParameter eventParameter = (EventParameter) obj;
			if ((this.id.equals(eventParameter.id))
					&& (this.type.equals(eventParameter.type))
					&& (this.value.equals(eventParameter.value)))
				equals = true;
		}
		return equals;
	}

}
