/*-
* $Id: DomainPerpective.java,v 1.6 2005/10/11 15:34:53 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.math.BigInteger;

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
 * @version $Revision: 1.6 $, $Date: 2005/10/11 15:34:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainPerpective implements Perspective {

	private final ManagerMainFrame graphText;
	private final DomainBean domainBean;
	private Object	cell;
	
	public DomainPerpective(final ManagerMainFrame graphText,
	                        final DomainBean domainBean,
	                        final Object cell) {
		this.graphText = graphText;
		this.domainBean = domainBean;
		this.cell = cell;
	}
	
	public void addEntities(final JToolBar entityToolBar) {
		this.graphText.createAction(UserBeanFactory.getInstance(this.graphText));
		this.graphText.createAction(ARMBeanFactory.getInstance(this.graphText));
		entityToolBar.addSeparator();
		this.graphText.createAction(RTUBeanFactory.getInstance(this.graphText));
		this.graphText.createAction(ServerBeanFactory.getInstance(this.graphText));
		this.graphText.createAction(MCMBeanFactory.getInstance(this.graphText));
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
						PermissionAttributes permissionAttributes = 
							this.domainBean.domain.getPermissionAttributes(systemUser.getId(), module);
						
						if (permissionAttributes == null) {
							permissionAttributes = PermissionAttributes.createInstance(
								LoginManager.getUserId(),
								this.getDomainId(),
								userId,
								module,
								BigInteger.ZERO,
								BigInteger.ZERO);
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
		this.graphText.showOnlyDescendants((DefaultGraphCell) this.cell);
		
		this.graphText.showOnly(new String[] {NetBeanFactory.NET_CODENAME, 
				ObjectEntities.SYSTEMUSER, 
				ARMBeanFactory.ARM_CODENAME, 
				ObjectEntities.KIS, 
				ObjectEntities.SERVER, 
				ObjectEntities.MCM});
		
	}
	
}

