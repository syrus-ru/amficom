/**
 * $Id: CableController.java,v 1.2 2005/01/30 15:38:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.MeasurementPathController;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/30 15:38:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class CableController extends AbstractLinkController
{
	private static CableController instance = null;
	
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
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
		if(! (me instanceof CablePath))
			return false;

		CablePath cpath = (CablePath)me;

		boolean isv = cpath.isSelected();
		if(!isv)
		{
			for(Iterator it = getLogicalNetLayer().getMapViewController().getMeasurementPaths(cpath).iterator(); it.hasNext();)
			{
				MeasurementPath mp = (MeasurementPath)it.next();
				MeasurementPathController mpc = (MeasurementPathController)getLogicalNetLayer().getMapViewController().getController(mp);
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
		if(! (me instanceof CablePath))
			return false;

		CablePath cpath = (CablePath)me;

		boolean vis = false;
		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			PhysicalLinkController plc = (PhysicalLinkController)getLogicalNetLayer().getMapViewController().getController(link);
			if(plc.isElementVisible(link, visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof CablePath))
			return null;

		CablePath cpath = (CablePath)me;
		
		String s1 = cpath.getName();
		String s2 = "";
		String s3 = "";
		try
		{
			AbstractNode smne = cpath.getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ LangModel.getString("node" + smne.getClass().getName()) 
				+ "]";
			AbstractNode emne = cpath.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ LangModel.getString("node" + emne.getClass().getName()) 
				+ "]";
		}
		catch(Exception e)
		{
			Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getToolTipText()", 
				e);
		}
		return s1 + s2 + s3;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof CablePath))
			return;

		CablePath cpath = (CablePath)me;
		
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

	public void paint(CablePath cpath, Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
	{
		if(!isElementVisible(cpath, visibleBounds))
			return;

		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			PhysicalLinkController plc = (PhysicalLinkController)getLogicalNetLayer().getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof CablePath))
			return false;

		CablePath cpath = (CablePath)me;

		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			PhysicalLinkController plc = (PhysicalLinkController)getLogicalNetLayer().getMapViewController().getController(link);
			if(plc.isMouseOnElement(link, currentMousePoint))
				return true;
		}
		return false;
	}

	public double getDistanceFromStartLt(CablePath cpath, Point pt)
	{
		double distance = 0.0;

		AbstractNode node = cpath.getStartNode();
		cpath.sortNodeLinks();
		for(Iterator it = cpath.getSortedNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();
			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer().getMapViewController().getController(mnle);
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
	public double getDistanceFromStartLf(CablePath cpath, Point pt)
	{
		double kd = cpath.getKd();
		return getDistanceFromStartLt(cpath, pt) * kd;
	}

	public int getLineSize (MapElement link)
	{
		return MapPropertiesManager.getUnboundThickness();
	}

	public String getStyle (MapElement link)
	{
		return MapPropertiesManager.getStyle();
	}

	public Stroke getStroke (MapElement link)
	{
		return MapPropertiesManager.getStroke();
	}

	public Color getColor(MapElement link)
	{
		return MapPropertiesManager.getUnboundLinkColor();
	}

	public Color getAlarmedColor(MapElement link)
	{
		return MapPropertiesManager.getAlarmedColor();
	}

	public int getAlarmedLineSize (MapElement link)
	{
		return MapPropertiesManager.getAlarmedThickness();
	}
}
