/*
 * $Id: Test.java,v 1.32 2004/08/17 09:04:29 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.util.HashCodeGenerator;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.ContinuousTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.PeriodicalTestTimeStamps;

/**
 * @version $Revision: 1.32 $, $Date: 2004/08/17 09:04:29 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Test extends StorableObject {

	private class TestTimeStamps {
		Date endTime;
		Date startTime;
		Identifier temporalPatternId;

		private int	discriminator;
		
		//private HashCodeGenerator hashCodeGenerator;

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
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					this.startTime = startTime;
					this.endTime = endTime;
					this.temporalPatternId = null;
					break;
				default:
					Log.errorMessage("TestTimeStamps | Illegal temporal type: " + temporalType + " of test");
			}
		}

		TestTimeStamps(TestTimeStamps_Transferable ttst) {
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
																								 (Identifier_Transferable)this.temporalPatternId.getTransferable()));
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
			if (Test.this.hashCodeGenerator==null)
				Test.this.hashCodeGenerator = new HashCodeGenerator();
			else Test.this.hashCodeGenerator.clear();
			Test.this.hashCodeGenerator.addInt(this.discriminator);
			Test.this.hashCodeGenerator.addObject(this.startTime);
			Test.this.hashCodeGenerator.addObject(this.endTime);
			Test.this.hashCodeGenerator.addObject(this.temporalPatternId);
			return Test.this.hashCodeGenerator.getResult();
		}
	}

	protected static final int		RETRIEVE_MEASUREMENTS	= 1;
	protected static final int		RETRIEVE_LAST_MEASUREMENT	= 2;
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
	HashCodeGenerator hashCodeGenerator;

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
		this.measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(tt.measurement_type_id), true);
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		this.analysisType = (tt.analysis_type_id.identifier_string != "") ? (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier(tt.analysis_type_id), true) : null;
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		this.evaluationType = (tt.evaluation_type_id.identifier_string != "")	? (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(tt.evaluation_type_id), true) : null;
		this.status = tt.status.value();
		this.monitoredElement = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(new Identifier(tt.monitored_element_id), true);
		this.returnType = tt.return_type.value();
		this.description = new String(tt.description);
		this.measurementSetupIds = new ArrayList(tt.measurement_setup_ids.length);
		for (int i = 0; i < tt.measurement_setup_ids.length; i++)
			this.measurementSetupIds.add(new Identifier(tt.measurement_setup_ids[i]));

		this.mainMeasurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject((Identifier)this.measurementSetupIds.get(0), true);

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
		try {
			this.testDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Measurement createMeasurement(Identifier measurementId,
																			 Identifier creatorId,
																			 Date startTime) throws CreateObjectException {
		Measurement measurement = Measurement.createInstance(measurementId,
																												 creatorId,
																												 this.measurementType,
																												 this.monitoredElement.getId(),
																												 this.mainMeasurementSetup,
																												 startTime,
																												 this.monitoredElement.getLocalAddress(),
																												 this.id);
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
	
	private Test(Identifier id,
							 Identifier creatorId,
							 Date startTime,
							 Date endTime,
							 Identifier temporalPatternId,
							 TestTemporalType temporalType,
							 MeasurementType measurementType,
							 AnalysisType analysisType,
							 EvaluationType evaluationType,
							 MonitoredElement monitoredElement,
							 TestReturnType returnType,
							 String description,
							 List measurementSetupIds){
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.temporalType = temporalType.value();
		this.timeStamps = new TestTimeStamps(this.temporalType,
																				 startTime,
																				 endTime,
																				 temporalPatternId);
		this.measurementType = measurementType;
		this.analysisType = analysisType;
		this.evaluationType = evaluationType;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType.value();
		this.description = description;
		this.measurementSetupIds = measurementSetupIds;

		this.status = TestStatus._TEST_STATUS_SCHEDULED;

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
																		Identifier temporalPatternId,
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
										temporalPatternId,
										temporalType,
										measurementType,
										analysisType,
										evaluationType,
										monitoredElement,
										returnType,
										description,
										measurementSetupIds);
		
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

	public Identifier getTemporalPatternId() {
		return this.timeStamps.temporalPatternId;
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
	public void setTemporalPatternId(Identifier temporalPatternId) {
		this.currentVersion = super.getNextVersion();
		this.timeStamps.temporalPatternId = temporalPatternId;
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
																						Identifier temporalPatternId,
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
																				 temporalPatternId);

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

		this.mainMeasurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject((Identifier)this.measurementSetupIds.get(0), true);
	}
	
	
	public int hashCode() {
		if (this.hashCodeGenerator==null)
			this.hashCodeGenerator = new HashCodeGenerator();
		else this.hashCodeGenerator.clear();
		this.hashCodeGenerator.addObject(this.id);
		this.hashCodeGenerator.addInt(this.timeStamps.hashCode());
		this.hashCodeGenerator.addObject(this.analysisType);
		this.hashCodeGenerator.addObject(this.evaluationType);
		this.hashCodeGenerator.addObject(this.measurementType);
		this.hashCodeGenerator.addObject(this.monitoredElement);
		this.hashCodeGenerator.addObject(this.mainMeasurementSetup);

		this.hashCodeGenerator.addObjectArray(this.measurementSetupIds.toArray());
		this.hashCodeGenerator.addInt(this.returnType);
		this.hashCodeGenerator.addInt(this.status);		
		this.hashCodeGenerator.addObject(this.description);
		return this.hashCodeGenerator.getResult();
	}
	
	public boolean equals(Object obj) {
		boolean equals = (this == obj);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		if ((!equals) && (obj instanceof Test)){
			Test test = (Test)obj;
			System.out.println(sdf.format(test.getStartTime()));
			System.out.println(sdf.format(getStartTime()));
			if (	(test.getId().equals(getId())) &&
					( ((test.getStartTime() == null) && (getStartTime() == null) ) 
							|| (test.getStartTime().getTime()==getStartTime().getTime()) ) &&
					( ((test.getEndTime() == null) && (getEndTime() == null) ) 
							|| (test.getEndTime().getTime()==getEndTime().getTime()) ) &&
					(test.getTemporalType().equals(getTemporalType())) &&
					( ((test.getTemporalPatternId()==null) && (getTemporalPatternId() == null)) 
							|| (test.getTemporalPatternId().equals(getTemporalPatternId())) ) &&
					( ((test.getAnalysisType()==null) && (getAnalysisType() == null)) 
								|| (test.getAnalysisType().equals(getAnalysisType())) ) &&
					( ((test.getEvaluationType()==null) && (getEvaluationType() == null)) 
								|| (test.getEvaluationType().equals(getEvaluationType())) ) &&
					(test.getMeasurementType().equals(getMeasurementType())) &&
					(test.getMonitoredElement().equals(getMonitoredElement())) &&
					(test.getMeasurementSetupIds().equals(getMeasurementSetupIds())) &&
					(test.getReturnType().equals(getReturnType())) && 
					(test.getStatus().equals(test.getStatus()))
					)
					equals = true;
		}
		return equals;
	}
}
