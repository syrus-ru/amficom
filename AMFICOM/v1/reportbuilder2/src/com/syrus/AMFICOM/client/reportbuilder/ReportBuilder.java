/*
 * $Id: ReportBuilder.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class ReportBuilder extends AbstractApplication {
	public static final String APPLICATION_NAME = "report";
	private static final String ICON_SCHEMATICS = "images/main/report.gif";
	
	ApplicationContext aContext = new ApplicationContext();

	public ReportBuilder() {
		super(APPLICATION_NAME);
	}
//		SplashScreen screen = SplashScreen.getDefaultSplashScreen();
//		screen.setVisible(true);
//screen.dispose();
	
	@Override
	protected void init() {
		super.aContext.setApplicationModel(new ReportBuilderApplicationModelFactory().create());
		super.startMainFrame(new ReportBuilderMainFrame(super.aContext), (Image)UIManager.get(ICON_SCHEMATICS));
	}

	public static void main(String[] args) {
		new ReportBuilder();
	}
}
