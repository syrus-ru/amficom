/*-
 * $Id: SchemeEditorRun.java,v 1.1 2005/04/05 14:10:45 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:10:45 $
 * @module schemeclient_v1
 */

public class SchemeEditorRun {
	public static void main(String[] args) {
		new SchemeEditor(new DefaultSchematicsApplicationModelFactory());
	}
}
