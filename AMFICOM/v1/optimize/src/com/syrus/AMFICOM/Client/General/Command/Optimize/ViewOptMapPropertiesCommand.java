package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;

//команда "открыть окно отображения свойств элементов карты"
//================================================================================================================
public class ViewOptMapPropertiesCommand  extends ViewMapPropertiesCommand
{	JDesktopPane dtp;
	public ViewOptMapPropertiesCommand()
	{
	}
	//----------------------------------------------------------------------------------------
	public ViewOptMapPropertiesCommand(JDesktopPane desktop, ApplicationContext aContext)
	{	super(desktop, aContext);
		this.dtp = desktop;
	}
	//----------------------------------------------------------------------------------------
	public void execute()
	{	super.execute();
		Dimension dim = new Dimension(dtp.getWidth(), dtp.getHeight());
		// это не моё окно, поэтому для расположения на экране использую не метод dispose(), а устанавливаю размеры напрямую
		int height = (int)(0.15*dim.height + 2 + 28), width = (int)(dim.width*0.22 - 2); // "+2" потому что просто подгонял при разрешении 1280*1024
		frame.setLocation( (int)(dim.width-width + 1), (int)(dim.height*(1-0.2)-height + 2.5) );
		frame.setSize(width, height);
		frame.setIconifiable(true);
		frame.setClosable(true);
//		frame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

		frame.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		frame.setVisible(true);
		frame.toFront();
	}
	//----------------------------------------------------------------------------------------
}
//================================================================================================================
