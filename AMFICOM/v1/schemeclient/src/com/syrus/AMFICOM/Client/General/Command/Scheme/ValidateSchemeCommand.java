/*-
 * $Id: ValidateSchemeCommand.java,v 1.2 2006/02/21 08:14:57 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.AbstractSchemeElement;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class ValidateSchemeCommand extends AbstractCommand {
	SchemeTabbedPane cellPane;
	
	public ValidateSchemeCommand(SchemeTabbedPane cellPane) {
		this.cellPane = cellPane;
	}
	
	@Override
	public void execute() {
		ElementsPanel panel = this.cellPane.getCurrentPanel();
		if (panel != null) {
			SchemeResource res = panel.getSchemeResource();
			SchemeGraph graph = panel.getGraph();
			if (res.getCellContainerType() == SchemeResource.SCHEME) {
				try {
					Scheme scheme = res.getScheme();
					
					Set<SchemePath> paths = scheme.getSchemePathsRecursively(true);
					for (SchemePath path : paths) {
						for (PathElement pe : path.getPathMembers()) {
							try {
								AbstractSchemeElement ase = pe.getAbstractSchemeElement();
								if (ase == null) {
									throw new NullPointerException("Linked AbstractSchemeElement '" + pe.getAbstractSchemeElementId() + "' is null");
								}
							} catch (Throwable t) {
								Log.errorMessage("PathElement " + pe + " is invalid. Caused by:\n" + t.getMessage());
								Log.errorMessage("Deleting " + pe + " Please check consistency of SchemePath " + path);
								pe.setParentPathOwner(null, false);
							}
						}
					}
					
					Set<SchemeCableLink> scls = scheme.getSchemeCableLinks(true);
					for (SchemeCableLink scl : scls) {
						DefaultCableLink dcl = SchemeActions.findSchemeCableLinkById(graph, scl.getId());

						if (dcl == null) {
							Log.errorMessage("SchemeCableLink " + scl + " is invalid. It doesn't found on parent schemeGraph. Deleting it.");
							scl.setParentScheme(null, false);
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				
			}
			
			SchemeActions.isValid(graph);
		
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
					SchemeActions.getValidationMessage(), 
					LangModelScheme.getString("Message.information"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
