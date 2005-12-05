#
# $Id: Makefile.java.inc.mk,v 1.11 2005/12/05 18:24:27 bass Exp $
#
# vim:set ft=make:
#

exesuffix =

# User may override MODULE_VERSION
MODULE_VERSION ?= $(shell date +%Y%m%d)

SRC_SUFFIXES = \
	.java \
	.generated.java

CLASS_SUFFIXES = \
	.class \
	$$%.class

IDLDIR := ../idl/idl
SRCDIR := src
CLASSDIR := classes
JAVADOCDIR := docs/api

AMFICOM_HOME := $(shell echo $(AMFICOM_HOME) | sed "s|~|$(HOME)|g")

LIBDIR = $(AMFICOM_HOME)/lib
EXTLIBDIR = $(AMFICOM_HOME)/../extlib

EMPTY =
SPACE = $(EMPTY) $(EMPTY)
APPCLASSPATH = $(subst $(SPACE),:,$(foreach DEPENDENCY,$(DEPENDENCIES),$(LIBDIR)/$(DEPENDENCY).jar))

checkenve = $(if $($(1)), , $(error $(1) is undefined. Define it prior to issuing "$(MAKE)"))
checkenvw = $(if $($(1)), , $(warning $(1) is undefined. Define it prior to issuing "$(MAKE)"))
pathsearch = $(firstword $(wildcard $(addsuffix /$(1), $(subst :, , $(PATH)))))

# User may override database connection settings
USER = AMFICOM
PASSWORD ?= amficom
HOST ?= amficom
SERVICE_NAME ?= jamaica

sqlj = $(firstword $(wildcard $(addsuffix /bin/sqlj$(exesuffix), $(1))))
SQLJ = $(call sqlj,$(ORACLE_HOME))
ifeq ($(SQLJ),)
SQLJ = $(call pathsearch,sqlj$(exesuffix))
endif
SQLJFLAGS = \
	-cache=true \
	-checkfilename=true \
	-checksource=true \
	-codegen=oracle \
	-compile=true \
	-compiler-encoding-flag=false \
	-explain=true \
	-linemap=true \
	-parse=both \
	-password "$(PASSWORD)" \
	-profile=true \
	-status=true \
	-url "jdbc:oracle:oci:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=$(HOST))(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=$(SERVICE_NAME))))" \
	-user "$(USER)" \
	-warn=all \
	-d $(CLASSDIR) \
	-encoding $(ENCODING)

JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/ojdbc14.jar)
ifeq ($(JDBCCLASSPATH),)
JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/ojdbc14.zip)
endif
ifeq ($(JDBCCLASSPATH),)
JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/classes12.jar)
endif
ifeq ($(JDBCCLASSPATH),)
JDBCCLASSPATH = $(wildcard $(ORACLE_HOME)/jdbc/lib/classes12.zip)
endif

runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12ee.jar)
ifeq ($(runtime12),)
runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12ee.zip)
endif
ifeq ($(runtime12),)
runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12.jar)
endif
ifeq ($(runtime12),)
runtime12 = $(wildcard $(ORACLE_HOME)/sqlj/lib/runtime12.zip)
endif
translator = $(wildcard $(ORACLE_HOME)/sqlj/lib/translator.jar)
ifeq ($(translator),)
translator = $(wildcard $(ORACLE_HOME)/sqlj/lib/translator.zip)
endif
SQLJCLASSPATH = $(runtime12):$(translator)

# User may override AMFICOMCLASSPATH and/or EXTCLASSPATH
AMFICOMCLASSPATH ?= $(shell find $(LIBDIR) -name '*\.[jz][ai][pr]' -printf '%p:')
EXTCLASSPATH ?= $(shell find $(EXTLIBDIR) -name '*\.[jz][ai][pr]' -printf '%p:')

jdevrt = $(wildcard $(ORACLE_HOME)/lib/jdev-rt.jar)
ifeq ($(jdevrt),)
jdevrt = $(wildcard $(ORACLE_HOME)/jdev/lib/jdev-rt.jar)
endif
ifeq ($(jdevrt),)
jdevrt = $(wildcard $(ORACLE_HOME)/lib/jdev-rt.zip)
endif
ifeq ($(jdevrt),)
jdevrt = $(wildcard $(ORACLE_HOME)/jdev/lib/jdev-rt.zip)
endif
DEPRECATEDCLASSPATH = $(jdevrt)

