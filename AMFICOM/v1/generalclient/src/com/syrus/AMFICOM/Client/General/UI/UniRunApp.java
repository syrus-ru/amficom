package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.Test.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.*;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;
//import com.syrus.AMFICOM.Client.Resource.Network.*;
//import com.syrus.AMFICOM.Client.Resource.ISM.*;
//import com.syrus.AMFICOM.Client.Configure.UI.*;

public class UniRunApp
{
	private boolean packFrame = false;

	//Construct the application
	public UniRunApp()
	{

		ApplicationContext aContext = new ApplicationContext();
		ApplicationModel am = new ApplicationModel();
		aContext.setApplicationModel(am);

		aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
		ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());

		aContext.setSessionInterface(Environment.getDefaultSessionInterface(aContext.getConnectionInterface()));
		SessionInterface.setActiveSession(aContext.getSessionInterface());

		DataSourceInterface dataSource = new RISDSurveyDataSource(aContext.getSessionInterface());

		aContext.setDataSourceInterface(dataSource);

/*
		aContext.getDataSourceInterface().LoadTestTypes();
		new SchemeDataSourceImage(dataSource).LoadNetDirectory();
		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		aContext.getDataSourceInterface().LoadNet();
		aContext.getDataSourceInterface().LoadISM();
*/
		RunFrame frame = new RunFrame();

		new SessionOpenCommand(Environment.the_dispatcher, aContext).execute();

		frame.getContentPane().add(new UniTreePanel(Environment.the_dispatcher, aContext, new ObserverTreeModel(aContext.getDataSourceInterface())));
/*
		//LinkGeneralPanel egp = new LinkGeneralPanel();
		//Link ap = (Link )Pool.get("link","link40");
		KISPane ep = new KISPane();
		KIS eq = (KIS )Pool.get("kis","kis2");
		//TransmissionPath ap = (TransmissionPath )(Pool.getHash(TransmissionPath.typ).elements().nextElement());
		ep.setObjectResource(eq);
		frame.getContentPane().add(ep);
*/
		//Validate frames that have preset sizes
		//Pack frames that have useful preferred size info, e.g. from their layout
		if (packFrame)
		{
			frame.pack();
		}
		else
		{
			frame.validate();
		}
		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}
	//Main method
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		new RunApp();
	}
}