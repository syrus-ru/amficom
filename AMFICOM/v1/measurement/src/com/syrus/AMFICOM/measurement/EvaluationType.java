/*
 * $Id: EvaluationType.java,v 1.18 2004/08/11 13:18:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;

/**
 * @version $Revision: 1.18 $, $Date: 2004/08/11 13:18:53 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class EvaluationType extends ActionType {

	private List inParameterTypeIds;
	private List thresholdParameterTypeIds;
	private List etalonParameterTypeIds;
	private List outParameterTypeIds;

	private StorableObjectDatabase	evaluationTypeDatabase;

	public EvaluationType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			this.evaluationTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public EvaluationType(EvaluationType_Transferable ett) throws CreateObjectException {
		super(new Identifier(ett.id),
					new Date(ett.created),
					new Date(ett.modified),
					new Identifier(ett.creator_id),
					new Identifier(ett.modifier_id),
					new String(ett.codename),
					new String(ett.description));

		this.inParameterTypeIds = new ArrayList(ett.in_parameter_type_ids.length);
		for (int i = 0; i < ett.in_parameter_type_ids.length; i++)
			this.inParameterTypeIds.add(new Identifier(ett.in_parameter_type_ids[i]));

		this.thresholdParameterTypeIds = new ArrayList(ett.threshold_parameter_type_ids.length);
		for (int i = 0; i < ett.threshold_parameter_type_ids.length; i++)
			this.thresholdParameterTypeIds.add(new Identifier(ett.threshold_parameter_type_ids[i]));

		this.etalonParameterTypeIds = new ArrayList(ett.etalon_parameter_type_ids.length);
		for (int i = 0; i < ett.etalon_parameter_type_ids.length; i++)
			this.etalonParameterTypeIds.add(new Identifier(ett.etalon_parameter_type_ids[i]));

		this.outParameterTypeIds = new ArrayList(ett.out_parameter_type_ids.length);
		for (int i = 0; i < ett.out_parameter_type_ids.length; i++)
			this.outParameterTypeIds.add(new Identifier(ett.out_parameter_type_ids[i]));

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			this.evaluationTypeDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}	
	
	private EvaluationType(Identifier id,
												 Identifier creatorId,
												 String codename,
												 String description,
												 List inParameterTypeIds,
												 List thresholdParameterTypeIds,
												 List etalonParameterTypeIds,
												 List outParameterTypeIds) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		super.description = description;
		this.inParameterTypeIds = inParameterTypeIds;
		this.thresholdParameterTypeIds = thresholdParameterTypeIds;
		this.etalonParameterTypeIds = etalonParameterTypeIds;
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
	 * @param thresholdParameterTypeIds
	 * @param etalonParameterTypeIds
	 * @param outParameterTypeIds
	 * @return
	 */
	public static EvaluationType createInstance(Identifier id,
																							Identifier creatorId,
																							String codename,
																							String description,
																							List inParameterTypeIds,
																							List thresholdParameterTypeIds,
																							List etalonParameterTypeIds,
																							List outParameterTypeIds) {
		return new EvaluationType(id,
															creatorId,
															codename,
															description,
															inParameterTypeIds,
															thresholdParameterTypeIds,
															etalonParameterTypeIds,
															outParameterTypeIds);
	}

	public Object getTransferable() {
		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypeIds.size()];
		int i = 0;
		for (Iterator iterator = this.inParameterTypeIds.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] thresholdParTypeIds = new Identifier_Transferable[this.thresholdParameterTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.thresholdParameterTypeIds.iterator(); iterator.hasNext();)
			thresholdParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] etalonParTypeIds = new Identifier_Transferable[this.etalonParameterTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.etalonParameterTypeIds.iterator(); iterator.hasNext();)
			etalonParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypeIds.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new EvaluationType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																					 super.created.getTime(),
																					 super.modified.getTime(),
																					 (Identifier_Transferable)super.creatorId.getTransferable(),
																					 (Identifier_Transferable)super.modifierId.getTransferable(),
																					 new String(super.codename),
																					 new String(super.description),
																					 inParTypeIds,
																					 thresholdParTypeIds,
																					 etalonParTypeIds,
																					 outParTypeIds);
	}

	public List getInParameterTypeIds() {
		return this.inParameterTypeIds;
	}

	public List getThresholdParameterTypeIds() {
		return this.thresholdParameterTypeIds;
	}

	public List getEtalonParameterTypeIds() {
		return this.etalonParameterTypeIds;
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
																									List thresholdParameterTypeIds,
																									List etalonParameterTypeIds,
																									List outParameterTypeIds) {
		this.inParameterTypeIds = inParameterTypeIds;
		this.thresholdParameterTypeIds = thresholdParameterTypeIds;
		this.etalonParameterTypeIds = etalonParameterTypeIds;
		this.outParameterTypeIds = outParameterTypeIds;
	}

	/**
	 * client setter for inParameterTypeIds
	 * 
	 * @param inParameterTypeIds
	 *            The inParameterTypeIds to set.
	 */
	public void setInParameterTypeIds(List inParameterTypeIds) {
		super.currentVersion = super.getNextVersion();
		this.inParameterTypeIds = inParameterTypeIds;
	}

	/**
	 * client setter for thresholdParameterTypeIds
	 * 
	 * @param thresholdParameterTypeIds
	 *            The thresholdParameterTypeIds to set.
	 */
	public void setThresholdParameterTypeIds(List thresholdParameterTypeIds) {
		super.currentVersion = super.getNextVersion();
		this.thresholdParameterTypeIds = thresholdParameterTypeIds;
	}

	/**
	 * client setter for etalonParameterTypeIds
	 * 
	 * @param etalonParameterTypeIds
	 *            The etalonParameterTypeIds to set.
	 */
	public void setEtalonParameterTypeIds(List etalonParameterTypeIds) {
		super.currentVersion = super.getNextVersion();
		this.etalonParameterTypeIds = etalonParameterTypeIds;
	}

	/**
	 * client setter for outParameterTypeIds
	 * 
	 * @param outParameterTypeIds
	 *            The outParameterTypeIds to set.
	 */
	public void setOutParameterTypeIds(List outParameterTypeIds) {
		super.currentVersion = super.getNextVersion();
		this.outParameterTypeIds = outParameterTypeIds;
	}
}
