package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePathPropertiesManager;
import com.syrus.AMFICOM.scheme.SchemePath;

public class PathEditCommand extends AbstractCommand {
	SchemeTabbedPane pane;

	public PathEditCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}

	@Override
	public void execute() {
		ApplicationContext aContext = this.pane.getContext();
		
		SchemePath path = SchemeResource.getSchemePath();
		if (path != null) {
			aContext.getDispatcher().firePropertyChange(
					new ObjectSelectedEvent(this, path, 
							SchemePathPropertiesManager.getInstance(aContext), 
							ObjectSelectedEvent.SCHEME_PATH));
			SchemeResource.setSchemePath(path, true);
		}
	}
}



