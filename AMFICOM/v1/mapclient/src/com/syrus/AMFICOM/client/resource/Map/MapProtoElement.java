// Copyright (c) 2002 Syrus
package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.Map.MapProtoElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MyDataFlavor;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;

public class MapProtoElement
		extends MapNodeElement
		implements Transferable, Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "mapprotoelement";
	MapProtoElement_Transferable transferable;

	public Vector pe_ids = new Vector();

//	protected String imageID = "";
	protected String codename = "";
	public long modified = 0;
	public boolean pe_is_kis = false;
	public boolean is_visual = false;
	public boolean is_topological = false;
	public String pe_class = "";

	public String domain_id = "";

	public MapProtoElement()
	{
		transferable = new MapProtoElement_Transferable();
	}

	public MapProtoElement(MapProtoElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		return null;
	}

	public void setLocalFromTransferable()
	{
				this.id = transferable.id;
				this.name = transferable.name;
				this.codename = transferable.codename;
				this.description = transferable.description;
				this.owner_id = transferable.owner_id;
				this.imageID = transferable.symbol_id;
		this.pe_is_kis = transferable.pe_is_kis;
		this.is_visual = transferable.is_visual;
		this.is_topological = transferable.is_topological;
		this.pe_class = transferable.pe_class;
		this.modified = transferable.modified;
		this.domain_id = transferable.domain_id;

		this.pe_ids = new Vector();
		for (int i = 0; i < transferable.pe_ids.length; i++)
			this.pe_ids.add( transferable.pe_ids[i]);

				for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
				transferable.id = this.id;
				transferable.name = this.name;
				transferable.codename = codename;
				transferable.description = this.description;
				transferable.owner_id = owner_id;
				transferable.symbol_id = this.imageID;
		transferable.pe_is_kis = pe_is_kis;
		transferable.is_visual = is_visual;
		transferable.is_topological = is_topological;
		transferable.pe_class = pe_class;
		transferable.modified = modified;
		transferable.domain_id = domain_id;

				int l = this.attributes.size();
				int i = 0;
				transferable.attributes = new ElementAttribute_Transferable[l];
				for(Enumeration e = attributes.elements(); e.hasMoreElements();)
				{
						ElementAttribute ea = (ElementAttribute )e.nextElement();
						ea.setTransferableFromLocal();
						transferable.attributes[i++] = ea.transferable;
				}

				l = this.pe_ids.size();
		transferable.pe_ids = new String[l];
		for (i = 0; i < pe_ids.size(); i++)
			transferable.pe_ids[i] = (String )pe_ids.get(i);
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
		return domain_id;
	}

	public void updateLocalFromTransferable()
	{
		setImageID(imageID);
	}

	public String getEquipmentTypeId()
	{
		return "";
	}

	public String getValue(String col_id)
	{
		String s = "";
		try
		{
			if(col_id.equals("id"))
				s = id;
			if(col_id.equals("name"))
				s = name;
			if(col_id.equals("codename"))
				s = codename;
		}
		catch(Exception e)
		{
//      System.out.println("error gettin field value - MapProto");
			s = "";
		}

		return s;
	}

	public Hashtable getPropertyColumns()
	{
		Hashtable cols = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			cols.put(ea.getId(), ea.getName());
		}
		return cols;
	}

	public String getPropertyValue(String col_id)
	{
		return (String )attributes.get(col_id);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public long getModified()
	{
		return modified;
	}

	public ObjectResourceModel getModel()
	{
		return new MapProtoModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapProtoDisplayModel();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(description);
		out.writeObject(owner_id);
		out.writeObject(imageID);
		out.writeBoolean(pe_is_kis);
		out.writeBoolean(is_visual);
		out.writeBoolean(is_topological);
		out.writeObject(pe_class);
		out.writeObject(pe_ids);
		out.writeObject(attributes);
		out.writeObject(domain_id);
		out.writeLong(modified);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		description = (String )in.readObject();
		owner_id = (String )in.readObject();
		imageID = (String )in.readObject();
		pe_is_kis = in.readBoolean();
		is_visual = in.readBoolean();
		is_topological = in.readBoolean();
		pe_class = (String )in.readObject();

		pe_ids = (Vector )in.readObject();
		attributes = (Hashtable )in.readObject();
		domain_id = (String )in.readObject();
		modified = in.readLong();

		transferable = new MapProtoElement_Transferable();
	}

//////////////////////////////
	public JPopupMenu getContextMenu(JFrame myFrame)
	{
		return null;
	}

	public Object getTransferData(DataFlavor flavor)
	{
		if (flavor.getHumanPresentableName()=="ElementLabel")
		{
			return (Object) (this);
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor myDataFlavor = new MyDataFlavor(this.getClass(),"ElementLabel");
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = myDataFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
//    dfs[1] = DataFlavor.plainTextFlavor;
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getHumanPresentableName()=="ElementLabel");
	}

}

class MapProtoModel extends ObjectResourceModel
{
	MapProtoElement mapproto;

	public MapProtoModel(MapProtoElement mapproto)
	{
		this.mapproto = mapproto;
	}

	public String getColumnValue(String col_id)
	{
		String s = "";
		try
		{
			if(col_id.equals("name"))
				s = mapproto.getName();
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Scheme");
			s = "";
		}
		return s;
	}
}

class MapProtoDisplayModel extends StubDisplayModel
{
	public PropertyEditor getColumnEditor(ObjectResource o, String col_id)
	{
		if (!(o instanceof MapProtoElement))
			return null;
		MapProtoElement mapproto = (MapProtoElement)o;

		if(col_id.equals("name"))
			return new TextFieldEditor(mapproto.getName());
		return null;
	}

	public String getColumnName (String col_id)
	{
		String s = "";
		if(col_id.equals("name"))
			s = "Название";
		return s;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource o, String col_id)
	{
		if (!(o instanceof MapProtoElement))
			return null;
		MapProtoElement mapproto = (MapProtoElement)o;

		if(col_id.equals("name"))
			return new TextFieldEditor(mapproto.getName());
		return null;
	}

	public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("name"))
			return true;
		return false;
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("name");
		return cols;
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("name"))
			return 100;
		return 100;
	}

	public Color getColumnColor (ObjectResource o, String col_id)
	{
		return Color.white;
	}

	public boolean isColumnColored (String col_id)
	{
		return false;
	}

}
