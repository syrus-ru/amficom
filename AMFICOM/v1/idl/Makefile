#
# $Id: Makefile,v 1.1 2004/06/03 08:14:20 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = idl_v1
MODULE_VERSION = $(shell date +%Y%m%d)

LIBNAME = corba.jar
LIBNAME_OBJECT = ServerObjectInterface.jar
LIBNAME_ADMIN = ServerInterface_Admin.jar
LIBNAME_ALARM = ServerInterface_Alarm.jar
LIBNAME_GENERAL = ServerInterface_General.jar
LIBNAME_ISM = ServerInterface_ISM.jar
LIBNAME_MAP = ServerInterface_Map.jar
LIBNAME_NETWORK = ServerInterface_Network.jar
LIBNAME_REPORT = ServerInterface_Report.jar
LIBNAME_SCHEME = ServerInterface_Scheme.jar
LIBNAME_SURVEY = ServerInterface_Survey.jar

IDLDIR = idl
SRCDIR = src
CLASSDIR = classes
ENCODING = windows-1251
JAVACFLAGS = -source 1.3 -sourcepath $(SRCDIR) -deprecation -g -O
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = \
	-target 1.2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	+E \
	+P \
	+Pmodifier-order \
	+Predundant-modifiers \
	+Pno-switchcheck \
	+Pnaming-convention \
	+T=4
IDL2JAVAFLAGS = \
	-C \
	-H \
	-no_bind \
	-no_examples \
	-narrow_compliance \
	-portable \
	-root_dir $(SRCDIR) \
	-strict \
	-no_toString

include Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

.PHONY: export
export: $(LIBNAME) $(LIBNAME_OBJECT) $(LIBNAME_ADMIN) $(LIBNAME_ALARM) $(LIBNAME_GENERAL) $(LIBNAME_ISM) $(LIBNAME_MAP) $(LIBNAME_NETWORK) $(LIBNAME_REPORT) $(LIBNAME_SCHEME) $(LIBNAME_SURVEY)

.PHONY: all
all: .all

.all: .idl
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(CLASSDIR) \
		-d $(CLASSDIR) \
		-encoding $(ENCODING) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

$(SRCDIR) $(CLASSDIR):
	mkdir -p $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	$(IDL2JAVA) \
		$(IDL2JAVAFLAGS) \
		$(IDLDIR)/AMFICOM.idl \
		$(IDLDIR)/admin.idl \
		$(IDLDIR)/alarm.idl \
		$(IDLDIR)/client.idl \
		$(IDLDIR)/constants.idl \
		$(IDLDIR)/general.idl \
		$(IDLDIR)/ISM.idl \
		$(IDLDIR)/map.idl \
		$(IDLDIR)/network.idl \
		$(IDLDIR)/report.idl \
		$(IDLDIR)/resource.idl \
		$(IDLDIR)/scheme.idl \
		$(IDLDIR)/survey.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r \
		$(SRCDIR)/* \
		.idl

.PHONY: clean
clean: idlclean
	$(RM) -r \
		$(CLASSDIR)/* \
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
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_portable_stub_AMFICOM.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClient.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHelper.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/AMFICOMClientHolder.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_AMFICOMClientImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_portable_stub_AMFICOMClient.class

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
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_ConstantsImplBase.class \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/_portable_stub_Constants.class

$(LIBNAME_ISM): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ISM \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/ISMDirectory

$(LIBNAME_MAP): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Map

$(LIBNAME_NETWORK): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Network \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/NetworkDirectory

$(LIBNAME_REPORT): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Report

$(LIBNAME_SCHEME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Scheme

$(LIBNAME_SURVEY): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/CORBA/Survey
