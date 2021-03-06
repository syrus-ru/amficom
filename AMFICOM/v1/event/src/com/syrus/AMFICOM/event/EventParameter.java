/*
 * $Id: EventParameter.java,v 1.36 2006/06/06 11:27:42 arseniy Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */
package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.general.ObjectEntities.EVENTPARAMETER_CODE;

import java.io.Serializable;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.event.corba.IdlEventPackage.IdlEventParameter;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.36 $, $Date: 2006/06/06 11:27:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */
public final class EventParameter implements Identifiable, Serializable,
		IdlTransferableObject<IdlEventParameter> {
	private static final long serialVersionUID = 4906660762164733352L;

	/**
	 * @serial include
	 */
	private Identifier id;
	/**
	 * @serial include
	 */
	private ParameterType type;
	/**
	 * @serial include
	 */
	private String value;

	public EventParameter(final IdlEventParameter ept) {
		this.id = Identifier.valueOf(ept.id);
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
			final EventParameter eventParameter = new EventParameter(IdentifierPool.getGeneratedIdentifier(EVENTPARAMETER_CODE),
					type,
					value);
			return eventParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	public IdlEventParameter getIdlTransferable(final ORB orb) {
		return new IdlEventParameter(this.id.getIdlTransferable(),
				this.type.getIdlTransferable(orb),
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
