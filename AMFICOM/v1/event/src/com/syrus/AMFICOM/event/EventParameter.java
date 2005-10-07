/*
 * $Id: EventParameter.java,v 1.28 2005/10/07 10:04:19 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.util.TransferableObject;

/**
 * @version $Revision: 1.28 $, $Date: 2005/10/07 10:04:19 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventParameter implements Identifiable,
		TransferableObject<IdlEventParameter> {
	private static final long serialVersionUID = 4906660762164733352L;

	private Identifier id;
	private ParameterType type;
	private String value;

	public EventParameter(final IdlEventParameter ept) {
		this.id = new Identifier(ept.id);
		this.type = ParameterType.fromTransferable(ept.type);
		this.value = ept.value;
	}

	protected EventParameter(final Identifier id, final ParameterType type, final String value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	public static EventParameter createInstance(final ParameterType type, final String value) throws CreateObjectException {
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
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlEventParameter getTransferable(final ORB orb) {
		return new IdlEventParameter(this.id.getTransferable(),
				this.type.getTransferable(orb),
				this.value);
	}

	public Identifier getId() {
		return this.id;
	}

	public ParameterType getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(final Object that) {
		return this.id.equals(that);
	}
}
