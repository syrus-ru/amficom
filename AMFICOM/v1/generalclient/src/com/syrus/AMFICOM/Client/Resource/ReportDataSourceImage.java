//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Класс хранения отображения БД на клиентскую часть          * //
// *           задача модуля - для минимизации трафика клиент-сервер      * //
// *           хранить подгружаемые с сервера объекты, так что при        * //
// *           последующем запуске клиентской части проверяется образ     * //
// *           на наличие необходимых объектов, и в случае их отсутствия  * //
// *           они подгружаются с сервера                                 * //
// *                                                                      * //
// * Тип: Java 1.4.0                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 24 mar 2003                                                      * //
// * Расположение: ISM\prog\java\AMFICOM\com\syrus\AMFICOM\Client\        * //
// *        Resource\DataSourceImage.java                                 * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 9.0.3.9.93                       * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.0)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;


import java.util.*;
import java.util.zip.*;
import java.io.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;

public class ReportDataSourceImage extends DataSourceImage
{
	protected ReportDataSourceImage()
	{
	}

	public ReportDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void LoadReportTemplates()
	{

		ResourceDescriptor_Transferable[] desc = GetDescriptors(ReportTemplate.typ);

		load(ReportTemplate.typ);
		Vector ids = filter(ReportTemplate.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadReportTemplates(id_s);
			save(ReportTemplate.typ);
		}

	}
}

