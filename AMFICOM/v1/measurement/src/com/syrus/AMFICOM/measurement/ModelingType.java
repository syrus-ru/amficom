/*
 * $Id: ModelingType.java,v 1.10 2005/02/14 11:00:52 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
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
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/02/14 11:00:52 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class ModelingType extends ActionType {
/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3904963062178854712L;

	public static final String CODENAME_DADARA = "dadara";

	private Collection inParameterTypes;
	private Collection outParameterTypes;

	private StorableObjectDatabase modelingTypeDatabase;

	public ModelingType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.modelingTypeDatabase = MeasurementDatabaseContext.modelingTypeDatabase;
		try {
			this.modelingTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		try {
			for (Iterator it = this.inParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
			for (Iterator it = this.outParameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
		}
	}

	public ModelingType(ModelingType_Transferable mtt) throws CreateObjectException {
		super(mtt.header,
			  new String(mtt.codename),
			  new String(mtt.description));

		try {
			List parTypeIds;

			parTypeIds = new ArrayList(mtt.in_parameter_type_ids.length);
			for (int i = 0; i < mtt.in_parameter_type_ids.length; i++)
				parTypeIds.add(new Identifier(mtt.in_parameter_type_ids[i]));
			this.inParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypeIds, true);
			
			parTypeIds.clear();
			for (int i = 0; i < mtt.out_parameter_type_ids.length; i++)
				parTypeIds.add(new Identifier(mtt.out_parameter_type_ids[i]));
			this.outParameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypeIds, true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.modelingTypeDatabase = MeasurementDatabaseContext.modelingTypeDatabase;
	}

	protected ModelingType(Identifier id,
							 Identifier creatorId,
							 long version,
							 String codename,
							 String description,
							 List inParameterTypes,
							 List outParameterTypes) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.inParameterTypes = new ArrayList();
		this.setInParameterTypes0(inParameterTypes);

		this.outParameterTypes = new ArrayList();
		this.setOutParameterTypes0(outParameterTypes);

		this.modelingTypeDatabase = MeasurementDatabaseContext.modelingTypeDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param inParameterTypes
	 * @param outParameterTypes
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static ModelingType createInstance(Identifier creatorId,
												String codename,
												String description,
												List inParameterTypes,
												List outParameterTypes) throws CreateObjectException {
		if (creatorId == null || codename == null || codename.length() == 0 || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			ModelingType modelingType = new ModelingType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELINGTYPE_ENTITY_CODE),
										creatorId,
										0L,
										codename,
										description,
										inParameterTypes,
										outParameterTypes);
			modelingType.changed = true;
			return modelingType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("ModelingType.createInstance | cannot generate identifier ", e);
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

		return new ModelingType_Transferable(super.getHeaderTransferable(),
											new String(super.codename),
											(super.description != null) ? (new String(super.description)) : "",
											inParTypeIds,
											outParTypeIds);
	}

  public short getEntityCode() {
		return ObjectEntities.MODELINGTYPE_ENTITY_CODE;
	}

  public Collection getInParameterTypes() {
		return Collections.unmodifiableCollection(this.inParameterTypes);
	}

	public Collection getOutParameterTypes() {
		return Collections.unmodifiableCollection(this.outParameterTypes);
	}

	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  long version,
											  String codename,
											  String description) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
	}

	protected synchronized void setParameterTypes(Collection inParameterTypes,
			Collection outParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		this.setOutParameterTypes0(outParameterTypes);
	}

	protected void setInParameterTypes0(Collection inParameterTypes) {
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
	public void setInParameterTypes(Collection inParameterTypes) {
		this.setInParameterTypes0(inParameterTypes);
		super.changed = true;		
	}

	protected void setOutParameterTypes0(Collection outParameterTypes) {
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
	public void setOutParameterTypes(Collection outParameterTypes) {
		this.setOutParameterTypes0(outParameterTypes);
		super.changed = true;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		if (this.inParameterTypes != null)
			dependencies.addAll(this.inParameterTypes);
				
		if (this.outParameterTypes != null)
			dependencies.addAll(this.outParameterTypes);
				
		return dependencies;
	}
}
