/*-
 * $Id: DomainBeanFactory.java,v 1.2 2005/07/20 14:51:07 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;

import com.syrus.AMFICOM.manager.UI.JGraphText;



/**
 * @version $Revision: 1.2 $, $Date: 2005/07/20 14:51:07 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class DomainBeanFactory extends AbstractBeanFactory {
	
	private static DomainBeanFactory instance;
	
	private Validator validator;
	
	private DomainBeanFactory() {
		super("Entity.Domain", 
			"Entity.Domain", 
			"com/syrus/AMFICOM/manager/resources/icons/cloud.gif", 
			"com/syrus/AMFICOM/manager/resources/cloud.png");
	}
	
	public static final DomainBeanFactory getInstance() {
		if(instance == null) {
			synchronized (DomainBeanFactory.class) {
				if(instance == null) {
					instance = new DomainBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean() {
		AbstractBean bean = new AbstractBean() {
			
			@Override
			public JPopupMenu getMenu(	final JGraphText graph,
										final Object cell) {

				if (cell != null) {
					JPopupMenu popupMenu = new JPopupMenu();
					String lastName = null;					

					popupMenu.add(new AbstractAction("Enter to domain") {

						public void actionPerformed(ActionEvent e) {
							
							graph.domainButton.setEnabled(false);
							graph.netButton.setEnabled(true);
							
							graph.userButton.setEnabled(true);

							graph.armButton.setEnabled(true);

							graph.rtuButton.setEnabled(true);

							graph.serverButton.setEnabled(true);

							graph.mcmButton.setEnabled(true);
							
							graph.showOnly(new String[] {"Domain"});
							
							System.out.println("DomainBeanFactory | entered");
						};
					});
					return popupMenu;
				}

				return null;
			}			
			

		};
		bean.setCodeName("Domain");
		bean.setValidator(this.getValidator());
		bean.setName("Domain" + (++this.count));
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						(sourceBean.getCodeName().equals("Domain") ||
						 sourceBean.getCodeName().equals("Net")) &&
						targetBean.getCodeName().equals("Domain");
				}
			};
		}
		return this.validator;
	}
}
