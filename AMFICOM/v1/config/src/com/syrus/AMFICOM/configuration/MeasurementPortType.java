/*
 * $Id: MeasurementPortType.java,v 1.64 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPortType;
import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPortTypeHelper;
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.64 $, $Date: 2005/07/27 15:59:22 $
 * @author $Author: bass $
 * @module config
 */

public final class MeasurementPortType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = 7733425194674608181L;

	private String name;

	MeasurementPortType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPortType(final IdlMeasurementPortType mptt) throws CreateObjectException {
		try {
			this.fromTransferable(mptt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	MeasurementPortType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name) {
			super(id,
				  new Date(System.currentTimeMillis()),
				  new Date(System.currentTimeMillis()),
				  creatorId,
				  creatorId,
				  version,
				  codename,
				  description);				
			this.name = name;
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static MeasurementPortType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			final MeasurementPortType measurementPortType = new MeasurementPortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name);

			assert measurementPortType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementPortType.markAsChanged();

			return measurementPortType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMeasurementPortType mptt = (IdlMeasurementPortType) transferable;
		super.fromTransferable(mptt, mptt.codename, mptt.description);
		this.name = mptt.name;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMeasurementPortType getTransferable(final ORB orb) {

		return IdlMeasurementPortTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "");
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}	

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

}
