package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import java.awt.datatransfer.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import com.syrus.AMFICOM.Client.Schematics.UI.SchemeElementPane;

public class SchemeElement extends StubResource
		implements Transferable, Serializable
{
	public static final String typ = "schemeelement";
	private static final long serialVersionUID = 02L;
	SchemeElement_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String equipment_id = "";
	public String proto_element_id = "";
	private String internal_scheme_id = "";
	private String scheme_id = "";

	public Collection devices;
	public Collection links;
	public Collection element_ids;

	public Map attributes;

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

		devices = new ArrayList();
		links = new ArrayList();
		element_ids = new ArrayList();
		attributes = new HashMap();
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

		EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);
		if (eqt != null)
			description = eqt.description;
		else
			description = "";

		devices = new ArrayList(proto.devices.size());
		for(Iterator it = proto.devices.iterator(); it.hasNext();)
			devices.add(((SchemeDevice)it.next()).clone(dataSource));

		element_ids = new ArrayList(proto.protoelement_ids.size());
		for(Iterator it = proto.protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement inner_proto = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			SchemeElement inner = new SchemeElement(inner_proto, dataSource);
			element_ids.add(inner.getId());
			Pool.put("proto2schemeids", inner_proto.getId(), inner.getId());
		}

		links = new ArrayList(proto.links.size());
		for(Iterator it = proto.links.iterator(); it.hasNext();)
			links.add(((SchemeLink)it.next()).clone(dataSource));

		transferable = new SchemeElement_Transferable();
		attributes = ResourceUtil.copyAttributes(dataSource, proto.attributes);

		schemecell = new byte[proto.schemecell.length];
		System.arraycopy(proto.schemecell, 0, schemecell, 0, schemecell.length);
		ugo = new byte[proto.ugo.length];
		System.arraycopy(proto.ugo, 0, ugo, 0, ugo.length);

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
		element.internal_scheme_id = internal_scheme_id;
		element.scheme_id = scheme_id;

	/*	if (!scheme_id.equals(""))
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
			Scheme cscheme = (Scheme)scheme.clone(dataSource);
			element.sch
		}*/

		for(Iterator it = element_ids.iterator(); it.hasNext();)
		{
			SchemeElement el = ((SchemeElement)Pool.get(SchemeElement.typ, (String)it.next()));
			SchemeElement cel = (SchemeElement)el.clone(dataSource);
			element.element_ids.add(cel.getId());
		}
		for(Iterator it = devices.iterator(); it.hasNext();)
			element.devices.add(((SchemeDevice)it.next()).clone(dataSource));
		for(Iterator it = links.iterator(); it.hasNext();)
			element.links.add(((SchemeLink)it.next()).clone(dataSource));

		element.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		element.schemecell = new byte[schemecell.length];
		System.arraycopy(schemecell, 0, element.schemecell, 0, schemecell.length);
		element.ugo = new byte[ugo.length];
		System.arraycopy(ugo, 0, element.ugo, 0, ugo.length);
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
		System.out.println("Getting SchemeElement property pane");
		return new SchemeElementPane();
	}

	public Collection getChildElements()
	{
		HashSet v = new HashSet();
		for (Iterator it = element_ids.iterator(); it.hasNext();)
		{
			SchemeElement inner_se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			for (Iterator it2 = inner_se.getAllChilds().iterator(); it2.hasNext(); )
				v.add(it2.next());
			v.add(inner_se);
		}
		return v;
	}

	public Collection getAllChilds()
	{
		if (internal_scheme_id.equals(""))
		{
			HashSet v = new HashSet();
			for (Iterator it = element_ids.iterator(); it.hasNext();)
			{
				SchemeElement inner_se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
				for (Iterator it2 = inner_se.getAllChilds().iterator(); it2.hasNext(); )
					v.add(it2.next());
				v.add(inner_se);
			}
			return v;
		}
		else
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, internal_scheme_id);
			return scheme.getAllTopElements();
		}
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
		internal_scheme_id = transferable.scheme_id;

		devices = new ArrayList(transferable.devices.length);
		links = new ArrayList(transferable.links.length);
		element_ids = new ArrayList(transferable.element_ids.length);
		attributes = new HashMap(transferable.attributes.length);

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
		transferable.scheme_id = internal_scheme_id;

		transferable.devices = new SchemeDevice_Transferable[devices.size()];
		transferable.links = new SchemeLink_Transferable[links.size()];
		transferable.element_ids = new String[element_ids.size()];

		int counter = 0;
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice device = (SchemeDevice)it.next();
			device.setTransferableFromLocal();
			transferable.devices[counter++] = (SchemeDevice_Transferable)device.getTransferable();
		}
		counter = 0;
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			link.setTransferableFromLocal();
			transferable.links[counter++] = (SchemeLink_Transferable)link.getTransferable();
		}
		counter = 0;
		for(Iterator it = element_ids.iterator(); it.hasNext();)
			transferable.element_ids[counter++] = (String)it.next();

		counter = 0;
		transferable.attributes = new ElementAttribute_Transferable[attributes.size()];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute)it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[counter++] = ea.transferable;
		}

		transferable.schemecell = schemecell;
		transferable.ugocell = ugo;
	}

	public void updateLocalFromTransferable()
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			Pool.put(SchemeDevice.typ, dev.getId(), dev);
			dev.updateLocalFromTransferable();
		}
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
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

	public Collection getAllElementsLinks()
	{
		HashSet ht = new HashSet();
		for(Iterator it = links.iterator(); it.hasNext();)
			ht.add(it.next());

		for(Iterator it = element_ids.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			if (se.internal_scheme_id.length() == 0)
			{
				for (Iterator it2 = se.getAllElementsLinks().iterator(); it2.hasNext();)
					ht.add(it2.next());
			}
		}
		return ht;
	}

	public Collection getAllSchemesLinks()
	{
		HashSet ht = new HashSet();

		for (Iterator it = getAllElementsLinks().iterator(); it.hasNext();)
		{
			SchemeLink l = (SchemeLink)it.next();
			ht.add(l);
		}

		if (!internal_scheme_id.equals(""))
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, internal_scheme_id);
			for (Iterator it = scheme.getAllLinks().iterator(); it.hasNext();)
				ht.add(it.next());
		}

		for(Iterator it = element_ids.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, (String)it.next());
			if (se.internal_scheme_id.length() != 0)
			{
				Scheme inner_scheme = (Scheme)Pool.get(Scheme.typ, se.internal_scheme_id);
				for (Iterator it2 = inner_scheme.getAllLinks().iterator(); it2.hasNext();)
					ht.add(it2.next());
			}
		}
		return ht;
	}

	public SchemeElement getSchemeElementByCablePort(String port_id)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.cableports.iterator(); it2.hasNext();)
			{
				SchemeCablePort scp = (SchemeCablePort)it2.next();
				if(scp.getId().equals(port_id))
					return this;
			}
		}

		for(Iterator it = element_ids.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			se = se.getSchemeElementByCablePort(port_id);
			if (se != null)
				return se;
		}

		if (internal_scheme_id.length() != 0)// Search inner schemes
		{
			Scheme child_scheme = (Scheme)Pool.get(Scheme.typ, internal_scheme_id);
			SchemeElement el = child_scheme.getSchemeElementByCablePort(port_id);
			if (el != null)
				return el;
		}

		return null;
	}

	public SchemeElement getSchemeElementByPort(String port_id)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.ports.iterator(); it2.hasNext();)
			{
				SchemePort sp = (SchemePort)it2.next();
				if(sp.getId().equals(port_id))
					return this;
			}
		}

		for(Iterator it = element_ids.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			se = se.getSchemeElementByPort(port_id);
			if (se != null)
				return se;
		}

		if (internal_scheme_id.length() != 0)// Search inner schemes
		{
			Scheme child_scheme = (Scheme)Pool.get(Scheme.typ, internal_scheme_id);
			SchemeElement el = child_scheme.getSchemeElementByPort(port_id);
			if (el != null)
				return el;
		}

		return null;
	}

	protected SchemeElement getSchemeElementByDevice(String device_id)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			if(sd.getId().equals(device_id))
				return this;
		}
		for(Iterator it = element_ids.iterator(); it.hasNext();)
		{
			SchemeElement child_element = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			// Search inner elements
			SchemeElement el = child_element.getSchemeElementByDevice(device_id);
			if (el != null)
				return el;
		}
		if (internal_scheme_id.length() != 0)// Search inner schemes
		{
			Scheme child_scheme = (Scheme)Pool.get(Scheme.typ, internal_scheme_id);
			SchemeElement el = child_scheme.getSchemeElementByDevice(device_id);
			if (el != null)
				return el;
		}
		return null;
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
		out.writeObject(internal_scheme_id);
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
		internal_scheme_id = (String )in.readObject();
		scheme_id = (String )in.readObject();
		devices = (Collection )in.readObject();
		links = (Collection )in.readObject();
		element_ids = (Collection )in.readObject();
		attributes = (Map )in.readObject();
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

	public String getSchemeId()
	{
		return scheme_id;
	}

	public void setSchemeId(String scheme_id)
	{
		for (Iterator it = getChildElements().iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			se.scheme_id = scheme_id;
		}
		for (Iterator it = getAllElementsLinks().iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			link.setSchemeId(scheme_id);
		}

		this.scheme_id = scheme_id;
	}

	public String getInternalSchemeId()
	{
		return internal_scheme_id;
	}

	public void setInternalSchemeId(String scheme_id)
	{
		this.internal_scheme_id = scheme_id;
	}

	public Scheme getInternalScheme()
	{
		return (Scheme)Pool.get(Scheme.typ, getInternalSchemeId());
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

