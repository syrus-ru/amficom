package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;

public class Characteristic extends StorableObject implements TypedObject {
	private Identifier typeId;
	private String name;
	private String description;
	private int sort;
	private String value;
	private Identifier characterizedId;

	private StorableObjectDatabase characteristicDatabase;

	public Characteristic(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
		try {
			this.characteristicDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Characteristic(Characteristic_Transferable ct) throws CreateObjectException {
		super(new Identifier(ct.id),
					new Date(ct.created),
					new Date(ct.modified),
					new Identifier(ct.creator_id),
					new Identifier(ct.modifier_id));
		this.typeId = new Identifier(ct.type_id);
		this.name = new String(ct.name);
		this.description = new String(ct.description);
		this.sort = ct.sort.value();
		this.value = new String(ct.value);
		this.characterizedId = new Identifier(ct.characterized_id);

		this.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
		try {
			this.characteristicDatabase.insert(this);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
	}

	public Object getTransferable() {
		return new Characteristic_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																					 super.created.getTime(),
																					 super.modified.getTime(),
																					 (Identifier_Transferable)super.creator_id.getTransferable(),
																					 (Identifier_Transferable)super.modifier_id.getTransferable(),
																					 (Identifier_Transferable)this.typeId.getTransferable(),
																					 new String(this.name),
																					 new String(this.description),
																					 CharacteristicSort.from_int(this.sort),
																					 new String(this.value),
																					 (Identifier_Transferable)this.characterizedId.getTransferable());
	}

	public Identifier getTypeId() {
		return this.typeId;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public CharacteristicSort getSort() {
		return CharacteristicSort.from_int(this.sort);
	}

	public String getValue() {
		return this.value;
	}

	public Identifier getCharacterizedId() {
		return this.characterizedId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier typeId,
																						String name,
																						String description,
																						int sort,
																						String value,
																						Identifier characterizedId) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.typeId = typeId;
		this.name = name;
		this.description = description;
		this.sort = sort;
		this.value = value;
		this.characterizedId = characterizedId;
	}
}