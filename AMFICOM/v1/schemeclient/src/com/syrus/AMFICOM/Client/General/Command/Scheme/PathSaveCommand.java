package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class PathSaveCommand extends AbstractCommand {
	SchemeTabbedPane pane;

	public PathSaveCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}

	@Override
	public void execute() {
		SchemePath path = SchemeResource.getSchemePath();
		try {
			StorableObjectPool.flush(path.getReverseDependencies(false), LoginManager.getUserId(), false);
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.information.path_saved"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.information"),  //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
			
			SchemeResource.setSchemePath(null, false);
//			SchemeResource.setCashedPathStart(null);
			SchemeResource.setCashedPathEnd(null);
			
			ApplicationContext aContext = this.pane.getContext();
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuPathNew", true);
			aModel.setEnabled("menuPathSave", false);
			aModel.setEnabled("menuPathCancel", false);
			aModel.fireModelChanged();
			
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
		}
	}
}



