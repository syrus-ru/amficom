package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;

import javax.swing.*;

import java.util.Vector;

public class AComboBox extends JComboBox {

	class ComboBoxRenderer extends JLabel implements ListCellRenderer {

		Font	font;

		public ComboBoxRenderer() {
			setOpaque(true);
			font = UIGeneralStorage.DIALOG_FONT;
		}

		public Font getFont() {
			return font;
		}

		/*
		 * This method finds the image and text corresponding to the selected
		 * value and returns the label, set up to display the text and image.
		 */
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			//Get the selected index. (The index param isn't
			//always valid, so just use the value.)

			this.setFont(font);

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

	public static final int	SMALL_FONT	= 10;

	static {
		UIDefaults def = UIManager.getDefaults();
		def.put("ComboBox.background", def.get("window"));
		def.put("ComboBox.disabledBackground", def.get("window"));
	}

	public AComboBox() {
		super();
		//setPreferredSize(new Dimension(10, 21));
		actualResize();
	}

	public AComboBox(ComboBoxModel aModel) {
		super(aModel);
		actualResize();
	}

	public AComboBox(int fontsize) {
		super();
		setFontSize(fontsize);
	}

	public AComboBox(final Object items[]) {
		super(items);
		actualResize();
	}

	public AComboBox(Vector items) {
		super(items);
		actualResize();
	}

	public void setFont(Font font) {
		super.setFont(font);
		actualResize();
	}

	public void setFontSize(int fontsize) {
		Font font = getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, fontsize));
		actualResize();
	}

	public void setModel(ComboBoxModel aModel) {
		super.setModel(aModel);
		actualResize();
	}

	private void actualResize() {
		ComboBoxRenderer renderer = null;
		if (this.getRenderer() instanceof ComboBoxRenderer)
				renderer = ((ComboBoxRenderer) this.getRenderer());
		Font font = (renderer == null ? this.getFont() : renderer.getFont());
		if (font != null) {
			//System.out.println("font:" + font.toString());
			FontMetrics fm = this.getFontMetrics(font);
			if (fm != null) {
				int height = fm.getHeight();
				int width = 0;
				ComboBoxModel model = this.getModel();
				for (int i = 0; i < model.getSize(); i++) {
					String value = model.getElementAt(i).toString();
					int w = fm.stringWidth(value);
					//System.out.println("value:" + value + "\tw:" + w);
					width = (width > w) ? width : w;
				}
				width += fm.stringWidth("--");
				Dimension d = new Dimension(width, height + 2);
				//this.setMinimumSize(d);
				this.setPreferredSize(d);
			}
		}
	}
}

