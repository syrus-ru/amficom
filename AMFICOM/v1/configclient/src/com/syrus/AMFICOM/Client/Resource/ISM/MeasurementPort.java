package com.syrus.AMFICOM.Client.Resource.ISM;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.ISM.MeasurementPort_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MeasurementPort extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "accessport";

	private MeasurementPort_Transferable transferable;

	public String id = "";
	public String name = "";
	public String typeId = "";
	public String portId = "";
	public String kisId = "";
	public String localId = "";
	public String domainId = "";

	public Map characteristics = new HashMap();

	public MeasurementPort()
	{
		transferable = new MeasurementPort_Transferable();
	}

	public MeasurementPort(MeasurementPort_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MeasurementPort(
			String id,
			String name,
			String typeId,
			String portId,
			String KISId,
			String localId)
	{
		this.id = id;
		this.name = name;
		this.typeId = typeId;
		this.portId = portId;
		this.kisId = KISId;
		this.localId = localId;

		transferable = new MeasurementPort_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		typeId = transferable._typeId;
		portId = transferable.portId;
		kisId = transferable.kisId;
		localId = transferable.localId;
		domainId = transferable.domainId;

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable._typeId = typeId;
		transferable.portId = portId;
		transferable.kisId = kisId;
		transferable.localId = localId;
		transferable.domainId = domainId;

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

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new MeasurementPortModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MeasurementPortDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.AccessPortPane";
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(typeId);
		out.writeObject(portId);
		out.writeObject(kisId);
		out.writeObject(localId);
		out.writeObject(domainId);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		typeId = (String )in.readObject();
		portId = (String )in.readObject();
		kisId = (String )in.readObject();
		localId = (String )in.readObject();
		domainId = (String )in.readObject();
		characteristics = (Map)in.readObject();

		transferable = new MeasurementPort_Transferable();
		updateLocalFromTransferable();
	}
}
