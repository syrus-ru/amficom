/**
 * $Id: TopologicalNodeController.java,v 1.8 2005/02/03 16:24:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

/**
 * Контроллер топологического узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/02/03 16:24:01 $
 * @module mapviewclient_v1
 */
public class TopologicalNodeController extends AbstractNodeController
{
	/** Размер пиктограммы поумолчанию. */
	public static final Rectangle NODE_BOUNDS = new Rectangle(10, 10);
	/** Минимальный размер элемента. */
	public static final Rectangle MIN_NODE_BOUNDS = new Rectangle(2, 2);
	/** Максимальный размер элемента. */
	public static final Rectangle MAX_NODE_BOUNDS = new Rectangle(15, 15);

	/** Имя пиктограммы закрытого топологического узла. */
	public static final String CLOSED_NODE = "node";
	/** Имя пиктограммы открытоготопологического узла. */
	public static final String OPEN_NODE = "void";

	/** Пиктограмма закрытого топологического узла. */
	public static final String CLOSED_NODE_IMAGE = "images/node.gif";
	/** Пиктограмма открытого топологического узла. */
	public static final String OPEN_NODE_IMAGE = "images/void.gif";

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	/**
	 * Флаг необходимости инициализировать изображения маркеров событий.
	 * Инициализация проводится один раз при первом обращении к отрисовке 
	 * маркера.
	 */
	private static boolean needInit = true;
	
	/** Идентификатор пиктограммы закрытого топологического узла. */
	private static Identifier openImageId;
	/** Идентификатор пиктограммы открытоготопологического узла. */
	private static Identifier closedImageId;

	/**
	 * Instace.
	 */
	private static TopologicalNodeController instance = null;

	/**
	 * Получить имя класса панели, описывающей свойства кабельного пути.
	 * @return имя класса
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	/**
	 * Private constructor.
	 */
	private TopologicalNodeController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new TopologicalNodeController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getDefaultBounds()
	{
		return TopologicalNodeController.NODE_BOUNDS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Rectangle getMinBounds()
	{
		return TopologicalNodeController.MIN_NODE_BOUNDS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Rectangle getMaxBounds()
	{
		return TopologicalNodeController.MAX_NODE_BOUNDS;
	}

	/**
	 * Eстановить активность топологического узла. Помимо изменения флага
	 * меняет пиктограмму в соответствии с активностью узла.
	 * @param node топологического узла
	 * @param active флаг активности
	 */
	public void setActive(TopologicalNode node, boolean active)
	{
		node.setActive(active);

		if(needInit)
		{
			Identifier creatorId = getLogicalNetLayer().getUserId();

			openImageId = NodeTypeController.getImageId(
					creatorId, 
					TopologicalNodeController.OPEN_NODE, 
					TopologicalNodeController.OPEN_NODE_IMAGE);
			closedImageId = NodeTypeController.getImageId(
					creatorId, 
					TopologicalNodeController.CLOSED_NODE, 
					TopologicalNodeController.CLOSED_NODE_IMAGE);

			MapPropertiesManager.setOriginalImage(
				openImageId,
				new ImageIcon(TopologicalNodeController.OPEN_NODE_IMAGE).getImage());
			MapPropertiesManager.setOriginalImage(
				closedImageId,
				new ImageIcon(TopologicalNodeController.CLOSED_NODE_IMAGE).getImage());
				
			needInit = false;
		}

		if(active)
			node.setImageId(openImageId);
		else
			node.setImageId(closedImageId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Image getImage(AbstractNode node)
	{
		if(needInit)
		{
			Identifier creatorId = 
				((RISDSessionInfo )(getLogicalNetLayer().getContext().getSessionInterface())).getUserIdentifier();

			openImageId = NodeTypeController.getImageId(creatorId, OPEN_NODE, OPEN_NODE_IMAGE);
			closedImageId = NodeTypeController.getImageId(creatorId, CLOSED_NODE, CLOSED_NODE_IMAGE);

			MapPropertiesManager.setOriginalImage(
				openImageId,
				new ImageIcon(OPEN_NODE_IMAGE).getImage());
			MapPropertiesManager.setOriginalImage(
				closedImageId,
				new ImageIcon(CLOSED_NODE_IMAGE).getImage());
				
			needInit = false;
		}

		TopologicalNode topologicalNode = (TopologicalNode )node;
		if(topologicalNode.isActive())
			return MapPropertiesManager.getScaledImage(closedImageId);

		return MapPropertiesManager.getScaledImage(openImageId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(mapElement instanceof TopologicalNode))
			return;
		TopologicalNode node = (TopologicalNode)mapElement;

		if(!isElementVisible(node, visibleBounds))
			return;
		
		super.paint(node, g, visibleBounds);

		if (node.isCanBind())
		{
			MapCoordinatesConverter converter = getLogicalNetLayer();
			
			Point p = converter.convertMapToScreen(node.getLocation());
	
			Graphics2D pg = (Graphics2D )g;
			
			int width = getBounds(node).width + 20;
			int height = getBounds(node).height + 20;
			
			pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
			pg.setColor(MapPropertiesManager.getCanBindColor());
			pg.drawRect( 
					p.x - width / 2,
					p.y - height / 2,
					width,
					height);
		}
	}
}
