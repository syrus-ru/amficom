package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import java.awt.datatransfer.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathProtoElement;
import com.syrus.AMFICOM.Client.Schematics.UI.SchemePathPane;

public class SchemePath extends StubResource
		implements Transferable, Serializable
{
	SchemePath_Transferable transferable;
	public static final String typ = "schemepath";
	private static final long serialVersionUID = 01L;

	public String id = "";
	public String name = "";
	public String path_id = "";
	public String type_id = "";
	public String start_device_id = "";
	public String end_device_id = "";

	public Map attributes;
	public List links;

	public MapTransmissionPathProtoElement mtppe = null;

	public SchemePath(SchemePath_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemePath(String id)
	{
		this.id = id;
		transferable = new SchemePath_Transferable();

		attributes = new HashMap();
		links = new ArrayList();
	}

	public SchemePath(SchemePath path)
	{
		id  = path.id;
		name = path.name;
		start_device_id = path.start_device_id;
		end_device_id = path.end_device_id;
		type_id = path.type_id;
		path_id = path.path_id;

		for(Iterator it = path.links.iterator(); it.hasNext();)
			links.add(it.next());

		for(Iterator it = path.attributes.keySet().iterator(); it.hasNext();)
		{
			Object key = it.next();
			attributes.put(key, path.attributes.get(key));
		}

		transferable = new SchemePath_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			SchemePath cloned = (SchemePath)Pool.get(SchemePath.typ, cloned_id);
			if (cloned == null)
				System.err.println("SchemePath.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		SchemePath path = new SchemePath(dataSource.GetUId(SchemePath.typ));

		path.name = name;
		path.path_id = path_id;
		path.type_id = type_id;
		path.start_device_id = (String)Pool.get("clonedids", start_device_id);
		if (path.start_device_id == null)
			path.start_device_id = start_device_id;
		path.end_device_id = (String)Pool.get("clonedids", end_device_id);
		if (path.end_device_id == null)
			path.end_device_id = end_device_id;

		path.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		for (Iterator it = links.iterator(); it.hasNext();)
			path.links.add(((PathElement)it.next()).clone(dataSource));

		path.mtppe = mtppe;

		Pool.put(SchemePath.typ, path.getId(), path);
		Pool.put("clonedids", id, path.getId());
		return path;
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
		return "sysdomain";
	}

	public String getTyp()
	{
		return typ;
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] { "name" }, new String[] { LangModelSchematics.getString("name") });
	}

	public static PropertiesPanel getPropertyPane()
	{
		System.out.println("Getting SchemePath property pane");
		return new SchemePathPane();
	}

	static public ObjectResourceSorter getSorter()
	{
		return new ObjectResourcePathSorter();
	}

	public ObjectResourceModel getModel()
	{
		return new SchemePathModel(this);
	}

	public double getPhysicalLength()
	{
		double length = 0;
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.is_cable)
			{
				SchemeCableLink link = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				length += link.physical_length;
			}
			else
			{
				SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
				length += link.physical_length;
			}
		}
		return length;
	}

	public double getOpticalLength()
	{
		double length = 0;
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (pe.is_cable)
			{
				SchemeCableLink link = (SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.link_id);
				length += link.optical_length;
			}
			else
			{
				SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, pe.link_id);
				length += link.optical_length;
			}
		}
		return length;
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		start_device_id = transferable.start_device_id;
		end_device_id = transferable.end_device_id;
		type_id = transferable.type_id;
		path_id = transferable.path_id;

		attributes = new HashMap(transferable.attributes.length);
		links = new ArrayList(transferable.links.length);

		for(int i = 0; i < transferable.links.length; i++)
			links.add(new PathElement(transferable.links[i]));

		ObjectResourceSorter sorter = getSorter();
		sorter.setDataSet(links);
		links = sorter.sort("num", ObjectResourceSorter.SORT_ASCENDING);

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.start_device_id = start_device_id;
		transferable.end_device_id = end_device_id;
		transferable.type_id = type_id;
		transferable.path_id = path_id;

		transferable.links = new PathElement_Transferable[links.size()];

		int counter = 0;
		for (Iterator it = links.iterator(); it.hasNext();)
			transferable.links[counter++] = (PathElement_Transferable)((PathElement)it.next()).getTransferable();

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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(path_id);
		out.writeObject(links);
		out.writeObject(attributes);
		out.writeObject(type_id);
		out.writeObject(start_device_id);
		out.writeObject(end_device_id);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		path_id = (String )in.readObject();
		links = (List )in.readObject();
		attributes = (Map )in.readObject();
		type_id = (String )in.readObject();
		start_device_id = (String )in.readObject();
		end_device_id = (String )in.readObject();

		transferable = new SchemePath_Transferable();
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

class SchemePathModel extends ObjectResourceModel
{
	private SchemePath sp;
	public SchemePathModel(SchemePath sp)
	{
		super(sp);
		this.sp = sp;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return sp.getId();
		if(col_id.equals("full_path"))
			return sp.getName();
		if(col_id.equals("name"))
			return sp.getName();
		return "";
	}
}

