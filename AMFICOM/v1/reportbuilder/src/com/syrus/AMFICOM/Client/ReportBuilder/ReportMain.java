package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.UIManager;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Lang.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * <p>Description: Запускной файл</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportMain
{
	native int decodeData(int pass1, int pass2);

	ApplicationContext aContext = new ApplicationContext();

	public ReportMain(ReportApplicationModelFactory factory)
	{
/*    if(!Environment.canRun(Environment.MODULE_ADMINISTRATE))
			return;*/

		aContext.setApplicationModel(factory.create());

		Frame frame = new ReportMDIMain(aContext);

		frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });

//		Environment.addWindow(frame);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
//		System.out.println(LangModel.resourceBundle + " vs " + LangModelAdmin.resourceBundle);

		Environment.initialize();
		try
		{
//      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
//		System.out.println(LangModel.resourceBundle + " vs " + LangModelAdmin.resourceBundle);
		LangModelReport.initialize();
//		LangModelSurvey.initialize();
		LangModelConfig.initialize();
		LangModelSchematics.initialize();
		LangModelAnalyse.initialize();
		LangModelModel.initialize();
		LangModelPrediction.initialize();

//		System.out.println(LangModel.resourceBundle + " vs " + LangModelAdmin.resourceBundle);

		// test!!!
//		new com.syrus.AMFICOM.Client.Test.LoadTestAdmin();

		new ReportMain(new ReportDefaultApplicationModelFactory());
	}
}
