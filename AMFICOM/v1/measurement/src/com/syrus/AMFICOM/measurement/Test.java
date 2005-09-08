/*-
 * $Id: Test.java,v 1.153 2005/09/08 18:26:30 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.util.Collections;
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.measurement.corba.IdlTestHelper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.ContinuousTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.PeriodicalTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.HashCodeGenerator;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.153 $, $Date: 2005/09/08 18:26:30 $
 * @author $Author: bass $
 * @module measurement
 */

public final class Test extends StorableObject {	
	private static final long	serialVersionUID	= 3688785890592241972L;

	private int temporalType;
	private TestTimeStamps timeStamps;
	private MeasurementType measurementType;
	private AnalysisType analysisType;
	private int status;
	private MonitoredElement monitoredElement;
	private String description;
	private int numberOfMeasurements;
	private Set<Identifier> measurementSetupIds;

	private MeasurementSetup mainMeasurementSetup;

	private Identifier groupTestId;
	private Identifier kisId;
	private Identifier mcmId;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Measurement createMeasurement(final Identifier measurementCreatorId, final Date startTime) throws CreateObjectException {
		if (this.status != TestStatus._TEST_STATUS_PROCESSING) {
			throw new CreateObjectException("Status of test '" + this.id + "' is " + this.status
					+ ", not " + TestStatus._TEST_STATUS_PROCESSING + " (PROCESSING)");
		}

		Measurement measurement;
		try {
			measurement = Measurement.createInstance(measurementCreatorId,
					this.measurementType,
					this.monitoredElement.getId(),
					LangModelMeasurement.getString("RR") + "-"
							+ this.monitoredElement.getName() + "-"
							+ EasyDateFormatter.formatDate(this.getStartTime()),
					this.mainMeasurementSetup,
					startTime,
					this.monitoredElement.getLocalAddress(),
					this.id);
		} catch (ApplicationException ae) {
			throw new CreateObjectException("Cannot create measurement for test '" + this.id + "' -- " + ae.getMessage(), ae);
		}
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = measurementCreatorId;
		this.numberOfMeasurements++;

		super.markAsChanged();

		return measurement;
	}	

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Test(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final int temporalType,
			final MeasurementType measurementType,
			final AnalysisType analysisType,
			final Identifier groupTestId,
			final MonitoredElement monitoredElement,
			final String description,
			final Set<Identifier> measurementSetupIds) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);

		this.temporalType = temporalType;
		if (startTime != null)
			this.timeStamps = new TestTimeStamps(this.temporalType, startTime, endTime, temporalPatternId);
		this.measurementType = measurementType;
		this.analysisType = analysisType;
		this.groupTestId = groupTestId;
		this.monitoredElement = monitoredElement;
		this.description = description;
		this.measurementSetupIds = new HashSet<Identifier>();
		this.setMeasurementSetupIds0(measurementSetupIds);
		this.status = TestStatus._TEST_STATUS_NEW;
		this.numberOfMeasurements = 0;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param startTime
	 * @param endTime
	 * @param temporalPatternId
	 * @param temporalType
	 * @param measurementType
	 * @param analysisType
	 * @param monitoredElement
	 * @param description
	 * @param measurementSetupIds
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final TestTemporalType temporalType,
			final MeasurementType measurementType,
			final AnalysisType analysisType,
			final Identifier groupTestId,
			final MonitoredElement monitoredElement,
			final String description,
			final Set<Identifier> measurementSetupIds) throws CreateObjectException {
		try {
			final Test test = new Test(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TEST_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					startTime,
					endTime,
					temporalPatternId,
					temporalType.value(),
					measurementType,
					analysisType,
					groupTestId,
					monitoredElement,
					description,
					measurementSetupIds);

			assert test.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			test.markAsChanged();

			return test;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}

	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Test(final IdlTest tt) throws CreateObjectException {
		try {
			this.fromTransferable(tt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlTest tt = (IdlTest)transferable;
		super.fromTransferable(tt);
		this.temporalType = tt.timeStamps.discriminator().value();
		this.timeStamps = new TestTimeStamps(tt.timeStamps);
		this.measurementType = MeasurementType.fromTransferable(tt.measurementType);
		this.analysisType = AnalysisType.fromTransferable(tt.analysisType);

		this.status = tt.status.value();
		this.groupTestId = new Identifier(tt.groupTestId);

		this.monitoredElement = (MonitoredElement) StorableObjectPool.getStorableObject(new Identifier(tt.monitoredElementId), true);

		this.description = tt.description;
		this.numberOfMeasurements = tt.numberOfMeasurements;

		this.measurementSetupIds = Identifier.fromTransferables(tt.measurementSetupIds);
		if (!this.measurementSetupIds.isEmpty()) {
			final Identifier msId = this.measurementSetupIds.iterator().next();
			this.mainMeasurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(msId, true);
		} else {
			throw new IllegalDataException("Cannot find measurement setup for test '" + this.id + '\'');
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.timeStamps != null && this.timeStamps.isValid()
				&& this.measurementType != null
				&& this.analysisType != null
				&& this.monitoredElement != null
				&& this.description != null
				&& this.measurementSetupIds != null
				&& this.groupTestId != null
			//&& !this.measurementSetupIds.isEmpty() && this.mainMeasurementSetup != null
			;
	}
	
	public short getEntityCode() {
		return ObjectEntities.TEST_CODE;
	}

	public MeasurementType getMeasurementType() {
		return this.measurementType;
	}

	public AnalysisType getAnalysisType() {
		return this.analysisType;
	}

	public String getDescription() {
		return this.description;
	}

	public Date getEndTime() {
		return this.timeStamps.endTime;
	}
	
	public void setEndTime(final Date endTime) {
		this.timeStamps.endTime = endTime;
		super.markAsChanged();
	}

	public Set<Identifier> getMeasurementSetupIds() {
		return Collections.unmodifiableSet(this.measurementSetupIds);
	}

	public Identifier getMainMeasurementSetupId() {
		return this.mainMeasurementSetup.getId();
	}

	public MonitoredElement getMonitoredElement() {
		return this.monitoredElement;
	}

	public Identifier getTemporalPatternId() {
		return this.timeStamps.temporalPatternId;
	}

	public Date getStartTime() {
		return this.timeStamps.startTime;
	}
	
	public void setStartTime(final Date startTime) {
		this.timeStamps.startTime = startTime;
		super.markAsChanged();
	}

	public TestStatus getStatus() {
		return TestStatus.from_int(this.status);
	}

	public TestTemporalType getTemporalType() {
		return TestTemporalType.from_int(this.temporalType);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTest getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] msIdsT = Identifier.createTransferables(this.measurementSetupIds);

		return IdlTestHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.timeStamps.getTransferable(orb),
				this.measurementType.getTransferable(orb),
				this.analysisType.getTransferable(orb),
				this.groupTestId.getTransferable(),
				TestStatus.from_int(this.status),
				this.monitoredElement.getId().getTransferable(),
				this.description,
				this.numberOfMeasurements,
				msIdsT);
	}

	/**
	 * @param measurementType The measurementType to set.
	 */
	public void setMeasurementType(final MeasurementType measurementType) {
		
		assert measurementType != null : ErrorMessages.NON_NULL_EXPECTED;
		
		this.measurementType = measurementType;
		super.markAsChanged();
	}

	/**
	 * @param analysisType The analysisType to set.
	 */
	public void setAnalysisType(final AnalysisType analysisType) {
		
		assert analysisType != null : ErrorMessages.NON_NULL_EXPECTED;
		
		this.analysisType = analysisType;
		super.markAsChanged();
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @param monitoredElement The monitoredElement to set.
	 */
	public void setMonitoredElement(final MonitoredElement monitoredElement) {
		this.monitoredElement = monitoredElement;
		super.markAsChanged();
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(final TestStatus status) {
		this.status = status.value();
		super.markAsChanged();
	}

	public Identifier getGroupTestId() {
		return this.groupTestId;
	}
	
	public void setGroupTestId(final Identifier groupTestId) {
		this.groupTestId = groupTestId;
		this.markAsChanged();
	}
	
	/**
	 * @param temporalPatternId The temporalPatternId to set.
	 */
	public void setTemporalPatternId(final Identifier temporalPatternId) {
		this.timeStamps.temporalPatternId = temporalPatternId;
		super.markAsChanged();
	}
	/**
	 * @param temporalType The temporalType to set.
	 */
	public void setTemporalType(final TestTemporalType temporalType) {
		this.temporalType = temporalType.value();
		super.markAsChanged();
	}

	public int getNumberOfMeasurements() {
		return this.numberOfMeasurements;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final int temporalType,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final MeasurementType measurementType,
			final AnalysisType analysisType,
			final Identifier groupTestId,
			final int status,
			final MonitoredElement monitoredElement,
			final String description,
			final int numberOfMeasurements) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version);
		this.temporalType = temporalType;
		this.timeStamps = new TestTimeStamps(temporalType,
			startTime,
			endTime,
			temporalPatternId);

		this.measurementType = measurementType;
		this.analysisType = analysisType;
		this.groupTestId = groupTestId;
		this.status = status;
		this.monitoredElement = monitoredElement;
		this.description = description;
		this.numberOfMeasurements = numberOfMeasurements;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setMeasurementSetupIds0(final Set<Identifier> measurementSetupIds) {
		this.measurementSetupIds.clear();
		if (measurementSetupIds != null) {
			this.measurementSetupIds.addAll(measurementSetupIds);
		}

		if (!this.measurementSetupIds.isEmpty()) {
			try {
				this.mainMeasurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(this.measurementSetupIds.iterator().next(),
						true);
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
	}

	public void setMeasurementSetupIds(final Set<Identifier> measurementSetupIds) {
		this.setMeasurementSetupIds0(measurementSetupIds);
		super.markAsChanged();
	}

	public Identifier getKISId() {
		if (this.kisId == null) {
			try {
				final MeasurementPort measurementPort = (MeasurementPort) StorableObjectPool.getStorableObject(this.getMonitoredElement().getMeasurementPortId(),
						true);
				this.kisId = measurementPort.getKISId();
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
		return this.kisId;
	}

	public Identifier getMCMId() {
		if (this.mcmId == null) {
			try {
				final KIS kis = (KIS) StorableObjectPool.getStorableObject(this.getKISId(), true);
				this.mcmId = kis.getMCMId();
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
		return this.mcmId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		if (this.temporalType == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL && !this.timeStamps.temporalPatternId.isVoid()) {
			dependencies.add(this.timeStamps.temporalPatternId);
		}

		dependencies.addAll(this.measurementSetupIds);

		dependencies.add(this.monitoredElement);
		return dependencies;
	}

	public static IdlTest[] createTransferables(final Set<Test> tests, final ORB orb) {
		assert tests != null : ErrorMessages.NON_NULL_EXPECTED;

		final IdlTest[] transferables = new IdlTest[tests.size()];
		int i = 0;
		synchronized (tests) {
			for (final Test test : tests) {
				transferables[i++] = test.getTransferable(orb);
			}
		}
		return transferables;
	}

	public final class TestTimeStamps implements TransferableObject {
		Date endTime;
		Date startTime;
		Identifier temporalPatternId;

		private int	discriminator;		

		TestTimeStamps(final int temporalType,
				final Date startTime,
				final Date endTime,
				final Identifier temporalPatternId) {
			this.discriminator = temporalType;
			switch (temporalType) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					this.startTime = startTime;
					this.endTime = null;
					this.temporalPatternId = VOID_IDENTIFIER;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
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
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					this.startTime = startTime;
					this.endTime = endTime;
					this.temporalPatternId = VOID_IDENTIFIER;
					if (this.endTime == null) {
						Log.errorMessage("ERROR: End time is NULL");
						this.endTime = this.startTime;
					}
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal temporal type: " + temporalType + " of test");
			}
			assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		}

		TestTimeStamps(final IdlTestTimeStamps ttst) {
			this.discriminator = ttst.discriminator().value();
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					this.startTime = new Date(ttst.startTime());
					this.endTime = null;
					this.temporalPatternId = VOID_IDENTIFIER;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					PeriodicalTestTimeStamps ptts = ttst.ptts();
					this.startTime = new Date(ptts.startTime);
					this.endTime = new Date(ptts.endTime);
					this.temporalPatternId = new Identifier(ptts.temporalPatternId);
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					ContinuousTestTimeStamps ctts = ttst.ctts();
					this.startTime = new Date(ctts.startTime);
					this.endTime = new Date(ctts.endTime);
					this.temporalPatternId = VOID_IDENTIFIER;
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.discriminator);
			}

			assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		}

		/**
		 * @param orb
		 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
		 */
		@SuppressWarnings("unused")
		public IdlTestTimeStamps getTransferable(final ORB orb) {
			assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			final IdlTestTimeStamps ttst = new IdlTestTimeStamps();
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					ttst.startTime(this.startTime.getTime());
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					ttst.ptts(new PeriodicalTestTimeStamps(this.startTime.getTime(),
							this.endTime.getTime(),
							this.temporalPatternId.getTransferable()));
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					ttst.ctts(new ContinuousTestTimeStamps(this.startTime.getTime(), this.endTime.getTime()));
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.discriminator);
			}
			return ttst;
		}

		protected boolean isValid() {
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					return this.startTime != null;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					return this.startTime != null
							&& this.endTime != null
							&& this.temporalPatternId != null
							&& this.startTime.getTime() < this.endTime.getTime();
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					return this.startTime != null && this.endTime != null && this.startTime.getTime() < this.endTime.getTime();
				default:
					return false;
			}
		}

		@Override
		public int hashCode() {
			final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
			hashCodeGenerator.addInt(this.discriminator);
			hashCodeGenerator.addObject(this.startTime);
			hashCodeGenerator.addObject(this.endTime);
			hashCodeGenerator.addObject(this.temporalPatternId);
			int result = hashCodeGenerator.getResult();
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			boolean equals = (this == obj);
			if ((!equals) && (obj instanceof TestTimeStamps)) {
				TestTimeStamps stamps = (TestTimeStamps) obj;
				if ((stamps.discriminator == this.discriminator)
						&& (stamps.startTime == this.startTime)
						&& (stamps.endTime == this.endTime)
						&& (stamps.temporalPatternId.equals(this.temporalPatternId)))
					equals = true;
			}
			return equals;
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

		public TestTemporalType getTestTemporalType() {
			return TestTemporalType.from_int(this.discriminator);
		}

		public void setEndTime(final Date endTime) {
			this.endTime = endTime;
		}

		public void setStartTime(final Date startTime) {
			this.startTime = startTime;
		}

		public void setTemporalPatternId(final Identifier temporalPatternId) {
			this.temporalPatternId = temporalPatternId;
		}

		public void setTestTemporalType(final TestTemporalType temporalType) {
			this.discriminator = temporalType.value();
		}
	}

}
