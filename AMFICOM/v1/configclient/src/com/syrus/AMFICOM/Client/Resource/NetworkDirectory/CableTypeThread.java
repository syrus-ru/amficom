package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.*;

public class CableTypeThread extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "cabletypethread";
	CableTypeThread_Transferable transferable;
	
	public String id = "";
	public String name = "";
	public String color = "";
	public String mark = "";
	public String link_type_id = "";

	public Hashtable characteristics = new Hashtable();

	public CableTypeThread()
	{
		transferable = new CableTypeThread_Transferable();
	}
	
	public CableTypeThread(CableTypeThread_Transferable tr)
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
		link_type_id = transferable.link_type_id;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.color = color;
		transferable.mark = mark;
		transferable.link_type_id = link_type_id;
		transferable.characteristics = new Characteristic_Transferable[0];
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
		out.writeObject(link_type_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		color = (String )in.readObject();
		mark = (String )in.readObject();
		link_type_id = (String )in.readObject();

//		characteristics = new Hashtable();
//		transferable = new CableTypeThread_Transferable();

		transferable.characteristics = new Characteristic_Transferable[0];
	}
}

