/*
 * $Id: CheckLoadersTestCase.java,v 1.1 2005/02/02 07:46:19 cvsadmin Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package test.com.syrus.AMFICOM.general;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/02 07:46:19 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class CheckLoadersTestCase extends TestCase {

	public CheckLoadersTestCase(String name) {
		super(name);
	}

	public void testGeneralObjectLoader() throws ClassNotFoundException, SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		for (short s = ObjectGroupEntities.GENERAL_GROUP_CODE; s < 0xFF; s++) {
			/*
			 * except EVENT_GROUP_CODE & SCHEME_GROUP_CODE due to these groups
			 * ara not exist
			 */
			if (s == ObjectGroupEntities.EVENT_GROUP_CODE || s == ObjectGroupEntities.SCHEME_GROUP_CODE)
				continue;
			String groupName = ObjectGroupEntities.codeToString(s);
			if (groupName == null)
				break;
			groupName = groupName.replaceAll("Group$", "");

			Class objectEntities = ObjectEntities.class;
			Field minEntityCodeField = objectEntities.getField(groupName.toUpperCase() + "_MIN_ENTITY_CODE");
			Field maxEntityCodeField = objectEntities.getField(groupName.toUpperCase() + "_MAX_ENTITY_CODE");

			short minEntityCode = minEntityCodeField.getShort(null);
			short maxEntityCode = maxEntityCodeField.getShort(null);

			String className = "com.syrus.AMFICOM." + groupName.toLowerCase() + "." + groupName + "ObjectLoader";
			System.out.println(className);
			Class clazz = Class.forName(className);
			Method[] methods = clazz.getMethods();

			for (short ec = minEntityCode; ec <= maxEntityCode; ec++) {
				/*
				 * except ObjectEntities.PERMATTR_ENTITY_CODE due to this entity
				 * is not exist
				 * ObjectEntities.SETPARAMETER_ENTITY_CODE, ObjectEntities.SETPARAMETER_ENTITY_CODE
				 * are not
				 * StorableObject and there is no reason operate this it
				 */
				if (ec == ObjectEntities.PERMATTR_ENTITY_CODE || ec == ObjectEntities.SETPARAMETER_ENTITY_CODE
						|| ec == ObjectEntities.RESULTPARAMETER_ENTITY_CODE)
					continue;
				String entityName = ObjectEntities.codeToString(ec);
				if (entityName != null) {
					String loadSimple = "load" + entityName;
					String saveSimple = "save" + entityName;

					String loadSeq = "load" + entityName + "s";
					String saveSeq = "save" + entityName + "s";
					if (entityName.equals(ObjectEntities.ANALYSIS_ENTITY)) {
						/* except Analysis due to its plural is Analyses */
						loadSeq = "loadAnalyses";
						saveSeq = "saveAnalyses";
					}

					String loadSeqButIds = loadSeq + "ButIds";

					boolean foundLoadSimple = false;
					boolean foundSaveSimple = false;
					boolean foundLoadSeqButIds = false;
					boolean foundLoadSeq = false;
					boolean foundSaveSeq = false;

					for (int i = 0; i < methods.length; i++) {
						String methodName = methods[i].getName();
						if (!foundLoadSimple && methodName.equals(loadSimple))
							foundLoadSimple = true;
						if (!foundSaveSimple && methodName.equals(saveSimple))
							foundSaveSimple = true;

						if (!foundLoadSeqButIds && methodName.equals(loadSeqButIds))
							foundLoadSeqButIds = true;

						if (!foundLoadSeq && methodName.equals(loadSeq))
							foundLoadSeq = true;
						if (!foundSaveSeq && methodName.equals(saveSeq))
							foundSaveSeq = true;

						if (foundLoadSimple && foundSaveSimple && foundLoadSeqButIds && foundLoadSeq && foundSaveSeq)
							break;
					}
					assertTrue(loadSimple + " method not found at " + className, foundLoadSimple);
					assertTrue(saveSimple + " method not found at " + className, foundSaveSimple);
					assertTrue(loadSeqButIds + " method not found at " + className, foundLoadSeqButIds);
					assertTrue(loadSeq + " method not found at " + className, foundLoadSeq);
					assertTrue(saveSeq + " method not found at " + className, foundSaveSeq);

				}
			}

		}
	}
}
