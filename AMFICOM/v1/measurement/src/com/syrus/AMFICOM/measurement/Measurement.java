/*
 * $Id: Measurement.java,v 1.20 2004/08/14 19:40:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.20 $, $Date: 2004/08/14 19:40:41 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

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

	private StorableObjectDatabase measurementDatabase;

	public Measurement(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Measurement(Measurement_Transferable mt) throws CreateObjectException {
		super(new Identifier(mt.id),
					new Date(mt.created),
					new Date(mt.modified),
					new Identifier(mt.creator_id),
					new Identifier(mt.modifier_id),
					(MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.type_id), true),
					new Identifier(mt.monitored_element_id));
		this.setup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.setup_id), true);
		this.startTime = new Date(mt.start_time);
		this.duration = mt.duration;
		this.status = mt.status.value();
		this.localAddress = new String(mt.local_address);
		this.testId = new Identifier(mt.test_id);

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private Measurement(Identifier id,
											Identifier creatorId,
											MeasurementType type,
											Identifier monitoredElementId,
											MeasurementSetup setup,
											Date startTime,
											String localAddress,
											Identifier testId) throws CreateObjectException {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.type = type;
		super.monitoredElementId = monitoredElementId;

		this.setup = setup;
		this.startTime = startTime;
		this.duration = this.setup.getMeasurementDuration();
		this.status = MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED;
		this.localAddress = localAddress;
		this.testId = testId;

		super.currentVersion = super.getNextVersion();

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			this.measurementDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Measurement_Transferable((Identifier_Transferable)super.id.getTransferable(),
																				super.created.getTime(),
																				super.modified.getTime(),
																				(Identifier_Transferable)super.creatorId.getTransferable(),
																				(Identifier_Transferable)super.modifierId.getTransferable(),
																				(Identifier_Transferable)super.type.getId().getTransferable(),
																				(Identifier_Transferable)super.monitoredElementId.getTransferable(),
																				new String(super.type.getCodename()),
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

	public synchronized void updateStatus(MeasurementStatus status, Identifier modifierId) throws UpdateObjectException {
		this.status = status.value();
		super.modified = new Date(System.currentTimeMillis());
		super.modifierId = (Identifier)modifierId.clone();
		try {
			this.measurementDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						MeasurementType type,
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
												type,
												monitoredElementId);
		this.setup = setup;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
		this.localAddress = localAddress;
		this.testId = testId;
	}

	protected static Measurement createInstance(Identifier id,
																							Identifier creatorId,
																							MeasurementType type,
																							Identifier monitoredElementId,
																							MeasurementSetup setup,
																							Date startTime,
																							String localAddress,
																							Identifier testId) throws CreateObjectException {
		return new Measurement(id,
													 creatorId,
													 type,
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
														 SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(id,
																 creatorId,
																 this,
																 this,
																 ResultSort.RESULT_SORT_MEASUREMENT,
																 alarmLevel,
																 parameters);						
	}

	public Result retrieveResult(ResultSort resultSort) throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (Result)this.measurementDatabase.retrieveObject(this, RETRIEVE_RESULT, resultSort);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
}
