/*
 * $Id: TransmissionPath.java,v 1.2 2004/08/04 08:59:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
/**
 * @version $Revision: 1.2 $, $Date: 2004/08/04 08:59:27 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class TransmissionPath extends MonitoredDomainMember implements Characterized {
	private String name;
	private String description;

	private StorableObjectDatabase transmissionPathDatabase;

	public TransmissionPath(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			this.transmissionPathDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public TransmissionPath(TransmissionPath_Transferable tpt) throws CreateObjectException {
		super(new Identifier(tpt.id),
					new Date(tpt.created),
					new Date(tpt.modified),
					new Identifier(tpt.creator_id),
					new Identifier(tpt.modifier_id),
					new Identifier(tpt.domain_id));
		super.monitoredElementIds = new ArrayList(tpt.monitored_element_ids.length);
		for (int i = 0; i < tpt.monitored_element_ids.length; i++)
			super.monitoredElementIds.add(new Identifier(tpt.monitored_element_ids[i]));

		this.name = new String(tpt.name);
		this.description = new String(tpt.description);

		this.transmissionPathDatabase = ConfigurationDatabaseContext.transmissionPathDatabase;
		try {
			this.transmissionPathDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}
}
