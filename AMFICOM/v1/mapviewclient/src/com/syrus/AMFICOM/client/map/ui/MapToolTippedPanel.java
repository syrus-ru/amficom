/**
 * $Id: MapToolTippedPanel.java,v 1.3 2004/12/22 16:38:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JToolTip;

/**
 * Класс используется для отображения всплывающих подсказок. Для этого
 * поверх NetMapViewer кладется объект MapToolTippedPanel, задача которого
 * только генерировать текст всплывающей подсказки и имитировать работу с 
 * NetMapViewer (прозрачно передавать сообщения мыши родительскому объекту 
 * NetMapViewer). Объект класса MapToolTippedPanel прозрачен и не видим для
 * пользователя
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/22 16:38:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapToolTippedPanel extends JComponent
{
	NetMapViewer parent;
	JComponent component;
	public MapToolTippedPanelListener ls;

	public MapToolTippedPanel(NetMapViewer parent)
	{
		super();
		this.parent = parent;
		this.component = parent.getVisualComponent();
		ls = new MapToolTippedPanelListener(this);
	}

	public int getWidth()
	{
		return component.getWidth();
	}

	public int getHeight()
	{
		return component.getHeight();
	}
	public boolean isShowing()
	{
		return component.isShowing();
	}

	public Point getLocationOnScreen()
	{
		return component.getLocationOnScreen();
	}

	public GraphicsConfiguration getGraphicsConfiguration()
	{
		return component.getGraphicsConfiguration();
	}

	public Container getTopLevelAncestor()
	{
		return component.getParent();
	}

	public Container getParent()
	{
		return component.getParent();
	}

    public JToolTip createToolTip() 
	{
//		System.out.println("create tooltip");
//        JToolTip tip = new MapToolTip(parent.getLogicalNetLayer());
        JToolTip tip = new MapToolTip();
        tip.setComponent(component);
//		System.out.println("tooltip created!");
        return tip;
    }

	public String getToolTipText()
	{
//		System.out.println("create tooltip");
		if(parent.getLogicalNetLayer() == null)
			return "";
		MapState mapState = parent.getLogicalNetLayer().getMapState();
		if(mapState.getOperationMode() != MapState.NO_OPERATION)
			return "";
		if(mapState.getMouseMode() != MapState.MOUSE_NONE)
			return "";
		if(mapState.getActionMode() != MapState.NULL_ACTION_MODE)
			return "";
		try 
		{
			MapElement me = parent.getLogicalNetLayer().getMapElementAtPoint(
					parent.getLogicalNetLayer().getCurrentPoint());
//			System.out.println("tooltip created! " + me.getToolTipText());
			return parent.getLogicalNetLayer().getMapViewController().getController(me).getToolTipText(me);
		} 
		catch (Exception ex) 
		{
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
 * @version $Revision: 1.3 $, $Date: 2004/12/22 16:38:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
class MapToolTippedPanelListener implements MouseListener, MouseMotionListener
{
	JComponent parent;
	
	public MapToolTippedPanelListener(JComponent parent)
	{
		this.parent = parent;
	}

	public void mouseClicked(MouseEvent e)
	{
		parent.dispatchEvent(
			new MouseEvent(
				parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mousePressed(MouseEvent e)
	{
		parent.dispatchEvent(
			new MouseEvent(
				parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseReleased(MouseEvent e)
	{
		parent.dispatchEvent(
			new MouseEvent(
				parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseEntered(MouseEvent e)
	{
		parent.dispatchEvent(
			new MouseEvent(
				parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseExited(MouseEvent e)
	{
		parent.dispatchEvent(
			new MouseEvent(
				parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseDragged(MouseEvent e)
	{
		parent.dispatchEvent(
			new MouseEvent(
				parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}

	public void mouseMoved(MouseEvent e)
	{
		parent.dispatchEvent(
			new MouseEvent(
				parent, 
				e.getID(), 
				e.getWhen(), 
				e.getModifiers(), 
				e.getX(), 
				e.getY(),
				e.getClickCount(), 
				e.isPopupTrigger()));
	}
}

