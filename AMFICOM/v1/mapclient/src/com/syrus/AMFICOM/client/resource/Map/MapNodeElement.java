/**
 * $Id: MapNodeElement.java,v 1.18 2004/12/07 17:02:03 krupenn Exp $
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
 * @version $Revision: 1.18 $, $Date: 2004/12/07 17:02:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class MapNodeElement extends StubResource
    implements MapElement 
{
	/** ������ ����������� ����������� 
	 * @deprecated
	 */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(14, 14);
	/** ����������� ������ �������� 
	 * @deprecated
	 * */
	public static final Rectangle MIN_BOUNDS = new Rectangle(6, 6);
	/** ������������ ������ �������� 
	 * @deprecated
	 * */
	public static final Rectangle MAX_BOUNDS = new Rectangle(40, 40);

	/** ����������� ��������������� ����������� �� ��������� 
	 * @deprecated
	 * */
	public static final double DEFAULT_SCALE_COEFFICIENT = 1.0;

	/** ����������� �� ��������� 
	 * @deprecated
	 * */	
	public static final String DEFAULT_IMAGE = "images/pc.gif";
	
	/** ����������� ��������������� ����������� 
	 * @deprecated
	 * */
	protected double scaleCoefficient = DEFAULT_SCALE_COEFFICIENT;

	/** ������ ����������� � �������� ����������� 
	 * @deprecated
	 * */
	protected Rectangle bounds = new Rectangle(DEFAULT_BOUNDS);

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
	
	/** ���������� �������� (���������������) 
	 * @deprecated
	 * */
	protected Point2D.Double anchor = new Point2D.Double( 0, 0);

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
	 * @deprecated
	 */
	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}
	
	/**
	 * @deprecated
	 */
	public Rectangle getMinBounds()
	{
		return MIN_BOUNDS;
	}
	
	/**
	 * @deprecated
	 */
	public Rectangle getMaxBounds()
	{
		return MAX_BOUNDS;
	}

	/**
	 * ���������� ����������� ��������������� �����������.
	 * ��� ���� ����������� ������� ��������
	 * @deprecated
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
	 * @deprecated
	 */
	public Rectangle getBounds()
	{
		return bounds;
	}

	/**
	 * ���������� ������� ��������
	 * @deprecated
	 */
	public void setBounds(Rectangle rec)
	{
		bounds = rec;
	}

	/**
	 * �������� ����������� ��������
	 * @deprecated
	 */
	public Image getImage()
	{
		return MapPropertiesManager.getScaledImage(getImageId());
	}
	
	/**
	 * @deprecated
	 */
	public Image getAlarmedImage()
	{
		return getImage();
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
	 * @deprecated
	 */
	public Point2D.Double getAnchor()
	{
		return new Point2D.Double(anchor.x, anchor.y);
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(location.x, location.y);
	}

	/**
	 * ���������� ����� ���������� ������ ��������
	 * @deprecated
	 */
	public void setAnchor(Point2D.Double aAnchor)
	{
		anchor.x = aAnchor.x;
		anchor.y = aAnchor.y;
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

	/**
	 * @deprecated
	 */
	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		return visibleBounds.contains(getAnchor());
	}

	/**
	 * @deprecated
	 */
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
				pg.drawImage(
						getAlarmedImage(),
						p.x - width / 2,
						p.y - height / 2,
						null);

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

	/**
	 * @deprecated
	 */
	private static Rectangle searchBounds = new Rectangle();

	/**
	 * @deprecated
	 */
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

	/**
	 * @deprecated
	 */
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
