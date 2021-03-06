package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
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
		try {
			final SchemePath path = SchemeResource.getSchemePath();
			path.saveChanges(LoginManager.getUserId());
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					LangModelScheme.getString("Message.information.path_saved"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.information"),  //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
			
			SchemeResource.setSchemePath(path, false);
//			SchemeResource.setCashedPathStart(null);
			SchemeResource.setCashedPathEnd(null);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}



