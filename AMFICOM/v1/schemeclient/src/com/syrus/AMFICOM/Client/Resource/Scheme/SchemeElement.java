package com.syrus.AMFICOM.Client.Resource.Scheme;

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
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import com.syrus.AMFICOM.Client.Schematics.UI.*;

public class SchemeElement extends ObjectResource
		implements Transferable, Serializable
{
	public static final String typ = "schemeelement";
	private static final long serialVersionUID = 01L;
	SchemeElement_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String equipment_id = "";
	public String proto_element_id = "";
	public String scheme_id = "";

	public Vector devices = new Vector();
	public Vector links = new Vector();
	public Vector element_ids = new Vector();

	public Hashtable attributes = new Hashtable();

	public Serializable serializable_cell;
	public Serializable serializable_ugo;
	public byte[] schemecell;
	public byte[] ugo = new byte[0];

	public MapProtoElement mpe = null;
	public String ugo_text = "";

	public boolean alarmed = false;
	public String alarmed_link_id = "";

	public SchemeElement(SchemeElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeElement(String id)
	{
		this.id = id;
		transferable = new SchemeElement_Transferable();
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public SchemeElement (ProtoElement proto, DataSourceInterface dataSource)
	{
		id = dataSource.GetUId(SchemeElement.typ);

		name = proto.name + " (" + id.substring(5, id.length()) + ")";
		proto_element_id = proto.getId();
		scheme_id = "";

		EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);
		if (eqt != null)
			description = eqt.description;
		else
			description = "";

		devices = new Vector();
		for (int i = 0; i < proto.devices.size(); i++)
			devices.add(((SchemeDevice)proto.devices.get(i)).clone(dataSource));

		element_ids = new Vector();
		for (int i = 0; i < proto.protoelement_ids.size(); i++)
		{
			ProtoElement inner_proto = (ProtoElement)Pool.get(ProtoElement.typ, (String)proto.protoelement_ids.get(i));
			SchemeElement inner = new SchemeElement(inner_proto, dataSource);
			element_ids.add(inner.getId());
			Pool.put("proto2schemeids", inner_proto.getId(), inner.getId());
		}

		links = new Vector();
		for (int i = 0; i < proto.links.size(); i++)
			links.add(((SchemeLink)proto.links.get(i)).clone(dataSource));

		transferable = new SchemeElement_Transferable();
		attributes = ResourceUtil.copyAttributes(dataSource, proto.attributes);

		schemecell = new byte[proto.schemecell.length];
		for (int i = 0; i < schemecell.length; i++)
			schemecell[i] = proto.schemecell[i];
		ugo = new byte[proto.ugo.length];
		for (int i = 0; i < ugo.length; i++)
			ugo[i] = proto.ugo[i];

//    Pool.put(ProtoElement.typ, proto.getId(), proto);
//		Pool.put("clonedids", proto.getId(), id);
		Pool.put(SchemeElement.typ, getId(), this);
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			SchemeElement cloned = (SchemeElement)Pool.get(SchemeElement.typ, cloned_id);
			if (cloned == null)
				System.err.println("SchemeElement.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		SchemeElement element = new SchemeElement(dataSource.GetUId(SchemeElement.typ));
		element.name = name;
		element.description = description;
		element.equipment_id = equipment_id;
		element.proto_element_id = proto_element_id;
		element.scheme_id = scheme_id;

	/*	if (!scheme_id.equals(""))
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
			Scheme cscheme = (Scheme)scheme.clone(dataSource);
			element.sch
		}*/

		element.element_ids = new Vector(element_ids.size());
		for (int i = 0; i < element_ids.size(); i++)
		{
			SchemeElement el = ((SchemeElement)Pool.get(SchemeElement.typ, (String)element_ids.get(i)));
			SchemeElement cel = (SchemeElement)el.clone(dataSource);
			element.element_ids.add(cel.getId());
		}
		element.devices = new Vector(devices.size());
		for (int i = 0; i < devices.size(); i++)
			element.devices.add(((SchemeDevice)devices.get(i)).clone(dataSource));

		element.links = new Vector(links.size());
		for (int i = 0; i < links.size(); i++)
			element.links.add(((SchemeLink)links.get(i)).clone(dataSource));

		element.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		element.schemecell = new byte[schemecell.length];
		for (int i = 0; i < schemecell.length; i++)
			element.schemecell[i] = schemecell[i];
		element.ugo = new byte[ugo.length];
		for (int i = 0; i < ugo.length; i++)
			element.ugo[i] = ugo[i];
		element.unpack();

		element.mpe = mpe;
		element.ugo_text = ugo_text;

		Pool.put(SchemeElement.typ, element.getId(), element);
		Pool.put("clonedids", id, element.getId());
		return element;
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

	public ObjectResourceModel getModel()
	{
		return new SchemeElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] { "name" }, new String[] { LangModelSchematics.getString("name") });
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new SchemeElementPane();
	}

	public Enumeration getChildElements()
	{
		if (scheme_id.equals(""))
		{
			Vector v = new Vector();
			for (Enumeration en = element_ids.elements(); en.hasMoreElements();)
				v.add(Pool.get(SchemeElement.typ, (String)en.nextElement()));
			return v.elements();
		}
		else
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
			return scheme.getTopLevelElements();
		}
	}

	public Enumeration getAllChilds()
	{
		if (scheme_id.equals(""))
		{
			Vector v = new Vector();
			for (Enumeration en = element_ids.elements(); en.hasMoreElements();)
			{
				SchemeElement inner_se = (SchemeElement)Pool.get(SchemeElement.typ, (String)en.nextElement());
				v.add(inner_se);
				for (Enumeration e = inner_se.getAllChilds(); e.hasMoreElements(); )
					v.add(e.nextElement());
			}
			return v.elements();
		}
		else
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
			return scheme.getAllTopElements();
		}
	}

	public Enumeration getChildTypes()
	{
		Vector vec = new Vector();
		vec.add("devices");
		vec.add("links");
		vec.add("elements");
		return vec.elements();
	}

	public Class getChildClass(String key)
	{
		if(key.equals("devices"))
		{
			return SchemeDevice.class;
		}
		else if(key.equals("links"))
		{
			return SchemeLink.class;
		}
		else if(key.equals("elements"))
		{
			return SchemeElement.class;
		}
		return ObjectResource.class;
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals("devices"))
		{
			return devices.elements();
		}
		else if(key.equals("links"))
		{
			return links.elements();
		}
		else if(key.equals("elements"))
		{
			Vector elements = new Vector();
			for (int i = 0; i < element_ids.size(); i++)
				elements.add(Pool.get(SchemeElement.typ, (String)element_ids.get(i)));
			return elements.elements();
		}
		return new Vector().elements();
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		description = transferable.description;
		equipment_id = transferable.equipment_id;
		proto_element_id = transferable.proto_element_id;
		scheme_id = transferable.scheme_id;

		devices = new Vector();
		links = new Vector();
		element_ids = new Vector();

		for (int i = 0; i < transferable.devices.length; i++)
			devices.add(new SchemeDevice(transferable.devices[i]));
		for (int i = 0; i < transferable.links.length; i++)
			links.add(new SchemeLink(transferable.links[i]));
		for (int i = 0; i < transferable.element_ids.length; i++)
			element_ids.add(transferable.element_ids[i]);

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		schemecell = transferable.schemecell;
		ugo = transferable.ugocell;
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.description = description;
		transferable.equipment_id = equipment_id;
		transferable.proto_element_id = proto_element_id;
		transferable.scheme_id = scheme_id;

		transferable.devices = new SchemeDevice_Transferable[devices.size()];
		transferable.links = new SchemeLink_Transferable[links.size()];
		transferable.element_ids = new String[element_ids.size()];

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
		for (int i=0; i<transferable.element_ids.length; i++)
			transferable.element_ids[i] = (String)element_ids.get(i);

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
			Pool.put(SchemeDevice.typ, dev.getId(), dev);
			dev.updateLocalFromTransferable();
		}
		for (int i = 0; i < links.size(); i++)
		{
			SchemeLink link = (SchemeLink)links.get(i);
			Pool.put(SchemeLink.typ, link.getId(), link);
			link.updateLocalFromTransferable();
		}
	}

	public SchemePort getPort(String port_id)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.ports.iterator(); it2.hasNext();)
			{
				SchemePort port = (SchemePort)it2.next();
				if(port.getId().equals(port_id))
					return port;
			}
		}
		for(Iterator it = element_ids.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement child_element = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			SchemePort port = child_element.getPort(port_id);
			if (port != null)
				return port;
		}
		return null;
	}

	public SchemeCablePort getCablePort(String cable_port_id)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.cableports.iterator(); it2.hasNext();)
			{
				SchemeCablePort port = (SchemeCablePort)it2.next();
				if(port.getId().equals(cable_port_id))
					return port;
			}
		}
		for(Iterator it = element_ids.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement child_element = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			SchemeCablePort port = child_element.getCablePort(cable_port_id);
			if (port != null)
				return port;
		}
		return null;
	}

	public Enumeration getAllElementsLinks()
	{
		Hashtable ht = new Hashtable();
		for (int i = 0; i < links.size(); i++)
		{
			SchemeLink l = (SchemeLink)links.get(i);
			ht.put(l.getId(), l);
		}

		for(int i = element_ids.size() - 1; i >= 0; i--)
		{
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, (String )element_ids.get(i));
			if (se.scheme_id.equals(""))
			{
				for (Enumeration e = se.getAllElementsLinks(); e.hasMoreElements();)
				{
					SchemeLink l = (SchemeLink)e.nextElement();
					ht.put(l.getId(), l);
				}
			}
		}
		return ht.elements();
	}

	public Enumeration getAllSchemesLinks()
	{
		Hashtable ht = new Hashtable();

		for (Enumeration e = getAllElementsLinks(); e.hasMoreElements();)
		{
			SchemeLink l = (SchemeLink)e.nextElement();
			ht.put(l.getId(), l);
		}


		if (!scheme_id.equals(""))
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
			for (Enumeration e = scheme.getAllLinks(); e.hasMoreElements();)
			{
				SchemeLink l = (SchemeLink)e.nextElement();
				ht.put(l.getId(), l);
			}
		}

		for(int i = element_ids.size() - 1; i >= 0; i--)
		{
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, (String )element_ids.get(i));
			if (!se.scheme_id.equals(""))
			{
				Scheme inner_scheme = (Scheme)Pool.get(Scheme.typ, se.scheme_id);
				for (Enumeration e = inner_scheme.getAllLinks(); e.hasMoreElements();)
				{
					SchemeLink l = (SchemeLink)e.nextElement();
					ht.put(l.getId(), l);
				}
			}
		}
		return ht.elements();
	}

	public double getLong()
	{
		return 0;
	}

	public double getLat()
	{
		return 0;
	}


	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(equipment_id);
		out.writeObject(proto_element_id);
		out.writeObject(scheme_id);
		out.writeObject(devices);
		out.writeObject(links);
		out.writeObject(element_ids);
		out.writeObject(attributes);
		out.writeObject(ugo_text);

		out.writeObject(schemecell);
		out.writeObject(ugo);
	}

	public void pack()
	{
		schemecell = pack(serializable_cell);
		ugo = new byte[0];
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
			schemecell = baos.toByteArray();
			return schemecell;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void unpack()
	{
		if (serializable_cell == null && schemecell.length != 0)
			serializable_cell = unpack(schemecell);
		if (serializable_ugo == null && ugo.length != 0)
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

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		equipment_id = (String )in.readObject();
		proto_element_id = (String )in.readObject();
		scheme_id = (String )in.readObject();
		devices = (Vector )in.readObject();
		links = (Vector )in.readObject();
		element_ids = (Vector )in.readObject();
		attributes = (Hashtable )in.readObject();
		ugo_text = (String )in.readObject();

		schemecell = (byte[] )in.readObject();
		ugo = (byte[] )in.readObject();

		transferable = new SchemeElement_Transferable();
		updateLocalFromTransferable();
	}

	public void set_attribute(String a_id, String a_val)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get(a_id);
		ea.value = a_val;
	}

	public String get_attribute(String a_id)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get(a_id);
		return (String)ea.value;
	}
