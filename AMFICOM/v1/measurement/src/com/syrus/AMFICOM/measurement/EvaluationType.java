/*
 * $Id: EvaluationType.java,v 1.43 2005/01/26 15:38:40 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.43 $, $Date: 2005/01/26 15:38:40 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class EvaluationType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3978420317305583161L;

	public static final String CODENAME_DADARA = "dadara";

	private List inParameterTypes;
	private List thresholdParameterTypes;
	private List etalonParameterTypes;
	private List outParameterTypes;

	private StorableObjectDatabase evaluationTypeDatabase;

	public EvaluationType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			this.evaluationTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		try {
			for (Iterator it = this.inParameterTypes.iterator(); it.hasNext();)
				MeasurementStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.thresholdParameterTypes.iterator(); it.hasNext();)
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

	public EvaluationType(EvaluationType_Transferable ett) throws CreateObjectException {
		super(ett.header,
			  new String(ett.codename),
			  new String(ett.description));

		try {
			this.inParameterTypes = new ArrayList(ett.in_parameter_type_ids.length);
			for (int i = 0; i < ett.in_parameter_type_ids.length; i++)
				this.inParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(ett.in_parameter_type_ids[i]), true));
	
			this.thresholdParameterTypes = new ArrayList(ett.threshold_parameter_type_ids.length);
			for (int i = 0; i < ett.threshold_parameter_type_ids.length; i++)
				this.thresholdParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(ett.threshold_parameter_type_ids[i]), true));
	
			this.etalonParameterTypes = new ArrayList(ett.etalon_parameter_type_ids.length);
			for (int i = 0; i < ett.etalon_parameter_type_ids.length; i++)
				this.etalonParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(ett.etalon_parameter_type_ids[i]), true));
	
			this.outParameterTypes = new ArrayList(ett.out_parameter_type_ids.length);
			for (int i = 0; i < ett.out_parameter_type_ids.length; i++)
				this.outParameterTypes.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(ett.out_parameter_type_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
	}	
	
	protected EvaluationType(Identifier id,
							 Identifier creatorId,
							 String codename,
							 String description,
							 List inParameterTypes,
							 List thresholdParameterTypes,
							 List etalonParameterTypes,
							 List outParameterTypes) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					codename,
					description);

		this.inParameterTypes = new ArrayList();
		this.setInParameterTypes0(inParameterTypes);

		this.thresholdParameterTypes = new ArrayList();
		this.setThresholdParameterTypes0(thresholdParameterTypes);

		this.etalonParameterTypes = new ArrayList();
		this.setEtalonParameterTypes0(etalonParameterTypes);

		this.outParameterTypes = new ArrayList();
		this.setOutParameterTypes0(outParameterTypes);

		this.evaluationTypeDatabase = MeasurementDatabaseContext.evaluationTypeDatabase;
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param thresholdParameterTypes
	 * @param etalonParameterTypes
	 * @param outParameterTypes
	 * @throws CreateObjectException
	 */
	public static EvaluationType createInstance(Identifier creatorId,
												String codename,
												String description,
												List inParameterTypes,
												List thresholdParameterTypes,
												List etalonParameterTypes,
												List outParameterTypes) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			return new EvaluationType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE),
										creatorId,
										codename,
										description,
										inParameterTypes,
										thresholdParameterTypes,
										etalonParameterTypes,
										outParameterTypes);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("EvaluationType.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.evaluationTypeDatabase != null)
				this.evaluationTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		int i;

		Identifier_Transferable[] inParTypeIds = new Identifier_Transferable[this.inParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.inParameterTypes.iterator(); iterator.hasNext();)
			inParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] thresholdParTypeIds = new Identifier_Transferable[this.thresholdParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.thresholdParameterTypes.iterator(); iterator.hasNext();)
			thresholdParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] etalonParTypeIds = new Identifier_Transferable[this.etalonParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.etalonParameterTypes.iterator(); iterator.hasNext();)
			etalonParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypes.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		return new EvaluationType_Transferable(super.getHeaderTransferable(),
											   new String(super.codename),
											   (super.description != null) ? (new String(super.description)) : "",
											   inParTypeIds,
											   thresholdParTypeIds,
											   etalonParTypeIds,
											   outParTypeIds);
	}

  public short getEntityCode() {
		return ObjectEntities.EVALUATIONTYPE_ENTITY_CODE;
	}

  public List getInParameterTypes() {
		return Collections.unmodifiableList(this.inParameterTypes);
	}

	public List getThresholdParameterTypes() {
		return Collections.unmodifiableList(this.thresholdParameterTypes);
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
												  List thresholdParameterTypes,
												  List etalonParameterTypes,
												  List outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setThresholdParameterTypes0(thresholdParameterTypes);
		this.setEtalonParameterTypes0(etalonParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

	protected void setInParameterTypes0(List inParameterTypes) {
		if (this.inParameterTypes == null)
			this.inParameterTypes = new ArrayList();
		else
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

	protected void setThresholdParameterTypes0(List thresholdParameterTypes) {
		if (this.thresholdParameterTypes == null)
			this.thresholdParameterTypes = new ArrayList();
		else
			this.thresholdParameterTypes.clear();
		if (thresholdParameterTypes != null)
			this.thresholdParameterTypes.addAll(thresholdParameterTypes);
	}

	/**
	 * client setter for thresholdParameterTypes
	 * 
	 * @param thresholdParameterTypes
	 *            The thresholdParameterTypes to set.
	 */
	public void setThresholdParameterTypes(List thresholdParameterTypes) {
		this.setThresholdParameterTypes0(thresholdParameterTypes);
		super.currentVersion = super.getNextVersion();
	}

	protected void setEtalonParameterTypes0(List etalonParameterTypes) {
		if (this.etalonParameterTypes == null)
			this.etalonParameterTypes = new ArrayList();
		else
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
		if (this.outParameterTypes == null)
			this.outParameterTypes = new ArrayList();
		else
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
				
		if (this.thresholdParameterTypes != null)
			dependencies.addAll(this.thresholdParameterTypes);
				
		if (this.etalonParameterTypes != null)
			dependencies.addAll(this.etalonParameterTypes);
				
		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);
				
		return dependencies;
	}
}
