/**
 * $Id: TopologicalNodeController.java,v 1.19 2005/06/23 08:27:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * Контроллер топологического узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/06/23 08:27:19 $
 * @module mapviewclient_v1
 */
public class TopologicalNodeController extends AbstractNodeController {
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
	 * Private constructor.
	 */
	private TopologicalNodeController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new TopologicalNodeController(netMapViewer);
	}

	public static void init(Identifier creatorId) throws ApplicationException {
		if(needInit) {

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
	}

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getDefaultBounds() {
		return TopologicalNodeController.NODE_BOUNDS;
	}

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getMinBounds() {
		return TopologicalNodeController.MIN_NODE_BOUNDS;
	}

	/**
	 * {@inheritDoc}
	 */
	public Rectangle getMaxBounds() {
		return TopologicalNodeController.MAX_NODE_BOUNDS;
	}

	/**
	 * Eстановить активность топологического узла. Помимо изменения флага меняет
	 * пиктограмму в соответствии с активностью узла.
	 * 
	 * @param node топологического узла
	 * @param active флаг активности
	 */
	public void setActive(TopologicalNode node, boolean active) {
		node.setActive(active);

		if(active)
			node.setImageId(openImageId);
		else
			node.setImageId(closedImageId);
	}

	public Identifier getImageId(AbstractNode node) {
		if(node.getImageId() == null) {
			TopologicalNode topologicalNode = (TopologicalNode )node;

			if(topologicalNode.isActive())
				topologicalNode.setImageId(openImageId);
			else
				topologicalNode.setImageId(closedImageId);
		}
		return node.getImageId();
	}

	/**
	 * {@inheritDoc}
	 */
	public Image getImage(AbstractNode node) {
		TopologicalNode topologicalNode = (TopologicalNode )node;

		if(topologicalNode.isActive())
			node.setImageId(openImageId);
		else
			node.setImageId(closedImageId);

		if(topologicalNode.isActive())
			return MapPropertiesManager.getScaledImage(closedImageId);

		return MapPropertiesManager.getScaledImage(openImageId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof TopologicalNode))
			return;
		TopologicalNode node = (TopologicalNode)mapElement;

		if(!isElementVisible(node, visibleBounds))
			return;
		
		super.paint(node, g, visibleBounds);

		if(node.isCanBind()) {
			MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
			
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
