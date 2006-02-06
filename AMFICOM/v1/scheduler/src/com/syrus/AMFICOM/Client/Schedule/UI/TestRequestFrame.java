package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

@SuppressWarnings("serial")
public final class TestRequestFrame extends JInternalFrame {

	private TestRequestPanel	panel;

	public TestRequestFrame(final ApplicationContext aContext) {
		super.setTitle(I18N.getString("Scheduler.Text.TestOptions.Title")); //$NON-NLS-1$
		super.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		super.setResizable(true);
		super.setClosable(false);
		super.setIconifiable(true);

		this.panel = new TestRequestPanel(aContext);
		this.getContentPane().add(this.panel, BorderLayout.CENTER);

	}	
}
