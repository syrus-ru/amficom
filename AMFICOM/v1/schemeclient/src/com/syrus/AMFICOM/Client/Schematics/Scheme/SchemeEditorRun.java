package com.syrus.AMFICOM.Client.Schematics.Scheme;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.*;

public class SchemeEditorRun
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

		new SchemeEditor(new DefaultSchematicsApplicationModelFactory());
	}
}

