/**
 * $Id: SiteNodeController.java,v 1.4 2004/12/23 16:57:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.SiteNode;

import com.syrus.AMFICOM.map.SiteNodeType;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class SiteNodeController extends AbstractNodeController
{
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_PROTO_ID = "proto_id";	
	public static final String COLUMN_CITY = "city";	
	public static final String COLUMN_STREET = "street";	
	public static final String COLUMN_BUILDING = "building";	
	public static final String COLUMN_IMAGE_ID = "image_id";

	private static SiteNodeController instance = null;
	
	protected SiteNodeController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new SiteNodeController();
		return instance;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof SiteNode))
			return;
		SiteNode site = (SiteNode)me;

		if(!isElementVisible(site, visibleBounds))
			return;
		
		super.paint(site, g, visibleBounds);
		
		if(MapPropertiesManager.isShowNodesNames())
		{
			MapCoordinatesConverter converter = getLogicalNetLayer();
			
			Point p = converter.convertMapToScreen(site.getLocation());
	
			int width = getBounds(site).width;
			int height = getBounds(site).height;

			g.setColor(MapPropertiesManager.getBorderColor());
			g.setFont(MapPropertiesManager.getFont());

			int fontHeight = g.getFontMetrics().getHeight();
			String text = site.getName();
			int textWidth = g.getFontMetrics().stringWidth(text);
			int centerX = p.x + width / 2;
			int centerY = p.y + height;

			g.drawRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextBackground());
			g.fillRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextColor());
			g.drawString(
					text,
					centerX,
					centerY);
		}
	}
/*
	protected static Object[][] exportColumns = null;

	public Object[][] exportColumns(SiteNode site)
	{
		if(exportColumns == null)
		{
			exportColumns = new Object[10][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PROTO_ID;
			exportColumns[4][0] = COLUMN_X;
			exportColumns[5][0] = COLUMN_Y;
			exportColumns[6][0] = COLUMN_CITY;
			exportColumns[7][0] = COLUMN_STREET;
			exportColumns[8][0] = COLUMN_BUILDING;
			exportColumns[9][0] = COLUMN_IMAGE_ID;
		}
		exportColumns[0][1] = site.getId().toString();
		exportColumns[1][1] = site.getName();
		exportColumns[2][1] = site.getDescription();
		exportColumns[3][1] = site.getType().getCodename();
		exportColumns[4][1] = String.valueOf(site.getLocation().getX());
		exportColumns[5][1] = String.valueOf(site.getLocation().getY());
		exportColumns[6][1] = site.getCity();
		exportColumns[7][1] = site.getStreet();
		exportColumns[8][1] = site.getBuilding();
		exportColumns[9][1] = site.getImageId().toString();
		
		return exportColumns;
	}

	public SiteNode importColumns(Object[][] exportColumns)
		throws CreateObjectException
	{
		Identifier id;
		String name;
		String description;
		SiteNodeType type;
		double x;
		double y;
		String city;
		String building;
		String street;
		Identifier imageId;
		
		for (int i = 0; i < exportColumns.length; i++) 
		{
			String field = (String )exportColumns[i][0];
			Object value = exportColumns[i][1];

			if(field.equals(COLUMN_ID))
				id = (Identifier )value;
			else
			if(field.equals(COLUMN_NAME))
				name = (String )value;
			else
			if(field.equals(COLUMN_DESCRIPTION))
				description = (String )value;
			else
			if(field.equals(COLUMN_PROTO_ID))
			{
				try
				{
					type = MapStorableObjectPool.getStorableObject(
							(Identifier )value,
							false);
				}
				catch (CommunicationException e)
				{
					throw new CreateObjectException("Import Site Node: " + e.getMessage());
				}
				catch (DatabaseException e)
				{
					throw new CreateObjectException("Import Site Node: " + e.getMessage());
				}
			}
			else
			if(field.equals(COLUMN_X))
				x = Double.parseDouble((String )value);
			else
			if(field.equals(COLUMN_Y))
				y = Double.parseDouble((String )value);
			else
			if(field.equals(COLUMN_CITY))
				city = (String )value;
			else
			if(field.equals(COLUMN_STREET))
				street = (String )value;
			else
			if(field.equals(COLUMN_BUILDING))
				building = (String )value;
			else
			if(field.equals(COLUMN_IMAGE_ID))
			{
				String codeName = (String )value;
				imageId = getLogicalNetLayer().getImageId(codeName, codeName);
			}
		}
		
		return SiteNode.createInstance(
				id,
				name,
				description,
				type,
				x,
				y,
				city,
				building,
				street,
				imageId);
	}
	
	public void setColumn(String field, Object value)
		throws CreateObjectException
	{
		if(field.equals(COLUMN_ID))
			super.id = (Identifier )value;
		else
		if(field.equals(COLUMN_NAME))
			super.name = (String )value;
		else
		if(field.equals(COLUMN_DESCRIPTION))
			super.description = (String )value;
		else
		if(field.equals(COLUMN_PROTO_ID))
		{
			try
			{
				SiteNodeType type = MapStorableObjectPool.getStorableObject(
						(Identifier )value,
						false);
				setType(type);
			}
			catch (CommunicationException e)
			{
				throw new CreateObjectException("Import Site Node: " + e.getMessage());
			}
			catch (DatabaseException e)
			{
				throw new CreateObjectException("Import Site Node: " + e.getMessage());
			}
		}
		else
		if(field.equals(COLUMN_X))
			location.x = Double.parseDouble((String )value);
		else
		if(field.equals(COLUMN_Y))
			location.y = Double.parseDouble((String )value);
		else
		if(field.equals(COLUMN_CITY))
			setCity((String )value);
		else
		if(field.equals(COLUMN_STREET))
			setStreet((String )value);
		else
		if(field.equals(COLUMN_BUILDING))
			setBuilding((String )value);
		else
		if(field.equals(COLUMN_IMAGE_ID))
			setIconName(value);
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapSitePane";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
*/
}
