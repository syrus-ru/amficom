/**
 * $Id: AbstractNodeController.java,v 1.8 2005/03/02 12:31:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;

/**
 * ���������� ����.
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/03/02 12:31:39 $
 * @module mapviewclient_v1
 */
public abstract class AbstractNodeController extends AbstractMapElementController
{
	/** ������ ����������� �����������. */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(14, 14);
	/** ����������� ������ ��������. */
	public static final Rectangle MIN_BOUNDS = new Rectangle(6, 6);
	/** ������������ ������ ��������. */
	public static final Rectangle MAX_BOUNDS = new Rectangle(40, 40);

	/** ������ ����������� � �������� �����������. */
	protected static java.util.Map boundsContainer = new HashMap();

	/**
	 * ��������� ������ �������� ������ �������. ������������ ��� �����������
	 * ��������� ������ �������� � �������.
	 */
	protected static Point2D.Double anchorContainer = new Point2D.Double(0.0D, 0.0D);

	/**
	 * ��������� ������ ������� ������. ������������ ��� �����������
	 * ��������� ������ �������� � ������� �������.
	 */
	protected static Rectangle searchBounds = new Rectangle();

	/**
	 * �������� ������ ����������� �����������.
	 * @return ������ ����������� �����������
	 */
	public Rectangle getDefaultBounds()
	{
		return AbstractNodeController.DEFAULT_BOUNDS;
	}
	
	/**
	 * �������� ����������� ������ ��������.
	 * @return ����������� ������ ��������
	 */
	public Rectangle getMinBounds()
	{
		return AbstractNodeController.MIN_BOUNDS;
	}
	
	/**
	 * �������� ������������ ������ ��������.
	 * @return ������������ ������ ��������
	 */
	public Rectangle getMaxBounds()
	{
		return AbstractNodeController.MAX_BOUNDS;
	}

	/**
	 * ���������� ����������� ��������������� �����������.
	 * ��� ���� ����������� ������� ��������.
	 * @param mapElement ������� �����
	 */
	public void updateScaleCoefficient(MapElement mapElement)
	{
		if(!(mapElement instanceof AbstractNode))
		{
			return;
		}
		AbstractNode node = (AbstractNode )mapElement;

		double scaleCoefficient = getLogicalNetLayer().getDefaultScale() 
				/ getLogicalNetLayer().getCurrentScale();

		int w = (int )(getDefaultBounds().getWidth() * scaleCoefficient);
		int h = (int )(getDefaultBounds().getHeight() * scaleCoefficient);
		
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
		this.setBounds(node, new Rectangle(w, h));

		MapPropertiesManager.setScaledImageSize(node.getImageId(), w, h);
	}
	
	/**
	 * ��������� ������� ��������. ��������� ����� ������ ��������������
	 * �������� ����� ��� ����������� �����, ������� �������� �
	 * ���-�������.
	 * @param node ����
	 * @return ������� �����������
	 */
	public Rectangle getBounds(AbstractNode node)
	{
		Rectangle rect = (Rectangle )boundsContainer.get(node);
		if(rect == null)
		{
			rect = new Rectangle(DEFAULT_BOUNDS);
			boundsContainer.put(node, rect);
		}
		return rect;
	}

	/**
	 * ���������� ������� ��������.
	 * @param node ����
	 * @param rect ������� �����������
	 */
	public void setBounds(AbstractNode node, Rectangle rect)
	{
		boundsContainer.put(node, rect);
	}

	/**
	 * �������� ����������� ��������.
	 * @param node ����
	 * @return �����������
	 */
	public Image getImage(AbstractNode node)
	{
		return MapPropertiesManager.getScaledImage(node.getImageId());
	}
	
	/**
	 * �������� ����������� �������� ��� ������� ������� �������.
	 * @param node ����
	 * @return �����������
	 */
	public Image getAlarmedImage(AbstractNode node)
	{
		return getImage(node);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(MapElement mapElement, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		if(!(mapElement instanceof AbstractNode))
		{
			return false;
		}
		AbstractNode node = (AbstractNode)mapElement;
		anchorContainer.setLocation(node.getLocation().getX(), node.getLocation().getY());
		return visibleBounds.contains(anchorContainer);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		if(!(mapElement instanceof AbstractNode))
		{
			return;
		}
		AbstractNode node = (AbstractNode )mapElement;

		if(!isElementVisible(node, visibleBounds))
		{
			return;
		}
		
		MapCoordinatesConverter converter = getLogicalNetLayer();
		
		Point p = converter.convertMapToScreen(node.getLocation());

		Graphics2D pg = (Graphics2D )g;
		
		int width = getBounds(node).width;
		int height = getBounds(node).height;

		pg.drawImage(
				getImage(node),
                p.x - width / 2,
                p.y - height / 2,
                null);

		// ���� �� �������� ���� ������ �������, �� ��������
		// � ����������� �� ����� getShowAlarmed()
		if (node.getAlarmState())
		{
			if ( MapPropertiesManager.isShowAlarmState() )
			{
				pg.drawImage(
						getAlarmedImage(node),
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
		if (node.isSelected())
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

	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseOnElement(MapElement mapElement, Point currentMousePoint)
		throws MapConnectionException, MapDataException
	{
		if(!(mapElement instanceof AbstractNode))
		{
			return false;
		}

		AbstractNode node = (AbstractNode )mapElement;

		MapCoordinatesConverter converter = getLogicalNetLayer();
		
		//�������� ���� ��� ������ ���������� � ��������������
		int width = (int )getBounds(node).getWidth();
		int height = (int )getBounds(node).getHeight();
		Point p = converter.convertMapToScreen(node.getLocation());
		searchBounds.setBounds(
				p.x - width / 2, 
				p.y - height / 2, 
				width, 
				height);
		if (searchBounds.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement)
	{
		if(!(mapElement instanceof AbstractNode))
		{
			return null;
		}

		AbstractNode node = (AbstractNode )mapElement;

		String s1 = node.getName();

		return s1 + " [" + MapViewController.getMapElementReadableType(node) + "]";
	}

}
