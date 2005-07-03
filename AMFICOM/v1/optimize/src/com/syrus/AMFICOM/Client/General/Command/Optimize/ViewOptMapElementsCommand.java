package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.General.Command.Map.*;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.UI.*;

//================================================================================================================
public class ViewOptMapElementsCommand extends ViewMapElementsCommand
{ JDesktopPane dtp;
	public ViewOptMapElementsCommand()
	{	super();
	}
	//----------------------------------------------------------------------------------------
	public ViewOptMapElementsCommand(JDesktopPane desktop, ApplicationContext aContext)
	{	super(desktop, aContext);
		this.dtp = desktop;
	}
	//----------------------------------------------------------------------------------------
	public void execute()
	{	super.execute();
		Dimension dim = new Dimension(dtp.getWidth(), dtp.getHeight());
		int height = dim.height/5, width = (int)(dim.width*0.22);
		frame.setLocation( dim.width - width, dim.height - height);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
}
//================================================================================================================