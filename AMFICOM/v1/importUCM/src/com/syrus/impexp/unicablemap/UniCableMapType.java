package com.syrus.impexp.unicablemap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UniCableMapType extends UniCableMapObject {

	public static String TYPED_OBJECT_QUERY = "select " +
			"UN, " +
			"TYP, " +
			"TEXT, " +
			"BUF, " +
			"MSK, " +
			"ORD, " +
			"DX, " +
			"DY, " +
			"X0, " +
			"Y0, " +
			"X1, " +
			"Y1, " +
			"STATE, " +
			"ELID " +
		"from OBJECT " + 
		"where TYP = ? ";
	public static PreparedStatement typedStatement;

	public static void init(Connection connection) 
			throws SQLException {
		typedStatement = connection.prepareStatement(TYPED_OBJECT_QUERY);
	}

	public static final String UCM_TYPE = "тип";

	public static final String UCM_DB_OBJECT = "объект БД";
	public static final String UCM_OBJECT = "объект";
	public static final String UCM_GEO_OBJECT = "гео объект";

	public static final String UCM_LAYER = "слой";
	public static final String UCM_POINT = "точка";
	public static final String UCM_CUT = "отрезок";
	public static final String UCM_POLYLINE = "ломаная";
	public static final String UCM_CIRCLE = "круг";
	public static final String UCM_RECTANGLE = "прямоугольник";
	public static final String UCM_POLYGONE = "многоугольник";

	public static final String UCM_CITY = "город";
	public static final String UCM_STREET_GENERAL = "улица обобщенная";
	public static final String UCM_STREET = "улица";
	public static final String UCM_BUILDING = "дом";
	public static final String UCM_BUILDING_PLAN = "план дома";
	public static final String UCM_FLOOR = "этаж";
	public static final String UCM_ADDRESS = "адресный";
	public static final String UCM_PLAN = "план";

	public static final String UCM_LINK_TYPE = "вид связи";

	public static final String UCM_COMMUTATOR = "коммутатор";
	public static final String UCM_NAME = "название по номеру и связям";
	public static final String UCM_NAME_TRANSFER = "передача названия";
	public static final String UCM_LABEL_BY_NAME = "подпись по названию";
	public static final String UCM_LABEL_BY_NUMBER = "подпись по номеру";
	public static final String UCM_QUERY_SAMPLE = "образец запросника";
	public static final String UCM_CASCADE_UPDATE = "каскадное обновление";
	public static final String UCM_QUERY_OBJECT = "объект запросника";
	public static final String UCM_CHAIN = "цепочка";
	public static final String UCM_MATRIX = "решетка";
	public static final String UCM_FOLDER = "папка";
	public static final String UCM_SYSTEM = "системные";
	public static final String UCM_FOLDER_CONTENTS = "содержимое папки";
	public static final String UCM_OPTICS = "оптика";
	public static final String UCM_FOR_QUERY = "для запросника";
	public static final String UCM_FOR_CITY = "для города";
	public static final String UCM_TEMPLATE = "шаблон";
	public static final String UCM_LINK_ABSTRACT = "связь абстрактная";
	public static final String UCM_DEFINITION = "определение";
	public static final String UCM_DEFINED_LINK = "определяемая связь";
	public static final String UCM_LINK = "связь";
	public static final String UCM_COMPOUND_OBJECT = "составной объект";
	public static final String UCM_EVOLVEMENT = "развертка";
	public static final String UCM_FULL_SCREEN = "на весь экран";
	public static final String UCM_START_POINT = "начальная точка";
	public static final String UCM_SUBSCRIBER = "абонент";
	public static final String UCM_NAME_BY_ABBREVIATION = "название по сокращению";
	public static final String UCM_NUMBER_TRANSFER = "передача номера";
	public static final String UCM_STYLE = "стиль";
	public static final String UCM_QUERY = "запрос";
	public static final String UCM_QUERY_TEMPLATE = "шаблон запроса";
	public static final String UCM_CONTRACTOR = "подрядчик";
	public static final String UCM_QUERY_LINK = "связь запроса";
	public static final String UCM_NAME_BY_NUMBER = "название по номеру";
	public static final String UCM_PROJECT = "проект";
	public static final String UCM_PROJECT_OBJECT = "объект проекта";
	public static final String UCM_PAIR = "парный";
	public static final String UCM_TEMPLATE_ALL = "все шаблоны";
	public static final String UCM_COPIED = "копируемый";
	public static final String UCM_DEFINITION_AREA = "область определений";
	public static final String UCM_END_POINT = "конечная точка";
	public static final String UCM_QUERY_AREA = "область запросов";
	public static final String UCM_MODELLER_OBJECT = "объект модельника";
	public static final String UCM_SIGNATURE_TRANSFER = "передача подписи";
	public static final String UCM_QUERY_ZONE = "зона запроса";
	public static final String UCM_QUERY_ZONE_POLYGONE = "зона запроса - многоугольник";
	public static final String UCM_QUERY_ZONE_CIRCLE = "зона запроса - круг";
	public static final String UCM_QUERY_TYPE_FOLDER = "папка типов для запроса";
	public static final String UCM_DIRECTORY = "справочник";
	public static final String UCM_ANY_OBJECT = "любой объект";
	public static final String UCM_QUERY_LINK_TYPE_FOLDER = "папка видов связи для запроса";
	public static final String UCM_BUFFER = "буфер";
	public static final String UCM_BUFFERED = "буферизуемый";
	public static final String UCM_PORT = "порт";
	public static final String UCM_MULTIPLEXOR_TYPE = "тип мультиплексора";
	public static final String UCM_QUERY_AND_DEFINITION_AREA = "область запросов и определений";

	public static final String UCM_USER = "пользователь";
	public static final String UCM_PARAMETER_PASSWORD = "параметр пароль";

	public static final String UCM_PARAMETER = "параметр";
	public static final String UCM_PARAMETER_INTEGER = "параметр целое";
	public static final String UCM_PARAMETER_FLOAT = "параметр вещественное";
	public static final String UCM_PARAMETER_DROPDOWN_LIST = "параметр выпадающий список";
	public static final String UCM_PARAMETER_DATE = "параметр дата";
	public static final String UCM_PARAMETER_BOOLEAN = "параметр логическое";
	public static final String UCM_PARAMETER_REFERENCE = "параметр ссылка";
	public static final String UCM_PARAMETER_STRING = "параметр строка";
	public static final String UCM_PARAMETER_IMAGE = "параметр изображение";
	public static final String UCM_PARAMETER_ABSTRACT = "параметр абстрактный";
	public static final String UCM_PARAMETER_REAL = "параметр реальный";
	public static final String UCM_PARAMETER_GRAPHICAL_LINK = "параметр графические связи";
	public static final String UCM_PARAMETER_BUTTON = "параметр кнопка";
	public static final String UCM_PARAMETER_DEPENDENCY = "зависимость параметров";
	public static final String UCM_PARAMETER_PICTURE = "параметр картинка";

	public static final String UCM_LINK_TYPE_GENERAL = "вид связи обобщенный";
	public static final String UCM_LINK_TYPE_REAL = "вид связи реальный";

	public static final String UCM_CABLE_LINEAR = "кабель линейный";
	public static final String UCM_WELL_GENERAL = "колодец обобщенный";
	public static final String UCM_WELL_ENLARGED = "колодец крупно";
	public static final String UCM_TUNNEL_GENERAL = "тоннель обобщенный";
	public static final String UCM_CABLE_OUTER = "кабель внешний";
	public static final String UCM_TUNNEL_PROFILE = "тоннель разрез";
	public static final String UCM_BLOCK = "блок";
	public static final String UCM_PIPE = "труба";
	public static final String UCM_WELL = "колодец";
	public static final String UCM_MUFF = "муфта";
	public static final String UCM_CABLE_PLACE = "место кабеля";
	public static final String UCM_TUNNEL = "тоннель";
	public static final String UCM_CABLE_CROSSOVER = "кабель перекидной";
	public static final String UCM_WELL_BINDING = "привязка колодца";
	public static final String UCM_CABLE_INLET = "кабельный ввод";
	public static final String UCM_WELL_GENERAL_ENLARGED = "колодец обобщенный крупно";
	public static final String UCM_FRAME = "стойка";
	public static final String UCM_MODEM = "модем";
	public static final String UCM_MUFF_PLACE = "место для муфты";
	public static final String UCM_CABLE_CONTINUED_END = "кабель продолжение конец";
	public static final String UCM_CABLE_INDOOR_END = "кабель внутренний конец";
	public static final String UCM_CABLE_GENERAL = "кабель обобщенный";
	public static final String UCM_MUFF_GENERAL = "муфта обобщенная";
	public static final String UCM_WELL_TYPE = "вид колодца";
	public static final String UCM_CABLE_TYPE = "вид кабеля";
	public static final String UCM_WELL_OR_TUNNEL = "колодец или тоннель";
	public static final String UCM_FIBRE = "волокно";
	public static final String UCM_CABLE_PROFILE = "разрез кабеля";
	public static final String UCM_CABLE_ALL = "все кабели";
	public static final String UCM_MUFF_ALL = "все муфты";
	public static final String UCM_TUNNELING = "канализация";
	public static final String UCM_LAYOUT = "распайка";
	public static final String UCM_PATCHCORD = "пачкорд";
	public static final String UCM_CABLE_CONTINUED = "кабель продолжение";
	public static final String UCM_CABLE_INDOOR_GENERAL = "кабель внутренний обобщенный";
	public static final String UCM_VRM_PLACE = "место для ВРМ";
	public static final String UCM_ODF = "ODF";
	public static final String UCM_MUFF_WITH_LAYOUT = "муфта с распайкой";
	public static final String UCM_SUBSCRIBER_MODEM = "модем абонента";
	public static final String UCM_CORD_END = "конец корда";
	public static final String UCM_PIQUET = "пикет";
	public static final String UCM_PIQUET_ENLARGED = "пикет крупно";
	public static final String UCM_COLLECTOR_FRAGMENT = "участок коллектора";
	public static final String UCM_COLLECTOR = "коллектор";
	public static final String UCM_WELL_OR_INLET = "колодец или ввод";
	public static final String UCM_MUFF_TYPE = "вид муфты";
	public static final String UCM_ODF_PLAN = "план ODF";
	public static final String UCM_LAYOUT_ABSTRACT = "распайка абстр";
	public static final String UCM_SPARE = "запас";
	public static final String UCM_CABLE_BACKBONE = "кабель магистральный";
	public static final String UCM_RECEIVER_MODEM = "модем получатель";
	public static final String UCM_STREET_MODEM = "модем на узле";
	public static final String UCM_CABLE_2_CUTS = "кабель 2 разреза";
	public static final String UCM_FRAME_AND_VRM = "стойка и ВРМ";
	public static final String UCM_TELEPHONE_NODE = "телефонный узел";
	public static final String UCM_TELEPHONE_NODE_OBJECT = "объект телефонного узла";
	public static final String UCM_ATS = "АТС";
	public static final String UCM_WELL_AND_ODF_MUFF = "колодец и ODF/муфта";

	public static Collection secondary_map_objects = new ArrayList();
	public static Collection map_objects = new ArrayList();
	public static Collection scheme_objects = new ArrayList();
	public static Collection other_objects = new ArrayList();
	
	static {
		other_objects.add(UCM_TYPE);

		other_objects.add(UCM_DB_OBJECT);
		other_objects.add(UCM_OBJECT);
		other_objects.add(UCM_GEO_OBJECT);

		other_objects.add(UCM_LAYER);
		other_objects.add(UCM_POINT);
		other_objects.add(UCM_CUT);
		other_objects.add(UCM_POLYLINE);
		other_objects.add(UCM_CIRCLE);
		other_objects.add(UCM_RECTANGLE);
		other_objects.add(UCM_POLYGONE);

		other_objects.add(UCM_CITY);
		other_objects.add(UCM_STREET_GENERAL);
		other_objects.add(UCM_STREET);
		other_objects.add(UCM_BUILDING);
		other_objects.add(UCM_BUILDING_PLAN);
		other_objects.add(UCM_FLOOR);
		other_objects.add(UCM_ADDRESS);
		other_objects.add(UCM_PLAN);

		other_objects.add(UCM_LINK_TYPE);

		other_objects.add(UCM_COMMUTATOR);
		other_objects.add(UCM_NAME);
		other_objects.add(UCM_NAME_TRANSFER);
		other_objects.add(UCM_LABEL_BY_NAME);
		other_objects.add(UCM_LABEL_BY_NUMBER);
		other_objects.add(UCM_QUERY_SAMPLE);
		other_objects.add(UCM_CASCADE_UPDATE);
		other_objects.add(UCM_QUERY_OBJECT);
		other_objects.add(UCM_CHAIN);
		other_objects.add(UCM_MATRIX);
		other_objects.add(UCM_FOLDER);
		other_objects.add(UCM_SYSTEM);
		other_objects.add(UCM_FOLDER_CONTENTS);
		other_objects.add(UCM_OPTICS);
		other_objects.add(UCM_FOR_QUERY);
		other_objects.add(UCM_FOR_CITY);
		other_objects.add(UCM_TEMPLATE);
		other_objects.add(UCM_LINK_ABSTRACT);
		other_objects.add(UCM_DEFINITION);
		other_objects.add(UCM_DEFINED_LINK);
		other_objects.add(UCM_LINK);
		other_objects.add(UCM_COMPOUND_OBJECT);
		other_objects.add(UCM_EVOLVEMENT);
		other_objects.add(UCM_FULL_SCREEN);
		other_objects.add(UCM_START_POINT);
		other_objects.add(UCM_SUBSCRIBER);
		other_objects.add(UCM_NAME_BY_ABBREVIATION);
		other_objects.add(UCM_NUMBER_TRANSFER);
		other_objects.add(UCM_STYLE);
		other_objects.add(UCM_QUERY);
		other_objects.add(UCM_QUERY_TEMPLATE);
		other_objects.add(UCM_CONTRACTOR);
		other_objects.add(UCM_QUERY_LINK);
		other_objects.add(UCM_NAME_BY_NUMBER);
		other_objects.add(UCM_PROJECT);
		other_objects.add(UCM_PROJECT_OBJECT);
		other_objects.add(UCM_PAIR);
		other_objects.add(UCM_TEMPLATE_ALL);
		other_objects.add(UCM_COPIED);
		other_objects.add(UCM_DEFINITION_AREA);
		other_objects.add(UCM_END_POINT);
		other_objects.add(UCM_QUERY_AREA);
		other_objects.add(UCM_MODELLER_OBJECT);
		other_objects.add(UCM_SIGNATURE_TRANSFER);
		other_objects.add(UCM_QUERY_ZONE);
		other_objects.add(UCM_QUERY_ZONE_POLYGONE);
		other_objects.add(UCM_QUERY_ZONE_CIRCLE);
		other_objects.add(UCM_QUERY_TYPE_FOLDER);
		other_objects.add(UCM_DIRECTORY);
		other_objects.add(UCM_ANY_OBJECT);
		other_objects.add(UCM_QUERY_LINK_TYPE_FOLDER);
		other_objects.add(UCM_BUFFER);
		other_objects.add(UCM_BUFFERED);
		other_objects.add(UCM_PORT);
		other_objects.add(UCM_MULTIPLEXOR_TYPE);
		other_objects.add(UCM_QUERY_AND_DEFINITION_AREA);

		other_objects.add(UCM_USER);
		other_objects.add(UCM_PARAMETER_PASSWORD);

		other_objects.add(UCM_PARAMETER);
		other_objects.add(UCM_PARAMETER_INTEGER);
		other_objects.add(UCM_PARAMETER_FLOAT);
		other_objects.add(UCM_PARAMETER_DROPDOWN_LIST);
		other_objects.add(UCM_PARAMETER_DATE);
		other_objects.add(UCM_PARAMETER_BOOLEAN);
		other_objects.add(UCM_PARAMETER_REFERENCE);
		other_objects.add(UCM_PARAMETER_STRING);
		other_objects.add(UCM_PARAMETER_IMAGE);
		other_objects.add(UCM_PARAMETER_ABSTRACT);
		other_objects.add(UCM_PARAMETER_REAL);
		other_objects.add(UCM_PARAMETER_GRAPHICAL_LINK);
		other_objects.add(UCM_PARAMETER_BUTTON);
		other_objects.add(UCM_PARAMETER_DEPENDENCY);
		other_objects.add(UCM_PARAMETER_PICTURE);

		other_objects.add(UCM_LINK_TYPE_GENERAL);
		other_objects.add(UCM_LINK_TYPE_REAL);

		secondary_map_objects.add(UCM_WELL_GENERAL);
		secondary_map_objects.add(UCM_WELL_GENERAL_ENLARGED);
		secondary_map_objects.add(UCM_TUNNEL_GENERAL);
		secondary_map_objects.add(UCM_CABLE_GENERAL);
		secondary_map_objects.add(UCM_MUFF_GENERAL);
		secondary_map_objects.add(UCM_CABLE_INDOOR_GENERAL);

		secondary_map_objects.add(UCM_FRAME);
		secondary_map_objects.add(UCM_MODEM);
		secondary_map_objects.add(UCM_SUBSCRIBER_MODEM);
		secondary_map_objects.add(UCM_RECEIVER_MODEM);
		secondary_map_objects.add(UCM_STREET_MODEM);

		secondary_map_objects.add(UCM_LAYOUT_ABSTRACT);

		secondary_map_objects.add(UCM_WELL_TYPE);
		secondary_map_objects.add(UCM_CABLE_TYPE);
		secondary_map_objects.add(UCM_TUNNELING);

		secondary_map_objects.add(UCM_WELL_OR_TUNNEL);
		secondary_map_objects.add(UCM_WELL_OR_INLET);

			map_objects.add(UCM_TUNNEL);
		map_objects.add(UCM_TUNNEL_PROFILE);
		map_objects.add(UCM_BLOCK);
		map_objects.add(UCM_PIPE);
		map_objects.add(UCM_COLLECTOR);
		map_objects.add(UCM_COLLECTOR_FRAGMENT);
			map_objects.add(UCM_WELL);
		map_objects.add(UCM_WELL_ENLARGED);
			map_objects.add(UCM_PIQUET);
		map_objects.add(UCM_PIQUET_ENLARGED);
			map_objects.add(UCM_CABLE_INLET);

		scheme_objects.add(UCM_MUFF);
		scheme_objects.add(UCM_CABLE_LINEAR);
		scheme_objects.add(UCM_CABLE_OUTER);
		scheme_objects.add(UCM_CABLE_CROSSOVER);
		scheme_objects.add(UCM_CABLE_PLACE);
		scheme_objects.add(UCM_WELL_BINDING);
		scheme_objects.add(UCM_MUFF_PLACE);
		scheme_objects.add(UCM_CABLE_CONTINUED_END);
		scheme_objects.add(UCM_CABLE_INDOOR_END);
		scheme_objects.add(UCM_FIBRE);
		scheme_objects.add(UCM_CABLE_PROFILE);
		scheme_objects.add(UCM_CABLE_ALL);
		scheme_objects.add(UCM_MUFF_ALL);
		scheme_objects.add(UCM_LAYOUT);
		scheme_objects.add(UCM_PATCHCORD);
		scheme_objects.add(UCM_CABLE_CONTINUED);
		scheme_objects.add(UCM_VRM_PLACE);
		scheme_objects.add(UCM_ODF);
		scheme_objects.add(UCM_MUFF_WITH_LAYOUT);
		scheme_objects.add(UCM_CORD_END);
		scheme_objects.add(UCM_MUFF_TYPE);
		scheme_objects.add(UCM_ODF_PLAN);
		scheme_objects.add(UCM_SPARE);
		scheme_objects.add(UCM_CABLE_BACKBONE);
		scheme_objects.add(UCM_CABLE_2_CUTS);
		scheme_objects.add(UCM_FRAME_AND_VRM);
		scheme_objects.add(UCM_TELEPHONE_NODE);
		scheme_objects.add(UCM_TELEPHONE_NODE_OBJECT);
		scheme_objects.add(UCM_ATS);
		scheme_objects.add(UCM_WELL_AND_ODF_MUFF);
	}

	public UniCableMapType() {
	}
}

