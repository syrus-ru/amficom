package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;

import com.syrus.AMFICOM.CORBA.Scheme.CableChannelingItem_Transferable;
import com.syrus.AMFICOM.Client.Resource.StubResource;

public class CableChannelingItem extends StubResource implements Serializable
{
	public static final String typ = "cablechannelingitem";
	private static final long serialVersionUID = 01L;
	public CableChannelingItem_Transferable transferable;

	public String id;
	public int n = 0;
	public String startSiteId = "";
	public double startSpare = 0;
	public String physicalLinkId = "";
	public double endSpare = 0;
	public String endSiteId = "";

	public CableChannelingItem(String id)
	{
		this.id = id;
		transferable = new CableChannelingItem_Transferable();
	}

	public CableChannelingItem(CableChannelingItem_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public String getTyp()
	{
		return typ;
	}

	public String getId()
	{
		return id;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		n = transferable.n;
		startSiteId = transferable.startSiteId;
		startSpare = transferable.startSpare;
		physicalLinkId = transferable.physicalLinkId;
		endSpare = transferable.endSpare;
		endSiteId = transferable.endSiteId;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.n = n;
		transferable.startSiteId = startSiteId;
		transferable.startSpare = startSpare;
		transferable.physicalLinkId = physicalLinkId;
		transferable.endSpare = endSpare;
		transferable.endSiteId = endSiteId;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeInt(n);
		out.writeObject(startSiteId);
		out.writeDouble(startSpare);
		out.writeObject(physicalLinkId);
		out.writeDouble(endSpare);
		out.writeObject(endSiteId);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		id = (String) in.readObject();
		n = in.readInt();
		startSiteId = (String) in.readObject();
		startSpare = in.readDouble();
		physicalLinkId = (String) in.readObject();
		endSpare = in.readDouble();
		endSiteId = (String) in.readObject();
	}
}