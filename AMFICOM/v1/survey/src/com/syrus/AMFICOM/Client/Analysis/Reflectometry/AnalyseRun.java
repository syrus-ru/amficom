package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class AnalyseRun
{
	public static void main(String[] args)
	{
		Environment.initialize();
		LangModelAnalyse.initialize();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		new Analyse(new ReflectometryAnalyseApplicationModelFactory());
	}
}