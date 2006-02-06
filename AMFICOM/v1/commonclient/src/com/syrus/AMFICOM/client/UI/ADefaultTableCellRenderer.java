/*
 * $Id: ADefaultTableCellRenderer.java,v 1.9 2006/01/16 12:06:03 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

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

import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ErrorMessages;

/**
 * @version $Revision: 1.9 $, $Date: 2006/01/16 12:06:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class ADefaultTableCellRenderer implements TableCellRenderer {

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
		this.initClassRenderers();
	}

	public static synchronized ADefaultTableCellRenderer getInstance() {
		if (instance == null) {
			instance = new ADefaultTableCellRenderer();
		}
		return instance;
	}
	
	/**
	 * add custom renderer for object of Clazz clazz
	 * 
	 * @param clazz
	 * @param cellRenderer
	 */
	public void addCustomRenderer(final Class clazz, final TableCellRenderer cellRenderer) {
		renderers.put(clazz, cellRenderer);
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
		if (value == null) {
			return null;
		}
		assert value != null : ErrorMessages.NON_NULL_EXPECTED;
		final Class clazz = value.getClass();
		TableCellRenderer cellRenderer = (TableCellRenderer) renderers.get(clazz);
		if (cellRenderer != null) {
			return cellRenderer.getTableCellRendererComponent(table, 
				value, 
				isSelected, 
				hasFocus, 
				rowIndex, 
				vColIndex);
		}
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

	@SuppressWarnings("unchecked")
	private void setLazyValue(	Hashtable h,
								Class c,
								String s) {
		h.put(c, new UIDefaults.ProxyLazyValue(s));
	}

	@SuppressWarnings("unchecked")
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
	public static class ObjectRenderer implements TableCellRenderer {

		private static final long	serialVersionUID	= 3257007670052335929L;
		
		private Color	unselectedForeground;
		
		private static boolean usingItalic = false;

		protected JLabel label;
		
		public ObjectRenderer() {
			this.label = new JLabel();
			this.label.setOpaque(true);
			this.label.setBorder(UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
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

			this.label.setBackground(table.getBackground());

			Color color = this.label.getBackground();

			if (isSelected) {
				this.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
						.getForeground());
				final Font font = UIManager.getFont("Table.selectedFont");
				if (usingItalic && font != null ) {
					this.label.setFont(font);
				}
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
				this.label.setFont(table.getFont());
				this.setBackground(color);
			}

			if (hasFocus) {
				this.label.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(rowIndex, vColIndex)) {
					this.setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				this.label.setBorder(UIManager.getBorder(ResourceKeys.TABLE_NO_FOCUS_BORDER));
			}

			return this.label;
		}

		protected void setValue(Object value) {
			this.label.setText((value == null) ? "" : value.toString());
		}

		public void setBackground(Color c) {
			this.label.setBackground(c);
		}

		public void setForeground(Color c) {
			this.label.setForeground(c);
			this.unselectedForeground = c;
		}
		
		public static boolean isUsingItalic() {
			return usingItalic;
		}
		
		public static void setUsingItalic(boolean italic) {
			usingItalic = italic;
		}
	}

	public static class NumberRenderer extends ObjectRenderer {

		private static final long	serialVersionUID	= 4122257333184640821L;

		public NumberRenderer() {
			super.label.setHorizontalAlignment(SwingConstants.LEFT);
		}
	}

	public static class DoubleRenderer extends NumberRenderer {

		private static final long	serialVersionUID	= 3256719567941023285L;

		NumberFormat	formatter;

		public DoubleRenderer() {
			super();
		}

		@Override
		public void setValue(Object value) {
			if (this.formatter == null) {
				this.formatter = NumberFormat.getInstance();
			}
			super.label.setText((value == null) ? "" : this.formatter.format(value));
		}
	}

	public static class DateRenderer extends ObjectRenderer {

		private static final long	serialVersionUID	= 3545516226924066360L;
		private final DateFormat	formatter;

		public DateRenderer() {
			this(DateFormat.getDateInstance());
		}

		public DateRenderer(final DateFormat formatter) {
			super();
			this.formatter = formatter;
		}
		
		@Override
		public void setValue(Object value) {
			super.label.setText((value == null) ? "" : this.formatter.format(value));
		}
	}

	public static class IconRenderer extends ObjectRenderer {

		private static final long	serialVersionUID	= 3257003237696420146L;

		public IconRenderer() {
			super();
			super.label.setHorizontalAlignment(SwingConstants.CENTER);
		}

		@Override
		public void setValue(Object value) {
			super.label.setIcon((value instanceof Icon) ? (Icon) value : null);
		}
	}

	public static class BooleanRenderer extends JCheckBox implements TableCellRenderer {

		private static final long	serialVersionUID	= 3258132461891563831L;

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
