/**
 * $Id: MeasurementPathController.java,v 1.19 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ??????? ?????????????????? ???????????????????
 *         ???????????????? ???????? ?????????? ???????????
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
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;

/**
 * ?????????? ??????????????? ????.
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public final class MeasurementPathController extends AbstractLinkController {

	/**
	 * Instance.
	 */
	private static MeasurementPathController instance = null;

	/**
	 * Private constructor.
	 */
	private MeasurementPathController() {
		// empty
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapElementController getInstance() {
		if(instance == null)
			instance = new MeasurementPathController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelectionVisible(MapElement mapElement) {
		if(!(mapElement instanceof MeasurementPath))
			return false;

		MeasurementPath mpath = (MeasurementPath )mapElement;

		return mpath.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(
			MapElement mapElement,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof MeasurementPath))
			return false;

		MeasurementPath mpath = (MeasurementPath )mapElement;

		boolean vis = false;
		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();) {
			CablePath cpath = (CablePath )it.next();
			CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(cpath);
			if(cc.isElementVisible(cpath, visibleBounds)) {
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement) {
		if(!(mapElement instanceof MeasurementPath))
			return null;

		MeasurementPath mpath = (MeasurementPath )mapElement;

		String s1 = mpath.getName();
		String s2 = "";
		String s3 = "";
		try {
			AbstractNode smne = mpath.getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ MapViewController.getMapElementReadableType(smne)
				+ "]";
			AbstractNode emne = mpath.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ MapViewController.getMapElementReadableType(emne)
				+ "]";
		}
		catch(Exception e) {
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
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof MeasurementPath))
			return;

		MeasurementPath mpath = (MeasurementPath)mapElement;
		
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

	/**
	 * ?????????? ???? ? ???????? ?????? ? ??????.
	 * @param mpath ????
	 * @param g ??????????? ????????
	 * @param visibleBounds ??????? ???????
	 * @param stroke ????? ?????
	 * @param color ???? ?????
	 * @param selectionVisible ???????? ????? ?????????
	 */
	public void paint(
			MeasurementPath mpath,
			Graphics g,
			Rectangle2D.Double visibleBounds,
			Stroke stroke,
			Color color,
			boolean selectionVisible)
			throws MapConnectionException, MapDataException {
		if(!isElementVisible(mpath, visibleBounds))
			return;

		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();) {
			CablePath cpath = (CablePath)it.next();
			CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(cpath);
			cc.paint(cpath, g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * {@inheritDoc}
	 * <br>????? ????????? ?? ????, ???? ??? ????????? ?? ????? ??????,
	 * ???????? ?????? ? ????.
	 */
	public boolean isMouseOnElement(
			MapElement mapElement,
			Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof MeasurementPath))
			return false;

		MeasurementPath mpath = (MeasurementPath )mapElement;

		for(Iterator it = mpath.getSortedCablePaths().iterator(); it.hasNext();) {
			CablePath cpath = (CablePath)it.next();
			CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(cpath);
			if(cc.isMouseOnElement(cpath, currentMousePoint))
				return true;
		}
		return false;
	}

	/**
	 * ???????? ?????????????? ???????, ??????????????? ??????? ???????? ????.
	 * @param path ????
	 * @param pe ??????? ????
	 * @return ??????? ?????
	 */
	public MapElement getMapElement(MeasurementPath path, PathElement pe) {
		MapElement me = null;
		MapView mapView = getLogicalNetLayer().getMapView();
		switch(pe.getKind().value()) {
			case Kind._SCHEME_ELEMENT:
				SchemeElement se = (SchemeElement )pe.getAbstractSchemeElement();
				SiteNode site = mapView.findElement(se);
				if(site != null) {
					me = site;
				}
				break;
			case Kind._SCHEME_LINK:
				SchemeLink link = (SchemeLink )pe.getAbstractSchemeElement();
				SchemeElement sse = SchemeUtils.getSchemeElementByDevice(path.getSchemePath().getScheme(), link.getSourceSchemePort().getParentSchemeDevice());
				SchemeElement ese = SchemeUtils.getSchemeElementByDevice(path.getSchemePath().getScheme(), link.getTargetSchemePort().getParentSchemeDevice());
				SiteNode ssite = mapView.findElement(sse);
				SiteNode esite = mapView.findElement(ese);
				if(ssite != null && ssite.equals(esite)) {
					me = ssite;
				}
				break;
			case Kind._SCHEME_CABLE_LINK:
				SchemeCableLink clink = (SchemeCableLink )pe.getAbstractSchemeElement();
				CablePath cp = mapView.findCablePath(clink);
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

	/**
	 * ???????? ????????????? ???????????? ???????, ???????? ?????????????
	 * ????????????? ????.
	 * @param path ????
	 * @return ????????????? ??? <code>null</code>, ???? ??????????? ?????? 
	 * ?? ??????
	 */
	public Identifier getMonitoredElementId(MeasurementPath path) {
		Identifier meid = null;
		MonitoredElement me = getMonitoredElement(path);
		if(me != null)
			meid = me.getId();
		return meid;
	}

	/**
	 * ???????? ??????????? ??????, ???????? ?????????????
	 * ????????????? ????.
	 * @param path ????
	 * @return ??????????? ?????? ??? <code>null</code>, ???? ??????????? ?????? 
	 * ?? ??????
	 */
	public MonitoredElement getMonitoredElement(MeasurementPath path) {
		MonitoredElement me = null;
		try {
			TransmissionPath tp = path.getSchemePath().getTransmissionPath();

			me = (MonitoredElement )
				StorableObjectPool.getStorableObject(
						(Identifier )(tp.getMonitoredElementIds().iterator().next()), 
						true);
		} catch(CommunicationException e) {
			e.printStackTrace();
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return me;
	}

}
