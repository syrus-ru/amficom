/**
 * $Id: MapMarker.java,v 1.9 2004/10/06 09:27:28 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathDecompositor;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.ImageIcon;

/**
 * Название: Маркер связывания оптической дистанции Lo, полученной      * 
 *         экспериментальным путем, со строительной дистанцией Lf,      * 
 *         вводимой при конфигурировании системы, и топологической      * 
 *         дистанцией Lt, полученной в результате расчетов по           * 
 *         координатам и используемо для отображения в окне карты       * 
 *         Связывание дистанций производится по коэффициентам:          * 
 *             Ku = Lo / Lf                                             * 
 *             Kd = Lf / Lt                                             * 
 *         Связывание отображение маркера на карте и на рефлектограмме  * 
 *         строится на основе дистанции Lo, в связи с чем принимаемая   * 
 *         в сообщении из окна рефлектограммы дистанция преобразуется   * 
 *         из Lo в Lf и Lt и наоборот, в отсылаемом сообщении Lt        * 
 *         преобразуется в Lf и Lo. Исключение составляет случай        * 
 *         создания маркера с карты, в этом случае отправляется Lf,     * 
 *         так как топологическая схема не содержит информации о Ku,    * 
 *         и окно рефлектограммы инициализирует маркер такой            * 
 *         информацией, после чего опять используется Lo.               * 
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/10/06 09:27:28 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */

public class MapMarker extends MapNodeElement implements MapElement
{
	public static final String typ = "mapmarker";

	/** Размер пиктограммы маркера */
	public static final Rectangle defaultBounds = new Rectangle(20, 20);

	protected Rectangle bounds = new Rectangle(defaultBounds);

	protected double distance = 0.0;
	protected String symbol_id;
	protected String path_id;
	protected String description;

	protected Point2D.Double bufferAnchor = new Point2D.Double( 0, 0);

	protected MapNodeLinkElement nodeLink;
	protected MapNodeElement startNode;
	protected MapNodeElement endNode;

	protected int nodeLinkIndex = 0;

	protected MapMeasurementPathElement measurementPath;
	protected SchemePath schemePath = null;
	protected TransmissionPath catalogPath = null;
	protected String me_id = "";

	protected PathDecompositor spd = null;

	protected String mapViewId = "";
	
	protected MapView mapView;


	public String[][] getExportColumns()
	{
		return null;
	}

	public void setColumn(String field, String value)
	{
	}

	public MapMarker(
			String id, 
			Map map,
			Rectangle bounds, 
			String imageId,
			double len, 
			MapMeasurementPathElement path)
	{
/*
		this.id = id;
		this.map = map;
		this.bounds = bounds;

		transmissionPath = path;
		if(transmissionPath.PATH_ID != null)
		{
			schemePath = (SchemePath )Pool.get(SchemePath.typ, transmissionPath.PATH_ID);
			if(schemePath != null)
			{
				if(schemePath.path_id != null)
				{
					catalogPath = (TransmissionPath )Pool.get(TransmissionPath.typ, schemePath.path_id);
					if(catalogPath != null)
						me_id = catalogPath.monitored_element_id;
				}
				spd = new SchemePathDecompositor();
				spd.setSchemePath(schemePath);
			}
		}
		setImageId("images/marker.gif");

		Vector nl = transmissionPath.sortNodeLinks();
		Vector n = transmissionPath.sortNodes();

		nodeLink = (MapNodeLinkElement )nl.get(0);
		nodeLinkIndex = 0;
		
		if ( n.indexOf(nodeLink.startNode) < n.indexOf(nodeLink.endNode))
		{
			startNode = nodeLink.startNode;
			endNode = nodeLink.endNode;
		}
		else
		{
			startNode = nodeLink.endNode;
			endNode = nodeLink.startNode;
		}

		anchor = new Point2D.Double(0.0, 0.0);

		moveToFromStartLo(len);
*/
	}

