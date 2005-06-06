/**
 * $Id: TristateCheckBox.java,v 1.2 2005/06/06 12:20:36 krupenn Exp $ 
 * Syrus Systems 
 * Научно-технический центр 
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;
import java.awt.event.*;

/**
 * http://www.javaspecialists.co.za/archive/Issue082.html
 * 
 *  Maintenance tip - There were some tricks to getting this code
 *  working:
 *  
 *  1. You have to overwite addMouseListener() to do nothing
 *  2. You have to add a mouse event on mousePressed by calling
 *  super.addMouseListener()
 *  3. You have to replace the UIActionMap for the keyboard event
 *  &quot;pressed&quot; with your own one.
 *  4. You have to remove the UIActionMap for the keyboard event
 *  &quot;released&quot;.
 *  5. You have to grab focus when the next state is entered,
 *  otherwise clicking on the component won't get the focus.
 *  6. You have to make a TristateDecorator as a button model that
 *  wraps the original button model and does state management.
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
			public void mousePressed(MouseEvent e) {
				grabFocus();
				TristateCheckBox.this.model.nextState();
			}
		});
		// Reset the keyboard action map
		ActionMap map = new ActionMapUIResource();
		map.put("pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				grabFocus();
				TristateCheckBox.this.model.nextState();
			}
		});
		map.put("released", null);
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
	public void addMouseListener(MouseListener l) {
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
