package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.EquipmentSort;

public class KIS extends Equipment {
	private Identifier mcm_id;

	private StorableObject_Database kisDatabase;

	public KIS(Identifier id) throws RetrieveObjectException {
		super(id);

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.retrieve(this);
		}
		catch (Exception e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public KIS(Equipment_Transferable eq) throws CreateObjectException {
		super(eq);
		this.mcm_id = new Identifier(eq.mcm_id);

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.insert(this);
		}
		catch (Exception e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] char_ids = new Identifier_Transferable[super.characteristic_ids.size()];
		for (Iterator iterator = super.characteristic_ids.iterator(); iterator.hasNext();)
			char_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] port_ids = new Identifier_Transferable[super.port_ids.size()];
		for (Iterator iterator = super.port_ids.iterator(); iterator.hasNext();)
			port_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] cport_ids = new Identifier_Transferable[super.cable_port_ids.size()];
		for (Iterator iterator = super.cable_port_ids.iterator(); iterator.hasNext();)
			cport_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		Identifier_Transferable[] sport_ids = new Identifier_Transferable[super.special_port_ids.size()];
		for (Iterator iterator = super.special_port_ids.iterator(); iterator.hasNext();)
			sport_ids[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new Equipment_Transferable((Identifier_Transferable)super.getId().getTransferable(),
																			super.created.getTime(),
																			super.modified.getTime(),
																			(Identifier_Transferable)super.creator_id.getTransferable(),
																			(Identifier_Transferable)super.modifier_id.getTransferable(),
																			(Identifier_Transferable)super.domain_id.getTransferable(),
																			(Identifier_Transferable)super.type_id.getTransferable(),
																			new String(super.name),
																			new String(super.description),
																			new String(super.latitude),
																			new String(super.longitude),
																			new String(super.hw_serial),
																			new String(super.sw_serial),
																			new String(super.hw_version),
																			new String(super.sw_version),
																			new String(super.inventory_number),
																			new String(super.manufacturer),
																			new String(super.manufacturer_code),
																			new String(super.supplier),
																			new String(super.supplier_code),
																			new String(super.eq_class),
																			(Identifier_Transferable)super.image_id.getTransferable(),
																			char_ids,
																			port_ids,
																			cport_ids,
																			sport_ids,
																			EquipmentSort.EQUIPMENT_SORT_KIS,
																			(Identifier_Transferable)this.mcm_id.getTransferable());
	}

	public Identifier getMCMId() {
		return this.mcm_id;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domain_id,
																						Identifier type_id,
																						String name,
																						String description,
																						String latitude,
																						String longitude,
																						String hw_serial,
																						String sw_serial,
																						String hw_version,
																						String sw_version,
																						String inventory_number,
																						String manufacturer,
																						String manufacturer_code,
																						String supplier,
																						String supplier_code,
																						String eq_class,
																						Identifier image_id,
																						Identifier mcm_id) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												domain_id,
												type_id,
												name,
												description,
												latitude,
												longitude,
												hw_serial,
												sw_serial,
												hw_version,
												sw_version,
												inventory_number,
												manufacturer,
												manufacturer_code,
												supplier,
												supplier_code,
												eq_class,
												image_id);
		this.mcm_id = mcm_id;
	}
}