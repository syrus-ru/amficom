package com.syrus.AMFICOM.Client.General.Report;

import java.util.List;
import java.util.Iterator;
import java.util.Set;

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

  public void setData (ReportTemplate rt, AMTReport aReport)
  {
		for (Iterator it = rt.objectRenderers.iterator(); it.hasNext();)
		{
			RenderingObject curRenderer = (RenderingObject)it.next();
			String itsTableTitle = curRenderer.getReportToRender().field;

      Set keysSet = aReport.data.keySet();
			for (Iterator keysIt = keysSet.iterator(); keysIt.hasNext();)
			{
				String curKey = (String)keysIt.next();
				if (curKey.equals(getLangForField(itsTableTitle)))
				{
					try
					{
						curRenderer.getReportToRender().setReserve(aReport.data.get(curKey));
					}
					catch (Exception exc)
					{
             exc.printStackTrace();
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
