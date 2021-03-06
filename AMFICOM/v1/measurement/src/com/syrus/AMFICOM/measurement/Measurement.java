/*
 * $Id: Measurement.java,v 1.107 2006/06/06 11:31:16 arseniy Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurement;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementHelper;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.IdlMeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.107 $, $Date: 2006/06/06 11:31:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Measurement extends Action
		implements IdlTransferableObjectExt<IdlMeasurement> {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3544388098013476664L;

	public static final long DEFAULT_MEASUREMENT_DURATION = 3*60*1000;//milliseconds
	protected static final int RETRIEVE_RESULT = 1;
	protected static final int UPDATE_STATUS = 1;

	private String name;
	private MeasurementSetup setup;
	private Date startTime;
	private long duration;
	private int status;
	private String localAddress;
	private Identifier testId;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Measurement(final IdlMeasurement mt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(mt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Measurement(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final MeasurementType type,
			final Identifier monitoredElementId,
			final String name,
			final MeasurementSetup setup,
			final Date startTime,
			final String localAddress,
			final Identifier testId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				type,
				monitoredElementId,
				VOID_IDENTIFIER);
		this.name = name;
		this.setup = setup;
		this.startTime = startTime;
		if (this.setup != null) {
			this.duration = this.setup.getMeasurementDuration();
		}
		this.status = IdlMeasurementStatus._MEASUREMENT_STATUS_SCHEDULED;
		this.localAddress = localAddress;
		this.testId = testId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void fromIdlTransferable(final IdlMeasurement mt)
	throws IdlConversionException {
		try {
			super.fromIdlTransferable(mt, MeasurementType.fromTransferable(mt.type), Identifier.valueOf(mt.monitoredElementId), VOID_IDENTIFIER);
	
			this.setup = (MeasurementSetup) StorableObjectPool.getStorableObject(Identifier.valueOf(mt.setupId), true);
	
			this.name = mt.name;
			this.startTime = new Date(mt.startTime);
			this.duration = mt.duration;
			this.status = mt.status.value();
			this.localAddress = mt.localAddress;
			this.testId = Identifier.valueOf(mt.testId);
	
			assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		} catch (final ApplicationException ae) {
			throw new IdlConversionException(ae);
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
				(IdlMeasurementType) super.type.getIdlTransferable(orb),
				super.monitoredElementId.getIdlTransferable(),
				this.name,
				this.setup.getId().getIdlTransferable(),
				this.startTime.getTime(),
				this.duration,
				IdlMeasurementStatus.from_int(this.status),
				this.localAddress,
				this.testId.getIdlTransferable());
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null
				&& this.setup != null
				&& this.startTime != null
				&& this.localAddress != null
				&& this.testId != null;
	}
	
	public short getEntityCode() {
		return ObjectEntities.MEASUREMENT_CODE;
	}

	public MeasurementSetup getSetup() {
		return this.setup;
	}
	
	public void setSetup(final MeasurementSetup setup) {
		this.setup = setup;
		super.markAsChanged();
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public long getDuration() {
		return this.duration;
	}

	public IdlMeasurementStatus getStatus() {
		return IdlMeasurementStatus.from_int(this.status);
	}

	public String getLocalAddress() {
		return this.localAddress;
	}

	public Identifier getTestId() {
		return this.testId;
	}

	public Test getTest() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getTestId(), true);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final MeasurementType type,
			final Identifier monitoredElementId,
			final String name,
			final MeasurementSetup setup,
			final Date startTime,
			final long duration,
			final int status,
			final String localAddress,
			final Identifier testId) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			type,
			monitoredElementId,
			VOID_IDENTIFIER);
		this.name = name;
		this.setup = setup;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
		this.localAddress = localAddress;
		this.testId = testId;
	}

	/**
	 * Create a new instance for client
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param name
	 * @param setup
	 * @param startTime
	 * @param localAddress
	 * @param testId
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	static Measurement createInstance(final Identifier creatorId,
			final MeasurementType type,
			final Identifier monitoredElementId,
			final String name,
			final MeasurementSetup setup,
			final Date startTime,
			final String localAddress,
			final Identifier testId) throws CreateObjectException {

		try {
			final Measurement measurement = new Measurement(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					type,
					monitoredElementId,
					name,
					setup,
					startTime,
					localAddress,
					testId);

			assert measurement.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurement.markAsChanged();

			return measurement;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	public Result createResult(final Identifier resultCreatorId, final Parameter[] resultParameters)
			throws CreateObjectException {
		return Result.createInstance(resultCreatorId, this.id, ResultSort.RESULT_SORT_MEASUREMENT, resultParameters);
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public Set<Result> getResults() throws ApplicationException {
		final LinkedIdsCondition condition = new LinkedIdsCondition(this.id, ObjectEntities.RESULT_CODE);
		return StorableObjectPool.getStorableObjectsByCondition(condition, true);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.testId);
		dependencies.add(this.setup);
		return dependencies;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(final IdlMeasurementStatus status) {
		this.status = status.value();
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MeasurementWrapper getWrapper() {
		return MeasurementWrapper.getInstance();
	}
}
