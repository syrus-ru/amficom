package com.syrus.AMFICOM.Client.Resource.Network;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.Network.Equipment_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Equipment extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "equipment";

	public Equipment_Transferable transferable;

	public String id = ""; //
	public String name = "";//
	public String description = "";//
	public String typeId = "";//

	public String longitude = "0.0";
	public String latitude = "0.0";

	public String hwSerial = "";
	public String swSerial = "";
	public String hwVersion = "";
	public String swVersion = "";
	public String inventoryNr = "";
	public String manufacturer = "";
	public String manufacturerCode = "";
	public String supplier = "";
	public String supplierCode = "";
	public String constructor = "";
	public String constructorCode = "";

	public String eqClass = "";
//	public boolean is_kis;
	public String domainId = "";
	public long modified;

	public Map characteristics = new HashMap();

	public Equipment()
	{
		transferable = new Equipment_Transferable();
	}

	public Equipment(Equipment_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Equipment(EquipmentType eq_type)
	{
		id = "new_id";
		name = eq_type.getName();
		typeId = eq_type.getId();

		description = "equipment " + eq_type.getName();
		manufacturer = eq_type.manufacturer;
		eqClass = eq_type.eqClass;

		characteristics = new HashMap();

		transferable = new Equipment_Transferable();
	}

	public Equipment(
			String id,
			String name,
			String typeId,
			String longitude,
			String latitude,

			String hwSerial,
			String swSerial,
			String hwVersion,
			String swVersion,
			String description,
			String inventoryNr,
			String manufacturer,
			String manufacturerCode,
			String supplier,
			String supplierCode,

			Map characteristics)
	{
		this.id = id;
		this.name = name;
		this.typeId = typeId;
		this.longitude = longitude;
		this.latitude = latitude;

		this.hwSerial = hwSerial;
		this.swSerial = swSerial;
		this.hwVersion = hwVersion;
		this.swVersion = swVersion;
		this.description = description;
		this.inventoryNr = inventoryNr;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.supplier = supplier;
		this.supplierCode = supplierCode;

		this.eqClass = eqClass;
		transferable = new Equipment_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		typeId = transferable._typeId;
		longitude = transferable.longitude;
		latitude = transferable.latitude;

		hwSerial = transferable.hwSerial;
		swSerial = transferable.swSerial;
		hwVersion = transferable.hwVersion;
		swVersion = transferable.swVersion;
		description = transferable.description;
		inventoryNr = transferable.inventoryNr;
		manufacturer = transferable.manufacturer;
		manufacturerCode = transferable.manufacturerCode;
		supplier = transferable.supplier;
		supplierCode = transferable.supplierCode;
		constructor = transferable.constructor;
		constructorCode = transferable.constructorCode;

		eqClass = transferable.eqClass;
		domainId = transferable.domainId;

		modified = transferable.modified;

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable._typeId = typeId;
		transferable.longitude = longitude;
		transferable.latitude = latitude;

		transferable.hwSerial = hwSerial;
		transferable.swSerial = swSerial;
		transferable.hwVersion = hwVersion;
		transferable.swVersion = swVersion;
		transferable.description = description;
		transferable.inventoryNr = inventoryNr;
		transferable.manufacturer = manufacturer;
		transferable.manufacturerCode = manufacturerCode;
		transferable.supplier = supplier;
		transferable.supplierCode = supplierCode;
		transferable.constructor = constructor;
		transferable.constructorCode = constructorCode;

		transferable.eqClass = eqClass;
		transferable.domainId = domainId;

		transferable.modified = modified;

		int l = this.characteristics.size();
		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[l];
		for(Iterator it = characteristics.values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			ch.setTransferableFromLocal();
			transferable.characteristics[i++] = ch.transferable;
		}
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return domainId;
	}

	public long getModified()
	{
		return modified;
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new EquipmentModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new EquipmentDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.EquipmentPane";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(typeId);
		out.writeObject(longitude);
		out.writeObject(latitude);
		out.writeObject(hwSerial);
		out.writeObject(swSerial);
		out.writeObject(hwVersion);
		out.writeObject(swVersion);
		out.writeObject(inventoryNr);
		out.writeObject(manufacturer);
		out.writeObject(manufacturerCode);
		out.writeObject(supplier);
		out.writeObject(supplierCode);
		out.writeObject(eqClass);
		out.writeObject(domainId);
		out.writeLong(modified);

		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		typeId = (String )in.readObject();
		longitude = (String )in.readObject();
		latitude = (String )in.readObject();
		hwSerial = (String )in.readObject();
		swSerial = (String )in.readObject();
		hwVersion = (String )in.readObject();
		swVersion = (String )in.readObject();
		inventoryNr = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturerCode = (String )in.readObject();
		supplier = (String )in.readObject();
		supplierCode = (String )in.readObject();
		eqClass = (String )in.readObject();
		domainId = (String )in.readObject();
		modified = in.readLong();

		characteristics = (Map )in.readObject();


		transferable = new Equipment_Transferable();

		updateLocalFromTransferable();
	}
}
