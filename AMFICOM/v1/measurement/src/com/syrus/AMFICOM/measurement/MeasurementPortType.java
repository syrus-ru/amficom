/*
 * $Id: MeasurementPortType.java,v 1.21.2.2 2006/02/14 01:26:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPortType;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPortTypeHelper;

/**
 * @version $Revision: 1.21.2.2 $, $Date: 2006/02/14 01:26:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MeasurementPortType extends StorableObjectType<MeasurementPortType> implements Namable {
	private static final long serialVersionUID = 4387531870305140904L;

	private String name;

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

	public MeasurementPortType(final IdlMeasurementPortType mptt) throws CreateObjectException {
		try {
			this.fromTransferable(mptt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}
	
	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static MeasurementPortType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name) throws CreateObjectException {
		if (creatorId == null || codename == null || name == null || description == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final MeasurementPortType measurementPortType = new MeasurementPortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
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

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMeasurementPortType getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlMeasurementPortTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "");
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMeasurementPortType mptt = (IdlMeasurementPortType) transferable;
		super.fromTransferable(mptt, mptt.codename, mptt.description);
		this.name = mptt.name;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
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
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MeasurementPortTypeWrapper getWrapper() {
		return MeasurementPortTypeWrapper.getInstance();
	}
}
