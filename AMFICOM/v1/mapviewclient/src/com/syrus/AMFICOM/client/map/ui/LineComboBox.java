/*-
 * $$Id: LineComboBox.java,v 1.8 2005/10/02 13:07:10 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.syrus.AMFICOM.client.UI.AComboBox;

/**
 * @version $Revision: 1.8 $, $Date: 2005/10/02 13:07:10 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class LineComboBox extends AComboBox {
	public static LineStyleHolder[] presetLines;
	static int lineWidth = 3;

	static {
		presetLines = new LineStyleHolder[5];
		presetLines[0] = new LineStyleHolder(
						new BasicStroke(lineWidth),
						"Solid line"); //$NON-NLS-1$
		presetLines[1] = new LineStyleHolder(
				new BasicStroke(
						lineWidth,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL,
						(float) 0.0,
						new float[] { 10, 3 },
						(float) 0.0), 
				"Dash line 1"); //$NON-NLS-1$
		presetLines[2] = new LineStyleHolder(
				new BasicStroke(
						lineWidth,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL,
						(float) 0.0,
						new float[] { 3, 10 },
						(float) 0.0), 
				"Dash line 2"); //$NON-NLS-1$
		presetLines[3] = new LineStyleHolder(
				new BasicStroke(
						lineWidth,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL,
						(float) 0.0,
						new float[] { 10, 3, 10 },
						(float) 0.0), 
				"Dash line 3"); //$NON-NLS-1$
		presetLines[4] = new LineStyleHolder(
				new BasicStroke(
						lineWidth,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL,
						(float) 0.0,
						new float[] { 10, 10, 3, 10 },
						(float) 0.0), 
				"Dash line 4"); //$NON-NLS-1$
	}

	public BasicStroke returnStroke;
	int prevComboBox = 0;

	public LineStyleHolder returnLineStyleHolder = new LineStyleHolder();
	LineStyleHolder[] lines;

	class ComboBoxRenderer extends JPanel implements ListCellRenderer {
		public JLabel lbl = new JLabel();
		public Drawfield a1 = new Drawfield();

		JComboBox jComboBox = new JComboBox();

		public ComboBoxRenderer(JComboBox Box) {
			setOpaque(true);
			this.jComboBox = Box;
			this.lbl.setFont(Box.getFont());
		}

		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			if(isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			LineStyleHolder myLine = (LineStyleHolder) value;
			LineComboBox.this.returnLineStyleHolder.setLineStyleHolder(
					((LineStyleHolder) value).basicStroke,
					((LineStyleHolder) value).text);

			if(isSelected) {
				this.lbl.setForeground(Color.white);
			} else {
				this.lbl.setForeground(Color.black);
			}

			this.a1.initDrawfield(
					0,
					this.jComboBox.getHeight() / 2,
					this.jComboBox.getWidth() / 2,
					this.jComboBox.getHeight() / 2,
					Color.red,
					myLine.basicStroke);

			this.a1.setBounds(new Rectangle(
					0,
					0,
					this.jComboBox.getWidth() / 2,
					this.jComboBox.getHeight()));

			this.lbl.setBounds(new Rectangle(
					this.jComboBox.getWidth() / 2,
					0,
					this.jComboBox.getWidth(),
					this.jComboBox.getHeight()));

			this.setLayout(null);

			LineComboBox.this.returnStroke = myLine.basicStroke;
			this.lbl.setText(myLine.text);
			this.add(this.a1);
			this.add(this.lbl);

			return this;
		}

		class Drawfield extends JLabel {
			int startX;
			int startY;
			int endX;
			int endY;
			Color color;
			BasicStroke basicStroke;

			public Drawfield() {
				super();
			}

			public void initDrawfield(
					int startx,
					int starty,
					int endx,
					int endy,
					Color col,
					BasicStroke bas) {
				this.startX = startx;
				this.startY = starty;
				this.endX = endx;
				this.endY = endy;
				this.color = col;
				this.basicStroke = bas;
			}

			@Override
			public void paint(Graphics g) {
				Graphics2D p = (Graphics2D) g;

				Stroke bs = p.getStroke();
				p.setStroke(this.basicStroke);

				p.setColor(this.color);
				p.drawLine(this.startX, this.startY, this.endX, this.endY);
				p.setStroke(bs);
			}
		}
	}

	public LineComboBox() {
		// todo set font size
		super();

		this.returnLineStyleHolder.setLineStyleHolder(
				presetLines[0].basicStroke,
				presetLines[0].text);
		this.returnStroke = presetLines[0].basicStroke;

		setModel(new DefaultComboBoxModel(presetLines));

		this.setBounds(new Rectangle(0, 0, 20, 20));

		this.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				this_itemStateChanged(e);
			}
		});
		ComboBoxRenderer renderer = new ComboBoxRenderer(this);
		renderer.setPreferredSize(new Dimension(this.getWidth(), this
				.getHeight()));
		this.setRenderer(renderer);
		this.setMaximumRowCount(5);
	}

	void this_itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED)
			if(this.getSelectedItem() instanceof LineStyleHolder) {
				LineStyleHolder ml = (LineStyleHolder) this.getSelectedItem();
				this.returnLineStyleHolder.setLineStyleHolder(ml.basicStroke, ml.text);
				this.returnStroke = ml.basicStroke;
			}
	}

	void this_actionPerformed(ActionEvent e) {
		this.returnLineStyleHolder.setLineStyleHolder(
				((LineStyleHolder) this.getSelectedItem()).basicStroke,
				((LineStyleHolder) this.getSelectedItem()).text);
		this.returnStroke = ((LineStyleHolder) this.getSelectedItem()).basicStroke;
	}

	void this_mousePressed(MouseEvent e) {
		this.returnLineStyleHolder.setLineStyleHolder(
				((LineStyleHolder) this.getSelectedItem()).basicStroke,
				((LineStyleHolder) this.getSelectedItem()).text);
		this.returnStroke = ((LineStyleHolder) this.getSelectedItem()).basicStroke;
	}

	public BasicStroke getSelectedStroke() {
		this.returnStroke = ((LineStyleHolder) this.getSelectedItem()).basicStroke;
		return this.returnStroke;
	}

	public void setLineSize(int i) {
		lineWidth = i;
	}

	public int getLineSize() {
		return lineWidth;
	}

	public LineStyleHolder getReturnLineStyleHolder() {
		this.returnLineStyleHolder.setLineStyleHolder(
				((LineStyleHolder) this.getSelectedItem()).basicStroke,
				((LineStyleHolder) this.getSelectedItem()).text);
		this.returnStroke = ((LineStyleHolder) this.getSelectedItem()).basicStroke;
		return this.returnLineStyleHolder;
	}

	public void setMyLine(LineStyleHolder myLine) {
		this.returnLineStyleHolder = myLine;
	}

	public Object getSelected() {
		return getReturnLineStyleHolder();// .text;
		// return getSelectedStroke();
	}

	public void setSelected(Object obj) {
		String str = (String) obj;
		for(int i = 0; i < presetLines.length; i++) {
			LineStyleHolder line = presetLines[i];
			if(line.text.equals(str)) {
				setSelectedItem(line);
				return;
			}
		}
	}

	public static BasicStroke getStrokeByType(String str) {
		for(int i = 0; i < presetLines.length; i++) {
			LineStyleHolder line = presetLines[i];
			if(line.text.equals(str)) {
				return line.basicStroke;
			}
		}
		return new BasicStroke(2);
	}

}
class LineStyleHolder {
	public BasicStroke basicStroke;
	public String text;

	public LineStyleHolder(BasicStroke col, String tx) {
		this.basicStroke = col;
		this.text = tx;
	}

	public LineStyleHolder() {
		this.basicStroke = new BasicStroke(2);
		this.text = "Solid line"; //$NON-NLS-1$
	}

	public void setLineStyleHolder(BasicStroke col, String tx) {
		this.basicStroke = col;
		this.text = tx;
	}

	@Override
	public String toString() {
		return this.text;
	}
}

