package com.syrus.AMFICOM.Client.Resource.SchemeDirectory;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class ProtoElement extends ObjectResource implements Serializable
{
	public static final String typ = "proto";
	private static final long serialVersionUID = 01L;
	SchemeProtoElement_Transferable transferable;

	public String id = "";
	public String name = "";
	public String equipment_type_id = "";
	public long modified = 0;
	public String domain_id = "";
	public String symbol_id = "";
	public String label = "";

	public Hashtable attributes = new Hashtable();
	public Vector devices = new Vector();
	public Vector links = new Vector();
	public Vector protoelement_ids = new Vector();

	public Serializable serializable_cell;
	public byte[] schemecell;
	public Serializable serializable_ugo;
	public byte[] ugo;

//	public transient boolean isKis = false;
	public transient MapProtoElement map_proto;
	//public transient boolean extended_state = true;

	public ProtoElement(SchemeProtoElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public ProtoElement(String id)
	{
		this.id = id;
		transferable = new SchemeProtoElement_Transferable();
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
		return domain_id;
	}

	public ObjectResourceModel getModel()
	{
		return new ProtoElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] { "name" }, new String[] { "name" });
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		equipment_type_id = transferable.equipment_type_id;
		modified = transferable.modified;
		domain_id = transferable.domain_id;
		symbol_id = transferable.symbol_id;
		label = transferable.label;

		devices = new Vector();
		links = new Vector();
		protoelement_ids = new Vector();

		for (int i = 0; i < transferable.devices.length; i++)
			devices.add(new SchemeDevice(transferable.devices[i]));
		for (int i = 0; i < transferable.links.length; i++)
			links.add(new SchemeLink(transferable.links[i]));
		for (int i = 0; i < transferable.proto_element_ids.length; i++)
			protoelement_ids.add(transferable.proto_element_ids[i]);

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		schemecell = transferable.schemecell;
		ugo = transferable.ugocell;
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.equipment_type_id = equipment_type_id;
		transferable.modified = modified;
		transferable.domain_id = domain_id;
		transferable.symbol_id = symbol_id;
		transferable.label = label;

		transferable.devices = new SchemeDevice_Transferable[devices.size()];
		transferable.links = new SchemeLink_Transferable[links.size()];
		transferable.proto_element_ids = new String[protoelement_ids.size()];

		for (int i=0; i<transferable.devices.length; i++)
		{
			SchemeDevice device = (SchemeDevice)devices.get(i);
			device.setTransferableFromLocal();
			transferable.devices[i] = (SchemeDevice_Transferable)device.getTransferable();
		}
		for (int i=0; i<transferable.links.length; i++)
		{
			SchemeLink link = (SchemeLink)links.get(i);
			link.setTransferableFromLocal();
			transferable.links[i] = (SchemeLink_Transferable)link.getTransferable();
		}
		for (int i=0; i<transferable.proto_element_ids.length; i++)
			transferable.proto_element_ids[i] = (String)protoelement_ids.get(i);

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Enumeration e = attributes.elements(); e.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute)e.nextElement();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}

		transferable.schemecell = schemecell;
		transferable.ugocell = ugo;
	}

	public void updateLocalFromTransferable()
	{
		for (int i = 0; i < devices.size(); i++)
		{
			SchemeDevice dev = (SchemeDevice)devices.get(i);
			dev.updateLocalFromTransferable();
			Pool.put(SchemeDevice.typ, dev.getId(), dev);
		}
		for (int i = 0; i < links.size(); i++)
		{
			SchemeLink link = (SchemeLink)links.get(i);
			link.updateLocalFromTransferable();
			Pool.put(SchemeLink.typ, link.getId(), link);
		}
	}

	public long getModified()
	{
		return modified;
	}

	public void pack()
	{
		schemecell = pack (serializable_cell);
		ugo = pack (serializable_ugo);
	}

	private byte[] pack(Serializable cell)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream zos = new GZIPOutputStream(baos);
			ObjectOutputStream out = new ObjectOutputStream(zos);
			out.writeObject(cell);
			out.flush();
			out.close();
			byte[] b = baos.toByteArray();
			return b;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void unpack()
	{
		if (serializable_cell == null)
			serializable_cell = unpack(schemecell);
		if (serializable_ugo == null)
			serializable_ugo = unpack(ugo);
	}

	public Serializable unpack(byte[] b)
	{
		Serializable cell;
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			GZIPInputStream zis = new GZIPInputStream(bais);
			ObjectInputStream in = new ObjectInputStream(zis);
			Object obj = in.readObject();
			in.close();
			cell = (Serializable)obj;
			return cell;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(equipment_type_id);
		out.writeLong(modified);
		out.writeObject(symbol_id);
		out.writeObject(domain_id);
		out.writeObject(label);
		out.writeObject(devices);
		out.writeObject(links);
		out.writeObject(protoelement_ids);
		out.writeObject(attributes);

		Object ob = schemecell;
		out.writeObject(ob);
		ob = ugo;
		out.writeObject(ob);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		equipment_type_id = (String )in.readObject();
		modified = in.readLong();
		symbol_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		label = (String )in.readObject();
		devices = (Vector )in.readObject();
		links = (Vector )in.readObject();
		protoelement_ids = (Vector )in.readObject();
		attributes = (Hashtable )in.readObject();

		Object ob = in.readObject();
		schemecell = (byte[] )ob;
		ob = in.readObject();
		ugo = (byte[] )ob;

		transferable = new SchemeProtoElement_Transferable();
		updateLocalFromTransferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			ProtoElement cloned = (ProtoElement)Pool.get(ProtoElement.typ, cloned_id);
			if (cloned == null)
				System.err.println("ProtoElement.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, equipment_type_id);

		EquipmentType new_eqt = eqt;
//    EquipmentType new_eqt = (EquipmentType)eqt.clone();
// 		new_eqt.characteristics = ResourceUtil.copyCharacteristics(dataSource, eqt.characteristics);
//		new_eqt.id = dataSource.GetUId(EquipmentType.typ);
//		Pool.put(EquipmentType.typ, new_eqt.getId(), new_eqt);

		ProtoElement proto = new ProtoElement(dataSource.GetUId(ProtoElement.typ));
		proto.modified = modified;
		proto.name = name;
		proto.equipment_type_id = new_eqt.getId();
		proto.map_proto = map_proto;
		proto.label = label;
		proto.symbol_id = symbol_id;
		proto.domain_id = domain_id;

		proto.protoelement_ids = new Vector(protoelement_ids.size());
		for (int i = 0; i < protoelement_ids.size(); i++)
		{
			ProtoElement p = ((ProtoElement)Pool.get(ProtoElement.typ, (String)protoelement_ids.get(i)));
			ProtoElement p1 = (ProtoElement)p.clone(dataSource);
			proto.protoelement_ids.add(p1.getId());
		}

		proto.devices = new Vector(devices.size());
		for (int i = 0; i < devices.size(); i++)
			proto.devices.add(((SchemeDevice)devices.get(i)).clone(dataSource));

		proto.links = new Vector(links.size());
		for (int i = 0; i < links.size(); i++)
			proto.links.add(((SchemeLink)links.get(i)).clone(dataSource));

		proto.schemecell = new byte[schemecell.length];
		for (int i = 0; i < schemecell.length; i++)
			proto.schemecell[i] = schemecell[i];
		proto.ugo = new byte[ugo.length];
		for (int i = 0; i < ugo.length; i++)
			proto.ugo[i] = ugo[i];

		proto.unpack();

		proto.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(ProtoElement.typ, proto.getId(), proto);
		Pool.put("clonedids", id, proto.id);
		return proto;
	}
}

class ProtoElementModel extends ObjectResourceModel
{
	ProtoElement proto;

	public ProtoElementModel(ProtoElement proto)
	{
		this.proto = proto;
	}

	public String getColumnValue(String col_id)
	{
		String s = "";
		try
		{
			if(col_id.equals("name"))
			{
				s = proto.getName();
			}
			if(col_id.equals("id"))
			{
				s = proto.getId();
			}
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - ProtoElement");
			s = "";
		}
		return s;
	}

	public void setColumnValue(String col_id, Object obj)
	{
	}
}
