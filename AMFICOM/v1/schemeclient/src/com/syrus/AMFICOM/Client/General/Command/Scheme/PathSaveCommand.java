package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanel;
import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

public class PathSaveCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemePanel panel;
	UgoPanel upanel;

	public PathSaveCommand(ApplicationContext aContext, SchemePanel panel, UgoPanel upanel)
	{
		this.aContext = aContext;
		this.panel = panel;
		this.upanel = upanel;
	}

	public Object clone()
	{
		return new PathSaveCommand(aContext, panel, upanel);
	}

	public void execute()
	{
		SchemePath path = panel.getGraph().currentPath;
		if (!MiscUtil.validName(path.name) ||
				path.end_device_id.equals("") ||
				path.start_device_id.equals("") ||
				path.links.isEmpty())
		{
			String err = new String();
			err += "Недостаточно данных для сохранения маршрута.\n";
			err += "Необходимо ввести название, начальное и\n";
			err += "конечное устройство и хотя бы одну связь.";
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), err, "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		aContext.getDispatcher().notify(new CreatePathEvent(panel,
				new SchemePath[] {path},
				CreatePathEvent.SAVE_PATH_EVENT));

		Pool.put(SchemePath.typ, path.getId(), path);
		boolean b = panel.insertPathToScheme(path);
		if (!b)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Ошибка при добавлении пути " + path.getName() + " на схему",
																		"Ошибка", JOptionPane.OK_OPTION);
		}
		else
		{
			new SchemeSaveCommand(aContext, panel, upanel).execute();
		}

		//new SchemeSaveCommand(aContext, panel).execute();
	}
}



