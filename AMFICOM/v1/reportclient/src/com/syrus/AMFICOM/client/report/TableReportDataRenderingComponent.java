/*
 * $Id: TableReportDataRenderingComponent.java,v 1.3 2005/09/03 12:42:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.BorderLayout;

import javax.swing.JTable;

import com.syrus.AMFICOM.report.TableDataStorableElement;

public final class TableReportDataRenderingComponent extends DataRenderingComponent {
	private static final long serialVersionUID = -7406942647346619853L;

	private JTable table = null;
	
	public TableReportDataRenderingComponent(TableDataStorableElement trde)
	{
		super(trde);
		
		jbinit();
	}
	
	private void jbinit()
	{
		BorderLayout layout = new BorderLayout();
		layout.setHgap(RenderingComponent.EDGE_SIZE);
		layout.setVgap(RenderingComponent.EDGE_SIZE);		
		this.setLayout(layout);

		this.add(this.table,BorderLayout.CENTER);
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
}
