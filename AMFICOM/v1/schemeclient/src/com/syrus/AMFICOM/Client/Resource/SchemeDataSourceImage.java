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

import java.util.Vector;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.Resource.General.*;
import com.syrus.AMFICOM.Client.Resource.General.CharacteristicType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemeDataSourceImage extends DataSourceImage
{
	protected SchemeDataSourceImage()
	{
	}

	public SchemeDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void LoadSchemeProto()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(ProtoElement.typ);

//		Pool.removeMap(ProtoElement.typ);

		load("imageresource", ImageCatalogue.hash);
		load(ProtoElement.typ);
		Vector ids = filter(ProtoElement.typ, desc, true);
		if(ids.size() > 0)
		{
			di.LoadSchemeProto(ids);
			save(ProtoElement.typ);
			save("imageresource", ImageCatalogue.hash);
		}
	}

	public void LoadNetDirectory()
	{
		ResourceDescriptor_Transferable[] desc1 = GetDescriptors(PortType.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(EquipmentType.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDescriptors(LinkType.typ);
		ResourceDescriptor_Transferable[] desc4 = GetDescriptors(CablePortType.typ);
		ResourceDescriptor_Transferable[] desc5 = GetDescriptors(CableLinkType.typ);
		ResourceDescriptor_Transferable[] desc6 = GetDescriptors(CharacteristicType.typ);

//		Pool.removeMap(PortType.typ);
//		Pool.removeMap(EquipmentType.typ);
//		Pool.removeMap(LinkType.typ);
//		Pool.removeMap(CablePortType.typ);
//		Pool.removeMap(CableLinkType.typ);
//		Pool.removeMap(CharacteristicType.typ);

		load(PortType.typ);
		load(EquipmentType.typ);
		load(LinkType.typ);
		load(CablePortType.typ);
		load(CableLinkType.typ);
		load(CharacteristicType.typ);
		Vector ids1 = filter(PortType.typ, desc1, true);
		Vector ids2 = filter(EquipmentType.typ, desc2, true);
		Vector ids3 = filter(LinkType.typ, desc3, true);
		Vector ids4 = filter(CablePortType.typ, desc4, true);
		Vector ids5 = filter(CableLinkType.typ, desc5, true);
		Vector ids6 = filter(CharacteristicType.typ, desc6, true);

		if(	ids1.size() > 0 ||
			ids2.size() > 0 ||
			ids3.size() > 0 ||
			ids4.size() > 0 ||
			ids5.size() > 0 ||
			ids6.size() > 0)

		{
			di.LoadNetDirectory(ids1, ids2, ids3, ids6, ids4, ids5);
			save(PortType.typ);
			save(EquipmentType.typ);
			save(LinkType.typ);
			save(CablePortType.typ);
			save(CableLinkType.typ);
			save(CharacteristicType.typ);
		}
	}

	public void LoadISMDirectory()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(KISType.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(MeasurementPortType.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDescriptors(TransmissionPathType.typ);

//		Pool.removeMap(KISType.typ);
//		Pool.removeMap(AccessPortType.typ);
//		Pool.removeMap(TransmissionPathType.typ);

		load(KISType.typ);
		load(MeasurementPortType.typ);
		load(TransmissionPathType.typ);
//		Vector ids = filter(KISType.typ, desc, true);
		Vector ids = new Vector();
		Vector ids2 = filter(MeasurementPortType.typ, desc2, true);
		Vector ids3 = filter(TransmissionPathType.typ, desc3, true);
		if(	ids.size() > 0 ||
			ids2.size() > 0 ||
			ids3.size() > 0 )
		{
			di.LoadISMDirectory(ids, ids2, ids3);
			save(KISType.typ);
			save(MeasurementPortType.typ);
			save(TransmissionPathType.typ);
		}
	}

	public void LoadSchemes()
	{
		ResourceDescriptor_Transferable[] desc = GetDomainDescriptors(Scheme.typ);

//		Pool.removeMap("scheme");
//		Pool.removeMap("schemeelement");
//		Pool.removeMap("schemecablelink");

		load("imageresource", ImageCatalogue.hash);
		load("scheme");
		Vector ids = filter("scheme", desc, true);
		if(ids.size() > 0)
		{
			di.LoadSchemes(ids);
			save("scheme");
			save("imageresource", ImageCatalogue.hash);
		}
	}

	public void LoadAttributeTypes()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(ElementAttributeType.typ);

//		Pool.removeMap(ElementAttributeType.typ);

		load(ElementAttributeType.typ);
		Vector ids = filter(ElementAttributeType.typ, desc, true);
		if(ids.size() > 0)
		{
			String [] id_s = new String[ids.size()];
			ids.copyInto(id_s);
			di.LoadAttributeTypes(id_s);
			save(ElementAttributeType.typ);
		}
	}

}

