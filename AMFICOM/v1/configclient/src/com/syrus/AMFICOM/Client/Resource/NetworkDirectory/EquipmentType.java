package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.EquipmentType_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class EquipmentType extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "equipmenttype";

	public EquipmentType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String eqClass = "";
	public String description = "";
	public String manufacturer = "";
	public String manufacturerCode = "";
	public String imageId = "";
	public long modified = 0;
//	public transient boolean is_modified = false;

	public Map characteristics = new HashMap();

	public EquipmentType()
	{
		transferable = new EquipmentType_Transferable();
	}

	public EquipmentType(EquipmentType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public EquipmentType(
			String id,
			String name,
			String codename,
			String eqClass,
			String description)
	{
		this.id = id;
		this.name = name;
		this.eqClass = eqClass;
		this.description = description;

		transferable = new EquipmentType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		eqClass = transferable.eqClass;
		description = transferable.description;

		manufacturer = transferable.manufacturer;
//		manufacturerCode = transferable.manufacturer_code;

		imageId = transferable.imageId;
		modified = transferable.modified;

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.eqClass = eqClass;
		transferable.description = description;

		transferable.manufacturer = manufacturer;
//		transferable.manufacturer = manufacturerCode;

		transferable.imageId = imageId;
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
		return "sysdomain";
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public long getModified()
	{
		return modified;
	}

	public ObjectResourceModel getModel()
	{
		return new EquipmentTypeModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new EquipmentTypeDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.EquipmentTypePane";
	}

	public Object clone()
	{
		EquipmentType eqt = new EquipmentType();
//		eqt.is_modified = true;

		eqt.transferable = new EquipmentType_Transferable();

		eqt.id = id;
		eqt.name = name;
		eqt.eqClass = eqClass;
		eqt.description = description;
		eqt.manufacturer = manufacturer;
		eqt.manufacturerCode = manufacturerCode;
		eqt.imageId =  imageId;
		eqt.modified = modified;

		eqt.characteristics = new Hashtable(characteristics.size());

		return eqt;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(eqClass);
		out.writeObject(description);
		out.writeObject(manufacturer);
		out.writeObject(manufacturerCode);
		out.writeObject(imageId);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		eqClass = (String )in.readObject();
		description = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturerCode = (String )in.readObject();
		imageId = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		transferable = new EquipmentType_Transferable();
	}
}
