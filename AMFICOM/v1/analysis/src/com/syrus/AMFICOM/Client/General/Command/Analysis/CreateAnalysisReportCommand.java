package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ReportTable;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleResizableFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportDialog;

public class CreateAnalysisReportCommand extends AbstractCommand {
	public static final String TABLE = "table";
	public static final String PANEL = "panel";

	private ApplicationContext aContext;
	private List<ReportTable> tableFrames = new LinkedList<ReportTable>();
	private List<SimpleResizableFrame> panels = new LinkedList<SimpleResizableFrame>();
	private String destinationModule;

	public CreateAnalysisReportCommand(ApplicationContext aContext, String moduleName) {
		this.aContext = aContext;
		this.destinationModule = moduleName;
	}

	@Override
	public void setParameter(String key, Object value) {
		if (key.equals(TABLE)) {
			this.tableFrames = (List<ReportTable>)value;
		} else if (key.equals(PANEL)) {
			this.panels = (List<SimpleResizableFrame>)value;
		}
	}

	@Override
	public void execute() {
		java.util.Map<Object,Object> reportData = new HashMap<Object,Object>();
		
		for (ReportTable tf : this.tableFrames) {
			reportData.put(tf.getReportTitle(),	tf.getTableModel());
		}

		for (SimpleResizableFrame rf : this.panels) {
			reportData.put(rf.getReportTitle(), rf.getTopGraphPanel());
		}

		new CreateReportDialog(
				this.aContext,
				this.destinationModule,
				reportData);
	}
}



