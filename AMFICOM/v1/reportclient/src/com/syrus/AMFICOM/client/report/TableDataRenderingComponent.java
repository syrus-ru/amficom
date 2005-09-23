/*
 * $Id: TableDataRenderingComponent.java,v 1.3 2005/09/23 12:10:05 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.report.TableDataStorableElement;

public final class TableDataRenderingComponent extends DataRenderingComponent {
	private static final long serialVersionUID = -7406942647346619853L;

	private ATable table = null;
	private TableModel tableModel = null;
	private List<Integer> tableColumnWidths = null;
	
	public TableDataRenderingComponent(
			TableDataStorableElement trde,
			TableModel tableModel,
			List<Integer> tableColumnWidths) {
		super(trde);
		
		this.tableModel = tableModel;
		this.tableColumnWidths = tableColumnWidths;
		
		jbinit();
	}
	
	private void jbinit() {
		BorderLayout layout = new BorderLayout();
		layout.setHgap(RenderingComponent.EDGE_SIZE);
		layout.setVgap(RenderingComponent.EDGE_SIZE);		
		this.setLayout(layout);
		
		this.table = new ATable(this.tableModel);
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			TableColumn tableColumn = this.table.getColumnModel().getColumn(i);
			tableColumn.setWidth(this.tableColumnWidths.get(i));
			tableColumn.setPreferredWidth(this.tableColumnWidths.get(i));			
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
