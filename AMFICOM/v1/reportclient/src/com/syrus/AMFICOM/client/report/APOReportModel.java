package com.syrus.AMFICOM.client.report;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

/**
 * <p>Title: </p>
 * <p>Description: Класс-родитель для моделей отчётов
 * по результатам анализа, оптимизации итд.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

abstract public class APOReportModel extends ReportModel
{
	public String getName() {return "aporeportmodel";}

	abstract public List getAvailableReports();
	abstract public String getLangForField(String field);

  public void installData (ReportTemplate template, Map data)
  {
		for (DataStorableElement renderingElement : template.getDataStorableElements())
		{
			String renderersRTEName = renderingElement.getReportName();
			Set keysSet = data.keySet();
			for (Iterator keysIt = keysSet.iterator(); keysIt.hasNext();)
			{
				String curKey = (String)keysIt.next();
				if (curKey.equals(renderersRTEName))
				{
					try
					{
						renderingElement.setReportObjectData(data.get(curKey));
					}
					catch (Exception exc)
					{
						Log.errorMessage("APOReportModel.setData | Error occured while installing data to template.");
					}
					break;
				}
			}
		}
  }

	public APOReportModel()
	{
	}
}
