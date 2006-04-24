/*
 * $Id: TableDataRenderingComponent.java,v 1.7 2006/04/24 06:30:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.report.TableDataStorableElement;

public final class TableDataRenderingComponent extends DataRenderingComponent {
	private static final long serialVersionUID = -7406942647346619853L;

	private ATable table = null;
	private TableModel tableModel = null;
	private TableDataStorableElement trdElement = null;
	
	public TableDataRenderingComponent(
			TableDataStorableElement trde,
			TableModel tableModel) {
		super(trde);
		
		this.tableModel = tableModel;
		this.trdElement = trde;
		
		jbinit();
	}
	
	private void jbinit() {
		BorderLayout layout = new BorderLayout();
		layout.setHgap(RenderingComponent.EDGE_SIZE);
		layout.setVgap(RenderingComponent.EDGE_SIZE);		
		this.setLayout(layout);
		
		this.table = new ATable(this.tableModel);
		Font tableFont = this.trdElement.getFont();
		this.table.setFont(tableFont);
		this.table.setRowHeight(tableFont.getSize() + 2);
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			setPreferredWidth(this.table, i);
		}
		JScrollPane scrollPane = new JScrollPane(this.table);
		scrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(scrollPane,BorderLayout.CENTER);

		this.setSize(
				this.getElement().getWidth(),
				(int)(this.table.getPreferredSize().getHeight()
				+ this.table.getTableHeader().getPreferredSize().getHeight()) + 2);
		this.setPreferredSize(this.getSize());
		this.setBorder(DataRenderingComponent.DEFAULT_BORDER);
	}
	
	private void setPreferredWidth(JTable table, int columnIndex) {
		final TableColumn column = table.getColumnModel().getColumn(columnIndex);
		final TableModel model = table.getModel();
		
		final TableCellRenderer headerRenderer = 
				table.getTableHeader() != null ? this.table.getTableHeader().getDefaultRenderer() 
				: null;
			
		Component comp = headerRenderer != null ?
				headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0) 
				: null;
							
		final int headerWidth = comp != null ? comp.getPreferredSize().width : 0;
							
		int cellWidth = 0;
		for(int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
			final Object valueAt = model.getValueAt(rowIndex, columnIndex);
			comp = table.getCellRenderer(rowIndex, columnIndex).
			getTableCellRendererComponent(table, valueAt, false, false, rowIndex, columnIndex);
			cellWidth = Math.max(comp.getPreferredSize().width, cellWidth);
		}
		column.setPreferredWidth(Math.max(headerWidth, cellWidth));
	}

	public void setX(int x) {
		this.setLocation(x,this.getY());
	}

	public void setY(int y) {
		this.setLocation(this.getX(),y);
	}

	public void setWidth(int width) {
		this.setSize(width,this.getHeight());
	}

	public void setHeight(int height) {
		this.setSize(this.getWidth(),height);
	}
	
	public ATable getTable() {
		return this.table;
	}
}
