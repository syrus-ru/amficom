/**
 * $Id: AbstractNodeController.java,v 1.2 2005/02/02 15:17:52 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.HashMap;

/**
 * Контроллер узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/02/02 15:17:52 $
 * @module mapviewclient_v1
 */
public abstract class AbstractNodeController implements MapElementController
{
	/** Размер пиктограммы поумолчанию. */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(14, 14);
	/** Минимальный размер элемента. */
	public static final Rectangle MIN_BOUNDS = new Rectangle(6, 6);
	/** Максимальный размер элемента. */
	public static final Rectangle MAX_BOUNDS = new Rectangle(40, 40);
	/** Коэффициент масштабирования пиктограммы по умолчанию. */
	public static final double DEFAULT_SCALE_COEFFICIENT = 1.0;
	/** Пиктограмма по умолчанию. */	
	public static final String DEFAULT_IMAGE = "images/pc.gif";

	/**
	 * Логический слой.
	 */
	protected LogicalNetLayer logcalNetLayer;

	/** Размер пиктограммы в экранных координатах. */
	protected static java.util.Map boundsContainer = new HashMap();

	/**
	 * Временный объект кординат центра эемента. Используется для определения
	 * вхождения центра элемента в область.
	 */
	protected static Point2D.Double anchorContainer = new Point2D.Double(0.0D, 0.0D);

	/**
	 * Временный объект области поиска. Используется для определения
	 * вхождения центра элемента в видимую область.
	 */
	protected static Rectangle searchBounds = new Rectangle();

	/**
	 * {@inheritDoc}
	 */
	public void setLogicalNetLayer(LogicalNetLayer logcalNetLayer)
	{
		this.logcalNetLayer = logcalNetLayer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return logcalNetLayer;
	}

	/**
	 * Получить размер пиктограммы поумолчанию.
	 * @return размер пиктограммы поумолчанию
	 */
	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}
	
	/**
	 * Получить минимальный размер элемента.
	 * @return минимальный размер элемента
	 */
	public Rectangle getMinBounds()
	{
		return MIN_BOUNDS;
	}
	
	/**
	 * Получить максимальный размер элемента.
	 * @return максимальный размер элемента
	 */
	public Rectangle getMaxBounds()
	{
		return MAX_BOUNDS;
	}

	/**
	 * Установить коэффициент масштабирования пиктограммы.
	 * При этом обновляются границы элемента.
	 * @param mapElement элемент карты
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
	 * Пполучить границы элемента. Поскольку вызов метода осуществляется
	 * довольно часто при перерисовке карты, границы хранятся в
	 * хэш-таблице.
	 * @param node узел
	 * @return границы пиктограммы
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
	 * Установить границы элемента.
	 * @param node узел
	 * @param rect границы пиктограммы
	 */
	public void setBounds(AbstractNode node, Rectangle rect)
	{
		boundsContainer.put(node, rect);
	}

	/**
	 * Получить пиктограмму элемента.
	 * @param node узел
	 * @return пиктограмма
	 */
	public Image getImage(AbstractNode node)
	{
		return MapPropertiesManager.getScaledImage(node.getImageId());
	}
	
	/**
	 * Получить пиктограмму элемента при наличии сигнала тревоги.
	 * @param node узел
	 * @return пиктограмма
	 */
	public Image getAlarmedImage(AbstractNode node)
	{
		return getImage(node);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof AbstractNode))
		{
			return false;
		}
		AbstractNode node = (AbstractNode)me;
		anchorContainer.setLocation(node.getLocation().getX(), node.getLocation().getY());
		return visibleBounds.contains(anchorContainer);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof AbstractNode))
		{
			return;
		}
		AbstractNode node = (AbstractNode)me;

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

		// если на элементе есть сигнал тревоги, то анимация
		// в зависимости от флага getShowAlarmed()
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

		//Если выбрано то рисовать рамку
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
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(!(me instanceof AbstractNode))
		{
			return false;
		}

		AbstractNode node = (AbstractNode)me;

		MapCoordinatesConverter converter = getLogicalNetLayer();
		
		//Проверка того что курсор находиться в прямоугольнике
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
	public String getToolTipText(MapElement me)
	{
		if(!(me instanceof AbstractNode))
		{
			return null;
		}

		AbstractNode node = (AbstractNode )me;

		String s1 = node.getName();

		return s1 + " [" + LangModel.getString("node" + node.getClass()) + "]";
	}



}
