/*
 * $Id: TransmissionPath.java,v 1.86 2005/08/28 15:44:10 arseniy Exp $
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

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPath;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPathHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
/**
 * @version $Revision: 1.86 $, $Date: 2005/08/28 15:44:10 $
 * @author $Author: arseniy $
 * @module config
 */

public final class TransmissionPath extends DomainMember implements MonitoredDomainMember, Characterizable, TypedObject {

	private static final long serialVersionUID = 8129503678304843903L;

	private TransmissionPathType type;
	private String name;
	private String description;
	private Identifier startPortId;
	private Identifier finishPortId;

	private transient CharacterizableDelegate characterizableDelegate;

	TransmissionPath(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.TRANSMISSIONPATH_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public TransmissionPath(final IdlTransmissionPath tpt) throws CreateObjectException {
		try {
			this.fromTransferable(tpt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	TransmissionPath(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
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
			final TransmissionPath transmissionPath = new TransmissionPath(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TRANSMISSIONPATH_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description,
					type,
					startPortId,
					finishPortId);

			assert transmissionPath.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			transmissionPath.markAsChanged();

			return transmissionPath;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlTransmissionPath tpt = (IdlTransmissionPath) transferable;
		super.fromTransferable(tpt, new Identifier(tpt.domainId));

		this.type = (TransmissionPathType) StorableObjectPool.getStorableObject(new Identifier(tpt._typeId), true);

		this.name = tpt.name;
		this.description = tpt.description;
		this.startPortId = new Identifier(tpt.startPortId);
		this.finishPortId = new Identifier(tpt.finishPortId);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTransmissionPath getTransferable(final ORB orb) {

		return IdlTransmissionPathHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.getDomainId().getTransferable(),
				this.name,
				this.description,
				this.type.getId().getTransferable(),
				this.startPortId.getTransferable(),
				this.finishPortId.getTransferable());
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
			final StorableObjectVersion version,
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

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.type);
		dependencies.add(this.startPortId);
		dependencies.add(this.finishPortId);
		return dependencies;
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
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
	public Set<Identifier> getMonitoredElementIds() {
		//TODO Implement
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
