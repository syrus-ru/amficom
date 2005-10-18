/*-
* $Id: AbstractPerspective.java,v 1.1 2005/10/18 15:10:38 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.viewers.BeanUI;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class AbstractPerspective implements Perspective {
	
	protected static Map<String, AbstractBeanFactory> FACTORY_MAP = new HashMap<String, AbstractBeanFactory>();
	
	protected final ManagerMainFrame managerMainFrame;
	
	public AbstractPerspective(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}

	protected final AbstractAction createAction(final AbstractBeanFactory<?> factory) {
		final String name = factory.getName();
		final BeanUI beanUI = BeanUI.BeanUIFactory.getBeanUI(factory.getCodename() + "BeanUI", this.managerMainFrame);
		Icon icon = beanUI.getIcon(factory);
		FACTORY_MAP.put(factory.getCodename(), factory);
		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
			
			public void actionPerformed(final ActionEvent e) {
				try {
					final AbstractBean bean = factory.createBean(managerMainFrame.getPerspective());
					managerMainFrame.createChild(null, 
						factory.getShortName() + "-" + factory.getCount(), 
						bean, 
						20, 
						20, 
						0, 
						0, 
						beanUI.getImage(bean));
				} catch (final ApplicationException ae) {
					ae.printStackTrace();
					JOptionPane.showMessageDialog(managerMainFrame.getGraph(), 
						ae.getMessage(), 
						I18N.getString("Manager.Error"),
						JOptionPane.ERROR_MESSAGE);
				}				
			}
		};		
	
		action.putValue(Action.SHORT_DESCRIPTION, name);
		return action;
	}
	
}

