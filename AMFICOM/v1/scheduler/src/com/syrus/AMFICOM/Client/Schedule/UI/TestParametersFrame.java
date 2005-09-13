
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

@SuppressWarnings("serial")
public class TestParametersFrame extends JInternalFrame {
	
	private TestParametersPanel	panel;

	public TestParametersFrame(final ApplicationContext aContext) {
		super.setTitle(LangModelSchedule.getString("Text.MeasurementParameter.Title")); //$NON-NLS-1$
		super.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		super.setResizable(true);
		super.setClosable(false);
		super.setIconifiable(true);

		this.panel = new TestParametersPanel(aContext);
		this.getContentPane().add(this.panel.getComponent(), BorderLayout.CENTER);
	}

}
