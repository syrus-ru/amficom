/*
 * $Id: ADefaultTableCellRenderer.java,v 1.5 2005/03/23 08:41:07 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/23 08:41:07 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module generalclient_v1
 */
public class ADefaultTableCellRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 3832622897947948339L;
	/**
	 * Weight of color blending using alpha-channel
	 */
	public static final double					ALPHA				= 0.3;
	/**
	 * 1.0 - ALPHA constant. see {@link #ALPHA}
	 */
	public static final double					ONE_MINUS_ALPHA		= 1.0 - ALPHA;

	protected static Hashtable					renderers;

	private static ADefaultTableCellRenderer	instance;

	public ADefaultTableCellRenderer() {
		super.setOpaque(true);
		super.setBorder(UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
		this.initClassRenderers();
	}

	public static synchronized ADefaultTableCellRenderer getInstance() {
		if (instance == null)
			instance = new ADefaultTableCellRenderer();
		return instance;
	}

	/**
	 * This method is called each time a cell in a column using this renderer
	 * needs to be rendered.
	 */
	public Component getTableCellRendererComponent(	JTable table,
													Object value,
													boolean isSelected,
													boolean hasFocus,
													int rowIndex,
													int vColIndex) {
		Class clazz = value.getClass();
		TableCellRenderer cellRenderer = (TableCellRenderer) renderers.get(clazz);
		if (cellRenderer != null)
			return cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
		cellRenderer = (TableCellRenderer) renderers.get(clazz.getSuperclass());
		return cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
	}

	private synchronized void initClassRenderers() {
		if (renderers == null) {
			renderers = new UIDefaults();
			// Objects
			setLazyRenderer(Object.class, ObjectRenderer.class.getName());

			// Numbers
			setLazyRenderer(Number.class, NumberRenderer.class.getName());

			// Doubles and Floats
			setLazyRenderer(Float.class, DoubleRenderer.class.getName());
			setLazyRenderer(Double.class, DoubleRenderer.class.getName());

			// Dates
			setLazyRenderer(Date.class, DateRenderer.class.getName());

			// Icons and ImageIcons
			setLazyRenderer(Icon.class, IconRenderer.class.getName());
			setLazyRenderer(ImageIcon.class, IconRenderer.class.getName());

			setLazyRenderer(Color.class, ColorCellRenderer.class.getName(), "getInstance");

			// Booleans
			setLazyRenderer(Boolean.class, BooleanRenderer.class.getName());

		}
	}

	private void setLazyValue(	Hashtable h,
								Class c,
								String s) {
		h.put(c, new UIDefaults.ProxyLazyValue(s));
	}

	private void setLazyValue(	Hashtable h,
								Class c,
								String s,
								String m) {
		h.put(c, new UIDefaults.ProxyLazyValue(s, m));
	}

	private void setLazyRenderer(	Class c,
									String s) {
		setLazyValue(renderers, c, s);
	}

	private void setLazyRenderer(	Class c,
									String s,
									String m) {
		setLazyValue(renderers, c, s, m);
	}

	/**
	 * Default Renderers
	 */
	public static class ObjectRenderer extends JLabel implements TableCellRenderer {

		private Color	unselectedForeground;

		public ObjectRenderer() {
			System.out.println("ObjectRenderer | ");
			super.setOpaque(true);
			super.setBorder(UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
		}

		/**
		 * This method is called each time a cell in a column using this
		 * renderer needs to be rendered.
		 */
		public Component getTableCellRendererComponent(	JTable table,
														Object value,
														boolean isSelected,
														boolean hasFocus,
														int rowIndex,
														int vColIndex) {
			this.setValue(value);

			super.setBackground(table.getBackground());

			Color color = super.getBackground();

			if (isSelected) {
				this.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
						.getForeground());
				Font font = table.getFont();
				font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
				this.setFont(font);
				Color c = table.getSelectionBackground();
				// calculate color with alpha-channel weight alpha
				this
						.setBackground(new Color(
													(int) (c.getRed() * ONE_MINUS_ALPHA + ALPHA * color.getRed()) % 256,
													(int) (c.getGreen() * ONE_MINUS_ALPHA + ALPHA * color.getGreen()) % 256,
													(int) (c.getBlue() * ONE_MINUS_ALPHA + ALPHA * color.getBlue()) % 256));

			} else {
				this.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
						.getForeground());
				this.setFont(table.getFont());
				this.setBackground(color);
			}

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(rowIndex, vColIndex)) {
					this.setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
			}

			return this;
		}

		protected void setValue(Object value) {
			setText((value == null) ? "" : value.toString());
		}

		public void setBackground(Color c) {
			super.setBackground(c);
		}

		public void setForeground(Color c) {
			super.setForeground(c);
			this.unselectedForeground = c;
		}
	}

	public static class NumberRenderer extends ObjectRenderer {

		public NumberRenderer() {
			super();
			System.out.println("NumberRenderer | ");
			setHorizontalAlignment(SwingConstants.LEFT);
		}
	}

	public static class DoubleRenderer extends NumberRenderer {

		NumberFormat	formatter;

		public DoubleRenderer() {
			super();
			System.out.println("DoubleRenderer | ");
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = NumberFormat.getInstance();
			}
			setText((value == null) ? "" : formatter.format(value));
		}
	}

	public static class DateRenderer extends ObjectRenderer {

		DateFormat	formatter;

		public DateRenderer() {
			super();
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = DateFormat.getDateInstance();
			}
			setText((value == null) ? "" : formatter.format(value));
		}
	}

	public static class IconRenderer extends ObjectRenderer {

		public IconRenderer() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		public void setValue(Object value) {
			setIcon((value instanceof Icon) ? (Icon) value : null);
		}
	}

	public static class BooleanRenderer extends JCheckBox implements TableCellRenderer {

		public BooleanRenderer() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		public Component getTableCellRendererComponent(	JTable table,
														Object value,
														boolean isSelected,
														boolean hasFocus,
														int row,
														int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelected((value != null && ((Boolean) value).booleanValue()));
			return this;
		}
	}
}
