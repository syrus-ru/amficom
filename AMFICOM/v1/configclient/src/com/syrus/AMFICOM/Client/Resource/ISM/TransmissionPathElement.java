package com.syrus.AMFICOM.Client.Resource.ISM;

import java.io.*;

import com.syrus.AMFICOM.CORBA.ISM.TransmissionPathElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.*;

public class TransmissionPathElement extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public int n;
	public boolean is_cable;
	public String link_id;
	public String thread_id;

	public TransmissionPathElement()
	{
	}

	public TransmissionPathElement(TransmissionPathElement_Transferable transferable)
	{
		n = transferable.n;
		is_cable = transferable.is_cable;
		link_id = transferable.link_id;
		thread_id = transferable.thread_id;
	}

	public Object getTransferable()
	{
		return new TransmissionPathElement_Transferable(n, is_cable, link_id, thread_id);
	}

	public ObjectResourceModel getModel()
	{
		return new TransmissionPathElementModel(this);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeInt(n);
		out.writeBoolean(is_cable);
		out.writeObject(link_id);
		out.writeObject(thread_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		n = in.readInt();
		is_cable = in.readBoolean();
		link_id = (String )in.readObject();
		thread_id = (String )in.readObject();
	}
}

class TransmissionPathElementModel extends ObjectResourceModel
{
	TransmissionPathElement pe;

	public TransmissionPathElementModel(TransmissionPathElement pe)
	{
		this.pe = pe;
	}
	public String getColumnValue(String col_id)
	{
		if(col_id.equals("thread"))
		{
			if(pe.is_cable)
				return pe.thread_id;
			else
				return pe.link_id;
		}
		else
		if(col_id.equals("cable"))
		{
			if(pe.is_cable)
				return pe.link_id;
		}
		return "";
	}
}