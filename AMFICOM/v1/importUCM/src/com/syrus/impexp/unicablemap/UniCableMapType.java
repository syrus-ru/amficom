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

	public static final String UCM_TYPE = "���";

	public static final String UCM_DB_OBJECT = "������ ��";
	public static final String UCM_OBJECT = "������";
	public static final String UCM_GEO_OBJECT = "��� ������";

	public static final String UCM_LAYER = "����";
	public static final String UCM_POINT = "�����";
	public static final String UCM_CUT = "�������";
	public static final String UCM_POLYLINE = "�������";
	public static final String UCM_CIRCLE = "����";
	public static final String UCM_RECTANGLE = "�������������";
	public static final String UCM_POLYGONE = "�������������";

	public static final String UCM_CITY = "�����";
	public static final String UCM_STREET_GENERAL = "����� ����������";
	public static final String UCM_STREET = "�����";
	public static final String UCM_BUILDING = "���";
	public static final String UCM_BUILDING_PLAN = "���� ����";
	public static final String UCM_FLOOR = "����";
	public static final String UCM_ADDRESS = "��������";
	public static final String UCM_PLAN = "����";

	public static final String UCM_LINK_TYPE = "��� �����";

	public static final String UCM_COMMUTATOR = "����������";
	public static final String UCM_NAME = "�������� �� ������ � ������";
	public static final String UCM_NAME_TRANSFER = "�������� ��������";
	public static final String UCM_LABEL_BY_NAME = "������� �� ��������";
	public static final String UCM_LABEL_BY_NUMBER = "������� �� ������";
	public static final String UCM_QUERY_SAMPLE = "������� ����������";
	public static final String UCM_CASCADE_UPDATE = "��������� ����������";
	public static final String UCM_QUERY_OBJECT = "������ ����������";
	public static final String UCM_CHAIN = "�������";
	public static final String UCM_MATRIX = "�������";
	public static final String UCM_FOLDER = "�����";
	public static final String UCM_SYSTEM = "���������";
	public static final String UCM_FOLDER_CONTENTS = "���������� �����";
	public static final String UCM_OPTICS = "������";
	public static final String UCM_FOR_QUERY = "��� ����������";
	public static final String UCM_FOR_CITY = "��� ������";
	public static final String UCM_TEMPLATE = "������";
	public static final String UCM_LINK_ABSTRACT = "����� �����������";
	public static final String UCM_DEFINITION = "�����������";
	public static final String UCM_DEFINED_LINK = "������������ �����";
	public static final String UCM_LINK = "�����";
	public static final String UCM_COMPOUND_OBJECT = "��������� ������";
	public static final String UCM_EVOLVEMENT = "���������";
	public static final String UCM_FULL_SCREEN = "�� ���� �����";
	public static final String UCM_START_POINT = "��������� �����";
	public static final String UCM_SUBSCRIBER = "�������";
	public static final String UCM_NAME_BY_ABBREVIATION = "�������� �� ����������";
	public static final String UCM_NUMBER_TRANSFER = "�������� ������";
	public static final String UCM_STYLE = "�����";
	public static final String UCM_QUERY = "������";
	public static final String UCM_QUERY_TEMPLATE = "������ �������";
	public static final String UCM_CONTRACTOR = "���������";
	public static final String UCM_QUERY_LINK = "����� �������";
	public static final String UCM_NAME_BY_NUMBER = "�������� �� ������";
	public static final String UCM_PROJECT = "������";
	public static final String UCM_PROJECT_OBJECT = "������ �������";
	public static final String UCM_PAIR = "������";
	public static final String UCM_TEMPLATE_ALL = "��� �������";
	public static final String UCM_COPIED = "����������";
	public static final String UCM_DEFINITION_AREA = "������� �����������";
	public static final String UCM_END_POINT = "�������� �����";
	public static final String UCM_QUERY_AREA = "������� ��������";
	public static final String UCM_MODELLER_OBJECT = "������ ����������";
	public static final String UCM_SIGNATURE_TRANSFER = "�������� �������";
	public static final String UCM_QUERY_ZONE = "���� �������";
	public static final String UCM_QUERY_ZONE_POLYGONE = "���� ������� - �������������";
	public static final String UCM_QUERY_ZONE_CIRCLE = "���� ������� - ����";
	public static final String UCM_QUERY_TYPE_FOLDER = "����� ����� ��� �������";
	public static final String UCM_DIRECTORY = "����������";
	public static final String UCM_ANY_OBJECT = "����� ������";
	public static final String UCM_QUERY_LINK_TYPE_FOLDER = "����� ����� ����� ��� �������";
	public static final String UCM_BUFFER = "�����";
	public static final String UCM_BUFFERED = "������������";
	public static final String UCM_PORT = "����";
	public static final String UCM_MULTIPLEXOR_TYPE = "��� ��������������";
	public static final String UCM_QUERY_AND_DEFINITION_AREA = "������� �������� � �����������";

	public static final String UCM_USER = "������������";
	public static final String UCM_PARAMETER_PASSWORD = "�������� ������";

	public static final String UCM_PARAMETER = "��������";
	public static final String UCM_PARAMETER_INTEGER = "�������� �����";
	public static final String UCM_PARAMETER_FLOAT = "�������� ������������";
	public static final String UCM_PARAMETER_DROPDOWN_LIST = "�������� ���������� ������";
	public static final String UCM_PARAMETER_DATE = "�������� ����";
	public static final String UCM_PARAMETER_BOOLEAN = "�������� ����������";
	public static final String UCM_PARAMETER_REFERENCE = "�������� ������";
	public static final String UCM_PARAMETER_STRING = "�������� ������";
	public static final String UCM_PARAMETER_IMAGE = "�������� �����������";
	public static final String UCM_PARAMETER_ABSTRACT = "�������� �����������";
	public static final String UCM_PARAMETER_REAL = "�������� ��������";
	public static final String UCM_PARAMETER_GRAPHICAL_LINK = "�������� ����������� �����";
	public static final String UCM_PARAMETER_BUTTON = "�������� ������";
	public static final String UCM_PARAMETER_DEPENDENCY = "����������� ����������";
	public static final String UCM_PARAMETER_PICTURE = "�������� ��������";

	public static final String UCM_LINK_TYPE_GENERAL = "��� ����� ����������";
	public static final String UCM_LINK_TYPE_REAL = "��� ����� ��������";

	public static final String UCM_CABLE_LINEAR = "������ ��������";
	public static final String UCM_WELL_GENERAL = "������� ����������";
	public static final String UCM_WELL_ENLARGED = "������� ������";
	public static final String UCM_TUNNEL_GENERAL = "������� ����������";
	public static final String UCM_CABLE_OUTER = "������ �������";
	public static final String UCM_TUNNEL_PROFILE = "������� ������";
	public static final String UCM_BLOCK = "����";
	public static final String UCM_PIPE = "�����";
	public static final String UCM_WELL = "�������";
	public static final String UCM_MUFF = "�����";
	public static final String UCM_CABLE_PLACE = "����� ������";
	public static final String UCM_TUNNEL = "�������";
	public static final String UCM_CABLE_CROSSOVER = "������ ����������";
	public static final String UCM_WELL_BINDING = "�������� �������";
	public static final String UCM_CABLE_INLET = "��������� ����";
	public static final String UCM_WELL_GENERAL_ENLARGED = "������� ���������� ������";
	public static final String UCM_FRAME = "������";
	public static final String UCM_MODEM = "�����";
	public static final String UCM_MUFF_PLACE = "����� ��� �����";
	public static final String UCM_CABLE_CONTINUED_END = "������ ����������� �����";
	public static final String UCM_CABLE_INDOOR_END = "������ ���������� �����";
	public static final String UCM_CABLE_GENERAL = "������ ����������";
	public static final String UCM_MUFF_GENERAL = "����� ����������";
	public static final String UCM_WELL_TYPE = "��� �������";
	public static final String UCM_CABLE_TYPE = "��� ������";
	public static final String UCM_WELL_OR_TUNNEL = "������� ��� �������";
	public static final String UCM_FIBRE = "�������";
	public static final String UCM_CABLE_PROFILE = "������ ������";
	public static final String UCM_CABLE_ALL = "��� ������";
	public static final String UCM_MUFF_ALL = "��� �����";
	public static final String UCM_TUNNELING = "�����������";
	public static final String UCM_LAYOUT = "��������";
	public static final String UCM_PATCHCORD = "�������";
	public static final String UCM_CABLE_CONTINUED = "������ �����������";
	public static final String UCM_CABLE_INDOOR_GENERAL = "������ ���������� ����������";
	public static final String UCM_VRM_PLACE = "����� ��� ���";
	public static final String UCM_ODF = "ODF";
	public static final String UCM_MUFF_WITH_LAYOUT = "����� � ���������";
	public static final String UCM_SUBSCRIBER_MODEM = "����� ��������";
	public static final String UCM_CORD_END = "����� �����";
	public static final String UCM_PIQUET = "�����";
	public static final String UCM_PIQUET_ENLARGED = "����� ������";
	public static final String UCM_COLLECTOR_FRAGMENT = "������� ����������";
	public static final String UCM_COLLECTOR = "���������";
	public static final String UCM_WELL_OR_INLET = "������� ��� ����";
	public static final String UCM_MUFF_TYPE = "��� �����";
	public static final String UCM_ODF_PLAN = "���� ODF";
	public static final String UCM_LAYOUT_ABSTRACT = "�������� �����";
	public static final String UCM_SPARE = "�����";
	public static final String UCM_CABLE_BACKBONE = "������ �������������";
	public static final String UCM_RECEIVER_MODEM = "����� ����������";
	public static final String UCM_STREET_MODEM = "����� �� ����";
	public static final String UCM_CABLE_2_CUTS = "������ 2 �������";
	public static final String UCM_FRAME_AND_VRM = "������ � ���";
	public static final String UCM_TELEPHONE_NODE = "���������� ����";
	public static final String UCM_TELEPHONE_NODE_OBJECT = "������ ����������� ����";
	public static final String UCM_ATS = "���";
	public static final String UCM_WELL_AND_ODF_MUFF = "������� � ODF/�����";

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

