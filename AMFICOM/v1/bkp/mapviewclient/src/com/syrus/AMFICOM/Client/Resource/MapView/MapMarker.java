/**
 * $Id: MapMarker.java,v 1.21 2004/12/22 16:38:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.PathDecompositor;

import java.lang.UnsupportedOperationException;

import java.util.Date;
import java.util.LinkedList;
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
 * @version $Revision: 1.21 $, $Date: 2004/12/22 16:38:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */

public class MapMarker extends AbstractNode implements MapElement
{
	private static final long serialVersionUID = 02L;
	
	/**
	 * @deprecated
	 */
	public static final String IMAGE_NAME = "marker";


	protected Identifier meId;

	protected double distance = 0.0;

	protected Object descriptor;

	protected MapView mapView;
	protected MapMeasurementPathElement measurementPath;
	protected PathDecompositor spd = null;
	protected MapCablePathElement cpath;
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
	public MapMarker(
			Identifier id, 
			MapView mapView,
			AbstractNode startNode,
			AbstractNode endNode,
			NodeLink mnle,
			MapMeasurementPathElement path,
			DoublePoint dpoint)
	{
		this(id, mapView, 0.0, path, path.getMonitoredElementId());
		
		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeLink = mnle;
		setLocation(dpoint);
	}

	public static MapMarker createInstance(
			MapView mapView,
			AbstractNode startNode,
			AbstractNode endNode,
			NodeLink mnle,
			MapMeasurementPathElement path,
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
			return new MapMarker(
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
	public MapMarker(
			Identifier id, 
			MapView mapView,
			double opticalDistance, 
			MapMeasurementPathElement path,
			Identifier meId)
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

	public static MapMarker createInstance(
			MapView mapView,
			double opticalDistance, 
			MapMeasurementPathElement path,
			Identifier meId)
		throws CreateObjectException 
	{
		if (meId == null || mapView == null || path == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new MapMarker(
				ide,
				mapView,
				opticalDistance, 
				path,
				meId);
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

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
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


	public void setMeasurementPath(MapMeasurementPathElement measurementPath)
	{
		this.measurementPath = measurementPath;
	}


	public MapMeasurementPathElement getMeasurementPath()
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

//	public List getCharacteristics() 
//	{
//		return Collections.unmodifiableList(this.characteristics);
//	}
//
//	public void addCharacteristic(Characteristic ch)
//	{
//		this.characteristics.add(ch);
//	}
//
//	public void removeCharacteristic(Characteristic ch)
//	{
//		this.characteristics.remove(ch);
//	}
//	
//	public void setId(Identifier id)
//	{
//		this.id = id;
//	}
//	
//	public Identifier getId()
//	{
//		return id;
//	}
//
//	public String getName() 
//	{
//		return this.name;
//	}
//
//	public void setName(String name) 
//	{
//		this.name = name;
//	}
//
//	public String getDescription() 
//	{
//		return this.description;
//	}
//
//	public void setDescription(String description) 
//	{
//		this.description = description;
//	}
//
//	public Identifier getImageId() 
//	{
//		return this.imageId;
//	}
//	
//	public void setImageId(Identifier imageId) 
//	{
//		this.imageId = imageId;
//	}
//	
//	public Map getMap()
//	{
//		return map;
//	}
//
//	public void setMap(Map map)
//	{
//		this.map = map;
//	}
//
//	public boolean isSelected()
//	{
//		return selected;
//	}
//
//	public void setSelected(boolean selected)
//	{
//		this.selected = selected;
//		getMap().setSelected(this, selected);
//	}
//
//	public void setAlarmState(boolean alarmState)
//	{
//		this.alarmState = alarmState;
//	}
//
//	public boolean getAlarmState()
//	{
//		return alarmState;
//	}
//
//	public DoublePoint getLocation()
//	{
//		return new DoublePoint(location.x, location.y);
//	}
//
//	public void setLocation(DoublePoint location)
//	{
//		this.location.x = location.x;
//		this.location.y = location.y;
//	}
//
//	public boolean isRemoved()
//	{
//		return removed;
//	}
//
//	public void setRemoved(boolean removed)
//	{
//		this.removed = removed;
//	}

//	protected Identifier id;
//
//	protected String	name;
//
//	protected String	description;
//
//	protected Identifier imageId;
//
//	protected List		characteristics;
//
//	protected DoublePoint location = new DoublePoint(0, 0);
//
//
//	protected transient boolean selected = false;
//
//	protected transient boolean alarmState = false;
//
//	protected transient boolean removed = false;
//
//	protected transient Map map = null;


}
