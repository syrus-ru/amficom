/**
 * $Id: MeasurementPath.java,v 1.13 2005/03/25 18:07:54 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.PathElementType;

import java.util.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * ������� ����.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/03/25 18:07:54 $
 * @module mapviewclient_v1
 */
public class MeasurementPath implements MapElement
{
	/**
	 * �������������.
	 */
	protected Identifier id;

	/**
	 * ��������.
	 */
	protected String	name;

	/**
	 * ��������.
	 */
	protected String	description;

	/**
	 * ���� ���������.
	 */
	protected transient boolean selected = false;

	/**
	 * ���� ������� ������� �������.
	 */
	protected transient boolean alarmState = false;

	/**
	 * ���� ��������.
	 */
	protected transient boolean removed = false;

	/**
	 * �������������� �����.
	 */
	protected transient Map map = null;

	/**
	 * ������� ����.
	 */
	protected SchemePath schemePath;
	/**
	 * �����.
	 */
	protected Scheme scheme;

	/**
	 * ��� �����.
	 */
	protected MapView mapView;

	/**
	 * �����������.
	 * @param schemePath ������� ����
	 * @param id �������������
	 * @param mapView ���
	 */
	protected MeasurementPath(
			SchemePath schemePath,
			Identifier id, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;
		this.schemePath = schemePath;
		this.name = schemePath.getName();
		if(mapView != null)
		{
			this.map = mapView.getMap();
		}
	}

