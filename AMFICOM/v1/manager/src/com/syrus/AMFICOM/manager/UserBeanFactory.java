/*-
* $Id: UserBeanFactory.java,v 1.4 2005/07/15 11:59:00 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;



/**
 * @version $Revision: 1.4 $, $Date: 2005/07/15 11:59:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends AbstractBeanFactory {

	private static UserBeanFactory instance;
	
	private int count = 0;
	
	private Validator validator;
	
	List<String> names;
	
	private UserBeanFactory() {
		super("Entity.User", 
			"Entity.User", 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
		
		this.names = new ArrayList<String>();
		this.names.add("Абонент");
		this.names.add(null);
		this.names.add("Администратор системный");
		this.names.add("Администратор среды мониторинга");
		this.names.add("Аналитик");
		this.names.add(null);
	}
	
	public static final UserBeanFactory getInstance() {
		if (instance == null) {
			synchronized (UserBeanFactory.class) {
				if (instance == null) {
					instance = new UserBeanFactory();
				}				
			}
		}
		return instance;
	}

	
	@Override
	public AbstractBean createBean() {
		AbstractBean bean = new AbstractBean() {

			
			
			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
				AttributeMap attributes = edge.getAttributes();
				GraphConstants.setLineWidth(attributes, 10.0f);
				GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_TECHNICAL);
				GraphConstants.setLineColor(attributes, Color.LIGHT_GRAY);
				GraphConstants.setForeground(attributes, Color.BLACK);
			}
			
			@Override
			public JPopupMenu getMenu(final	JGraph graph,
			                          final Object cell) {
				
				if (cell != null) {
					System.out.println(".getMenu() | cell:" + cell + '[' + cell.getClass().getName() + ']');
					// Edit
					JPopupMenu menu = new JPopupMenu();
					String lastName = null;
					for(final String name: names) {
						if (name != null) {
							menu.add(new AbstractAction(name) {
								public void actionPerformed(ActionEvent e) {
									storableObject = name;
									AttributeMap attributeMap = new AttributeMap();
									GraphConstants.setValue(attributeMap, name);
									Map viewMap = new Hashtable();
									viewMap.put(cell, attributeMap);
									graph.getModel().edit(viewMap, null, null, null);
								}
							});
						} else {
							menu.addSeparator();
						}
						
						lastName = name;
					}
					
					if (lastName != null) {
						menu.addSeparator();
					}
					
					menu.add(new AbstractAction(LangModelManager.getString("Entity.User.new")  + "...") {
						
						public void actionPerformed(ActionEvent e) {
							String string = JOptionPane.showInputDialog(null, "", "New...", JOptionPane.OK_CANCEL_OPTION);
							names.add(string);
							storableObject = string;
							AttributeMap attributeMap = new AttributeMap();
							GraphConstants.setValue(attributeMap, string);
							Map viewMap = new Hashtable();
							viewMap.put(cell, attributeMap);
							graph.getModel().edit(viewMap, null, null, null);
						}
					});
//					menu.add(new AbstractAction("Edit") {
//						public void actionPerformed(ActionEvent e) {
//							graph.startEditingAtCell(cell);
//						}
//					});
					return menu;
				}
				
//				// Remove
//				if (!graph.isSelectionEmpty()) {
//					menu.addSeparator();
//					menu.add(new AbstractAction("Remove") {
//						public void actionPerformed(ActionEvent e) {
//							remove.actionPerformed(e);
//						}
//					});
//				}
//				menu.addSeparator();
//				// Insert
//				menu.add(new AbstractAction("Insert") {
//					public void actionPerformed(ActionEvent ev) {
////						insert(pt);
//					}
//				});
				return null;
			}
			
		};
		bean.setStorableObject("User" + (++this.count));
		bean.setValidator(this.getValidator());
		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("UserBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getStorableObject() 
						+ " -> " 
						+ targetBean.getStorableObject());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getStorableObject().startsWith("User") &&
						targetBean.getStorableObject().startsWith("ARM");
				}
			};
		}
		return this.validator;
	}
	
}

