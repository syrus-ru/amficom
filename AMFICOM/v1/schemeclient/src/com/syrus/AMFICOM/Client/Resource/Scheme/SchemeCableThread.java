package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SchemeCableThread extends StubResource implements Serializable
{
	public static final String typ = "schemecablethread";
	private static final long serialVersionUID = 01L;
	public SchemeCableThread_Transferable transferable;

	public String id = "";
	public String name = "";
	public String link_type_id = "";
	public String thread_id = "";

	public Map attributes = new HashMap();

	public SchemeCableThread(SchemeCableThread_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeCableThread(String id)
	{
		this.id = id;
		transferable = new SchemeCableThread_Transferable();
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

	public Object getTransferable()
	{
		return transferable;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		link_type_id = transferable.link_type_id;
		thread_id = transferable.thread_id;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.link_type_id = link_type_id;
		transferable.thread_id = thread_id;

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute)it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object clone(DataSourceInterface dataSource)
	{
		SchemeCableThread thread = new SchemeCableThread(dataSource.GetUId(SchemeCableThread.typ));

		thread.name = name;
		thread.link_type_id = link_type_id;
		thread.thread_id = thread_id;

		thread.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put("clonedids", id, thread.id);
		return thread;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(link_type_id);
		out.writeObject(thread_id);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		link_type_id = (String )in.readObject();
		thread_id = (String )in.readObject();
		attributes = (Hashtable )in.readObject();

		transferable = new SchemeCableThread_Transferable();
	}
}