	public Object clone(DataSourceInterface dataSource)
	{
		return null;
	}
/*	
	//Послать сообщения что маркер создан
	public void sendMessage_Marker_Created()
	{
		if(transmissionPath.PATH_ID == null || transmissionPath.PATH_ID.equals(""))
			return;

		if(schemePath != null)
		{
			spd = new SchemePathDecompositor();
			spd.setSchemePath(schemePath);
		}

		MapNavigateEvent mne = new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_CREATED_EVENT,
				getId(),
				getFromStartLengthLf(),
				transmissionPath.PATH_ID,
				me_id);
//		mne.spd = spd;
		getLogicalNetLayer().mapMainFrame.aContext.getDispatcher().notify(mne);
	}

	//Послать сообщения что маркер удален
	public void sendMessage_Marker_Deleted()
	{
		getLogicalNetLayer().mapMainFrame.aContext.getDispatcher().notify( 
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_DELETED_EVENT,
				getId(),
				0.0D,
				transmissionPath.getId(),
				me_id) );
	}

	//Послать сообщения что маркер перемещается
	public void sendMessage_Marker_Moved()
	{
		getLogicalNetLayer().mapMainFrame.aContext.getDispatcher().notify( 
			new MapNavigateEvent(
				this,
				MapNavigateEvent.MAP_MARKER_MOVED_EVENT,
				getId(),
				getFromStartLengthLo(),
				transmissionPath.getId(),
				me_id) );
	}

	//Получить сообщение что маркер выбран
	public void getMessage_Marker_Selected()
	{
		selected = true;
	}

	//Получить сообщение что выбор маркера снят
	public void getMessage_Marker_Deselected()
	{
		selected = false;
	}
*/
	public Rectangle getBounds()
	{
		return bounds;
	}

	public void setBounds(Rectangle rec)
	{
		bounds = rec;
	}

	public Point2D.Double getAnchor()
	{
/*
		Vector nl = transmissionPath.sortNodeLinks();
		if ( anchor != bufferAnchor )
		{
			LogicalNetLayer lnl = getLogicalNetLayer();

			//Рисование о пределение координат маркера происходит путм проецирования координат
			//курсора	на линию на которой маркер находится
			double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
			double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

			double endNodeX = lnl.convertLongLatToScreen(endNode.getAnchor()).x;
			double endNodeY = lnl.convertLongLatToScreen(endNode.getAnchor()).y;

			double nodeLinkLength =  Math.sqrt( 
					(endNodeX - startNodeX) * (endNodeX - startNodeX) +
					(endNodeY - startNodeY) * (endNodeY - startNodeY) );

			double thisX = lnl.convertLongLatToScreen(anchor).x;
			double thisY = lnl.convertLongLatToScreen(anchor).y;

			double lengthFromStartNode = Math.sqrt( 
					(thisX - startNodeX) * (thisX - startNodeX) +
					(thisY - startNodeY) * (thisY - startNodeY) );

			double cos_b =  (endNodeY - startNodeY) / nodeLinkLength;
			double sin_b =  (endNodeX - startNodeX) / nodeLinkLength;

			if ( lengthFromStartNode > nodeLinkLength )
			{
//				int i1 = nl.indexOf(nodeLink);

				if(nodeLinkIndex < nl.size() - 1)
				{
					nodeLinkIndex++;
					MapNodeLinkElement mnle = (MapNodeLinkElement) nl.get(nodeLinkIndex);
					startNode = endNode;
					nodeLink = mnle;
					endNode = getMap().getOtherNodeOfNodeLink(mnle, startNode);
					lengthFromStartNode -= nodeLinkLength;
				}
				lengthFromStartNode = nodeLinkLength;
			}
			else
			if ( lengthFromStartNode < 0 )
			{
//				int i1 = nl.indexOf(nodeLink);

				if(nodeLinkIndex > 0)
				{
					nodeLinkIndex--;
					MapNodeLinkElement mnle = (MapNodeLinkElement) nl.get(nodeLinkIndex);
					endNode = startNode;
					nodeLink = mnle;
					startNode = getMap().getOtherNodeOfNodeLink(mnle, endNode);
					lengthFromStartNode += getNodeLinkScreenLength(mnle);
				}
				lengthFromStartNode = 0;
			}

			anchor = lnl.convertScreenToLongLat(new Point(
				(int)Math.round(startNodeX + sin_b * lengthFromStartNode ),
				(int)Math.round(startNodeY + cos_b * lengthFromStartNode ) ) );
			bufferAnchor = anchor;
		}// if ( anchor != bufferAnchor )
*/
		return anchor;
	}

	public void setAnchor(Point2D.Double aAnchor)
	{
		anchor = aAnchor;
	}

