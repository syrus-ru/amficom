package com.syrus.AMFICOM.Client.Schematics.Elements;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class ElementsEditorRun
{
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

