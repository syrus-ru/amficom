/*
 * $Id: Test.java,v 1.50 2004/09/01 15:26:05 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.util.HashCodeGenerator;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.ContinuousTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.PeriodicalTestTimeStamps;

/**
 * @version $Revision: 1.50 $, $Date: 2004/09/01 15:26:05 $
 * @author $Author: max $
 * @module measurement_v1
 */

public class Test extends StorableObject {
	protected static final int		RETRIEVE_MEASUREMENTS	= 1;
	protected static final int		RETRIEVE_LAST_MEASUREMENT	= 2;
	protected static final int		RETRIEVE_NUMBER_OF_MEASUREMENTS	= 3;
	protected static final int		RETRIEVE_NUMBER_OF_RESULTS	= 4;
	protected static final int		UPDATE_MODIFIED			= 2;
	protected static final int		UPDATE_STATUS			= 1;

	private int temporalType;
	private TestTimeStamps timeStamps;
	private MeasurementType measurementType;
	private AnalysisType analysisType;
	private EvaluationType evaluationType;
	private int status;
	private MonitoredElement monitoredElement;
	private int	returnType;
	private String description;
	private List measurementSetupIds;

	private MeasurementSetup mainMeasurementSetup;
	
	private StorableObjectDatabase	testDatabase;

