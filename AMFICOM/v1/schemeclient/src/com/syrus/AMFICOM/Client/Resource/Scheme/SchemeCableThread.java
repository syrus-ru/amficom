package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.SchemeCableThread_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;

public class SchemeCableThread extends StubResource implements Serializable
{
	public static final String typ = "schemecablethread";
	private static final long serialVersionUID = 02L;
	public SchemeCableThread_Transferable transferable;

	public String id = "";
	public String name = "";
	public String linkTypeId = "";
	public String threadId = "";
	public String cableLinkId = "";

	public Map attributes;

	public SchemeCableThread(SchemeCableThread_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeCableThread(String id)
	{
		this.id = id;
		transferable = new SchemeCableThread_Transferable();
		attributes = new HashMap();
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

	public String getPropertyPaneClassName()
	{
		return "";
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		linkTypeId = transferable.linkTypeId;
		threadId = transferable.threadId;
		cableLinkId = transferable.cableLinkId;

		attributes = new HashMap(transferable.attributes.length);
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.linkTypeId = linkTypeId;
		transferable.threadId = threadId;
		transferable.cableLinkId = cableLinkId;

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
		thread.linkTypeId = linkTypeId;
		thread.threadId = threadId;

		thread.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put("clonedids", id, thread.id);
		return thread;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(linkTypeId);
		out.writeObject(threadId);
		out.writeObject(cableLinkId);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		linkTypeId = (String )in.readObject();
		threadId = (String )in.readObject();
		cableLinkId = (String )in.readObject();
		attributes = (Map )in.readObject();

		transferable = new SchemeCableThread_Transferable();
	}
}

