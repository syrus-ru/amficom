/**
 * $Id: CollectorController.java,v 1.1 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.CharacteristicType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.Identifier;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class CollectorController extends AbstractLinkController
{
	private static CollectorController instance = null;
	
	private CollectorController()
	{
	}
	
	public static CollectorController getInstance()
	{
		if(instance == null)
			instance = new CollectorController();
		return instance;
	}

	public boolean isSelectionVisible(MapElement me)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof MapPipePathElement))
			return false;

		MapPipePathElement collector = (MapPipePathElement )me;

		
		boolean vis = false;
		for(Iterator it = collector.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(link);
			if(plc.isElementVisible(link, visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof MapPipePathElement))
			return;

		MapPipePathElement collector = (MapPipePathElement )me;
		
		if(!isElementVisible(collector, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(collector);
		Stroke str = new BasicStroke(
				getLineSize(collector), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = getColor(collector);

		for(Iterator it = collector.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, str, color, false);
		}
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof MapPipePathElement))
			return false;

		MapPipePathElement collector = (MapPipePathElement )me;
		
		return false;
	}

}
