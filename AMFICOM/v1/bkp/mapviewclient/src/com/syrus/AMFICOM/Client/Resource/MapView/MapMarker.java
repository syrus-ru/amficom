/**
 * $Id: MapMarker.java,v 1.20 2004/12/08 16:20:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathDecompositor;
import com.syrus.AMFICOM.general.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

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
 * @version $Revision: 1.20 $, $Date: 2004/12/08 16:20:22 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */

public class MapMarker extends MapNodeElement
{
	private static final long serialVersionUID = 02L;
	
	/**
	 * @deprecated
	 */
	public static final String typ = "mapmarker";

	/**
	 * @deprecated
	 */
	public static final String IMAGE_NAME = "marker";

	protected Identifier meId;
	protected double distance = 0.0;

	protected MapView mapView;
	

	protected MapMeasurementPathElement measurementPath;

	protected PathDecompositor spd = null;

	protected Object descriptor;

	protected MapCablePathElement cpath;
	protected MapNodeLinkElement nodeLink;
	protected MapNodeElement startNode;
	protected MapNodeElement endNode;

	/**
	 * Создание маркера пользователем на карте
	 * 
	 * @param id
	 * @param mapView
	 * @param startNode
	 * @param mnle
	 * @param topologicalDistance
	 * @param path
	 */
	public MapMarker(
			String id, 
			MapView mapView,
			MapNodeElement startNode,
			MapNodeElement endNode,
			MapNodeLinkElement mnle,
			MapMeasurementPathElement path,
			DoublePoint dpoint)
	{
		this(id, mapView, 0.0, path, path.getMonitoredElementId());
		
		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeLink = mnle;
		setLocation(dpoint);
	}

	/**
	 * Создание маркера на основе полученного сообщения с указанием оптической 
	 * дистанции
	 * 
	 * @param id
	 * @param mapView
	 * @param opticalDistance
	 * @param path
	 * @param meId
	 */
	public MapMarker(
			String id, 
			MapView mapView,
			double opticalDistance, 
			MapMeasurementPathElement path,
			Identifier meId)
	{
		this.setId(id);
		this.setName(id);
		this.mapView = mapView;
		this.meId = meId;
		if(mapView != null)
		{
			this.map = mapView.getMap();
			super.setMap(map);
		}
		this.setImageId(IMAGE_NAME);

		this.measurementPath = path;
		this.startNode = measurementPath.getStartNode();
		
		attributes = new HashMap();

		spd = new PathDecompositor(measurementPath.getSchemePath());

		location = new DoublePoint(0.0, 0.0);

//		moveToFromStartLo(opticalDistance);
	}

	/**
	 * @deprecated
	 */
	public String getTyp()
	{
		return typ;
	}

	public void setLocation(DoublePoint aLocation)
	{
		super.setLocation(aLocation);
//		distance = this.getFromStartLengthLo();
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
//	public Point2D.Double getAnchor()
//	{
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
//		return anchor;
//	}
/*
	public MotionDescriptor getMotionDescriptor(Point point)
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

		double sinB =  (endNodeY - startNodeY) / nodeLinkLength;

		double cosB =  (endNodeX - startNodeX) / nodeLinkLength;

		double mousePointX = point.x;
		double mousePointY = point.y;

		double lengthThisToMousePoint = Math.sqrt( 
			(mousePointX - thisX) * (mousePointX - thisX) +
			(mousePointY - thisY) * (mousePointY - thisY) );

		double cosA = (lengthThisToMousePoint == 0 ) ? 0.0 :
			(	(endNodeX - startNodeX) * (mousePointX - thisX) + 
				(endNodeY - startNodeY) * (mousePointY - thisY) ) /
			( nodeLinkLength * lengthThisToMousePoint );

		lengthFromStartNode = lengthFromStartNode + cosA * lengthThisToMousePoint;
		
		return new MotionDescriptor(
			converter.convertMapToScreen(startNode.getAnchor()),
			converter.convertMapToScreen(endNode.getAnchor()),
			converter.convertMapToScreen(getAnchor()),
			point);
	}
*/

	public double getOpticalDistanceFromStart()
	{
		return 0.0;
	}

	public MapNodeLinkElement previousNodeLink()
	{
		MapNodeLinkElement nl;
		int index = measurementPath.getSortedNodeLinks().indexOf(nodeLink);
		if(index == 0)
			nl = null;
		else
			nl = (MapNodeLinkElement )(measurementPath.getSortedNodeLinks().get(index - 1));
		return nl;
	}

	public MapNodeLinkElement nextNodeLink()
	{
		MapNodeLinkElement nl;
		int index = measurementPath.getSortedNodeLinks().indexOf(nodeLink);
		if(index == measurementPath.getSortedNodeLinks().size() - 1)
			nl = null;
		else
			nl = (MapNodeLinkElement )(measurementPath.getSortedNodeLinks().get(index + 1));
		return nl;
	}

	public MapSiteNodeElement getLeft()
	{
		List nodes = cpath.getSortedNodes();
		MapNodeElement node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(startNode)); lit.hasPrevious();)
		{
			node = (MapNodeElement )lit.previous();
			if(node instanceof MapSiteNodeElement)
				break;
			node = null;
		}
		return (MapSiteNodeElement )node;
	}

	public MapSiteNodeElement getRight()
	{
		List nodes = cpath.getSortedNodes();
		MapNodeElement node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(endNode) - 1); lit.hasNext();)
		{
			node = (MapNodeElement )lit.next();
			if(node instanceof MapSiteNodeElement)
				break;
			node = null;
		}
		return (MapSiteNodeElement )node;
	}


	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	public void setMeasurementPath(MapMeasurementPathElement measurementPath)
	{
		this.measurementPath = measurementPath;
	}


	public MapMeasurementPathElement getMeasurementPath()
	{
		return measurementPath;
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	public void setDescriptor(Object descriptor)
	{
		this.descriptor = descriptor;
	}


	public Object getDescriptor()
	{
		return descriptor;
	}

	public PathDecompositor getPathDecompositor()
	{
		return spd;
	}
	
	public void setPathDecompositor(PathDecompositor spd)
	{
		this.spd = spd;
	}

	public void setNodeLink(MapNodeLinkElement nodeLink)
	{
		this.nodeLink = nodeLink;
	}


	public MapNodeLinkElement getNodeLink()
	{
		return nodeLink;
	}


	public void setStartNode(MapNodeElement startNode)
	{
		this.startNode = startNode;
	}


	public MapNodeElement getStartNode()
	{
		return startNode;
	}


	public void setEndNode(MapNodeElement endNode)
	{
		this.endNode = endNode;
	}


	public MapNodeElement getEndNode()
	{
		return endNode;
	}


	public MapCablePathElement getCablePath()
	{
		return cpath;
	}


	public void setMeId(Identifier meId)
	{
		this.meId = meId;
	}


	public Identifier getMeId()
	{
		return meId;
	}

}
