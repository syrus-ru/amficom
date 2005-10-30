package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class PathCancelCommand extends AbstractCommand {
	SchemeTabbedPane pane;

	public PathCancelCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}

	@Override
	public void execute() {
		SchemePath path = SchemeResource.getSchemePath();
		try {
			path.setParentSchemeMonitoringSolution(null, false);
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
		}
		SchemeResource.setSchemePath(null, false);
//		SchemeResource.setCashedPathStart(null);
		SchemeResource.setCashedPathEnd(null);
		
		ApplicationContext aContext = this.pane.getContext();
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuPathNew", true);
		aModel.setEnabled("menuPathSave", false);
		aModel.setEnabled("menuPathCancel", false);
		aModel.fireModelChanged();
	}
}



