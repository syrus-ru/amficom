/**
 * $Id: AbstractNodeController.java,v 1.3 2004/12/22 16:38:42 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * ������� ����� - ���� 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/22 16:38:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public abstract class AbstractNodeController implements MapElementController
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

	protected LogicalNetLayer lnl;

	/** ������ ����������� � �������� ����������� */
	protected static java.util.Map boundsContainer = new HashMap();

	protected static Point2D.Double anchorContainer = new Point2D.Double(0.0D, 0.0D);

	protected static Rectangle searchBounds = new Rectangle();

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}
	
	public LogicalNetLayer getLogicalNetLayer()
	{
		return lnl;
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
	public void updateScaleCoefficient(MapElement me)
	{
		if(!(me instanceof AbstractNode))
			return;
		AbstractNode node = (AbstractNode)me;

		double scaleCoefficient = getLogicalNetLayer().getDefaultScale() 
				/ getLogicalNetLayer().getCurrentScale();

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
		this.setBounds(node, new Rectangle(w, h));

		MapPropertiesManager.setScaledImageSize(node.getImageId(), w, h);
	}
	
	/**
	 * �������� ������� ��������
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
	 * ���������� ������� ��������
	 */
	public void setBounds(AbstractNode node, Rectangle rect)
	{
		boundsContainer.put(node, rect);
	}

	/**
	 * �������� ����������� ��������
	 */
	public Image getImage(AbstractNode node)
	{
		return MapPropertiesManager.getScaledImage(node.getImageId());
	}
	
	public Image getAlarmedImage(AbstractNode node)
	{
		return getImage(node);
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof AbstractNode))
			return false;
		AbstractNode node = (AbstractNode)me;
		anchorContainer.setLocation(node.getLocation().getX(), node.getLocation().getY());
		return visibleBounds.contains(anchorContainer);
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof AbstractNode))
			return;
		AbstractNode node = (AbstractNode)me;

		if(!isElementVisible(node, visibleBounds))
			return;
		
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

	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(!(me instanceof AbstractNode))
			return false;

		AbstractNode node = (AbstractNode)me;

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

	public String getToolTipText(MapElement me)
	{
		if(!(me instanceof AbstractNode))
			return null;

		AbstractNode node = (AbstractNode)me;

		String s1 = node.getName();

		return s1 + " [" + LangModel.getString("node" + node.getClass()) + "]";
	}



}
