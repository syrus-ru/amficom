package com.syrus.AMFICOM.Client.Resource;

import java.util.Vector;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;

public final class ReportDataSourceImage extends DataSourceImage {
	public ReportDataSourceImage(DataSourceInterface di) {
		super(di);
	}

	public void LoadReportTemplates() {

		ResourceDescriptor_Transferable[] desc = GetDescriptors(ReportTemplate.typ);

		load(ReportTemplate.typ);
		Vector ids = filter(ReportTemplate.typ, desc, true);
		if (ids.size() > 0) {
			String[] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadReportTemplates(id_s);
			save(ReportTemplate.typ);
		}

	}
}