	public boolean isSelected ()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return getMap().getDomainId();
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		return visibleBounds.contains(getAnchor());
	}

	//рисуем маркер
	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		MapCoordinatesConverter converter = getMap().getConverter();

		int width = (int )getBounds().getWidth();
		int height = (int )getBounds().getHeight();
	
		Point p = converter.convertMapToScreen( getAnchor());

		Graphics2D pg = (Graphics2D )g;
		pg.setStroke(MapPropertiesManager.getSelectionStroke());

		pg.drawImage(
				getImage(),
				p.x - width / 2,
				p.y - height / 2,
				null);

		//Если выбрано то рисуем прямоугольник
		if (isSelected())
		{
			pg.setColor(MapPropertiesManager.getSelectionColor());
			pg.drawRect( 
				p.x - width / 2,
				p.y - height / 2,
				width,
				height);
		}
	}

	public void move (double deltaX, double deltaY)
	{
		double x = anchor.getX();
		double y = anchor.getY();
		this.anchor = new Point2D.Double(x + deltaX, y + deltaY);
	}
	
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public MapMarker.MotionDescriptor getMotionDescriptor(Point point)
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		//Рисование о пределение координат маркера происходит путм проецирования координат
		//курсора	на линию на которой маркер находится

		double startNodeX = converter.convertMapToScreen(startNode.getAnchor()).x;
		double startNodeY = converter.convertMapToScreen(startNode.getAnchor()).y;

		double endNodeX = converter.convertMapToScreen(endNode.getAnchor()).x;
		double endNodeY = converter.convertMapToScreen(endNode.getAnchor()).y;

		double nodeLinkLength = Math.sqrt( 
			(endNodeX - startNodeX) * (endNodeX - startNodeX) +
			(endNodeY - startNodeY) * (endNodeY - startNodeY) );

		double thisX = converter.convertMapToScreen(getAnchor()).x;
		double thisY = converter.convertMapToScreen(getAnchor()).y;

		double lengthFromStartNode = Math.sqrt( 
			(thisX - startNodeX) * (thisX - startNodeX) +
			(thisY - startNodeY) * (thisY - startNodeY) );

		double cos_b =  (endNodeY - startNodeY) / nodeLinkLength;

		double sin_b =  (endNodeX - startNodeX) / nodeLinkLength;

		double mousePointX = point.x;
		double mousePointY = point.y;

		double lengthThisToMousePoint = Math.sqrt( 
			(mousePointX - thisX) * (mousePointX - thisX) +
			(mousePointY - thisY) * (mousePointY - thisY) );

		double cos_a = (lengthThisToMousePoint == 0 ) ? 0.0 :
			(	(endNodeX - startNodeX) * (mousePointX - thisX) + 
				(endNodeY - startNodeY)*(mousePointY - thisY) ) /
			( nodeLinkLength * lengthThisToMousePoint );

		lengthFromStartNode = lengthFromStartNode + cos_a * lengthThisToMousePoint;
		
		return new MapMarker.MotionDescriptor(
				cos_b,
				sin_b,
				lengthThisToMousePoint,
				cos_a,
				lengthFromStartNode,
				nodeLinkLength);
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap( Map map)
	{
		this.map = map;
	}

	public int getType()
	{
		return 0;
	}

	public double getNodeLinkScreenLength( MapNodeLinkElement nodeLink)
	{
		MapCoordinatesConverter converter = getMap().getConverter();

		double x1 = converter.convertMapToScreen(nodeLink.getStartNode().getAnchor()).x;
		double y1 = converter.convertMapToScreen(nodeLink.getStartNode().getAnchor()).y;

		double x1_ = converter.convertMapToScreen(nodeLink.getEndNode().getAnchor()).x;
		double y1_ = converter.convertMapToScreen(nodeLink.getEndNode().getAnchor()).y;

		return Math.sqrt( 
				(x1_ - x1) * (x1_ - x1) +
				(y1_ - y1) * (y1_ - y1) );
	}

	public String getTyp()
	{
		return typ;
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapMarkerDisplayModel();
	}
	
	public ObjectResourceModel getModel()
	{
		return null;//new MapMarkerModel(this);
	}

	public double getFromStartLengthLt()
	{
		double path_length = 0;
/*
		Vector nl = transmissionPath.sortNodeLinks();
		Vector pl = transmissionPath.sortPhysicalLinks();
		Vector n = transmissionPath.sortNodes();
		
		MapNodeLinkElement mnle;
		boolean point_reached = false;
		for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();
			if(nodeLink.PhysicalLinkID.equals(mple.getId()))
			{
				Vector nl2 = mple.sortNodeLinks();
				point_reached = true;
				boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
				int size = nl2.size();
				for(int i = 0; i < size; i++)
				{
					if(direct_order)
						mnle = (MapNodeLinkElement )nl2.get(i);
					else
						mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
					if ( mnle == nodeLink)
					{
						if ( n.indexOf(startNode) < n.indexOf(endNode))
							return path_length + getSizeInDoubleLt();
						else
							return path_length + nodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
					}
					else
					{
						path_length += mnle.getSizeInDoubleLt();
					}
				}// for(int i
			}// if(nodeLink.PhysicalLinkID
			else
			{
				path_length += mple.getSizeInDoubleLt();
			}
		}// for(Enumeration plen
*/
		return 0;
	}

	public double getFromStartLengthLf()
	{
/*
		if(schemePath == null)
			return 0.0D;

		Vector nl = transmissionPath.sortNodeLinks();
		Vector pl = transmissionPath.sortPhysicalLinks();
		Vector n = transmissionPath.sortNodes();
		
		double path_length = 0;
		MapNodeElement bufferNode = transmissionPath.startNode;

		boolean point_reached = false;
		MapNodeLinkElement mnle;

		PathElement pes[] = new PathElement[schemePath.links.size()];
		for(int i = 0; i < schemePath.links.size(); i++)
		{
			PathElement pe = (PathElement )schemePath.links.get(i);
			pes[pe.n] = pe;
		}
		Vector pvec = new Vector();
		for(int i = 0; i < pes.length; i++)
		{
			pvec.add(pes[i]);
		}

		Enumeration enum = pvec.elements();
		PathElement pe = null;

		for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

			pe = (PathElement )enum.nextElement();
			bufferNode.countPhysicalLength(schemePath, pe, enum);
			path_length += bufferNode.getPhysicalLength();

			if(bufferNode.equals(mple.startNode))
				bufferNode = mple.endNode;
			else
				bufferNode = mple.startNode;

			if(nodeLink.PhysicalLinkID.equals(mple.getId()))
			{
				Vector nl2 = mple.sortNodeLinks();
				point_reached = true;
				double temp_length = 0.0D; // Count topological length over cable until marker reached
				boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
				int size = nl2.size();
				for(int i = 0; i < size; i++)
				{
					if(direct_order)
						mnle = (MapNodeLinkElement )nl2.get(i);
					else
						mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
					if ( mnle == nodeLink)
					{
						if ( n.indexOf(startNode) < n.indexOf(endNode))
							temp_length += getSizeInDoubleLt();
						else
							temp_length += nodeLink.getSizeInDoubleLt() - getSizeInDoubleLt();
						return path_length + mple.getKd() * temp_length;// Convert to physical length
					}
					else
					{
						temp_length += mnle.getSizeInDoubleLt();
					}
				}// for(int i
			}// if(nodeLink.PhysicalLinkID
			else
			{
				path_length += mple.getSizeInDoubleLf();
			}
		}// for(Enumeration plen
*/
		return 0.0D;
	}
