package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.awt.*;
import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.CORBA.Scheme.MapProtoGroup_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapProtoElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MyDataFlavor;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

public class MapProtoGroup extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	MapProtoGroup_Transferable transferable;

	public static final String typ = "mapprotogroup";

	public String id = "";
	public String name = "";
	public String parent_id = "";
	public Vector group_ids = new Vector();
	public Vector mapproto_ids = new Vector();
	public long modified = 0L;

	public MapProtoGroup()
	{
		transferable = new MapProtoGroup_Transferable();
	}

	public MapProtoGroup(MapProtoGroup_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
				this.id = transferable.id;
				this.name = transferable.name;
				this.parent_id = transferable.parent_id;
		this.modified = transferable.modified;

		this.group_ids = new Vector();
		for (int i = 0; i < transferable.group_ids.length; i++)
			this.group_ids.add( transferable.group_ids[i]);

		this.mapproto_ids = new Vector();
		for (int i = 0; i < transferable.mapproto_ids.length; i++)
			this.mapproto_ids.add( transferable.mapproto_ids[i]);
	}

	public void setTransferableFromLocal()
	{
		int l;
		int i;

				transferable.id = id;
				transferable.name = name;
		transferable.parent_id = parent_id;
		transferable.modified = modified;

				l = this.group_ids.size();
		transferable.group_ids = new String[l];
		for (i = 0; i < group_ids.size(); i++)
			transferable.group_ids[i] = (String )group_ids.get(i);

				l = this.mapproto_ids.size();
		transferable.mapproto_ids = new String[l];
		for (i = 0; i < mapproto_ids.size(); i++)
			transferable.mapproto_ids[i] = (String )mapproto_ids.get(i);
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getTyp()
	{
		return typ;
	}

	public long getModified()
	{
		return modified;
	}

	public ObjectResourceModel getModel()
	{
		return new MapProtoGroupModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MapProtoGroupDisplayModel();
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public Object getTransferable()
	{
		return transferable;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(parent_id);
		out.writeLong(modified);
		out.writeObject(group_ids);
		out.writeObject(mapproto_ids);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		parent_id = (String )in.readObject();

		modified = in.readLong();
		group_ids = (Vector )in.readObject();
		mapproto_ids = (Vector )in.readObject();

		transferable = new MapProtoGroup_Transferable();
	}
}

class MapProtoGroupModel extends ObjectResourceModel
{
	MapProtoGroup mapproto;

	public MapProtoGroupModel(MapProtoGroup mapproto)
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

class MapProtoGroupDisplayModel extends StubDisplayModel
{
	public PropertyEditor getColumnEditor(ObjectResource o, String col_id)
	{
		if (!(o instanceof MapProtoGroup))
			return null;
		MapProtoGroup mapproto = (MapProtoGroup)o;

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
		if (!(o instanceof MapProtoGroup))
			return null;
		MapProtoGroup mapproto = (MapProtoGroup)o;

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