# If XMLBEANS_HOME is defined, search in ${XMLBEANS_HOME}/lib
ifeq ($(XMLBEANS_HOME),)
XMLBEANSCLASSPATH = $(EXTLIBDIR)/xbean.jar:$(EXTLIBDIR)/jsr173_api.jar
else
# XMLBEANS_HOME found.
XMLBEANSLIBDIR = $(XMLBEANS_HOME)/lib
XMLBEANSCLASSPATH = $(XMLBEANSLIBDIR)/xbean.jar:$(XMLBEANSLIBDIR)/jsr173_api.jar
endif

XMLDIR ?= xml
ifeq ($(shell hostname -s),bass)
XMLSRCDIR = $(SRCDIR)
XMLCLASSDIR = $(CLASSDIR)
else
XMLSRCDIR = $(XMLDIR)/src
XMLCLASSDIR = $(XMLDIR)/classes
endif

SCOMP=$(shell which scomp 2>/dev/null)
ifeq ($(SCOMP),)
SCOMP=$(XMLBEANS_HOME)/bin/scomp
endif

SCOMPFLAGS = -javasource 1.5 -src $(XMLSRCDIR) -d $(XMLCLASSDIR)

# Trove4J
ifeq ($(TROVE4J_HOME),)
TROVE4JCLASSPATH = $(EXTLIBDIR)/trove.jar
else
TROVE4JCLASSPATH = $(TROVE4J_HOME)/lib/trove.jar
endif

# JavaBeans Activation Framework
ifeq ($(JAF_HOME),)
JAFCLASSPATH = $(EXTLIBDIR)/activation.jar
else
JAFCLASSPATH = $(JAF_HOME)/activation.jar
endif

# JavaMail
ifeq ($(JAVAMAIL_HOME),)
JAVAMAILCLASSPATH = $(EXTLIBDIR)/mail.jar
else
JAVAMAILCLASSPATH = $(JAVAMAIL_HOME)/mail.jar
endif

# Xalan-J
ifeq ($(XALANJ_HOME),)
XALANJCLASSPATH := $(EXTLIBDIR)/xalan.jar
else
XALANJCLASSPATH := $(XALANJ_HOME)/bin/xalan.jar
endif

ENCODING ?= KOI8-R
JAVAC_WARNOFF_FLAG = -nowarn
# User may specify either -source 1.3 or -source 1.4 as an initial value
# of JAVACFLAGS
JAVACFLAGS += \
	-Xlint \
	-source 1.5 \
	-target 1.5 \
	-O \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-encoding $(ENCODING) \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g
# User may specify -target 1.1/1.2/1.3/1.4
SUNJAVACFLAGS +=
# User may specify -target 1.1/1.2/1.3/1.4/1.4.2 
JIKESFLAGS += \
	+E \
	+P \
	+Pall \
	+Pno-switchcheck \
	+T=2
ifeq ($(JAVAC),)
JAVAC = $(JAVA_HOME)/bin/javac
endif
JAVACFLAGS := $(JAVACFLAGS) $(SUNJAVACFLAGS)

CP = $(call pathsearch,cp$(exesuffix))
ifeq ($(CP),)
CP = cp
endif
ifeq ($(CP),)
CP = copy
endif

JAR = $(call pathsearch,jar-3.4.1$(exesuffix))
ifeq ($(JAR),)
JAR = $(call pathsearch,jar$(exesuffix))
endif
ifeq ($(JAR),)
JAR = $(JAVA_HOME)/bin/jar
endif

JAVA = $(JAVA_HOME)/bin/java

# Default IDL to Java compiler type, one of:
#	IDLJ,
#	VBJ_IDL2JAVA,
#	TCOO_IDL2JAVA,
#	ORBACUS_JIDL.
# May be changed by user.
IDL2JAVA ?= IDLJ

# Sun idlj
# Do not modify!
IDLJ = $(JAVA_HOME)/bin/idlj
IDLJFLAGS = -fallTIE -i $(IDLDIR) -td $(SRCDIR)

# Borland VisiBroker for Java idl2java
# Do not modify!
VBJ_IDL2JAVA = $(VBROKER_HOME)/bin/idl2java
VBJ_IDL2JAVAFLAGS = -DMY_COMPILER_IS_NOT_BRAINDEAD -package com.syrus.AMFICOM

