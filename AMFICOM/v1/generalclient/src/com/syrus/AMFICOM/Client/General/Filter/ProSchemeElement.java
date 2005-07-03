package com.syrus.AMFICOM.Client.General.Filter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class ProSchemeElement
{
  abstract String getTyp();
  public int x = 0;
  public int y = 0;

  public boolean selected = false;

  public ProSchemeElement()
  {
  }
}
