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
	private Date startTime;
	private long duration;
	private int status;
	private String localAddress;
	private Identifier testId;

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
		this.startTime = new Date(mt.start_time);
		this.duration = mt.duration;
		this.status = mt.status.value();
		this.localAddress = new String(mt.local_address);
		this.testId = new Identifier(mt.test_id);

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Measurement(Identifier id,
											Identifier creatorId,
											Identifier typeId,
											Identifier monitoredElementId,
											MeasurementSetup setup,
											Date startTime,
											String localAddress,
											Identifier testId) throws CreateObjectException {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					typeId,
					monitoredElementId);
		this.setup = setup;
		this.startTime = startTime;
		this.duration = this.setup.getMeasurementDuration();
		this.status = MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED;
		this.localAddress = localAddress;
		this.testId = testId;

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
																				(Identifier_Transferable)super.typeId.getTransferable(),
																				(Identifier_Transferable)super.monitoredElementId.getTransferable(),
																				(Identifier_Transferable)this.setup.getId().getTransferable(),
																				this.startTime.getTime(),
																				this.duration,
																				MeasurementStatus.from_int(this.status),
																				new String(this.localAddress),
																				(Identifier_Transferable)this.testId.getTransferable());
	}

	public MeasurementSetup getSetup() {
		return this.setup;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public long getDuration() {
		return this.duration;
	}

	public MeasurementStatus getStatus() {
		return MeasurementStatus.from_int(this.status);
	}

	public String getLocalAddress() {
		return this.localAddress;
	}

	public Identifier getTestId() {
		return this.testId;
	}

	public synchronized void setStatus(MeasurementStatus status, Identifier modifierId) throws UpdateObjectException {
		this.status = status.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifier_id = (Identifier)modifierId.clone();
		try {
			this.measurementDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (Exception e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier typeId,
																						Identifier monitoredElementId,
																						MeasurementSetup setup,
																						Date startTime,
																						long duration,
																						int status,
																						String localAddress,
																						Identifier testId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												typeId,
												monitoredElementId);
		this.setup = setup;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
		this.localAddress = localAddress;
		this.testId = testId;
	}

	protected static Measurement create(Identifier id,
																			Identifier creatorId,
																			Identifier typeId,
																			Identifier monitoredElementId,
																			MeasurementSetup setup,
																			Date startTime,
																			String localAddress,
																			Identifier testId) throws CreateObjectException {
		return new Measurement(id,
													 creatorId,
													 typeId,
													 monitoredElementId,
													 setup,
													 startTime,
													 localAddress,
													 testId);
	}

	public Result createResult(Identifier id,
														 Identifier creatorId,
														 Measurement measurement,
														 AlarmLevel alarmLevel,
														 Identifier[] parameterIds,
														 Identifier[] parameterTypeIds,
														 byte[][] parameterValues) throws CreateObjectException {
		return Result.create(id,
												 creatorId,
												 this,
												 this,
												 ResultSort.RESULT_SORT_MEASUREMENT,
												 alarmLevel,
												 parameterIds,
												 parameterTypeIds,
												 parameterValues);						
	}

	public Result retrieveResult(ResultSort resultSort) throws RetrieveObjectException {
		try {
			return (Result)this.measurementDatabase.retrieveObject(this, RETRIEVE_RESULT, resultSort);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
}