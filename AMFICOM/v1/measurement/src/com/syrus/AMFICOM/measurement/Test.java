/*-
 * $Id: Test.java,v 1.175 2005/10/30 14:49:05 bass Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.measurement.corba.IdlTestHelper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStops;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.PeriodicalTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.Log;
import com.syrus.util.TransferableObject;

/**
 * @version $Revision: 1.175 $, $Date: 2005/10/30 14:49:05 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Test extends StorableObject<Test> implements Describable {	
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
	
	private SortedMap<Date, String> stoppingMap;  

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
							+ EasyDateFormatter.formatDate(startTime),
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
		if (startTime != null) {
			this.timeStamps = new TestTimeStamps(this.temporalType, startTime, endTime, temporalPatternId);
		}
		this.measurementType = measurementType;
		this.analysisType = analysisType;
		this.groupTestId = groupTestId;
		this.monitoredElement = monitoredElement;
		this.description = description;
		this.measurementSetupIds = new HashSet<Identifier>();
		this.setMeasurementSetupIds0(measurementSetupIds);
		this.status = TestStatus._TEST_STATUS_NEW;
		this.numberOfMeasurements = 0;
		this.stoppingMap = new TreeMap<Date, String>();
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
		
		this.stoppingMap = new TreeMap<Date, String>();
		for(int index = 0; index < tt.stoppings.length; index++) {
			this.stoppingMap.put(new Date(tt.stoppings[index].time), tt.stoppings[index].reason);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

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
				&& this.stoppingMap != null
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
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
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

	public Identifier getMonitoredElementId() {
		return this.monitoredElement == null
				? VOID_IDENTIFIER
				: this.monitoredElement.getId();
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
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlTest getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] msIdsT = Identifier.createTransferables(this.measurementSetupIds);

		final TestStops[] testStops = new TestStops[this.stoppingMap.size()];
		
		int index = 0;
		for(final Date stopDate : this.stoppingMap.keySet()) {
			testStops[index++] = new TestStops(stopDate.getTime(), this.stoppingMap.get(stopDate));
		}
		
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
				testStops,
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
	 * <p>Never call this method ! </p>  
	 * @throws UnsupportedOperationException
	 */
	public void setName(final String name) {
		throw new UnsupportedOperationException("Test.setName() is unsupported");		
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
	synchronized void setStoppingMap0(final SortedMap<Date, String> stoppingMap) {
		this.stoppingMap.clear();
		if (stoppingMap != null) {
			this.stoppingMap.putAll(stoppingMap);
		}
	}
	
	/**
	 * add test stopping 
	 * @param stoppingTime time, when stop test
	 * @param reason reason to stop test
	 */
	public void addStopping(final Date stoppingTime, final String reason) {
		this.stoppingMap.put(new Date(stoppingTime.getTime()), reason);
		super.markAsChanged();
	}
	
	/**
	 * stop test now. 
	 * @param reason reason to stop test
	 * @see #addStopping(Date, String)
	 */
	public void addStopping(final String reason) {
		this.addStopping(new Date(), reason);
	}
	
	/**
	 * @return map of test stop time and reason to stop
	 */
	public SortedMap<Date, String> getStoppingMap() {
		return Collections.unmodifiableSortedMap(this.stoppingMap);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	synchronized void setMeasurementSetupIds0(final Set<Identifier> measurementSetupIds) {
		this.measurementSetupIds.clear();
		if (measurementSetupIds != null) {
			this.measurementSetupIds.addAll(measurementSetupIds);
		}

		if (!this.measurementSetupIds.isEmpty()) {
			try {
				this.mainMeasurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(this.measurementSetupIds.iterator().next(),
						true);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
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
				Log.errorMessage(ae);
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
				Log.errorMessage(ae);
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

		if (!this.groupTestId.isVoid() && !this.id.equals(this.groupTestId)) {
			dependencies.add(this.groupTestId);
		}

		dependencies.addAll(this.measurementSetupIds);
		
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		
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

	public final class TestTimeStamps
			implements TransferableObject<IdlTestTimeStamps> {
		private static final long serialVersionUID = -3560328752462377043L;

		Date startTime;
		Date endTime;
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
					this.endTime = this.startTime;
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
					this.endTime = this.startTime;
					this.temporalPatternId = VOID_IDENTIFIER;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					PeriodicalTestTimeStamps ptts = ttst.ptts();
					this.startTime = new Date(ptts.startTime);
					this.endTime = new Date(ptts.endTime);
					this.temporalPatternId = new Identifier(ptts.temporalPatternId);
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.discriminator);
			}

			assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		}

		/**
		 * @param orb
		 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
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
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.discriminator);
			}
			return ttst;
		}

		protected boolean isValid() {
			boolean valid = (this.startTime != null) && (this.endTime != null);
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					return valid;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					return valid && (this.temporalPatternId != null) && (this.startTime.before(this.endTime));
				default:
					return false;
			}
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 37 * result + this.discriminator;
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
				TestTimeStamps that = (TestTimeStamps) obj;
				if (that.discriminator == this.discriminator
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
