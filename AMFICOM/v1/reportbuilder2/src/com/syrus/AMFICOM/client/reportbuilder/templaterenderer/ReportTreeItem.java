/*
 * $Id: ReportTreeItem.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Объект, который кладётся в каждый "листок" дерева доступных
 * элементов шаблона. В последствии по этим данным строится
 * StorableElement.
 * @author $Author: bass $
 * @version $Revision: 1.1.1.1 $, $Date: 2005/12/02 11:37:17 $
 * @module reportbuilder_v1
 */
public class ReportTreeItem implements Serializable{
	private String reportModel;
	private String reportName;
	
	public ReportTreeItem(String reportModel, String reportName) {
		this.reportModel = reportModel;
		this.reportName = reportName;		
	}

	public String getReportModel() {
		return this.reportModel;
	}

	public String getReportName() {
		return this.reportName;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.reportModel);
		out.writeObject(this.reportName);		
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.reportModel = (String)in.readObject();
		this.reportName = (String)in.readObject();		
	}
}