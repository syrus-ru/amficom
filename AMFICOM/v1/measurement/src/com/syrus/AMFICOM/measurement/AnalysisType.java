/*
 * $Id: AnalysisType.java,v 1.21 2004/08/11 13:18:53 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

/**
 * @version $Revision: 1.21 $, $Date: 2004/08/11 13:18:53 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class AnalysisType extends ActionType {

	private List inParameterTypeIds;
	private List criteriaParameterTypeIds;
	private List etalonParameterTypeIds;
	private List outParameterTypeIds;

	private StorableObjectDatabase	analysisTypeDatabase;

	public AnalysisType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public AnalysisType(AnalysisType_Transferable att) throws CreateObjectException {
		super(new Identifier(att.id),
					new Date(att.created),
					new Date(att.modified),
					new Identifier(att.creator_id),
					new Identifier(att.modifier_id),
					new String(att.codename),
					new String(att.description));

		this.inParameterTypeIds = new ArrayList(att.in_parameter_type_ids.length);
		for (int i = 0; i < att.in_parameter_type_ids.length; i++)
			this.inParameterTypeIds.add(new Identifier(att.in_parameter_type_ids[i]));

		this.criteriaParameterTypeIds = new ArrayList(att.criteria_parameter_type_ids.length);
		for (int i = 0; i < att.criteria_parameter_type_ids.length; i++)
			this.criteriaParameterTypeIds.add(new Identifier(att.criteria_parameter_type_ids[i]));

		this.etalonParameterTypeIds = new ArrayList(att.etalon_parameter_type_ids.length);
		for (int i = 0; i < att.etalon_parameter_type_ids.length; i++)
			this.etalonParameterTypeIds.add(new Identifier(att.etalon_parameter_type_ids[i]));

		this.outParameterTypeIds = new ArrayList(att.out_parameter_type_ids.length);
		for (int i = 0; i < att.out_parameter_type_ids.length; i++)
			this.outParameterTypeIds.add(new Identifier(att.out_parameter_type_ids[i]));

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	private AnalysisType(Identifier id,
											 Identifier creatorId,
											 String codename,
											 String description,
											 List inParameterTypeIds,
											 List criteriaParameterTypeIds,
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
		this.criteriaParameterTypeIds = criteriaParameterTypeIds;
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
	 * @param criteriaParameterTypeIds
	 * @param etalonParameterTypeIds
	 * @param outParameterTypeIds
	 * @return
	 */
	public static AnalysisType createInstance(Identifier id,
																						Identifier creatorId,
																						String codename,
																						String description,
																						List inParameterTypeIds,
																						List criteriaParameterTypeIds,
																						List etalonParameterTypeIds,
																						List outParameterTypeIds){
		return new AnalysisType(id,
														creatorId,
														codename,
														description,
														inParameterTypeIds,
														criteriaParameterTypeIds,
														etalonParameterTypeIds,
														outParameterTypeIds);
	}
	
	public Object getTransferable() {
		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypeIds.size()];
		int i = 0;
		for (Iterator iterator = this.inParameterTypeIds.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] criteriaParTypeIds = new Identifier_Transferable[this.criteriaParameterTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.criteriaParameterTypeIds.iterator(); iterator.hasNext();)
			criteriaParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] etalonParTypeIds = new Identifier_Transferable[this.etalonParameterTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.etalonParameterTypeIds.iterator(); iterator.hasNext();)
			etalonParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypeIds.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypeIds.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((Identifier) iterator.next()).getTransferable();

		return new AnalysisType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																				 super.created.getTime(),
																				 super.modified.getTime(),
																				 (Identifier_Transferable)super.creatorId.getTransferable(),
																				 (Identifier_Transferable)super.modifierId.getTransferable(),
																				 new String(super.codename),
																				 new String(super.description),
																				 inParTypeIds,
																				 criteriaParTypeIds,
																				 etalonParTypeIds,
																				 outParTypeIds);
	}

	public List getInParameterTypeIds() {
		return this.inParameterTypeIds;
	}

	public List getCriteriaParameterTypeIds() {
		return this.criteriaParameterTypeIds;
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
																									List criteriaParameterTypeIds,
																									List etalonParameterTypeIds,
																									List outParameterTypeIds) {
		this.inParameterTypeIds = inParameterTypeIds;
		this.criteriaParameterTypeIds = criteriaParameterTypeIds;
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
	 * client setter for criteriaParameterTypeIds
	 * 
	 * @param criteriaParameterTypeIds
	 *            The criteriaParameterTypeIds to set.
	 */
	public void setCriteriaParameterTypeIds(List criteriaParameterTypeIds) {
		super.currentVersion = super.getNextVersion();
		this.criteriaParameterTypeIds = criteriaParameterTypeIds;
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
