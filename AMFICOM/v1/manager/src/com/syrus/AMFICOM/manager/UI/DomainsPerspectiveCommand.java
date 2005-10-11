/*-
* $Id: DomainsPerspectiveCommand.java,v 1.1 2005/10/11 15:36:25 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import javax.swing.JToolBar;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.DomainBeanFactory;
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.Perspective;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/11 15:36:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainsPerspectiveCommand extends AbstractCommand {

	private class DomainsPerspective implements Perspective {
		
		public String getCodename() {
			return "domains";
		}
		
		public String getName() {
			return I18N.getString("Manager.Label.DomainsLevel");
		}
		
		public void addEntities(final JToolBar entityToolBar) {
			DomainsPerspectiveCommand.this.graphText.createAction(NetBeanFactory.getInstance(DomainsPerspectiveCommand.this.graphText));
			entityToolBar.addSeparator();
			DomainsPerspectiveCommand.this.graphText.createAction(DomainBeanFactory.getInstance(DomainsPerspectiveCommand.this.graphText));
		}
		
		public boolean isValid() {
			final JGraph graph = DomainsPerspectiveCommand.this.graphText.getGraph();
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
			DomainsPerspectiveCommand.this.graphText.treeModel.setRoot(null);
			DomainsPerspectiveCommand.this.graphText.showOnly(new String[] {NetBeanFactory.NET_CODENAME, 
					ObjectEntities.DOMAIN});
			
		}
	
	}
	
	final ManagerMainFrame graphText;
	private Perspective	perspective;
	
	public DomainsPerspectiveCommand(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {

		if (this.perspective == null) {
			this.perspective = new DomainsPerspective();
		}
		
		this.graphText.setPerspective(this.perspective);	

	}
}

