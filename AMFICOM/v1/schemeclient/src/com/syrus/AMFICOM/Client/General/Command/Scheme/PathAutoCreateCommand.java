package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class PathAutoCreateCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;
	SchemePanel panel;

	public PathAutoCreateCommand(ApplicationContext aContext, SchemePanel panel)
	{
		this.aContext = aContext;
		this.panel = panel;
		this.graph = panel.getGraph();
	}

	public Object clone()
	{
		return new PathAutoCreateCommand(aContext, panel);
	}

	public void execute()
	{
		Scheme scheme = panel.scheme;
		SchemePath path = graph.currentPath;
		if (path.start_device_id.length() == 0 ||
				path.end_device_id.length() == 0)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Ошибка",
																		"Не введено начальное и/или конечное устройство",
																		JOptionPane.OK_OPTION);
			return;
		}

		SchemeElement startSE = scheme.getTopLevelElement(path.start_device_id);
		SchemeElement endSE = scheme.getTopLevelElement(path.end_device_id);
		if (startSE == null || endSE == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
																		"Ошибка",
																		"Начальное или конечное устройство не найдено на схеме",
																		JOptionPane.OK_OPTION);
			return;
		}

		Scheme startScheme = scheme;
		if (startSE.scheme_id.length() != 0)
			startScheme = (Scheme)Pool.get(Scheme.typ, startSE.scheme_id);

		ArrayList links = new ArrayList();
		SchemeDevice endDevice;
		String id = "";
		while (!id.equals(endSE.getId()))
		{

		}
	}
}

