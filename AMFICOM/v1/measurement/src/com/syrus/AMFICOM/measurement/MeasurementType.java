/*
 * $Id: MeasurementType.java,v 1.28 2004/10/20 15:08:31 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.28 $, $Date: 2004/10/20 15:08:31 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementType extends ActionType {
	private List inParameterTypes;
	private List outParameterTypes;
	private List measurementPortTypes;
	
	private StorableObjectDatabase	measurementTypeDatabase;

	public MeasurementType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
		try {
			this.measurementTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		try {
			for (Iterator it = this.inParameterTypes.iterator(); it.hasNext();)
				MeasurementStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.outParameterTypes.iterator(); it.hasNext();)
				MeasurementStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.measurementPortTypes.iterator(); it.hasNext();)
				ConfigurationStorableObjectPool.putStorableObject((MeasurementPortType) it.next());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
		}
	}

	public MeasurementType(MeasurementType_Transferable mtt) throws CreateObjectException {
		super(new Identifier(mtt.id),
					new Date(mtt.created),
					new Date(mtt.modified),
					new Identifier(mtt.creator_id),
					new Identifier(mtt.modifier_id),
					new String(mtt.codename),
					new String(mtt.description));

		try {
			this.inParameterTypes = new ArrayList(mtt.in_parameter_type_ids.length);
			for (int i = 0; i < mtt.in_parameter_type_ids.length; i++)
				this.inParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(mtt.in_parameter_type_ids[i]), true));
	
			this.outParameterTypes = new ArrayList(mtt.out_parameter_type_ids.length);
			for (int i = 0; i < mtt.out_parameter_type_ids.length; i++)
				this.outParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(mtt.out_parameter_type_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		
	}
	
	protected MeasurementType(Identifier id,
													Identifier creatorId,
													String codename,
													String description,
													List inParameterTypes,
													List	outParameterTypes,
													List measurementPortTypes){
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		super.description = description;
		this.inParameterTypes = inParameterTypes;
		this.outParameterTypes = outParameterTypes;
		this.measurementPortTypes = measurementPortTypes;

		super.currentVersion = super.getNextVersion();
		
		this.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
	}
	
	/**
	 * create new instance for client
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param outParameterTypes
	 * @return
	 */
	public static MeasurementType createInstance(Identifier id,
																							 Identifier creatorId,
																							 String codename,
																							 String description,
																							 List inParameterTypes,
																							 List	outParameterTypes,
																							 List measurementPortTypes) {
		return new MeasurementType(id,
															 creatorId,
															 codename,
															 description,
															 inParameterTypes,
															 outParameterTypes,
															 measurementPortTypes);
	}
	
	public static MeasurementType getInstance(MeasurementType_Transferable mtt) throws CreateObjectException {
		
		MeasurementType measurementType = new MeasurementType(mtt);
		
		measurementType.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
		try {
			if (measurementType.measurementTypeDatabase != null)
				measurementType.measurementTypeDatabase.insert(measurementType);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
		
		return measurementType;
	}

	public Object getTransferable() {
		int i;

		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypes.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();
		
		Identifier_Transferable[] measurementPortTypeIds = new Identifier_Transferable[this.measurementPortTypes.size()];
		i = 0;
		for (Iterator iterator = this.measurementPortTypes.iterator(); iterator.hasNext();)
			measurementPortTypeIds[i++] = (Identifier_Transferable) ((MeasurementPortType) iterator.next()).getId().getTransferable();


		return new MeasurementType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																						super.created.getTime(),
																						super.modified.getTime(),
																						(Identifier_Transferable)super.creatorId.getTransferable(),
																						(Identifier_Transferable)super.modifierId.getTransferable(),
																						new String(super.codename),
																						(super.description != null) ? (new String(super.description)) : "",
																						inParTypeIds,
																						outParTypeIds,
																						measurementPortTypeIds);
	}

    public short getEntityCode() {
        return ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE;
    }
    
    public List getInParameterTypes() {
		return this.inParameterTypes;
	}

	public List getOutParameterTypes() {
		return this.outParameterTypes;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
	}

	protected synchronized void setParameterTypes(List inParameterTypes,
																								List outParameterTypes) {
		this.inParameterTypes = inParameterTypes;
		this.outParameterTypes = outParameterTypes;
	}
	/**
	 * client setter for inParameterTypes
	 * @param inParameterTypes The inParameterTypes to set.
	 */
	public void setInParameterTypes(List inParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.inParameterTypes = inParameterTypes;
	}
	/**
	 * client setter for outParameterTypes
	 * @param outParameterTypes The outParameterTypes to set.
	 */
	public void setOutParameterTypes(List outParameterTypes) {
		this.currentVersion = super.getNextVersion();
		this.outParameterTypes = outParameterTypes;
	}
	
	public List getMeasurementPortTypes() {
		return this.measurementPortTypes;
	}
	
	public void setMeasurementPortTypes(List measurementPortTypes) {
		this.measurementPortTypes = measurementPortTypes;
	}
}
