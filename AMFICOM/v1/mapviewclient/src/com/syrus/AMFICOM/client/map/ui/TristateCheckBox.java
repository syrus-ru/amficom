/*-
 * $$Id: TristateCheckBox.java,v 1.7 2005/09/30 16:08:42 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

/**
 * http://www.javaspecialists.co.za/archive/Issue082.html
 * 
 * @version $Revision: 1.7 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class TristateCheckBox extends JCheckBox {
	/** This is a type-safe enumerated type */
	public static class State {
		State() {
			// empty
		}
	}

	public static final State NOT_SELECTED = new State();

	public static final State SELECTED = new State();

	public static final State DONT_CARE = new State();

	final TristateDecorator model;

	public TristateCheckBox(String text, Icon icon, State initial) {
		super(text, icon);
		// Add a listener for when the mouse is pressed
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				grabFocus();
				TristateCheckBox.this.model.nextState();
			}
		});
		// Reset the keyboard action map
		ActionMap map = new ActionMapUIResource();
		map.put("pressed", new AbstractAction() { //$NON-NLS-1$
			public void actionPerformed(ActionEvent e) {
				grabFocus();
				TristateCheckBox.this.model.nextState();
			}
		});
		map.put("released", null); //$NON-NLS-1$
		SwingUtilities.replaceUIActionMap(this, map);
		// set the model to the adapted model
		this.model = new TristateDecorator(getModel());
		setModel(this.model);
		setState(initial);
	}

	public TristateCheckBox(String text, State initial) {
		this(text, null, initial);
	}

	public TristateCheckBox(String text) {
		this(text, DONT_CARE);
	}

	public TristateCheckBox() {
		this(null);
	}

	/** No one may add mouse listeners, not even Swing! */
	@Override
	public void addMouseListener(MouseListener l) {
		// empty
	}

	/**
	 * Set the new state to either SELECTED, NOT_SELECTED or DONT_CARE. 
	 * If state == null, it is treated as DONT_CARE.
	 */
	public void setState(State state) {
		this.model.setState(state);
	}

	/**
	 * Return the current state, which is determined by the selection status of
	 * the model.
	 */
	public State getState() {
		return this.model.getState();
	}

	@Override
	public void setSelected(boolean b) {
		if(b) {
			setState(SELECTED);
		}
		else {
			setState(NOT_SELECTED);
		}
	}

	/**
	 * Exactly which Design Pattern is this? Is it an Adapter, a Proxy or a
	 * Decorator? In this case, my vote lies with the Decorator, because we are
	 * extending functionality and "decorating" the original model with a more
	 * powerful model.
	 */
	private class TristateDecorator implements ButtonModel {
		private final ButtonModel other;

		private TristateDecorator(ButtonModel other) {
			this.other = other;
		}

		void setState(State state) {
			if(state == NOT_SELECTED) {
				this.other.setArmed(false);
				setPressed(false);
				setSelected(false);
			}
			else
				if(state == SELECTED) {
					this.other.setArmed(false);
					setPressed(false);
					setSelected(true);
				}
				else { // either "null" or DONT_CARE
					this.other.setArmed(true);
					setPressed(true);
					setSelected(true);
				}
		}

		/**
		 *  The current state is embedded in the selection / armed
		 *  state of the model.
		 *  
		 *  We return the SELECTED state when the checkbox is selected
		 *  but not armed, DONT_CARE state when the checkbox is
		 *  selected and armed (grey) and NOT_SELECTED when the
		 *  checkbox is deselected.
		 */
		State getState() {
			if(isSelected() && !isArmed()) {
				// normal black tick
				return SELECTED;
			}
			else
				if(isSelected() && isArmed()) {
					// don't care grey tick
					return DONT_CARE;
				}
				else {
					// normal deselected
					return NOT_SELECTED;
				}
		}

		/** We rotate between NOT_SELECTED, SELECTED and DONT_CARE. */
		void nextState() {
			State current = getState();
			if(current == NOT_SELECTED) {
				setState(SELECTED);
			}
			else
				if(current == SELECTED) {
					setState(DONT_CARE);
				}
				else
					if(current == DONT_CARE) {
						setState(NOT_SELECTED);
					}
		}

		/** Filter: No one may change the armed status except us. */
		public void setArmed(boolean b) {
			// empty
		}

		/**
		 * We disable focusing on the component when it is not enabled.
		 */
		public void setEnabled(boolean b) {
			setFocusable(b);
			this.other.setEnabled(b);
		}

		/**
		 * All these methods simply delegate to the "other" model that is being
		 * decorated.
		 */
		public boolean isArmed() {
			return this.other.isArmed();
		}

		public boolean isSelected() {
			return this.other.isSelected();
		}

		public boolean isEnabled() {
			return this.other.isEnabled();
		}

		public boolean isPressed() {
			return this.other.isPressed();
		}

		public boolean isRollover() {
			return this.other.isRollover();
		}

		public void setSelected(boolean b) {
			this.other.setSelected(b);
		}

		public void setPressed(boolean b) {
			this.other.setPressed(b);
		}

		public void setRollover(boolean b) {
			this.other.setRollover(b);
		}

		public void setMnemonic(int key) {
			this.other.setMnemonic(key);
		}

		public int getMnemonic() {
			return this.other.getMnemonic();
		}

		public void setActionCommand(String s) {
			this.other.setActionCommand(s);
		}

		public String getActionCommand() {
			return this.other.getActionCommand();
		}

		public void setGroup(ButtonGroup group) {
			this.other.setGroup(group);
		}

		public void addActionListener(ActionListener l) {
			this.other.addActionListener(l);
		}

		public void removeActionListener(ActionListener l) {
			this.other.removeActionListener(l);
		}

		public void addItemListener(ItemListener l) {
			this.other.addItemListener(l);
		}

		public void removeItemListener(ItemListener l) {
			this.other.removeItemListener(l);
		}

		public void addChangeListener(ChangeListener l) {
			this.other.addChangeListener(l);
		}

		public void removeChangeListener(ChangeListener l) {
			this.other.removeChangeListener(l);
		}

		public Object[] getSelectedObjects() {
			return this.other.getSelectedObjects();
		}
	}
}
