package com.syrus.AMFICOM.Client.Map;

import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.*;

public class MapNodeLinkSizeField extends JTextField 
{
	LogicalNetLayer lnl;
	MapNodeLinkElement nodelink;
	MapNodeElement node;
	
	public MapNodeLinkSizeField(
			LogicalNetLayer lnl,
			MapNodeLinkElement nodelink,
			MapNodeElement node)
	{
		super();
		this.lnl = lnl;
		this.nodelink = nodelink;
		this.node = node;
		jbInit();
	}

	public void jbInit()
	{
		this.setFont(nodelink.getFont());

		addKeyListener(new MapNodeLinkSizeField_KeyAdapter(this));
		addFocusListener(new MapNodeLinkSizeField_FocusAdapter(this));
	}

	class MapNodeLinkSizeField_FocusAdapter extends FocusAdapter
	{
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeField_FocusAdapter(MapNodeLinkSizeField adaptee)
		{
			this.adaptee = adaptee;
		}

		public void focusGained(FocusEvent e)
		{
		}
		
		public void focusLost(FocusEvent e)
		{
			adaptee.setVisible(false);
			if(adaptee.getParent() != null)
				adaptee.getParent().remove(adaptee);
			adaptee.removeFocusListener(this);
		}
	}
	
	class MapNodeLinkSizeField_KeyAdapter extends java.awt.event.KeyAdapter
	{
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeField_KeyAdapter(MapNodeLinkSizeField adaptee)
		{
			this.adaptee = adaptee;
		}

		public void keyPressed(KeyEvent e) 
		{
			int code = e.getKeyCode();

			if (code == KeyEvent.VK_ESCAPE)
			{
				adaptee.setVisible(false);
				if(adaptee.getParent() != null)
					adaptee.getParent().remove(adaptee);
				adaptee.removeKeyListener(this);
			}

			if (code == KeyEvent.VK_ENTER)
			{
				try
				{
					double dist = Double.parseDouble(adaptee.getText());
					adaptee.nodelink.setSizeFrom(adaptee.node, dist);
					adaptee.setVisible(false);
					if(adaptee.getParent() != null)
						adaptee.getParent().remove(adaptee);
					adaptee.removeKeyListener(this);

					if(adaptee.lnl != null)
					{
						adaptee.lnl.postDirtyEvent();
						adaptee.lnl.postPaintEvent();
					}
				}
				catch(Exception ex)
				{
					System.out.println("Illegal distance");
				}
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
	
}