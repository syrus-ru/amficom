/*-
 * $Id: ConvertEquipmentType.java,v 1.1.2.1 2006/04/03 15:14:52 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.DOT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_CODE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/03 15:14:52 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class ConvertEquipmentType extends TestCase {
	private static final String EQUIPMENT_TYPE_OLD = "EquipmentTypeOld";

	public ConvertEquipmentType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTest(new ConvertEquipmentType("testConvert"));
		return commonTest.createTestSetup();
	}

	public void testConvert() throws ApplicationException {
		final Map<Identifier, String> oldProtoEquipmentTypeMap = this.retrieveOldProtoEquipmentTypes();
		System.out.println(oldProtoEquipmentTypeMap);

		final EquivalentCondition equivalentCondition = new EquivalentCondition(PROTOEQUIPMENT_CODE);
		final Set<ProtoEquipment> protoEquipments = StorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);

		equivalentCondition.setEntityCode(EQUIPMENT_TYPE_CODE);
		final Set<EquipmentType> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);
		final Map<String, EquipmentType> codenameIdentifierMap = StorableObjectType.createCodenamesMap(equipmentTypes);

		for (final ProtoEquipment protoEquipment : protoEquipments) {
			final Identifier protoEquipmentId = protoEquipment.getId();
			final String equipmentTypeCodename = oldProtoEquipmentTypeMap.get(protoEquipmentId);
			final EquipmentType equipmentType = codenameIdentifierMap.get(equipmentTypeCodename);
			System.out.println("Setting '" + protoEquipmentId + "' type '" + equipmentType.getCodename() + "'");
			protoEquipment.setTypeId(equipmentType.getId());
		}

		StorableObjectPool.flush(protoEquipments, LoginManager.getUserId(), false);
	}

	private Map<Identifier, String> retrieveOldProtoEquipmentTypes() throws RetrieveObjectException {
		final Map<Identifier, String> oldProtoEquipmentTypMap = new HashMap<Identifier, String>();

		final String sql = SQL_SELECT
				+ EQUIPMENT_TYPE_OLD + DOT + COLUMN_CODENAME + " " + COLUMN_CODENAME + COMMA
				+ PROTOEQUIPMENT + DOT + COLUMN_ID + " " + COLUMN_ID
				+ SQL_FROM + EQUIPMENT_TYPE_OLD + COMMA + PROTOEQUIPMENT
				+ SQL_WHERE + EQUIPMENT_TYPE_OLD + DOT + COLUMN_CODE + EQUALS + PROTOEQUIPMENT + DOT + COLUMN_TYPE_CODE;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				oldProtoEquipmentTypMap.put(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), resultSet.getString(COLUMN_CODENAME));
			}
		} catch (SQLException sqle) {
			Log.errorMessage(sqle);
			throw new RetrieveObjectException(sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}

		return oldProtoEquipmentTypMap;
	}
}
