package com.syrus.AMFICOM.Client.Resource.SchemeDirectory;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Configure.UI.EquipmentTypePane;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;

import com.syrus.AMFICOM.Client.Schematics.UI.*;

public class ProtoElement extends StubResource
		implements Transferable, Serializable
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

	public Map attributes;
	public Collection devices;
	public Collection links;
	public Collection protoelement_ids;

	public Serializable serializable_cell;
	public byte[] schemecell;
	public Serializable serializable_ugo;
	public byte[] ugo;

	public transient MapProtoElement map_proto;

	public ProtoElement(SchemeProtoElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public ProtoElement(String id)
	{
		this.id = id;
		transferable = new SchemeProtoElement_Transferable();

		devices = new ArrayList();
		links = new ArrayList();
		protoelement_ids = new ArrayList();
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
		//return new StubDisplayModel(new String[] { "name" }, new String[] { "name" });
		return new ProtoElementDisplayModel();
	}

	public static PropertiesPanel getPropertyPane()
	{
		System.out.println("Getting ProtoElement property pane");
		return new EquipmentTypePane();
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

		devices = new ArrayList(transferable.devices.length);
		links = new ArrayList(transferable.links.length);
		protoelement_ids = new ArrayList(transferable.proto_element_ids.length);
		attributes = new HashMap(transferable.attributes.length);

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

		int counter = 0;
		for (Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice device = (SchemeDevice)it.next();
			device.setTransferableFromLocal();
			transferable.devices[counter++] = (SchemeDevice_Transferable)device.getTransferable();
		}
		counter = 0;
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			link.setTransferableFromLocal();
			transferable.links[counter++] = (SchemeLink_Transferable)link.getTransferable();
		}
		counter = 0;
		for (Iterator it = protoelement_ids.iterator(); it.hasNext();)
			transferable.proto_element_ids[counter++] = (String)it.next();

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute)it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}

		transferable.schemecell = schemecell;
		transferable.ugocell = ugo;
	}

	public void updateLocalFromTransferable()
	{
		for (Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			dev.updateLocalFromTransferable();
			Pool.put(SchemeDevice.typ, dev.getId(), dev);
		}
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
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
		devices = (Collection )in.readObject();
		links = (Collection )in.readObject();
		protoelement_ids = (Collection )in.readObject();
		attributes = (Map)in.readObject();

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

//		EquipmentType new_eqt = eqt;
		EquipmentType new_eqt = (EquipmentType)eqt.clone();
		new_eqt.characteristics = ResourceUtil.copyCharacteristics(dataSource, eqt.characteristics);
		new_eqt.id = dataSource.GetUId(EquipmentType.typ);
		Pool.put(EquipmentType.typ, new_eqt.getId(), new_eqt);

		ProtoElement proto = new ProtoElement(dataSource.GetUId(ProtoElement.typ));
		proto.modified = modified;
		proto.name = name;
		proto.equipment_type_id = new_eqt.getId();
		proto.map_proto = map_proto;
		proto.label = label;
		proto.symbol_id = symbol_id;
		proto.domain_id = domain_id;

		for (Iterator it = protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement p = ((ProtoElement)Pool.get(ProtoElement.typ, (String)it.next()));
			ProtoElement p1 = (ProtoElement)p.clone(dataSource);
			proto.protoelement_ids.add(p1.getId());
		}
		for (Iterator it = devices.iterator(); it.hasNext();)
			proto.devices.add(((SchemeDevice)it.next()).clone(dataSource));
		for (Iterator it = links.iterator(); it.hasNext();)
			proto.links.add(((SchemeLink)it.next()).clone(dataSource));

		proto.schemecell = new byte[schemecell.length];
		System.arraycopy(schemecell, 0, proto.schemecell, 0, schemecell.length);
		proto.ugo = new byte[ugo.length];
		System.arraycopy(ugo, 0, proto.ugo, 0, ugo.length);

		proto.unpack();
		proto.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(ProtoElement.typ, proto.getId(), proto);
		Pool.put("clonedids", id, proto.id);
		return proto;
	}

	public Object getTransferData(DataFlavor flavor)
	{
		if (flavor.getHumanPresentableName().equals("ProtoElementLabel"))
		{
			return (Object) (this);
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor myDataFlavor = new MyDataFlavor(this.getClass(),"ProtoElementLabel");
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = myDataFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getHumanPresentableName().equals("ProtoElementLabel"));
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
