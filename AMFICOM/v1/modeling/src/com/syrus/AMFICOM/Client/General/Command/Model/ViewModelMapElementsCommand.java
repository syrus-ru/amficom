package com.syrus.AMFICOM.Client.General.Command.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.Model.ModelMDIMain;

import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class ViewModelMapElementsCommand extends ViewMapElementsCommand
{
	ModelMDIMain parent;
	JDesktopPane dtp;
	public ViewModelMapElementsCommand()
	{
		super();
	}
	public ViewModelMapElementsCommand(ModelMDIMain parent, JDesktopPane desktop, ApplicationContext aContext)
	{
		super(desktop, aContext);
		this.dtp = desktop;
		this.parent = parent;
	}


	public void execute()
	{
		super.execute();

		Dimension dim = desktop.getSize();

		frame.setLocation(4 * dim.width / 5, 2 * dim.height / 5);
		frame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(dim.width / 5, 3 * dim.height / 10);
		frame.setVisible(true);

		parent.mapElementsFrame = frame;
	}


}