package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.SystemColor;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.LazyAdjustmentListener;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

@SuppressWarnings("serial")
public final class PlanFrame extends JInternalFrame {

	private JToolBar	toolBar;
	private PlanPanel	mainPanel;

	public PlanFrame(ApplicationContext aContext) {
		super.setTitle(I18N.getString("Scheduler.Text.Plan.Title")); //$NON-NLS-1$
		super.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		super.setResizable(true);
		super.setClosable(false);
		super.setMaximizable(false);
		super.setIconifiable(true);
		super.setDoubleBuffered(true);

		final JScrollPane scroll = new JScrollPane();

		this.mainPanel = new PlanPanel(scroll, aContext);
		this.toolBar = this.mainPanel.getToolBar().getToolBar();
		scroll.getViewport().add(this.mainPanel);
		scroll.setAutoscrolls(true);

		final AdjustmentListener adjustmentListener = new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				mainPanel.updateVisibleRectOfTestLines();				
			}
		};
		scroll.getHorizontalScrollBar().addAdjustmentListener(new LazyAdjustmentListener(adjustmentListener, 100));
		
		final Container contentPane = getContentPane();
		contentPane.setBackground(SystemColor.window);
		contentPane.setLayout(new BorderLayout());
		contentPane.add(this.toolBar, BorderLayout.NORTH);
		contentPane.add(scroll, BorderLayout.CENTER);
	}

	
	public PlanPanel getPlanPanel(){
		return this.mainPanel;
	}
}
