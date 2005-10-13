/*-
* $Id: SystemUserPerpective.java,v 1.2 2005/10/13 15:28:14 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import javax.swing.JToolBar;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.2 $, $Date: 2005/10/13 15:28:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserPerpective implements Perspective {

	private final ManagerMainFrame graphText;
	private final UserBean userBean;
	private Object	cell;
	
	public SystemUserPerpective(final ManagerMainFrame graphText,
	                        final UserBean userBean,
	                        final Object cell) {
		this.graphText = graphText;
		this.userBean = userBean;
		this.cell = cell;
	}
	
	public void addEntities(final JToolBar entityToolBar) {
		for(final Module module : Module.getValueList()) {
			if (!module.isEnable()) {
				continue;
			}
			this.graphText.createAction(PermissionBeanFactory.getInstance(this.graphText, module));
		}
	}
	
	public String getCodename() {
		return this.getUserId().getIdentifierString();
	}
	
	public String getName() {		
		return this.userBean.getName();
	}
	
	public final Identifier getDomainId() {
		return this.userBean.getId();
	}
	
	public final Identifier getUserId() {
		return this.userBean.getId();
	}
	
	public boolean isValid() {
		JGraph graph = this.graphText.getGraph();
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
						!port.getBean().getCodeName().startsWith(ObjectEntities.SYSTEMUSER)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	
	public void perspectiveApplied() {
		this.graphText.showOnlyDescendants((DefaultGraphCell) this.cell);
		
		this.graphText.showOnly(new String[] {ObjectEntities.SYSTEMUSER, 
				ObjectEntities.PERMATTR});
		
	}
	
}

