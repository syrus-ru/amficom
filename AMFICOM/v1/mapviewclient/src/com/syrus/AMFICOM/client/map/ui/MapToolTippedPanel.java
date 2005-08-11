/**
 * $Id: MapToolTippedPanel.java,v 1.10 2005/08/11 12:43:32 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
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
 * ����� ������������ ��� ����������� ����������� ���������. ��� �����
 * ������ NetMapViewer �������� ������ MapToolTippedPanel, ������ ��������
 * ������ ������������ ����� ����������� ��������� � ����������� ������ � 
 * NetMapViewer (��������� ���������� ��������� ���� ������������� ������� 
 * NetMapViewer). ������ ������ MapToolTippedPanel ��������� � �� ����� ���
 * ������������
 * @version $Revision: 1.10 $, $Date: 2005/08/11 12:43:32 $
 * @author $Author: arseniy $
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
//			System.out.println("tooltip created! " + me.getToolTipText());
			return logicalNetLayer.getMapViewController().getController(me).getToolTipText(me);
		} 
		catch (Exception ex) {
//			System.out.println("TOOLTIP: " + ex.getMessage());
			return "";
		} 
	}
}

/**
 * ����� MapToolTippedPanelListener ������������ ��� ���������� �������� 
 * ����������� ������� ���� ������������� �������
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2005/08/11 12:43:32 $
 * @author $Author: arseniy $
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

