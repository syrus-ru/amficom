package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Model.*;

public class ElementsTreeFrame extends JInternalFrame
{
	ApplicationContext aContext;

	public ElementsTreeFrame(ApplicationContext aContext)
	{
		this.aContext = aContext;

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setTitle("Дерево компонентов");
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
	}

	public void init()
	{
		setContentPane(new ElementsTreePanel(aContext, aContext.getDispatcher(),
				new TestsTreeModel(aContext.getDataSourceInterface())));
	}
}