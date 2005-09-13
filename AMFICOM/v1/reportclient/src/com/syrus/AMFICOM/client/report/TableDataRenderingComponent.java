/*
 * $Id: TableDataRenderingComponent.java,v 1.2 2005/09/13 12:23:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.report.TableDataStorableElement;

public final class TableDataRenderingComponent extends DataRenderingComponent {
	private static final long serialVersionUID = -7406942647346619853L;

	private JTable table = null;
	private TableModel tableModel = null;
	private TableColumnModel tableColumnModel = null;
	
	public TableDataRenderingComponent(
			TableDataStorableElement trde,
			TableModel tableModel,
			TableColumnModel tableColumnModel) {
		super(trde);
		
		this.tableModel = tableModel;
		this.tableColumnModel = tableColumnModel;
		
		jbinit();
	}
	
	private void jbinit() {
		BorderLayout layout = new BorderLayout();
		layout.setHgap(RenderingComponent.EDGE_SIZE);
		layout.setVgap(RenderingComponent.EDGE_SIZE);		
		this.setLayout(layout);
		
		this.table = new JTable(this.tableModel,this.tableColumnModel);
		this.add(this.table,BorderLayout.CENTER);
		
		this.setSize(
				this.getElement().getWidth(),
				this.table.getPreferredSize().height);
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
	
	public JTable getTable() {
		return this.table;
	}
}