	/**
	 * ������� ����� ������� ����.
	 * @param schemePath ������� ����
	 * @param mapView ��� �����
	 * @return ����� ������� ����
	 * @throws com.syrus.AMFICOM.general.CreateObjectException ������ �������
	 */
	public static MeasurementPath createInstance(
			SchemePath schemePath,
			MapView mapView)
		throws CreateObjectException 
	{
		if (mapView == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new MeasurementPath(
				schemePath,
				ide,
				mapView);
		}
		catch (IllegalObjectEntityException e)
		{
			throw new CreateObjectException("MapMeasurementPathElement.createInstance | cannot generate identifier ", e);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapMeasurementPathElement.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection getCharacteristics() 
	{
		return this.schemePath.getCharacteristics();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCharacteristic(Characteristic ch)
	{
		this.schemePath.addCharacteristic(ch);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		this.schemePath.removeCharacteristic(ch);
	}
	
	/**
	 * ���������� �������������
	 * @param id �������������
	 */
	public void setId(Identifier id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Identifier getId()
	{
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() 
	{
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) 
	{
		this.name = name;
	}

	/**
	 * �������� ��������.
	 * @return ��������
	 */
	public String getDescription() 
	{
		return this.description;
	}

	/**
	 * ���������� ��������.
	 * @param description ��������
	 */
	public void setDescription(String description) 
	{
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map getMap()
	{
		return this.map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState()
	{
		return this.alarmState;
	}

	protected DoublePoint location = new DoublePoint(0.0, 0.0);

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation()
	{
		int count = 0;
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			DoublePoint an = cpath.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}
		this.location.setLocation(x /= count, y /= count);
		
		return this.location;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved()
	{
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
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

	/**
	 * ���������� ������� ����.
	 * @param schemePath ������� ����.
	 */
	public void setSchemePath(SchemePath schemePath)
	{
		this.schemePath = schemePath;
		this.name = schemePath.getName();
		this.scheme = schemePath.scheme();
	}

	/**
	 * �������� ������� ����.
	 * @return ������� ����
	 */
	public SchemePath getSchemePath()
	{
		return this.schemePath;
	}

	/**
	 * @deprecated
	 */
	public boolean isSelectionVisible()
	{
		return isSelected();
	}

	/**
	 * ���������� �������������� ������ � ������
	 * @return �������������� �����
	 */
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getSortedCablePaths().iterator();
		while( e.hasNext())
		{
			CablePath cpath = (CablePath)e.next();
			length = length + cpath.getLengthLt();
		}
		return length;
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @return ���������� �����
	 */
	public double getLengthLf()
	{
		return SchemeUtils.getPhysicalLength(this.schemePath);
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @return ���������� �����
	 */
	public double getLengthLo()
	{
		return SchemeUtils.getOpticalLength(this.schemePath);
	}

	/**
	 * ��������������� ������ ��������� �����, �� ������� �������� 
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List unsortedCablePaths = new LinkedList();

	/**
	 * �������� ������ �������������� �������, ������� ������ � ������ ����.
	 * ������ �������� �����������.
	 * @return ������ ��������� �����
	 */
	protected List getCablePaths()
	{
		synchronized(this.unsortedCablePaths)
		{
			this.unsortedCablePaths.clear();
			for(int i = 0; i < this.schemePath.links().length; i++)
			{
				PathElement pe = this.schemePath.links()[i];
				switch(pe.getPathElementType().value())
				{
					case PathElementType._SCHEME_ELEMENT:
						SchemeElement se = (SchemeElement )pe.getAbstractSchemeElement();
						SiteNode site = this.mapView.findElement(se);
						if(site != null)
						{
							//TODO think if link to 'site' is needed for mPath
		//					mPath.addCablePath(site);
						}
						break;
					case PathElementType._SCHEME_LINK:
						SchemeLink link = (SchemeLink )pe.getAbstractSchemeElement();
						SchemeElement sse = SchemeUtils.getSchemeElementByDevice(this.scheme, link.getSourceSchemePort().getParentSchemeDevice());
						SchemeElement ese = SchemeUtils.getSchemeElementByDevice(this.scheme, link.getTargetSchemePort().getParentSchemeDevice());
						SiteNode ssite = this.mapView.findElement(sse);
						SiteNode esite = this.mapView.findElement(ese);
						if(ssite == esite)
						{
							//TODO think if link to 'link' is needed for mPath
		//					mPath.addCablePath(ssite);
						}
						break;
					case PathElementType._SCHEME_CABLE_LINK:
						SchemeCableLink clink = (SchemeCableLink )pe.getAbstractSchemeElement();
						CablePath cp = this.mapView.findCablePath(clink);
						if(cp != null)
						{
							this.unsortedCablePaths.add(cp);
						}
						break;
					default:
						throw new UnsupportedOperationException();
				}
			}
		}
		return Collections.unmodifiableList(this.unsortedCablePaths);
	}

	/**
	 * ������������� ������ ��������� �����, �� ������� �������� 
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List sortedCablePaths = new LinkedList();
	/**
	 * ������������� ������ ���������� �����, �� ������� �������� 
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List sortedNodeLinks = new LinkedList();
	/**
	 * ������������� ������ �����, �� ������� �������� 
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List sortedNodes = new LinkedList();

	/**
	 * Get {@link #sortedNodeLinks}.
	 * @return this.sortedNodeLinks
	 */
	public List getSortedNodeLinks()
	{
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}
	
	/**
	 * Get {@link #sortedNodes}.
	 * @return this.sortedNodes
	 */
	public List getSortedNodes()
	{
		return Collections.unmodifiableList(this.sortedNodes);
	}
	
	/**
	 * Get {@link #sortedCablePaths}.
	 * @return this.sortedCablePaths
	 */
	public List getSortedCablePaths()
	{
		return Collections.unmodifiableList(this.sortedCablePaths);
	}
	
	/**
	 * �������� ��������� ���� ����. ������������ �����������.
	 * @return ����
	 */
	public AbstractNode getStartNode()
	{
		return getMapView().getStartNode(this.getSchemePath());
	}
	
	/**
	 * �������� �������� ���� ����. ������������ �����������.
	 * @return ����
	 */
	public AbstractNode getEndNode()
	{
		return getMapView().getEndNode(this.getSchemePath());
	}
	
	/**
	 * ����������� �������� ����. �������� ���������� ��������� �����,
	 * ���������� ����� � �����.
	 */
	public void sortPathElements()
	{
		this.sortedCablePaths.clear();
		this.sortedNodeLinks.clear();
		this.sortedNodes.clear();
		
		AbstractNode node = getStartNode();

		this.sortedCablePaths.addAll(getCablePaths());
		
		for(Iterator it = this.sortedCablePaths.iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			cpath.sortNodeLinks();
			if(cpath.getStartNode().equals(node))
			{
				this.sortedNodeLinks.addAll(cpath.getSortedNodeLinks());
				this.sortedNodes.addAll(cpath.getSortedNodes());
			}
			else
			{
				for(ListIterator lit = cpath.getSortedNodeLinks().listIterator(cpath.getSortedNodeLinks().size()); lit.hasPrevious();)
				{
					this.sortedNodeLinks.add(lit.previous());
				}
				for(ListIterator lit = cpath.getSortedNodes().listIterator(cpath.getSortedNodes().size()); lit.hasPrevious();)
				{
					this.sortedNodes.add(lit.previous());
				}
			}
			node = cpath.getOtherNode(node);

			// to avoid duplicate entry
			this.sortedNodes.remove(node);
		}
		this.sortedNodes.add(node);
	}

	/**
	 * �������� ��������� �������� �� ������� ������������� ����������.
	 * @param nodeLink ��������
	 * @return ��������� ��������, ��� <code>null</code>, ���� nl - ��������� 
	 * � ������
	 */
	public NodeLink nextNodeLink(NodeLink nodeLink)
	{
		int index = getSortedNodeLinks().indexOf(nodeLink);
		if(index == getSortedNodeLinks().size() - 1)
			return null;

		return (NodeLink)getSortedNodeLinks().get(index + 1);
	}

	/**
	 * �������� ���������� �������� �� ������� ������������� ����������.
	 * @param nodeLink ��������
	 * @return ���������� ��������, ��� <code>null</code>, ���� nl - ������ 
	 * � ������
	 */
	public NodeLink previousNodeLink(NodeLink nodeLink)
	{
		int index = getSortedNodeLinks().indexOf(nodeLink);
		if(index == 0)
			return null;

		return (NodeLink)getSortedNodeLinks().get(index - 1);
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

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
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
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
		throw new UnsupportedOperationException();
	}
}
