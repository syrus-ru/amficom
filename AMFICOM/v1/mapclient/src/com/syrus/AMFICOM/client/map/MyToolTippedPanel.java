package com.syrus.AMFICOM.Client.Configure.Map;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

import com.syrus.AMFICOM.Client.General.UI.*;

public class MyToolTippedPanel extends JComponent
{
	NetMapViewer parent;
	public MyListener ls;

	public MyToolTippedPanel(NetMapViewer parent)
	{
		super();
		this.parent = parent;
		ls = new MyListener(this);
	}

	public int getWidth()
	{
		return parent.getWidth();
	}

	public int getHeight()
	{
		return parent.getHeight();
	}
	public boolean isShowing()
	{
		return parent.isShowing();
	}

	public Point getLocationOnScreen()
	{
		return parent.getLocationOnScreen();
	}

	public GraphicsConfiguration getGraphicsConfiguration()
	{
		return parent.getGraphicsConfiguration();
	}

	public Container getTopLevelAncestor()
	{
//		return super.getTopLevelAncestor()
		return parent.getParent();
	}

	public Container getParent()
	{
		return parent.getParent();
	}

    public JToolTip createToolTip() 
	{
//		System.out.println("create tooltip");
//        JToolTip tip = new JToolTip();
//		tip.setUI(new MultiRowToolTipUI());
        JToolTip tip = new MyMapToolTip(parent.lnl);
        tip.setComponent(this);
//		System.out.println("tooltip created!");
        return tip;
    }

	public String getToolTipText()
	{
//		System.out.println("create tooltip");
		if(parent.lnl == null)
			return "";
		if(parent.lnl.mapState != LogicalNetLayer.NO_ACTION)
			return "";
		if(parent.lnl.mode != LogicalNetLayer.NULL_MODE)
			return "";
		if(parent.lnl.actionMode != LogicalNetLayer.NULL_ACTION_MODE)
			return "";
		MapContext mc = parent.lnl.getMapContext();
		try 
		{
			MapElement me = mc.getCurrentMapElement(parent.lnl.getCurrentPoint());
//			System.out.println("tooltip created! " + me.getToolTipText());
			return me.getToolTipText();
			
		} 
		catch (Exception ex) 
		{
//			System.out.println("TOOLTIP: " + ex.getMessage());
			return "";
		} 
	}
	
}

class MyListener implements MouseListener, MouseMotionListener
{
	JComponent parent;
	
	public MyListener(JComponent parent)
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
//				e.getButton()));
//		e.setSource(parent);
//		parent.dispatchEvent(e);
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
//				e.getButton()));
//		e.setSource(parent);
//		parent.dispatchEvent(e);
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
//				e.getButton()));
//		e.setSource(parent);
//		parent.dispatchEvent(e);
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
//				e.getButton()));
//		e.setSource(parent);
//		parent.dispatchEvent(e);
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
//		e.setSource(parent);
//		parent.dispatchEvent(e);
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
//		e.setSource(parent);
//		parent.dispatchEvent(e);
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
//		e.setSource(parent);
//		parent.dispatchEvent(e);
	}
}

