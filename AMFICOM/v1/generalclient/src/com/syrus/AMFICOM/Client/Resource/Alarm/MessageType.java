package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/18 11:22:53 $
 * @author $Author: peskovsky $
 *
 * @deprecated
 */
public class MessageType extends StubResource
{
	/**
	 * Value: {@value}.
	 */
	public static final String ID_ALARM = "alarm";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_CRITICAL = "critical";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_DIAGNOSTICS = "diagnostics";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_ERROR = "error";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_INFO = "info";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_WARNING = "warning";

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ALARM = ID_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_CRITICAL = ID_CRITICAL;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_DIAGNOSTICS = ID_DIAGNOSTICS;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ERROR = ID_ERROR;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_INFO = ID_INFO;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_WARNING = ID_WARNING;

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_ALARM = "Сигнал тревоги";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_CRITICAL = "Критическая ошибка";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_DIAGNOSTICS = "Диагностическое";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_ERROR = "Ошибка";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_INFO = "Информационное";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_WARNING = "Предупредительное";

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_ALARM = NAME_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_CRITICAL = NAME_CRITICAL;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_DIAGNOSTICS = NAME_DIAGNOSTICS;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_ERROR = NAME_ERROR;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_INFO = NAME_INFO;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_WARNING = NAME_WARNING;

	public static String type = "messagetype";

	public String name = "";

	public String id = "";

	public MessageType()
	{
	}

	public MessageType(String id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public static MessageType[] createTypes (int number,String[] ids, String[] names)
	{
		MessageType[] result = new MessageType[number];
		for (int i = 0; i < number; i++)
		{
			result[i] = new MessageType(ids[i], names[i]);
			Pool.put(MessageType.type, result[i].getId(), result[i]);
		}
		return result;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
}
