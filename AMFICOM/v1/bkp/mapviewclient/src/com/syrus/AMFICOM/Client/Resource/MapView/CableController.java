/**
 * $Id: CableController.java,v 1.2 2004/12/08 16:20:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.AbstractLinkController;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementController;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.NodeLinkController;
import com.syrus.AMFICOM.Client.Resource.Map.PhysicalLinkController;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
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
 * @version $Revision: 1.2 $, $Date: 2004/12/08 16:20:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class CableController extends AbstractLinkController
{
	private static CableController instance = null;
	
	private CableController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new CableController();
		return instance;
	}

	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof MapCablePathElement))
			return false;

		MapCablePathElement cpath = (MapCablePathElement )me;

		boolean isv = cpath.isSelected();
		if(!isv)
		{
			for(Iterator it = cpath.getMapView().getMeasurementPaths(cpath).iterator(); it.hasNext();)
			{
				MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
				MeasurementPathController mpc = (MeasurementPathController )getLogicalNetLayer().getMapViewController().getController(mp);
				if(mpc.isSelectionVisible(mp))
				{
					isv = true;
					break;
				}
			}
		}
		return isv;
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof MapCablePathElement))
			return false;

		MapCablePathElement cpath = (MapCablePathElement )me;

		boolean vis = false;
		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();)
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
		if(! (me instanceof MapCablePathElement))
			return;

		MapCablePathElement cpath = (MapCablePathElement )me;
		
		if(!isElementVisible(cpath, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(cpath);
		Stroke str = new BasicStroke(
				getLineSize(cpath), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = getColor(cpath);

		paint(cpath, g, visibleBounds, str, color, isSelectionVisible(cpath));
	}

	public void paint(MapCablePathElement cpath, Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
	{
		if(!isElementVisible(cpath, visibleBounds))
			return;

		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof MapCablePathElement))
			return false;

		MapCablePathElement cpath = (MapCablePathElement )me;

		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(link);
			if(plc.isMouseOnElement(link, currentMousePoint))
				return true;
		}
		return false;
	}

	public double getDistanceFromStartLt(MapCablePathElement cpath, Point pt)
	{
		double distance = 0.0;

		MapNodeElement node = cpath.getStartNode();
		cpath.sortNodeLinks();
		for(Iterator it = cpath.getSortedNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(mnle);
			if(nlc.isMouseOnElement(mnle, pt))
			{
				DoublePoint dpoint = getLogicalNetLayer().convertScreenToMap(pt);
				distance += getLogicalNetLayer().distance(dpoint, node.getLocation());
				break;
			}
			else
				distance += mnle.getLengthLt();

			if(mnle.getStartNode().equals(node))
				node = mnle.getEndNode();
			else
				node = mnle.getStartNode();
		}
		return distance;
	}

	/**
	 * Возвращяет длинну линии пересчитанную на коэффициент топологической 
	 * привязки
	 */
	public double getDistanceFromStartLf(MapCablePathElement cpath, Point pt)
	{
		double kd = cpath.getKd();
		return getDistanceFromStartLt(cpath, pt) * kd;
	}

	public int getLineSize (MapLinkElement link)
	{
		return MapPropertiesManager.getUnboundThickness();
	}

	public String getStyle (MapLinkElement link)
	{
		return MapPropertiesManager.getStyle();
	}

	public Stroke getStroke (MapLinkElement link)
	{
		return MapPropertiesManager.getStroke();
	}

	public Color getColor(MapLinkElement link)
	{
		return MapPropertiesManager.getUnboundLinkColor();
	}

	public Color getAlarmedColor(MapLinkElement link)
	{
		return MapPropertiesManager.getAlarmedColor();
	}

	public int getAlarmedLineSize (MapLinkElement link)
	{
		return MapPropertiesManager.getAlarmedThickness();
	}
}
