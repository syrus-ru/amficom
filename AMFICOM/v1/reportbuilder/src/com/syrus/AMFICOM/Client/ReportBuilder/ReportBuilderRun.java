package com.syrus.AMFICOM.Client.ReportBuilder;

import javax.swing.UIManager;
import com.syrus.AMFICOM.Client.General.Lang.*;

import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * <p>Description: Запускной файл</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportBuilderRun
{
	public ReportBuilderRun()
	{
	}

	public static void main(String[] args)
	{
			Environment.initialize();
			try
			{
				UIManager.setLookAndFeel(Environment.getLookAndFeel());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			LangModelReport.initialize();
//			LangModelSurvey.initialize();
			LangModelConfig.initialize();
			LangModelSchematics.initialize();
			LangModelAnalyse.initialize();
			LangModelModel.initialize();
			LangModelPrediction.initialize();

			new ReportMain(new ReportDefaultApplicationModelFactory());
	}
}