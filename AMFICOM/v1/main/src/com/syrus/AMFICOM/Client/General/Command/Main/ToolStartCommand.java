
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.General.Command.Main;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Main.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.ReportBuilder.*;

import javax.swing.JOptionPane;

public class ToolStartCommand extends VoidCommand implements Command
{
	String toolLabel = "";
	String toolName = "";
	Dispatcher disp;
	
	public ToolStartCommand(Dispatcher disp, String toolName, String toolLabel)
	{
		this.toolName = toolName;
		this.toolLabel = toolLabel;
		this.disp = disp;
	}

	public Object clone()
	{
		return new ToolStartCommand(disp, toolName, toolLabel);
	}

	public void execute()
	{
		new MyThread(disp, toolName, toolLabel).start();
	}
}
	
class MyThread extends Thread
{
	String toolLabel = "";
	String toolName = "";
	Dispatcher disp;
	
	public MyThread(Dispatcher disp, String toolName, String toolLabel)
	{
		this.toolName = toolName;
		this.toolLabel = toolLabel;
		this.disp = disp;
	}

	public void run()
	{
		try
		{
			disp.notify(new StatusMessageEvent("Запуск модуля " + toolLabel));

			if(toolName.equals("com.syrus.AMFICOM.Client.ReportBuilder.ReportMain"))
			{
				LangModelSurvey.initialize();
				LangModelConfig.initialize();
				LangModelSchematics.initialize();
				LangModelAnalyse.initialize();
				LangModelModel.initialize();
				LangModelPrediction.initialize();  
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.ReportBuilder.ReportMain(new ReportDefaultApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Analysis.Reflectometry.Analyse"))
			{
				LangModelAnalyse.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Analysis.Reflectometry.Analyse(new ReflectometryAnalyseApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt"))
			{
				LangModelAnalyse.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt(new ReflectometryAnalyseApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Administrate.Administrate"))
			{
				LangModelAdmin.initialize();
			    new com.syrus.AMFICOM.Client.Administrate.Administrate(new DefaultAdminApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Configure.Configure"))
			{
				LangModelConfig.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Configure.Configure(new DefaultConfigApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeEditor"))
			{
				LangModelSchematics.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeEditor(new DefaultSchematicsApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Configure.Map.Editor.MapEditor"))
			{
				LangModelConfig.initialize();
				LangModelMap.initialize();
				LangModelSchematics.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Configure.Map.Editor.MapEditor(new DefaultMapEditorApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Schematics.Elements.ElementsEditor"))
			{
				LangModelSchematics.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Schematics.Elements.ElementsEditor(new DefaultSchematicsApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Optimize.Optimize"))
			{
				LangModelOptimize.initialize();
				LangModelSchematics.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Optimize.Optimize(new DefaultOptimizeApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Schedule.Schedule"))
			{
				LangModelSurvey.initialize();
				LangModelSchedule.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Schedule.Schedule(new DefaultScheduleApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Survey.Survey"))
			{
				LangModelConfig.initialize();
				LangModelMap.initialize();
				LangModelAnalyse.initialize();
				LangModelSchematics.initialize();
				LangModelSurvey.initialize();
				LangModelSchedule.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Survey.Survey(new DefaultSurveyApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Model.Model"))
			{
				LangModelModel.initialize();
				LangModelAnalyse.initialize();
				LangModelSchematics.initialize();
				LangModelMap.initialize();
				LangModelReport.initialize();
				LangModelConfig.initialize();
				new com.syrus.AMFICOM.Client.Model.Model(new DefaultModelApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation"))
			{
				LangModelAnalyse.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation(new ReflectometryAnalyseApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Prediction.Prediction"))
			{
				LangModelPrediction.initialize();
				LangModelAnalyse.initialize();
				LangModelReport.initialize();
				new com.syrus.AMFICOM.Client.Prediction.Prediction(new DefaultPredictionApplicationModelFactory());
			}
			else
/*			
			if(toolName.equals("com.syrus.AMFICOM.Client.Maintenance.Maintenance"))
			{
				LangModelAnalyse.initialize();
				new com.syrus.AMFICOM.Client.Maintenance.Maintenance(new ReflectometryAnalyseApplicationModelFactory());
			}
			else
*/			
			throw new ClassNotFoundException(toolName);


			disp.notify(new StatusMessageEvent("Операция завершена"));
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("Не найден ресурс класса. проверьте настройки системы: " + cnfe.getMessage());
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					"Модуль " + toolLabel + " не установлен", 
					"Модуль не установлен!", 
					JOptionPane.INFORMATION_MESSAGE);
			disp.notify(new StatusMessageEvent("Модуль " + toolLabel + " не установлен!"));
		}
		catch(Exception e)
		{
			System.out.println("Ошибка запуска модуля - " + e.getMessage());
			disp.notify(new StatusMessageEvent("Ошибка открытия модуля " + toolLabel));
		}
	}
}

 