/*-
 * $Id: UserBean.java,v 1.3 2005/07/25 05:58:53 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.3 $, $Date: 2005/07/25 05:58:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBean extends AbstractBean {

	List<String>	names;
	
	private List<PropertyChangeListener>						propertyChangeListeners;

	private String	fullName;

	private String	nature;
	
	WrapperedPropertyTable table;

	UserBean(List<String> names) {
		this.names = names;
	}

	@Override
	public void updateEdgeAttributes(	DefaultEdge edge,
										MPort port) {
		AttributeMap attributes = edge.getAttributes();
		GraphConstants.setLineWidth(attributes, 10.0f);
		GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_TECHNICAL);
		GraphConstants.setLineColor(attributes, Color.LIGHT_GRAY);
		GraphConstants.setForeground(attributes, Color.BLACK);
	}

	@Override
	public JPopupMenu getMenu(	final JGraphText graphText,
								final Object cell) {

		if (cell != null) {
			JPopupMenu popupMenu = new JPopupMenu();
			String lastName = null;
			for (final String name1 : this.names) {
				if (name1 != null) {
					popupMenu.add(new AbstractAction(name1) {

						public void actionPerformed(ActionEvent e) {
							UserBean.this.setNature(name1);
							AttributeMap attributeMap = new AttributeMap();
							GraphConstants.setValue(attributeMap, name1);
							Map viewMap = new Hashtable();
							viewMap.put(cell, attributeMap);
							graphText.getGraph().getModel().edit(viewMap, null, null, null);
						}
					});
				} else {
					popupMenu.addSeparator();
				}

				lastName = name1;
			}

			if (lastName != null) {
				popupMenu.addSeparator();
			}

			popupMenu.add(new AbstractAction(LangModelManager
					.getString("Entity.User.new")
					+ "...") {

				public void actionPerformed(ActionEvent e) {
					String string = JOptionPane.showInputDialog(null,
						LangModelManager.getString("Dialog.Add.User"),
						LangModelManager.getString("Entity.User.new"),
						JOptionPane.OK_CANCEL_OPTION);
					if (string == null) { return; }
					UserBean.this.names.add(string);
					UserBean.this.setNature(string);
					AttributeMap attributeMap = new AttributeMap();
					GraphConstants.setValue(attributeMap, string);
					Map viewMap = new Hashtable();
					viewMap.put(cell, attributeMap);
					graphText.getGraph().getModel().edit(viewMap, null, null, null);
				}
			});
			return popupMenu;
		}

		return null;
	}

	protected final String getFullName() {
		return this.fullName;
	}

	protected final void setFullName(final String fullName) {
		this.fullName = fullName;
		if (this.fullName != fullName &&
				(this.fullName != null && !this.fullName.equals(fullName) ||
				!fullName.equals(this.fullName))) {
			String oldValue = this.fullName;
			this.fullName = fullName;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_FULL_NAME, oldValue, fullName));
		}	
	}

	protected final String getNature() {
		return this.nature;
	}

	protected final void setNature(final String nature) {
		if (this.nature != nature &&
				(this.nature != null && !this.nature.equals(nature) ||
				!nature.equals(this.nature))) {
			String oldValue = this.name;
			this.nature = nature;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.KEY_USER_NATURE, oldValue, nature));
		}		
	}
	
	private void firePropertyChangeEvent(PropertyChangeEvent event) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.isEmpty()) {
			for (PropertyChangeListener listener : this.propertyChangeListeners) {
				listener.propertyChange(event);
			}
		}
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new LinkedList<PropertyChangeListener>();
		}
		if (!this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.add(propertyChangeListener);
		}
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.remove(propertyChangeListener);
		}
	}
	
	@Override
	public JPanel getPropertyPanel() {
		WrapperedPropertyTableModel model = 
			(WrapperedPropertyTableModel) this.table.getModel();
		model.setObject(this);
		return super.getPropertyPanel();
	}

}
