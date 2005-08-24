/*-
 * $Id: FilterTreeNodeHolder.java,v 1.7 2005/08/24 15:00:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/24 15:00:29 $
 * @module filter
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
