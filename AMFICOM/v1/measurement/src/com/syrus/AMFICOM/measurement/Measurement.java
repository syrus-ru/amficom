package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
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

	private StorableObject_Database measurementDatabase;

	public Measurement(Identifier id) throws RetrieveObjectException {
		super(id);

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Measurement(Measurement_Transferable mt) throws CreateObjectException {
		super(new Identifier(mt.id),
					new Date(mt.created),
					new Date(mt.modified),
					new Identifier(mt.creator_id),
					new Identifier(mt.modifier_id),
					new Identifier(mt.type_id),
					new Identifier(mt.monitored_element_id));
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

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Measurement(Identifier id,
											Identifier creator_id,
											Identifier type_id,
											Identifier monitored_element_id,
											MeasurementSetup setup,
											Date start_time,
											String local_address,
											Identifier test_id) throws CreateObjectException {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creator_id,
					creator_id,
					type_id,
					monitored_element_id);
		this.setup = setup;
		this.start_time = start_time;
		this.duration = this.setup.getMeasurementDuration();
		this.status = MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED;
		this.local_address = local_address;
		this.test_id = test_id;

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Measurement_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																				super.created.getTime(),
																				super.modified.getTime(),
																				(Identifier_Transferable)super.creator_id.getTransferable(),
																				(Identifier_Transferable)super.modifier_id.getTransferable(),
																				(Identifier_Transferable)super.type_id.getTransferable(),
																				(Identifier_Transferable)super.monitored_element_id.getTransferable(),
																				(Identifier_Transferable)this.setup.getId().getTransferable(),
																				this.start_time.getTime(),
																				this.duration,
																				MeasurementStatus.from_int(this.status),
																				new String(this.local_address),
																				(Identifier_Transferable)this.test_id.getTransferable());
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

	public synchronized void setStatus(MeasurementStatus status, Identifier modifier_id) throws UpdateObjectException {
		this.status = status.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifier_id = (Identifier)modifier_id.clone();
		try {
			this.measurementDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (Exception e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier type_id,
																						Identifier monitored_element_id,
																						MeasurementSetup setup,
																						Date start_time,
																						long duration,
																						int status,
																						String local_address,
																						Identifier test_id) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												type_id,
												monitored_element_id);
		this.setup = setup;
		this.start_time = start_time;
		this.duration = duration;
		this.status = status;
		this.local_address = local_address;
		this.test_id = test_id;
	}

	protected static Measurement create(Identifier id,
																			Identifier creator_id,
																			Identifier type_id,
																			Identifier monitored_element_id,
																			MeasurementSetup setup,
																			Date start_time,
																			String local_address,
																			Identifier test_id) throws CreateObjectException {
		return new Measurement(id,
													 creator_id,
													 type_id,
													 monitored_element_id,
													 setup,
													 start_time,
													 local_address,
													 test_id);
	}

	public Result createResult(Identifier id,
														 Identifier creator_id,
														 Measurement measurement,
														 AlarmLevel alarm_level,
														 Identifier[] parameter_ids,
														 Identifier[] parameter_type_ids,
														 byte[][] parameter_values) throws CreateObjectException {
		return Result.create(id,
												 creator_id,
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