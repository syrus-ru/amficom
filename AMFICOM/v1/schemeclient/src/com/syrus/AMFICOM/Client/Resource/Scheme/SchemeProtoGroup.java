package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;
import java.util.List;

import java.awt.Color;

import com.syrus.AMFICOM.CORBA.Scheme.SchemeProtoGroup_Transferable;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class SchemeProtoGroup extends StubResource implements Serializable
{
	private static final long serialVersionUID = 02L;
	SchemeProtoGroup_Transferable transferable;

	public static final String typ = "schemeprotogroup";

	public String id = "";
	public String name = "";
	public String parentId = "";
	public String description = "";
	protected String imageID = "";
	public String peClass = "";

	public Collection groupIds;
	private Collection protoIds;
	public long modified = 0L;

//	boolean peIsKis;

	public SchemeProtoGroup()
	{
		groupIds = new LinkedList();
		protoIds = new LinkedList();
		transferable = new SchemeProtoGroup_Transferable();
	}

	public SchemeProtoGroup(SchemeProtoGroup_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		parentId = transferable.parentId;
		modified = transferable.modified;
		peClass = transferable.peClass;
		imageID = transferable.symbolId;

		this.groupIds = new ArrayList(transferable.groupIds.length);
		for (int i = 0; i < transferable.groupIds.length; i++)
			this.groupIds.add(transferable.groupIds[i]);

		this.protoIds = new ArrayList(transferable.peIds.length);
		for (int i = 0; i < transferable.peIds.length; i++)
			this.protoIds.add( transferable.peIds[i]);
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.parentId = parentId;
		transferable.modified = modified;
		transferable.peClass = peClass;
		transferable.symbolId = imageID;

		transferable.groupIds = new String[groupIds.size()];
		Iterator it = groupIds.iterator();
		for (int i = 0; i < groupIds.size(); i++)
			transferable.groupIds[i] = (String)it.next();

		transferable.peIds = new String[protoIds.size()];
		it = protoIds.iterator();
		for (int i = 0; i < protoIds.size(); i++)
			transferable.peIds[i] = (String)it.next();
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

	public String getImageID()
	{
		return imageID;
	}

	public void setImageID(String imageID)
	{
		this.imageID = imageID;
	}

	public Collection getProtoIds()
	{
		return protoIds;
	}

	public ObjectResourceModel getModel()
	{
		return new SchemeProtoGroupModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new SchemeProtoGroupDisplayModel();
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
		out.writeObject(description);
		out.writeObject(parentId);
		out.writeObject(imageID);
		out.writeObject(peClass);
		out.writeLong(modified);
		out.writeObject(groupIds);
		out.writeObject(protoIds);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String)in.readObject();
		name = (String)in.readObject();
		description = (String)in.readObject();
		parentId = (String)in.readObject();
		imageID = (String)in.readObject();
		peClass = (String)in.readObject();
		modified = in.readLong();
		groupIds = (Collection)in.readObject();
		protoIds = (Collection)in.readObject();

		transferable = new SchemeProtoGroup_Transferable();
	}
}

class SchemeProtoGroupModel extends ObjectResourceModel
{
	SchemeProtoGroup protogroup;

	public SchemeProtoGroupModel(SchemeProtoGroup protogroup)
	{
		this.protogroup = protogroup;
	}

	public String getColumnValue(String colId)
	{
		String s = "";
		if(colId.equals("name"))
			s = protogroup.getName();
		return s;
	}
}

class SchemeProtoGroupDisplayModel extends StubDisplayModel
{
	public PropertyEditor getColumnEditor(ObjectResource o, String colId)
	{
		if (!(o instanceof SchemeProtoGroup))
			return null;
		SchemeProtoGroup protogroup = (SchemeProtoGroup)o;

		if(colId.equals("name"))
			return new TextFieldEditor(protogroup.getName());
		return null;
	}

	public String getColumnName (String colId)
	{
		String s = "";
		if(colId.equals("name"))
			s = "Название";
		return s;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource o, String colId)
	{
		if (!(o instanceof SchemeProtoGroup))
			return null;
		SchemeProtoGroup protogroup = (SchemeProtoGroup)o;

		if(colId.equals("name"))
			return new TextFieldEditor(protogroup.getName());
		return null;
	}

	public boolean isColumnEditable(String colId)
	{
		if(colId.equals("name"))
			return true;
		return false;
	}

	public List getColumns()
	{
		Vector cols = new Vector();
		cols.add("name");
		return cols;
	}

	public int getColumnSize(String colId)
	{
		if(colId.equals("name"))
			return 100;
		return 100;
	}

	public Color getColumnColor (ObjectResource o, String colId)
	{
		return Color.white;
	}

	public boolean isColumnColored (String colId)
	{
		return false;
	}
}
