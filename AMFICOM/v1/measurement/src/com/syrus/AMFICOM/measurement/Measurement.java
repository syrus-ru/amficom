/*
 * $Id: Measurement.java,v 1.33 2004/11/12 11:44:53 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.event.corba.AlarmLevel;

/**
 * @version $Revision: 1.33 $, $Date: 2004/11/12 11:44:53 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class Measurement extends Action {
	public static final long DEFAULT_MEASUREMENT_DURATION = 3*60*1000;//milliseconds
	protected static final int RETRIEVE_RESULT = 1;
	protected static final int UPDATE_STATUS = 1;

	private String name;
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
		super(mt.header,
			  null,
			  new Identifier(mt.monitored_element_id));
		try {
			super.type = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.type_id), true);

			this.setup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.setup_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = mt.name;
		this.startTime = new Date(mt.start_time);
		this.duration = mt.duration;
		this.status = mt.status.value();
		this.localAddress = new String(mt.local_address);
		this.testId = new Identifier(mt.test_id);

	}

	protected Measurement(Identifier id,
											Identifier creatorId,
											MeasurementType type,
											String name,
											Identifier monitoredElementId,
											MeasurementSetup setup,
											Date startTime,
											String localAddress,
											Identifier testId) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.type = type;
		this.name = name;
		super.monitoredElementId = monitoredElementId;

		this.setup = setup;
		this.startTime = startTime;
		if (this.setup != null)
			this.duration = this.setup.getMeasurementDuration();
		this.status = MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED;
		this.localAddress = localAddress;
		this.testId = testId;

		super.currentVersion = super.getNextVersion();
		
		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
	}
	
	public static Measurement getInstance(Measurement_Transferable mt) throws CreateObjectException{
		Measurement measurement = new Measurement(mt);

		measurement.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
		try {
			if (measurement.measurementDatabase != null)
				measurement.measurementDatabase.insert(measurement);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		
		return measurement;
	}

	public Object getTransferable() {
		return new Measurement_Transferable(super.getHeaderTransferable(),
											(Identifier_Transferable)super.type.getId().getTransferable(),
											(Identifier_Transferable)super.monitoredElementId.getTransferable(),
											new String(super.type.getCodename()),
											this.name,
											(Identifier_Transferable)this.setup.getId().getTransferable(),
											this.startTime.getTime(),
											this.duration,
											MeasurementStatus.from_int(this.status),
											new String(this.localAddress),
											(Identifier_Transferable)this.testId.getTransferable());
	}

    public short getEntityCode() {
        return ObjectEntities.MEASUREMENT_ENTITY_CODE;
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
		catch (VersionCollisionException vce){
			throw new UpdateObjectException(vce.getMessage(), vce);
		}
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						MeasurementType type,
																						String name,
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
		this.name = name;
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
																							String name,
																							Identifier monitoredElementId,
																							MeasurementSetup setup,
																							Date startTime,
																							String localAddress,
																							Identifier testId) throws CreateObjectException {
		return new Measurement(id,
													 creatorId,
													 type,
													 name,
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
	
	/** 
	 * @deprecated as unsupport method
	 */
	public Result createResult(Identifier id, Identifier creatorId, SetParameter[] parameters)
			throws CreateObjectException {
		throw new UnsupportedOperationException("method isn't support");
	}

	public Result retrieveResult(ResultSort resultSort) throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (Result)this.measurementDatabase.retrieveObject(this, RETRIEVE_RESULT, resultSort);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.testId);
		dependencies.add(this.setup);
		return dependencies;
	}
}
