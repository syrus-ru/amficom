/**
 * $Id: MeasurementPathController.java,v 1.2 2005/01/30 15:38:18 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;

import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElement;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;
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
 * @version $Revision: 1.2 $, $Date: 2005/01/30 15:38:18 $
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
		if(! (me instanceof MeasurementPath))
			return false;

		MeasurementPath mpath = (MeasurementPath)me;

		return mpath.isSelected();
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof MeasurementPath))
			return false;

		MeasurementPath mpath = (MeasurementPath)me;

		boolean vis = false;
		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(cpath);
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
		if(! (me instanceof MeasurementPath))
			return null;

		MeasurementPath mpath = (MeasurementPath)me;
		
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
		if(! (me instanceof MeasurementPath))
			return;

		MeasurementPath mpath = (MeasurementPath)me;
		
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

	public void paint(MeasurementPath mpath, Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
	{
		if(!isElementVisible(mpath, visibleBounds))
			return;

		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(cpath);
			cc.paint(cpath, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof MeasurementPath))
			return false;

		MeasurementPath mpath = (MeasurementPath)me;

		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(cpath);
			if(cc.isMouseOnElement(cpath, currentMousePoint))
				return true;
		}
		return false;
	}

	public MapElement getMapElement(MeasurementPath path, PathElement pe)
	{
		MapElement me = null;
		switch(pe.type().value())
		{
			case Type._SCHEME_ELEMENT:
				SchemeElement se = (SchemeElement )pe.abstractSchemeElement();
				SiteNode site = getLogicalNetLayer().getMapViewController().findElement(se);
				if(site != null)
				{
					me = site;
				}
				break;
			case Type._SCHEME_LINK:
				SchemeLink link = (SchemeLink )pe.abstractSchemeElement();
				SchemeElement sse = SchemeUtils.getSchemeElementByDevice(path.getSchemePath().scheme(), link.sourceSchemePort().schemeDevice());
				SchemeElement ese = SchemeUtils.getSchemeElementByDevice(path.getSchemePath().scheme(), link.targetSchemePort().schemeDevice());
				SiteNode ssite = getLogicalNetLayer().getMapViewController().findElement(sse);
				SiteNode esite = getLogicalNetLayer().getMapViewController().findElement(ese);
				if(ssite != null && ssite.equals(esite))
				{
					me = ssite;
				}
				break;
			case Type._SCHEME_CABLE_LINK:
				SchemeCableLink clink = (SchemeCableLink )pe.abstractSchemeElement();
				CablePath cp = getLogicalNetLayer().getMapViewController().findCablePath(clink);
				if(cp != null)
				{
					me = cp;
				}
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return me;
	}

}
