package com.syrus.AMFICOM.Client.Schematics.Scheme;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class SchemeEditorRun
{
	public static void main(String[] args)
	{
		Environment.initialize();
		LangModelSchematics.initialize();

		try
		{
//			JFrame.setDefaultLookAndFeelDecorated(true);
//
//			KunststoffLookAndFeel plaf = (KunststoffLookAndFeel)KunststoffLookAndFeel.class.newInstance();
//			plaf.setCurrentTheme(new AMFICOMMetalTheme());
//			UIManager.setLookAndFeel(plaf);
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		new SchemeEditor(new DefaultSchematicsApplicationModelFactory());
	}
}