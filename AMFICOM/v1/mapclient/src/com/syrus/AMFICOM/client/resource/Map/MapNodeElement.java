/**
 * $Id: MapNodeElement.java,v 1.14 2004/10/18 12:43:13 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ������� ����� - ���� 
 * 
 * 
 * 
 * @version $Revision: 1.14 $, $Date: 2004/10/18 12:43:13 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapNodeElement extends StubResource
    implements MapElement 
{
	/** ������ ����������� ����������� */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(14, 14);
	/** ����������� ������ �������� */
	public static final Rectangle MIN_BOUNDS = new Rectangle(6, 6);
	/** ������������ ������ �������� */
	public static final Rectangle MAX_BOUNDS = new Rectangle(40, 40);

	/** ����������� ��������������� ����������� �� ��������� */
	public static final double DEFAULT_SCALE_COEFFICIENT = 1.0;

	/** ����������� �� ��������� */	
	public static final String DEFAULT_IMAGE = "images/pc.gif";
	
	/** ����������� ��������������� ����������� */
	protected double scaleCoefficient = DEFAULT_SCALE_COEFFICIENT;

	/** ������ ����������� � �������� ����������� */
	protected Rectangle bounds = new Rectangle(DEFAULT_BOUNDS);

	protected Map map;

	/** ���� ��������� �������� */
	protected boolean selected = false;

	protected String id = "";
	protected String name = "";
	protected String description = "";
	protected String mapId = "";
	protected String imageId = "";
	
	/** ���������� �������� (���������������) */
	protected Point2D.Double anchor = new Point2D.Double( 0, 0);

	public String optimizerAttribute = "optional";// "mandatory", "restricted"

	/** �������� ����������� */
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

	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}
	
	public Rectangle getMinBounds()
	{
		return MIN_BOUNDS;
	}
	
	public Rectangle getMaxBounds()
	{
		return MAX_BOUNDS;
	}

	/**
	 * ���������� ����������� ��������������� �����������.
	 * ��� ���� ����������� ������� ��������
	 */
	public void setScaleCoefficient(double ss)
	{
		scaleCoefficient = ss;
		int w = (int )((double )getDefaultBounds().width * scaleCoefficient);
		int h = (int )((double )getDefaultBounds().height * scaleCoefficient);
		
		if (w >= this.getMaxBounds().width || h >= this.getMaxBounds().height )
		{
			w = this.getMaxBounds().width;
			h = this.getMaxBounds().height;
		}
		else
		if (w <= this.getMinBounds().width || h <= this.getMinBounds().height )
		{
			w = this.getMinBounds().width;
			h = this.getMinBounds().height;
		}
		this.setBounds(new Rectangle(w, h));

		MapPropertiesManager.setScaledImageSize(this.getImageId(), w, h);
	}
	
	/**
	 * �������� ������� ��������
	 */
	public Rectangle getBounds()
	{
		return bounds;
	}

	/**
	 * ���������� ������� ��������
	 */
	public void setBounds(Rectangle rec)
	{
		bounds = rec;
	}

	/**
	 * �������� ����������� ��������
	 */
	public Image getImage()
	{
		return MapPropertiesManager.getScaledImage(getImageId());
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

	/**
	 * �������� ����� ������ �������� (��� ��������� ��� ��������� �����������)
	 */
	public Point2D.Double getAnchor()
	{
		return new Point2D.Double(anchor.x, anchor.y);
	}

	/**
	 * ���������� ����� ���������� ������ ��������
	 */
	public void setAnchor(Point2D.Double aAnchor)
	{
		anchor.x = aAnchor.x;
		anchor.y = aAnchor.y;
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

	public String getDomainId()
	{
		return getMap().getDomainId();
	}

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		return visibleBounds.contains(getAnchor());
	}

	public void paint (Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;
		
		MapCoordinatesConverter converter = getMap().getConverter();
		
		Point p = converter.convertMapToScreen(getAnchor());

		Graphics2D pg = (Graphics2D )g;
		
		int width = getBounds().width;
		int height = getBounds().height;

		pg.drawImage(
				getImage(),
                p.x - width / 2,
                p.y - height / 2,
                null);

		// ���� �� �������� ���� ������ �������, �� ��������
		// � ����������� �� ����� getShowAlarmed()
		if (this.getAlarmState())
		{
			if ( MapPropertiesManager.isShowAlarmState() )
			{
				pg.setStroke(MapPropertiesManager.getAlarmedStroke());
				pg.setColor(MapPropertiesManager.getAlarmedColor());
				pg.drawRect( 
						p.x - width / 2,
						p.y - height / 2,
						width,
						height);
			}
		}

		//���� ������� �� �������� �����
		if (isSelected())
		{
			pg.setStroke(new BasicStroke(3));
			pg.setColor(MapPropertiesManager.getSelectionColor());
			pg.drawRect( 
					p.x - width / 2,
					p.y - height / 2,
					width,
					height);
		}
	}

	public void setAlarmState(boolean i)
	{
		alarmState = i;
	}

	public boolean getAlarmState()
	{
		return alarmState;
	}

	private static Rectangle searchBounds = new Rectangle();

	public boolean isMouseOnThisObject (Point currentMousePoint)
	{
		MapCoordinatesConverter converter = getMap().getConverter();
		
		//�������� ���� ��� ������ ���������� � ��������������
		int width = (int )getBounds().getWidth();
		int height = (int )getBounds().getHeight();
		Point p = converter.convertMapToScreen(getAnchor());
		searchBounds.setBounds(
				p.x - width / 2 , 
				p.y - height / 2 , 
				width, 
				height);
		if (searchBounds.contains(currentMousePoint))
		{
			return true;
		}
		return false;
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

	public String getToolTipText()
	{
		String s1 = getName();

		return s1 + " [" + LangModel.getString("node" + getTyp()) + "]";
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
		
		LinkedList returnList = new LinkedList();
		for(Iterator it = map.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			
			//���� ���� �� ������ �������� ������ node �� ��������� ��� � ������
			if ( (nodeLink.endNode == this) || (nodeLink.startNode == this))
			{
				returnList.add(nodeLink);
			}
		}

		return returnList;
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
		
		LinkedList returnList = new LinkedList();

		for(Iterator it = map.getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement physicalLink = 
				(MapPhysicalLinkElement )it.next();
			
			//���� ���� �� ������ �������� ������ node �� ��������� ��� � ������
			if ( (physicalLink.endNode == this) 
					|| (physicalLink.startNode == this) )
				returnList.add( physicalLink);
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

			if ( nodeLink.getEndNode() == this )
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
		setBounds(mnes.bounds);
		setScaleCoefficient(mnes.scaleCoefficient);
		setName(mnes.name);
		setDescription(mnes.description);
		setImageId(mnes.imageId);
		setAnchor(mnes.anchor);
		optimizerAttribute = mnes.optimizerAttribute;
		attributes = new HashMap(mnes.attributes);
	}

}