/*
	public double getFromEndLengthLt()
	{
		double length = transmissionPath.getSizeInDoubleLt();
		return length - getFromStartLengthLt();
	}	

	public double getFromEndLengthLf()
	{
		double length = transmissionPath.getSizeInDoubleLf();
		return length - getFromStartLengthLf();
	}
*/
	public double getSizeInDoubleLt()
	{

		Point2D.Double from = startNode.getAnchor();
		Point2D.Double to = getAnchor();

		MapCoordinatesConverter converter = getMap().getConverter();
		return converter.distance(from, to);
	}

	public double getSizeInDoubleLf()
	{
		double Kd = 1.0;//getMap().getPhysicalLink(nodeLink.getPhysicalLinkId()).getKd();
		return getSizeInDoubleLt() * Kd;
	}

	//Передвинуть в точку на заданной расстоянии от нсчала
	public void moveToFromStartLt(double distance)
	{
/*
		LogicalNetLayer lnl = getLogicalNetLayer();
		if ( lnl.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
		{
			double pathl = transmissionPath.getSizeInDoubleLt();
			if ( distance > pathl)
				distance = pathl;

			Vector nl = transmissionPath.sortNodeLinks();
			Vector pl = transmissionPath.sortPhysicalLinks();
			Vector n = transmissionPath.sortNodes();
		
			double path_length = 0;

			boolean point_reached = false;
			MapNodeLinkElement mnle;
			
			for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
			{
				MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

				if ( path_length + mple.getSizeInDoubleLt() > distance)
				{
					Vector nl2 = mple.sortNodeLinks();
					point_reached = true;
					boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nodeLink));
					int size = nl2.size();
					for(int i = 0; i < size; i++)
					{
						if(direct_order)
							mnle = (MapNodeLinkElement )nl2.get(i);
						else
							mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
						if ( path_length + mnle.getSizeInDoubleLt() > distance)
						{
							nodeLink = mnle;
							nodeLinkIndex = nl.indexOf(mnle);
							if ( n.indexOf(mnle.startNode) < n.indexOf(mnle.endNode))
							{
								startNode = mnle.startNode;
								endNode = mnle.endNode;
							}
							else
							{
								startNode = mnle.endNode;
								endNode = mnle.startNode;
							}

							double nl_distance = distance - path_length;

							adjustPosition(nl_distance, false);
							return;
						}// if ( ... > distance
						else
						{
							path_length += mnle.getSizeInDoubleLt();
						}
					}// for(int i
				}// if ( ... > distance
				else
				{
					path_length += mple.getSizeInDoubleLt();
				}
			}// for(Enumeration plen
		}// if ( lnl.mapMainFrame
*/
	}
