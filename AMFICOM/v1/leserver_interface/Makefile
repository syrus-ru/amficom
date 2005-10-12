#
# $Id: Makefile,v 1.5 2005/10/12 10:03:30 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := leserver_interface
IDL_SOURCES = $(IDLDIR)/leserver.idl

LIBNAME = $(MODULE_NAME).jar

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)

LIBNAME_GENERALXML = generalxml.jar

DEPENDENCIES = \
general \
administration \
event \
util

EXTCLASSPATH := $(TROVE4JCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)

$(LIBNAME): .corba
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/_LoginServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/LoginServerHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServer.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/_EventServerStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/leserver/corba/EventServerHolder.class

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/leserver/corba/*.java
	touch $@

.PHONY: clean
clean: cleancorba
	$(RM) $(LIBNAME)

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) .corba

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) .idl

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	install -m 0644 $(LIBNAME) $(LIBDIR)
