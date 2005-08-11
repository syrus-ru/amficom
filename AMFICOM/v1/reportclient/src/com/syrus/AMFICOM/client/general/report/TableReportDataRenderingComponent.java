/*
 * $Id: TableReportDataRenderingComponent.java,v 1.1 2005/08/11 11:17:34 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.general.report;

import java.awt.BorderLayout;

import javax.swing.JTable;

import com.syrus.AMFICOM.report.TableDataRenderingElement;

public final class TableReportDataRenderingComponent extends DataRenderingComponent {
	private static final long serialVersionUID = -7406942647346619853L;

	private JTable table = null;
	
	public TableReportDataRenderingComponent(TableDataRenderingElement trde)
	{
		super(trde);
		
		jbinit();
	}
	
	private void jbinit()
	{
		BorderLayout layout = new BorderLayout();
		layout.setHgap(DataRenderingComponent.EDGE_SIZE);
		layout.setVgap(DataRenderingComponent.EDGE_SIZE);		
		this.setLayout(layout);

		this.add(this.table,BorderLayout.CENTER);
	}
}
