package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.ContinuousTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.PeriodicalTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.KIS;

public class Test extends StorableObject {

	protected static final int		RETRIEVE_MEASUREMENTS	= 1;
	protected static final int		UPDATE_STATUS			= 1;
	protected static final int		UPDATE_MODIFIED			= 2;

	private int						temporalType;
	private TestTimeStamps			timeStamps;
	private Identifier				temporalPatternId;
	private Identifier				measurementTypeId;
	private Identifier				analysisTypeId;
	private Identifier				evaluationTypeId;
	private int						status;
	private int						returnType;
	private String					description;
	private MonitoredElement		monitoredElement;
	private ArrayList				measurementSetupIds;

	private MeasurementSetup		mainMeasurementSetup;
	private KIS						kis;

	private StorableObjectDatabase	testDatabase;

	public Test(Identifier id) throws RetrieveObjectException {
		super(id);

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
		try {
			this.testDatabase.retrieve(this);
		} catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Test(Test_Transferable tt) throws CreateObjectException {
		super(new Identifier(tt.id), new Date(tt.created), new Date(tt.modified), new Identifier(tt.creator_id),
				new Identifier(tt.modifier_id));
		this.temporalType = tt.temporal_type.value();
		this.timeStamps = new TestTimeStamps(tt.time_stamps);
		this.measurementTypeId = new Identifier(tt.measurement_type_id);
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		this.analysisTypeId = (tt.analysis_type_id.identifier_string != null) ? (new Identifier(tt.analysis_type_id))
				: null;
		/**
		 * @todo when change DB Identifier model ,change identifier_string to
		 *       identifier_code
		 */
		this.evaluationTypeId = (tt.evaluation_type_id.identifier_string != null)	? (new Identifier(tt.evaluation_type_id)) : null;
		this.status = tt.status.value();
		try {
			this.monitoredElement = new MonitoredElement(new Identifier(tt.monitored_element_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		catch (ObjectNotFoundException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		this.returnType = tt.return_type.value();
		this.description = new String(tt.description);
		this.measurementSetupIds = new ArrayList(tt.measurement_setup_ids.length);
		for (int i = 0; i < tt.measurement_setup_ids.length; i++)
			this.measurementSetupIds.add(new Identifier(tt.measurement_setup_ids[i]));

		try {
			this.mainMeasurementSetup = new MeasurementSetup((Identifier) this.measurementSetupIds.get(0));
			this.kis = new KIS(this.monitoredElement.getKISId());
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		catch (ObjectNotFoundException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
		try {
			this.testDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
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
																 (Identifier_Transferable)this.temporalPatternId.getTransferable(),
																 (Identifier_Transferable)this.measurementTypeId.getTransferable(),
																 (this.analysisTypeId != null) ? (Identifier_Transferable)this.analysisTypeId.getTransferable() : null,
																 (this.evaluationTypeId != null) ? (Identifier_Transferable)this.evaluationTypeId.getTransferable() : null,
																 TestStatus.from_int(this.status),
																 (Identifier_Transferable)this.monitoredElement.getId().getTransferable(),
																 TestReturnType.from_int(this.returnType),
																 new String(this.description),
																 msIds);
	}

	public TestTemporalType getTemporalType() {
		return TestTemporalType.from_int(this.temporalType);
	}

	public Date getStartTime() {
		return this.timeStamps.startTime;
	}

	public Date getEndTime() {
		return this.timeStamps.endTime;
	}

	public Identifier getPTTemplateId() {
		return this.timeStamps.temporalPatternId;
	}

	public Identifier getMeasurementTypeId() {
		return this.measurementTypeId;
	}

	public Identifier getTemporalPatternId() {
		return this.temporalPatternId;
	}

	public Identifier getAnalysisTypeId() {
		return this.analysisTypeId;
	}

	public Identifier getEvaluationTypeId() {
		return this.evaluationTypeId;
	}

	public TestStatus getStatus() {
		return TestStatus.from_int(this.status);
	}

	public MonitoredElement getMonitoredElement() {
		return this.monitoredElement;
	}

	public KIS getKIS() {
		return this.kis;
	}

	public TestReturnType getReturnType() {
		return TestReturnType.from_int(this.returnType);
	}

	public String getDescription() {
		return this.description;
	}

	public ArrayList getMeasurementSetupIds() {
		return this.measurementSetupIds;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
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
												modifierId);
		this.temporalType = temporalType;
		this.temporalPatternId = temporalPatternId;
		this.timeStamps = new TestTimeStamps(temporalType, startTime, endTime, temporalPatternId);

		this.measurementTypeId = measurementTypeId;
		this.analysisTypeId = analysisTypeId;
		this.evaluationTypeId = evaluationTypeId;
		this.status = status;
		this.monitoredElement = monitoredElement;
		this.returnType = returnType;
		this.description = description;

		try {
			this.kis = new KIS(this.monitoredElement.getKISId());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		catch (ObjectNotFoundException e) {
			Log.errorException(e);
		}
	}

	protected synchronized void setMeasurementSetupIds(ArrayList measurementSetupIds) {
		this.measurementSetupIds = measurementSetupIds;

		try {
			this.mainMeasurementSetup = new MeasurementSetup((Identifier) this.measurementSetupIds.get(0));
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		catch (ObjectNotFoundException e) {
			Log.errorException(e);
		}
	}

	/*
	 * public ArrayList getMeasurements() { return this.measurements; }
	 */

	public void setStatus(TestStatus status, Identifier modifierId) throws UpdateObjectException {
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

	public Measurement createMeasurement(Identifier measurementId,
																			 Date startTime,
																			 Identifier creatorId) throws CreateObjectException {
		Measurement measurement = Measurement.create(measurementId,
																								 creatorId,
																								 this.measurementTypeId,
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

	public List retrieveMeasurementsOrderByStartTime(MeasurementStatus measurementStatus)
			throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (List)this.testDatabase.retrieveObject(this, RETRIEVE_MEASUREMENTS, measurementStatus);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	private class TestTimeStamps {

		private int	discriminator;
		Date		startTime;
		Date		endTime;
		Identifier	temporalPatternId;

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
			}
		}

		TestTimeStamps(int temporalType, Date startTime, Date endTime, Identifier ptTemplateId) {
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
					this.temporalPatternId = ptTemplateId;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					this.startTime = startTime;
					this.endTime = endTime;
					break;
			}
		}

		TestTimeStamps_Transferable getTransferable() {
			TestTimeStamps_Transferable ttst = new TestTimeStamps_Transferable();
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					ttst.start_time(this.startTime.getTime());
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					ttst.ptts(new PeriodicalTestTimeStamps(this.startTime.getTime(), this.endTime.getTime(),
															(Identifier_Transferable) this.temporalPatternId
																	.getTransferable()));
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					ttst.ctts(new ContinuousTestTimeStamps(this.startTime.getTime(), this.endTime.getTime()));
					break;
			}
			return ttst;
		}
	}
}