package com.syrus.AMFICOM.Client.General.Model;

public class AdminApplicationModelFactory
    implements ApplicationModelFactory
{
  public AdminApplicationModelFactory()
  {
  }

  public ApplicationModel create()
  {
    ApplicationModel aModel = new AdminApplicationModel();
    return aModel;
  }
}