/*
 * $Id: Modeling.java,v 1.22 2005/01/12 13:34:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import java.util.List;
import java.util.Collections;

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
 * @version $Revision: 1.22 $, $Date: 2005/01/12 13:34:13 $
 * @author $Author: arseniy $
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

		this.modelingDatabase = MeasurementDatabaseContext.modelingDatabase;
		try {
			this.modelingDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Modeling(Modeling_Transferable mt) throws CreateObjectException {
		super(mt.header,
			  null,
			  new Identifier(mt.monitored_element_id));

		try {
			super.type = (ModelingType)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.type_id), true);

			this.argumentSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(mt.argument_set_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.name = new String(mt.name);

		this.modelingDatabase = MeasurementDatabaseContext.modelingDatabase;
	}

	protected Modeling(Identifier id,
					   Identifier creatorId,
						 ModelingType type,
					   Identifier monitoredElementId,
					   String name,
					   Set argumentSet) {
		super(id,
					new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),
					creatorId,
					creatorId,
					type,
					monitoredElementId);
		this.name = name;
		this.argumentSet = argumentSet;

		this.modelingDatabase = MeasurementDatabaseContext.modelingDatabase;

	}
	
	/**
	 * create new instance for client
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
			return new Modeling(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MODELING_ENTITY_CODE),
										creatorId,
										type,
										monitoredElementId,
										name,
										argumentSet);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Modeling.createInstance | cannot generate identifier ", e);
		}
		
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.modelingDatabase != null)
				this.modelingDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Result createResult(Identifier creatorId,
								Measurement measurement,
								SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(creatorId,
											null,
											this,
											ResultSort.RESULT_SORT_MODELING,
											parameters);
	}

	public Result createResult(Identifier creatorId,		
							   SetParameter[] parameters) throws CreateObjectException {
		return Result.createInstance(creatorId,
											null,
											this,
											ResultSort.RESULT_SORT_MODELING,
											parameters);
	}

	public Object getTransferable() {
		return new Modeling_Transferable(super.getHeaderTransferable(),
									(Identifier_Transferable)super.type.getId().getTransferable(),
									(Identifier_Transferable)super.monitoredElementId.getTransferable(),
									new String(this.name),
									(Identifier_Transferable)this.argumentSet.getId().getTransferable());
	}

	protected synchronized void setAttributes(Date created,
																	Date modified,
																	Identifier creatorId,
																	Identifier modifierId,
																	ModelingType type,
																	Identifier monitoredElementId,
																	String name,
																	Set argumentSet) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												type,
												monitoredElementId);
		this.name = name;
		this.argumentSet = argumentSet;
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
		
	public List getDependencies() {
		return Collections.singletonList(this.argumentSet);
	}
}
