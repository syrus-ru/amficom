package com.syrus.AMFICOM.Client.Schematics.Report;

import com.syrus.AMFICOM.Client.General.Report.APOReportModel;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.RenderingObject;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.General.Scheme.SchemePanelNoEdition;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;

import javax.swing.JComponent;

import java.util.Vector;


public class SchemeReportModel extends APOReportModel
{
	public String getName() {return "schemereportmodel";}

	public static String scheme = "label_scheme";

	public String getObjectsName()
	{
		return LangModelReport.String("label_repPhysicalScheme");
	}

	public String getReportsName(ObjectsReport rp)
	{
		String return_value = this.getObjectsName() + ":" + LangModelReport.String(rp.field);
		if (rp.reserveName != null)
			return_value += rp.reserveName;

		return return_value;
	}

	public String getLangForField(String field)
	{
		return LangModelReport.String(field);
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

	public Vector getAvailableReports()
	{
		Vector result = new Vector();

		result.add(SchemeReportModel.scheme);

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

	public boolean isTableReport(ObjectsReport rp)
	{
		return false;
	}

	public JComponent createReport(
			ObjectsReport rp,
			int divisionsNumber,
			ReportTemplate rt,
			ApplicationContext aContext,
			boolean fromAnotherModule)

			throws CreateReportException
	{
		JComponent returnValue = null;

		if (rp.field.equals(SchemeReportModel.scheme))
		{
			SchemePanelNoEdition schemePanel = new SchemePanelNoEdition(aContext);
			Scheme sc = (Scheme) Pool.get(Scheme.typ,(String) rp.getReserve());
			if (sc == null)
				throw new CreateReportException(
					rp.getName(),
					CreateReportException.cantImplement);

			schemePanel.openScheme(sc);

			SchemeGraph schemeGraph = schemePanel.getGraph();
			RenderingObject reportsRO = rt.findROforReport(rp);

			returnValue = new SchemeRenderPanel(reportsRO, schemeGraph);
		}

		return returnValue;
	}

	public void setData(ReportTemplate rt,Object data)
	{
	};
}
