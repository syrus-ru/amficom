package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

public class ComponentNewCommand extends AbstractCommand {
	ApplicationContext aContext;
	UgoTabbedPane cellPane;
	UgoTabbedPane ugoPane;

	public ComponentNewCommand(ApplicationContext aContext,
			UgoTabbedPane cellPane, UgoTabbedPane ugoPane) {
		this.aContext = aContext;
		this.cellPane = cellPane;
		this.ugoPane = ugoPane;
	}

	public Object clone() {
		return new ComponentNewCommand(aContext, cellPane, ugoPane);
	}

	public void execute() {
		SchemeGraph cellGraph = cellPane.getGraph();
		
		if (cellGraph.getAll().length != 0) {
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Создать новый компонент?", "Новый компонент",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.NO_OPTION)
				return;
		}
		GraphActions.clearGraph(cellGraph);
		GraphActions.clearGraph(ugoPane.getGraph());
		cellGraph.selectionNotify();
	}
}
