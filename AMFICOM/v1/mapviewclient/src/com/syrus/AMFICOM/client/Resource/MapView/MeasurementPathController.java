/**
 * $Id: MeasurementPathController.java,v 1.4 2004/12/23 16:58:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.AbstractLinkController;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementController;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/23 16:58:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MeasurementPathController extends AbstractLinkController
{
	private static MeasurementPathController instance = null;
	
	private MeasurementPathController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new MeasurementPathController();
		return instance;
	}

	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof MapMeasurementPathElement))
			return false;

		MapMeasurementPathElement mpath = (MapMeasurementPathElement )me;

		return mpath.isSelected();
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof MapMeasurementPathElement))
			return false;

		MapMeasurementPathElement mpath = (MapMeasurementPathElement )me;

		boolean vis = false;
		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			CableController cc = (CableController )getLogicalNetLayer().getMapViewController().getController(cpath);
			if(cc.isElementVisible(cpath, visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof MapMeasurementPathElement))
			return null;

		MapMeasurementPathElement mpath = (MapMeasurementPathElement )me;
		
		String s1 = mpath.getName();
		String s2 = "";
		String s3 = "";
		try
		{
			AbstractNode smne = mpath.getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ LangModel.getString("node" + smne.getClass().getName()) 
				+ "]";
			AbstractNode emne = mpath.getEndNode();
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
		if(! (me instanceof MapMeasurementPathElement))
			return;

		MapMeasurementPathElement mpath = (MapMeasurementPathElement )me;
		
		if(!isElementVisible(mpath, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(mpath);
		Stroke str = new BasicStroke(
				getLineSize(mpath), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = getColor(mpath);

		paint(mpath, g, visibleBounds, str, color, isSelectionVisible(mpath));
	}

	public void paint(MapMeasurementPathElement mpath, Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
	{
		if(!isElementVisible(mpath, visibleBounds))
			return;

		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			CableController cc = (CableController )getLogicalNetLayer().getMapViewController().getController(cpath);
			cc.paint(cpath, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof MapMeasurementPathElement))
			return false;

		MapMeasurementPathElement mpath = (MapMeasurementPathElement )me;

		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			CableController cc = (CableController )getLogicalNetLayer().getMapViewController().getController(cpath);
			if(cc.isMouseOnElement(cpath, currentMousePoint))
				return true;
		}
		return false;
	}

}
