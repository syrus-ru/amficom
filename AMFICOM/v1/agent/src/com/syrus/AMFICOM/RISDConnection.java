package com.syrus.AMFICOM;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import oracle.aurora.jndi.sess_iiop.ServiceCtx;
import com.syrus.util.Log;

public class RISDConnection {
  public static final String DEFAULT_serviceURL = "sess_iiop://research:2481:research";
	public static final String DEFAULT_user = "amficom";
	public static final String DEFAULT_password = "amficom";
  private String serviceURL;
  private String username;
  private String password;

  public RISDConnection() {
    this.setDefaults();
  }
  
  public RISDConnection(String serviceURL, String username, String password) {
    this.serviceURL = serviceURL;
  	this.username = username;
		this.password = password;
  }

  public void setDefaults() {
    this.serviceURL = DEFAULT_serviceURL;
    this.username = DEFAULT_user;
    this.password = DEFAULT_password;
  }

  public Object getServerObject(String objectName) {
    Hashtable env = new Hashtable ();
		env.put(Context.URL_PKG_PREFIXES, "oracle.aurora.jndi");
		env.put(Context.SECURITY_PRINCIPAL, this.username);
		env.put(Context.SECURITY_CREDENTIALS, this.password);
		env.put(Context.SECURITY_AUTHENTICATION, ServiceCtx.NON_SSL_LOGIN);
    Object serverObject = null;
    try {
      InitialContext ic = new InitialContext(env);
      serverObject = ic.lookup(this.serviceURL + objectName);
    }
    catch(Exception ex) {
      Log.errorException(ex);
    }
    return serverObject;
  }
}
