package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;

import java.awt.datatransfer.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Schematics.UI.SchemeCableLinkPane;

public class SchemeCableLink extends StubResource
		implements Transferable, Serializable
{
	public static final String typ = "schemecablelink";
	private static final long serialVersionUID = 01L;
	public SchemeCableLink_Transferable transferable;

	public String id = "";
	public String name = "";
	public String source_port_id = "";
	public String target_port_id = "";
	public String cable_link_id = "";
	public String cable_link_type_id = "";

	public double optical_length = 0;
	public double physical_length = 0;

	public Collection cable_threads = new ArrayList();
	public Map attributes = new HashMap();

	public MapPhysicalLinkProtoElement mplpe = null;

	public boolean alarmed = false;

	public SchemeCableLink(SchemeCableLink_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public SchemeCableLink(String id)
	{
		this.id = id;
		transferable = new SchemeCableLink_Transferable();
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

	public SchemeCableThread getCableThread(String thread_id)
	{
		for (Iterator it = cable_threads.iterator(); it.hasNext();)
		{
			SchemeCableThread thread = (SchemeCableThread)it.next();
			if (thread.getId().equals(thread_id))
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

	public static PropertiesPanel getPropertyPane()
	{
		System.out.println("Getting CableLink property pane");
		return new SchemeCableLinkPane();
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
		name = transferable.name;
		source_port_id = transferable.source_port_id;
		target_port_id = transferable.target_port_id;
		cable_link_id = transferable.cable_link_id;
		cable_link_type_id = transferable.cable_link_type_id;

		try
		{
			optical_length = Double.parseDouble(transferable.optical_length);
		}
		catch (NumberFormatException ex)
		{
			optical_length = 0;
		}
		try
		{
			physical_length = Double.parseDouble(transferable.physical_length);
		}
		catch (NumberFormatException ex)
		{
			physical_length = 0;
		}

		cable_threads = new ArrayList();
		for (int i = 0; i < transferable.cable_threads.length; i++)
			cable_threads.add(new SchemeCableThread(transferable.cable_threads[i]));

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id  = id;
		transferable.name = name;
		transferable.source_port_id = source_port_id;
		transferable.target_port_id = target_port_id;
		transferable.cable_link_id = cable_link_id;
		transferable.cable_link_type_id = cable_link_type_id;

		transferable.optical_length = String.valueOf(optical_length);
		transferable.physical_length = String.valueOf(physical_length);

		transferable.cable_threads = new SchemeCableThread_Transferable[cable_threads.size()];
		int counter = 0;
		for (Iterator it = cable_threads.iterator(); it.hasNext();)
		{
			SchemeCableThread cable_thread = (SchemeCableThread)it.next();
			cable_thread.setTransferableFromLocal();
			transferable.cable_threads[counter++] = (SchemeCableThread_Transferable)cable_thread.getTransferable();
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
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			SchemeCableLink cloned_link = (SchemeCableLink)Pool.get(SchemeCableLink.typ, cloned_id);
			if (cloned_link == null)
				System.err.println("SchemeCableLink.clone() id not found: " + cloned_id);
			else
				return cloned_link;
		}

		SchemeCableLink link = new SchemeCableLink(dataSource.GetUId(SchemeCableLink.typ));

		link.name = name;
		link.cable_link_id = cable_link_id;
		link.cable_link_type_id = cable_link_type_id;
		link.source_port_id = (String)Pool.get("clonedids", source_port_id);
		if (link.source_port_id == null)
			link.source_port_id = source_port_id;
		link.target_port_id = (String)Pool.get("clonedids", target_port_id);
		if (link.target_port_id == null)
			link.target_port_id = target_port_id;

		link.optical_length = optical_length;
		link.physical_length = physical_length;

		if (!link.source_port_id.equals(""))
		{
			SchemeCablePort source_port = (SchemeCablePort)Pool.get(SchemeCablePort.typ, link.source_port_id);
			source_port.cable_link_id = link.getId();
		}
		if (!link.target_port_id.equals(""))
		{
			SchemeCablePort target_port = (SchemeCablePort)Pool.get(SchemeCablePort.typ, link.target_port_id);
			target_port.cable_link_id = link.getId();
		}

		link.cable_threads = new ArrayList(cable_threads.size());
		for (Iterator it = cable_threads.iterator(); it.hasNext();)
			link.cable_threads.add(((SchemeCableThread)it.next()).clone(dataSource));

		link.attributes = ResourceUtil.copyAttributes(dataSource, attributes);

		Pool.put(SchemeCableLink.typ, link.getId(), link);
		Pool.put("clonedids", id, link.id);
		return link;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(source_port_id);
		out.writeObject(target_port_id);
		out.writeObject(cable_link_id);
		out.writeObject(cable_link_type_id);
		out.writeObject(cable_threads);
		out.writeDouble(optical_length);
		out.writeDouble(physical_length);
		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		source_port_id = (String )in.readObject();
		target_port_id = (String )in.readObject();
		cable_link_id = (String )in.readObject();
		cable_link_type_id = (String )in.readObject();
		cable_threads = (Collection )in.readObject();
		optical_length = in.readDouble();
		physical_length = in.readDouble();
		attributes = (Map )in.readObject();

		transferable = new SchemeCableLink_Transferable();
	}

//	double physical_length = 0.0D;

	public double getPhysicalLength()
	{
		if (!cable_link_id.equals(""))
		{
			CableLink link = (CableLink)Pool.get(CableLink.typ, cable_link_id);
			if (link != null)
				return link.physical_length;
		}
		return physical_length;
	}

	public void setPhysicalLength(double d)
	{
		physical_length = d;
		if (!cable_link_id.equals(""))
		{
			CableLink link = (CableLink)Pool.get(CableLink.typ, cable_link_id);
			if (link != null)
				link.physical_length = d;
		}
	}

	public double getOpticalLength()
	{
		if (!cable_link_id.equals(""))
		{
			CableLink link = (CableLink)Pool.get(CableLink.typ, cable_link_id);
			if (link != null)
				return link.optical_length;
		}
		return optical_length;
	}

	public void setOpticalLength(double d)
	{
		optical_length = d;
		if (!cable_link_id.equals(""))
		{
			CableLink link = (CableLink)Pool.get(CableLink.typ, cable_link_id);
			if (link != null)
				link.optical_length = d;
		}
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

	public String getColumnValue(String col_id)
	{
		String s = "";
		try
		{
			if(col_id.equals("optimizerRibAttribute"))
			{
				ElementAttribute ea = (ElementAttribute )scl.attributes.get("optimizerRibAttribute");
				s = ea.value;
			}
			if(col_id.equals("name"))
			{
				s = scl.getName();
			}
			if(col_id.equals("id"))
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

	public void setColumnValue(String col_id, Object obj)
	{
		try
		{
			if(col_id.equals("optimizerRibAttribute"))
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
