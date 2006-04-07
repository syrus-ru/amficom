package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.resource.LangModelScheme;

public class ComponentNewCommand extends AbstractCommand {
	ApplicationContext aContext;
	UgoTabbedPane cellPane;

	public ComponentNewCommand(ApplicationContext aContext,
			UgoTabbedPane cellPane) {
		this.aContext = aContext;
		this.cellPane = cellPane;
	}

	@Override
	public void execute() {
		ApplicationModel aModel = this.aContext.getApplicationModel(); 
		aModel.getCommand("menuWindowScheme").execute();
		aModel.getCommand("menuWindowTree").execute();
		aModel.getCommand("menuWindowUgo").execute();
		aModel.getCommand("menuWindowProps").execute();
		aModel.getCommand("menuWindowList").execute();
		
		aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
		
		SchemeGraph cellGraph = this.cellPane.getGraph();
		if (cellGraph.isGraphChanged()) {
			int ret = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.confirmation.component_not_saved"), //$NON-NLS-1$
					LangModelScheme.getString("Message.confirmation"), //$NON-NLS-1$
					JOptionPane.OK_CANCEL_OPTION);
			if (ret == JOptionPane.CANCEL_OPTION)
				return;
		} 
		((ElementsPanel)this.cellPane.getCurrentPanel()).getSchemeResource().setSchemeProtoElement(null);
		GraphActions.clearGraph(cellGraph);
		cellGraph.selectionNotify();
	}
}
