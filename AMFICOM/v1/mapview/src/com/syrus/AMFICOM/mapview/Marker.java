/**
 * $Id: Marker.java,v 1.5 2005/02/02 15:17:30 krupenn Exp $
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
import com.syrus.AMFICOM.scheme.PathDecompositor;

import java.util.Date;
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
 * @version $Revision: 1.5 $, $Date: 2005/02/02 15:17:30 $
 * @module mapview_v1
 * @author $Author: krupenn $
 */

public class Marker extends AbstractNode
{
	private static final long serialVersionUID = 02L;
	
	/**
	 * Идентификатор исследуемого объекта.
	 */
	protected Identifier monitoredElementId;

	/**
	 * Дистанция от начала измерительного пути до маркера.
	 */
	protected double distance = 0.0;

	/**
	 * Описатель. Интерпретируется в соответствии с типом маркера.
	 */
	protected Object descriptor;

	/**
	 * Вид карты.
	 */
	protected MapView mapView;
	/**
	 * Измерительный путь, на котором находится маркер.
	 */
	protected MeasurementPath measurementPath;
	/**
	 * Декомпозитор пути, позволяющий маркеру передвигаться вдоль 
	 * измерительного пути.
	 */
	protected PathDecompositor spd = null;
	/**
	 * Текущий кабель, на котором находится маркер.
	 */
	protected CablePath cpath;
	/**
	 * Текущий фрагмент линии, на котором находится маркер.
	 */
	protected NodeLink nodeLink;
	/**
	 * Начальный узел на фрагменте линии, на котором находится маркер.
	 */
	protected AbstractNode startNode;
	/**
	 * Конечный узел на фрагменте линии, на котором находится маркер.
	 */
	protected AbstractNode endNode;

	/**
	 * Создание маркера пользователем на карте.
	 * 
	 * @param id ижентификатор
	 * @param creatorId пользователь
	 * @param mapView вид карты
	 * @param startNode начальный узел фрагмента
	 * @param endNode конечный узел фрагмента
	 * @param nodeLink фрагмент
	 * @param path измерительный путь
	 * @param monitoredElementId исследуемый объект
	 * @param dpoint географические координаты меркера
	 */
	protected Marker(
			Identifier id, 
			Identifier creatorId,
			MapView mapView,
			AbstractNode startNode,
			AbstractNode endNode,
			NodeLink nodeLink,
			MeasurementPath path,
			Identifier monitoredElementId,
			DoublePoint dpoint)
	{
		this(id, creatorId, mapView, 0.0, path, monitoredElementId, String.valueOf(id.getMinor()));
		
		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeLink = nodeLink;
		setLocation(dpoint);
	}

