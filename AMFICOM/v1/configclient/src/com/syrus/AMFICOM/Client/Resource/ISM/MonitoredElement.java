package com.syrus.AMFICOM.Client.Resource.ISM;

import java.io.*;

import com.syrus.AMFICOM.CORBA.ISM.MonitoredElement_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;

public class MonitoredElement extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "monitoredelement";

	public MonitoredElement_Transferable transferable;

	public String id = "";
	public String elementName = "";
	public String elementType = "";
	public String elementId = "";
	public String localAddress = "";
	public String measurementPortId = "";
	public String domainId = "";

	public long modified;

	public ObjectResource element;

	public MonitoredElement()
	{
		transferable = new MonitoredElement_Transferable();
	}

	public MonitoredElement(MonitoredElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		elementName = transferable.elementName;
		elementId = transferable.elementId;
		localAddress = transferable.localAddress;
		elementType = transferable.elementType;
		modified = transferable.modified;
		measurementPortId = transferable.measurementPortId;
		domainId = transferable.domainId;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.elementName = elementName;
		transferable.elementId = elementId;
		transferable.elementType = elementType;
		transferable.localAddress = localAddress;
		transferable.modified = modified;
		transferable.measurementPortId = measurementPortId;
		transferable.domainId = domainId;
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return elementName;
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

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MonitoredElementDisplayModel();
	}

	public ObjectResourceModel getModel()
	{
		return new MonitoredElementModel(this);
	}

	public void updateLocalFromTransferable()
	{
		if(elementType.equals("path"))
			element = (ObjectResource )Pool.get(TransmissionPath.typ, elementId);
		else if(elementType.equals("equipment"))
			element = (ObjectResource )Pool.get(Equipment.typ, elementId);
		else if(elementType.equals("port"))
			element = (ObjectResource )Pool.get(Port.typ, elementId);
		else if(elementType.equals("link"))
			element = (ObjectResource )Pool.get(Link.typ, elementId);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(elementName);
		out.writeObject(elementType);
		out.writeObject(elementId);
		out.writeObject(localAddress);
		out.writeLong(modified);
		out.writeObject(measurementPortId);
		out.writeObject(domainId);
		out.writeObject(element);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		elementName = (String )in.readObject();
		elementType = (String )in.readObject();
		elementId = (String )in.readObject();
		localAddress = (String )in.readObject();
		modified = in.readLong();
		measurementPortId = (String )in.readObject();
		domainId = (String )in.readObject();
		element = (ObjectResource )in.readObject();

		transferable = new MonitoredElement_Transferable();
		updateLocalFromTransferable();
	}
}