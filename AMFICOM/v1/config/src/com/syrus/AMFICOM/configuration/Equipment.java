package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;

public abstract class Equipment extends DomainMember implements Characterized, TypedObject {
	Identifier type_id;
	String name;
	String description;
	String latitude;
	String longitude;
	String hw_serial;
	String sw_serial;
	String hw_version;
	String sw_version;
	String inventory_number;
	String manufacturer;
	String manufacturer_code;
	String supplier;
	String supplier_code;
	String eq_class;
	Identifier image_id;

	ArrayList characteristic_ids;
	ArrayList port_ids;
	ArrayList cable_port_ids;
	ArrayList special_port_ids;

	Equipment(Identifier id) throws RetrieveObjectException {
		super(id);
	}

	Equipment(Equipment_Transferable eq) throws CreateObjectException {
		super(new Identifier(eq.id),
					new Date(eq.created),
					new Date(eq.modified),
					new Identifier(eq.creator_id),
					new Identifier(eq.modifier_id),
					new Identifier(eq.domain_id));
		this.type_id = new Identifier(eq.type_id);
		this.name = new String(eq.name);
		this.description = new String(eq.description);
		this.latitude = new String(eq.latitude);
		this.longitude = new String(eq.longitude);
		this.hw_serial = new String(eq.hw_serial);
		this.sw_serial = new String(eq.sw_serial);
		this.hw_version = new String(eq.hw_version);
		this.sw_version = new String(eq.sw_version);
		this.inventory_number = new String(eq.inventory_nr);
		this.manufacturer = new String(eq.manufacturer);
		this.manufacturer_code = new String(eq.manufacturer_code);
		this.supplier = new String(eq.supplier);
		this.supplier_code = new String(eq.supplier_code);
		this.eq_class = new String(eq.eq_class);
		this.image_id = new Identifier(eq.image_id);

		this.characteristic_ids = new ArrayList(eq.characteristic_ids.length);
		for (int i = 0; i < eq.characteristic_ids.length; i++)
			this.characteristic_ids.add(new Identifier(eq.characteristic_ids[i]));

		this.port_ids = new ArrayList(eq.port_ids.length);
		for (int i = 0; i < eq.port_ids.length; i++)
			this.port_ids.add(new Identifier(eq.port_ids[i]));

		this.cable_port_ids = new ArrayList(eq.cable_port_ids.length);
		for (int i = 0; i < eq.cable_port_ids.length; i++)
			this.cable_port_ids.add(new Identifier(eq.cable_port_ids[i]));

		this.special_port_ids = new ArrayList(eq.special_port_ids.length);
		for (int i = 0; i < eq.special_port_ids.length; i++)
			this.special_port_ids.add(new Identifier(eq.special_port_ids[i]));
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

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public String getHWSerial() {
		return this.hw_serial;
	}

	public String getSWSerial() {
		return this.sw_serial;
	}

	public String getHWVersion() {
		return this.hw_version;
	}

	public String getSWVersion() {
		return this.sw_version;
	}

	public String getInventoryNumber() {
		return this.inventory_number;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public String getManufacturerCode() {
		return this.manufacturer_code;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public String getSupplierCode() {
		return this.supplier_code;
	}

	public String getEqClass() {
		return this.eq_class;
	}

	public ArrayList getCharacteristicIds() {
		return this.characteristic_ids;
	}

	public ArrayList getPortIds() {
		return this.port_ids;
	}

	public ArrayList getCablePortIds() {
		return this.cable_port_ids;
	}

	public ArrayList getSpecialPortIds() {
		return this.special_port_ids;
	}

	public void setCharacteristicIds(ArrayList characteristic_ids) {
		this.characteristic_ids = characteristic_ids;
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
																						Identifier image_id) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												domain_id);
		this.type_id = type_id;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hw_serial = hw_serial;
		this.sw_serial = sw_serial;
		this.hw_version = hw_version;
		this.sw_version = sw_version;
		this.inventory_number = inventory_number;
		this.manufacturer = manufacturer;
		this.manufacturer_code = manufacturer_code;
		this.supplier = supplier;
		this.supplier_code = supplier_code;
		this.eq_class = eq_class;
		this.image_id = image_id;
	}
}