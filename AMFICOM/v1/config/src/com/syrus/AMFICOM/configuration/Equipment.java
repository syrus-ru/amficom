/*
 * $Id: Equipment.java,v 1.54 2005/01/17 11:49:37 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;

/**
 * @version $Revision: 1.54 $, $Date: 2005/01/17 11:49:37 $
 * @author $Author: stas $
 * @module config_v1
 */

public class Equipment extends MonitoredDomainMember implements Characterized, TypedObject {

	private static final long serialVersionUID = -6115401698444070841L;

	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_EQUIPMENT_TYPE = "type";
	public static final String COLUMN_IMAGE_ID = "imageId";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_SUPPLIER = "supplier";
	public static final String COLUMN_SUPPLIER_CODE = "supplierCode";
	public static final String COLUMN_HW_SERIAL = "hwSerial";
	public static final String COLUMN_HW_VERSION = "hwVersion";
	public static final String COLUMN_SW_SERIAL = "swSerial";
	public static final String COLUMN_SW_VERSION = "swVersion";
	public static final String COLUMN_INVENTORY_NUMBER = "inventoryNumber";
	public static final String COLUMN_PORT_IDS = "portIds";
	public static final String COLUMN_CHARACTERISTICS = "characteristics";
	private static Object[][] exportColumns = null;

	private EquipmentType          type;
	private String                 name;
	private String                 description;
	private Identifier             imageId;
	private float                  longitude;
	private float                  latitude;
	private String                 supplier;
	private String                 supplierCode;
	private String                 hwSerial;
	private String                 hwVersion;
	private String                 swSerial;
	private String                 swVersion;
	private String                 inventoryNumber;

	private List portIds;
	private List characteristics;

	private StorableObjectDatabase equipmentDatabase;

