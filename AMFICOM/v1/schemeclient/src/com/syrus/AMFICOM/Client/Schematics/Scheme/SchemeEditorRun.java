package com.syrus.AMFICOM.Client.Schematics.Scheme;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class SchemeEditorRun
{
	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.schematics";

	public static void main(String[] args)
	{
		Environment.initialize();

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