
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Configure.Map.Editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class MapEditor
{
	ApplicationContext aContext = new ApplicationContext();

	public MapEditor(MapEditorApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_MAP))
			return;

		aContext.setApplicationModel(factory.create());
		Frame frame = new MapMDIMain(aContext);
//		Environment.addWindow(frame);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		Environment.initialize();
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
			LookAndFeel laf = UIManager.getLookAndFeel();
			UIDefaults ui = laf.getDefaults();
			ui.put(
					"InternalFrame.icon", 
					LookAndFeel.makeIcon(JDialog.class, "images/general.gif"));
//			BasicInternalFrameTitlePane rpui;
//			JInternalFrame jf;
//			com.sun.java.swing.plaf.windows.WindowsLookAndFeel wlf;
//			javax.swing.plaf.basic.BasicLookAndFeel blf;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		LangModelConfig.initialize();
		LangModelMap.initialize();
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}

