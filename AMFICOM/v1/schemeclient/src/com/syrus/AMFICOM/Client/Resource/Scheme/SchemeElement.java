package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import java.awt.datatransfer.*;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemeElement extends StubResource
		implements Transferable, Serializable
{
	public static final String typ = "schemeelement";
	private static final long serialVersionUID = 02L;
	SchemeElement_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";

	public String equipmentId = "";
	private String kisId = "";

	public Equipment equipment;
	public KIS kis;

	public String protoElementId = "";
	private String schemeId = "";
	private String internalSchemeId = "";
	public String siteId = "";

	public Collection devices;
	public Collection links;
	public Collection elementIds;

	public Map attributes;

	public Serializable serializable_cell;
	public Serializable serializable_ugo;
	public byte[] schemecell;
	public byte[] ugo = new byte[0];

	public SchemeProtoGroup mpe = null;
	public String ugoText = "";

	public boolean alarmed = false;
	public String alarmedLinkId = "";

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
		elementIds = new ArrayList();
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
		protoElementId = proto.getId();

		EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipmentTypeId);
		if (eqt != null)
			description = eqt.description;
		else
			description = "";

		devices = new ArrayList(proto.devices.size());
		for(Iterator it = proto.devices.iterator(); it.hasNext();)
			devices.add(((SchemeDevice)it.next()).clone(dataSource));

		elementIds = new ArrayList(proto.protoelementIds.size());
		for(Iterator it = proto.protoelementIds.iterator(); it.hasNext();)
		{
			ProtoElement inner_proto = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			SchemeElement inner = new SchemeElement(inner_proto, dataSource);
			elementIds.add(inner.getId());
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
		String clonedId = (String)Pool.get("clonedids", id);
		if (clonedId != null)
		{
			SchemeElement cloned = (SchemeElement)Pool.get(SchemeElement.typ, clonedId);
			if (cloned == null)
				System.err.println("SchemeElement.clone() id not found: " + clonedId);
			else
				return cloned;
		}

		SchemeElement element = new SchemeElement(dataSource.GetUId(SchemeElement.typ));
		element.name = name;
		element.description = description;
		element.equipmentId = equipmentId;
		element.equipment = equipment;
		element.kis = kis;
		element.protoElementId = protoElementId;
		element.internalSchemeId = internalSchemeId;
		element.schemeId = schemeId;

	/*	if (!schemeId.equals(""))
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, schemeId);
			Scheme cscheme = (Scheme)scheme.clone(dataSource);
			element.sch
		}*/

		for(Iterator it = elementIds.iterator(); it.hasNext();)
		{
			SchemeElement el = ((SchemeElement)Pool.get(SchemeElement.typ, (String)it.next()));
			SchemeElement cel = (SchemeElement)el.clone(dataSource);
			element.elementIds.add(cel.getId());
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
		element.ugoText = ugoText;

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

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Schematics.UI.SchemeElementPane";
	}

//	public Collection getChildElements()
//	{
//		if (schemeId.equals(""))
//		{
//			HashSet v = new HashSet();
//			for (Iterator it = elementIds.iterator(); it.hasNext();)
//			{
//				SchemeElement innerSe = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
//				for (Iterator it2 = innerSe.getAllChilds().iterator(); it2.hasNext(); )
//					v.add(it2.next());
//				v.add(innerSe);
//			}
//			return v;
//		}
//		else
//		{
//			Scheme scheme = (Scheme)Pool.get(Scheme.typ, schemeId);
//			return scheme.getTopLevelElements();
//		}
//	}

	public Collection getChildElements()
	{
		HashSet v = new HashSet();
		for (Iterator it = elementIds.iterator(); it.hasNext(); )
		{
			SchemeElement innerSe = (SchemeElement) Pool.get(SchemeElement.typ, (String) it.next());
			for (Iterator it2 = innerSe.getAllChilds().iterator(); it2.hasNext(); )
				v.add(it2.next());
			v.add(innerSe);
		}
		return v;
	}

	public Collection getAllChilds()
	{
		if (internalSchemeId.equals(""))
		{
			HashSet v = new HashSet();
			for (Iterator it = elementIds.iterator(); it.hasNext();)
			{
				SchemeElement innerSe = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
				for (Iterator it2 = innerSe.getAllChilds().iterator(); it2.hasNext(); )
					v.add(it2.next());
				v.add(innerSe);
			}
			return v;
		}
		else
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, internalSchemeId);
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
		equipmentId = transferable.equipmentId;
		kisId = transferable.kisId;

		protoElementId = transferable.protoElementId;
		schemeId = transferable.schemeId;
		internalSchemeId = transferable.internalSchemeId;
		siteId = transferable.siteId;

		devices = new ArrayList(transferable.devices.length);
		links = new ArrayList(transferable.links.length);
		elementIds = new ArrayList(transferable.elementIds.length);
		attributes = new HashMap(transferable.attributes.length);

		for (int i = 0; i < transferable.devices.length; i++)
			devices.add(new SchemeDevice(transferable.devices[i]));
		for (int i = 0; i < transferable.links.length; i++)
			links.add(new SchemeLink(transferable.links[i]));
		for (int i = 0; i < transferable.elementIds.length; i++)
			elementIds.add(transferable.elementIds[i]);

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
		transferable.equipmentId = equipment == null ? equipmentId : equipment.getId();
		transferable.kisId = kis == null ? kisId : kis.getId();
		transferable.protoElementId = protoElementId;
		transferable.internalSchemeId = internalSchemeId;
		transferable.schemeId = schemeId;
		transferable.siteId = siteId;

		transferable.devices = new SchemeDevice_Transferable[devices.size()];
		transferable.links = new SchemeLink_Transferable[links.size()];
		transferable.elementIds = new String[elementIds.size()];

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
		for(Iterator it = elementIds.iterator(); it.hasNext();)
			transferable.elementIds[counter++] = (String)it.next();

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
		if (equipmentId.length() != 0)
			equipment = (Equipment)Pool.get(Equipment.typ, equipmentId);
		if (kisId.length() != 0)
			kis = (KIS)Pool.get(KIS.typ, kisId);

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

	public SchemePort getPort(String portId)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.ports.iterator(); it2.hasNext();)
			{
				SchemePort port = (SchemePort)it2.next();
				if(port.getId().equals(portId))
					return port;
			}
		}
		for(Iterator it = elementIds.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement childElement = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			SchemePort port = childElement.getPort(portId);
			if (port != null)
				return port;
		}
		return null;
	}

	public Collection getPorts()
	{
		if (devices.size() == 1)
			return ((SchemeDevice)devices.iterator().next()).ports;

		Collection p = new ArrayList();
		for (Iterator it = devices.iterator(); it.hasNext(); )
		{
			SchemeDevice sd = (SchemeDevice) it.next();
			p.addAll(sd.ports);
		}
		return p;
	}

	public Collection getCablePorts()
	{
		if (devices.size() == 1)
			return ((SchemeDevice)devices.iterator().next()).cableports;

		Collection p = new ArrayList();
		for (Iterator it = devices.iterator(); it.hasNext(); )
		{
			SchemeDevice sd = (SchemeDevice) it.next();
			p.addAll(sd.cableports);
		}
		return p;
	}

	public SchemeCablePort getCablePort(String cable_portId)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.cableports.iterator(); it2.hasNext();)
			{
				SchemeCablePort port = (SchemeCablePort)it2.next();
				if(port.getId().equals(cable_portId))
					return port;
			}
		}
		for(Iterator it = elementIds.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement childElement = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			SchemeCablePort port = childElement.getCablePort(cable_portId);
			if (port != null)
				return port;
		}
		return null;
	}

	public Collection getMeasurementPorts()
	{
		Collection p = new ArrayList();
		for (Iterator it = getPorts().iterator(); it.hasNext();)
		{
			SchemePort port = (SchemePort)it.next();
			if (port.measurementPortId.length() != 0)
				p.add(Pool.get(MeasurementPort.typ, port.measurementPortId));
		}
		for (Iterator it = getCablePorts().iterator(); it.hasNext();)
		{
			SchemeCablePort port = (SchemeCablePort)it.next();
			if (port.measurementPortId.length() != 0)
				p.add(Pool.get(MeasurementPort.typ, port.measurementPortId));
		}
		return p;
	}

	public Collection getAllElementsLinks()
	{
		HashSet ht = new HashSet();
		for(Iterator it = links.iterator(); it.hasNext();)
			ht.add(it.next());

		for(Iterator it = elementIds.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			if (se.internalSchemeId.length() == 0)
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

		if (!internalSchemeId.equals(""))
		{
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, internalSchemeId);
			for (Iterator it = scheme.getAllLinks().iterator(); it.hasNext();)
				ht.add(it.next());
		}

		for(Iterator it = elementIds.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, (String)it.next());
			if (se.internalSchemeId.length() != 0)
			{
				Scheme innerScheme = (Scheme)Pool.get(Scheme.typ, se.internalSchemeId);
				for (Iterator it2 = innerScheme.getAllLinks().iterator(); it2.hasNext();)
					ht.add(it2.next());
			}
		}
		return ht;
	}

	public SchemeElement getSchemeElementByCablePort(String portId)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.cableports.iterator(); it2.hasNext();)
			{
				SchemeCablePort scp = (SchemeCablePort)it2.next();
				if(scp.getId().equals(portId))
					return this;
			}
		}

		for(Iterator it = elementIds.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			se = se.getSchemeElementByCablePort(portId);
			if (se != null)
				return se;
		}

		if (internalSchemeId.length() != 0)// Search inner schemes
		{
			Scheme childScheme = (Scheme)Pool.get(Scheme.typ, internalSchemeId);
			SchemeElement el = childScheme.getSchemeElementByCablePort(portId);
			if (el != null)
				return el;
		}

		return null;
	}

	public SchemeElement getSchemeElementByPort(String portId)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			for(Iterator it2 = sd.ports.iterator(); it2.hasNext();)
			{
				SchemePort sp = (SchemePort)it2.next();
				if(sp.getId().equals(portId))
					return this;
			}
		}

		for(Iterator it = elementIds.iterator(); it.hasNext();)			// Search inner elements
		{
			SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			se = se.getSchemeElementByPort(portId);
			if (se != null)
				return se;
		}

		if (internalSchemeId.length() != 0)// Search inner schemes
		{
			Scheme childScheme = (Scheme)Pool.get(Scheme.typ, internalSchemeId);
			SchemeElement el = childScheme.getSchemeElementByPort(portId);
			if (el != null)
				return el;
		}

		return null;
	}

	protected SchemeElement getSchemeElementByDevice(String deviceId)
	{
		for(Iterator it = devices.iterator(); it.hasNext();)
		{
			SchemeDevice sd = (SchemeDevice)it.next();
			if(sd.getId().equals(deviceId))
				return this;
		}
		for(Iterator it = elementIds.iterator(); it.hasNext();)
		{
			SchemeElement childElement = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			// Search inner elements
			SchemeElement el = childElement.getSchemeElementByDevice(deviceId);
			if (el != null)
				return el;
		}
		if (internalSchemeId.length() != 0)// Search inner schemes
		{
			Scheme childScheme = (Scheme)Pool.get(Scheme.typ, internalSchemeId);
			SchemeElement el = childScheme.getSchemeElementByDevice(deviceId);
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
		out.writeObject(equipmentId);
		out.writeObject(kisId);

		Equipment[] eq;
		if (equipment == null)
			eq = new Equipment[0];
		else
		{
			eq = new Equipment[1];
			eq[0] = equipment;
		}
		KIS[] k;
		if (kis == null)
			k = new KIS[0];
		else
		{
			k = new KIS[1];
			k[0] = kis;
		}
		out.writeObject(eq);
		out.writeObject(k);

		out.writeObject(protoElementId);
		out.writeObject(internalSchemeId);
		out.writeObject(schemeId);
		out.writeObject(devices);
		out.writeObject(links);
		out.writeObject(elementIds);
		out.writeObject(attributes);
		out.writeObject(ugoText);

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
		equipmentId = (String )in.readObject();
		kisId = (String )in.readObject();

		Equipment[] eq = (Equipment[])in.readObject();
		if (eq.length == 1)
			equipment = eq[0];
		KIS[] k = (KIS[])in.readObject();
		if (k.length == 1)
			kis = k[0];

		protoElementId = (String )in.readObject();
		internalSchemeId = (String )in.readObject();
		schemeId = (String )in.readObject();
		devices = (Collection )in.readObject();
		links = (Collection )in.readObject();
		elementIds = (Collection )in.readObject();
		attributes = (Map )in.readObject();
		ugoText = (String )in.readObject();

		schemecell = (byte[] )in.readObject();
		ugo = (byte[] )in.readObject();

		transferable = new SchemeElement_Transferable();
	//	updateLocalFromTransferable();
	}

	public void set_attribute(String aId, String a_val)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get(aId);
		ea.value = a_val;
	}

	public String get_attribute(String aId)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get(aId);
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

	public String getEquipmentTypeId()
	{
		ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, protoElementId);
		if (proto != null)
			return proto.equipmentTypeId;
		return "";
	}

	public void setEquipmentTypeId(String equipmentTypeId)
	{
		ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, protoElementId);
		if (proto != null)
			proto.equipmentTypeId = equipmentTypeId;
	}

	public String getSchemeId()
	{
		return schemeId;
	}

	public void setSchemeId(String schemeId)
	{
		for (Iterator it = getChildElements().iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			se.schemeId = schemeId;
		}
		for (Iterator it = getAllElementsLinks().iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			link.setSchemeId(schemeId);
		}

		this.schemeId = schemeId;
	}

	public String getInternalSchemeId()
	{
		return internalSchemeId;
	}

	public void setInternalSchemeId(String internalSchemeId)
	{
		this.internalSchemeId = internalSchemeId;
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

	public String getColumnValue(String colId)
	{
		String s = "";
		try
		{
			if(colId.equals("optimizerNodeAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )se.attributes.get("optimizerNodeAttribute");
				s = ea.value;
			}
			if(colId.equals("name"))
			{
				s = se.getName();
			}
			if(colId.equals("id"))
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

	public void setColumnValue(String colId, Object obj)
	{
		try
		{
			if(colId.equals("optimizerNodeAttribute"))
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

