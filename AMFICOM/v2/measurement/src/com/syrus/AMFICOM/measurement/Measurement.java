package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

public class Measurement extends Action {
	public static final long DEFAULT_MEASUREMENT_DURATION = 3*60*1000;//milliseconds
	protected static final int RETRIEVE_RESULT = 1;
	protected static final int UPDATE_STATUS = 1;

	private MeasurementSetup setup;
	private Date start_time;
	private long duration;
	private int status;
	private String local_address;
	private Identifier test_id;
	private Date modified;

	private StorableObject_Database measurementDatabase;

	public Measurement(Identifier id) throws RetrieveObjectException {
		super(id);

		this.measurementDatabase = StorableObject_DatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Measurement(Measurement_Transferable mt) throws CreateObjectException {
		super(new Identifier(mt.id), new Identifier(mt.type_id), new Identifier(mt.monitored_element_id));
		try {
			this.setup = new MeasurementSetup(new Identifier(mt.setup_id));
		}
		catch (RetrieveObjectException roe) {
			throw new CreateObjectException(roe.getMessage(), roe);
		}
		this.start_time = new Date(mt.start_time);
		this.duration = mt.duration;
		this.status = mt.status.value();
		this.local_address = new String(mt.local_address);
		this.test_id = new Identifier(mt.test_id);
		this.modified = new Date(mt.modified);

		this.measurementDatabase = StorableObject_DatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	protected Measurement(Identifier id,
												Identifier type_id,
												Identifier monitored_element_id,
												MeasurementSetup setup,
												Date start_time,
												String local_address,
												Identifier test_id) throws CreateObjectException {
		super(id, type_id, monitored_element_id);
		this.setup = setup;
		this.start_time = start_time;
		this.duration = this.setup.getMeasurementDuration();
		this.status = MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED;
		this.local_address = local_address;
		this.test_id = test_id;
		this.modified = new Date(System.currentTimeMillis());

		this.measurementDatabase = StorableObject_DatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Measurement_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																				(Identifier_Transferable)super.type_id.getTransferable(),
																				(Identifier_Transferable)this.setup.getId().getTransferable(),
																				this.start_time.getTime(),
																				this.duration,
																				MeasurementStatus.from_int(this.status),
																				new String(this.local_address),
																				(Identifier_Transferable)this.monitored_element_id.getTransferable(),
																				(Identifier_Transferable)this.test_id.getTransferable(),
																				this.modified.getTime());
	}

	public MeasurementSetup getSetup() {
		return this.setup;
	}

	public Date getStartTime() {
		return this.start_time;
	}

	public long getDuration() {
		return this.duration;
	}

	public MeasurementStatus getStatus() {
		return MeasurementStatus.from_int(this.status);
	}

	public String getLocalAddress() {
		return this.local_address;
	}

	public Identifier getTestId() {
		return this.test_id;
	}

	public Date getModified() {
		return this.modified;
	}

	public synchronized void setStatus(MeasurementStatus status) throws UpdateObjectException {
		this.status = status.value();
		this.modified = new Date(System.currentTimeMillis());
		try {
			this.measurementDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (Exception e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
	}

	protected synchronized void setAttributes(Identifier type_id,
																						MeasurementSetup setup,
																						Date start_time,
																						long duration,
																						int status,
																						String local_address,
																						Identifier monitored_element_id,
																						Identifier test_id,
																						Date modified) {
		super.type_id = type_id;
		this.setup = setup;
		this.start_time = start_time;
		this.duration = duration;
		this.status = status;
		this.local_address = local_address;
		super.monitored_element_id = monitored_element_id;
		this.test_id = test_id;
		this.modified = modified;
	}

	public Result createResult(Identifier id,
														 Measurement measurement,
														 AlarmLevel alarm_level,
														 Identifier[] parameter_ids,
														 Identifier[] parameter_type_ids,
														 byte[][] parameter_values) throws CreateObjectException {
		return new Result(id,
											this,
											this,
											ResultSort.RESULT_SORT_MEASUREMENT,
											alarm_level,
											parameter_ids,
											parameter_type_ids,
											parameter_values);						
	}

	public Result retrieveResult(ResultSort result_sort) throws RetrieveObjectException {
		try {
			return (Result)this.measurementDatabase.retrieveObject(this, RETRIEVE_RESULT, result_sort);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
}