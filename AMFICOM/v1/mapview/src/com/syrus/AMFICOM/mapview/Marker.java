/**
 * $Id: Marker.java,v 1.1 2005/02/01 09:28:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.PathDecompositor;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.CablePath;

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
 * @version $Revision: 1.1 $, $Date: 2005/02/01 09:28:17 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */

public class Marker extends AbstractNode
{
	private static final long serialVersionUID = 02L;
	
	protected Identifier meId;

	protected double distance = 0.0;

	protected Object descriptor;

	protected MapView mapView;
	protected MeasurementPath measurementPath;
	protected PathDecompositor spd = null;
	protected CablePath cpath;
	protected NodeLink nodeLink;
	protected AbstractNode startNode;
	protected AbstractNode endNode;

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
	protected Marker(
			Identifier id, 
			MapView mapView,
			AbstractNode startNode,
			AbstractNode endNode,
			NodeLink mnle,
			MeasurementPath path,
			DoublePoint dpoint)
	{
		this(id, mapView, 0.0, path, path.getMonitoredElementId(), String.valueOf(id.getMinor()));
		
		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeLink = mnle;
		setLocation(dpoint);
	}

	public static Marker createInstance(
			MapView mapView,
			AbstractNode startNode,
			AbstractNode endNode,
			NodeLink mnle,
			MeasurementPath path,
			DoublePoint dpoint)
		throws CreateObjectException 
	{
		if (startNode == null || mapView == null || endNode == null
				|| path == null || dpoint == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new Marker(
				ide,
				mapView,
				startNode,
				endNode,
				mnle,
				path,
				dpoint);
		}
		catch (IllegalObjectEntityException e)
		{
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
		catch (IdentifierGenerationException e) 
		{
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
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
	public Marker(
			Identifier id, 
			MapView mapView,
			double opticalDistance, 
			MeasurementPath path,
			Identifier meId,
			String name)
	{
		super(id);

		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = mapView.getMap().getCreatorId();
		super.modifierId = super.creatorId;
		super.name = id.toString();
		super.description = "";
		super.location = new DoublePoint(0.0, 0.0);
		super.name = name;

		this.mapView = mapView;
		this.meId = meId;
		if(mapView != null)
		{
			this.map = mapView.getMap();
			setMap(map);
		}
//		this.setImageId(IMAGE_NAME);

		this.measurementPath = path;
		this.startNode = measurementPath.getStartNode();
		
		spd = new PathDecompositor(measurementPath.getSchemePath());
	}

	public static Marker createInstance(
			MapView mapView,
			double opticalDistance, 
			MeasurementPath path,
			Identifier meId,
			String name)
		throws CreateObjectException 
	{
		if (meId == null || mapView == null || path == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new Marker(
				ide,
				mapView,
				opticalDistance, 
				path,
				meId,
				name);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
		catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
	}

	public void setId(Identifier id)
	{
		super.id = id;
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	public double getOpticalDistanceFromStart()
	{
		return 0.0;
	}

	public NodeLink previousNodeLink()
	{
		NodeLink nl;
		int index = measurementPath.getSortedNodeLinks().indexOf(nodeLink);
		if(index == 0)
			nl = null;
		else
			nl = (NodeLink)(measurementPath.getSortedNodeLinks().get(index - 1));
		return nl;
	}

	public NodeLink nextNodeLink()
	{
		NodeLink nl;
		int index = measurementPath.getSortedNodeLinks().indexOf(nodeLink);
		if(index == measurementPath.getSortedNodeLinks().size() - 1)
			nl = null;
		else
			nl = (NodeLink)(measurementPath.getSortedNodeLinks().get(index + 1));
		return nl;
	}

	public SiteNode getLeft()
	{
		List nodes = cpath.getSortedNodes();
		AbstractNode node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(startNode)); lit.hasPrevious();)
		{
			node = (AbstractNode)lit.previous();
			if(node instanceof SiteNode)
				break;
			node = null;
		}
		return (SiteNode)node;
	}

	public SiteNode getRight()
	{
		List nodes = cpath.getSortedNodes();
		AbstractNode node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(endNode) - 1); lit.hasNext();)
		{
			node = (AbstractNode)lit.next();
			if(node instanceof SiteNode)
				break;
			node = null;
		}
		return (SiteNode)node;
	}


	public void setMeasurementPath(MeasurementPath measurementPath)
	{
		this.measurementPath = measurementPath;
	}


	public MeasurementPath getMeasurementPath()
	{
		return measurementPath;
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

	public void setNodeLink(NodeLink nodeLink)
	{
		this.nodeLink = nodeLink;
	}


	public NodeLink getNodeLink()
	{
		return nodeLink;
	}


	public void setStartNode(AbstractNode startNode)
	{
		this.startNode = startNode;
	}


	public AbstractNode getStartNode()
	{
		return startNode;
	}


	public void setEndNode(AbstractNode endNode)
	{
		this.endNode = endNode;
	}


	public AbstractNode getEndNode()
	{
		return endNode;
	}


	public CablePath getCablePath()
	{
		return cpath;
	}

	public void setCablePath(CablePath cpath)
	{
		this.cpath = cpath;
	}


	public void setMeId(Identifier meId)
	{
		this.meId = meId;
	}


	public Identifier getMeId()
	{
		return meId;
	}

	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

////////////////////////////////////////////////////////////////////////////////

	public void insert() throws CreateObjectException
	{
		throw new UnsupportedOperationException();
	}

	public List getDependencies()
	{
		throw new UnsupportedOperationException();
	}

	public StorableObject_Transferable getHeaderTransferable()
	{
		throw new UnsupportedOperationException();
	}

	public Object getTransferable()
	{
		throw new UnsupportedOperationException();
	}


	public void setDistance(double distance)
	{
		this.distance = distance;
	}


	public double getDistance()
	{
		return distance;
	}

	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}
}
