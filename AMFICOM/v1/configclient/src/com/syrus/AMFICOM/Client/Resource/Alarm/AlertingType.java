package com.syrus.AMFICOM.Client.Resource.Alarm;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;

public class AlertingType extends StubResource
{
	/**
	 * Value: {@value}.
	 */
	public static final String ID_EMAIL = "email";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_FAX = "fax";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_LOG = "log";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_PAGING = "paging";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_POPUP = "popup";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_SMS = "sms";

	/**
	 * Value: {@value}.
	 */
	public static final String ID_VOICE = "voice";

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_EMAIL = ID_EMAIL;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_FAX = ID_FAX;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_LOG = ID_LOG;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_PAGING = ID_PAGING;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_POPUP = ID_POPUP;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_SMS = ID_SMS;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_VOICE = ID_VOICE;

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_EMAIL = "�� E-mail";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_FAX = "�� �����";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_LOG = "������ � ���";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_PAGING = "�� ��������";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_POPUP = "����������� �����";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_SMS = "�� SMS";

	/**
	 * Value: {@value}.
	 */
	public static final String NAME_VOICE = "���������";	

  public static String type = "alertingtype";
  public String name = "";
  public String id = "";

  public AlertingType()
  {
  }

  public AlertingType(String id, String name)
  {
    this.id = id;
    this.name = name;
  }

  public static AlertingType[] createTypes (int number,String[] ids, String[] names)
  {
    AlertingType[] result = new AlertingType[number];
    for (int i = 0; i < number; i++)
	{
      result[i] = new AlertingType(ids[i], names[i]);
	  Pool.put(AlertingType.typ, result[i].getId(), result[i]);
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

