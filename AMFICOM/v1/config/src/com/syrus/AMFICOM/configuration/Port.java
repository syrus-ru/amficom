/*
 * $Id: Port.java,v 1.82 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlPort;
import com.syrus.AMFICOM.configuration.corba.IdlPortHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.82 $, $Date: 2005/07/27 15:59:22 $
 * @author $Author: bass $
 * @module config
 */
public final class Port extends StorableObject implements Characterizable, TypedObject {
	private static final long serialVersionUID = -5139393638116159453L;

	private PortType type;
	private String description;
	private Identifier equipmentId;

	Port(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.PORT_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Port(final IdlPort pt) throws CreateObjectException {
		try {
			this.fromTransferable(pt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Port(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final PortType type,
			final String description,
			final Identifier equipmentId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param description
	 * @param equipmentId
	 * @throws CreateObjectException
	 */
	public static Port createInstance(final Identifier creatorId,
			final PortType type,
			final String description,
			final Identifier equipmentId) throws CreateObjectException {
		if (creatorId == null || type == null || description == null ||
				type == null || equipmentId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Port port = new Port(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PORT_CODE),
						creatorId,
						StorableObjectVersion.createInitial(),
						type,
						description,
						equipmentId);

			assert port.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			port.markAsChanged();

			return port;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlPort pt = (IdlPort) transferable;
		super.fromTransferable(pt);

		this.type = (PortType) StorableObjectPool.getStorableObject(new Identifier(pt._typeId), true);

		this.description = pt.description;
		this.equipmentId = new Identifier(pt.equipmentId);
	}
	

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPort getTransferable(final ORB orb) {

		return IdlPortHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.type.getId().getTransferable(),
				this.description,
				this.equipmentId.getTransferable());
	}

	public PortType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final PortType type,
			final String description,
			final Identifier equipmentId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.type);
		dependencies.add(this.equipmentId);
		return dependencies;
	}	
	/**
	 * @param equipmentId The equipmentId to set.
	 */
	public void setEquipmentId(final Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.markAsChanged();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(final PortType type) {
		this.type = type;
		super.markAsChanged();
	}
}
