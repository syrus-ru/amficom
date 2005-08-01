package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

public class ComponentNewCommand extends AbstractCommand {
	ApplicationContext aContext;
	UgoTabbedPane cellPane;

	public ComponentNewCommand(ApplicationContext aContext,
			UgoTabbedPane cellPane) {
		this.aContext = aContext;
		this.cellPane = cellPane;
	}

	public Object clone() {
		return new ComponentNewCommand(aContext, cellPane);
	}

	public void execute() {
		ApplicationModel aModel = aContext.getApplicationModel(); 
		aModel.getCommand("menuWindowScheme").execute();
		aModel.getCommand("menuWindowTree").execute();
		aModel.getCommand("menuWindowUgo").execute();
		aModel.getCommand("menuWindowProps").execute();
		aModel.getCommand("menuWindowList").execute();
		
		aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
		
		SchemeGraph cellGraph = cellPane.getGraph();
		if (cellGraph.isGraphChanged()) {
			int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
					"Компонент не сохранен. Продолжить?", "Новый компонент",
					JOptionPane.OK_CANCEL_OPTION);
			if (ret == JOptionPane.CANCEL_OPTION)
				return;
		} 
		((ElementsPanel)cellPane.getCurrentPanel()).getSchemeResource().setSchemeProtoElement(null);
		GraphActions.clearGraph(cellGraph);
		cellGraph.selectionNotify();
	}
}
