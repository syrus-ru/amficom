package com.syrus.AMFICOM.Client.Schematics.Report;

import com.syrus.AMFICOM.Client.General.Report.APOReportModel;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.*;

import com.syrus.AMFICOM.Client.General.Scheme.UgoPanel;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;

import javax.swing.JComponent;

import java.util.*;

public class SchemeReportModel extends APOReportModel
{
	public String getName() {return "schemereportmodel";}

	public static String scheme = "schemeMainTitle";
	public static String ugo = "elementsUGOTitle";

	public String getObjectsName()
	{
		return LangModelReport.getString("label_repPhysicalScheme");
	}

	public String getReportsName(ObjectsReport rp)
	{
		String return_value = this.getObjectsName() + ":" + LangModelSchematics.getString(rp.field);
		if (rp.reserveName != null)
			return_value += rp.reserveName;

		return return_value;
	}

	public String getLangForField(String field)
	{
		return LangModelSchematics.getString(field);
	}

	public String getReportsReserveName(ObjectsReport rp)
			throws CreateReportException
	{
		String reserve_str = (String)rp.getReserve();
		Scheme scheme = (Scheme)Pool.get(Scheme.typ,reserve_str);
		return ":" + scheme.name;
	}

	public SchemeReportModel()
	{
	}

	public List getAvailableReports()
	{
		List result = new ArrayList(2);

		result.add(SchemeReportModel.scheme);
		result.add(SchemeReportModel.ugo);

		return result;
	}

	public void loadRequiredObjects(
				DataSourceInterface dsi,
				ObjectsReport rp,
				ReportTemplate rt)
	{
		if (rp.field.equals(SchemeReportModel.scheme))
		{
			String curValue = (String) rt.resourcesLoaded.get("schemesLoaded");
			if (curValue.equals("false"))
			{
				new SchemeDataSourceImage(dsi).LoadSchemes();
				rt.resourcesLoaded.put("schemesLoaded","true");
			}
		}
	}

	public int getReportKind(ObjectsReport rp)
	{
		return -1;
	}

	public JComponent createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule)

			throws CreateReportException
	{
		SchemeRenderPanel schemePanel = null;

		if (  rp.field.equals(SchemeReportModel.scheme)
			 || rp.field.equals(SchemeReportModel.ugo))
		{
			Scheme sc = null;

			if (rp.getReserve() instanceof UgoPanel)
			{
				sc = ((UgoPanel) rp.getReserve()).scheme;
			}
			else
			{
				sc = (Scheme) Pool.get(Scheme.typ, (String) rp.getReserve());
				if (sc == null)
					throw new CreateReportException(
						rp.getName(),
						CreateReportException.cantImplement);
			}

			schemePanel = new SchemeRenderPanel(aContext);
			schemePanel.openScheme(sc);

			RenderingObject reportsRO = rt.findROforReport(rp);
			schemePanel.initializeSize(reportsRO);
		}

		return schemePanel;
	}

	public void setData(ReportTemplate rt, AMTReport aReport)
	{
		if (rt.templateType.equals(ReportTemplate.rtt_Scheme))
		{
			super.setData(rt,aReport);
		}
	}
}
