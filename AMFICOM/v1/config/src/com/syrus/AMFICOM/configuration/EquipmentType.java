/*
 * $Id: EquipmentType.java,v 1.4 2004/08/10 19:01:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/10 19:01:08 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class EquipmentType extends StorableObjectType {

	private StorableObjectDatabase equipmentTypeDatabase;

	public EquipmentType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			this.equipmentTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public EquipmentType(EquipmentType_Transferable ett) throws CreateObjectException {
		super(new Identifier(ett.id),
					new Date(ett.created),
					new Date(ett.modified),
					new Identifier(ett.creator_id),
					new Identifier(ett.modifier_id),
					new String(ett.codename),
					new String(ett.description));

		this.equipmentTypeDatabase = ConfigurationDatabaseContext.equipmentTypeDatabase;
		try {
			this.equipmentTypeDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}
	
	public Object getTransferable() {
		return new EquipmentType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																					super.created.getTime(),
																					super.modified.getTime(),
																					(Identifier_Transferable)super.creatorId.getTransferable(),
																					(Identifier_Transferable)super.modifierId.getTransferable(),
																					new String(super.codename),
																					new String(super.description));
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
