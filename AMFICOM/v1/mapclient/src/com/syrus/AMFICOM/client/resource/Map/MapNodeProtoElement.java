/**
 * $Id: MapNodeProtoElement.java,v 1.10 2004/12/07 17:02:03 krupenn Exp $
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
import com.syrus.AMFICOM.CORBA.Map.MapNodeProtoElement_Transferable;
import com.syrus.AMFICOM.Client.Map.UI.MapDataFlavor;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * тип узла 
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2004/12/07 17:02:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeProtoElement
		extends MapNodeElement
		implements Transferable, Serializable
{
	private static final long serialVersionUID = 02L;
	/**
	 * @deprecated
	 */
	public static final String typ = "mapprotoelement";
	
	public static final String WELL = "well";
	public static final String PIQUET = "piquet";
	public static final String ATS = "ats";
	public static final String BUILDING = "building";
	public static final String UNBOUND = "unbound";
	public static final String CABLE_INLET = "cableinlet";

	/**
	 * @deprecated
	 */
	protected MapNodeProtoElement_Transferable transferable;

	/**
	 * @deprecated
	 */
	protected long modified = 0;
	
	/**
	 * @deprecated
	 */
	protected boolean isTopological = false;

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapNodeProtoElement(
		String id,
		String name,
		boolean isTopological,
		String imageId,
		String description)
	{
		super();
	    setImageId(imageId);
		setName(name);
		setId(id);
		setDescription(description);
		setTopological(isTopological);
		transferable = new MapNodeProtoElement_Transferable();
	}

	/**
	 * @deprecated
	 */
	public MapNodeProtoElement(MapNodeProtoElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	/**
	 * @deprecated
	 */
	public Object clone(DataSourceInterface dataSource)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.imageId = transferable.symbolId;
		this.isTopological = transferable.isTopological;
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
		transferable.symbolId = this.imageId;
		transferable.isTopological = isTopological;
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
	public String getTyp()
	{
		return typ;
	}

	public void setTopological(boolean isTopological)
	{
		this.isTopological = isTopological;
	}

	public boolean isTopological()
	{
		return isTopological;
	}

	public String getDomainId()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
		setImageId(imageId);
	}

	/**
	 * @deprecated
	 */
	public Object getTransferable()
	{
		return transferable;
	}

	public long getModified()
	{
		return modified;
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public Object getTransferData(DataFlavor flavor)
	{
		if (flavor.getHumanPresentableName().equals(MapDataFlavor.MAP_PROTO_LABEL))
		{
			return this;
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor dataFlavor = new MapDataFlavor(this.getClass(), MapDataFlavor.MAP_PROTO_LABEL);
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = dataFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getHumanPresentableName().equals(MapDataFlavor.MAP_PROTO_LABEL));
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(imageId);
		out.writeBoolean(isTopological);
		out.writeObject(attributes);
		out.writeLong(modified);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		imageId = (String )in.readObject();
		isTopological = in.readBoolean();

		attributes = (HashMap )in.readObject();
		modified = in.readLong();

		transferable = new MapNodeProtoElement_Transferable();
	}
}