/*
	public double getFromStartLengthLo()
	{
		if(spd == null)
			return getFromStartLengthLf();
		else
			return spd.getOpticalDistance(getFromStartLengthLf());
	}

	public void moveToFromStartLo(double dist)
	{
		if(spd == null)
			moveToFromStartLf(dist);
		else
			moveToFromStartLf(spd.getPhysicalDistance(dist));
	}
*/
	//Передвинуть в точку на заданном расстоянии от начала (физ)
	public void moveToFromStartLf(double dist)
	{
/*
		double distance = 0.0D;
		distance = dist;

		if ( transmissionPath.getLogicalNetLayer().mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
		{
			double pathl = transmissionPath.getSizeInDoubleLf();
			if ( distance > pathl)
				distance = pathl;

			Vector pl = transmissionPath.sortPhysicalLinks();
		
			double path_length = 0;
			MapNodeElement bufferNode = transmissionPath.startNode;

			boolean point_reached = false;
			MapNodeLinkElement mnle;

			PathElement pes[] = new PathElement[schemePath.links.size()];
			for(int i = 0; i < schemePath.links.size(); i++)
			{
				PathElement pe = (PathElement )schemePath.links.get(i);
				pes[pe.n] = pe;
			}
			Vector pvec = new Vector();
			for(int i = 0; i < pes.length; i++)
			{
				pvec.add(pes[i]);
			}

			Enumeration enum = pvec.elements();
			PathElement pe = null;

			for(Enumeration plen = pl.elements(); plen.hasMoreElements() && !point_reached;)
			{
				MapPhysicalLinkElement mple = (MapPhysicalLinkElement )plen.nextElement();

				pe = (PathElement )enum.nextElement();
				bufferNode.countPhysicalLength(schemePath, pe, enum);
				double nd = bufferNode.getPhysicalLength();
				if(path_length + nd > distance)
				{
					setRelativeToLink(0, mple);
					return;
				}
				else
				{
					path_length += nd;
				}

				if(bufferNode.equals(mple.startNode))
					bufferNode = mple.endNode;
				else
					bufferNode = mple.startNode;

				if ( path_length + mple.getSizeInDoubleLf() > distance)
				{
					setRelativeToLink(distance - path_length, mple);
					return;
				}
				else
				{
					path_length += mple.getSizeInDoubleLf();
				}
			}// for(Enumeration plen
		}// if ( isEnabled("mapActionMarkerMove")
*/
	}

	public void setRelativeToLink(double pl_distance, MapPhysicalLinkElement mple)
	{
/*
		MapNodeLinkElement mnle;
	
		Vector nl = transmissionPath.sortNodeLinks();
		Vector n = transmissionPath.sortNodes();
		Vector nl2 = mple.sortNodeLinks();
		boolean point_reached = true;
		double temp_length = 0.0D; // Count topological length over cable until marker reached
		boolean direct_order = (nl.indexOf(nl2.get(0)) <= nl.indexOf(nl2.get(nl2.size() - 1)));
		int size = nl2.size();
		for(int i = 0; i < size; i++)
		{
			if(direct_order)
				mnle = (MapNodeLinkElement )nl2.get(i);
			else
				mnle = (MapNodeLinkElement )nl2.get(size - i - 1);
							
			if ( temp_length + mnle.getSizeInDoubleLf() > pl_distance)
			{
				nodeLink = mnle;
				nodeLinkIndex = nl.indexOf(mnle);
				if ( n.indexOf(mnle.startNode) < n.indexOf(mnle.endNode))
				{
					startNode = mnle.startNode;
					endNode = mnle.endNode;
				}
				else
				{
					startNode = mnle.endNode;
					endNode = mnle.startNode;
				}

				double nl_distance = pl_distance - temp_length;

				adjustPosition(nl_distance, true);
				return;
			}// if ( ... > pl_distance
			else
			{
				temp_length += mnle.getSizeInDoubleLf();
			}
		}// for(int i
*/
	}
