package com.syrus.AMFICOM.Client.General.Command;

import java.awt.Dimension;
import java.awt.Toolkit;


import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Report.SelectTypeTemplateWindow;

public class OpenTypedTemplateCommand extends VoidCommand
{
	public ApplicationContext aContext;
	public String templateType;
	public Object reportData;

	public SelectTypeTemplateWindow frame;

	public OpenTypedTemplateCommand()
	{
	}

	public OpenTypedTemplateCommand(
			ApplicationContext aContext,
			String templateType,
			Object reportData)
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
		DataSourceInterface dsi = aContext.getDataSourceInterface();
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
