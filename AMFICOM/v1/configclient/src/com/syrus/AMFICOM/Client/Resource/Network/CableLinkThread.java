package com.syrus.AMFICOM.Client.Resource.Network;

import java.io.*;

import com.syrus.AMFICOM.CORBA.Network.CableLinkThread_Transferable;
import com.syrus.AMFICOM.Client.Resource.StubResource;

public class CableLinkThread extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "cablelinkthread";
	CableLinkThread_Transferable transferable;

	public String id = "";
	public String name = "";
	public String color = "";
	public String mark = "";
	public String linkTypeId = "";

	public CableLinkThread()
	{
		transferable = new CableLinkThread_Transferable();
	}

	public CableLinkThread(CableLinkThread_Transferable tr)
	{
		transferable = tr;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		color = transferable.color;
		mark = transferable.mark;
		linkTypeId = transferable.linkTypeId;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.color = color;
		transferable.mark = mark;
		transferable.linkTypeId = linkTypeId;
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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(color);
		out.writeObject(mark);
		out.writeObject(linkTypeId);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		color = (String )in.readObject();
		mark = (String )in.readObject();
		linkTypeId = (String )in.readObject();

		transferable = new CableLinkThread_Transferable();
	}
}
