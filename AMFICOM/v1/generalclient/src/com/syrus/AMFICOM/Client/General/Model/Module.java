/**
 * $Id: Module.java,v 1.2 2005/04/13 21:40:46 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/04/13 21:40:46 $
 * @module generalclient_v1
 * @author $Author: arseniy $
 * @see com.syrus.AMFICOM.Client.General.Command.ExitCommand#execute
 */
public interface Module 
{
	void initModule();
	void finalizeModule();
}
