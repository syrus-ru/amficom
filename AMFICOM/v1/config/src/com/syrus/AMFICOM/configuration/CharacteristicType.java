/*
 * $Id: CharacteristicType.java,v 1.6 2004/07/28 12:54:18 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2004/07/28 12:54:18 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class CharacteristicType extends StorableObjectType {
	private int dataType;
	private boolean isEditable;
	private boolean isVisible;

	private StorableObjectDatabase characteristicTypeDatabase;

	public CharacteristicType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristicTypeDatabase = ConfigurationDatabaseContext.characteristicTypeDatabase;
		try {
			this.characteristicTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CharacteristicType(CharacteristicType_Transferable ctt) throws CreateObjectException {
		super(new Identifier(ctt.id),
					new Date(ctt.created),
					new Date(ctt.modified),
					new Identifier(ctt.creator_id),
					new Identifier(ctt.modifier_id),
					new String(ctt.codename),
					new String(ctt.description));
		this.dataType = ctt.data_type.value();
		this.isEditable = ctt.is_editable;
		this.isVisible = ctt.is_visible;

		this.characteristicTypeDatabase = ConfigurationDatabaseContext.characteristicTypeDatabase;
		try {
			this.characteristicTypeDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		return new CharacteristicType_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																							 super.created.getTime(),
																							 super.modified.getTime(),
																							 (Identifier_Transferable)super.creatorId.getTransferable(),
																							 (Identifier_Transferable)super.modifierId.getTransferable(),
																							 new String(super.codename),
																							 new String(super.description),
																							 DataType.from_int(this.dataType),
																							 this.isEditable,
																							 this.isVisible);
	}

	public DataType getDataType() {
		return DataType.from_int(this.dataType);
	}

	public boolean getIsEditable() {
		return this.isEditable;
	}

	public boolean getIsVisible() {
		return this.isVisible;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description,
																						int dataType,
																						boolean isEditable,
																						boolean isVisible) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
		this.dataType = dataType;
		this.isEditable = isEditable;
		this.isVisible = isVisible;
	}
}
