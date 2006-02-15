package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

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
	}
}



