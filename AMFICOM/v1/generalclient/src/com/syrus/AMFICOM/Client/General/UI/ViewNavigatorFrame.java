package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import javax.swing.*;

import javax.swing.JInternalFrame;

public class ViewNavigatorFrame extends JInternalFrame
{
	JScrollPane scroll = new JScrollPane();
	UniTreePanel panel = null;
	
	public ViewNavigatorFrame()
	{
		super();
		jbInit();
	}

	public ViewNavigatorFrame(String title)
	{
		super(title);
		jbInit();
	}
	
	public ViewNavigatorFrame(UniTreePanel utp)
	{
		super();
		jbInit();
		setPanel(utp);
	}

	public ViewNavigatorFrame(String title, UniTreePanel utp)
	{
		super(title);
		jbInit();
		setPanel(utp);
	}

	public void setPanel(UniTreePanel utp)
	{
		if(panel != null)
			scroll.getViewport().remove(panel);
		panel = utp;
		if(panel != null)
			scroll.getViewport().add(panel);
	}

	public Component getPanel()
	{
		return scroll;
	}

	public void jbInit()
	{
			setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
//			frame.setTitle(title);
			setSize(new Dimension (300, 600));
			setClosable(true);
			setResizable(true);
			setMaximizable(true);
			setIconifiable(true);
			getContentPane().setLayout(new BorderLayout());

			scroll.setWheelScrollingEnabled(true);
			scroll.setAutoscrolls(true);
			getContentPane().add(scroll, BorderLayout.CENTER);
	}

	public void doDefaultCloseAction()
	{
		if (isMaximum())
		try
		{
			setMaximum(false);
		}
		catch (java.beans.PropertyVetoException ex)
		{
			ex.printStackTrace();
		}
		super.doDefaultCloseAction();
    }
}
