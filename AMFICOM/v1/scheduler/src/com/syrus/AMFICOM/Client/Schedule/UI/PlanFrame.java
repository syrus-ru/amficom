package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class PlanFrame extends JInternalFrame {

	private static final long	serialVersionUID	= 3761969371601776689L;
	private JToolBar	toolBar;
	private PlanPanel	mainPanel;

	public PlanFrame(ApplicationContext aContext) {
		setTitle(LangModelSchedule.getString("Tests time table")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setDoubleBuffered(true);

		JScrollPane scroll = new JScrollPane();

		this.mainPanel = new PlanPanel(scroll, aContext);
		this.toolBar = this.mainPanel.getToolBar().getToolBar();
		scroll.getViewport().add(this.mainPanel);
		scroll.setAutoscrolls(true);

		getContentPane().setBackground(SystemColor.window);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.toolBar, BorderLayout.NORTH);
		getContentPane().add(scroll, BorderLayout.CENTER);
	}

	
	public PlanPanel getPlanPanel(){
		return this.mainPanel;
	}
}
