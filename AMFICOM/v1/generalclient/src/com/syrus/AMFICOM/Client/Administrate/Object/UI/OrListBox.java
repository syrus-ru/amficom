package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.util.*;


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

  public Vector getVectorIDfromList()
  {
    int l = this.getModel().getSize();
    Vector v = new Vector();
    for(int i=0; i<l; i++)
    {
      String s = ((ObjectResource)this.getModel().getElementAt(i)).getId();
//      System.out.println(s);
      v.add(s);
    }
    return v;
  }
}