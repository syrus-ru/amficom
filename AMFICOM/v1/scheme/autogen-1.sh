#!/bin/bash

ENTITY_NAMES="CableChannelingItem PathElement Scheme SchemeCableLink SchemeCablePort SchemeCableThread SchemeDevice SchemeElement SchemeLink SchemeMonitoringSolution SchemeOptimizeInfo SchemePath SchemePort SchemeProtoElement SchemeProtoGroup"
for ENTITY_NAME in ${ENTITY_NAMES}
do
	entity_name=`echo ${ENTITY_NAME} | sed 's/[A-Z]/\L&/'`
	cat <<EOF
	StorableObject load${ENTITY_NAME}(final Identifier id) throws ApplicationException;

	Collection load${ENTITY_NAME}s(final Collection ids) throws ApplicationException;

	Collection load${ENTITY_NAME}sButIds(final StorableObjectCondition storableObjectCondition, final Collection ids) throws ApplicationException;

	void save${ENTITY_NAME}(final ${ENTITY_NAME} ${entity_name}, final boolean force) throws ApplicationException;

	void save${ENTITY_NAME}s(final Collection ${entity_name}s, final boolean force) throws ApplicationException;

EOF
done
