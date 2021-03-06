/*-
 * $Id: DeliveryAttributesDatabase.java,v 1.10 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.LINKED_COLUMN_DELIVERY_ATTRIBUTES_ID;
import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.LINKED_COLUMN_ROLE_ID;
import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.LINKED_COLUMN_SYSTEM_USER_ID;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.DELIVERYATTRIBUTES_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.TableNames.DELIVERY_ATTRIBUTES_ROLE_LINK;
import static com.syrus.AMFICOM.general.TableNames.DELIVERY_ATTRIBUTES_SYSTEM_USER_LINK;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2006/03/28 10:17:19 $
 * @module event
 */
public final class DeliveryAttributesDatabase extends
		StorableObjectDatabase<DeliveryAttributes> {
	
	private static final String LINK_DELIVERY_ATTRIBUTES_ID = "delivery_attributes_id";
	
	private static String columns;
	private static String updateMultipleSQLValues;	
	
	/**
	 * @see StorableObjectDatabase#getEntityCode()
	 */
	@Override
	protected short getEntityCode() {
		return DELIVERYATTRIBUTES_CODE;
	}

	/**
	 * @see StorableObjectDatabase#getColumnsTmpl()
	 */
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_SEVERITY;
		}
		return columns;
	}

	/**
	 * @see StorableObjectDatabase#getUpdateMultipleSQLValuesTmpl()
	 */
	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues =  QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param deliveryAttributes
	 * @throws IllegalDataException
	 * @see StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final DeliveryAttributes deliveryAttributes)
	throws IllegalDataException {
		return Integer.toString(deliveryAttributes.getSeverity().ordinal());
	}

	/**
	 * @param deliveryAttributes
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final DeliveryAttributes deliveryAttributes,
			final PreparedStatement preparedStatement,
			int startParameterNumber)
	throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, deliveryAttributes.getSeverity().ordinal());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 * @see StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, ResultSet)
	 */
	@Override
	protected DeliveryAttributes updateEntityFromResultSet(final DeliveryAttributes storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final DeliveryAttributes deliveryAttributes = (storableObject == null)
				? new DeliveryAttributes(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						VOID_IDENTIFIER,
						null,
						ILLEGAL_VERSION,
						Severity.SEVERITY_NONE)
				: storableObject;
		deliveryAttributes.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				Severity.valueOf(resultSet.getInt(COLUMN_SEVERITY)));
		return deliveryAttributes;
	}
	
	private void retrieveLinksByOneQuery(final Set<DeliveryAttributes> deliveryAttributes) throws RetrieveObjectException {
		if ((deliveryAttributes == null) || (deliveryAttributes.isEmpty())) {
			return;
		}

		final Map<Identifier, Set<Identifier>> roleIdsMap = this.retrieveLinkedEntityIds(deliveryAttributes,
				DELIVERY_ATTRIBUTES_ROLE_LINK,
				LINK_DELIVERY_ATTRIBUTES_ID,
				LINKED_COLUMN_ROLE_ID);

		final Map<Identifier, Set<Identifier>> userIdsMap = this.retrieveLinkedEntityIds(deliveryAttributes,
			DELIVERY_ATTRIBUTES_SYSTEM_USER_LINK,
			LINK_DELIVERY_ATTRIBUTES_ID,
			LINKED_COLUMN_SYSTEM_USER_ID);
		
		for (final DeliveryAttributes attributes : deliveryAttributes) {
			final Identifier attributeId = attributes.getId();
			
			final Set<Identifier> roleIds = roleIdsMap.get(attributeId);			
			attributes.setRoleIds0(roleIds == null ? Collections.<Identifier>emptySet() : roleIds);
			
			final Set<Identifier> userIds = userIdsMap.get(attributeId);
			attributes.setSystemUserIds0(userIds == null ? Collections.<Identifier>emptySet() : userIds);
		}
	}

	@Override
	protected Set<DeliveryAttributes> retrieveByCondition(final String conditionQuery) 
	throws RetrieveObjectException, IllegalDataException {
		final Set<DeliveryAttributes> deliveryAttributes = super.retrieveByCondition(conditionQuery);
		
		this.retrieveLinksByOneQuery(deliveryAttributes);
		
		return deliveryAttributes;
	}

	/**
	 * @param deliveryAttributes
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 * @see StorableObjectDatabase#insert(Set)
	 */
	@Override
	protected void insert(final Set<DeliveryAttributes> deliveryAttributes)
	throws IllegalDataException, CreateObjectException {
		super.insert(deliveryAttributes);

		this.insertLinkedEntityIds(createRoleIdsMap(deliveryAttributes),
				DELIVERY_ATTRIBUTES_ROLE_LINK,
				LINKED_COLUMN_DELIVERY_ATTRIBUTES_ID,
				LINKED_COLUMN_ROLE_ID);
		this.insertLinkedEntityIds(createSystemUserIdsMap(deliveryAttributes),
				DELIVERY_ATTRIBUTES_SYSTEM_USER_LINK,
				LINKED_COLUMN_DELIVERY_ATTRIBUTES_ID,
				LINKED_COLUMN_SYSTEM_USER_ID);
	}

	/**
	 * @param deliveryAttributes
	 * @throws UpdateObjectException
	 * @see StorableObjectDatabase#update(Set)
	 */
	@Override
	protected void update(final Set<DeliveryAttributes> deliveryAttributes)
	throws UpdateObjectException {
		super.update(deliveryAttributes);

		this.updateLinkedEntityIds(createRoleIdsMap(deliveryAttributes),
				DELIVERY_ATTRIBUTES_ROLE_LINK,
				LINKED_COLUMN_DELIVERY_ATTRIBUTES_ID,
				LINKED_COLUMN_ROLE_ID);
		this.updateLinkedEntityIds(createSystemUserIdsMap(deliveryAttributes),
				DELIVERY_ATTRIBUTES_SYSTEM_USER_LINK,
				LINKED_COLUMN_DELIVERY_ATTRIBUTES_ID,
				LINKED_COLUMN_SYSTEM_USER_ID);
	}

	private static Map<Identifier, Set<Identifier>> createRoleIdsMap(final Set<DeliveryAttributes> deliveryAttributes) {
		final Map<Identifier, Set<Identifier>> roleIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final DeliveryAttributes deliveryAttribute : deliveryAttributes) {
			roleIdsMap.put(deliveryAttribute.getId(), deliveryAttribute.getRoleIds());
		}
		return roleIdsMap;
	}

	private static Map<Identifier, Set<Identifier>> createSystemUserIdsMap(final Set<DeliveryAttributes> deliveryAttributes) {
		final Map<Identifier, Set<Identifier>> userIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final DeliveryAttributes deliveryAttribute : deliveryAttributes) {
			userIdsMap.put(deliveryAttribute.getId(), deliveryAttribute.getSystemUserIds());
		}
		return userIdsMap;
	}
}
