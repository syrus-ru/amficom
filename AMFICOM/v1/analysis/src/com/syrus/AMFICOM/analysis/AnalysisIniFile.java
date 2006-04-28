/*-
 * $Id: AnalysisIniFile.java,v 1.1 2006/04/28 11:19:21 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

/**
 * Заглушка для управления файлом analysis.ini.
 * На данный момент лишь описывает его имя, а сохранение данных
 * в нем производится каждым пользователем этого класса по своему умению.
 * <p>
 * Так сложилось исторически, а сейчас есть время лишь реализовать самое
 * необходимое - вынести имя файла analysis.ini в общий класс.
 * 
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/04/28 11:19:21 $
 * @module
 */
public class AnalysisIniFile {
	public static final String INI_FILE_NAME = "analysis.ini";
}
