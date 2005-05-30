/**
 * $Id: MapToolTippedPanel.java,v 1.6 2005/05/30 12:19:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;

/**
 * Класс используется для отображения всплывающих подсказок. Для этого
 * поверх NetMapViewer кладется объект MapToolTippedPanel, задача которого
 * только генерировать текст всплывающей подсказки и имитировать работу с 
 * NetMapViewer (прозрачно передавать сообщения мыши родительскому объекту 
 * NetMapViewer). Объект класса MapToolTippedPanel прозрачен и не видим для
 * пользователя
 * @version $Revision: 1.6 $, $Date: 2005/05/30 12:19:02 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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

	public int getWidth() {
		return this.component.getWidth();
	}

	public int getHeight() {
		return this.component.getHeight();
	}

	public boolean isShowing() {
		return this.component.isShowing();
	}

	public Point getLocationOnScreen() {
		return this.component.getLocationOnScreen();
	}

	public GraphicsConfiguration getGraphicsConfiguration() {
		return this.component.getGraphicsConfiguration();
	}

	public Container getTopLevelAncestor() {
		return this.component.getParent();
	}

	public Container getParent() {
		return this.component.getParent();
	}

	public JToolTip createToolTip() {
// System.out.println("create tooltip");
//        JToolTip tip = new MapToolTip(parent.getLogicalNetLayer());
        JToolTip tip = new MapToolTip();
        tip.setComponent(this.component);
//		System.out.println("tooltip created!");
        return tip;
    }

	public String getToolTipText() 	{
//		System.out.println("create tooltip");
		if(this.parent.getLogicalNetLayer() == null)
			return "";
		MapState mapState = this.parent.getLogicalNetLayer().getMapState();
		if(mapState.getOperationMode() != MapState.NO_OPERATION)
			return "";
		if(mapState.getMouseMode() != MapState.MOUSE_NONE)
			return "";
		if(mapState.getActionMode() != MapState.NULL_ACTION_MODE)
			return "";
		try {
			MapElement me = this.parent.getLogicalNetLayer().getMapElementAtPoint(
					this.parent.getLogicalNetLayer().getCurrentPoint());
//			System.out.println("tooltip created! " + me.getToolTipText());
			return this.parent.getLogicalNetLayer().getMapViewController().getController(me).getToolTipText(me);
		} 
		catch (Exception ex) {
//			System.out.println("TOOLTIP: " + ex.getMessage());
			return "";
		} 
	}
}

/**
 * Класс MapToolTippedPanelListener используется для прозрачной передачи 
 * возникающих событий мыши родительскому объекту
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2005/05/30 12:19:02 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
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

