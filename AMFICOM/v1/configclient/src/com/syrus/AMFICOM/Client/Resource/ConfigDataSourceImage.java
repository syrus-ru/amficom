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

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;

public class ConfigDataSourceImage extends DataSourceImage
{
	protected ConfigDataSourceImage()
	{
	}

	public ConfigDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void LoadNet()
	{
//		ResourceDescriptor_Transferable[] desc1 = GetDescriptors(Port.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDomainDescriptors(Equipment.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDomainDescriptors(Link.typ);
//		ResourceDescriptor_Transferable[] desc4 = GetDescriptors(CablePort.typ);
		ResourceDescriptor_Transferable[] desc5 = GetDomainDescriptors(CableLink.typ);
//		ResourceDescriptor_Transferable[] desc6 = GetDescriptors(TestPort.typ);

//		Pool.removeHash(Port.typ);
//		Pool.removeHash(CablePort.typ);
//		Pool.removeHash(TestPort.typ);
//		Pool.removeHash(Equipment.typ);
//		Pool.removeHash(Link.typ);
//		Pool.removeHash(CableLink.typ);

//		load(Port.typ);
//		load(CablePort.typ);
//		load(TestPort.typ);
		load(Equipment.typ);
		load(Link.typ);
		load(CableLink.typ);
//		Vector ids1 = filter(Port.typ, desc1, true);
		Vector ids1 = new Vector();
		Vector ids2 = filter(Equipment.typ, desc2, true);
		Vector ids3 = filter(Link.typ, desc3, true);
//		Vector ids4 = filter(CablePort.typ, desc4, true);
		Vector ids4 = new Vector();
		Vector ids5 = filter(CableLink.typ, desc5, true);
//		Vector ids6 = filter(TestPort.typ, desc6, true);
		Vector ids6 = new Vector();

		if(
//		ids1.size() > 0 ||
			ids2.size() > 0 ||
			ids3.size() > 0 ||
//			ids4.size() > 0 ||
			ids5.size() > 0)
//			|| ids6.size() > 0)

		{
			di.LoadNet(ids1, ids4, ids2, ids3, ids5);
//			save(Port.typ);
//			save(CablePort.typ);
//			save(TestPort.typ);
			save(Equipment.typ);
			save(Link.typ);
			save(CableLink.typ);
		}
	}

	public void LoadISM()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(KIS.typ);
//		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(AccessPort.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDomainDescriptors(MonitoredElement.typ);
		ResourceDescriptor_Transferable[] desc4 = GetDomainDescriptors(TransmissionPath.typ);

//		Pool.removeHash(KIS.typ);
//		Pool.removeHash(AccessPort.typ);
//		Pool.removeHash(MonitoredElement.typ);
//		Pool.removeHash(TransmissionPath.typ);

//		load(Port.typ);
//		load(CablePort.typ);
		load(KIS.typ);
//		load(AccessPort.typ);
		load(MonitoredElement.typ);
		load(TransmissionPath.typ);
		Vector ids = filter(KIS.typ, desc, true);
//		Vector ids2 = filter(AccessPort.typ, desc2, true);
		Vector ids2 = new Vector();
		Vector ids3 = filter(MonitoredElement.typ, desc3, true);
		Vector ids4 = filter(TransmissionPath.typ, desc4, true);
		if(	ids.size() > 0 ||
			ids2.size() > 0 ||
			ids3.size() > 0 ||
			ids4.size() > 0 )
		{
			di.LoadISM(ids, ids2, ids3, ids4);
//			save(Port.typ);
//			save(CablePort.typ);
			save(KIS.typ);
//			save(AccessPort.typ);
			save(MonitoredElement.typ);
			save(TransmissionPath.typ);
		}
	}

	// получить каталог сераилизованных объектов фиксированного типа
	protected void load(String type)
	{
		super.load(type);
		if(type.equals(KIS.typ) || type.equals(Equipment.typ))
		{
			Map ht = Pool.getMap(type);
			if(ht == null)
				return;
			for(Iterator it = ht.values().iterator(); it.hasNext();)
			{
				ObjectResource or = (ObjectResource)it.next();;
				Pool.put("kisequipment", or.getId(), or);
			}
		}
	}

}