	public Equipment(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.portIds = new LinkedList();
		this.characteristics = new LinkedList();
		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
		try {
			this.equipmentDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Equipment(Equipment_Transferable et) throws CreateObjectException {
		super(et.header,
				new Identifier(et.domain_id));

		super.monitoredElementIds = new ArrayList(et.monitored_element_ids.length);
		for (int i = 0; i < et.monitored_element_ids.length; i++)
			super.monitoredElementIds.add(new Identifier(et.monitored_element_ids[i]));

		try {
			this.type = (EquipmentType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(et.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.name = new String(et.name);
		this.description = new String(et.description);
		this.imageId = new Identifier(et.image_id);
		this.supplier = new String(et.supplier);
		this.supplier = new String(et.supplierCode);
		this.longitude = et.longitude;
		this.latitude = et.latitude;
		this.hwSerial = et.hwSerial;
		this.hwVersion = et.hwVersion;
		this.swSerial = et.swSerial;
		this.swVersion = et.swVersion;
		this.inventoryNumber = et.inventoryNumber;

		this.portIds = new ArrayList(et.port_ids.length);
		for (int i = 0; i < et.port_ids.length; i++)
			this.portIds.add(new Identifier(et.port_ids[i]));

		try {
			this.characteristics = new ArrayList(et.characteristic_ids.length);
			for (int i = 0; i < et.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(et.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
	}

	protected Equipment(Identifier id,
										Identifier creatorId,
										Identifier domainId,
										EquipmentType type,
										String name,
										String description,
										Identifier imageId,
										String supplier,
										String supplierCode,
										float longitude,
										float latitude,
										String hwSerial,
										String hwVersion,
										String swSerial,
										String swVersion,
										String inventoryNumber) {
				super(id,
							new Date(System.currentTimeMillis()),
							new Date(System.currentTimeMillis()),
							creatorId,
							creatorId,
							domainId);

				super.monitoredElementIds = new LinkedList();

				this.type = type;
				this.name = name;
				this.description = description;
				this.imageId = imageId;
				this.supplier = supplier;
				this.supplierCode = supplierCode;
				this.longitude = longitude;
				this.latitude = latitude;
				this.hwSerial = hwSerial;
				this.hwVersion = hwVersion;
				this.swSerial = swSerial;
				this.swVersion = swVersion;
				this.inventoryNumber = inventoryNumber;

				this.portIds = new LinkedList();

				this.characteristics = new LinkedList();

				super.currentVersion = super.getNextVersion();

				this.equipmentDatabase = ConfigurationDatabaseContext.equipmentDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param type
	 * @param name
	 * @param description
	 * @param imageId
	 * @throws CreateObjectException
	 */
	public static Equipment createInstance(Identifier creatorId,
														 Identifier domainId,
														 EquipmentType type,
														 String name,
														 String description,
														 Identifier imageId,
														 String supplier,
														 String supplierCode,
														 float longitude,
														 float latitude,
														 String hwSerial,
														 String hwVersion,
														 String swSerial,
														 String swVersion,
														 String inventoryNumber) throws CreateObjectException {
		if (creatorId == null || domainId == null || type == null || name == null
								|| description == null || imageId == null || supplier == null || supplierCode == null
								|| hwSerial == null || hwVersion == null || swSerial == null || swVersion == null
								|| inventoryNumber == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			return new Equipment(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENT_ENTITY_CODE),
											creatorId,
											domainId,
											type,
											name,
											description,
											imageId,
											supplier,
											supplierCode,
											longitude,
											latitude,
											hwSerial,
											hwVersion,
											swSerial,
											swVersion,
											inventoryNumber);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Equipment.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.equipmentDatabase != null)
				this.equipmentDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		int i = 0;

		Identifier_Transferable[] meIds = new Identifier_Transferable[super.monitoredElementIds.size()];
		for (Iterator iterator = super.monitoredElementIds.iterator(); iterator.hasNext();)
			meIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		i = 0;
		Identifier_Transferable[] pIds = new Identifier_Transferable[this.portIds.size()];
		for (Iterator iterator = this.portIds.iterator(); iterator.hasNext();)
			pIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		return new Equipment_Transferable(super.getHeaderTransferable(),
										(Identifier_Transferable)this.getDomainId().getTransferable(),
										meIds,
										(Identifier_Transferable)this.type.getId().getTransferable(),
										new String(this.name),
										new String(this.description),
										new String(this.supplier),
										new String(this.supplierCode),
										this.longitude,
										this.latitude,
										new String(this.hwSerial),
										new String(this.hwVersion),
										new String(this.swSerial),
										new String(this.swVersion),
										new String(this.inventoryNumber),
										(Identifier_Transferable)this.imageId.getTransferable(),
										pIds,
										charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public List getPortIds() {
		return Collections.unmodifiableList(this.portIds);
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}

	protected synchronized void setAttributes(Date created,
																	Date modified,
																	Identifier creatorId,
																	Identifier modifierId,
																	Identifier domainId,
																	EquipmentType type,
																	String name,
																	String description,
																	Identifier imageId,
																	String supplier,
																	String supplierCode,
																	float longitude,
																	float latitude,
																	String hwSerial,
																	String hwVersion,
																	String swSerial,
																	String swVersion,
																	String inventoryNumber) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							domainId);
		this.type = type;
		this.name = name;
		this.description = description;
		this.imageId = imageId;
		this.supplier = supplier;
		this.supplierCode = supplierCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.hwSerial = hwSerial;
		this.hwVersion = hwVersion;
		this.swSerial = swSerial;
		this.swVersion = swVersion;
		this.inventoryNumber = inventoryNumber;
	}

	protected synchronized void setPortIds0(final List portIds) {
		this.portIds.clear();
		if (portIds != null)
			this.portIds.addAll(portIds);
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.monitoredElementIds);
		dependencies.add(this.portIds);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getHwSerial() {
		return this.hwSerial;
	}

	public void setHwSerial(String hwSerial) {
		this.hwSerial = hwSerial;
	}

	public String getHwVersion() {
		return this.hwVersion;
	}

	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}

	public String getInventoryNumber() {
		return this.inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSwSerial() {
		return this.swSerial;
	}

	public void setSwSerial(String swSerial) {
		this.swSerial = swSerial;
	}

	public String getSwVersion() {
		return this.swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}

	public Object[][] exportColumns() {
		if (exportColumns == null) {
			exportColumns = new Object[16][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_EQUIPMENT_TYPE;
			exportColumns[4][0] = COLUMN_IMAGE_ID;
			exportColumns[5][0] = COLUMN_LONGITUDE;
			exportColumns[6][0] = COLUMN_LATITUDE;
			exportColumns[7][0] = COLUMN_SUPPLIER;
			exportColumns[8][0] = COLUMN_SUPPLIER_CODE;
			exportColumns[9][0] = COLUMN_HW_SERIAL;
			exportColumns[10][0] = COLUMN_HW_VERSION;
			exportColumns[11][0] = COLUMN_SW_SERIAL;
			exportColumns[12][0] = COLUMN_SW_VERSION;
			exportColumns[13][0] = COLUMN_INVENTORY_NUMBER;
			exportColumns[14][0] = COLUMN_PORT_IDS;
			exportColumns[15][0] = COLUMN_CHARACTERISTICS;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getType().getId();
		exportColumns[4][1] = getImageId();
		exportColumns[5][1] = String.valueOf(getLatitude());
		exportColumns[6][1] = String.valueOf(getLatitude());
		exportColumns[7][1] = getSupplier();
		exportColumns[8][1] = getSupplierCode();
		exportColumns[9][1] = getHwSerial();
		exportColumns[10][1] = getHwVersion();
		exportColumns[11][1] = getSwSerial();
		exportColumns[12][1] = getSwVersion();
		exportColumns[13][1] = getInventoryNumber();
		exportColumns[14][1] = getPortIds();
		List characteristics = new ArrayList(getCharacteristics().size());
		for (Iterator it = getCharacteristics().iterator(); it.hasNext(); ) {
			Characteristic ch = (Characteristic)it.next();
			characteristics.add(ch.exportColumns());
		}
		exportColumns[15][1] = characteristics;

		return exportColumns;
	}
}
