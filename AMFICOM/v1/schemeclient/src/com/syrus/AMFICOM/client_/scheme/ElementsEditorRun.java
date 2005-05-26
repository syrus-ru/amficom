/*-
 * $Id: ElementsEditorRun.java,v 1.2 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

public class ElementsEditorRun {
	public static void main(String[] args) {
		new ElementsEditor(new DefaultSchematicsApplicationModelFactory());
	}
}


