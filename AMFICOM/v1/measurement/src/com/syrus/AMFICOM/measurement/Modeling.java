/*
 * $Id: Modeling.java,v 1.32 2005/04/04 16:06:27 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.Collections;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.32 $, $Date: 2005/04/04 16:06:27 $
 * @author $Author: bass $
 * @author arseniy
 * @module measurement_v1
 */
public class Modeling extends Action {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7265091663685378139L;

	private String name;
	private Set argumentSet;
	
	private StorableObjectDatabase modelingDatabase;

	public Modeling(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.modelingDatabase = MeasurementDatabaseContext.getModelingDatabase();
		try {
			this.modelingDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Modeling(Modeling_Transferable mt) throws CreateObjectException {
		this.modelingDatabase = MeasurementDatabaseContext.getModelingDatabase();
		this.fromTransferable(mt);
	}

	protected Modeling(Identifier id,
					   Identifier creatorId,
					   long version,
					   ModelingType type,
					   Identifier monitoredElementId,
					   String name,
					   Set argumentSet) {
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
		this.argumentSet = argumentSet;

		this.modelingDatabase = MeasurementDatabaseContext.getModelingDatabase();

	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		Modeling_Transferable mt = (Modeling_Transferable)transferable;
		super.fromTransferable(mt.header,
			  null,
			  new Identifier(mt.monitored_element_id),
			  null);

		try {
			super.type = (ModelingType)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.type_id), true);

			this.argumentSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.argument_set_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.name = mt.name;
	}
	
	public IDLEntity getTransferable() {
		return new Modeling_Transferable(super.getHeaderTransferable(),
									(Identifier_Transferable)super.type.getId().getTransferable(),
									(Identifier_Transferable)super.monitoredElementId.getTransferable(),
									this.name,
									(Identifier_Transferable)this.argumentSet.getId().getTransferable());
	}

  public short getEntityCode() {
		return ObjectEntities.MODELING_ENTITY_CODE;
	}

	public String getName() {
		return this.name;
	}

  public Set getArgumentSet() {
		return this.argumentSet;
	}

	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												ModelingType type,
												Identifier monitoredElementId,
												String name,
												Set argumentSet) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							type,
							monitoredElementId,
							null);
		this.name = name;
		this.argumentSet = argumentSet;
	}

	/**
	 * Create a new instance for client
	 * @param creatorId
	 * @param type
	 * @param monitoredElementId
	 * @param name
	 * @param argumentSet
	 * @return a newly generated instance
	 * @throws CreateObjectException
	 */
	public static Modeling createInstance(Identifier creatorId,
															ModelingType type,
															Identifier monitoredElementId,
															String name,
															Set argumentSet) throws CreateObjectException{
		if (creatorId == null || type == null || monitoredElementId == null || 
				name == null || argumentSet == null)
			throw new IllegalArgumentException("Argument is null'");

		try {
			Modeling modeling = new Modeling(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_ENTITY_CODE),
										creatorId,
										0L,
										type,
										monitoredElementId,
										name,
										argumentSet);
			modeling.changed = true;
			return modeling;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Modeling.createInstance | cannot generate identifier ", e);
		}
	}

	public Result createResult(Identifier resultCreatorId, SetParameter[] resultParameters) throws CreateObjectException {
		return Result.createInstance(resultCreatorId,
				this,
				ResultSort.RESULT_SORT_MODELING,
				resultParameters);
	}

	public java.util.Set getDependencies() {
		return Collections.singleton(this.argumentSet);
	}
	
	/**
	 * @param argumentSet The argumentSet to set.
	 */
	public void setArgumentSet(Set argumentSet) {
		this.argumentSet = argumentSet;
		super.changed = true;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
}
