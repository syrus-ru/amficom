package com.syrus.AMFICOM.Client.Resource.Object;

public interface AdminObjectRWXresource
{
  public void SetUserR(boolean t);
  public void SetUserW(boolean t);
  public void SetUserX(boolean t);
  public void SetGroupR(boolean t);
  public void SetGroupW(boolean t);
  public void SetGroupX(boolean t);
  public void SetOtherR(boolean t);
  public void SetOtherW(boolean t);
  public void SetOtherX(boolean t);

  public void SetAllR(boolean t);
  public void SetAllW(boolean t);
  public void SetAllX(boolean t);
  public void SetRWX(boolean t);

  public boolean getUserR();
  public boolean getUserW();
  public boolean getUserX();
  public boolean getGroupR();
  public boolean getGroupW();
  public boolean getGroupX();
  public boolean getOtherR();
  public boolean getOtherW();
  public boolean getOtherX();
}