/*
 * $Id: KIS.java,v 1.12 2004/08/10 19:01:09 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;

/**
 * @version $Revision: 1.12 $, $Date: 2004/08/10 19:01:09 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class KIS extends DomainMember {
	private Identifier mcmId;
	private String name;
	private String description;
	
	private List measurementPortIds;

	private StorableObjectDatabase kisDatabase;

	public KIS(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public KIS(KIS_Transferable kt) throws CreateObjectException {
		super(new Identifier(kt.id),
					new Date(kt.created),
					new Date(kt.modified),
					new Identifier(kt.creator_id),
					new Identifier(kt.modifier_id),
					new Identifier(kt.domain_id));
		this.mcmId = new Identifier(kt.mcm_id);
		
		this.measurementPortIds = new ArrayList(kt.measurement_port_ids.length);
		for (int i = 0; i < kt.measurement_port_ids.length; i++)
			this.measurementPortIds.add(new Identifier(kt.measurement_port_ids[i]));

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] mportIds = new Identifier_Transferable[this.measurementPortIds.size()];
		for (Iterator iterator = this.measurementPortIds.iterator(); iterator.hasNext();)
			mportIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new KIS_Transferable((Identifier_Transferable)super.id.getTransferable(),
																super.created.getTime(),
																super.modified.getTime(),
																(Identifier_Transferable)super.creatorId.getTransferable(),
																(Identifier_Transferable)super.modifierId.getTransferable(),
																(Identifier_Transferable)super.domainId.getTransferable(),
																new String(this.name),
																new String(this.description),
																(Identifier_Transferable)this.mcmId.getTransferable(),
																mportIds);
	}

	public Identifier getMCMId() {
		return this.mcmId;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						Identifier domainId,
																						String name,
																						String description,																						
																						Identifier mcmId) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												domainId);
		this.name = name;
		this.description = description;
		this.mcmId = mcmId;
	}
}
