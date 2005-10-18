/*-
* $Id: DomainPerpective.java,v 1.8 2005/10/18 15:10:39 bob Exp $
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

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.8 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainPerpective extends AbstractPerspective {

	private final DomainBean domainBean;
	private Object	cell;
	
	public DomainPerpective(final ManagerMainFrame graphText,
	                        final DomainBean domainBean,
	                        final Object cell) {
		super(graphText);
		this.domainBean = domainBean;
		this.cell = cell;
	}	
	
	public void addEntities(final JToolBar entityToolBar) {
		this.managerMainFrame.addAction(super.createAction(UserBeanFactory.getInstance(this.managerMainFrame)));
		this.managerMainFrame.addAction(super.createAction(WorkstationBeanFactory.getInstance(this.managerMainFrame)));
		entityToolBar.addSeparator();
		this.managerMainFrame.addAction(super.createAction(RTUBeanFactory.getInstance(this.managerMainFrame)));
		this.managerMainFrame.addAction(super.createAction(ServerBeanFactory.getInstance(this.managerMainFrame)));
		this.managerMainFrame.addAction(super.createAction(MCMBeanFactory.getInstance(this.managerMainFrame)));
		entityToolBar.addSeparator();		
	}
	
	public String getCodename() {
		return this.getDomainId().getIdentifierString();
	}
	
	public String getName() {		
		return ((DefaultGraphCell) this.cell).getUserObject().toString();
	}
	
	public final Identifier getDomainId() {
		return this.domainBean.getId();
	}
	
	public boolean isValid() {
		final JGraph graph = this.managerMainFrame.getGraph();
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
						!port.getBean().getCodeName().startsWith(NetBeanFactory.NET_CODENAME)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	public void insertBean(AbstractBean bean) {
		Identifier beanId = bean.getId();
		if (beanId != null) {
			try {
				StorableObject storableObject = StorableObjectPool.getStorableObject(beanId, true);
				if (storableObject instanceof DomainMember) {
					DomainMember  domainMember = (DomainMember)storableObject;
					domainMember.setDomainId(this.getDomainId());
				}
				if (storableObject instanceof SystemUser) {
					SystemUser systemUser = (SystemUser) storableObject;
					Identifier userId = systemUser.getId();
					
					for(final Module module : Module.getValueList()) {
						if (!module.isEnable()) {
							continue;
						}
						PermissionAttributes permissionAttributes = 
							this.domainBean.domain.getPermissionAttributes(systemUser.getId(), module);
						
						if (permissionAttributes == null) {
							permissionAttributes = PermissionAttributes.createInstance(
								LoginManager.getUserId(),
								this.getDomainId(),
								userId,
								module);
						}
					}
				}
			} catch (final ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void perspectiveApplied() {
		this.managerMainFrame.showOnlyDescendants((DefaultGraphCell) this.cell);
		
		this.managerMainFrame.showOnly(new String[] {NetBeanFactory.NET_CODENAME, 
				ObjectEntities.SYSTEMUSER, 
				WorkstationBeanFactory.WORKSTATION_CODENAME, 
				ObjectEntities.KIS, 
				ObjectEntities.SERVER, 
				ObjectEntities.MCM});
		
	}
	
}

