/*
 * $Id: AnalysisType.java,v 1.37 2004/12/06 13:21:03 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
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
 * @version $Revision: 1.37 $, $Date: 2004/12/06 13:21:03 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class AnalysisType extends ActionType {

	private static final long	serialVersionUID	= 3257284751344874800L;
	
	private List inParameterTypes;
	private List criteriaParameterTypes;
	private List etalonParameterTypes;
	private List outParameterTypes;

	private StorableObjectDatabase	analysisTypeDatabase;	

	public AnalysisType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.inParameterTypes = new LinkedList();
		this.criteriaParameterTypes = new LinkedList();
		this.etalonParameterTypes = new LinkedList();
		this.outParameterTypes = new LinkedList();
		
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
		super(att.header,
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
		
		this.inParameterTypes = new LinkedList();
		this.setInParameterTypes0(inParameterTypes);
		
		
		this.criteriaParameterTypes = new LinkedList();
		this.setCriteriaParameterTypes0(criteriaParameterTypes);
		
		
		this.etalonParameterTypes = new LinkedList();
		this.setEtalonParameterTypes0(etalonParameterTypes);
		
		
		this.outParameterTypes = new LinkedList();
		this.setOutParameterTypes0(outParameterTypes);

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
	public static AnalysisType createInstance(Identifier creatorId,
											  String codename,
											  String description,
											  List inParameterTypes,
											  List criteriaParameterTypes,
											  List etalonParameterTypes,
											  List outParameterTypes){
		if (creatorId == null || codename == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return new AnalysisType(IdentifierPool.generateId(ObjectEntities.ANALYSISTYPE_ENTITY_CODE),
			creatorId,
			codename,
			description,
			inParameterTypes,
			criteriaParameterTypes,
			etalonParameterTypes,
			outParameterTypes);
	}
	
	public static AnalysisType getInstance(AnalysisType_Transferable att) throws CreateObjectException{
		AnalysisType analysisType = new AnalysisType(att);

		analysisType.analysisTypeDatabase = MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			if (analysisType.analysisTypeDatabase != null)
				analysisType.analysisTypeDatabase.insert(analysisType);
		}
		catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return analysisType;
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

		return new AnalysisType_Transferable(super.getHeaderTransferable(),
											 new String(super.codename),
											 (super.description != null) ? (new String(super.description)) : "",
											 inParTypeIds,
											 criteriaParTypeIds,
											 etalonParTypeIds,
											 outParTypeIds);
	}

	public short getEntityCode() {
        return ObjectEntities.ANALYSISTYPE_ENTITY_CODE;
    }
    
    public List getInParameterTypes() {
		return Collections.unmodifiableList(this.inParameterTypes);
	}

	public List getCriteriaParameterTypes() {
		return Collections.unmodifiableList(this.criteriaParameterTypes);
	}

	public List getEtalonParameterTypes() {
		return Collections.unmodifiableList(this.etalonParameterTypes);
	}

	public List getOutParameterTypes() {
		return Collections.unmodifiableList(this.outParameterTypes);
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
		this.setInParameterTypes0(inParameterTypes);
		this.setCriteriaParameterTypes0(criteriaParameterTypes);
		this.setEtalonParameterTypes0(etalonParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

	protected void setInParameterTypes0(List inParameterTypes) {
		this.inParameterTypes.clear();
		if (inParameterTypes != null)
	     	this.inParameterTypes.addAll(inParameterTypes);
	}
	
	/**
	 * client setter for inParameterTypes
	 * 
	 * @param inParameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setInParameterTypes(List inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.currentVersion = super.getNextVersion();		
	}

	protected void setCriteriaParameterTypes0(List criteriaParameterTypes) {
		this.criteriaParameterTypes.clear();
		if (criteriaParameterTypes != null)
	     	this.criteriaParameterTypes.addAll(criteriaParameterTypes);
	}
	/**
	 * client setter for criteriaParameterTypes
	 * 
	 * @param thresholdParameterTypes
	 *            The thresholdParameterTypes to set.
	 */
	public void setCriteriaParameterTypes(List thresholdParameterTypes) {
		this.setCriteriaParameterTypes0(thresholdParameterTypes);
		super.currentVersion = super.getNextVersion();
	}

	protected void setEtalonParameterTypes0(List etalonParameterTypes) {
		this.etalonParameterTypes.clear();
		if (etalonParameterTypes != null)
	     	this.etalonParameterTypes.addAll(etalonParameterTypes);
	}
	/**
	 * client setter for etalonParameterTypes
	 * 
	 * @param etalonParameterTypes
	 *            The etalonParameterTypes to set.
	 */
	public void setEtalonParameterTypes(List etalonParameterTypes) {
		this.setEtalonParameterTypes0(etalonParameterTypes);
		super.currentVersion = super.getNextVersion();
	}

	protected void setOutParameterTypes0(List outParameterTypes) {
		this.outParameterTypes.clear();
		if (outParameterTypes != null)
	     	this.outParameterTypes.addAll(outParameterTypes);
	}
	/**
	 * client setter for outParameterTypes
	 * 
	 * @param outParameterTypes
	 *            The outParameterTypes to set.
	 */
	public void setOutParameterTypes(List outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.currentVersion = super.getNextVersion();
	}	
	
	public List getDependencies() {
		List dependencies = new LinkedList();
		if (this.inParameterTypes != null)
			dependencies.addAll(this.inParameterTypes);
			
		if (this.criteriaParameterTypes != null)
			dependencies.addAll(this.criteriaParameterTypes);
        
		if (this.etalonParameterTypes != null)
			dependencies.addAll(this.etalonParameterTypes);
				
		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);
				
		return dependencies;
	}
}
