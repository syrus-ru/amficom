package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;

public class EvaluationRun
{
	public static void main(String[] args)
	{
		Environment.initialize();
//		LangModelAnalyse.initialize();
//		LangModelReport.initialize();

		try {
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		new Evaluation(new ReflectometryAnalyseApplicationModelFactory());
	}
}