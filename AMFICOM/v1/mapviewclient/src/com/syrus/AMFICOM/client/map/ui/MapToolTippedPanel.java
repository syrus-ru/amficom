/**
 * $Id: MapToolTippedPanel.java,v 1.12 2005/09/04 13:50:06 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;

/**
 * Класс используется для отображения всплывающих подсказок. Для этого
 * поверх NetMapViewer кладется объект MapToolTippedPanel, задача которого
 * только генерировать текст всплывающей подсказки и имитировать работу с 
 * NetMapViewer (прозрачно передавать сообщения мыши родительскому объекту 
 * NetMapViewer). Объект класса MapToolTippedPanel прозрачен и не видим для
 * пользователя
 * @version $Revision: 1.12 $, $Date: 2005/09/04 13:50:06 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapToolTippedPanel extends JComponent {
	NetMapViewer parent;
	JComponent component;
	public MapToolTippedPanelListener toolTippedPanelListener;

	public MapToolTippedPanel(NetMapViewer parent) {
		super();
		this.parent = parent;
		this.component = parent.getVisualComponent();
		this.toolTippedPanelListener = new MapToolTippedPanelListener(this);
	}

	@Override
	public int getWidth() {
		return this.component.getWidth();
	}

	@Override
	public int getHeight() {
		return this.component.getHeight();
	}

	@Override
	public boolean isShowing() {
		return this.component.isShowing();
	}

	@Override
	public Point getLocationOnScreen() {
		return this.component.getLocationOnScreen();
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration() {
		return this.component.getGraphicsConfiguration();
	}

	@Override
	public Container getTopLevelAncestor() {
		return this.component.getParent();
	}

	@Override
	public Container getParent() {
		return this.component.getParent();
	}

	@Override
	public JToolTip createToolTip() {
        JToolTip tip = new MapToolTip();
        tip.setComponent(this.component);
        return tip;
    }

	@Override
	public String getToolTipText() 	{
		LogicalNetLayer logicalNetLayer = this.parent.getLogicalNetLayer();
		if(logicalNetLayer == null)
			return "";
		MapState mapState = logicalNetLayer.getMapState();
		if(mapState.getOperationMode() != MapState.NO_OPERATION)
			return "";
		if(mapState.getMouseMode() != MapState.MOUSE_NONE)
			return "";
		if(mapState.getActionMode() != MapState.NULL_ACTION_MODE)
			return "";
		try {
			MapElement me = logicalNetLayer.getMapElementAtPoint(
					logicalNetLayer.getCurrentPoint(),
					this.parent.getVisibleBounds());
			return logicalNetLayer.getMapViewController().getController(me).getToolTipText(me);
		} 
		catch (Exception ex) {
			return "";
		} 
	}
}

/**
 * Класс MapToolTippedPanelListener используется для прозрачной передачи 
 * возникающих событий мыши родительскому объекту
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/09/04 13:50:06 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
class MapToolTippedPanelListener implements MouseListener, MouseMotionListener {
	JComponent parent;

	public MapToolTippedPanelListener(JComponent parent) {
		this.parent = parent;
	}

	public void mouseClicked(MouseEvent e) {
		this.parent.dispatchEvent(
			new MouseEvent(
				this.parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mousePressed(MouseEvent e) {
		this.parent.dispatchEvent(
			new MouseEvent(
				this.parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseReleased(MouseEvent e) {
		this.parent.dispatchEvent(
			new MouseEvent(
				this.parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseEntered(MouseEvent e) {
		this.parent.dispatchEvent(
			new MouseEvent(
				this.parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseExited(MouseEvent e) {
		this.parent.dispatchEvent(
			new MouseEvent(
				this.parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseDragged(MouseEvent e) {
		this.parent.dispatchEvent(
			new MouseEvent(
				this.parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseMoved(MouseEvent e) {
		this.parent.dispatchEvent(
			new MouseEvent(
				this.parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}
}

