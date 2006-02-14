/*
 * $Id: Measurement.java,v 1.101.2.3 2006/02/14 01:26:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurement;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementHelper;

/**
 * @version $Revision: 1.101.2.3 $, $Date: 2006/02/14 01:26:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Measurement extends Action<Measurement> {
	private static final long serialVersionUID = -1217428566443489958L;

	private Identifier testId;

	Measurement(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status,
			final Identifier testId) {
		super(id,
				creatorId,
				version,
				typeId,
				monitoredElementId,
				actionTemplateId,
				name,
				startTime,
				duration,
				status);
		this.testId = testId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Measurement(final IdlMeasurement idlMeasurement) throws CreateObjectException {
		super(idlMeasurement);
	}

	static Measurement createInstance(final Identifier creatorId,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final Identifier testId) throws CreateObjectException {

		try {
			final Measurement measurement = new Measurement(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					typeId,
					monitoredElementId,
					actionTemplateId,
					name,
					startTime,
					duration,
					ActionStatus.ACTION_STATUS_NEW,
					testId);

			assert measurement.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurement.markAsChanged();

			return measurement;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlMeasurement getIdlTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		return IdlMeasurementHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getTypeId().getIdlTransferable(orb),
				super.getMonitoredElementId().getIdlTransferable(orb),
				super.getActionTemplateId().getIdlTransferable(orb),
				super.getName(),
				super.getStartTime().getTime(),
				super.getDuration(),
				super.getStatus().getIdlTransferable(),
				this.testId.getIdlTransferable(orb));
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlMeasurement idlMeasurement = (IdlMeasurement) transferable;
		super.fromTransferable(idlMeasurement);
		this.testId = Identifier.valueOf(idlMeasurement.testId);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final Identifier monitoredElementId,
			final Identifier actionTemplateId,
			final String name,
			final Date startTime,
			final long duration,
			final ActionStatus status,
			final Identifier testId) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			typeId,
			monitoredElementId,
			actionTemplateId,
			name,
			startTime,
			duration,
			status);
		this.testId = testId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.getTypeId().getMajor() == ObjectEntities.MEASUREMENT_TYPE_CODE
				&& this.testId != null && this.testId.getMajor() == ObjectEntities.TEST_CODE;
	}

	public Identifier getTestId() {
		return this.testId;
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = super.getDependenciesTmpl();
		dependencies.add(this.testId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MeasurementWrapper getWrapper() {
		return MeasurementWrapper.getInstance();
	}
}
