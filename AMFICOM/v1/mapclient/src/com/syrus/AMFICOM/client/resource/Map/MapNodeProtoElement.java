/**
 * $Id: MapNodeProtoElement.java,v 1.4 2004/09/16 10:37:49 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.UI.MapDataFlavor;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * тип узла 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/09/16 10:37:49 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeProtoElement
		extends MapNodeElement
		implements Transferable, Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapprotoelement";
	
	public static final String WELL = "well";
	public static final String PIQUET = "piquet";
	public static final String ATS = "ats";
	public static final String BUILDING = "building";
	public static final String UNBOUND = "unbound";

	protected MapNodeProtoElement_Transferable transferable;

	protected long modified = 0;
	
	protected boolean isTopological = false;

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

	public MapNodeProtoElement(MapNodeProtoElement_Transferable transferable)
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
		this.description = transferable.description;
		this.imageId = transferable.symbolId;
		this.isTopological = transferable.isTopological;
		this.modified = transferable.modified;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

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

	public void updateLocalFromTransferable()
	{
		setImageId(imageId);
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
		return null;//new MapProtoModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapProtoDisplayModel();
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
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
		DataFlavor dataFlavor = new MapDataFlavor(this.getClass(), "ElementLabel");
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = dataFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
//    dfs[1] = DataFlavor.plainTextFlavor;
		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getHumanPresentableName().equals("ElementLabel"));
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
/*
class MapNodeProtoModel extends ObjectResourceModel
{
	MapNodeProtoElement mapproto;

	public MapNodeProtoModel(MapNodeProtoElement mapproto)
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
*/
/*
class MapNodeProtoDisplayModel extends StubDisplayModel
{
	TextFieldEditor tfe = new TextFieldEditor();
	
	public PropertyEditor getColumnEditor(ObjectResource o, String col_id)
	{
		if (!(o instanceof MapNodeProtoElement))
			return null;
		MapNodeProtoElement mapproto = (MapNodeProtoElement )o;

		if(col_id.equals("name"))
		{
			tfe.setText(mapproto.getName());
			return tfe;
		}
		return null;
	}

	public String getColumnName (String col_id)
	{
		String s = "";
		if(col_id.equals("name"))
			s = LangModelMap.getString("Name");
		return s;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource o, String col_id)
	{
		if (!(o instanceof MapNodeProtoElement))
			return null;
		MapNodeProtoElement mapproto = (MapNodeProtoElement )o;

		if(col_id.equals("name"))
		{
			tfe.setText(mapproto.getName());
			return tfe;
		}
		return null;
	}

	public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("name"))
			return true;
		return false;
	}

	List cols = new LinkedList();
	
	{
		cols.add("name");
	}

	public List getColumns()
	{
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
*/
