package com.syrus.AMFICOM.Client.Schematics.Scheme;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.SchematicsApplicationModelFactory;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;


public class SchemeEditor
{
	ApplicationContext aContext = new ApplicationContext();

	public SchemeEditor(SchematicsApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_SCHEMATICS))
			return;

		aContext.setApplicationModel(factory.create());
		aContext.setDispatcher(new Dispatcher());
		SchemeEditorMainFrame frame = new SchemeEditorMainFrame(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		Environment.initialize();
		LangModelSchematics.initialize();

		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		new SchemeEditor(new DefaultSchematicsApplicationModelFactory());
	}
}

