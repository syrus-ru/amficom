package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.util.Log;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.PeriodicalTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.KIS;

public class Test extends StorableObject {
	protected static final int RETRIEVE_MEASUREMENTS = 1;
	protected static final int UPDATE_STATUS = 1;
	protected static final int UPDATE_MODIFIED = 2;

	private int temporal_type;
	private TestTimeStamps timeStamps;
	private Identifier measurement_type_id;
	private Identifier analysis_type_id;
	private Identifier evaluation_type_id;
	private int status;
	private MonitoredElement monitoredElement;
	private int return_type;
	private String description;
	private ArrayList measurement_setup_ids;

	private MeasurementSetup mainMeasurementSetup;
	private KIS kis;

	private StorableObject_Database testDatabase;

	public Test(Identifier id) throws RetrieveObjectException {
		super(id);

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
		try {
			this.testDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Test(Test_Transferable tt) throws CreateObjectException {
		super(new Identifier(tt.id),
					new Date(tt.created),
					new Date(tt.modified),
					new Identifier(tt.creator_id),
					new Identifier(tt.modifier_id));
		this.temporal_type = tt.temporal_type.value();
		this.timeStamps = new TestTimeStamps(tt.time_stamps);
		this.measurement_type_id = new Identifier(tt.measurement_type_id);
		this.analysis_type_id = (tt.analysis_type_id.identifier_code == 0)?(new Identifier(tt.analysis_type_id)):null;
		this.evaluation_type_id = (tt.evaluation_type_id.identifier_code == 0)?(new Identifier(tt.evaluation_type_id)):null;
		this.status = tt.status.value();
		try {
			this.monitoredElement = new MonitoredElement(new Identifier(tt.monitored_element_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		this.return_type = tt.return_type.value();
		this.description = new String(tt.description);
		this.measurement_setup_ids = new ArrayList(tt.measurement_setup_ids.length);
		for (int i = 0; i < tt.measurement_setup_ids.length; i++)
			this.measurement_setup_ids.add(new Identifier(tt.measurement_setup_ids[i]));

		try {
			this.mainMeasurementSetup = new MeasurementSetup((Identifier)this.measurement_setup_ids.get(0));
			this.kis = new KIS(this.monitoredElement.getKISId());
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}

		this.testDatabase = MeasurementDatabaseContext.testDatabase;
		try {
			this.testDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		Identifier_Transferable[] ms_ids = new Identifier_Transferable[this.measurement_setup_ids.size()];
		int i = 0;
		for (Iterator iterator = this.measurement_setup_ids.iterator(); iterator.hasNext();)
			ms_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();
		return new Test_Transferable((Identifier_Transferable)this.id.getTransferable(),
																 super.created.getTime(),
																 super.modified.getTime(),
																 (Identifier_Transferable)super.creator_id.getTransferable(),
																 (Identifier_Transferable)super.modifier_id.getTransferable(),
																 TestTemporalType.from_int(this.temporal_type),
																 this.timeStamps.getTransferable(),
																 (Identifier_Transferable)this.measurement_type_id.getTransferable(),
																 (this.analysis_type_id != null)?(Identifier_Transferable)this.analysis_type_id.getTransferable():new Identifier_Transferable(0),
																 (this.evaluation_type_id != null)?(Identifier_Transferable)this.evaluation_type_id.getTransferable():new Identifier_Transferable(0),
																 TestStatus.from_int(this.status),
																 (Identifier_Transferable)this.monitoredElement.getId().getTransferable(),
																 TestReturnType.from_int(this.return_type),
																 new String(this.description),
																 ms_ids);
	}

	public TestTemporalType getTemporalType() {
		return TestTemporalType.from_int(this.temporal_type);
	}

	public Date getStartTime() {
		return this.timeStamps.start_time;
	}

	public Date getEndTime() {
		return this.timeStamps.end_time;
	}

	public Identifier getPTTemplateId() {
		return this.timeStamps.pt_template_id;
	}

	public Date[] getTTTimestamps() {
		return this.timeStamps.tt_timestamps;
	}

	public Identifier getMeasurementTypeId() {
		return this.measurement_type_id;
	}

	public Identifier getAnalysisTypeId() {
		return this.analysis_type_id;
	}

	public Identifier getEvaluationTypeId() {
		return this.evaluation_type_id;
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
		return TestReturnType.from_int(this.return_type);
	}

	public String getDescription() {
		return this.description;
	}

	public ArrayList getMeasurementSetupIds() {
		return this.measurement_setup_ids;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						int temporal_type,
																						Date start_time,
																						Date end_time,
																						Identifier pt_template_id,
																						Date[] tt_timestamps,
																						Identifier measurement_type_id,
																						Identifier analysis_type_id,
																						Identifier evaluation_type_id,
																						int status,
																						MonitoredElement monitoredElement,
																						int return_type,
																						String description) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.temporal_type = temporal_type;
		this.timeStamps = new TestTimeStamps(temporal_type, start_time, end_time, pt_template_id, tt_timestamps);
		this.measurement_type_id = measurement_type_id;
		this.analysis_type_id = analysis_type_id;
		this.evaluation_type_id = evaluation_type_id;
		this.status = status;
		this.monitoredElement = monitoredElement;
		this.return_type = return_type;
		this.description = description;

		try {
			this.kis = new KIS(this.monitoredElement.getKISId());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
	}

	protected synchronized void setMeasurementSetupIds(ArrayList measurement_setup_ids) {
		this.measurement_setup_ids = measurement_setup_ids;

		try {
			this.mainMeasurementSetup = new MeasurementSetup((Identifier)this.measurement_setup_ids.get(0));
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
	}

	/*	!!
	 * Test(ClientTest_Transferable) */

/*
	public ArrayList getMeasurements() {
		return this.measurements;
	}*/

	public void setStatus(TestStatus status, Identifier modifier_id) throws UpdateObjectException {
		this.status = status.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifier_id = (Identifier)modifier_id.clone();
		try {
			this.testDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (Exception e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
	}

	public Measurement createMeasurement(Identifier measurement_id,
																			 Date start_time,
																			 Identifier creator_id) throws CreateObjectException {
			Measurement measurement = Measurement.create(measurement_id,
																									 creator_id,
																									 this.measurement_type_id,
																									 this.monitoredElement.getId(),
																									 this.mainMeasurementSetup,
																									 start_time,
																									 this.monitoredElement.getLocalAddress(),
																									 this.id);
			super.modified = new Date(System.currentTimeMillis());
			super.modifier_id = (Identifier)creator_id.clone();
			try {
				this.testDatabase.update(this, UPDATE_MODIFIED, null);
			}
			catch (Exception e) {
				throw new CreateObjectException(e.getMessage(), e);
			}
			return measurement;
		}

	public ArrayList retrieveMeasurementsOrderByStartTime(MeasurementStatus measurementStatus) throws RetrieveObjectException {
		try {
			return (ArrayList)this.testDatabase.retrieveObject(this, RETRIEVE_MEASUREMENTS, measurementStatus);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	private class TestTimeStamps {
		private int discriminator;
		private Date start_time;
		private Date end_time;
		private Identifier pt_template_id;
		private Date[] tt_timestamps;

		TestTimeStamps(TestTimeStamps_Transferable ttst) {
			this.discriminator = ttst.discriminator().value();
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					this.start_time = new Date(ttst.start_time());
					this.end_time = null;
					this.pt_template_id = null;
					this.tt_timestamps = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					PeriodicalTestTimeStamps ptts = ttst.ptts();
					this.start_time = new Date(ptts.start_time);
					this.end_time = new Date(ptts.end_time);
					this.pt_template_id = new Identifier(ptts.template_id);
					this.tt_timestamps = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
					long[] ti = ttst.ti();
					this.tt_timestamps = new Date[ti.length];
					for (int i = 0; i < this.tt_timestamps.length; i++)
						this.tt_timestamps[i] = new Date(ti[i]);
					this.start_time = null;
					this.end_time = null;
					this.pt_template_id = null;
			}
		}

		TestTimeStamps(int temporal_type, Date start_time, Date end_time, Identifier pt_template_id, Date[] tt_timestamps) {
			this.discriminator = temporal_type;
			switch (temporal_type) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					this.start_time = start_time;
					this.end_time = null;
					this.pt_template_id = null;
					this.tt_timestamps = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					this.start_time = start_time;
					this.end_time = end_time;
					this.pt_template_id = pt_template_id;
					this.tt_timestamps = null;
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
					this.tt_timestamps = tt_timestamps;
					this.start_time = null;
					this.end_time = null;
					this.pt_template_id = null;
			}
		}

		TestTimeStamps_Transferable getTransferable() {
			TestTimeStamps_Transferable ttst = new TestTimeStamps_Transferable();
			switch (this.discriminator) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					ttst.start_time(this.start_time.getTime());
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					ttst.ptts(new PeriodicalTestTimeStamps(this.start_time.getTime(),
																								 this.end_time.getTime(),
																								 (Identifier_Transferable)this.pt_template_id.getTransferable()));
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
					long[] ti = new long[this.tt_timestamps.length];
					for (int i = 0; i < ti.length; i++)
						ti[i] = this.tt_timestamps[i].getTime();
					ttst.ti(ti);
					break;
			}
			return ttst;
		}
	}
}