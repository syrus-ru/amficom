/**
 * $Id: MapNodeElement.java,v 1.19 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ������� ����� - ���� 
 * 
 * 
 * 
 * @version $Revision: 1.19 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapNodeElement extends StubResource
    implements MapElement 
{
	protected Map map;

	/** ���� ��������� �������� */
	protected boolean selected = false;

	/**
	 * @deprecated
	 */
	protected String id = "";
	
	/**
	 * @deprecated
	 */
	protected String name = "";
	
	/**
	 * @deprecated
	 */
	protected String description = "";
	
	/**
	 * @deprecated
	 */
	protected String mapId = "";
	
	/**
	 * @deprecated
	 */
	protected String imageId = "";
	
	protected DoublePoint location = new DoublePoint(0, 0);

	public String optimizerAttribute = "optional";// "mandatory", "restricted"

	/** �������� ����������� 
	 * @deprecated
	 * */
	public java.util.Map attributes = new HashMap();
	
	/** ���� ������� ������� ������� */
	protected boolean alarmState = false;
	
	/** ���� ����, ��� ������� ������ */
	protected boolean removed = false;

	public boolean isRemoved()
	{
		return removed;
	}
	
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	/**
	 * ���������� ������������� �����������, �� �������� ������������ 
	 * �����������
	 */
	public void setImageId(String iconPath)
	{
		imageId = iconPath;
	}

	/**
	 * �������� ������������� �����������
	 */
	public String getImageId()
	{
		return imageId;
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(location.x, location.y);
	}

	public void setLocation(DoublePoint location)
	{
		this.location.x = location.x;
		this.location.y = location.y;
	}

	public boolean isSelected ()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}
	
	public String getId()
	{
		return id;
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @deprecated
	 */
	public String getDomainId()
	{
		return getMap().getDomainId();
	}

	public void setAlarmState(boolean i)
	{
		alarmState = i;
	}

	public boolean getAlarmState()
	{
		return alarmState;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap( Map map)
	{
		this.map = map;
		if(map != null)
			this.mapId = map.getId();
	}

	/**
	 * �������� ������ NodeLinks, ���������� �������� Node
	 */
	public List getNodeLinks()
	{
		Environment.log(
			Environment.LOG_LEVEL_FINER, 
			"method call", 
			getClass().getName(), 
			"getNodeLinks()");
		
		List returnList = new LinkedList();
		for(Iterator it = getMap().getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			
			//���� ���� �� ������ �������� ������ node �� ��������� ��� � ������
			if ( (nodeLink.getEndNode().equals(this)) || (nodeLink.getStartNode().equals(this)))
			{
				returnList.add(nodeLink);
			}
		}

		return returnList;
	}

	/**
	 * ���������� �������� �����, �� ������ ����������� � ���������.
	 * ��� ��������������� ���� ���������� ������������ ���������������,
	 * ��� �������� ���� �� ����� ���� ���������, �� ���� ������� �����
	 * �� ������ �������������� � ���������� null
	 */
	public MapNodeLinkElement getOtherNodeLink(MapNodeLinkElement nl)
	{
		if(!this.getClass().equals(MapPhysicalNodeElement.class))
		{
			try
			{
				throw new Exception("dummy");
			}
			catch(Exception e)
			{
				Environment.log(
						Environment.LOG_LEVEL_FINER, 
						"current execution point with call stack:", 
						null, 
						null, 
						e);
			}

			return null;
		}

		MapNodeLinkElement startNodeLink = null;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
			{
				MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
				if(nl != mnle)
				{
					startNodeLink = mnle;
					break;
				}
			}
			
		return startNodeLink;
	}


	/**
	 * �������� ������ PhysicalLink, ������������ ��� ���������������
	 * �� ������ ����
	 */
	public List getPhysicalLinks()
	{
		Environment.log(
			Environment.LOG_LEVEL_FINER, 
			"method call", 
			getClass().getName(), 
			"getPhysicalLinks()");
		
		List returnList = new LinkedList();

		for(Iterator it = getMap().getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement physicalLink = 
				(MapPhysicalLinkElement )it.next();
			
			//���� ���� �� ������ �������� ������ node �� ��������� ��� � ������
			if ( (physicalLink.getEndNode().equals(this)) 
					|| (physicalLink.getStartNode().equals(this)) )
				returnList.add(physicalLink);
		}

		return returnList;
	}

	/**
	 * �������� ������ Node ��������������� � ���� �������� NodeLink, �������
	 * ��������
	 */
	public List getOppositeNodes()
	{
		Environment.log(
			Environment.LOG_LEVEL_FINER, 
			"method call", 
			getClass().getName(), 
			"getOppositeNodes()");
		
		Iterator e = getNodeLinks().iterator();
		LinkedList returnList = new LinkedList();

		while (e.hasNext())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();

			if ( nodeLink.getEndNode().equals(this) )
				returnList.add(nodeLink.getStartNode());
			else
				returnList.add(nodeLink.getEndNode());
		}

		return returnList;
	}
	
	/**
	 * �������� ������� ���������
	 */
	public MapElementState getState()
	{
		return new MapNodeElementState(this);
	}

	/**
	 * ������������ ���������
	 */
	public void revert(MapElementState state)
	{
		MapNodeElementState mnes = (MapNodeElementState )state;
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		setLocation(mnes.location);
		optimizerAttribute = mnes.optimizerAttribute;
		attributes = new HashMap(mnes.attributes);
	}

}
