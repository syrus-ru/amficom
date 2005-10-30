/*-
 * $$Id: MapNodeLinkSizeField.java,v 1.17 2005/10/30 15:20:33 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;

import javax.swing.JTextField;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.util.Log;

/**
 * Поле редактирования длины фрагмента линии. При отображении компонент
 * помещается на родительский компонент. При удачном редактировании
 * (пользователь нажал ENTER) меняет фрагмент линии в карте. При завершении
 * редактирования сам убирает себя из родительского компонента
 * 
 * @version $Revision: 1.17 $, $Date: 2005/10/30 15:20:33 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapNodeLinkSizeField extends JTextField {
	LogicalNetLayer logicalNetLayer;
	NodeLink nodeLink;
	AbstractNode node;

	public MapNodeLinkSizeField(
			LogicalNetLayer logicalNetLayer,
			NodeLink nodeLink,
			AbstractNode node) {
		super();
		this.logicalNetLayer = logicalNetLayer;
		this.nodeLink = nodeLink;
		this.node = node;
		jbInit();
		grabFocus();
	}

	public void jbInit() {
		this.setFont(MapPropertiesManager.getFont());
		this.setBackground(MapPropertiesManager.getTextBackground());
		this.setForeground(MapPropertiesManager.getTextColor());

		addKeyListener(new MapNodeLinkSizeField.MapNodeLinkSizeFieldKeyAdapter(
				this));
		addFocusListener(new MapNodeLinkSizeField.MapNodeLinkSizeFieldFocusAdapter(
				this));
	}

	class MapNodeLinkSizeFieldFocusAdapter extends FocusAdapter {
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeFieldFocusAdapter(MapNodeLinkSizeField adaptee) {
			this.adaptee = adaptee;
		}

		@Override
		public void focusGained(FocusEvent e) {
			// empty
		}

		@Override
		public void focusLost(FocusEvent e) {
			this.adaptee.setVisible(false);
			if(this.adaptee.getParent() != null)
				this.adaptee.getParent().remove(this.adaptee);
			this.adaptee.removeFocusListener(this);
		}
	}

	class MapNodeLinkSizeFieldKeyAdapter extends java.awt.event.KeyAdapter {
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeFieldKeyAdapter(MapNodeLinkSizeField adaptee) {
			this.adaptee = adaptee;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();

			if(code == KeyEvent.VK_ESCAPE) {
				this.adaptee.setVisible(false);
				if(this.adaptee.getParent() != null)
					this.adaptee.getParent().remove(this.adaptee);
				this.adaptee.removeKeyListener(this);
			}

			if(code == KeyEvent.VK_ENTER) {
				try {
					double dist = Double.parseDouble(this.adaptee.getText());
					this.adaptee.logicalNetLayer.setNodeLinkSizeFrom(
							this.adaptee.nodeLink,
							this.adaptee.node,
							dist);
					this.adaptee.setVisible(false);
					if(this.adaptee.getParent() != null)
						this.adaptee.getParent().remove(this.adaptee);
					this.adaptee.removeKeyListener(this);

					if(this.adaptee.logicalNetLayer != null) {
						this.adaptee.logicalNetLayer
								.sendMapEvent(MapEvent.NEED_REPAINT);
					}
				} catch(Exception ex) {
					assert Log.debugMessage("Illegal distance", Level.INFO); //$NON-NLS-1$
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// empty
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// empty
		}
	}

}
