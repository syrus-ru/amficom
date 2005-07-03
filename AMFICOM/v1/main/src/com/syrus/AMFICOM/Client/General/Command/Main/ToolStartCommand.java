
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
				new com.syrus.AMFICOM.Client.ReportBuilder.ReportMain(new ReportDefaultApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Analysis.Reflectometry.Analyse"))
			{
				new com.syrus.AMFICOM.Client.Analysis.Reflectometry.Analyse(new ReflectometryAnalyseApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt"))
			{
				new com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt(new ReflectometryAnalyseApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Administrate.Administrate"))
			{
			    new com.syrus.AMFICOM.Client.Administrate.Administrate(new DefaultAdminApplicationModelFactory());
			}
			else
/*
			if(toolName.equals("com.syrus.AMFICOM.Client.Configure.Configure"))
			{
				new com.syrus.AMFICOM.Client.Configure.Configure(new DefaultConfigApplicationModelFactory());
			}
			else
*/
			if(toolName.equals("com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeEditor"))
			{
				new com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeEditor(new DefaultSchematicsApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Map.Editor.MapEditor"))
			{
				new com.syrus.AMFICOM.Client.Map.Editor.MapEditor(new DefaultMapEditorApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Schematics.Elements.ElementsEditor"))
			{
				new com.syrus.AMFICOM.Client.Schematics.Elements.ElementsEditor(new DefaultSchematicsApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Optimize.Optimize"))
			{
				new com.syrus.AMFICOM.Client.Optimize.Optimize(new DefaultOptimizeApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Schedule.Schedule"))
			{
				new com.syrus.AMFICOM.Client.Schedule.Schedule();
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Survey.Survey"))
			{
				new com.syrus.AMFICOM.Client.Survey.Survey(new DefaultSurveyApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Model.Model"))
			{
				new com.syrus.AMFICOM.Client.Model.Model(new DefaultModelApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation"))
			{
				new com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation(new ReflectometryAnalyseApplicationModelFactory());
			}
			else
			if(toolName.equals("com.syrus.AMFICOM.Client.Prediction.Prediction"))
			{
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

 