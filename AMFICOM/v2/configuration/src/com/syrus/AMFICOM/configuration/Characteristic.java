package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;

public class Characteristic extends StorableObject {
	private Identifier type_id;
	private String name;
	private String description;
	private int sort;
	private String value;
	private Identifier characterized_id;

	private StorableObject_Database characteristicDatabase;

	public Characteristic(Identifier id) throws RetrieveObjectException {
		super(id);

		this.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
		try {
			this.characteristicDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Characteristic(Characteristic_Transferable ct) throws CreateObjectException {
		super(new Identifier(ct.id),
					new Date(ct.created),
					new Date(ct.modified),
					new Identifier(ct.creator_id),
					new Identifier(ct.modifier_id));
		this.type_id = new Identifier(ct.type_id);
		this.name = new String(ct.name);
		this.description = new String(ct.description);
		this.sort = ct.sort.value();
		this.value = new String(ct.value);
		this.characterized_id = new Identifier(ct.characterized_id);

		this.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
		try {
			this.characteristicDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		return new Characteristic_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																					 super.created.getTime(),
																					 super.modified.getTime(),
																					 (Identifier_Transferable)super.creator_id.getTransferable(),
																					 (Identifier_Transferable)super.modifier_id.getTransferable(),
																					 (Identifier_Transferable)this.type_id.getTransferable(),
																					 new String(this.name),
																					 new String(this.description),
																					 CharacteristicSort.from_int(this.sort),
																					 new String(this.value),
																					 (Identifier_Transferable)this.characterized_id.getTransferable());
	}

	public Identifier getTypeId() {
		return this.type_id;
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
		return this.characterized_id;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier type_id,
																						String name,
																						String description,
																						int sort,
																						String value,
																						Identifier characterized_id) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.type_id = type_id;
		this.name = name;
		this.description = description;
		this.sort = sort;
		this.value = value;
		this.characterized_id = characterized_id;
	}
}