/**
 * $Id: MapLinkProtoElement.java,v 1.13 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapLinkProtoElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.io.IOException;
import java.io.Serializable;

import java.lang.UnsupportedOperationException;

import java.util.HashMap;
import java.util.Iterator;

/**
 * тип физической линии 
 * 
 * 
 * 
 * @version $Revision: 1.13 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */	
public final class MapLinkProtoElement extends StubResource
		implements Serializable
{
	private static final long serialVersionUID = 02L;
	/**
	 * @deprecated
	 */
	public static final String typ = "maplinkproto";

	public static final String TUNNEL = "tunnel";
	public static final String COLLECTIOR = "collector";
	public static final String UNBOUND = "cable";

	/**
	 * @deprecated
	 */
	protected String id = "";
	
	/**
	 * @deprecated
	 */
	protected String name = "";
	
	/**
	 * @deprecated
	 */
	protected String description = "";

	/** атрибуты отображения */
	public java.util.Map attributes = new HashMap();

	/**
	 * @deprecated
	 */
	protected MapLinkProtoElement_Transferable transferable;

	/**
	 * @deprecated
	 */
	protected long modified;
	
	/**
	 * Размерность тоннеля.
	 * Для тоннеля обозначает размерность матрицы труб в разрезе,
	 * для участка коллектора - число полок и мест на полках
	 */
	protected IntDimension bindingDimension;

	/** имя класса панели свойств объекта */
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapLinkProtoElement(
			String id,
			String name,
			String description,
			IntDimension bindingDimension)
	{
		super();
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setBindingDimension(bindingDimension);
	}
	
	/**
	 * @deprecated
	 */
	public MapLinkProtoElement(MapLinkProtoElement_Transferable transferable)
	{
		super();
		this.transferable = transferable;
		this.setLocalFromTransferable();
	}

	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.modified = transferable.modified;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	/**
	 * @deprecated
	 */
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.modified = modified;

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	/**
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
	}
	
	/**
	 * @deprecated
	 */
	public Object getTransferable()
	{
		return transferable;
	}
	
	/**
	 * @deprecated
	 */
	public String getTyp()
	{
		return typ;
	}

	/**
	 * @deprecated
	 */
	public String getDomainId()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @deprecated
	 */
	public long getModified()
	{
		return modified;
	}

	/**
	 * @deprecated
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	/**
	 * @deprecated
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeLong(modified);
		out.writeObject(attributes);
	}

	/**
	 * @deprecated
	 */
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		modified = in.readLong();
		attributes = (HashMap )in.readObject();

		transferable = new MapLinkProtoElement_Transferable();
	}


	public void setBindingDimension(IntDimension bindingDimension)
	{
		this.bindingDimension = bindingDimension;
	}


	public IntDimension getBindingDimension()
	{
		return bindingDimension;
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}

