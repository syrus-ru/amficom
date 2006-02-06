
package com.syrus.AMFICOM.client.UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/04 10:56:16 $
 * @author $Author: bob $
 * @module commonclient
 */
public class AComboBox extends JComboBox {
	private static final long serialVersionUID = -4432933469351433936L;

	public AComboBox() {
		super();
		// setPreferredSize(new Dimension(10, 21));
		this.actualResize();
	}

	public AComboBox(ComboBoxModel aModel) {
		super(aModel);
		this.actualResize();
	}

	public AComboBox(final int fontsize) {
		super();
		this.setFontSize(fontsize);
	}

	public AComboBox(final Object items[]) {
		super(items);
		this.actualResize();
	}

	public AComboBox(Vector items) {
		super(items);
		this.actualResize();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.actualResize();
	}

	public void setFontSize(int fontsize) {
		final Font font = getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, fontsize));
		this.actualResize();
	}

	@Override
	public void setModel(ComboBoxModel aModel) {
		super.setModel(aModel);
		this.actualResize();
	}

	private void actualResize() {
		final FontMetrics fm = this.getFontMetrics(UIManager.getFont("AComboBox.font"));
		if (fm != null) {
			final int height = fm.getHeight();
			int width = 0;
			final ComboBoxModel model = this.getModel();
			for (int i = 0; i < model.getSize(); i++) {
				String value = model.getElementAt(i).toString();
				int w = fm.stringWidth(value);
				// System.out.println("value:" + value + "\tw:" + w);
				width = (width > w) ? width : w;
			}
			width += fm.stringWidth("--");
			final Dimension d = new Dimension(width, height + 2);
			// this.setMinimumSize(d);
			this.setPreferredSize(d);
		}
	}
}
