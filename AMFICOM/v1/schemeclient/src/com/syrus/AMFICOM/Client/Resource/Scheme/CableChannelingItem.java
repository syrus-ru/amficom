package com.syrus.AMFICOM.Client.Resource.Scheme;

import com.syrus.AMFICOM.CORBA.Scheme.CableChannelingItem_Transferable;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

public class CableChannelingItem extends StubResource implements Serializable
{
	public static final String typ = "cablechannelingitem";
	private static final long serialVersionUID = 01L;
	public CableChannelingItem_Transferable transferable;

	protected String id;
	public int n = 0;
	public String startSiteId = "";
	public double startSpare = 0;
	public String physicalLinkId = "";
	public double endSpare = 0;
	public String endSiteId = "";
	
	public int row_x = -1;
	public int place_y = -1;
	

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
		row_x = transferable.row_x;
		place_y = transferable.place_y;
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

		transferable.row_x = row_x;
		transferable.place_y = place_y;
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
		out.writeInt(row_x);
		out.writeInt(place_y);
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
		row_x = in.readInt();
		place_y = in.readInt();
	}
}