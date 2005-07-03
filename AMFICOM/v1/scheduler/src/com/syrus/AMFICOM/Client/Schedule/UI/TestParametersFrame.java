
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class TestParametersFrame extends JInternalFrame {

	private static final long	serialVersionUID	= 3257291331117134642L;
	
	private TestParametersPanel	panel;

	public TestParametersFrame(ApplicationContext aContext) {
		setTitle(LangModelSchedule.getString("Measurement_options")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		this.panel = new TestParametersPanel(aContext);
		this.getContentPane().add(this.panel.getComponent(), BorderLayout.CENTER);
	}

}
