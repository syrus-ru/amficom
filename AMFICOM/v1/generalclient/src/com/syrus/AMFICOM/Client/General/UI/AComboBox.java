package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import javax.swing.*;

import java.util.Vector;

public class AComboBox extends JComboBox
{
	static {
		UIDefaults def = UIManager.getDefaults();
		def.put("ComboBox.background", def.get("window"));
		def.put("ComboBox.disabledBackground", def.get("window"));
	}

	{
		setPreferredSize(new Dimension(10, 21));
	}

	public static final int SMALL_FONT = 10;

	public AComboBox()
	{
		super();
	}

	public AComboBox(int fontsize)
	{
		super();

		setFontSize(fontsize);
	}

	public void setFontSize(int fontsize)
	{
		Font font = getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, fontsize));
	}

	public AComboBox(ComboBoxModel aModel)
	{
		super(aModel);
	}

	public AComboBox(final Object items[])
	{
		super(items);
	}

	public AComboBox(Vector items)
	{
		super(items);
	}

	class ComboBoxRenderer extends JLabel
			implements ListCellRenderer {
		Font font;
		public ComboBoxRenderer() {
			setOpaque(true);
			font = new Font("Dialog", Font.PLAIN, 11);
		}

	/*
		* This method finds the image and text corresponding
		* to the selected value and returns the label, set up
		* to display the text and image.
	 */
		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			//Get the selected index. (The index param isn't
			//always valid, so just use the value.)

			setFont(font);

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}


			setText(value.toString());
			return this;
		}
	}
}

