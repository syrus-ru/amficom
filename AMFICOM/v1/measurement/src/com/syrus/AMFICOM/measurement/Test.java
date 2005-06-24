/*-
 * $Id: Test.java,v 1.133 2005/06/24 13:54:36 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.ContinuousTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.PeriodicalTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.AMFICOM.resource.LangModelMeasurement;
import com.syrus.util.HashCodeGenerator;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.133 $, $Date: 2005/06/24 13:54:36 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class Test extends StorableObject {	
	private static final long	serialVersionUID	= 3688785890592241972L;

	protected static final int		RETRIEVE_MEASUREMENTS	= 1;
	protected static final int		RETRIEVE_LAST_MEASUREMENT	= 2;
	protected static final int		RETRIEVE_NUMBER_OF_RESULTS	= 3;

	private int temporalType;
	private TestTimeStamps timeStamps;
	private Identifier measurementTypeId;
	private Identifier analysisTypeId;
	private Identifier evaluationTypeId;
	private int status;
	private MonitoredElement monitoredElement;
	private int	returnType;
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
	Test(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.measurementSetupIds = new HashSet<Identifier>();

		final TestDatabase database = (TestDatabase) DatabaseContext.getDatabase(ObjectEntities.TEST_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Measurement createMeasurement(final Identifier measurementCreatorId, final Date startTime) throws CreateObjectException {
		if (this.status != TestStatus._TEST_STATUS_PROCESSING)
			throw new CreateObjectException("Status of test '" + this.id + "' is " + this.status
					+ ", not " + TestStatus._TEST_STATUS_PROCESSING + " (PROCESSING)");

		Measurement measurement;
		try {
			measurement = Measurement.createInstance(measurementCreatorId,
					(MeasurementType) StorableObjectPool.getStorableObject(this.measurementTypeId, true),
					this.monitoredElement.getId(),
					LangModelMeasurement.getString("created by Test") + ":'"
						+ this.getDescription()
						+ "' " + LangModelMeasurement.getString("at.time")
						+ " " + DatabaseDate.SDF.format(new Date(System.currentTimeMillis())),
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
			final long version,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final int temporalType,
			final Identifier measurementTypeId,
			final Identifier analysisTypeId,
			final Identifier evaluationTypeId,
			final Identifier groupTestId,
			final MonitoredElement monitoredElement,
			final int returnType,
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
		this.measurementTypeId = measurementTypeId;
		this.analysisTypeId = analysisTypeId;
		this.evaluationTypeId = evaluationTypeId;
		this.groupTestId = groupTestId;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType;
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
	 * @param measurementTypeId
	 * @param analysisTypeId
	 * @param evaluationTypeId
	 * @param monitoredElement
	 * @param returnType
	 * @param description
	 * @param measurementSetupIds
	 * @throws CreateObjectException
	 */
	public static Test createInstance(final Identifier creatorId,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final TestTemporalType temporalType,
			final Identifier measurementTypeId,
			final Identifier analysisTypeId,
			final Identifier evaluationTypeId,
			final Identifier groupTestId,
			final MonitoredElement monitoredElement,
			final TestReturnType returnType,
			final String description,
			final Set<Identifier> measurementSetupIds) throws CreateObjectException {
		try {
			Test test = new Test(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TEST_CODE),
					creatorId,
					0L,
					startTime,
					endTime,
					temporalPatternId,
					temporalType.value(),
					measurementTypeId,
					analysisTypeId,
					evaluationTypeId,
					groupTestId,
					monitoredElement,
					returnType.value(),
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
	public void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlTest tt = (IdlTest)transferable;
		super.fromTransferable(tt.header);
		this.temporalType = tt.timeStamps.discriminator().value();
		this.timeStamps = new TestTimeStamps(tt.timeStamps);
		this.measurementTypeId = new Identifier(tt.measurementTypeId);
		this.analysisTypeId = new Identifier(tt.analysisTypeId);
		this.evaluationTypeId = new Identifier(tt.evaluationTypeId);

		this.status = tt.status.value();

		this.monitoredElement = (MonitoredElement) StorableObjectPool.getStorableObject(new Identifier(tt.monitoredElementId), true);

		this.returnType = tt.returnType.value();
		this.description = tt.description;
		this.numberOfMeasurements = tt.numberOfMeasurements;

		this.measurementSetupIds = Identifier.fromTransferables(tt.measurementSetupIds);
		if (!this.measurementSetupIds.isEmpty()) {
			final Identifier msId = this.measurementSetupIds.iterator().next();
			this.mainMeasurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(msId, true);
		} else
			throw new IllegalDataException("Cannot find measurement setup for test '" + this.id + '\'');
		
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
				&& this.timeStamps != null
				&& this.timeStamps.isValid()
				&& this.measurementTypeId != null
				&& this.monitoredElement != null
				&& this.description != null
				&& this.measurementSetupIds != null
			//&& !this.measurementSetupIds.isEmpty() && this.mainMeasurementSetup != null
			;
	}
	
	public short getEntityCode() {
		return ObjectEntities.TEST_CODE;
	}

	public Identifier getAnalysisTypeId() {
		return this.analysisTypeId;
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

	public Identifier getEvaluationTypeId() {
		return this.evaluationTypeId;
	}

	public Set<Identifier> getMeasurementSetupIds() {
		return Collections.unmodifiableSet(this.measurementSetupIds);
	}

	public Identifier getMainMeasurementSetupId() {
		return this.mainMeasurementSetup.getId();
	}

	public Identifier getMeasurementTypeId() {
		return this.measurementTypeId;
	}

	public MonitoredElement getMonitoredElement() {
		return this.monitoredElement;
	}

	public Identifier getTemporalPatternId() {
		return this.timeStamps.temporalPatternId;
	}

	public TestReturnType getReturnType() {
		return TestReturnType.from_int(this.returnType);
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

	public IdlTest getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final IdlIdentifier[] msIdsT = Identifier.createTransferables(this.measurementSetupIds);

		return new IdlTest(super.getHeaderTransferable(),
				this.timeStamps.getTransferable(),
				this.measurementTypeId.getTransferable(),
				this.analysisTypeId.getTransferable(),
				this.evaluationTypeId.getTransferable(),
				this.groupTestId.getTransferable(),
				TestStatus.from_int(this.status),
				this.monitoredElement.getId().getTransferable(),
				TestReturnType.from_int(this.returnType),
				this.description,
				this.numberOfMeasurements,
				msIdsT);
	}

	public Measurement retrieveLastMeasurement() throws RetrieveObjectException, ObjectNotFoundException {
		final TestDatabase database = (TestDatabase) DatabaseContext.getDatabase(ObjectEntities.TEST_CODE);
		try {
			final Measurement measurement = (Measurement) database.retrieveObject(this, RETRIEVE_LAST_MEASUREMENT, null);
			try {
				StorableObjectPool.putStorableObject(measurement);
			} catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}
			return measurement;
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public int retrieveNumberOfResults(final ResultSort resultSort) throws RetrieveObjectException, ObjectNotFoundException {
		final TestDatabase database = (TestDatabase) DatabaseContext.getDatabase(ObjectEntities.TEST_CODE);
		try {
			return ((Integer) database.retrieveObject(this, RETRIEVE_NUMBER_OF_RESULTS, resultSort)).intValue();
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	/**
	 * @param analysisTypeId The analysisTypeId to set.
	 */
	public void setAnalysisTypeId(final Identifier analysisTypeId) {
		this.analysisTypeId = analysisTypeId;
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
	 * @param evaluationTypeId The evaluationTypeId to set.
	 */
	public void setEvaluationTypeId(final Identifier evaluationTypeId) {
		this.evaluationTypeId = evaluationTypeId;
		super.markAsChanged();
	}

	/**
	 * @param measurementTypeId The measurementTypeId to set.
	 */
	public void setMeasurementTypeId(final Identifier measurementTypeId) {
		this.measurementTypeId = measurementTypeId;
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
	 * @param returnType The returnType to set.
	 */
	public void setReturnType(final TestReturnType returnType) {
		this.returnType = returnType.value();
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
			final long version,
			final int temporalType,
			final Date startTime,
			final Date endTime,
			final Identifier temporalPatternId,
			final Identifier measurementTypeId,
			final Identifier analysisTypeId,
			final Identifier evaluationTypeId,
			final Identifier groupTestId,
			final int status,
			final MonitoredElement monitoredElement,
			final int returnType,
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

		this.measurementTypeId = measurementTypeId;
		this.analysisTypeId = analysisTypeId;
		this.evaluationTypeId = evaluationTypeId;
		this.groupTestId = groupTestId;
		this.status = status;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType;
		this.description = description;
		this.numberOfMeasurements = numberOfMeasurements;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setMeasurementSetupIds0(final Set<Identifier> measurementSetupIds) {
		this.measurementSetupIds.clear();
		if (measurementSetupIds != null)
			this.measurementSetupIds.addAll(measurementSetupIds);

		if (!this.measurementSetupIds.isEmpty())
			try {
				this.mainMeasurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(this.measurementSetupIds.iterator().next(),
						true);
			} catch (ApplicationException ae) {
				Log.errorException(ae);
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
		if (this.timeStamps.temporalPatternId != null)
			dependencies.add(this.timeStamps.temporalPatternId);

		dependencies.addAll(this.measurementSetupIds);
		dependencies.add(this.measurementTypeId);

		if (this.analysisTypeId != null)
			dependencies.add(this.analysisTypeId);

		if (this.evaluationTypeId != null)
			dependencies.add(this.evaluationTypeId);

		dependencies.add(this.monitoredElement);
		return dependencies;
	}

	public final class TestTimeStamps {
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
					this.temporalPatternId = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					this.startTime = startTime;
					this.endTime = endTime;
					this.temporalPatternId = temporalPatternId;
					if (this.endTime == null) {
						Log.errorMessage("ERROR: End time is NULL");
						this.endTime = this.startTime;
					}
					if (this.temporalPatternId == null)
						Log.errorMessage("ERROR: Temporal pattern is NULL");
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					this.startTime = startTime;
					this.endTime = endTime;
					this.temporalPatternId = null;
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
					this.temporalPatternId = null;
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
					this.temporalPatternId = null;
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.discriminator);
			}

			assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		}

		IdlTestTimeStamps getTransferable() {
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
					ttst.ctts(new ContinuousTestTimeStamps(this.startTime.getTime(),
						this.endTime.getTime()));
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
					return this.startTime != null && this.endTime != null && this.temporalPatternId != null && this.startTime.getTime() < this.endTime.getTime();
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					return this.startTime != null && this.endTime != null && this.startTime.getTime() < this.endTime.getTime();
				default:
					return false;
			}
		}

		@Override
		public int hashCode() {
			HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
			hashCodeGenerator.addInt(this.discriminator);
			hashCodeGenerator.addObject(this.startTime);
			hashCodeGenerator.addObject(this.endTime);
			hashCodeGenerator.addObject(this.temporalPatternId);
			int result = hashCodeGenerator.getResult();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			boolean equals = (this==obj);
			if ((!equals)&&(obj instanceof TestTimeStamps)) {
				TestTimeStamps stamps = (TestTimeStamps)obj;
				if ((stamps.discriminator==this.discriminator)&&
					(stamps.startTime==this.startTime)&&	
					(stamps.endTime==this.endTime)&&
					(stamps.temporalPatternId.equals(this.temporalPatternId)))
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
		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}
		public void setTemporalPatternId(Identifier temporalPatternId) {
			this.temporalPatternId = temporalPatternId;
		}
		public void setTestTemporalType(TestTemporalType temporalType) {
			this.discriminator = temporalType.value();
		}
	}

}
