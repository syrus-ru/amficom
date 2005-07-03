package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class TestRequestFrame extends JInternalFrame {

	private static final long	serialVersionUID	= 3256445815399788853L;
	
	private TestRequestPanel	panel;

	public TestRequestFrame(ApplicationContext aContext) {
		//this.aContext = aContext;
		setTitle(LangModelSchedule.getString("TestOptions")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);

		this.panel = new TestRequestPanel(aContext);
		this.getContentPane().add(this.panel, BorderLayout.CENTER);

	}	
}
