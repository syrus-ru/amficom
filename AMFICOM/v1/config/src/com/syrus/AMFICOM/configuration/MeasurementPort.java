/*
 * $Id: MeasurementPort.java,v 1.72 2005/08/02 18:08:46 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPort;
import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPortHelper;
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
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.72 $, $Date: 2005/08/02 18:08:46 $
 * @author $Author: arseniy $
 * @module config
 */
public final class MeasurementPort extends StorableObject implements Characterizable, TypedObject {
	private static final long serialVersionUID = -5100885507408715167L;

	private MeasurementPortType type;

	private String name;
	private String description;

	private Identifier kisId;
	private Identifier portId;

	MeasurementPort(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPort(final IdlMeasurementPort mpt) throws CreateObjectException {
		try {
			this.fromTransferable(mpt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	MeasurementPort(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final MeasurementPortType type,
			final String name,
			final String description,	
			final Identifier kisId,
			final Identifier portId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param name
	 * @param description
	 * @param kisId
	 * @param portId
	 * @throws CreateObjectException
	 */
	public static MeasurementPort createInstance(final Identifier creatorId,
			final MeasurementPortType type,
			final String name,
			final String description,	
			final Identifier kisId,
			final Identifier portId) throws CreateObjectException{
		if (creatorId == null || type == null || name == null || description == null ||
				kisId == null || portId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final MeasurementPort measurementPort = new MeasurementPort(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORT_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					type,
					name,
					description,
					kisId,
					portId);

			assert measurementPort.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementPort.markAsChanged();

			return measurementPort;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlMeasurementPort mpt = (IdlMeasurementPort) transferable;
		super.fromTransferable(mpt);

		this.type = (MeasurementPortType) StorableObjectPool.getStorableObject(new Identifier(mpt._typeId), true);

		this.name = mpt.name;
		this.description = mpt.description;

		this.kisId = new Identifier(mpt.kisId);
		this.portId = new Identifier(mpt.portId);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMeasurementPort getTransferable(final ORB orb) {
		return IdlMeasurementPortHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.type.getId().getTransferable(),
				this.name,
				this.description,
				this.kisId.getTransferable(),
				this.portId.getTransferable());
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}
	
	public String getName() {
		return this.name;
	}
	
	public Identifier getKISId() {
		return this.kisId;
	}

	public Identifier getPortId() {
		return this.portId;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final MeasurementPortType type,
			final String name,
			final String description,	
			final Identifier kisId,
			final Identifier portId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		dependencies.add(this.kisId);
		dependencies.add(this.portId);
		return dependencies;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	
	public void setType(final MeasurementPortType type) {
		this.type = type;
		super.markAsChanged();
	}

	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

	/**
	 * @param kisId The kisId to set.
	 */
	public void setKISId(Identifier kisId) {
		this.kisId = kisId;
		super.markAsChanged();
	}
	/**
	 * @param portId The portId to set.
	 */
	public void setPortId(Identifier portId) {
		this.portId = portId;
		super.markAsChanged();
	}
}
