/*-
 * $Id: ColorChooserComboBox.java,v 1.2 2005/06/17 11:50:08 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/06/17 11:50:08 $
 * @module commonclient_v1
 */

public class ColorChooserComboBox extends JComboBox {
	private static final long serialVersionUID = 3258133565697832497L;

	JButton addColorButton;
	DefaultComboBoxModel model;
	Color selectedColor;
	
	public static Color[] DEFAULT_COLOR_SET = new Color[] { Color.WHITE, Color.BLUE,
		Color.GREEN, Color.RED, Color.GRAY, Color.CYAN, Color.MAGENTA,
		Color.ORANGE, Color.PINK, Color.YELLOW, Color.BLACK };
	
	public ColorChooserComboBox() {
		this(DEFAULT_COLOR_SET);
	}
	
	public ColorChooserComboBox(Color[] colors) {
		this.model = new DefaultComboBoxModel(colors);

		this.addColorButton = new JButton(LangModelGeneral.getString(ResourceKeys.I18N_CUSTOM));
		this.addColorButton.setHorizontalAlignment(SwingConstants.LEFT);
		this.addColorButton.setBorder(BorderFactory.createEmptyBorder());
		this.addColorButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(Environment.getActiveWindow(), LangModelGeneral
						.getString(ResourceKeys.I18N_CHOOSE_COLOR), ColorChooserComboBox.this.selectedColor);

				if (newColor != null) {
					addItem(newColor);
					ColorChooserComboBox.this.selectedColor = newColor;
				}
				setSelectedItem(ColorChooserComboBox.this.selectedColor);
			}
		});
		this.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object selected = getSelectedItem();
				if (selected instanceof JButton) {
					((JButton) selected).doClick();
				} else {
					ColorChooserComboBox.this.selectedColor = (Color) selected;
				}
			}
		});
		this.model.addElement(this.addColorButton);
		setModel(this.model);
		setRenderer(ColorListCellRenderer.getInstance());
		this.selectedColor = (Color) getSelectedItem();
	}
	
	public void addItem(Object newColor) {
		if (newColor instanceof Color) {
			Color color = (Color)newColor;
			if (!conatainsColor(color)) {
				this.model.insertElementAt(color, getItemCount() > 0 ? getItemCount() - 1 : 0);
			} else {
				setSelectedItem(color);
			}
		}
	}
	
	protected boolean conatainsColor(Color color) {
		for (int i = 0; i < this.model.getSize(); i++)
			if (this.model.getElementAt(i).equals(color))
				return true;
		return false;
	}
}
