/*
 * $Id: Test.java,v 1.97 2005/04/01 08:43:32 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.ContinuousTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.PeriodicalTestTimeStamps;
import com.syrus.util.HashCodeGenerator;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.97 $, $Date: 2005/04/01 08:43:32 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Test extends StorableObject {	
	private static final long	serialVersionUID	= 3688785890592241972L;

	protected static final int		RETRIEVE_MEASUREMENTS	= 1;
	protected static final int		RETRIEVE_LAST_MEASUREMENT	= 2;
	protected static final int		RETRIEVE_NUMBER_OF_MEASUREMENTS	= 3;
	protected static final int		RETRIEVE_NUMBER_OF_RESULTS	= 4;
	/**
	 * @deprecated
	 */
	protected static final int		UPDATE_MODIFIED			= 2;
	/**
	 * @deprecated
	 */
	protected static final int		UPDATE_STATUS			= 1;

	private int temporalType;
	private TestTimeStamps timeStamps;
	private Identifier measurementTypeId;
	private Identifier analysisTypeId;
	private Identifier evaluationTypeId;
	private int status;
	private MonitoredElement monitoredElement;
	private int	returnType;
	private String description;
	private java.util.Set measurementSetupIds;

	private MeasurementSetup mainMeasurementSetup;

	private StorableObjectDatabase	testDatabase;

	private Identifier kisId;
	private Identifier mcmId;

	public Test(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.measurementSetupIds = new HashSet();

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
		try {
			this.testDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Measurement createMeasurement(Identifier measurementCreatorId, Date startTime) throws CreateObjectException {
		Measurement measurement;
		try {
			measurement = Measurement.createInstance(measurementCreatorId,
					(MeasurementType) MeasurementStorableObjectPool.getStorableObject(this.measurementTypeId, true),
					this.monitoredElement.getId(),
					"created by Test:'" + this.getDescription() + "' at " + DatabaseDate.SDF.format(new Date(System.currentTimeMillis())),
					this.mainMeasurementSetup,
					startTime,
					this.monitoredElement.getLocalAddress(),
					this.id);
			MeasurementDatabaseContext.measurementDatabase.update(measurement, measurementCreatorId, StorableObjectDatabase.UPDATE_FORCE);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException("Cannot create measurement for test '" + this.id + "' -- " + ae.getMessage(), ae);
		}
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = measurementCreatorId;
		try {
			this.testDatabase.update(this, measurementCreatorId, StorableObjectDatabase.UPDATE_FORCE);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
		return measurement;
	}	
	
	protected Test(Identifier id,
					 Identifier creatorId,
					 long version,
					 Date startTime,
					 Date endTime,
					 Identifier temporalPatternId,
					 int temporalType,
					 Identifier measurementTypeId,
					 Identifier analysisTypeId,
					 Identifier evaluationTypeId,
					 MonitoredElement monitoredElement,
					 int returnType,
					 String description,
					 java.util.Set measurementSetupIds) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version);

		this.temporalType = temporalType;
		if (startTime != null)
				this.timeStamps = new TestTimeStamps(this.temporalType,
													 startTime,
													 endTime,
													 temporalPatternId);
		this.measurementTypeId = measurementTypeId;
		this.analysisTypeId = analysisTypeId;
		this.evaluationTypeId = evaluationTypeId;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType;
		this.description = description;
		this.measurementSetupIds = new HashSet();
		this.setMeasurementSetupIds0(measurementSetupIds);
		this.status = TestStatus._TEST_STATUS_NEW;
		
		this.testDatabase = MeasurementDatabaseContext.testDatabase;
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
	public static Test createInstance(Identifier creatorId,
			Date startTime,
			Date endTime,
			Identifier temporalPatternId,
			TestTemporalType temporalType,
			Identifier measurementTypeId,
			Identifier analysisTypeId,
			Identifier evaluationTypeId,
			MonitoredElement monitoredElement,
			TestReturnType returnType,
			String description,
			java.util.Set measurementSetupIds) throws CreateObjectException {
		if (creatorId == null
				|| startTime == null
				|| temporalType == null
				|| (temporalType.equals(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL) && (temporalPatternId == null || endTime == null))
				|| (temporalType.equals(TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS) && endTime == null)
				|| measurementTypeId == null
				|| monitoredElement == null
				|| returnType == null
				|| description == null
				|| (measurementSetupIds == null || measurementSetupIds.isEmpty()))
			throw new IllegalArgumentException("Argument is 'null'");
		if (measurementSetupIds.isEmpty())
			throw new IllegalArgumentException("No measurement setups");

		try {
			Test test = new Test(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TEST_ENTITY_CODE),
					creatorId,
					0L,
					startTime,
					endTime,
					temporalPatternId,
					temporalType.value(),
					measurementTypeId,
					analysisTypeId,
					evaluationTypeId,
					monitoredElement,
					returnType.value(),
					description,
					measurementSetupIds);
			test.changed = true;
			return test;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Test.createInstance | cannot generate identifier ", e);
		}

	}

	public Test(Test_Transferable tt) throws CreateObjectException {
		super(tt.header);
		this.temporalType = tt.temporal_type.value();
		this.timeStamps = new TestTimeStamps(tt.time_stamps);
		this.measurementTypeId = new Identifier(tt.measurement_type_id);
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		this.analysisTypeId = (tt.analysis_type_id.identifier_string.length() != 0) ? new Identifier(tt.analysis_type_id) : null;
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		this.evaluationTypeId = (tt.evaluation_type_id.identifier_string.length() != 0)	? new Identifier(tt.evaluation_type_id) : null;

		this.status = tt.status.value();

		try {
			this.monitoredElement = (MonitoredElement) ConfigurationStorableObjectPool.getStorableObject(new Identifier(tt.monitored_element_id),
					true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.returnType = tt.return_type.value();
		this.description = new String(tt.description);

		this.measurementSetupIds = Identifier.fromTransferables(tt.measurement_setup_ids);
		if (!this.measurementSetupIds.isEmpty()) {
			Identifier msId = (Identifier) this.measurementSetupIds.iterator().next();
			try {
				this.mainMeasurementSetup = (MeasurementSetup) MeasurementStorableObjectPool.getStorableObject(msId, true);
			}
			catch (ApplicationException ae) {
				throw new CreateObjectException(ae);
			}
		}
		else
			throw new CreateObjectException("Cannot find measurement setup for test '" + this.id + '\'');

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
	}

	public short getEntityCode() {
		return ObjectEntities.TEST_ENTITY_CODE;
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
	
	public void setEndTime(Date endTime) {
		this.timeStamps.endTime = endTime;
		super.changed = true;
	}

	public Identifier getEvaluationTypeId() {
		return this.evaluationTypeId;
	}

	public java.util.Set getMeasurementSetupIds() {
		return Collections.unmodifiableSet(this.measurementSetupIds);
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
	
	public void setStartTime(Date startTime) {
		this.timeStamps.startTime = startTime;
		super.changed = true;
	}

	public TestStatus getStatus() {
		return TestStatus.from_int(this.status);
	}

	public TestTemporalType getTemporalType() {
		return TestTemporalType.from_int(this.temporalType);
	}

	public Object getTransferable() {
		Identifier_Transferable[] msIdsT = null;
		try {
			msIdsT = Identifier.createTransferables(this.measurementSetupIds);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}

		return new Test_Transferable(super.getHeaderTransferable(),
				TestTemporalType.from_int(this.temporalType),
				this.timeStamps.getTransferable(),
				(Identifier_Transferable) this.measurementTypeId.getTransferable(),
				(this.analysisTypeId != null) ? (Identifier_Transferable) this.analysisTypeId.getTransferable()
						: (new Identifier_Transferable("")),
				(this.evaluationTypeId != null) ? (Identifier_Transferable) this.evaluationTypeId.getTransferable()
						: (new Identifier_Transferable("")),
				TestStatus.from_int(this.status),
				(Identifier_Transferable) this.monitoredElement.getId().getTransferable(),
				TestReturnType.from_int(this.returnType),
				new String(this.description),
				msIdsT);
	}

	public java.util.Set retrieveMeasurementsOrderByStartTime(MeasurementStatus measurementStatus)	throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (java.util.Set) this.testDatabase.retrieveObject(this, RETRIEVE_MEASUREMENTS, measurementStatus);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
	
	public Measurement retrieveLastMeasurement() throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (Measurement) this.testDatabase.retrieveObject(this, RETRIEVE_LAST_MEASUREMENT, null);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public int retrieveNumberOfMeasurements() throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return ((Integer) this.testDatabase.retrieveObject(this, RETRIEVE_NUMBER_OF_MEASUREMENTS, null)).intValue();
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public int retrieveNumberOfResults(ResultSort resultSort) throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return ((Integer) this.testDatabase.retrieveObject(this, RETRIEVE_NUMBER_OF_RESULTS, resultSort)).intValue();
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	/**
	 * @param analysisTypeId The analysisTypeId to set.
	 */
	public void setAnalysisTypeId(Identifier analysisTypeId) {
		super.changed = true;
		this.analysisTypeId = analysisTypeId;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		super.changed = true;
		this.description = description;
	}

	/**
	 * @param evaluationTypeId The evaluationTypeId to set.
	 */
	public void setEvaluationTypeId(Identifier evaluationTypeId) {
		super.changed = true;
		this.evaluationTypeId = evaluationTypeId;
	}

//	/**
//	 * @param measurementSetupIds The measurementSetupIds to set.
//	 */
//	public void setMeasurementSetupIds(List measurementSetupIds) {
//		super.changed = true;
//		this.measurementSetupIds = measurementSetupIds;
//	}

	/**
	 * @param measurementTypeId The measurementTypeId to set.
	 */
	public void setMeasurementTypeId(Identifier measurementTypeId) {
		super.changed = true;
		this.measurementTypeId = measurementTypeId;
	}
	/**
	 * @param monitoredElement The monitoredElement to set.
	 */
	public void setMonitoredElement(MonitoredElement monitoredElement) {
		super.changed = true;
		this.monitoredElement = monitoredElement;
	}

	/**
	 * @param returnType The returnType to set.
	 */
	public void setReturnType(TestReturnType returnType) {
		super.changed = true;
		this.returnType = returnType.value();
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(TestStatus status) {
		super.changed = true;
		this.status = status.value();
	}

	/*
	 * public ArrayList getMeasurements() { return this.measurements; }
	 */
	/**
	 * @deprecated
	 */
	public void updateStatus(TestStatus status1, Identifier modifierId1) throws UpdateObjectException {
		this.status = status1.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = modifierId1;
		try {
			this.testDatabase = MeasurementDatabaseContext.testDatabase;
			this.testDatabase.update(this, modifierId1, StorableObjectDatabase.UPDATE_FORCE);
		}
		catch (VersionCollisionException vce){
			throw new UpdateObjectException(vce.getMessage(), vce);
		}
	}

	/**
	 * @param temporalPatternId The temporalPatternId to set.
	 */
	public void setTemporalPatternId(Identifier temporalPatternId) {
		super.changed = true;
		this.timeStamps.temporalPatternId = temporalPatternId;
	}
	/**
	 * @param temporalType The temporalType to set.
	 */
	public void setTemporalType(TestTemporalType temporalType) {
		super.changed = true;
		this.temporalType = temporalType.value();
	}

	public synchronized void setAttributes(Date created,
										   Date modified,
										   Identifier creatorId,
										   Identifier modifierId,
										   long version,
										   int temporalType,
										   Date startTime,
										   Date endTime,
										   Identifier temporalPatternId,
										   Identifier measurementTypeId,
										   Identifier analysisTypeId,
										   Identifier evaluationTypeId,
										   int status,
										   MonitoredElement monitoredElement,
										   int returnType,
										   String description) {
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
		this.status = status;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType;
		this.description = description;
	}

	protected synchronized void setMeasurementSetupIds0(java.util.Set measurementSetupIds) {
		this.measurementSetupIds.clear();
		if (measurementSetupIds != null)
			this.measurementSetupIds.addAll(measurementSetupIds);

		if (!this.measurementSetupIds.isEmpty())
			try {
				this.mainMeasurementSetup = (MeasurementSetup) MeasurementStorableObjectPool.getStorableObject((Identifier) this.measurementSetupIds.iterator().next(),
						true);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
	}

	public void setMeasurementSetupIds(java.util.Set measurementSetupIds) {
		this.setMeasurementSetupIds0(measurementSetupIds);
		super.changed = true;
	}

	public Identifier getKISId() {
		if (this.kisId == null) {
			try {
				MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(this.getMonitoredElement().getMeasurementPortId(), true);
				this.kisId = measurementPort.getKISId();
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
		return this.kisId;
	}

	public Identifier getMCMId() {
		if (this.mcmId == null) {
			try {
				KIS kis = (KIS) ConfigurationStorableObjectPool.getStorableObject(this.getKISId(), true);
				this.mcmId = kis.getMCMId();
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
		return this.mcmId;
	}

	public java.util.Set getDependencies() {
		java.util.Set dependencies = new HashSet();
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

	public class TestTimeStamps {
		Date endTime;
		Date startTime;
		Identifier temporalPatternId;

		private int	discriminator;		

		TestTimeStamps(int temporalType,
									 Date startTime,
									 Date endTime,
									 Identifier temporalPatternId) {
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
		}

		TestTimeStamps(TestTimeStamps_Transferable ttst) throws CreateObjectException {
			this.discriminator = ttst.discriminator().value();
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					this.startTime = new Date(ttst.start_time());
					this.endTime = null;
					this.temporalPatternId = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					PeriodicalTestTimeStamps ptts = ttst.ptts();
					this.startTime = new Date(ptts.start_time);
					this.endTime = new Date(ptts.end_time);
					this.temporalPatternId = new Identifier(ptts.temporal_pattern_id);
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					ContinuousTestTimeStamps ctts = ttst.ctts();
					this.startTime = new Date(ctts.start_time);
					this.endTime = new Date(ctts.end_time);
					this.temporalPatternId = null;
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal discriminator: " + this.discriminator);
			}
		}

		TestTimeStamps_Transferable getTransferable() {
			TestTimeStamps_Transferable ttst = new TestTimeStamps_Transferable();
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					ttst.start_time(this.startTime.getTime());
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					ttst.ptts(new PeriodicalTestTimeStamps(this.startTime.getTime(),
																								 this.endTime.getTime(),
																								 (this.temporalPatternId != null) ? (Identifier_Transferable)this.temporalPatternId.getTransferable() : (new Identifier_Transferable("")) ));
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
		
		
		public int hashCode() {
			HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
			hashCodeGenerator.addInt(this.discriminator);
			hashCodeGenerator.addObject(this.startTime);
			hashCodeGenerator.addObject(this.endTime);
			hashCodeGenerator.addObject(this.temporalPatternId);
			int result = hashCodeGenerator.getResult(); 
			return result;
		}
		
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
