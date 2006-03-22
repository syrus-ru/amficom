/*-
 * $Id: Test.java,v 1.183.2.10 2006/03/22 16:48:43 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CRONTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.INTERVALSTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERIODICALTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.measurement.corba.IdlTestHelper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestStops;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.IdlPeriodicalTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.IdlTestTemporalType;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObject;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.183.2.10 $, $Date: 2006/03/22 16:48:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Test extends StorableObject implements IdlTransferableObjectExt<IdlTest>, Describable {
	private static final long serialVersionUID = -6387317612272518101L;

	private String description;
	private Identifier groupTestId;
	private Identifier monitoredElementId;
	private TestStatus status;
	private TestTemporalType temporalType;
	private TestTimeStamps timeStamps;
	private Set<Identifier> measurementSetupIds;
	private Identifier measurementTypeId;
	private int numberOfMeasurements;
	private Identifier analysisTypeId;

	private SortedMap<Date, String> stopMap;  

	private transient Identifier kisId;
	private transient Identifier mcmId;
	private transient Identifier currentMeasurementSetupId;

	Test(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final TestStatus status,
			final TestTemporalType temporalType,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId,
			final int numberOfMeasurements,
			final Identifier analysisTypeId,
			final SortedMap<Date, String> stopMap) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.description = description;
		this.groupTestId = groupTestId;
		this.monitoredElementId = monitoredElementId;
		this.status = status;
		this.temporalType = temporalType;
		this.timeStamps = new TestTimeStamps(startTime, endTime, temporalPatternId);
		this.measurementSetupIds = new HashSet<Identifier>();
		this.setMeasurementSetupIds0(measurementSetupIds);
		this.measurementTypeId = measurementTypeId;
		this.numberOfMeasurements = numberOfMeasurements;
		this.analysisTypeId = analysisTypeId;
		this.stopMap = new TreeMap<Date, String>();
		this.setStopMap0(stopMap);
	}

	public Test(final IdlTest idlTest) throws CreateObjectException {
		this.measurementSetupIds = new HashSet<Identifier>();
		try {
			this.fromIdlTransferable(idlTest);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * Create new instance for client.
	 * Creates onetime test without analysis.
	 * @param creatorId
	 * @param description
	 * @param groupTestId
	 * @param monitoredElementId
	 * @param startTime
	 * @param measurementSetupIds
	 * @param measurementTypeId
	 * @return New instance of onetime test.
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final Date startTime,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId) throws CreateObjectException {
		try {
			final Test test = new Test(IdentifierPool.getGeneratedIdentifier(TEST_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					groupTestId,
					monitoredElementId,
					TestStatus.TEST_STATUS_NEW,
					TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
					startTime,
					startTime,
					VOID_IDENTIFIER,
					measurementSetupIds,
					measurementTypeId,
					0,
					VOID_IDENTIFIER,
					null);

			assert test.isValid() : OBJECT_STATE_ILLEGAL;

			test.markAsChanged();

			return test;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * Create new instance for client.
	 * Creates onetime test with analysis.
	 * @param creatorId
	 * @param description
	 * @param groupTestId
	 * @param monitoredElementId
	 * @param startTime
	 * @param measurementSetupIds
	 * @param measurementTypeId
	 * @param analysisTypeId
	 * @return New instance of onetime test.
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final Date startTime,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId,
			final Identifier analysisTypeId) throws CreateObjectException {
		try {
			final Test test = new Test(IdentifierPool.getGeneratedIdentifier(TEST_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					groupTestId,
					monitoredElementId,
					TestStatus.TEST_STATUS_NEW,
					TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
					startTime,
					startTime,
					VOID_IDENTIFIER,
					measurementSetupIds,
					measurementTypeId,
					0,
					analysisTypeId,
					null);

			assert test.isValid() : OBJECT_STATE_ILLEGAL;

			test.markAsChanged();

			return test;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * Create new instance for client.
	 * Creates periodical test without analysis.
	 * @param creatorId
	 * @param description
	 * @param groupTestId
	 * @param monitoredElementId
	 * @param startTime
	 * @param endTime
	 * @param temporalPatternId
	 * @param measurementSetupIds
	 * @param measurementTypeId
	 * @return New instance of periodical test.
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId) throws CreateObjectException {
		try {
			final Test test = new Test(IdentifierPool.getGeneratedIdentifier(TEST_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					groupTestId,
					monitoredElementId,
					TestStatus.TEST_STATUS_NEW,
					TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL,
					startTime,
					endTime,
					temporalPatternId,
					measurementSetupIds,
					measurementTypeId,
					0,
					VOID_IDENTIFIER,
					null);

			assert test.isValid() : OBJECT_STATE_ILLEGAL;

			test.markAsChanged();

			return test;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * Create new instance for client.
	 * Creates periodical test with analysis.
	 * @param creatorId
	 * @param description
	 * @param groupTestId
	 * @param monitoredElementId
	 * @param startTime
	 * @param endTime
	 * @param temporalPatternId
	 * @param measurementSetupIds
	 * @param measurementTypeId
	 * @param analysisTypeId
	 * @return New instance of periodical test.
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Set<Identifier> measurementSetupIds,
			final Identifier measurementTypeId,
			final Identifier analysisTypeId) throws CreateObjectException {
		try {
			final Test test = new Test(IdentifierPool.getGeneratedIdentifier(TEST_CODE),
					creatorId,
					INITIAL_VERSION,
					description,
					groupTestId,
					monitoredElementId,
					TestStatus.TEST_STATUS_NEW,
					TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL,
					startTime,
					endTime,
					temporalPatternId,
					measurementSetupIds,
					measurementTypeId,
					0,
					analysisTypeId,
					null);

			assert test.isValid() : OBJECT_STATE_ILLEGAL;

			test.markAsChanged();

			return test;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTest getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final IdlTestStops[] idlTestStops = new IdlTestStops[this.stopMap.size()];
		int i = 0;
		for (final Date stopDate : this.stopMap.keySet()) {
			idlTestStops[i++] = new IdlTestStops(stopDate.getTime(), this.stopMap.get(stopDate));
		}

		return IdlTestHelper.init(orb,
				this.id.getIdlTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(orb),
				this.modifierId.getIdlTransferable(orb),
				this.version.longValue(),
				this.description,
				this.groupTestId.getIdlTransferable(orb),
				this.monitoredElementId.getIdlTransferable(orb),
				this.status.getIdlTransferable(),
				this.timeStamps.getIdlTransferable(orb),
				Identifier.createTransferables(this.measurementSetupIds),
				this.measurementTypeId.getIdlTransferable(orb),
				this.numberOfMeasurements,
				this.analysisTypeId.getIdlTransferable(orb),
				idlTestStops);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void fromIdlTransferable(final IdlTest idlTest) throws IdlConversionException {
		super.fromIdlTransferable(idlTest);

		this.description = idlTest.description;
		this.groupTestId = Identifier.valueOf(idlTest.groupTestId);
		this.monitoredElementId = Identifier.valueOf(idlTest.monitoredElementId);
		this.status = TestStatus.valueOf(idlTest.status);
		this.temporalType = TestTemporalType.valueOf(idlTest.timeStamps.discriminator());
		this.timeStamps = new TestTimeStamps(idlTest.timeStamps);
		this.setMeasurementSetupIds0(Identifier.fromTransferables(idlTest.measurementSetupIds));
		this.measurementTypeId = Identifier.valueOf(idlTest.measurementTypeId);
		this.numberOfMeasurements = idlTest.numberOfMeasurements;
		this.analysisTypeId = Identifier.valueOf(idlTest.analysisTypeId);
		this.stopMap = new TreeMap<Date, String>();
		for (int i = 0; i < idlTest.stops.length; i++) {
			final IdlTestStops idlTestStops = idlTest.stops[i];
			this.stopMap.put(new Date(idlTestStops.time), idlTestStops.reason);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		assert description != null : NON_NULL_EXPECTED;
		this.description = description;
		super.markAsChanged();
	}

	public String getName() {
		return this.description;
	}

	/**
	 * <p>Never call this method ! </p>  
	 * @throws UnsupportedOperationException
	 */
	public void setName(final String name) {
		throw new UnsupportedOperationException("Test.setName() is unsupported");		
	}	

	public Identifier getGroupTestId() {
		return this.groupTestId;
	}

	public void setGroupTestId(final Identifier groupTestId) {
		assert groupTestId != null : NON_NULL_EXPECTED;
		assert (groupTestId.isVoid() || groupTestId.getMajor() == TEST_CODE) : ILLEGAL_ENTITY_CODE;
		this.groupTestId = groupTestId;
		this.markAsChanged();
	}

	public Identifier getMonitoredElementId() {
		return this.monitoredElementId;
	}

	public MonitoredElement getMonitoredElement() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.monitoredElementId, true);
	}

	/**
	 * @param monitoredElement The monitoredElement to set.
	 */
	public void setMonitoredElementId(final Identifier monitoredElementId) {
		assert monitoredElementId != null : NON_NULL_EXPECTED;
		assert monitoredElementId.getMajor() == MONITOREDELEMENT_CODE : ILLEGAL_ENTITY_CODE;
		this.monitoredElementId = monitoredElementId;
		super.markAsChanged();
	}

	public TestStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(final TestStatus status) {
		assert status != null : NON_NULL_EXPECTED;
		this.status = status;
		super.markAsChanged();
	}

	public TestTemporalType getTemporalType() {
		return this.temporalType;
	}

	/**
	 * @param temporalType The temporalType to set.
	 */
	public void setTemporalType(final TestTemporalType temporalType) {
		assert temporalType != null : NON_NULL_EXPECTED;
		this.temporalType = temporalType;
		super.markAsChanged();
	}

	public Date getStartTime() {
		return this.timeStamps.getStartTime();
	}
	
	public void setStartTime(final Date startTime) {
		this.timeStamps.setStartTime(startTime);
		super.markAsChanged();
	}

	public Date getEndTime() {
		return this.timeStamps.getEndTime();
	}
	
	public void setEndTime(final Date endTime) {
		this.timeStamps.setEndTime(endTime);
		super.markAsChanged();
	}

	public Identifier getTemporalPatternId() {
		return this.timeStamps.getTemporalPatternId();
	}

	/**
	 * @param temporalPatternId The temporalPatternId to set.
	 */
	public void setTemporalPatternId(final Identifier temporalPatternId) {
		this.timeStamps.setTemporalPatternId(temporalPatternId);
		super.markAsChanged();
	}

	public Set<Identifier> getMeasurementSetupIds() {
		return Collections.unmodifiableSet(this.measurementSetupIds);
	}

	public void setMeasurementSetupIds(final Set<Identifier> measurementSetupIds) {
		assert measurementSetupIds != null : NON_NULL_EXPECTED;
		assert measurementSetupIds.size() > 0 : NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(measurementSetupIds) == MEASUREMENTSETUP_CODE : ILLEGAL_ENTITY_CODE;
		this.setMeasurementSetupIds0(measurementSetupIds);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	synchronized void setMeasurementSetupIds0(final Set<Identifier> measurementSetupIds) {
		this.measurementSetupIds.clear();
		if (measurementSetupIds != null) {
			this.measurementSetupIds.addAll(measurementSetupIds);
		}
	}

	public Identifier getMeasurementTypeId() {
		return this.measurementTypeId;
	}

	/**
	 * @param measurementType The measurementType to set.
	 */
	public void setMeasurementType(final Identifier measurementTypeId) {
		assert measurementTypeId != null : NON_NULL_EXPECTED;
		assert measurementTypeId.getMajor() == MEASUREMENT_TYPE_CODE : ILLEGAL_ENTITY_CODE;
		this.measurementTypeId = measurementTypeId;
		super.markAsChanged();
	}

	public int getNumberOfMeasurements() {
		return this.numberOfMeasurements;
	}

	public Identifier getAnalysisTypeId() {
		return this.analysisTypeId;
	}

	/**
	 * @param analysisType The analysisType to set.
	 */
	public void setAnalysisTypeId(final Identifier analysisTypeId) {
		assert analysisTypeId != null : NON_NULL_EXPECTED;
		assert (analysisTypeId.isVoid() || analysisTypeId.getMajor() == ANALYSIS_TYPE_CODE) : ILLEGAL_ENTITY_CODE;
		this.analysisTypeId = analysisTypeId;
		super.markAsChanged();
	}

	/**
	 * @return map of test stop time and reason to stop
	 */
	public SortedMap<Date, String> getStopMap() {
		return Collections.unmodifiableSortedMap(this.stopMap);
	}

	/**
	 * stop test now. 
	 * @param reason reason to stop test
	 * @see #addStop(Date, String)
	 */
	public void addStop(final String reason) {
		this.addStop(new Date(), reason);
	}

	/**
	 * add test stop 
	 * @param stopTime time, when stop test
	 * @param reason reason to stop test
	 */
	public void addStop(final Date stopTime, final String reason) {
		assert stopTime != null : NON_NULL_EXPECTED;
		assert reason != null : NON_NULL_EXPECTED;
		this.stopMap.put(new Date(stopTime.getTime()), reason);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	synchronized void setStopMap0(final SortedMap<Date, String> stopMap) {
		this.stopMap.clear();
		if (stopMap != null) {
			this.stopMap.putAll(stopMap);
		}
	}

	public Identifier getKISId() throws ApplicationException {
		if (this.kisId == null) {
			final Identifier measurementPortId = this.getMonitoredElement().getMeasurementPortId();
			final MeasurementPort measurementPort = StorableObjectPool.getStorableObject(measurementPortId, true);
			this.kisId = measurementPort.getKISId();
		}
		return this.kisId;
	}

	public Identifier getMCMId() throws ApplicationException {
		if (this.mcmId == null) {
			final KIS kis = StorableObjectPool.getStorableObject(this.getKISId(), true);
			this.mcmId = kis.getMCMId();
		}
		return this.mcmId;
	}

	public Identifier getCurrentMeasurementSetupId() {
		this.ensureCurrentMeasurementSetupIsSet();
		return this.currentMeasurementSetupId;
	}

	public MeasurementSetup getCurrentMeasurementSetup() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getCurrentMeasurementSetupId(), true);
	}

	/**
	 * @todo Implement more sophysticated method to determine current MeasurementSetup
	 *
	 */
	private void ensureCurrentMeasurementSetupIsSet() {
		if (this.currentMeasurementSetupId == null) {
			this.currentMeasurementSetupId = this.measurementSetupIds.iterator().next();
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		final boolean valid = super.isValid()
				&& this.description != null
				&& this.groupTestId != null
					&& (this.groupTestId.isVoid() || this.groupTestId.getMajor() == TEST_CODE)
				&& this.monitoredElementId != null
					&& this.monitoredElementId.getMajor() == MONITOREDELEMENT_CODE
				&& this.status != null
				&& this.temporalType != null
				&& this.timeStamps.isValid()
				&& this.measurementSetupIds != null
					&& this.measurementSetupIds.size() > 0
					&& StorableObject.getEntityCodeOfIdentifiables(this.measurementSetupIds) == MEASUREMENTSETUP_CODE
				&& this.measurementTypeId != null
					&& this.measurementTypeId.getMajor() == MEASUREMENT_TYPE_CODE
				&& this.numberOfMeasurements >= 0
				&& this.analysisTypeId != null
					&& (this.analysisTypeId.isVoid() || this.analysisTypeId.getMajor() == ANALYSIS_TYPE_CODE)
				&& this.stopMap != null;
		if (!valid) {
			return false;
		}

		try {
			final Set<MeasurementSetup> measurementSetups = StorableObjectPool.getStorableObjects(this.measurementSetupIds, true);
			for (final MeasurementSetup actionTemplate : measurementSetups) {
				if (!actionTemplate.isAttachedToMonitoredElement(this.monitoredElementId.getId())) {
					Log.errorMessage("Test '" + this.id + "': "
							+ "MeasurementSetup: '" + actionTemplate.getId()
							+ "' is not attached to MonitoredElement: '" + this.monitoredElementId.getId() + "'");
					return false;
				}
			}
			return true;
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return true;
		}
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	public Measurement createMeasurement(final Identifier measurementCreatorId, final Date startTime) throws CreateObjectException {
		if (this.status != TestStatus.TEST_STATUS_PROCESSING) {
			throw new CreateObjectException("Status of test '" + this.id + "' is " + this.status + ", not PROCESSING");
		}

		MonitoredElement monitoredElement = null;
		ActionTemplate measurementTemplate = null;
		try {
			monitoredElement = StorableObjectPool.getStorableObject(this.monitoredElementId, true);
			final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(this.getCurrentMeasurementSetupId(), true);
			measurementTemplate = StorableObjectPool.getStorableObject(measurementSetup.getMeasurementTemplateId(), true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		if (monitoredElement == null) {
			throw new CreateObjectException("Cannot find monitored element '" + this.monitoredElementId + "'");
		}
		if (measurementTemplate == null) {
			throw new CreateObjectException("Cannot find measurement template");
		}
		final Measurement measurement = Measurement.createInstance(measurementCreatorId,
				this.measurementTypeId,
				this.monitoredElementId,
				measurementTemplate.getId(),
				LangModelMeasurement.getString("RR")
						+ "-" + monitoredElement.getName()
						+ "-" + EasyDateFormatter.formatDate(startTime),
				startTime,
				measurementTemplate.getApproximateActionDuration(),
				this.id);
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = measurementCreatorId;
		this.numberOfMeasurements++;

		super.markAsChanged();

		return measurement;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String description,
			final Identifier groupTestId,
			final Identifier monitoredElementId,
			final TestStatus status,
			final TestTemporalType temporalType,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Identifier measurementTypeId,
			final int numberOfMeasurements,
			final Identifier analysisTypeId) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.description = description;
		this.groupTestId = groupTestId;
		this.monitoredElementId = monitoredElementId;
		this.status = status;
		this.temporalType = temporalType;
		this.timeStamps = new TestTimeStamps(startTime, endTime, temporalPatternId);
		this.measurementTypeId = measurementTypeId;
		this.numberOfMeasurements = numberOfMeasurements;
		this.analysisTypeId = analysisTypeId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();

		if (!this.groupTestId.isVoid() && !this.id.equals(this.groupTestId)) {
			dependencies.add(this.groupTestId);
		}

		dependencies.add(this.monitoredElementId);

		final Identifier temporalPatternId = this.timeStamps.getTemporalPatternId();
		if (this.temporalType == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL && !temporalPatternId.isVoid()) {
			dependencies.add(temporalPatternId);
		}

		dependencies.add(this.measurementTypeId);
		dependencies.addAll(this.measurementSetupIds);
		if (!this.analysisTypeId.isVoid()) {
			dependencies.add(this.analysisTypeId);
		}

		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected TestWrapper getWrapper() {
		return TestWrapper.getInstance();
	}

	public static IdlTest[] createTransferables(final Set<Test> tests, final ORB orb) {
		assert tests != null : NON_NULL_EXPECTED;

		final IdlTest[] transferables = new IdlTest[tests.size()];
		int i = 0;
		synchronized (tests) {
			for (final Test test : tests) {
				transferables[i++] = test.getIdlTransferable(orb);
			}
		}
		return transferables;
	}



	public static enum TestStatus {
		TEST_STATUS_NEW,		//Сохранён в БД на сервере
		TEST_STATUS_SCHEDULED,	//Передан на МУИ и сохранён в БД
		TEST_STATUS_PROCESSING,	//Извлечён из очереди; первое измерение начинает выполняться
		TEST_STATUS_COMPLETED,	//Последнее измерение завершено
		TEST_STATUS_STOPPING,	//Остановливается по запросу
		TEST_STATUS_STOPPED,	//Остановлен по запросу
		TEST_STATUS_ABORTED;	//Прерван

		private static final TestStatus[] VALUES = values();

		IdlTestStatus getIdlTransferable() {
			return IdlTestStatus.from_int(this.ordinal());
		}

		static TestStatus valueOf(final int code) {
			return VALUES[code];
		}

		static TestStatus valueOf(final IdlTestStatus idlTestStatus) {
			return valueOf(idlTestStatus.value());
		}

		@Override
		public String toString() {
			return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
		}
	}

	public static enum TestTemporalType {
		TEST_TEMPORAL_TYPE_ONETIME,
		TEST_TEMPORAL_TYPE_PERIODICAL;

		private static final TestTemporalType[] VALUES = values();

		IdlTestTemporalType getIdlTransferable() {
			return IdlTestTemporalType.from_int(this.ordinal());
		}

		static TestTemporalType valueOf(final int code) {
			return VALUES[code];
		}

		static TestTemporalType valueOf(final IdlTestTemporalType idlTestTemporalType) {
			return valueOf(idlTestTemporalType.value());
		}

		@Override
		public String toString() {
			return this.name() + "(" + Integer.toString(this.ordinal()) + ")";
		}
	}

	private final class TestTimeStamps implements IdlTransferableObject<IdlTestTimeStamps> {
		private static final long serialVersionUID = -3560328752462377043L;

		private Date startTime;
		private Date endTime;
		private Identifier temporalPatternId;

		TestTimeStamps(final Date startTime, final Date endTime, final Identifier temporalPatternId) {
			switch (this.getTestTemporalType()) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					this.startTime = startTime;
					this.endTime = this.startTime;
					this.temporalPatternId = VOID_IDENTIFIER;
					break;
				case TEST_TEMPORAL_TYPE_PERIODICAL:
					this.startTime = startTime;
					this.endTime = endTime;
					this.temporalPatternId = temporalPatternId;
					if (this.endTime == null) {
						Log.errorMessage("ERROR: End time is NULL");
						this.endTime = this.startTime;
					}
					if (this.temporalPatternId == null) {
						Log.errorMessage("ERROR: Temporal pattern is NULL");
						this.temporalPatternId = VOID_IDENTIFIER;
					}
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal temporal type: " + this.getTestTemporalType() + " of test");
			}
			assert this.isValid() : OBJECT_STATE_ILLEGAL;
		}

		TestTimeStamps(final IdlTestTimeStamps idlTestTimeStamps) {
			switch (this.getTestTemporalType()) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					this.startTime = new Date(idlTestTimeStamps.startTime());
					this.endTime = this.startTime;
					this.temporalPatternId = VOID_IDENTIFIER;
					break;
				case TEST_TEMPORAL_TYPE_PERIODICAL:
					final IdlPeriodicalTestTimeStamps idlPeriodicalTestTimeStamps = idlTestTimeStamps.ptts();
					this.startTime = new Date(idlPeriodicalTestTimeStamps.startTime);
					this.endTime = new Date(idlPeriodicalTestTimeStamps.endTime);
					this.temporalPatternId = Identifier.valueOf(idlPeriodicalTestTimeStamps.temporalPatternId);
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.getTestTemporalType());
			}

			assert this.isValid() : OBJECT_STATE_ILLEGAL;
		}

		/**
		 * @param orb
		 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
		 */
		public IdlTestTimeStamps getIdlTransferable(final ORB orb) {
			assert this.isValid() : OBJECT_STATE_ILLEGAL;

			final IdlTestTimeStamps idlTestTimeStamps = new IdlTestTimeStamps();
			switch (this.getTestTemporalType()) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					idlTestTimeStamps.startTime(this.startTime.getTime());
					break;
				case TEST_TEMPORAL_TYPE_PERIODICAL:
					idlTestTimeStamps.ptts(new IdlPeriodicalTestTimeStamps(this.startTime.getTime(),
							this.endTime.getTime(),
							this.temporalPatternId.getIdlTransferable()));
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.getTestTemporalType());
			}
			return idlTestTimeStamps;
		}

		protected boolean isValid() {
			if (this.startTime == null || this.endTime == null) {
				return false;
			}
			switch (this.getTestTemporalType()) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					return this.startTime == this.endTime;
				case TEST_TEMPORAL_TYPE_PERIODICAL:
					if (this.temporalPatternId == null) {
						return false;
					}
					final short temporalPatternIdMajor = this.temporalPatternId.getMajor();
					return (temporalPatternIdMajor == CRONTEMPORALPATTERN_CODE
									|| temporalPatternIdMajor == INTERVALSTEMPORALPATTERN_CODE
									|| temporalPatternIdMajor == PERIODICALTEMPORALPATTERN_CODE)
							&& this.startTime.before(this.endTime);
				default:
					return false;
			}
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 37 * result + this.startTime.hashCode();
			result = 37 * result + this.endTime.hashCode();
			result = 37 * result + this.temporalPatternId.hashCode();
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof TestTimeStamps) {
				final TestTimeStamps that = (TestTimeStamps) obj;
				if (that.getTestTemporalType() == this.getTestTemporalType()
						&& (this.startTime == that.startTime || 
								this.startTime != null && this.startTime.equals(that.startTime))
						&& (this.endTime == that.endTime || 
							this.endTime != null && this.endTime.equals(that.endTime))
						&& (this.temporalPatternId == that.temporalPatternId || 
								this.temporalPatternId != null && this.temporalPatternId.equals(that.temporalPatternId))) {
					return true;
				}
			}
			return false;
		}

		public TestTemporalType getTestTemporalType() {
			return Test.this.getTemporalType();
		}

		public Date getEndTime() {
			return this.endTime;
		}

		public Date getStartTime() {
			return this.startTime;
		}

		public Identifier getTemporalPatternId() {
			return this.temporalPatternId;
		}

		public void setStartTime(final Date startTime) {
			assert startTime != null : NON_NULL_EXPECTED;
			this.startTime = startTime;
		}

		public void setEndTime(final Date endTime) {
			assert endTime != null : NON_NULL_EXPECTED;
			this.endTime = endTime;
		}

		public void setTemporalPatternId(final Identifier temporalPatternId) {
			assert temporalPatternId != null : NON_NULL_EXPECTED;
			final short temporalPatternIdMajor = temporalPatternId.getMajor();
			assert temporalPatternId.isVoid()
					|| temporalPatternIdMajor == CRONTEMPORALPATTERN_CODE
					|| temporalPatternIdMajor == INTERVALSTEMPORALPATTERN_CODE
					|| temporalPatternIdMajor == PERIODICALTEMPORALPATTERN_CODE
				: ILLEGAL_ENTITY_CODE;
			this.temporalPatternId = temporalPatternId;
		}
	}

}
