#
# $Id: Makefile,v 1.17 2005/01/18 07:29:55 bob Exp $
#
# vim:ft=make:
#

MODULE_NAME = idl_v1

LIBNAME = corba.jar
LIBNAME_OBJECT = ServerObjectInterface.jar
LIBNAME_ADMIN = ServerInterface_Admin.jar
LIBNAME_ALARM = ServerInterface_Alarm.jar
LIBNAME_GENERAL = ServerInterface_General.jar
LIBNAME_REPORT = ServerInterface_Report.jar

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

.PHONY: export
export: $(LIBNAME) $(LIBNAME_OBJECT) $(LIBNAME_ADMIN) $(LIBNAME_ALARM) $(LIBNAME_GENERAL) $(LIBNAME_REPORT)

.PHONY: all
all: .all

.all: .idl
	$(JAVAC) \
		$(JAVACFLAGS) \
		$(JAVAC_WARNOFF_FLAG) \
		-classpath $(AMFICOMCLASSPATH)$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

# Target created for IDEs compiling everything from scratch not to tie client
# code to (cm)server implementation.
.PHONY: xtra
xtra: .xtra

.xtra: .idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/event.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/configuration.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/measurement.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/cmserver.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/lilmapletty.idl
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/AMFICOM.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/admin.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/alarm.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/client.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/constants.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/report.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/resource.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/administration.idl

	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r \
		$(SRCDIR)/* \
		.idl

.PHONY: xtraclean
xtraclean: idlclean
	$(RM) -r \
		.xtra

$(SRCDIR) $(CLASSDIR):
	mkdir -p $@

.PHONY: clean
clean: xtraclean
	$(RM) \
		.all

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

.PHONY: distclean
distclean: clean
	$(RM) -r \
		$(SRCDIR) \
		$(CLASSDIR) \
		*.jar

$(LIBNAME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

$(LIBNAME_OBJECT): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMPOA.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMPOATie.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMStub.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClient.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientPOA.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientPOATie.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMClientStub.class

$(LIBNAME_ADMIN): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Admin

$(LIBNAME_ALARM): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Alarm

$(LIBNAME_GENERAL): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Constant \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/General \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Resource \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Constants.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsOperations.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsPOA.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ConstantsPOATie.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_ConstantsStub.class

$(LIBNAME_REPORT): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Report
