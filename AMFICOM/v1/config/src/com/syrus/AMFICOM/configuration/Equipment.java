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
	Identifier typeId;
	String name;
	String description;
	String latitude;
	String longitude;
	String hwSerial;
	String swSerial;
	String hwVersion;
	String swVersion;
	String inventoryNumber;
	String manufacturer;
	String manufacturerCode;
	String supplier;
	String supplierCode;
	String eqClass;
	Identifier imageId;

	ArrayList characteristicIds;
	ArrayList portIds;
	ArrayList cablePortIds;
	ArrayList specialPortIds;

	Equipment(Identifier id) {
		super(id);
	}

	Equipment(Equipment_Transferable et) {
		super(new Identifier(et.id),
					new Date(et.created),
					new Date(et.modified),
					new Identifier(et.creator_id),
					new Identifier(et.modifier_id),
					new Identifier(et.domain_id));
		this.typeId = new Identifier(et.type_id);
		this.name = new String(et.name);
		this.description = new String(et.description);
		this.latitude = new String(et.latitude);
		this.longitude = new String(et.longitude);
		this.hwSerial = new String(et.hw_serial);
		this.swSerial = new String(et.sw_serial);
		this.hwVersion = new String(et.hw_version);
		this.swVersion = new String(et.sw_version);
		this.inventoryNumber = new String(et.inventory_nr);
		this.manufacturer = new String(et.manufacturer);
		this.manufacturerCode = new String(et.manufacturer_code);
		this.supplier = new String(et.supplier);
		this.supplierCode = new String(et.supplier_code);
		this.eqClass = new String(et.eq_class);
		this.imageId = new Identifier(et.image_id);

		this.characteristicIds = new ArrayList(et.characteristic_ids.length);
		for (int i = 0; i < et.characteristic_ids.length; i++)
			this.characteristicIds.add(new Identifier(et.characteristic_ids[i]));

		this.portIds = new ArrayList(et.port_ids.length);
		for (int i = 0; i < et.port_ids.length; i++)
			this.portIds.add(new Identifier(et.port_ids[i]));

		this.cablePortIds = new ArrayList(et.cable_port_ids.length);
		for (int i = 0; i < et.cable_port_ids.length; i++)
			this.cablePortIds.add(new Identifier(et.cable_port_ids[i]));

		this.specialPortIds = new ArrayList(et.special_port_ids.length);
		for (int i = 0; i < et.special_port_ids.length; i++)
			this.specialPortIds.add(new Identifier(et.special_port_ids[i]));
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

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public String getHWSerial() {
		return this.hwSerial;
	}

	public String getSWSerial() {
		return this.swSerial;
	}

	public String getHWVersion() {
		return this.hwVersion;
	}

	public String getSWVersion() {
		return this.swVersion;
	}

	public String getInventoryNumber() {
		return this.inventoryNumber;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public String getEqClass() {
		return this.eqClass;
	}

	public ArrayList getCharacteristicIds() {
		return this.characteristicIds;
	}

	public ArrayList getPortIds() {
		return this.portIds;
	}

	public ArrayList getCablePortIds() {
		return this.cablePortIds;
	}

	public ArrayList getSpecialPortIds() {
		return this.specialPortIds;
	}

	public void setCharacteristicIds(ArrayList characteristicIds) {
		this.characteristicIds = characteristicIds;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creator_id,
																						Identifier modifier_id,
																						Identifier domainId,
																						Identifier typeId,
																						String name,
																						String description,
																						String latitude,
																						String longitude,
																						String hwSerial,
																						String swSerial,
																						String hwVersion,
																						String swVersion,
																						String inventoryNumber,
																						String manufacturer,
																						String manufacturerCode,
																						String supplier,
																						String supplierCode,
																						String eqClass,
																						Identifier imageId) {
		super.setAttributes(created,
												modified,
												creator_id,
												modifier_id,
												domainId);
		this.typeId = typeId;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hwSerial = hwSerial;
		this.swSerial = swSerial;
		this.hwVersion = hwVersion;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.eqClass = eqClass;
		this.imageId = imageId;
	}
}