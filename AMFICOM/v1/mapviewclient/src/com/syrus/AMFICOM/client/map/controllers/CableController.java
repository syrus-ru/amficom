/**
 * $Id: CableController.java,v 1.3 2005/01/31 12:19:18 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;

import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItemDefaultFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;

/**
 * Контроллер кабеля.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/01/31 12:19:18 $
 * @module mapviewclient_v1
 */
public final class CableController extends AbstractLinkController
{
	private static CableController instance = null;

	/**
	 * Фабричный объект создания привязки кабеля к линии.
	 */
	private static CableChannelingItemDefaultFactory cciFactory = new CableChannelingItemDefaultFactory();
	
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * Отрисовать все линии, из которых состоит кабель, с заданным стилем.
	 * и цветом линии
	 * @param cpath кабель
	 * @param g графический контекст
	 * @param visibleBounds видимая область
	 * @param stroke стиль линии
	 * @param color цвет линии
	 * @param selectionVisible рисовать рамку выделения
	 */
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
	 * {@inheritDoc}
	 * <br>Точка находится на кабеле, если она находится на любой линии,
	 * которая входит в кабель.
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

	/**
	 * Создать новый объект привязки к линии.
	 * @param link лниия
	 * @return объект привязки, или <code>null</code> при возникновении ошибки
	 */
	public static CableChannelingItem generateCCI(PhysicalLink link)
	{
		CableChannelingItem cci = cciFactory.newInstance();
		cci.startSiteNodeImpl((SiteNode )link.getStartNode());
		if(! (link instanceof UnboundLink))
		{
			cci.startSpare(MapPropertiesManager.getSpareLength());
			cci.physicalLinkImpl(link);
			cci.endSpare(MapPropertiesManager.getSpareLength());
		}
		cci.endSiteNodeImpl((SiteNode )link.getEndNode());
		
		try
		{
			SchemeStorableObjectPool.putStorableObject(cci);
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			cci = null;
		}

		return cci;
	}
	/**
	 * Получить расстояние от начального узла кабеля до заданной точки.
	 * @param cpath кабель
	 * @param pt точка в экранных координатах
	 * @return дистанция топологическая
	 */
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
	 * Возвращяет расстояние от начального узла кабеля до заданной точки, 
	 * пересчитанное на коэффициент топологической привязки.
	 * @param cpath кабель
	 * @param pt точка в экранных координатах
	 * @return дистанция физическая
	 */
	public double getDistanceFromStartLf(CablePath cpath, Point pt)
	{
		double kd = cpath.getKd();
		return getDistanceFromStartLt(cpath, pt) * kd;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLineSize (MapElement link)
	{
		return MapPropertiesManager.getUnboundThickness();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStyle (MapElement link)
	{
		return MapPropertiesManager.getStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	public Stroke getStroke (MapElement link)
	{
		return MapPropertiesManager.getStroke();
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getColor(MapElement link)
	{
		return MapPropertiesManager.getUnboundLinkColor();
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getAlarmedColor(MapElement link)
	{
		return MapPropertiesManager.getAlarmedColor();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAlarmedLineSize (MapElement link)
	{
		return MapPropertiesManager.getAlarmedThickness();
	}
}
