package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import java.awt.datatransfer.*;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;

public class SchemePath extends StubResource
		implements Transferable, Serializable
{
	SchemePath_Transferable transferable;
	public static final String typ = "schemepath";
	private static final long serialVersionUID = 01L;

	public String id = "";
	public String name = "";
	public String pathId = "";
	public TransmissionPath path;

	public String typeId = "";
	public String startDeviceId = "";
	public String endDeviceId = "";
	private String schemeId = "";

	public Map attributes;
	public List links;

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
		startDeviceId = path.startDeviceId;
		endDeviceId = path.endDeviceId;
		typeId = path.typeId;
		pathId = path.pathId;

		links = new ArrayList(path.links.size());
		attributes = new HashMap(path.attributes.size());

		for(Iterator it = path.links.iterator(); it.hasNext();)
			links.add(it.next());

		for(Iterator it = path.attributes.keySet().iterator(); it.hasNext();)
		{
			Object key = it.next();
			attributes.put(key, path.attributes.get(key));
		}

		transferable = new SchemePath_Transferable();
	}
/*
	public Object clone(DataSourceInterface dataSource)
	{
		String clonedId = (String)Pool.get("clonedids", id);
		if (clonedId != null)
		{
			SchemePath cloned = (SchemePath)Pool.get(SchemePath.typ, clonedId);
			if (cloned == null)
				System.err.println("SchemePath.clone() id not found: " + clonedId);
			else
				return cloned;
		}

		SchemePath path = new SchemePath(dataSource.GetUId(SchemePath.typ));

		path.name = name;
		path.pathId = pathId;
		path.typeId = typeId;
		path.startDeviceId = (String)Pool.get("clonedids", startDeviceId);
		if (path.startDeviceId == null)
			path.startDeviceId = startDeviceId;
		path.endDeviceId = (String)Pool.get("clonedids", endDeviceId);
		if (path.endDeviceId == null)
			path.endDeviceId = endDeviceId;

		path.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		for (Iterator it = links.iterator(); it.hasNext();)
			path.links.add(((PathElement)it.next()).clone(dataSource));

		path.mtppe = mtppe;

		Pool.put(SchemePath.typ, path.getId(), path);
		Pool.put("clonedids", id, path.getId());
		return path;
	}
*/
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

	public boolean isElementInPath(String linkId)
	{
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			PathElement pe = (PathElement)it.next();
			if (linkId.equals(pe.getObjectId()))
				return true;
		}
		return false;
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] { "name" }, new String[] { LangModelSchematics.getString("name") });
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Schematics.UI.SchemePathPane";
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
			if (pe.type == PathElement.CABLE_LINK)
				length += pe.getSchemeCableLink().physicalLength;
			else if (pe.type == PathElement.LINK)
				length += pe.getSchemeLink().physicalLength;
		}
		return length;
	}

	public double getOpticalLength()
	{
		double length = 0;
		for (Iterator it = links.iterator(); it.hasNext(); )
		{
			PathElement pe = (PathElement) it.next();
			if (pe.type == PathElement.CABLE_LINK)
				length += pe.getSchemeCableLink().opticalLength;
			else if (pe.type == PathElement.LINK)
				length += pe.getSchemeLink().opticalLength;
		}
		return length;
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;
		name = transferable.name;
		startDeviceId = transferable.startDeviceId;
		endDeviceId = transferable.endDeviceId;
		typeId = transferable._typeId;
		pathId = transferable.pathId;

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
		transferable.startDeviceId = startDeviceId;
		transferable.endDeviceId = endDeviceId;
		transferable._typeId = typeId;
		transferable.pathId = path == null ? pathId : path.getId();

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
		if (pathId.length() != 0)
			path = (TransmissionPath)Pool.get(TransmissionPath.typ, pathId);
	}

	public String getSchemeId()
	{
		return schemeId;
	}

	public void setSchemeId(String schemeId)
	{
		this.schemeId = schemeId;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(pathId);
		TransmissionPath[] p;
		if (path == null)
			p = new TransmissionPath[0];
		else
		{
			p = new TransmissionPath[1];
			p[0] = path;
		}

		out.writeObject(links);
		out.writeObject(attributes);
		out.writeObject(typeId);
		out.writeObject(startDeviceId);
		out.writeObject(endDeviceId);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		pathId = (String )in.readObject();
		TransmissionPath[] p = (TransmissionPath[])in.readObject();
		if (p.length == 1)
			path = p[0];

		links = (List )in.readObject();
		attributes = (Map )in.readObject();
		typeId = (String )in.readObject();
		startDeviceId = (String )in.readObject();
		endDeviceId = (String )in.readObject();

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

	public String getColumnValue(String colId)
	{
		if(colId.equals("id"))
			return sp.getId();
		if(colId.equals("full_path"))
		{
			String s = "";
			String id = "", name = "";
			s += ((SchemeElement)Pool.get( SchemeElement.typ, sp.startDeviceId)).name;
			PathElement pe;
			for( Iterator links = sp.links.iterator();  links.hasNext();)
			{ pe = (PathElement) links.next();
				id = pe.getObjectId();
				name = pe.getName();
				if(! links.hasNext())
				{ // в конце стрелочку не ставим (если это последдний элемент, то выход)
					s += name;
					break;
				}
				s += name + "->";
			}

			return s;
		}
		if(colId.equals("name"))
			return sp.getName();
		return "";
	}
}

