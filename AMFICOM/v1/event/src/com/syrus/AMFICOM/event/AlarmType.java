/*
 * $Id: AlarmType.java,v 1.1 2004/12/27 13:05:54 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.event.corba.AlarmType_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/27 13:05:54 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class AlarmType extends StorableObjectType {

	private StorableObjectDatabase alarmTypeDatabase;

	public AlarmType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.alarmTypeDatabase = EventDatabaseContext.alarmTypeDatabase;
		try {
			this.alarmTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public AlarmType(AlarmType_Transferable att) throws CreateObjectException {
		super(att.header,
					new String(att.codename),
					new String(att.description));
	}

	protected AlarmType(Identifier id,
								Identifier creatorId,
								String codename,
								String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				codename,
				description);
	}

	/**
	 * Create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @return a newly generated object
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 */
	public static AlarmType createInstance(Identifier creatorId,
															String codename,
															String description) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null)
			throw new IllegalArgumentException("Argument is null'");

		try {
			return new AlarmType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.ALARMTYPE_ENTITY_CODE),
										creatorId,
										codename,
										description);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new CreateObjectException("AlarmType.createInstance | cannot generate identifier ", ioee);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.alarmTypeDatabase != null)
				this.alarmTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		return new AlarmType_Transferable(super.getHeaderTransferable(),
										new String(super.codename),
										(super.description != null) ? (new String(super.description)) : "");
	}

	public List getDependencies() {
		return Collections.EMPTY_LIST;
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
}
