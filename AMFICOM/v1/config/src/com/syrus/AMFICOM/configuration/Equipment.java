package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;

public abstract class Equipment extends DomainMember {
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
	Identifier image_id;

	ArrayList port_ids;
	ArrayList cable_port_ids;
	ArrayList special_port_ids;

	public Equipment(Identifier id) {
		super(id);
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

	public ArrayList getPortIds() {
		return this.port_ids;
	}

	public ArrayList getCablePortIds() {
		return this.cable_port_ids;
	}

	public ArrayList getSchemePortIds() {
		return this.scheme_port_ids;
	}
}