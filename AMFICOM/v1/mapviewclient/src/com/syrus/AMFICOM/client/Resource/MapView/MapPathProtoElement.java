/**
 * $Id: MapPathProtoElement.java,v 1.1 2004/09/13 12:33:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

//import com.syrus.AMFICOM.CORBA.Map.MapPathProtoElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.MapView.MapPathElement;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;

/**
 * тип пути 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */	
public class MapPathProtoElement 
		extends MapPathElement
		implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mappathproto";
//	protected MapPathProtoElement_Transferable transferable;

	protected long modified;

//	public Vector pathtype_ids = new Vector();

	public MapPathProtoElement()
	{
		super("", null, null, null);
	}
	
//	public MapPathProtoElement( MapPathProtoElement_Transferable transferable)
//	{
//		super("", null, null, null);
//		this.transferable = transferable;
//		setLocalFromTransferable();
//	}
//
//	public void setLocalFromTransferable()
//	{
//		this.id = transferable.id;
//		this.name = transferable.name;
//		this.description = transferable.description;
//		this.modified = transferable.modified;
//
//		for(int i = 0; i < transferable.attributes.length; i++)
//			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
//	}
//
//	public void setTransferableFromLocal()
//	{
//		transferable.id = this.id;
//		transferable.name = this.name;
//		transferable.description = this.description;
//		transferable.modified = modified;
//
//		int l = this.attributes.size();
//		int i = 0;
//		transferable.attributes = new ElementAttribute_Transferable[l];
//		for(Iterator it = attributes.values().iterator(); it.hasNext();)
//		{
//			ElementAttribute ea = (ElementAttribute )it.next();
//			ea.setTransferableFromLocal();
//			transferable.attributes[i++] = ea.transferable;
//		}
//	}

	public void updateLocalFromTransferable()
	{
	}
	
	public String getTyp()
	{
		return typ;
	}

	public long getModified()
	{
		return modified;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(attributes);
		out.writeLong(modified);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		attributes = (HashMap )in.readObject();
		modified = in.readLong();

//		transferable = new MapPathProtoElement_Transferable();
	}
}

