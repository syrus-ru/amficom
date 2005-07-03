/**
 * $Id: CableController.java,v 1.23 2005/06/23 08:26:04 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
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

/**
 * ���������� ������.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.23 $, $Date: 2005/06/23 08:26:04 $
 * @module mapviewclient_v1
 */
public final class CableController extends AbstractLinkController {

	/**
	 * Private constructor.
	 */
	private CableController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new CableController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelectionVisible(MapElement me) {
		if(!(me instanceof CablePath))
			return false;

		CablePath cpath = (CablePath )me;

		boolean isv = cpath.isSelected();
		if(!isv) {
			for(Iterator it = this.logicalNetLayer.getMapView().getMeasurementPaths(cpath).iterator(); it.hasNext();) {
				MeasurementPath mp = (MeasurementPath )it.next();
				MeasurementPathController mpc = (MeasurementPathController )this.logicalNetLayer.getMapViewController().getController(mp);
				if(mpc.isSelectionVisible(mp)) {
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
	public boolean isElementVisible(
			MapElement me,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(me instanceof CablePath))
			return false;

		CablePath cpath = (CablePath )me;

		boolean vis = false;
		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			PhysicalLinkController plc = (PhysicalLinkController )this.logicalNetLayer.getMapViewController().getController(link);
			if(plc.isElementVisible(link, visibleBounds)) {
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement me) {
		if(!(me instanceof CablePath))
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
	public void paint(
			MapElement me,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(me instanceof CablePath))
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
	public void paint(
			CablePath cpath,
			Graphics g,
			Rectangle2D.Double visibleBounds,
			Stroke stroke,
			Color color,
			boolean selectionVisible)
			throws MapConnectionException, MapDataException {
		if(!isElementVisible(cpath, visibleBounds))
			return;

		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink)it.next();
			PhysicalLinkController plc = (PhysicalLinkController)this.logicalNetLayer.getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * {@inheritDoc}
	 * <br>����� ��������� �� ������, ���� ��� ��������� �� ����� �����,
	 * ������� ������ � ������.
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		if(!(me instanceof CablePath))
			return false;

		CablePath cpath = (CablePath )me;

		for(Iterator it = cpath.getLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink)it.next();
			PhysicalLinkController plc = (PhysicalLinkController)this.logicalNetLayer.getMapViewController().getController(link);
			if(plc.isMouseOnElement(link, currentMousePoint))
				return true;
		}
		return false;
	}

	/**
	 * ������� ����� ������ �������� � �����.
	 * @param cablePath ��������� ������
	 * @param link �����
	 * @param creatorId ������� ������������
	 * @return ������ ��������, ��� <code>null</code> ��� ������������� ������
	 */
	public static CableChannelingItem generateCCI(
			CablePath cablePath,
			PhysicalLink link,
			Identifier creatorId)
	{
		CableChannelingItem cci = null;
		try {
			SiteNode startNode = (SiteNode )link.getStartNode();
			SiteNode endNode = (SiteNode )link.getEndNode();
			double startSpare = 0.0D;
			double endSpare = 0.0D;
			SchemeCableLink schemeCableLink = cablePath.getSchemeCableLink();
			if(!(link instanceof UnboundLink)) {
				startSpare = MapPropertiesManager.getSpareLength();
				endSpare = MapPropertiesManager.getSpareLength();

				cci = CableChannelingItem.createInstance(
						creatorId, 
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
						startNode,
						endNode,
						schemeCableLink);
			}
		
			StorableObjectPool.putStorableObject(cci);
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
			cci = null;
		} catch(CreateObjectException e) {
			e.printStackTrace();
			cci = null;
		}

		return cci;
	}
	/**
	 * �������� ���������� �� ���������� ���� ������ �� �������� �����.
	 * 
	 * @param cpath ������
	 * @param pt ����� � �������� �����������
	 * @return ��������� ��������������
	 */
	public double getDistanceFromStartLt(CablePath cpath, Point pt)
			throws MapConnectionException, MapDataException {
		double distance = 0.0;

		AbstractNode node = cpath.getStartNode();
		cpath.sortNodeLinks();
		for(Iterator it = cpath.getSortedNodeLinks().iterator(); it.hasNext();) {
			NodeLink mnle = (NodeLink)it.next();
			NodeLinkController nlc = (NodeLinkController)this.logicalNetLayer.getMapViewController().getController(mnle);
			if(nlc.isMouseOnElement(mnle, pt)) {
				DoublePoint dpoint = this.logicalNetLayer.getConverter().convertScreenToMap(pt);
				distance += this.logicalNetLayer.getConverter().distance(dpoint, node.getLocation());
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
			throws MapConnectionException, MapDataException {
		double kd = cpath.getKd();
		return getDistanceFromStartLt(cpath, pt) * kd;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLineSize(MapElement link) {
		return MapPropertiesManager.getUnboundThickness();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStyle(MapElement link) {
		return MapPropertiesManager.getStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	public Stroke getStroke(MapElement link) {
		return MapPropertiesManager.getStroke();
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getColor(MapElement link) {
		return MapPropertiesManager.getUnboundLinkColor();
	}

	/**
	 * {@inheritDoc}
	 */
	public Color getAlarmedColor(MapElement link) {
		return MapPropertiesManager.getAlarmedColor();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAlarmedLineSize(MapElement link) {
		return MapPropertiesManager.getAlarmedThickness();
	}
}