	public Test(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
		try {
			this.testDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Measurement createMeasurement(Identifier measurementId,
																			 Identifier creatorId,
																			 Date startTime) throws CreateObjectException {
		Measurement measurement1 = Measurement.createInstance(measurementId,
															 creatorId,
															 this.measurementType,
															 this.monitoredElement.getId(),
															 this.mainMeasurementSetup,
															 startTime,
															 this.monitoredElement.getLocalAddress(),
															 this.id);
		Measurement measurement = Measurement.getInstance((Measurement_Transferable)measurement1.getTransferable());
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = (Identifier) creatorId.clone();
		try {
			this.testDatabase.update(this, UPDATE_MODIFIED, null);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		return measurement;
	}	
	
	protected Test(Identifier id,
							 Identifier creatorId,
							 Date startTime,
							 Date endTime,
							 TemporalPattern temporalPattern,
							 int temporalType,
							 MeasurementType measurementType,
							 AnalysisType analysisType,
							 EvaluationType evaluationType,
							 MonitoredElement monitoredElement,
							 int returnType,
							 String description,
							 List measurementSetupIds){
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.temporalType = temporalType;
		if (startTime != null)
				this.timeStamps = new TestTimeStamps(this.temporalType,
													 startTime,
													 endTime,
													 temporalPattern);
		this.measurementType = measurementType;
		this.analysisType = analysisType;
		this.evaluationType = evaluationType;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType;
		this.description = description;
		this.measurementSetupIds = measurementSetupIds;
		this.measurementSetupIds = new LinkedList();

		this.status = TestStatus._TEST_STATUS_NEW;
		
		this.testDatabase = MeasurementDatabaseContext.testDatabase;

		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * create new instance for client
	 * @param id
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
	 * @return
	 */

	public static Test createInstance(Identifier id,
																		Identifier creatorId,
																		Date startTime,
																		Date endTime,
																		TemporalPattern temporalPattern,
																		TestTemporalType temporalType,
																		MeasurementType measurementType,
																		AnalysisType analysisType,
																		EvaluationType evaluationType,
																		MonitoredElement monitoredElement,
																		TestReturnType returnType,
																		String description,
																		List measurementSetupIds){
		return new Test(id,
										creatorId,
										startTime,
										endTime,
										temporalPattern,
										temporalType.value(),
										measurementType,
										analysisType,
										evaluationType,
										monitoredElement,
										returnType.value(),
										description,
										measurementSetupIds);
		
	}


	public Test(Test_Transferable tt) throws CreateObjectException {
			super(new Identifier(tt.id),
					new Date(tt.created),
					new Date(tt.modified),
					new Identifier(tt.creator_id),
					new Identifier(tt.modifier_id));
		this.temporalType = tt.temporal_type.value();
		this.timeStamps = new TestTimeStamps(tt.time_stamps);
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		try {
			this.measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(tt.measurement_type_id), true);
			/**
			 * @todo when change DB Identifier model ,change identifier_string to
			 *       identifier_code
			 */
			this.analysisType = (tt.analysis_type_id.identifier_string.length() != 0) ? (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier(tt.analysis_type_id), true) : null;
			/**
			 * @todo when change DB Identifier model ,change identifier_string to
			 *       identifier_code
			 */
			this.evaluationType = (tt.evaluation_type_id.identifier_string.length() != 0)	? (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(tt.evaluation_type_id), true) : null;
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	
		this.status = tt.status.value();
	
		try {
			this.monitoredElement = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(new Identifier(tt.monitored_element_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	
		this.returnType = tt.return_type.value();
		this.description = new String(tt.description);
		this.measurementSetupIds = new ArrayList(tt.measurement_setup_ids.length);
		for (int i = 0; i < tt.measurement_setup_ids.length; i++)
			this.measurementSetupIds.add(new Identifier(tt.measurement_setup_ids[i]));
	
		try {
			this.mainMeasurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject((Identifier)this.measurementSetupIds.get(0), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	
	}
	
	public static Test getInstance(Test_Transferable tt) throws CreateObjectException {
		Test test = new Test(tt);
		
		test.testDatabase = MeasurementDatabaseContext.testDatabase;		
		try {
			if (test.testDatabase != null)
				test.testDatabase.insert(test);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		
		return test;
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

	public EvaluationType getEvaluationType() {
		return this.evaluationType;
	}

	public List getMeasurementSetupIds() {
		return this.measurementSetupIds;
	}

	public MeasurementType getMeasurementType() {
		return this.measurementType;
	}

	public MonitoredElement getMonitoredElement() {
		return this.monitoredElement;
	}

	public TemporalPattern getTemporalPattern() {
		return this.timeStamps.temporalPattern;
	}

	public TestReturnType getReturnType() {
		return TestReturnType.from_int(this.returnType);
	}

	public Date getStartTime() {
		return this.timeStamps.startTime;
	}

	public TestStatus getStatus() {
		return TestStatus.from_int(this.status);
	}

	public TestTemporalType getTemporalType() {
		return TestTemporalType.from_int(this.temporalType);
	}

	public Object getTransferable() {
		Identifier_Transferable[] msIds = new Identifier_Transferable[this.measurementSetupIds.size()];
		int i = 0;
		for (Iterator iterator = this.measurementSetupIds.iterator(); iterator.hasNext();)
			msIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();
		
		return new Test_Transferable((Identifier_Transferable)this.id.getTransferable(),
																 super.created.getTime(),
																 super.modified.getTime(),
																 (Identifier_Transferable)super.creatorId.getTransferable(),
																 (Identifier_Transferable)super.modifierId.getTransferable(),
																 TestTemporalType.from_int(this.temporalType),
																 this.timeStamps.getTransferable(),
																 (Identifier_Transferable)this.measurementType.getId().getTransferable(),
																 (this.analysisType != null) ? (Identifier_Transferable)this.analysisType.getId().getTransferable() : (new Identifier_Transferable("")),
																 (this.evaluationType != null) ? (Identifier_Transferable)this.evaluationType.getId().getTransferable() : (new Identifier_Transferable("")),
																 TestStatus.from_int(this.status),
																 (Identifier_Transferable)this.monitoredElement.getId().getTransferable(),
																 TestReturnType.from_int(this.returnType),
																 new String(this.description),
																 msIds);
	}

	public List retrieveMeasurementsOrderByStartTime(MeasurementStatus measurementStatus)	throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (List)this.testDatabase.retrieveObject(this, RETRIEVE_MEASUREMENTS, measurementStatus);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
	
	public Measurement retrieveLastMeasurement() throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (Measurement)this.testDatabase.retrieveObject(this, RETRIEVE_LAST_MEASUREMENT, null);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public int retrieveNumberOfMeasurements() throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return ((Integer)this.testDatabase.retrieveObject(this, RETRIEVE_NUMBER_OF_MEASUREMENTS, null)).intValue();
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public int retrieveNumberOfResults(ResultSort resultSort) throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return ((Integer)this.testDatabase.retrieveObject(this, RETRIEVE_NUMBER_OF_RESULTS, resultSort)).intValue();
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	/**
	 * @param analysisTypeId The analysisTypeId to set.
	 */
	public void setAnalysisType(AnalysisType analysisType) {
		this.currentVersion = super.getNextVersion();
		this.analysisType = analysisType;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.currentVersion = super.getNextVersion();
		this.description = description;
	}
	/**
	 * @param evaluationTypeId The evaluationTypeId to set.
	 */
	public void setEvaluationTypeId(EvaluationType evaluationType) {
		this.currentVersion = super.getNextVersion();
		this.evaluationType = evaluationType;
	}
//	/**
//	 * @param measurementSetupIds The measurementSetupIds to set.
//	 */
//	public void setMeasurementSetupIds(List measurementSetupIds) {
//		this.currentVersion = super.getNextVersion();
//		this.measurementSetupIds = measurementSetupIds;
//	}
	/**
	 * @param measurementTypeId The measurementTypeId to set.
	 */
	public void setMeasurementType(MeasurementType measurementType) {
		this.currentVersion = super.getNextVersion();
		this.measurementType = measurementType;
	}
	/**
	 * @param monitoredElement The monitoredElement to set.
	 */
	public void setMonitoredElement(MonitoredElement monitoredElement) {
		this.currentVersion = super.getNextVersion();
		this.monitoredElement = monitoredElement;
	}
	/**
	 * @param returnType The returnType to set.
	 */
	public void setReturnType(TestReturnType returnType) {
		this.currentVersion = super.getNextVersion();
		this.returnType = returnType.value();
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(TestStatus status) {
		this.currentVersion = super.getNextVersion();
		this.status = status.value();
	}

	/*
	 * public ArrayList getMeasurements() { return this.measurements; }
	 */

	public void updateStatus(TestStatus status, Identifier modifierId) throws UpdateObjectException {
		this.status = status.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = (Identifier) modifierId.clone();
		try {
			this.testDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
	}
	/**
	 * @param temporalPatternId The temporalPatternId to set.
	 */
	public void setTemporalPattern(TemporalPattern temporalPattern) {
		this.currentVersion = super.getNextVersion();
		this.timeStamps.temporalPattern = temporalPattern;
	}
	/**
	 * @param temporalType The temporalType to set.
	 */
	public void setTemporalType(TestTemporalType temporalType) {
		this.currentVersion = super.getNextVersion();
		this.temporalType = temporalType.value();
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						int temporalType,
																						Date startTime,
																						Date endTime,
																						TemporalPattern temporalPattern,
																						MeasurementType measurementType,
																						AnalysisType analysisType,
																						EvaluationType evaluationType,
																						int status,
																						MonitoredElement monitoredElement,
																						int returnType,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.temporalType = temporalType;
		this.timeStamps = new TestTimeStamps(temporalType,
																				 startTime,
																				 endTime,
																				 temporalPattern);

		this.measurementType = measurementType;
		this.analysisType = analysisType;
		this.evaluationType = evaluationType;
		this.status = status;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType;
		this.description = description;
	}

	protected synchronized void setMeasurementSetupIds(List measurementSetupIds) {
		this.measurementSetupIds = measurementSetupIds;

		try {
			this.mainMeasurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject((Identifier)this.measurementSetupIds.get(0), true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}
	
	
	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.created);
		hashCodeGenerator.addObject(this.creatorId);
		hashCodeGenerator.addObject(this.modified);
		hashCodeGenerator.addObject(this.modifierId);
		hashCodeGenerator.addInt(this.timeStamps.hashCode());
		hashCodeGenerator.addObject(this.analysisType);
		hashCodeGenerator.addObject(this.evaluationType);
		hashCodeGenerator.addObject(this.measurementType);
		hashCodeGenerator.addObject(this.monitoredElement);
		hashCodeGenerator.addObject(this.mainMeasurementSetup);
		hashCodeGenerator.addObjectArray(this.measurementSetupIds.toArray());
		hashCodeGenerator.addInt(this.returnType);
		hashCodeGenerator.addInt(this.status);		
		hashCodeGenerator.addObject(this.description);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;
	}
	
	public boolean equals(Object obj) {
		boolean equals = (this == obj);		
		if ((!equals) && (obj instanceof Test)){
			Test test = (Test)obj;
			if (	(test.id.equals(this.id)) &&
					HashCodeGenerator.equalsDate(this.created,test.created) &&
					(this.creatorId.equals(test.creatorId))&&
					HashCodeGenerator.equalsDate(this.modified,test.modified) &&
					(this.modifierId.equals(test.modifierId))&&					
					( ((test.getStartTime() == null) && (getStartTime() == null) ) 
							|| (Math.abs(test.getStartTime().getTime()-getStartTime().getTime())<1000) ) &&
					( ((test.getEndTime() == null) && (getEndTime() == null) ) 
							|| (Math.abs(test.getEndTime().getTime()-getEndTime().getTime())<1000) ) &&
					(test.getTemporalType().equals(getTemporalType())) &&
					( ((test.getTemporalPattern()==null) && (getTemporalPattern() == null)) 
							|| (test.getTemporalPattern().equals(getTemporalPattern())) ) &&
					( ((test.getAnalysisType()==null) && (getAnalysisType() == null)) 
								|| (test.getAnalysisType().equals(getAnalysisType())) ) &&
					( ((test.getEvaluationType()==null) && (getEvaluationType() == null)) 
								|| (test.getEvaluationType().equals(getEvaluationType())) ) &&
					(test.getMeasurementType().equals(getMeasurementType())) &&
					(test.getMonitoredElement().equals(getMonitoredElement())) &&
					( ((test.getMeasurementSetupIds()==null) && (getMeasurementSetupIds() == null)) 
							|| (test.getMeasurementSetupIds().equals(getMeasurementSetupIds())) ) &&
					(test.getReturnType().equals(getReturnType())) && 
					(test.getStatus().equals(test.getStatus()))
					)
					equals = true;
		}
		return equals;
	}

	private class TestTimeStamps {
		Date endTime;
		Date startTime;
		TemporalPattern temporalPattern;

		private int	discriminator;		

		TestTimeStamps(int temporalType,
									 Date startTime,
									 Date endTime,
									 TemporalPattern temporalPattern) {
			this.discriminator = temporalType;
			switch (temporalType) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					this.startTime = startTime;
					this.endTime = null;
					this.temporalPattern = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					this.startTime = startTime;
					this.endTime = endTime;
					this.temporalPattern = temporalPattern;
					if (this.endTime == null) {
						Log.errorMessage("ERROR: End time is NULL");
						this.endTime = this.startTime;
					}
					if (this.temporalPattern == null)
						Log.errorMessage("ERROR: Temporal pattern is NULL");
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					this.startTime = startTime;
					this.endTime = endTime;
					this.temporalPattern = null;
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
					this.temporalPattern = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					PeriodicalTestTimeStamps ptts = ttst.ptts();
					this.startTime = new Date(ptts.start_time);
					this.endTime = new Date(ptts.end_time);
					try {
						this.temporalPattern = (TemporalPattern) MeasurementStorableObjectPool.getStorableObject(new Identifier(ptts.temporal_pattern_id), true);
					}
					catch (ApplicationException ae) {
						throw new CreateObjectException(ae);
					}
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					ContinuousTestTimeStamps ctts = ttst.ctts();
					this.startTime = new Date(ctts.start_time);
					this.endTime = new Date(ctts.end_time);
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
																								 (this.temporalPattern != null) ? (Identifier_Transferable)this.temporalPattern.getId().getTransferable() : (new Identifier_Transferable("")) ));
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
			hashCodeGenerator.addObject(this.temporalPattern);
			int result = hashCodeGenerator.getResult(); 
			return result;
		}
		
		public boolean equals(Object obj) {
			boolean equals = (this==obj);
			if ((!equals)&&(obj instanceof TestTimeStamps)){
				TestTimeStamps stamps = (TestTimeStamps)obj;
				if ((stamps.discriminator==this.discriminator)&&
					(stamps.startTime==this.startTime)&&	
					(stamps.endTime==this.endTime)&&
					(stamps.temporalPattern.equals(this.temporalPattern)))
					equals = true;
			}
			return equals;
		}
	}

}
