/*
 * $Id: StringFieldConditionTestCase.java,v 1.5 2005/03/10 15:12:02 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import java.lang.reflect.Field;
import junit.framework.TestCase;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/03/10 15:12:02 $
 * @module resource_v1
 */
public class StringFieldConditionTestCase extends TestCase {
	public static void main(String[] args) {
		junit.awtui.TestRunner.run(StringFieldConditionTestCase.class);
	}

	/**
	 * Scheme is probably not on the classpath.
	 *
	 * @throws ApplicationException
	 */
	public final void testAbsentCtor() throws ApplicationException {
		final String string = "any"; //$NON-NLS-1$
		final short entityCode = ObjectEntities.SCHEME_MIN_ENTITY_CODE;
		final StringFieldSort sort = StringFieldSort.STRINGSORT_BASE;
		final StringFieldCondition stringFieldCondition = new StringFieldCondition(
			string,
			entityCode,
			sort);
		assertFalse(stringFieldCondition.isConditionTrue(null));
		assertEquals(stringFieldCondition.getString(), string);
		assertEquals(stringFieldCondition.getEntityCode().shortValue(), entityCode);
		assertEquals(stringFieldCondition.getSort().value(), sort.value());
	}

	public final void testInvalidCtor() {
		try {
			new StringFieldCondition(
				String.valueOf(ImageResourceSort._BITMAP),
				ObjectEntities.RESOURCE_MAX_ENTITY_CODE,
				StringFieldSort.STRINGSORT_INTEGER);
			fail("Either resource group is fully populated, or you run the test with assertions disabled."); //$NON-NLS-1$
		} catch (AssertionError ae) {
			assertTrue(true);
		}
	}

	public final void testToString() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		final String string = String.valueOf(ImageResourceSort._BITMAP);
		final short entityCode = ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE;
		final StringFieldSort sort = StringFieldSort.STRINGSORT_INTEGER;
		final StringFieldCondition stringFieldCondition = new StringFieldCondition(
			string,
			entityCode,
			sort);

		final Field field = StringFieldCondition.class.getDeclaredField("delegate"); //$NON-NLS-1$
		field.setAccessible(true);

		System.out.println(stringFieldCondition);
		System.out.println(field.get(stringFieldCondition));
	}

	public final void testValidCtor() {
		final String string = String.valueOf(ImageResourceSort._BITMAP);
		final short entityCode = ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE;
		final StringFieldSort sort = StringFieldSort.STRINGSORT_INTEGER;
		StringFieldCondition stringFieldCondition = new StringFieldCondition(
			string,
			entityCode,
			sort);
		assertEquals(stringFieldCondition.getString(), string);
		assertEquals(stringFieldCondition.getEntityCode().shortValue(), entityCode);
		assertEquals(stringFieldCondition.getSort().value(), sort.value());
	}
}
