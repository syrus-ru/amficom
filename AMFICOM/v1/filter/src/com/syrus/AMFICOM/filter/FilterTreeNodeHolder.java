package com.syrus.AMFICOM.filter;

import java.io.*;

public class FilterTreeNodeHolder implements Serializable
{
	public String id = "";
	public int state = 0;
	public String name = "";
	  
	public String parent_id = "";
	public String[] children_ids;

	public FilterTreeNodeHolder(String id, int state, String name, String parent_id, String[] children_ids)
	{
		this.id = id;
		this.state = state;
		this.name = name;
		
		this.parent_id = parent_id;
		
		this.children_ids = new String [children_ids.length];
		for (int i = 0; i < this.children_ids.length; i++)
			this.children_ids[i] = new String(children_ids[i]);
	}

	private void writeObject(java.io.ObjectOutputStream out)
		throws IOException
	{
		out.writeObject(id);
		out.writeInt(state);
		out.writeObject(name);
		out.writeObject(parent_id);
		out.writeObject(children_ids);
	}

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		state = in.readInt();
		name = (String )in.readObject();
		parent_id = (String )in.readObject();
		children_ids = (String[] )in.readObject();
	}
}