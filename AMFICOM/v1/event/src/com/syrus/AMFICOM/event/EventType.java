/*
 * $Id: EventType.java,v 1.5 2005/01/21 16:24:48 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collections;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.event.corba.EventType_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/01/21 16:24:48 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventType extends StorableObjectType {
	private static final long serialVersionUID = -8660055955879452510L;

	private List parameterTypes;

	private StorableObjectDatabase eventTypeDatabase;

	public EventType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.eventTypeDatabase = EventDatabaseContext.eventTypeDatabase;
		try {
			this.eventTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}

		try {
			for (Iterator it = this.parameterTypes.iterator(); it.hasNext();)
				GeneralStorableObjectPool.putStorableObject((ParameterType) it.next());
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
		}
	}

	public EventType(EventType_Transferable ett) throws CreateObjectException {
		super(ett.header,
					new String(ett.codename),
					new String(ett.description));

		try {
			List parameterTypeIds = new ArrayList(ett.parameter_type_ids.length);
			for (int i = 0; i < ett.parameter_type_ids.length; i++)
				parameterTypeIds.add(new Identifier(ett.parameter_type_ids[i]));

			this.parameterTypes = GeneralStorableObjectPool.getStorableObjects(parameterTypeIds, true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.eventTypeDatabase = EventDatabaseContext.eventTypeDatabase;
	}

	protected EventType(Identifier id,
								Identifier creatorId,
								String codename,
								String description,
								List parameterTypes) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				codename,
				description);

		this.parameterTypes = new ArrayList(); 
		this.setParameterTypes0(parameterTypes);

		super.currentVersion = super.getNextVersion();

		this.eventTypeDatabase = EventDatabaseContext.eventTypeDatabase;
	}

	/**
	 * Create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param parameterTypes
	 * @return a newly generated object
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static EventType createInstance(Identifier creatorId,
															String codename,
															String description,
															List parameterTypes) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null)
			throw new IllegalArgumentException("Argument is null'");

		try {
			return new EventType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTTYPE_ENTITY_CODE),
										creatorId,
										codename,
										description,
										parameterTypes);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EventType.createInstance | cannot generate identifier ", ioee);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.eventTypeDatabase != null)
				this.eventTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		Identifier_Transferable[] parTypeIds = new Identifier_Transferable[this.parameterTypes.size()];
		int i = 0;
		for (Iterator iterator = this.parameterTypes.iterator(); iterator.hasNext();)
			parTypeIds[i++] = (Identifier_Transferable) ((ParameterType) iterator.next()).getId().getTransferable();

		return new EventType_Transferable(super.getHeaderTransferable(),
										new String(super.codename),
										(super.description != null) ? (new String(super.description)) : "",
										parTypeIds);
	}

  public List getParameterTypes() {
		return Collections.unmodifiableList(this.parameterTypes);
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

	protected void setParameterTypes0(List parameterTypes) {
		this.parameterTypes.clear();
		if (parameterTypes != null)
	     	this.parameterTypes.addAll(parameterTypes);
	}

	/**
	 * client setter for parameterTypes
	 * 
	 * @param parameterTypes
	 *            The inParameterTypes to set.
	 */
	public void setParameterTypes(List parameterTypes) {
		this.setParameterTypes0(parameterTypes);
		super.currentVersion = super.getNextVersion();		
	}

	public List getDependencies() {
		List dependencies = new LinkedList();

		if (this.parameterTypes != null)
			dependencies.addAll(this.parameterTypes);

		return dependencies;
	}

	public String toString() {
		String str = getClass().getName() + EOSL
					 + ID + this.id + EOSL
					 + ID_CREATED + this.created.toString() + EOSL		
					 + ID_CREATOR_ID + this.creatorId.toString() + EOSL
					 + ID_MODIFIED + this.modified.toString() + EOSL
					 + ID_MODIFIER_ID + this.modifierId.toString() + EOSL
					 + TypedObject.ID_CODENAME + this.codename+ EOSL
					 + TypedObject.ID_DESCRIPTION + this.description + EOSL;
		return str;
	}
}
