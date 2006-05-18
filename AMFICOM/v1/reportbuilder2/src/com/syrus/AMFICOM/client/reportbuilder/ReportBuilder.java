/*
 * $Id: ReportBuilder.java,v 1.2 2006/04/11 05:58:32 stas Exp $
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
	
	ApplicationContext aContext = new ApplicationContext();

	public ReportBuilder() {
		super(APPLICATION_NAME);
	}
	
	@Override
	protected void init() {
		super.aContext.setApplicationModel(new ReportBuilderApplicationModelFactory().create());
		super.startMainFrame(new ReportBuilderMainFrame(super.aContext), (Image)UIManager.get(ReportBuilderResourceKeys.ICON_REPORTBUILDER_MAIN));
	}

	public static void main(String[] args) {
		new ReportBuilder();
	}
}