/*
	public void adjustPosition(double nl_distance, boolean is_lf)
	{
		MapCoordinatesConverter converter = getMap().getConverter();
	
		double startNodeX = converter.convertMapToScreen(startNode.getAnchor()).x;
		double startNodeY = converter.convertMapToScreen(startNode.getAnchor()).y;

		double endNodeX = converter.convertMapToScreen(endNode.getAnchor()).x;
		double endNodeY = converter.convertMapToScreen(endNode.getAnchor()).y;

		double nodeLinkLength =  Math.sqrt( 
				(endNodeX - startNodeX) * (endNodeX - startNodeX) +
				(endNodeY - startNodeY) * (endNodeY - startNodeY) );

		double thisX = converter.convertMapToScreen(anchor).x;
		double thisY = converter.convertMapToScreen(anchor).y;

		double lengthFromStartNode = Math.sqrt( 
			(thisX - startNodeX) * (thisX - startNodeX) +
			(thisY - startNodeY) * (thisY - startNodeY) );

		double cos_b = (endNodeY - startNodeY) / nodeLinkLength;

		double sin_b = (endNodeX - startNodeX) / nodeLinkLength;

		double nl_fromstart = (is_lf) ? nodeLink.getSizeInDoubleLf() : nodeLink.getSizeInDoubleLt();

		setAnchor(converter.convertScreenToMap(new Point(
			(int )Math.round(startNodeX + sin_b * ( nl_distance / nl_fromstart * nodeLinkLength )),
			(int )Math.round(startNodeY + cos_b * ( nl_distance / nl_fromstart * nodeLinkLength )) ) ) );
	}
*/
	//Проверка того что объект можно перемещать
	public boolean isMovable()
	{
/*
		if ( transmissionPath.getLogicalNetLayer().mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
		{
			return true;
		}
*/
		return false;
	}

	public String getToolTipText()
	{
		String s1 = getName() + " (" + LangModelMap.getString("Path_lowercase") + " " + measurementPath.getName() + ")";

		return s1;
	}

	class MotionDescriptor
	{
		double cos_b;
		double sin_b;
		double lengthThisToMousePoint;
		double cos_a;
		double lengthFromStartNode;
		double nodeLinkLength;

		public MotionDescriptor(double cb, double sb, double lt, double ca, double lf, double nl)
		{
			cos_b = cb;
			sin_b = sb;
			lengthThisToMousePoint = lt;
			cos_a = ca;
			lengthFromStartNode = lf;
			nodeLinkLength = nl;
		}
	}

	public MapElementState getState()
	{
		return null;
	}

	public void revert(MapElementState state)
	{
	}

	public boolean isRemoved()
	{
		return false;
	}
	
	public void setRemoved(boolean removed)
	{
	}


	public void setMeasurementPath(MapMeasurementPathElement measurementPath)
	{
		this.measurementPath = measurementPath;
	}


	public MapMeasurementPathElement getMeasurementPath()
	{
		return measurementPath;
	}

}
