/*
 * $Id: FilterTreeNodeHolder.java,v 1.5 2005/04/13 19:09:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/13 19:09:41 $
 * @module filter_v1
 */
public class FilterTreeNodeHolder implements Serializable
{
	private static final long serialVersionUID = 3691035461384681266L;

	public String id = "";
	public int state = 0;
	public String name = "";

	public String parentId = "";
	public String[] childrenIds;

	public FilterTreeNodeHolder(String id, int state, String name, String parentId, String[] childrenIds)
	{
		this.id = id;
		this.state = state;
		this.name = name;

		this.parentId = parentId;

		this.childrenIds = new String [childrenIds.length];
		for (int i = 0; i < this.childrenIds.length; i++)
			this.childrenIds[i] = childrenIds[i];
	}

	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.writeObject(this.id);
		out.writeInt(this.state);
		out.writeObject(this.name);
		out.writeObject(this.parentId);
		out.writeObject(this.childrenIds);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		this.id = (String )in.readObject();
		this.state = in.readInt();
		this.name = (String )in.readObject();
		this.parentId = (String )in.readObject();
		this.childrenIds = (String[] )in.readObject();
	}
}
