package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

public class ViewMapSchemeNavigatorCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public MapSchemeTreeFrame frame;
	public String title = "Топологическая привязка схемы";

	public ViewMapSchemeNavigatorCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ViewMapSchemeNavigatorCommand(desktop, aContext);
	}

	public void execute()
	{
/*
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		new SchemeDataSourceImage(dataSource).LoadSchemes();
*/
		frame = null;
		
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if (comp instanceof MapSchemeTreeFrame)
			{
//				desktop.remove(comp);
				frame = (MapSchemeTreeFrame)comp;
				break;
			}
		}

		if(frame == null)
		{
			MapSchemeTreePanel panel = new MapSchemeTreePanel("", aContext);

			frame = new MapSchemeTreeFrame();
			frame.setTitle(title);
			frame.setClosable(true);
			frame.setResizable(true);
			frame.setMaximizable(false);
			frame.setIconifiable(false);
			frame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			desktop.add(frame);

			Dimension dim = desktop.getSize();

			frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			frame.setSize(dim.width / 5, dim.height / 2);
		}

		frame.setVisible(true);
		frame.toFront();
	}

}