	/**
	 * Создание маркера пользователем на карте.
	 * 
	 * @param creatorId пользователь
	 * @param mapView вид карты
	 * @param startNode начальный узел фрагмента
	 * @param endNode конечный узел фрагмента
	 * @param nodeLink фрагмент
	 * @param path измерительный путь
	 * @param monitoredElementId исследуемый объект
	 * @param dpoint географические координаты меркера
	 * @return новый маркер
	 * @throws com.syrus.AMFICOM.general.CreateObjectException нельзя создать
	 */
	public static Marker createInstance(
			Identifier creatorId,
			MapView mapView,
			AbstractNode startNode,
			AbstractNode endNode,
			NodeLink nodeLink,
			MeasurementPath path,
			Identifier monitoredElementId,
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
				creatorId,
				mapView,
				startNode,
				endNode,
				nodeLink,
				path,
				monitoredElementId,
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
	 * дистанции.
	 * 
	 * @param id идентификатор
	 * @param creatorId пользователь
	 * @param mapView вид карты
	 * @param opticalDistance оптическая дистанция от начала пути
	 * @param path измерительный путь
	 * @param monitoredElementId идентификатор исследуемого объекта
	 * @param name название маркера
	 */
	public Marker(
			Identifier id, 
			Identifier creatorId,
			MapView mapView,
			double opticalDistance, 
			MeasurementPath path,
			Identifier monitoredElementId,
			String name)
	{
		super(id);

		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = super.creatorId;
		super.name = id.toString();
		super.description = "";
		super.location = new DoublePoint(0.0, 0.0);
		super.name = name;

		this.mapView = mapView;
		this.monitoredElementId = monitoredElementId;
		if(mapView != null)
		{
			this.map = mapView.getMap();
			setMap(this.map);
		}

		this.measurementPath = path;
		this.startNode = this.measurementPath.getStartNode();
		
		this.spd = new PathDecompositor(this.measurementPath.getSchemePath());
	}

	/**
	 * Создание маркера на основе полученного сообщения с указанием оптической 
	 * дистанции.
	 * 
	 * @param creatorId пользователь
	 * @param mapView вид карты
	 * @param opticalDistance оптическая дистанция от начала пути
	 * @param path измерительный путь
	 * @param monitoredElementId идентификатор исследуемого объекта
	 * @param name название маркера
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * @return новый маркер
	 */
	public static Marker createInstance(
			Identifier creatorId,
			MapView mapView,
			double opticalDistance, 
			MeasurementPath path,
			Identifier monitoredElementId,
			String name)
		throws CreateObjectException 
	{
		if (monitoredElementId == null || mapView == null || path == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new Marker(
				ide,
				creatorId,
				mapView,
				opticalDistance, 
				path,
				monitoredElementId,
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

	/**
	 * Установить идентификатор.
	 * @param id идентификатор
	 */
	public void setId(Identifier id)
	{
		super.id = id;
	}

	/**
	 * Установить вид карты.
	 * @param mapView вид карты
	 */
	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	/**
	 * Получить вид карты.
	 * @return вид карты
	 */
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
		NodeLink nLink;
		int index = this.measurementPath.getSortedNodeLinks().indexOf(this.nodeLink);
		if(index == 0)
			nLink = null;
		else
			nLink = (NodeLink )(this.measurementPath.getSortedNodeLinks().get(index - 1));
		return nLink;
	}

	public NodeLink nextNodeLink()
	{
		NodeLink nLink;
		int index = this.measurementPath.getSortedNodeLinks().indexOf(this.nodeLink);
		if(index == this.measurementPath.getSortedNodeLinks().size() - 1)
			nLink = null;
		else
			nLink = (NodeLink )(this.measurementPath.getSortedNodeLinks().get(index + 1));
		return nLink;
	}

	public SiteNode getLeft()
	{
		List nodes = this.cpath.getSortedNodes();
		AbstractNode node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(this.startNode)); lit.hasPrevious();)
		{
			node = (AbstractNode )lit.previous();
			if(node instanceof SiteNode)
				break;
			node = null;
		}
		return (SiteNode )node;
	}

	public SiteNode getRight()
	{
		List nodes = this.cpath.getSortedNodes();
		AbstractNode node = null;
		for(ListIterator lit = nodes.listIterator(nodes.indexOf(this.endNode) - 1); lit.hasNext();)
		{
			node = (AbstractNode )lit.next();
			if(node instanceof SiteNode)
				break;
			node = null;
		}
		return (SiteNode )node;
	}

	public void setMeasurementPath(MeasurementPath measurementPath)
	{
		this.measurementPath = measurementPath;
	}

	public MeasurementPath getMeasurementPath()
	{
		return this.measurementPath;
	}

	public void setDescriptor(Object descriptor)
	{
		this.descriptor = descriptor;
	}

	public Object getDescriptor()
	{
		return this.descriptor;
	}

	public PathDecompositor getPathDecompositor()
	{
		return this.spd;
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
		return this.nodeLink;
	}

	public void setStartNode(AbstractNode startNode)
	{
		this.startNode = startNode;
	}

	public AbstractNode getStartNode()
	{
		return this.startNode;
	}

	public void setEndNode(AbstractNode endNode)
	{
		this.endNode = endNode;
	}

	public AbstractNode getEndNode()
	{
		return this.endNode;
	}

	public CablePath getCablePath()
	{
		return this.cpath;
	}

	public void setCablePath(CablePath cpath)
	{
		this.cpath = cpath;
	}

	public void setMeId(Identifier meId)
	{
		this.monitoredElementId = meId;
	}

	public Identifier getMeId()
	{
		return this.monitoredElementId;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public double getDistance()
	{
		return this.distance;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public void insert() throws CreateObjectException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public List getDependencies()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public StorableObject_Transferable getHeaderTransferable()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable 
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public Object getTransferable()
	{
		throw new UnsupportedOperationException();
	}


}