# The Community OpenORB idl2java
# Do not modify!
TCOO_IDL2JAVA = $(TCOO_HOME)/OpenORB/bin/idl2java
TCOO_IDL2JAVAFLAGS = -DNO_STRUCT_RECURSION -DMY_COMPILER_IS_NOT_BRAINDEAD

# IONA ORBacus jidl
# Do not modify!
ORBACUS_JIDL = jidl
ORBACUS_JIDLFLAGS = -I. -DMY_COMPILER_IS_NOT_BRAINDEAD --auto-package

# Figuring out what flags to use based on user's choice.
# Do not modify!
IDL2JAVAFLAGS := $($(IDL2JAVA)FLAGS)
IDL2JAVA := $($(IDL2JAVA))

# Modules to build IDL within.
IDL_MODULES = \
reflectometry \
event \
general \
resource \
admin \
config \
measurement \
map \
scheme \
mapview \
cmserver_interface \
leserver_interface \
mcm_interface \
mscharserver_interface \
mserver_interface \
commonclient \
report

# Figuring out what command(s) to run for "idl" target.
ifeq ($(IDL_SOURCES),)
ifeq ($(MODULE_NAME),idl)
IDL_COMMAND = $(foreach idl_module,$(IDL_MODULES),$(MAKE) -C ../$(idl_module) idl;)
else
IDL_COMMAND = $(error IDL compilation is not supported for module: $(MODULE_NAME))
endif
else
IDL_COMMAND = $(foreach idl_source,$(IDL_SOURCES),$(IDL2JAVA) $(IDL2JAVAFLAGS) $(idl_source);)
endif

PMD_JAR = $(shell find $(PMD_HOME) -name pmd-*.jar)
PMD_JARS = $(shell find $(PMD_HOME) -name '*\.[jz][ai][pr]' -printf '%p:')

PMD_RULES = rulesets/unusedcode.xml,rulesets/imports.xml,rulesets/basic.xml,rulesets/clone.xml,rulesets/codesize.xml,rulesets/coupling.xml,rulesets/design.xml,rulesets/favorites.xml,rulesets/junit.xml,rulesets/naming.xml,rulesets/newrules.xml,rulesets/scratchpad.xml,rulesets/strictexception.xml,rulesets/strings.xml

#
# Javadoc
#
JAVADOCHOST = bass.science.syrus.ru
HTTP_DOCROOT = /var/www/html
JAVADOC = $(JAVA_HOME)/bin/javadoc
JAVADOCFLAGS = \
	-locale ru_RU \
	-private \
	-quiet \
	-encoding $(ENCODING) \
	-d $(JAVADOCDIR) \
	-use \
	-version \
	-author \
	-splitindex \
	-windowtitle "$(MODULE_NAME)" \
	-doctitle "$(MODULE_NAME)" \
	-linksource \
	-serialwarn \
	-charset $(ENCODING) \
	-docencoding $(ENCODING) \
	-tag todo:a:"To Do:" \
	-tag bug:a:"Bug:" \
	-tag module:t:"Module:" \
	-notimestamp \
	$(foreach DEPENDENCY,$(DEPENDENCIES),-link "http://$(JAVADOCHOST)/apidocs/amficom/$(DEPENDENCY)/") \
	-link "http://$(JAVADOCHOST)/apidocs/jdk/"

#
# Targets
#

$(SRCDIR) $(CLASSDIR) $(LIBDIR) $(EXTLIBDIR) $(JAVADOCDIR):
	mkdir -p $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(IDL_SOURCES)
	$(IDL_COMMAND)
	touch $@

.PHONY: javadocs
javadocs: .javadocs

.PHONY: javadocsclean
javadocsclean:
	$(RM) -r \
		$(wildcard $(JAVADOCDIR)/*) \
		.javadocs

.PHONY: javadocsinstall
javadocsinstall: .javadocs javadocsuninstall
	ln -s $(AMFICOM_HOME)/$(MODULE_NAME)/$(JAVADOCDIR) $(HTTP_DOCROOT)/apidocs/amficom/$(MODULE_NAME)

.PHONY: javadocsuninstall
javadocsuninstall:
	$(RM) $(HTTP_DOCROOT)/apidocs/amficom/$(MODULE_NAME)

.PHONY: distclean
distclean: javadocsclean clean
	$(RM) -r \
		$(wildcard $(CLASSDIR)/*) \
		$(wildcard *.jar) \
		.xml \
		.idl \
		.mach \
		.corba
