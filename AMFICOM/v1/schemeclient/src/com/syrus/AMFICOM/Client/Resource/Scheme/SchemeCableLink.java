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
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;

public class SchemeCableLink extends StubResource
		implements Transferable, Serializable
{
	public static final String typ = "schemecablelink";
	public static final char delimeter = ':';
	private static final long serialVersionUID = 04L;
	public SchemeCableLink_Transferable transferable;

	public String id = "";
	private String name = "";
	public String sourcePortId = "";
	public String targetPortId = "";
	public String cableLinkId = "";
	public CableLink cableLink;
	public String cableLinkTypeId = "";
	private String schemeId = "";

	public double opticalLength = 0;
	public double physicalLength = 0;

	public List cableThreads;
	public List channelingItems;
	public Map attributes;

	public boolean alarmed = false;

	private static CableThreadSorter sorter;

	public SchemeCableLink(SchemeCableLink_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeCableLink(String id)
	{
		this.id = id;
		transferable = new SchemeCableLink_Transferable();

		cableThreads = new ArrayList();
		channelingItems = new ArrayList();
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

	public void setName(String name)
	{
		this.name = name;
		for (Iterator it = cableThreads.iterator(); it.hasNext();)
		{
			SchemeCableThread thread = (SchemeCableThread)it.next();
			thread.name = new StringBuffer(name).append(delimeter).append(parseName(thread.getName())).toString();
		}
	}

	public static String parseName(String name)
	{
		int pos = name.lastIndexOf(delimeter);
		if (pos == -1)
			return name;
		return pos == name.length() ? name : name.substring(pos + 1);
	}

	public String getId()
	{
		return id;
	}

	public SchemeCableThread getCableThread(String threadId)
	{
		for (Iterator it = cableThreads.iterator(); it.hasNext();)
		{
			SchemeCableThread thread = (SchemeCableThread)it.next();
			if (thread.getId().equals(threadId))
				return thread;
		}
		return null;
	}

	public ObjectResourceModel getModel()
	{
		return new SchemeCableLinkModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new StubDisplayModel(new String[] { "name" }, new String[] { LangModelSchematics.getString("name") });
	}

	public String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Schematics.UI.SchemeCableLinkPane";
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id  = transferable.id;

		sourcePortId = transferable.sourcePortId;
		targetPortId = transferable.targetPortId;
		cableLinkId = transferable.cableLinkId;
		cableLinkTypeId = transferable.cableLinkTypeId;

		try
		{
			opticalLength = Double.parseDouble(transferable.opticalLength);
		}
		catch (NumberFormatException ex)
		{
			opticalLength = 0;
		}
		try
		{
			physicalLength = Double.parseDouble(transferable.physicalLength);
		}
		catch (NumberFormatException ex)
		{
			physicalLength = 0;
		}
		cableThreads = new ArrayList();
		for (int i = 0; i < transferable.cableThreads.length; i++)
			cableThreads.add(new SchemeCableThread(transferable.cableThreads[i]));
		channelingItems = new ArrayList();
		for (int i = 0; i < transferable.channeling.length; i++)
			channelingItems.add(new CableChannelingItem(transferable.channeling[i]));

		setName(transferable.name);

		attributes = new HashMap(transferable.attributes.length);
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.sourcePortId = sourcePortId;
		transferable.targetPortId = targetPortId;
		transferable.cableLinkId = cableLinkId;
		transferable.cableLinkTypeId = cableLinkTypeId;
		transferable.cableLinkId = cableLink == null ? cableLinkId : cableLink.getId();
		transferable.opticalLength = String.valueOf(opticalLength);
		transferable.physicalLength = String.valueOf(physicalLength);

		transferable.cableThreads = new SchemeCableThread_Transferable[cableThreads.size()];
		int counter = 0;
		for (Iterator it = cableThreads.iterator(); it.hasNext();)
		{
			SchemeCableThread cableThread = (SchemeCableThread)it.next();
			cableThread.setTransferableFromLocal();
			transferable.cableThreads[counter++] = (SchemeCableThread_Transferable)cableThread.getTransferable();
		}
		transferable.channeling = new CableChannelingItem_Transferable[channelingItems.size()];
		counter = 0;
		for (Iterator it = channelingItems.iterator(); it.hasNext();)
		{
			CableChannelingItem item = (CableChannelingItem)it.next();
			item.setTransferableFromLocal();
			transferable.channeling[counter++] = (CableChannelingItem_Transferable)item.getTransferable();
		}

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
		if (cableLinkId.length() != 0)
			cableLink = (CableLink)Pool.get(CableLink.typ, cableLinkId);

		ObjectResourceSorter sorter = getSorter();
		sorter.setDataSet(cableThreads);
		cableThreads = sorter.sort("num", ObjectResourceSorter.SORT_ASCENDING);
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String clonedId = (String)Pool.get("clonedids", id);
		if (clonedId != null)
		{
			SchemeCableLink clonedLink = (SchemeCableLink)Pool.get(SchemeCableLink.typ, clonedId);
			if (clonedLink == null)
				System.err.println("SchemeCableLink.clone() id not found: " + clonedId);
			else
				return clonedLink;
		}

		SchemeCableLink link = new SchemeCableLink(dataSource.GetUId(SchemeCableLink.typ));

		link.cableLinkId = cableLinkId;
		link.cableLink = cableLink;
		link.cableLinkTypeId = cableLinkTypeId;
		link.sourcePortId = (String)Pool.get("clonedids", sourcePortId);
		if (link.sourcePortId == null)
			link.sourcePortId = sourcePortId;
		link.targetPortId = (String)Pool.get("clonedids", targetPortId);
		if (link.targetPortId == null)
			link.targetPortId = targetPortId;

		link.opticalLength = opticalLength;
		link.physicalLength = physicalLength;

		if (!link.sourcePortId.equals(""))
		{
			SchemeCablePort sourcePort = (SchemeCablePort)Pool.get(SchemeCablePort.typ, link.sourcePortId);
			sourcePort.cableLinkId = link.getId();
		}
		if (!link.targetPortId.equals(""))
		{
			SchemeCablePort targetPort = (SchemeCablePort)Pool.get(SchemeCablePort.typ, link.targetPortId);
			targetPort.cableLinkId = link.getId();
		}

		for (Iterator it = cableThreads.iterator(); it.hasNext();)
		{
			SchemeCableThread thread = (SchemeCableThread)((SchemeCableThread)it.next()).clone(dataSource);
			thread.cableLinkId = link.cableLinkId;
			link.cableThreads.add(thread);
		}

		link.setName(name);

		link.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeCableLink.typ, link.getId(), link);
		Pool.put("clonedids", id, link.id);
		return link;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(sourcePortId);
		out.writeObject(targetPortId);
		out.writeObject(cableLinkId);
		CableLink[] l;
		if (cableLink == null)
			l = new CableLink[0];
		else
		{
			l = new CableLink[1];
			l[0] = cableLink;
		}

		out.writeObject(cableLinkTypeId);
		out.writeObject(schemeId);
		out.writeObject(cableThreads);
		out.writeObject(channelingItems);
		out.writeDouble(opticalLength);
		out.writeDouble(physicalLength);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		sourcePortId = (String )in.readObject();
		targetPortId = (String )in.readObject();
		cableLinkId = (String )in.readObject();
		CableLink[] l = (CableLink[])in.readObject();
		if (l.length == 1)
			cableLink = l[0];

		cableLinkTypeId = (String )in.readObject();
		schemeId = (String )in.readObject();
		cableThreads = (List )in.readObject();
		channelingItems = (List )in.readObject();
		opticalLength = in.readDouble();
		physicalLength = in.readDouble();
		attributes = (Map )in.readObject();

		transferable = new SchemeCableLink_Transferable();
	}

	public double getPhysicalLength()
	{
		return physicalLength;
	}

	public void setPhysicalLength(double d)
	{
		physicalLength = d;
	}

	public double getOpticalLength()
	{
		return opticalLength;
	}

	public void setOpticalLength(double d)
	{
		opticalLength = d;
	}

	public String getSchemeId()
	{
		return schemeId;
	}

	public void setSchemeId(String schemeId)
	{
		this.schemeId = schemeId;
	}

	static public ObjectResourceSorter getSorter()
	{
		if (sorter == null)
			sorter = new CableThreadSorter();
		return sorter;
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

class SchemeCableLinkModel extends ObjectResourceModel
{
	SchemeCableLink scl;

	public SchemeCableLinkModel(SchemeCableLink schemeCableLink)
	{
		scl = schemeCableLink;
	}

	public String getColumnValue(String colId)
	{
		String s = "";
		try
		{
			if(colId.equals("optimizerRibAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )scl.attributes.get("optimizerRibAttribute");
				s = ea.value;
			}
			if(colId.equals("name"))
			{
				s = scl.getName();
			}
			if(colId.equals("id"))
			{
				s = scl.getId();
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
			if(colId.equals("optimizerRibAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )scl.attributes.get("optimizerRibAttribute");
				ea.setValue(obj);
			}
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Analysis");
		}
	}
}

class CableThreadSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"num", "long"},
		{"name", "string"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column)
	{
		if (or instanceof SchemeCableThread)
		{
			SchemeCableThread thread = (SchemeCableThread)or;
			if (column.equals("name"))
				return thread.getName();
		}
		return "";
	}

	public long getLong(ObjectResource or, String column)
	{
		int num = 0;
		if (or instanceof SchemeCableThread)
		{
			SchemeCableThread thread = (SchemeCableThread)or;
			try {
				num = ResourceUtil.parseNumber(SchemeCableLink.parseName(thread.getName()));
			}
			catch (Exception ex) {
			}
		}
		return num;
	}
}