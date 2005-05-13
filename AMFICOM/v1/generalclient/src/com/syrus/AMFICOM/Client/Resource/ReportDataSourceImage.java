package com.syrus.AMFICOM.Client.Resource;

import java.util.Vector;

public final class ReportDataSourceImage extends DataSourceImage {
	public ReportDataSourceImage(DataSourceInterface di) {
		super(di);
	}

	public void LoadReportTemplates() {

		ResourceDescriptor_Transferable[] desc = GetDescriptors(ReportTemplate.class.getName());

		load(ReportTemplate.class.getName());
		Vector ids = filter(ReportTemplate.class.getName(), desc, true);
		if (ids.size() > 0) {
			String[] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadReportTemplates(id_s);
			save(ReportTemplate.class.getName());
		}

	}
}
