/*
 * $Id: TableDataRenderingComponent.java,v 1.5 2005/09/30 08:13:22 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer.ObjectRenderer;
import com.syrus.AMFICOM.report.TableDataStorableElement;

public final class TableDataRenderingComponent extends DataRenderingComponent {
	private static final long serialVersionUID = -7406942647346619853L;

	private ATable table = null;
	private TableModel tableModel = null;
	private List<Integer> tableColumnWidths = null;
	private TableDataStorableElement trdElement = null;
	
	public TableDataRenderingComponent(
			TableDataStorableElement trde,
			TableModel tableModel,
			List<Integer> tableColumnWidths) {
		super(trde);
		
		this.tableModel = tableModel;
		this.tableColumnWidths = tableColumnWidths;
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
			TableColumn tableColumn = this.table.getColumnModel().getColumn(i);
			tableColumn.setWidth(this.tableColumnWidths.get(i));
			tableColumn.setPreferredWidth(this.tableColumnWidths.get(i));
			tableColumn.setCellRenderer(new ObjectRenderer());
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
