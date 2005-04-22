/**
 * $Id: CableController.java,v 1.15 2005/04/22 15:10:07 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;

/**
 * ���������� ������.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/04/22 15:10:07 $
 * @module mapviewclient_v1
 */
public final class CableController extends AbstractLinkController
		implements VisualManager
{
	/**
	 * Instance.
	 */
	private static CableController instance = null;

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane";

	/**
	 * Private constructor.
	 */
	private CableController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new CableController();
		return instance;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	public ObjectResourceController getController() {
//		return CablePathWrapper.getInstance();
		return null;
	}
	public StorableObjectEditor getGeneralPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * �������� ��� ������ ������, ����������� �������� ���������� ����.
	 * @return ��� ������
	 */
	public static String getPropertyPaneClassName()
	{
		return CableController.PROPERTY_PANE_CLASS_NAME;
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
			for(Iterator it = getLogicalNetLayer().getMapView().getMeasurementPaths(cpath).iterator(); it.hasNext();)
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
		throws MapConnectionException, MapDataException
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
				+ MapViewController.getMapElementReadableType(smne)
				+ "]";
			AbstractNode emne = cpath.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ MapViewController.getMapElementReadableType(emne)
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
		throws MapConnectionException, MapDataException
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
	 * ���������� ��� �����, �� ������� ������� ������, � �������� ������.
	 * � ������ �����
	 * @param cpath ������
	 * @param g ����������� ��������
	 * @param visibleBounds ������� �������
	 * @param stroke ����� �����
	 * @param color ���� �����
	 * @param selectionVisible �������� ����� ���������
	 */
	public void paint(CablePath cpath, Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
		throws MapConnectionException, MapDataException
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
	 * <br>����� ��������� �� ������, ���� ��� ��������� �� ����� �����,
	 * ������� ������ � ������.
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
		throws MapConnectionException, MapDataException
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
	 * ������� ����� ������ �������� � �����.
	 * @param cablePath TODO
	 * @param link �����
	 * @param creatorId TODO
	 * @return ������ ��������, ��� <code>null</code> ��� ������������� ������
	 */
	public static CableChannelingItem generateCCI(CablePath cablePath, PhysicalLink link, Identifier creatorId)//, Identifier creatorId)
	{
		CableChannelingItem cci = null;
		try
		{
			SiteNode startNode = (SiteNode )link.getStartNode();
			SiteNode endNode = (SiteNode )link.getEndNode();
			double startSpare = 0.0D;
			double endSpare = 0.0D;
			SchemeCableLink schemeCableLink = cablePath.getSchemeCableLink();
			if(! (link instanceof UnboundLink))
			{
				startSpare = MapPropertiesManager.getSpareLength();
				endSpare = MapPropertiesManager.getSpareLength();

				cci = CableChannelingItem.createInstance(
						creatorId, 
						"",//default
						"",//default
						startSpare,
						endSpare,
						0,//default
						0,//default
						0,//default
						link,
						startNode,
						endNode,
						schemeCableLink);
			}
			else {
				cci = CableChannelingItem.createInstance(
						creatorId, 
						"",//default 
						startNode,
						endNode,
						schemeCableLink);
			}
		
			SchemeStorableObjectPool.putStorableObject(cci);
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			cci = null;
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
			cci = null;
		}
		

		return cci;
	}
	/**
	 * �������� ���������� �� ���������� ���� ������ �� �������� �����.
	 * @param cpath ������
	 * @param pt ����� � �������� �����������
	 * @return ��������� ��������������
	 */
	public double getDistanceFromStartLt(CablePath cpath, Point pt)
		throws MapConnectionException, MapDataException
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

			distance += mnle.getLengthLt();

			if(mnle.getStartNode().equals(node))
				node = mnle.getEndNode();
			else
				node = mnle.getStartNode();
		}
		return distance;
	}

	/**
	 * ���������� ���������� �� ���������� ���� ������ �� �������� �����, 
	 * ������������� �� ����������� �������������� ��������.
	 * @param cpath ������
	 * @param pt ����� � �������� �����������
	 * @return ��������� ����������
	 */
	public double getDistanceFromStartLf(CablePath cpath, Point pt)
		throws MapConnectionException, MapDataException
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
