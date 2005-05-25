/*
 * $Id: Measurement.java,v 1.69 2005/05/25 13:01:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.HashSet;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.69 $, $Date: 2005/05/25 13:01:05 $
 * @author $Author: bass $
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

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Measurement(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		MeasurementDatabase database = (MeasurementDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Measurement(Measurement_Transferable mt) throws CreateObjectException {
		try {
			this.fromTransferable(mt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	Measurement(Identifier id,
							Identifier creatorId,
							long version,
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
				version,
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
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Measurement_Transferable mt = (Measurement_Transferable) transferable;
		super.fromTransferable(mt.header, null, new Identifier(mt.monitored_element_id), null);

		super.type = (MeasurementType) StorableObjectPool.getStorableObject(new Identifier(mt.type_id),
			true);

		this.setup = (MeasurementSetup) StorableObjectPool.getStorableObject(
			new Identifier(mt.setup_id), true);

		this.name = mt.name;
		this.startTime = new Date(mt.start_time);
		this.duration = mt.duration;
		this.status = mt.status.value();
		this.localAddress = mt.local_address;
		this.testId = new Identifier(mt.test_id);
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new Measurement_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) super.type.getId().getTransferable(),
				(Identifier_Transferable) super.monitoredElementId.getTransferable(),
				this.name,
				(Identifier_Transferable) this.setup.getId().getTransferable(),
				this.startTime.getTime(),
				this.duration,
				MeasurementStatus.from_int(this.status),
				this.localAddress,
				(Identifier_Transferable) this.testId.getTransferable());
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.setup != null && this.startTime != null && this.localAddress != null && this.testId != null;
	}
	
	public short getEntityCode() {
		return ObjectEntities.MEASUREMENT_ENTITY_CODE;
	}

	public MeasurementSetup getSetup() {
		return this.setup;
	}
	
	public void setSetup(MeasurementSetup setup) {
		this.setup = setup;
		super.changed = true;
	}

	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		super.changed = true;
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

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
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
			version,
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

		try {
			Measurement measurement = new Measurement(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENT_ENTITY_CODE),
										creatorId,
										0L,
										type,
										monitoredElementId,
										name,
										setup,
										startTime,
										localAddress,
										testId);
			
			assert measurement.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			
			measurement.changed = true;
			return measurement;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
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
		super.changed = true;
	}

	public java.util.Set getResults() {
		LinkedIdsCondition condition = new LinkedIdsCondition(this.id, ObjectEntities.RESULT_ENTITY_CODE);
		java.util.Set results = null;
		try {
			results = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
		return results;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public java.util.Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		java.util.Set dependencies = new HashSet();
		dependencies.add(this.testId);
		dependencies.add(this.setup);
		return dependencies;
	}
	/**
	 * @param localAddress The localAddress to set.
	 */
	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
		super.changed = true;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(MeasurementStatus status) {
		this.status = status.value();
		super.changed = true;
	}
	/**
	 * @param testId The testId to set.
	 */
	public void setTestId(Identifier testId) {
		this.testId = testId;
		super.changed = true;
	}
}
