/*
 * $Id: TransmissionPath.java,v 1.67 2005/06/03 20:37:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
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
 * @version $Revision: 1.67 $, $Date: 2005/06/03 20:37:53 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class TransmissionPath extends DomainMember implements MonitoredDomainMember, Characterizable, TypedObject {

	private static final long serialVersionUID = 8129503678304843903L;

	private TransmissionPathType type;
	private String name;
	private String description;
	private Identifier startPortId;
	private Identifier finishPortId;

	private Set characteristics;

	TransmissionPath(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();

		TransmissionPathDatabase database = (TransmissionPathDatabase) DatabaseContext.getDatabase(ObjectEntities.TRANSPATH_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	TransmissionPath(final TransmissionPath_Transferable tpt) throws CreateObjectException {
		try {
			this.fromTransferable(tpt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	TransmissionPath(final Identifier id,
			final Identifier creatorId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final TransmissionPathType type,
			final Identifier startPortId,
			final Identifier finishPortId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;

		this.characteristics = new HashSet();
	}

	/**
	 * create new instance for client
	 *
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param startPortId
	 * @param finishPortId
	 * @throws CreateObjectException
	 */
	public static TransmissionPath createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final TransmissionPathType type,
			final Identifier startPortId,
			final Identifier finishPortId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null ||
				type == null || startPortId == null || finishPortId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			TransmissionPath transmissionPath = new TransmissionPath(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TRANSPATH_ENTITY_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description,
					type,
					startPortId,
					finishPortId);

			assert transmissionPath.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			transmissionPath.markAsChanged();

			return transmissionPath;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		TransmissionPath_Transferable tpt = (TransmissionPath_Transferable) transferable;
		super.fromTransferable(tpt.header, new Identifier(tpt.domain_id));

		this.name = tpt.name;
		this.description = tpt.description;
		this.startPortId = new Identifier(tpt.start_port_id);
		this.finishPortId = new Identifier(tpt.finish_port_id);

		Set characteristicIds = Identifier.fromTransferables(tpt.characteristic_ids);
		this.characteristics = new HashSet(tpt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);

		return new TransmissionPath_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				this.name,
				this.description,
				(Identifier_Transferable) this.type.getId().getTransferable(),
				(Identifier_Transferable) this.startPortId.getTransferable(),
				(Identifier_Transferable) this.finishPortId.getTransferable(),
				charIds);
	}

	public String getName() {
		return this.name;
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

	public Identifier getFinishPortId() {
		return this.finishPortId;
	}

	public Identifier getStartPortId() {
		return this.startPortId;
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final Identifier domainId,
			final String name,
			final String description,
			final TransmissionPathType type,
			final Identifier startPortId,
			final Identifier finishPortId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.add(this.startPortId);
		dependencies.add(this.finishPortId);
		return dependencies;
	}

	public void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.markAsChanged();
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}	
	
	/**
	 * @param finishPortId The finishPortId to set.
	 */
	public void setFinishPortId(final Identifier finishPortId) {
		this.finishPortId = finishPortId;
		super.markAsChanged();
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	/**
	 * @param startPortId The startPortId to set.
	 */
	public void setStartPortId(final Identifier startPortId) {
		this.startPortId = startPortId;
		super.markAsChanged();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(final TransmissionPathType type) {
		this.type = type;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.MonitoredDomainMember#getMonitoredElementIds()
	 */
	public Set getMonitoredElementIds() {
		//TODO Implement
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
