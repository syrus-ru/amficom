package com.syrus.AMFICOM.Client.Resource.Object;

import com.syrus.AMFICOM.Client.Administrate.Object.UI.*;
import com.syrus.AMFICOM.Client.General.UI.*;

public class DomainForAdmin extends Domain
{


  public static PropertiesPanel getPropertyPane()
  {
    return new DomainPaneAdmin();
  }
}