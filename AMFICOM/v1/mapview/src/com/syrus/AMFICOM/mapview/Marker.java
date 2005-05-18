/*-
 * $Id: Marker.java,v 1.16 2005/05/18 12:37:39 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;

/**
 * ��������: ������ ���������� ���������� ��������� Lo, ����������      *
 *         ����������������� �����, �� ������������ ���������� Lf,      *
 *         �������� ��� ���������������� �������, � ��������������      *
 *         ���������� Lt, ���������� � ���������� �������� ��           *
 *         ����������� � ����������� ��� ����������� � ���� �����       *
 *         ���������� ��������� ������������ �� �������������:          *
 *             Ku = Lo / Lf                                             *
 *             Kd = Lf / Lt                                             *
 *         ���������� ����������� ������� �� ����� � �� ��������������  *
 *         �������� �� ������ ��������� Lo, � ����� � ��� �����������   *
 *         � ��������� �� ���� �������������� ��������� �������������   *
 *         �� Lo � Lf � Lt � ��������, � ���������� ��������� Lt        *
 *         ������������� � Lf � Lo. ���������� ���������� ������        *
 *         �������� ������� � �����, � ���� ������ ������������ Lf,     *
 *         ��� ��� �������������� ����� �� �������� ���������� � Ku,    *
 *         � ���� �������������� �������������� ������ �����            *
 *         �����������, ����� ���� ����� ������������ Lo.               *
 *
 *
 *
 * @version $Revision: 1.16 $, $Date: 2005/05/18 12:37:39 $
 * @module mapview_v1
 * @author $Author: bass $
 */

public class Marker extends AbstractNode
{
	private static final long serialVersionUID = 02L;
	
	/**
	 * ������������� ������������ �������.
	 */
	protected Identifier monitoredElementId;

	/**
	 * ��������� �� ������ �������������� ���� �� �������.
	 */
	protected double distance = 0.0;

	/**
	 * ���������. ���������������� � ������������ � ����� �������.
	 */
	protected Object descriptor;

	/**
	 * ��� �����.
	 */
	protected MapView mapView;
	/**
	 * ������������� ����, �� ������� ��������� ������.
	 */
	protected MeasurementPath measurementPath;
	/**
	 * ������� ������, �� ������� ��������� ������.
	 */
	protected CablePath cpath;
	/**
	 * ������� �������� �����, �� ������� ��������� ������.
	 */
	protected NodeLink nodeLink;
	/**
	 * ��������� ���� �� ��������� �����, �� ������� ��������� ������.
	 */
	protected AbstractNode startNode;
	/**
	 * �������� ���� �� ��������� �����, �� ������� ��������� ������.
	 */
	protected AbstractNode endNode;

	/**
	 * �������� ������� ������������� �� �����.
	 *
	 * @param id �������������
	 * @param creatorId ������������
	 * @param mapView ��� �����
	 * @param startNode ��������� ���� ���������
	 * @param endNode �������� ���� ���������
	 * @param nodeLink ��������
	 * @param path ������������� ����
	 * @param monitoredElementId ����������� ������
	 * @param dpoint �������������� ���������� �������
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
	 * �������� ������� ������������� �� �����.
	 *
	 * @param creatorId ������������
	 * @param mapView ��� �����
	 * @param startNode ��������� ���� ���������
	 * @param endNode �������� ���� ���������
	 * @param nodeLink ��������
	 * @param path ������������� ����
	 * @param monitoredElementId ����������� ������
	 * @param dpoint �������������� ���������� �������
	 * @return ����� ������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException ������ �������
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
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
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
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * �������� ������� �� ������ ����������� ��������� � ��������� ����������
	 * ���������.
	 *
	 * @param id �������������
	 * @param creatorId ������������
	 * @param mapView ��� �����
	 * @param opticalDistance ���������� ��������� �� ������ ����
	 * @param path ������������� ����
	 * @param monitoredElementId ������������� ������������ �������
	 * @param name �������� �������
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
		this.measurementPath = path;
		this.startNode = this.measurementPath.getStartNode();
	}

	/**
	 * �������� ������� �� ������ ����������� ��������� � ��������� ����������
	 * ���������.
	 *
	 * @param creatorId ������������
	 * @param mapView ��� �����
	 * @param opticalDistance ���������� ��������� �� ������ ����
	 * @param path ������������� ����
	 * @param monitoredElementId ������������� ������������ �������
	 * @param name �������� �������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * @return ����� ������
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
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
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
	}

	/**
	 * ���������� �������������.
	 * @param id �������������
	 */
	public void setId(Identifier id)
	{
		super.id = id;
	}

	/**
	 * ���������� ��� �����.
	 * @param mapView ��� �����
	 */
	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	/**
	 * �������� ��� �����.
	 * @return ��� �����
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
		SiteNode previous = null;
		for (Iterator it = nodes.iterator(); it.hasNext();) {
			AbstractNode node = (AbstractNode) it.next();
			if (node instanceof SiteNode)
				previous = (SiteNode) node;
			if(node.equals(this.startNode))
				break;
		}
		return previous;
	}

	public SiteNode getRight()
	{
		List nodes = this.cpath.getSortedNodes();
		SiteNode found = null;
		
		for (ListIterator it = nodes.listIterator(nodes.size()); it.hasPrevious();) {
			AbstractNode node = (AbstractNode) it.previous();
			if (node instanceof SiteNode)
				found = (SiteNode )node;
			if(node.equals(this.endNode))
				break;
		}
		return found;
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
	public void insert()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public Set getDependencies()
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
	public IDLEntity getTransferable()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(Set characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(Set characteristics) {
		throw new UnsupportedOperationException();
	}
}
