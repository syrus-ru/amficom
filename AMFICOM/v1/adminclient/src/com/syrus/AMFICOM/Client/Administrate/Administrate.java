/*
 * $Id: Administrate.java,v 1.3 2004/07/13 12:19:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAdmin;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.awt.*;
import javax.swing.UIManager;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/13 12:19:10 $
 * @author $Author: peskovsky $
 * @module admin_v1
 */
public class Administrate
{
	ApplicationContext aContext = new ApplicationContext();

	public Administrate(AdminApplicationModelFactory factory)
	{
		if (!Environment.canRun(Environment.MODULE_ADMINISTRATE))
			return;

		aContext.setApplicationModel(factory.create());

		Frame frame = new AdministrateMDIMain(aContext);
/*
		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();

		frame.setSize(screenSize.width-40, screenSize.height-40);

//		if (frameSize.height > screenSize.height)
//		{
//			frameSize.height = screenSize.height;
//		}
//		if (frameSize.width > screenSize.width)
//		{
//			frameSize.width = screenSize.width;
//		}
		frame.setLocation(20, 0);
//		frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });
*/
//		Environment.addWindow(frame);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/administrate_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH));

		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
//		System.out.println(LangModel.resourceBundle + " vs " + LangModelAdmin.resourceBundle);
		Environment.initialize();
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		new Administrate(new DefaultAdminApplicationModelFactory());
	}
}
