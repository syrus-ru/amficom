package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.Notifier;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

public class PathCancelCommand extends AbstractCommand {
	SchemeTabbedPane pane;

	public PathCancelCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}

	@Override
	public void execute() {
//		SchemePath path = SchemeResource.getSchemePath();
//		try {
//			path.setParentSchemeMonitoringSolution(null, false);
//		} catch (ApplicationException e) {
//			Log.errorMessage(e);
//		}
		SchemeResource.setSchemePath(null, false);
//		SchemeResource.setCashedPathStart(null);
		SchemeResource.setCashedPathEnd(null);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.PATHELEMENT_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.SCHEMEPATH_CODE);
		this.pane.setLinkMode();
		
		final ElementsPanel currentPanel = this.pane.getCurrentPanel();
		final SchemeGraph graph = currentPanel.getGraph();
		graph.clearSelection();

		try {
			Notifier.notify(graph, this.pane.getContext(), currentPanel.getSchemeResource().getCellContainer());
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}



