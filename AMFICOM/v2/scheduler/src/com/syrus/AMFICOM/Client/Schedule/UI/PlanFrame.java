package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;

public class PlanFrame extends JInternalFrame
{
	ApplicationContext aContext;
	PlanToolBar toolBar;
	PlanPanel mainPanel;

	public PlanFrame(ApplicationContext aContext)
	{
		this.aContext = aContext;

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		toolBar.apply_changes();
	}
	
	private void jbInit() throws Exception
	{
		setTitle("План-график");
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setDoubleBuffered(true);

		JScrollPane scroll = new JScrollPane();


//		PlanLayeredPanel panel = new PlanLayeredPanel();
		mainPanel = new PlanPanel(scroll, aContext);
		toolBar = mainPanel.getToolBar();
//		panel.setGraphPanel(mainPanel);
		scroll.getViewport().add(mainPanel);

		getContentPane().setBackground(SystemColor.window);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(scroll, BorderLayout.CENTER);
	}


}