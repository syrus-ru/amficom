/*-
* $Id: WrapperedTable.java,v 1.25 2005/10/30 14:48:51 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.syrus.util.Log;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.25 $, $Date: 2005/10/30 14:48:51 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class WrapperedTable<T> extends ATable {

	private static final long	serialVersionUID	= -437251205606073016L;
	
	private boolean allowSorting = true;
	private boolean allowAutoResize = false;

	private int	sortedColumnIndex;

	private MouseListener	mouseListener;

	public WrapperedTable(final Wrapper<T> controller, final List<T> objectResourceList, final String[] keys) {
		this(new WrapperedTableModel<T>(controller, objectResourceList, keys));
	}
	
	public WrapperedTable(final Wrapper<T> controller, final String[] keys) {
		this(new WrapperedTableModel<T>(controller, keys));
	}

	public WrapperedTable(final WrapperedTableModel<T> dm) {
		super(dm);
		this.initialization();
	}

	@Override
	public WrapperedTableModel<T> getModel() {
		return (WrapperedTableModel<T>) super.getModel();
	}

	public void setDefaultTableCellRenderer() {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			TableCellRenderer renderer = StubLabelCellRenderer.getInstance();
			final TableColumn col = this.getColumnModel().getColumn(mColIndex);
			final Class clazz = model.wrapper.getPropertyClass(model.keys[mColIndex]);
			if (clazz.equals(Boolean.class)) {
				renderer = null;
			}
			else if (clazz.equals(Color.class)) {
				renderer = ColorCellRenderer.getInstance();
			}

			col.setCellRenderer(renderer);
		}
	}
	
	public void setEditor(final TableCellEditor editor, final String key) {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			if (model.keys[mColIndex].equals(key)) {
				final TableColumn col = this.getColumnModel().getColumn(mColIndex);
				col.setCellEditor(editor);
			}
		}
	}

	/**
	 * set custom renderer
	 * @param renderer
	 * @param key see {@link Wrapper#getKeys()}
	 */
	public void setRenderer(final TableCellRenderer renderer, final String key) {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			if (model.keys[mColIndex].equals(key)) {
				final TableColumn col = this.getColumnModel().getColumn(mColIndex);
				col.setCellRenderer(renderer);
			}
		}
	}

	public void addSelectedValue(final T t) {
		if (t == null) {
			return;
		}
		
		final WrapperedTableModel<T> tableModel = this.getModel();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			final T tInTable = tableModel.getObject(i);
			if (tInTable.equals(t)) {
				this.addRowSelectionInterval(i, i);
				break;
			}
		}
	}

	public void addSelectedValues(final Set<T> ts) {
		if (ts == null) {
			return;
		}
		
		this.selectIndices(this.indexOfTs(ts), true);	
	}
	
	
	public void setSelectedValue(final T t) {		
		if (t == null) {
			this.clearSelection();
			return;
		}
		
		final WrapperedTableModel<T> tableModel = this.getModel();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			final T tInTable = tableModel.getObject(i);
			if (tInTable.equals(t)) {
				this.setRowSelectionInterval(i, i);
				break;
			}
		}
	}

	public void setSelectedValues(final Set<T> ts) {		
		if (ts == null) {
			this.clearSelection();
			return;
		}
		
		this.selectIndices(this.indexOfTs(ts), false);		
	}
	
	private void selectIndices(final int[] tsIndices,
	                           final boolean addSelection) {
		if (tsIndices.length == 0) {
			return;
		}
		
		int prevIndex = tsIndices[0];
		for (int i=1; i < tsIndices.length; i++) {
			if (tsIndices[i] - prevIndex > 1) {
				if (addSelection) {
					assert Log.debugMessage("add selection "
							+ prevIndex + " .. " + tsIndices[i - 1],
						Log.DEBUGLEVEL09);
					this.addRowSelectionInterval(prevIndex, tsIndices[i - 1]);
				} else {
					assert Log.debugMessage("set selection "
						+ prevIndex + " .. " + tsIndices[i - 1],
					Log.DEBUGLEVEL09);
					this.setRowSelectionInterval(prevIndex, tsIndices[i - 1]);
				}
				prevIndex = tsIndices[i]; 
			}
		}
		if (addSelection) {
			assert Log.debugMessage("add selection "
				+ prevIndex + " .. " + tsIndices[tsIndices.length - 1],
			Log.DEBUGLEVEL09);
			this.addRowSelectionInterval(prevIndex, tsIndices[tsIndices.length - 1]);
		} else {
			assert Log.debugMessage("set selection "
				+ prevIndex + " .. " + tsIndices[tsIndices.length - 1],
			Log.DEBUGLEVEL09);
			this.setRowSelectionInterval(prevIndex, tsIndices[tsIndices.length - 1]);
		}
	}
	
	private int[] indexOfTs(final Set<T> ts) {
		if (ts.isEmpty()) {
			return new int[] {};
		}
		
		final int[] tmpIndices = new int[ts.size()];
		int index = 0;
		final WrapperedTableModel<T> tableModel = this.getModel();
		for(final T t : ts) {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				final T tInTable = tableModel.getObject(i);
				if (tInTable.equals(t)) {
					tmpIndices[index++] = i;
					break;
				}
			}
		}
		
		final int[] indices;
		if (index == ts.size()) {
			indices = tmpIndices;
		} else {
			assert Log.debugMessage("allocate size:" 
					+ tmpIndices.length 
					+ ", fill only first " 
					+ index 
					+ " elements", 
				Log.DEBUGLEVEL09);
			indices = new int[index];
			System.arraycopy(tmpIndices, 0, indices, 0, index);
		}

		Arrays.sort(indices);
		
		return indices;
	}

	public T getSelectedValue(){
		final int selectedRow = this.getSelectedRow();
		
		if (selectedRow == -1) {
			return null;
		}
		
		final WrapperedTableModel<T> tableModel = this.getModel();
		return tableModel.getObject(selectedRow);
	}
	
	public Set<T> getSelectedValues(){
		final int[] selectedRows = this.getSelectedRows();
		
		if (selectedRows.length == 0) {
			return Collections.emptySet();
		}
		
		final WrapperedTableModel<T> tableModel = this.getModel();
		final Set<T> selectedTs = new HashSet<T>(selectedRows.length);
		for (final int index : selectedRows) {
			selectedTs.add(tableModel.getObject(index));
		}
		return selectedTs;
	}
	
	@Override
	public void setTableHeader(final JTableHeader tableHeader) {
		
		if (this.mouseListener == null) {
			this.mouseListener = new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent evt) {
					if (!WrapperedTable.this.isAllowSorting()) {
						return;
					}
					
					final JTableHeader header = (JTableHeader) evt.getSource();
					final JTable table = header.getTable();
					final TableColumnModel colModel = table.getColumnModel();

					// The index of the column whose header was
					// clicked
					final int columnIndex = colModel.getColumnIndexAtX(evt.getX());
					final int mColIndex = table.convertColumnIndexToModel(columnIndex);

					WrapperedTable.this.sortColumn(mColIndex);

					// Return if not clicked on any column header
					if (columnIndex == -1) {
						return;
					}

					// Determine if mouse was clicked between column
					// heads
					final Rectangle headerRect = table.getTableHeader().getHeaderRect(columnIndex);
					if (columnIndex == 0) {
						headerRect.width -= 3; // Hard-coded
						// constant
					} else {
						headerRect.grow(-3, 0); // Hard-coded
						// constant
					}
					if (!headerRect.contains(evt.getX(), evt.getY())) {
						// Mouse was clicked between column
						// heads
						// vColIndex is the column head closest
						// to the click

						// vLeftColIndex is the column head to
						// the left of the
						// click
						int vLeftColIndex = columnIndex;
						if (evt.getX() < headerRect.x) {
							vLeftColIndex--;
						}
					}
				}
			};
		}

		if (this.tableHeader != null) {
            this.tableHeader.removeMouseListener(this.mouseListener);
            TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
            if (defaultRenderer instanceof SortableHeaderRenderer) {
                this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
            }
        }
		
		super.setTableHeader(tableHeader);
		
        if (tableHeader != null) {
            tableHeader.addMouseListener(this.mouseListener);
            tableHeader.setDefaultRenderer(
                    new SortableHeaderRenderer<T>(tableHeader.getDefaultRenderer()));
        }
        
    }
	
	public void setAllowSorting(final boolean allowSorting) {
		this.allowSorting = allowSorting;
	}
	
	public boolean isAllowSorting() {
		return this.allowSorting;
	}
	
	public final boolean isAllowAutoResize() {
		return this.allowAutoResize;
	}
	
	public final void setAllowAutoResize(boolean allowAutoResize) {
		this.allowAutoResize = allowAutoResize;
	}
	
	@Override
	protected void resizeAndRepaint() {
		if (this.allowAutoResize) {
			final TableCellRenderer headerRenderer =
	            this.tableHeader != null ? this.tableHeader.getDefaultRenderer() : null;
			
	            
	            SwingUtilities.invokeLater(new Runnable() {
	            	
	            	public void run() {
	            		final WrapperedTableModel<T> model = getModel();
	        	        for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
	        	        	final TableColumn column = getColumnModel().getColumn(columnIndex);
	        	
	        	        	Component comp = headerRenderer != null ?
	        	        			headerRenderer.getTableCellRendererComponent(
	        	                                 null, column.getHeaderValue(),
	        	                                 false, false, 0, 0) : 
	        	                    null;
	        	            final int headerWidth = comp != null ? comp.getPreferredSize().width : 0;
	        	            
	        	            int cellWidth = 0;
	        	            for(int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
	        		            final Object valueAt = model.getValueAt(rowIndex, columnIndex);
	        					comp = getCellRenderer(rowIndex, columnIndex).
	        		                             getTableCellRendererComponent(
	        		                                 WrapperedTable.this, valueAt,
	        		                                 false, false, rowIndex, columnIndex);
	        		            cellWidth = Math.max(comp.getPreferredSize().width, cellWidth);
	        	            }
	        	
	        	            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
	        	        }
	            	}
	            });
		    
		}
		
		super.resizeAndRepaint();
	}
	
	@SuppressWarnings("unchecked")
	private void updateModel() {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			final Object obj = model.wrapper.getPropertyValue(model.keys[mColIndex]);
			if (obj instanceof Map) {
				final Map<?, ?> map = (Map) obj;
				final AComboBox comboBox = new AComboBox();
				List keys = new ArrayList(map.keySet());
				Collections.sort(keys);
				comboBox.setRenderer(LabelCheckBoxRenderer.getInstance());
				for (Iterator it = keys.iterator(); it.hasNext();) {
					comboBox.addItem(it.next());
				}
				keys.clear();
				keys = null;
				final TableColumn sportColumn = getColumnModel().getColumn(mColIndex);
				sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
				comboBox.addActionListener(new ActionListener() {

					public void actionPerformed(final ActionEvent e) {
						final AComboBox cb = (AComboBox) e.getSource();
						if (cb.getItemCount() != map.keySet().size()) {
							cb.removeAllItems();
							List keys1 = new ArrayList(map.keySet());
							Collections.sort(keys1);
							for (Iterator it = keys1.iterator(); it.hasNext();) {
								cb.addItem(it.next());
							}
							keys1.clear();
							keys1 = null;
						}

					}
				});

			}
		}
	}
	
	public void sortColumn(int mColIndex) {
		this.sortedColumnIndex = mColIndex;
		WrapperedTableModel<T> model = this.getModel();
		model.sortRows(mColIndex);
	}
	
	private final void initialization() {
		this.updateModel();
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);	
	}
	
    Icon getHeaderRendererIcon(int column) {
    	WrapperedTableModel<T> model = this.getModel();
        if (this.sortedColumnIndex != column) {
            return null; 
        } 
        return model.getSortOrder(column) ? Arrow.ARROW_ASCEND : Arrow.ARROW_DESCEND; 
    }
	
	private class SortableHeaderRenderer<K> implements TableCellRenderer {
        final TableCellRenderer tableCellRenderer;

        public SortableHeaderRenderer(final TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        public Component getTableCellRendererComponent(final JTable table, 
                                                       final Object value,
                                                       final boolean isSelected, 
                                                       final boolean hasFocus,
                                                       final int row, 
                                                       final int column) {
        	final  Component c = this.tableCellRenderer.getTableCellRendererComponent(table, value, 
                                                                          isSelected, 
                                                                          hasFocus, 
                                                                          row, column);
            if (c instanceof JLabel) {
            	final JLabel label = (JLabel) c;
                label.setHorizontalTextPosition(SwingConstants.LEFT);
                int modelColumn = WrapperedTable.this.convertColumnIndexToModel(column);
//                label.setIcon(getHeaderRendererIcon(modelColumn, label.getFont().getSize()));
                label.setIcon(WrapperedTable.this.getHeaderRendererIcon(modelColumn));
            }
            return c;
        }
    }
	
	private static final class Arrow implements Icon {
		
		private final int	size;
		private final boolean	ascend;
		
		protected static final Arrow ARROW_ASCEND = new Arrow(true, 10);
		protected static final Arrow ARROW_DESCEND = new Arrow(false, 10);
		
		private Arrow(final boolean ascend, 
			final int size){
			this.ascend = ascend;
			this.size = size;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconHeight()
		 */
		public int getIconHeight() {			
			return this.size + 2;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconWidth()
		 */
		public int getIconWidth() {
			return this.size + 2;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
		 */
		public void paintIcon(	final Component c,
		                      	final Graphics g,
		                      	final int x,
		                      	final int y) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.DARK_GRAY);
			final int y1 = (this.ascend ? 0 : 1 + this.size/2);
			final int y2 = this.size/3 + 1;
			g2d.fillPolygon(new int[] {x, x + this.size + (this.ascend ? -1 : 0), x + this.size/2+ (this.ascend ? -1 : 0)}, 
				new int[] { y + y1 + y2 , y + y1  + y2 , y + (this.ascend ? 0 + this.size/2 : 0) + y2 },
				3);
		}
	}

}
