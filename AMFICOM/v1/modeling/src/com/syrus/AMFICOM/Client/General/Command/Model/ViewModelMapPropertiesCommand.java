package com.syrus.AMFICOM.Client.General.Command.Model;

import java.awt.Dimension;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.Model.ModelMDIMain;

public class ViewModelMapPropertiesCommand  extends ViewMapPropertiesCommand
{
	JDesktopPane desktop;
	ModelMDIMain parent;
	public ViewModelMapPropertiesCommand()
	{
	}

	public ViewModelMapPropertiesCommand(ModelMDIMain parent, JDesktopPane desktop, ApplicationContext aContext)
	{
		super(desktop, aContext);
		this.desktop = desktop;
		this.parent = parent;
	}

	public void execute()
	{
		super.execute();

		Dimension dim = desktop.getSize();

		frame.setLocation(4 * dim.width / 5, 7 * dim.height / 10);
		frame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(dim.width / 5, 3 * dim.height / 10);
		frame.setVisible(true);

		parent.mapPropertyFrame = frame;
	}
}