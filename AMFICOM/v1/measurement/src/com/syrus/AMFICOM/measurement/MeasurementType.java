/*
 * $Id: MeasurementType.java,v 1.15 2004/08/11 10:59:55 arseniy Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;

/**
 * @version $Revision: 1.15 $, $Date: 2004/08/11 10:59:55 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementType extends ActionType {
	private List inParameterTypeIds;
	private List outParameterTypeIds;

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
	}

	public MeasurementType(MeasurementType_Transferable mtt) throws CreateObjectException {
		super(new Identifier(mtt.id),
					new Date(mtt.created),
					new Date(mtt.modified),
					new Identifier(mtt.creator_id),
					new Identifier(mtt.modifier_id),
					new String(mtt.codename),
					new String(mtt.description));

		this.inParameterTypeIds = new ArrayList(mtt.in_parameter_type_ids.length);
		for (int i = 0; i < mtt.in_parameter_type_ids.length; i++)
			this.inParameterTypeIds.add(new Identifier(mtt.in_parameter_type_ids[i]));

		this.outParameterTypeIds = new ArrayList(mtt.out_parameter_type_ids.length);
		for (int i = 0; i < mtt.out_parameter_type_ids.length; i++)
			this.outParameterTypeIds.add(new Identifier(mtt.out_parameter_type_ids[i]));

		this.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
		try {
			this.measurementTypeDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}
	
	private MeasurementType(Identifier id,
													Identifier creatorId,
													String codename,
													String description,
													List inParameterTypeIds,
													List	outParameterTypeIds){
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		super.description = description;
		this.inParameterTypeIds = inParameterTypeIds;
		this.outParameterTypeIds = outParameterTypeIds;

		super.currentVersion = super.getNextVersion();
	}
	
	/**
	 * create new instance for client
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypeIds
	 * @param outParameterTypeIds
	 * @return
	 */
	public static MeasurementType createInstance(Identifier id,
																							 Identifier creatorId,
																							 String codename,
																							 String description,
																							 List inParameterTypeIds,
																							 List	outParameterTypeIds){
		return new MeasurementType(id,
															 creatorId,
															 codename,
															 description,
															 inParameterTypeIds,
															 outParameterTypeIds);
	}

	public Object getTransferable() {
		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypeIds.size()];
		int i = 0;
		for (Iterator iterator = this.inParameterTypeIds.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypeIds.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new MeasurementType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																						super.created.getTime(),
																						super.modified.getTime(),
																						(Identifier_Transferable)super.creatorId.getTransferable(),
																						(Identifier_Transferable)super.modifierId.getTransferable(),
																						new String(super.codename),
																						new String(super.description),
																						inParTypeIds,
																						outParTypeIds);
	}

	public List getInParameterTypeIds() {
		return this.inParameterTypeIds;
	}

	public List getOutParameterTypeIds() {
		return this.outParameterTypeIds;
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

	protected synchronized void setParameterTypeIds(List inParameterTypeIds,
																									List outParameterTypeIds) {
		this.inParameterTypeIds = inParameterTypeIds;
		this.outParameterTypeIds = outParameterTypeIds;
	}
	/**
	 * client setter for inParameterTypeIds
	 * @param inParameterTypeIds The inParameterTypeIds to set.
	 */
	public void setInParameterTypeIds(List inParameterTypeIds) {
		this.currentVersion = super.getNextVersion();
		this.inParameterTypeIds = inParameterTypeIds;
	}
	/**
	 * client setter for outParameterTypeIds
	 * @param outParameterTypeIds The outParameterTypeIds to set.
	 */
	public void setOutParameterTypeIds(List outParameterTypeIds) {
		this.currentVersion = super.getNextVersion();
		this.outParameterTypeIds = outParameterTypeIds;
	}
}
