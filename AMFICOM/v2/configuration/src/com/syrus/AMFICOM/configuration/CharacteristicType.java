package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;

public class CharacteristicType extends StorableObjectType  {
	private int data_type;
	private boolean is_editable;
	private boolean is_visible;

	private StorableObject_Database characteristicTypeDatabase;

	public CharacteristicType(Identifier id) throws RetrieveObjectException {
		super(id);

		this.characteristicTypeDatabase = ConfigurationDatabaseContext.characteristicTypeDatabase;
		try {
			this.characteristicTypeDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
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
		this.data_type = ctt.data_type.value();
		this.is_editable = ctt.is_editable;
		this.is_visible = ctt.is_visible;

		this.characteristicTypeDatabase = ConfigurationDatabaseContext.characteristicTypeDatabase;
		try {
			this.characteristicTypeDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new CharacteristicType_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																							 super.created.getTime(),
																							 super.modified.getTime(),
																							 (Identifier_Transferable)super.creator_id.getTransferable(),
																							 (Identifier_Transferable)super.modifier_id.getTransferable(),
																							 new String(super.codename),
																							 new String(super.description),
																							 DataType.from_int(this.data_type),
																							 this.is_editable,
																							 this.is_visible);
	}

	public DataType getDataType() {
		return DataType.from_int(this.data_type);
	}

	public boolean getIsEditable() {
		return this.is_editable;
	}

	public boolean getIsVisible() {
		return this.is_visible;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						String codename,
																						String description,
																						int data_type,
																						boolean is_editable,
																						boolean is_visible) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												codename,
												description);
		this.data_type = data_type;
		this.is_editable = is_editable;
		this.is_visible = is_visible;
	}
}