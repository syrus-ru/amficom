/**
 * $Id: MapMarkElement.java,v 1.23 2004/12/23 16:35:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.Mark;

/**
 * Метка имеет координаты и привязана по дистанции к тоннелю (MapLink) 
 * 
 * 
 * 
 * @version $Revision: 1.23 $, $Date: 2004/12/23 16:35:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @deprecated
 */
public final class MapMarkElement extends Mark
{
	private static final long serialVersionUID = 02L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";

	/**
	 * @deprecated
	 */
	public static final String IMAGE_NAME = "mark";

	public static String[][] exportColumns = null;
	
	public MapMarkElement()
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("mark"));
//		this.setIconName(IMAGE_NAME);
	}
	
	public MapMarkElement(
			Map map,
			MapPhysicalLinkElement link,
			double len)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("mark"));
		this.setName(id.toString());
		this.setMap(map);
		this.setPhysicalLink(link);
//		this.setIconName(IMAGE_NAME);
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
/*
	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[7][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
			exportColumns[4][0] = COLUMN_DISTANCE;
			exportColumns[5][0] = COLUMN_X;
			exportColumns[6][0] = COLUMN_Y;
		}
		exportColumns[0][1] = getId().toString();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getPhysicalLink().getId().toString();
		exportColumns[4][1] = String.valueOf(getDistance());
		exportColumns[5][1] = String.valueOf(getLocation().x);
		exportColumns[6][1] = String.valueOf(getLocation().y);
		
		return exportColumns;
	}
	
	public void setColumn(String field, String value)
	{
		if(field.equals(COLUMN_ID))
			this.setId(value);
		else
		if(field.equals(COLUMN_NAME))
			this.setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			this.setDescription(value);
		else
		if(field.equals(COLUMN_PHYSICAL_LINK_ID))
			linkId = value;
		else
		if(field.equals(COLUMN_DISTANCE))
			distance = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_X))
			location.x = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_Y))
			location.y = Double.parseDouble(value);
	}
*/
/*	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(bounds);
		out.writeObject(mapId);
		out.writeObject(linkId);
		out.writeDouble(anchor.x);
		out.writeDouble(anchor.y);
		out.writeDouble(distance);
		out.writeObject(imageId);

		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		bounds = (Rectangle )in.readObject();
		mapId = (String )in.readObject();
		linkId = (String )in.readObject();
		double x = in.readDouble();
		double y = in.readDouble();
		anchor = new Point2D.Double(x, y);
		distance = in.readDouble();
		imageId = (String )in.readObject();

		attributes = (HashMap )in.readObject();

		transferable = new MapMarkElement_Transferable();
		this.updateLocalFromTransferable();

//		this.moveToFromStart(this.distance);
	}
*/

}
