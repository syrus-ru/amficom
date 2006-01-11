/*-
 * $Id: ValidateSchemeCommand.java,v 1.1 2006/01/11 13:59:49 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.resource.LangModelScheme;

public class ValidateSchemeCommand extends AbstractCommand {
	UgoTabbedPane cellPane;
	
	public ValidateSchemeCommand(UgoTabbedPane cellPane) {
		this.cellPane = cellPane;
	}
	
	@Override
	public void execute() {
		SchemeGraph graph = this.cellPane.getGraph();
		SchemeActions.isValid(graph);
		
		JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
				SchemeActions.getValidationMessage(), 
				LangModelScheme.getString("Message.information"),
				JOptionPane.INFORMATION_MESSAGE);
	}
}
