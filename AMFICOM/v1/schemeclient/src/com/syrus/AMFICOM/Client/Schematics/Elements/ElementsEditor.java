package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.SchematicsApplicationModelFactory;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;


public class ElementsEditor
{
	ApplicationContext aContext = new ApplicationContext();

	public ElementsEditor(SchematicsApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_COMPONENTS))
			return;

		aContext.setApplicationModel(factory.create());
	//	aContext.setDispatcher(new Dispatcher());
		ElementsEditorMainFrame frame = new ElementsEditorMainFrame(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/components_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		new ElementsEditor(new DefaultSchematicsApplicationModelFactory());
	}
}
