/*
 * $Id: MeasurementType.java,v 1.46 2005/01/26 09:57:30 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
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
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.46 $, $Date: 2005/01/26 09:57:30 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementType extends ActionType {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256726169373062969L;
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
		super(mtt.header,
			  new String(mtt.codename),
			  new String(mtt.description));

		try {
			List inParameterTypeIds = new ArrayList(mtt.in_parameter_type_ids.length);
			for (int i = 0; i < mtt.in_parameter_type_ids.length; i++)
				inParameterTypeIds.add(new Identifier(mtt.in_parameter_type_ids[i]));

			this.inParameterTypes = MeasurementStorableObjectPool.getStorableObjects(inParameterTypeIds, true);

			List outParameterTypeIds = new ArrayList(mtt.out_parameter_type_ids.length);
			for (int i = 0; i < mtt.out_parameter_type_ids.length; i++)
				outParameterTypeIds.add(new Identifier(mtt.out_parameter_type_ids[i]));

			this.outParameterTypes = MeasurementStorableObjectPool.getStorableObjects(outParameterTypeIds, true);

			List measurementPortTypeIds = new ArrayList(mtt.measurement_port_type_ids.length);
			for (int i = 0; i < mtt.measurement_port_type_ids.length; i++)
				measurementPortTypeIds.add(new Identifier(mtt.measurement_port_type_ids[i]));

			this.measurementPortTypes = ConfigurationStorableObjectPool.getStorableObjects(measurementPortTypeIds, true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
	}

	protected MeasurementType(Identifier id,
							  Identifier creatorId,
							  String codename,
							  String description,
							  List inParameterTypes,
							  List outParameterTypes,
							  List measurementPortTypes) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					codename,
					description);

		this.inParameterTypes = new ArrayList(); 
		this.setInParameterTypes0(inParameterTypes);

		this.outParameterTypes = new ArrayList();
		this.setOutParameterTypes0(outParameterTypes);

		this.measurementPortTypes = new ArrayList();
		this.setMeasurementPortTypes0(measurementPortTypes);

		this.measurementTypeDatabase = MeasurementDatabaseContext.measurementTypeDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param outParameterTypes
	 * @throws CreateObjectException
	 */
	public static MeasurementType createInstance(Identifier creatorId,
												 String codename,
												 String description,
												 List inParameterTypes,
												 List outParameterTypes,
												 List measurementPortTypes) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0  || description == null)
			throw new IllegalArgumentException("Argument is 'null'");		

		try {
			return new MeasurementType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE),
										creatorId,
										codename,
										description,
										inParameterTypes,
										outParameterTypes,
										measurementPortTypes);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MeasurementType.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.measurementTypeDatabase != null)
				this.measurementTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
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

		Identifier_Transferable[] outParTypeIds = new Identifier_Transferable[this.outParameterTypes.size()];
		i = 0;
		for (Iterator iterator = this.outParameterTypes.iterator(); iterator.hasNext();)
			outParTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();
		
		Identifier_Transferable[] measurementPortTypeIds = new Identifier_Transferable[this.measurementPortTypes.size()];
		i = 0;
		for (Iterator iterator = this.measurementPortTypes.iterator(); iterator.hasNext();){
			MeasurementPortType measurementPortType = (MeasurementPortType) iterator.next();
			if (measurementPortType != null)
				measurementPortTypeIds[i++] = (Identifier_Transferable) measurementPortType.getId().getTransferable();
		}

		return new MeasurementType_Transferable(super.getHeaderTransferable(),
												new String(super.codename),
												(super.description != null) ? (new String(super.description)) : "",
												inParTypeIds,	
												outParTypeIds,
												measurementPortTypeIds);
	}

  public List getInParameterTypes() {
		return Collections.unmodifiableList(this.inParameterTypes);
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
												  List outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

	protected void setInParameterTypes0(List inParameterTypes) {
		if (this.inParameterTypes == null)
			this.inParameterTypes = new ArrayList();
		else
			this.inParameterTypes.clear();
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

	protected void setOutParameterTypes0(List outParameterTypes) {
		if (this.outParameterTypes == null)
			this.outParameterTypes = new ArrayList();
		else
			this.outParameterTypes.clear();
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

	protected void setMeasurementPortTypes0(List measurementPortTypes) {
		this.measurementPortTypes.clear();
		if (measurementPortTypes != null)
	     	this.measurementPortTypes.addAll(measurementPortTypes);
	}

	/**
	 * client setter for measurementPortTypes
	 * @param measurementPortTypes
	 * 		The measurementPortTypes to set
	 */
	public void setMeasurementPortTypes(List measurementPortTypes) {
		this.setMeasurementPortTypes0(measurementPortTypes);
		super.currentVersion = super.getNextVersion();		
	}

	public List getMeasurementPortTypes() {
		return Collections.unmodifiableList(this.measurementPortTypes);
	}

	public List getDependencies() {
		List dependencies = new LinkedList();

		if (this.inParameterTypes != null)
			dependencies.addAll(this.inParameterTypes);

		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);

		if (this.measurementPortTypes != null)
			dependencies.addAll(this.measurementPortTypes);

		return dependencies;
	}
}
