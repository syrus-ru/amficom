package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class OrListBox extends ObjectResourceListBox
{

  public OrListBox()
  {
  }

  public List getVectorIDfromList()
  {
    int l = this.getModel().getSize();
    List v = new ArrayList();
    for(int i=0; i<l; i++)
    {
      String s = ((ObjectResource)this.getModel().getElementAt(i)).getId();
      v.add(s);
    }
    return v;
  }
}
