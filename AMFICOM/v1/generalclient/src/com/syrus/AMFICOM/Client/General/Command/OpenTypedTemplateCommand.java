/*
 * $Id: OpenTypedTemplateCommand.java,v 1.7 2005/05/05 11:04:46 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.SelectTypeTemplateWindow;

/**
 * @author $Author: bob $
 * @version $Revision: 1.7 $, $Date: 2005/05/05 11:04:46 $
 * @module generalclient_v1
 */
public class OpenTypedTemplateCommand extends VoidCommand
{
	public ApplicationContext aContext;
	public String templateType;
	public AMTReport reportData;

	public SelectTypeTemplateWindow frame;

	public OpenTypedTemplateCommand()
	{
	}

	public OpenTypedTemplateCommand(
			ApplicationContext aContext,
			String templateType,
			AMTReport reportData)
	{
		this.aContext = aContext;
		this.templateType = templateType;
		this.reportData = reportData;
	}

	public Object clone()
	{
		return new OpenTypedTemplateCommand(aContext,templateType,reportData);
	}

	public void execute()
	{
		DataSourceInterface dsi = aContext.getDataSource();
		if (dsi == null)
			return;

		{
			frame =
					new SelectTypeTemplateWindow(aContext,templateType,reportData);
			frame.setModal(true);

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation((int)(dim.getWidth() - frame.getWidth()) / 2,
												(int)(dim.getHeight() - frame.getHeight()) / 2);
		}

		frame.setVisible(true);
		frame.repaint();
	}
}
