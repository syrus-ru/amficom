/*
 * $Id: EventType.java,v 1.7 2005/02/14 13:09:40 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collection;
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
 * @version $Revision: 1.7 $, $Date: 2005/02/14 13:09:40 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventType extends StorableObjectType {
	private static final long serialVersionUID = -8660055955879452510L;

	public static final String CODENAME_MEASUREMENT_ALARM = "measurement_alarm";

	private Collection parameterTypes;

	private StorableObjectDatabase eventTypeDatabase;

	public EventType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.parameterTypes = new ArrayList();

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
			List parTypeIds = new ArrayList(ett.parameter_type_ids.length);
			for (int i = 0; i < ett.parameter_type_ids.length; i++)
				parTypeIds.add(new Identifier(ett.parameter_type_ids[i]));

			this.parameterTypes = GeneralStorableObjectPool.getStorableObjects(parTypeIds, true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.eventTypeDatabase = EventDatabaseContext.eventTypeDatabase;
	}

	protected EventType(Identifier id,
								Identifier creatorId,
								long version,
								String codename,
								String description,
								List parameterTypes) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);

		this.parameterTypes = new ArrayList(); 
		this.setParameterTypes0(parameterTypes);

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
			EventType eventType = new EventType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EVENTTYPE_ENTITY_CODE),
										creatorId,
										0L,
										codename,
										description,
										parameterTypes);
			eventType.changed = true;
			return eventType;
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("EventType.createInstance | cannot generate identifier ", ioee);
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

  public Collection getParameterTypes() {
		return Collections.unmodifiableCollection(this.parameterTypes);
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

	protected void setParameterTypes0(Collection parameterTypes) {
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
	public void setParameterTypes(Collection parameterTypes) {
		this.setParameterTypes0(parameterTypes);
		this.changed = true;
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
