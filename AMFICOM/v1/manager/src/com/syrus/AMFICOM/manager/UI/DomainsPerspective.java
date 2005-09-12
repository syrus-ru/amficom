/*-
* $Id: DomainsPerspective.java,v 1.4 2005/09/12 11:10:16 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.LangModelManager;
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.Perspective;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/09/12 11:10:16 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainsPerspective extends AbstractCommand {

	final ManagerMainFrame graphText;
	private Perspective	perspective;
	
	public DomainsPerspective(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {

		if (this.perspective == null) {
			this.perspective = new Perspective() {
				public String getPerspectiveName() {
					return "domains";
				}
				
				public boolean isValid() {
					final JGraph graph = DomainsPerspective.this.graphText.getGraph();
					final GraphLayoutCache graphLayoutCache = graph.getGraphLayoutCache();
					final GraphModel model = graph.getModel();
					for(final Object root : graph.getRoots()) {
						if (!model.isPort(root) 
								&& !model.isEdge(root) 
								&& graphLayoutCache.isVisible(root)) {
							MPort port = (MPort) ((DefaultGraphCell)root).getChildAt(0);
							int visibleTarget = 0;
							for(final Port targetPort : port.getTargets()) {
								visibleTarget += graphLayoutCache.isVisible(targetPort) ? 1 : 0;
							}
							
							if (visibleTarget == 0 && 
									!port.getBean().getCodeName().startsWith(ObjectEntities.DOMAIN)) {
								return false;
							}
							
						}
						
					}
					return true;
				}
				
				public void perspectiveApplied() {

					DomainsPerspective.this.graphText.currentPerspectiveLabel.setText(LangModelManager.getString("Label.DomainsLevel"));
				
					DomainsPerspective.this.graphText.domainButton.setEnabled(true);
					DomainsPerspective.this.graphText.netButton.setEnabled(true);
					
					DomainsPerspective.this.graphText.userButton.setEnabled(false);

					DomainsPerspective.this.graphText.armButton.setEnabled(false);

					DomainsPerspective.this.graphText.rtuButton.setEnabled(false);

					DomainsPerspective.this.graphText.serverButton.setEnabled(false);

					DomainsPerspective.this.graphText.mcmButton.setEnabled(false);

					DomainsPerspective.this.graphText.treeModel.setRoot(null);
					
					DomainsPerspective.this.graphText.domainsButton.setEnabled(false);
					
					DomainsPerspective.this.graphText.showOnly(new String[] {NetBeanFactory.NET_CODENAME, 
							ObjectEntities.DOMAIN});
					
				}
			};
		}
		
		this.graphText.setPerspective(this.perspective);	

	}
}

