package com.syrus.AMFICOM.Client.Resource.ISM;

import com.syrus.AMFICOM.CORBA.General.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.ISM.TransmissionPath_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TransmissionPath extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "path";

	public TransmissionPath_Transferable transferable;

	public String id = "";
	public String name = "";
	public String domainId = "";

	public long modified;

	public Map characteristics = new HashMap();

	public TransmissionPath()
	{
		transferable = new TransmissionPath_Transferable();
	}

	public TransmissionPath(
			String id,
			String name)
	{
		this.id = id;
		this.name = name;

		transferable = new TransmissionPath_Transferable();
	}

	public TransmissionPath(TransmissionPath_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		domainId = transferable.domainId;
		modified = transferable.modified;

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.domainId = domainId;

		transferable.modified = modified;

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

	public long getModified()
	{
		return modified;
	}

	public void updateLocalFromTransferable()
	{
/*
		links = new Vector();
		for(int i = 0; i < link_ids.size(); i++)
		{
			Link link = (Link )Pool.get("link", (String )link_ids.get(i));
			links.add(link);
		}
*/
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new TransmissionPathModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new TransmissionPathDisplayModel();
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Configure.UI.TransmissionPathPane";
	}

/*
	public List sortPorts()
	{
		ArrayList vec = new ArrayList();
		AccessPort ap;
		ObjectResource endport;
		ObjectResource startport;
		Equipment cur_eq;
		ObjectResource bufendport;
		ObjectResource bufstartport;
		TransmissionPathElement tpe;


		ap = (AccessPort )Pool.get(AccessPort.typ, this.access_port_id);
		cur_eq = (Equipment )Pool.get("kisequipment", ap.KIS_id);

		TransmissionPathElement []pe =
				(TransmissionPathElement[])links.toArray(new TransmissionPathElement[links.size()]);
		TransmissionPathElement []tmp = new TransmissionPathElement[pe.length];

		for(int i = 0; i < pe.length; i++)
			tmp[pe[i].n] = pe[i];
		pe = tmp;

		for(int i = 0; i < pe.length; i++) // count through all of the Path Elements
		{
			tpe = pe[i];
			if(tpe.thread_id == null || tpe.thread_id.equals(""))
			{
				Link link = (Link )Pool.get(Link.typ, tpe.link_id);
				bufstartport = (ObjectResource )Pool.get(Port.typ, link.start_port_id);
				bufendport = (ObjectResource )Pool.get(Port.typ, link.end_port_id);

				if(((Port )bufstartport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufstartport;
					endport = bufendport;
				}
				else
				if(((Port )bufendport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufendport;
					endport = bufstartport;
				}
				else
					return new ArrayList();

				cur_eq = (Equipment )Pool.get("kisequipment", ((Port )endport).equipment_id);
			}
			else
			{
				CableLink link = (CableLink )Pool.get(CableLink.typ, tpe.link_id);
				bufstartport = (ObjectResource )Pool.get(CablePort.typ, link.start_port_id);
				bufendport = (ObjectResource )Pool.get(CablePort.typ, link.end_port_id);

				if(((CablePort )bufstartport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufstartport;
					endport = bufendport;
				}
				else
				if(((CablePort )bufendport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufendport;
					endport = bufstartport;
				}
				else
					return new ArrayList();

				cur_eq = (Equipment )Pool.get("kisequipment", ((CablePort )endport).equipment_id);
			}
			vec.add(startport);
			vec.add(endport);
		}
		return vec;
	}
*/
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(domainId);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		domainId = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		transferable = new TransmissionPath_Transferable();
		updateLocalFromTransferable();
	}
}