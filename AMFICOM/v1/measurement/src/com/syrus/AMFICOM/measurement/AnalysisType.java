/*
 * $Id: AnalysisType.java,v 1.26 2004/08/30 15:00:05 bob Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2004/08/30 15:00:05 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class AnalysisType extends ActionType {

	private List inParameterTypes;
	private List criteriaParameterTypes;
	private List etalonParameterTypes;
	private List outParameterTypes;

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

		try {
			for (Iterator it = this.inParameterTypes.iterator(); it.hasNext();)
				MeasurementStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.criteriaParameterTypes.iterator(); it.hasNext();)
				MeasurementStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.etalonParameterTypes.iterator(); it.hasNext();)
				MeasurementStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.outParameterTypes.iterator(); it.hasNext();)
				MeasurementStorableObjectPool.putStorableObject((ParameterType) it.next());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
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

		try {
			this.inParameterTypes = new ArrayList(att.in_parameter_type_ids.length);
			for (int i = 0; i < att.in_parameter_type_ids.length; i++)
				this.inParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(att.in_parameter_type_ids[i]), true));
	
			this.criteriaParameterTypes = new ArrayList(att.criteria_parameter_type_ids.length);
			for (int i = 0; i < att.criteria_parameter_type_ids.length; i++)
				this.criteriaParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(att.criteria_parameter_type_ids[i]), true));
	
			this.etalonParameterTypes = new ArrayList(att.etalon_parameter_type_ids.length);
			for (int i = 0; i < att.etalon_parameter_type_ids.length; i++)
				this.etalonParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(att.etalon_parameter_type_ids[i]), true));
	
			this.outParameterTypes = new ArrayList(att.out_parameter_type_ids.length);
			for (int i = 0; i < att.out_parameter_type_ids.length; i++)
				this.outParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(att.out_parameter_type_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			this.analysisTypeDatabase.insert(this);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	protected AnalysisType(Identifier id,
											 Identifier creatorId,
											 String codename,
											 String description,
											 List inParameterTypes,
											 List criteriaParameterTypes,
											 List etalonParameterTypes,
											 List outParameterTypes) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		super.description = description;
		this.inParameterTypes = inParameterTypes;
		this.criteriaParameterTypes = criteriaParameterTypes;
		this.etalonParameterTypes = etalonParameterTypes;
		this.outParameterTypes = outParameterTypes;

		super.currentVersion = super.getNextVersion();
		
		this.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param criteriaParameterTypes
	 * @param etalonParameterTypes
	 * @param outParameterTypes
	 * @return
	 */
	public static AnalysisType createInstance(Identifier id,
																						Identifier creatorId,
																						String codename,
																						String description,
																						List inParameterTypes,
																						List criteriaParameterTypes,
																						List etalonParameterTypes,
																						List outParameterTypes){
		return new AnalysisType(id,
														creatorId,
														codename,
														description,
														inParameterTypes,
														criteriaParameterTypes,
														etalonParameterTypes,
														outParameterTypes);
	}
	
	public Object getTransferable() {
		int i;

		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] criteriaParTypeIds = new Identifier_Transferable[this.criteriaParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.criteriaParameterTypes.iterator(); iterator.hasNext();)
			criteriaParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] etalonParTypeIds = new Identifier_Transferable[this.etalonParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.etalonParameterTypes.iterator(); iterator.hasNext();)
			etalonParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypes.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		return new AnalysisType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																				 super.created.getTime(),
																				 super.modified.getTime(),
																				 (Identifier_Transferable)super.creatorId.getTransferable(),
																				 (Identifier_Transferable)super.modifierId.getTransferable(),
																				 new String(super.codename),
																				 (super.description != null) ? (new String(super.description)) : "",
																				 inParTypeIds,
																				 criteriaParTypeIds,
																				 etalonParTypeIds,
																				 outParTypeIds);
	}

	public List getInParameterTypes() {
		return this.inParameterTypes;
	}

	public List getCriteriaParameterTypes() {
		return this.criteriaParameterTypes;
	}

	public List getEtalonParameterTypes() {
		return this.etalonParameterTypes;
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
																								List criteriaParameterTypes,
																								List etalonParameterTypes,
																								List outParameterTypes) {
		this.inParameterTypes = inParameterTypes;
		this.criteriaParameterTypes = criteriaParameterTypes;
		this.etalonParameterTypes = etalonParameterTypes;
		this.outParameterTypes = outParameterTypes;
	}

	/**
	 * client setter for inParameterTypes
	 * 
	 * @param inParameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setInParameterTypeIds(List inParameterTypes) {
		super.currentVersion = super.getNextVersion();
		this.inParameterTypes = inParameterTypes;
	}

	/**
	 * client setter for criteriaParameterTypes
	 * 
	 * @param criteriaParameterTypes
	 *            The criteriaParameterTypes to set.
	 */
	public void setCriteriaParameterTypeIds(List criteriaParameterTypes) {
		super.currentVersion = super.getNextVersion();
		this.criteriaParameterTypes = criteriaParameterTypes;
	}

	/**
	 * client setter for etalonParameterTypes
	 * 
	 * @param etalonParameterTypes
	 *            The etalonParameterTypes to set.
	 */
	public void setEtalonParameterTypeIds(List etalonParameterTypes) {
		super.currentVersion = super.getNextVersion();
		this.etalonParameterTypes = etalonParameterTypes;
	}

	/**
	 * client setter for outParameterTypes
	 * 
	 * @param outParameterTypes
	 *            The outParameterTypes to set.
	 */
	public void setOutParameterTypeIds(List outParameterTypes) {
		super.currentVersion = super.getNextVersion();
		this.outParameterTypes = outParameterTypes;
	}
}
