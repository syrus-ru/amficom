/**
 * $Id: Module.java,v 1.1 2004/07/28 12:27:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

/**
 * Интерфейс нужен для начальной инициализации данных модуля и завершающего
 * освобождения ресурсов модуля (отписки от событий, уничтожения файлов и т.д.)
 * при закрытии главного окна модуля
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/07/28 12:27:43 $
 * @module generalclient_v1
 * @author $Author: krupenn $
 * @see com.syrus.AMFICOM.Client.General.Command.ExitCommand#execute
 */
public interface Module 
{
	void initModule();
	void finalizeModule();
}