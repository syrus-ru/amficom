/**
 * $Id: MapNodeProtoElement.java,v 1.12 2004/12/22 16:17:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

//import com.syrus.AMFICOM.Client.Map.UI.MapDataFlavor;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.SiteNodeType;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.io.Serializable;

/**
 * тип узла 
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2004/12/22 16:17:38 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeProtoElement
		extends SiteNodeType
		implements Transferable, Serializable
{
	private static final long serialVersionUID = 02L;
	
	public static final String WELL = "well";
	public static final String PIQUET = "piquet";
	public static final String ATS = "ats";
	public static final String BUILDING = "building";
	public static final String UNBOUND = "unbound";
	public static final String CABLE_INLET = "cableinlet";

	/**
	 * @deprecated
	 */
	protected boolean isTopological = false;

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapNodeProtoElement(
		String name,
		boolean isTopological,
		Identifier imageId,
		String description)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("sitenodetype"));
	    setImageId(imageId);
		setName(name);
		setDescription(description);
		setTopological(isTopological);
	}

	public void setTopological(boolean isTopological)
	{
		this.isTopological = isTopological;
	}

	public boolean isTopological()
	{
		return isTopological;
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public Object getTransferData(DataFlavor flavor)
	{
//		if (flavor.getHumanPresentableName().equals(MapDataFlavor.MAP_PROTO_LABEL))
//		{
//			return this;
//		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
//		DataFlavor dataFlavor = new MapDataFlavor(this.getClass(), MapDataFlavor.MAP_PROTO_LABEL);
//		DataFlavor[] dfs = new DataFlavor[2];
//		dfs[0] = dataFlavor;
//		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();
//		return dfs;
		return null;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
//		return (flavor.getHumanPresentableName().equals(MapDataFlavor.MAP_PROTO_LABEL));
		return false;
	}

}
