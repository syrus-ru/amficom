package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;

public class Port extends StorableObject implements Characterized {
	private Identifier type_id;
	private String name;
	private String description;
	private Identifier equipment_id;

	private ArrayList characteristics;

	private StorableObject_Database portDatabase;

	public Port(Identifier id) throws RetrieveObjectException {
		super(id);

		this.portDatabase = ConfigurationDatabaseContext.portDatabase;
		try {
			this.portDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Port(Port_Transferable pt) throws CreateObjectException {
		super(new Identifier(pt.id),
					new Date(pt.created),
					new Date(pt.modified),
					new Identifier(pt.creator_id),
					new Identifier(pt.modifier_id));
		this.type_id = new Identifier(pt.type_id);
		this.name = new String(pt.name);
		this.description = new String(pt.description);
		this.equipment_id = new Identifier(pt.equipment_id);

		this.characteristics = new ArrayList(pt.characteristics.length);
		for (int i = 0; i < pt.characteristics.length; i++)
			this.characteristics.add(new Characteristic(pt.characteristics[i]));

		this.portDatabase = ConfigurationDatabaseContext.portDatabase;
		try {
			this.portDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		Characteristic_Transferable[] ct = new Characteristic_Transferable[this.characteristics.size()];
		int i = 0;
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			ct[i++] = (Characteristic_Transferable)((Characteristic)iterator.next()).getTransferable();
		return new Port_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																 super.created.getTime(),
																 super.modified.getTime(),
																 (Identifier_Transferable)super.creator_id.getTransferable(),
																 (Identifier_Transferable)super.modifier_id.getTransferable(),
																 (Identifier_Transferable)this.type_id.getTransferable(),
																 new String(this.name),
																 new String(this.description),
																 (Identifier_Transferable)this.equipment_id.getTransferable(),
																 ct);
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

	public Identifier getEquipmentId() {
		return this.equipment_id;
	}

	public ArrayList getCharacteristics() {
		return this.characteristics;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier type_id,
																						String name,
																						String description,
																						Identifier equipment_id) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id);
		this.type_id = type_id;
		this.name = name;
		this.description = description;
		this.equipment_id = equipment_id;
	}

	public synchronized void setCharacteristics(ArrayList characteristics) {
		this.characteristics = characteristics;
	}
}