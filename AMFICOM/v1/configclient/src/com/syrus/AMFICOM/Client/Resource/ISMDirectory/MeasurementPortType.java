package com.syrus.AMFICOM.Client.Resource.ISMDirectory;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.ISMDirectory.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MeasurementPortType extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "accessporttype";

	private MeasurementPortType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String accessType = "";
	public long modified;

	public Collection testTypeIds = new ArrayList();

	public Map characteristics = new HashMap();

	public MeasurementPortType()
	{
		transferable = new MeasurementPortType_Transferable();
	}

	public MeasurementPortType(MeasurementPortType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MeasurementPortType(
			String id,
			String name,
			String description,
			String access_type)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.accessType = access_type;

		transferable = new MeasurementPortType_Transferable();
	}

	public ObjectResourceModel getModel()
	{
		return new MeasurementPortTypeModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MeasurementPortTypeDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.AccessPortTypePane";
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		accessType = transferable.accessType;
		modified = transferable.modified;

		MiscUtil.addToCollection(testTypeIds, transferable.testTypeIds);

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.accessType = accessType;
		transferable.modified = modified;

		transferable.testTypeIds = (String[])testTypeIds.toArray(new String[testTypeIds.size()]);

		int l = this.characteristics.size();
		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[l];
		for(Iterator it = characteristics.values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(accessType);
		out.writeLong(modified);
		out.writeObject(testTypeIds);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		accessType = (String )in.readObject();
		modified = in.readLong();
		testTypeIds = (Collection )in.readObject();
		characteristics = (Map )in.readObject();

		transferable = new MeasurementPortType_Transferable();
	}
}