//////////////////////////////////////////////////
	public Object getTransferData(DataFlavor flavor)
	{
		if (flavor.getHumanPresentableName().equals("SchemeElementLabel"))
		{
			return (Object) (this);
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor myDataFlavor = new MyDataFlavor(this.getClass(),"SchemeElementLabel");
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = myDataFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
//    dfs[1] = DataFlavor.plainTextFlavor;
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getHumanPresentableName().equals("SchemeElementLabel"));
	}

}

class SchemeElementModel extends ObjectResourceModel
{
	SchemeElement se;

	public SchemeElementModel(SchemeElement schemeElement)
	{
		se = schemeElement;
	}

	public String getColumnValue(String col_id)
	{
		String s = "";
		try
		{
			if(col_id.equals("optimizerNodeAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )se.attributes.get("optimizerNodeAttribute");
				s = ea.value;
			}
			if(col_id.equals("name"))
			{
				s = se.getName();
			}
			if(col_id.equals("id"))
			{
				s = se.getId();
			}
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Analysis");
			s = "";
		}
		return s;
	}

	public void setColumnValue(String col_id, Object obj)
	{
		try
		{
			if(col_id.equals("optimizerNodeAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )se.attributes.get("optimizerNodeAttribute");
				ea.setValue(obj);
			}
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Analysis");
		}
	}
}

