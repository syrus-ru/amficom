/*
 * $Id: Measurement.java,v 1.47 2005/02/01 11:37:12 arseniy Exp $
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
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
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

/**
 * @version $Revision: 1.47 $, $Date: 2005/02/01 11:37:12 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class Measurement extends Action {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3544388098013476664L;

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
			  new Identifier(mt.monitored_element_id),
			  null);
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

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
	}

	protected Measurement(Identifier id,
											Identifier creatorId,
											MeasurementType type,
											Identifier monitoredElementId,
											String name,
											MeasurementSetup setup,
											Date startTime,
											String localAddress,
											Identifier testId) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					type,
					monitoredElementId,
					null);
		this.name = name;
		this.setup = setup;
		this.startTime = startTime;
		if (this.setup != null)
			this.duration = this.setup.getMeasurementDuration();
		this.status = MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED;
		this.localAddress = localAddress;
		this.testId = testId;

		this.measurementDatabase = MeasurementDatabaseContext.measurementDatabase;
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.measurementDatabase != null)
				this.measurementDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		return new Measurement_Transferable(super.getHeaderTransferable(),
											(Identifier_Transferable)super.type.getId().getTransferable(),
											(Identifier_Transferable)super.monitoredElementId.getTransferable(),
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
	
	public void setSetup(MeasurementSetup setup) {
		this.setup = setup;
		super.currentVersion = super.getNextVersion();
	}

	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		super.currentVersion = super.getNextVersion();
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
		super.modifierId = (Identifier) modifierId.clone();
		try {
			this.measurementDatabase.update(this, UPDATE_STATUS, null);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException(e.getMessage(), e);
		}
		catch (VersionCollisionException vce) {
			throw new UpdateObjectException(vce.getMessage(), vce);
		}
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  MeasurementType type,
											  Identifier monitoredElementId,
											  String name,
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
			monitoredElementId,
			null);
		this.name = name;
		this.setup = setup;
		this.startTime = startTime;
		this.duration = duration;
		this.status = status;
		this.localAddress = localAddress;
		this.testId = testId;
	}

	/**
	 * Create a new instance for client
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param name
	 * @param setup
	 * @param startTime
	 * @param localAddress
	 * @param testId
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	protected static Measurement createInstance(Identifier creatorId,
												MeasurementType type,
												Identifier monitoredElementId,
												String name,
												MeasurementSetup setup,
												Date startTime,
												String localAddress,
												Identifier testId) throws CreateObjectException {
		if (creatorId == null || type == null || name == null ||
				monitoredElementId == null || setup == null || startTime == null || localAddress == null || testId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new Measurement(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE),
										creatorId,
										type,
										monitoredElementId,
										name,
										setup,
										startTime,
										localAddress,
										testId);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Measurement.createInstance | cannot generate identifier ", e);
		}
	}

	public Result createResult(Identifier resultCreatorId, SetParameter[] resultParameters) throws CreateObjectException {
		return Result.createInstance(resultCreatorId,
				this,
				ResultSort.RESULT_SORT_MEASUREMENT,
				resultParameters);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.testId);
		dependencies.add(this.setup);
		return dependencies;
	}
	/**
	 * @param localAddress The localAddress to set.
	 */
	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(MeasurementStatus status) {
		this.status = status.value();
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param testId The testId to set.
	 */
	public void setTestId(Identifier testId) {
		this.testId = testId;
		super.currentVersion = super.getNextVersion();
	}
}
