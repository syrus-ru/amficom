/*
 * $Id: EventParameter.java,v 1.22 2005/08/19 14:19:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.event.corba.IdlEventPackage.IdlEventParameter;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterTypeEnum;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.22 $, $Date: 2005/08/19 14:19:13 $
 * @author $Author: arseniy $
 * @module event
 */
public final class EventParameter implements Identifiable, TransferableObject {
	private Identifier id;
	private ParameterTypeEnum type;
	private String value;

	public EventParameter(final IdlEventParameter ept) {
		this.id = new Identifier(ept.id);
		this.type = ParameterTypeEnum.fromTransferable(ept.type);
		this.value = ept.value;
	}

	protected EventParameter(final Identifier id, final ParameterTypeEnum type, final String value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	public static EventParameter createInstance(final ParameterTypeEnum type, final String value) throws CreateObjectException {
		if (type == null || value == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final EventParameter eventParameter = new EventParameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTPARAMETER_CODE),
					type,
					value);
			return eventParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlEventParameter getTransferable(final ORB orb) {
		return new IdlEventParameter(this.id.getTransferable(),
				this.type.getTransferable(orb),
				this.value);
	}

	public Identifier getId() {
		return this.id;
	}

	public ParameterTypeEnum getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	public @Override int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.type);
		hashCodeGenerator.addObject(this.value);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;
	}

	public @Override  boolean equals(Object obj) {
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
