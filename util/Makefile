#
# $Id: Makefile,v 1.6 2004/06/01 11:03:35 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = util
MODULE_VERSION = $(shell date +%Y%m%d)

USER = AMFICOM
PASSWORD = amficom
HOST = RESEARCH
SERVICE_NAME = SHAITAN

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
SQLJFLAGS = \
	-cache=true \
	-checkfilename=true \
	-checksource=true \
	-codegen=iso \
	-compile=true \
	-compiler-encoding-flag=true \
	-explain=true \
	-linemap=true \
	-parse=both \
	-password "$(PASSWORD)" \
	-profile=true \
	-ser2class=true \
	-status=false \
	-url "jdbc:oracle:oci:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=$(HOST))(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=$(SERVICE_NAME))))" \
	-user "$(USER)" \
	-warn=all

include Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)

.PHONY: all
all: sqlj sqljclean
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(AURORACLASSPATH):$(VBJCLASSPATH):$(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
		-d $(CLASSDIR) \
		-encoding $(ENCODING) \
	 	$(shell find $(SRCDIR) -name '*.java')

.PHONY: sqlj
sqlj: $(CLASSDIR)
	echo "sqlj.compiler-executable=$(JAVAC) $(JAVACFLAGS)" > sqlj.properties
	$(SQLJ) \
		$(SQLJFLAGS) \
		-classpath $(JDBCCLASSPATH):$(SQLJCLASSPATH):$(CLASSDIR) \
		-d $(CLASSDIR) \
		-encoding $(ENCODING) \
		$(SRCDIR)/com/syrus/util/database/ServerProperties.sqlj \
		$(SRCDIR)/com/syrus/util/Log.java \
		$(SRCDIR)/com/syrus/util/Logger.java

.PHONY: sqljclean
sqljclean:
	$(RM) \
		sqlj.properties \
		$(SRCDIR)/com/syrus/util/database/ServerProperties.java \
		$(SRCDIR)/com/syrus/util/database/ServerProperties.generated.java

$(CLASSDIR):
	mkdir -p $@

.PHONY: clean
clean: sqljclean

.PHONY: export
export: $(MODULE_NAME).jar $(MODULE_NAME)server.jar

$(MODULE_NAME).jar: all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

$(MODULE_NAME)server.jar: all
	-$(JAR) cf $@ \
        -C $(CLASSDIR) com/syrus/io/Rewriter.class \
        -C $(CLASSDIR) com/syrus/util/ByteArray.class \
        -C $(CLASSDIR) com/syrus/util/Log.class \
        -C $(CLASSDIR) com/syrus/util/Logger.class \
        -C $(CLASSDIR) com/syrus/util/ServerLogger.class \
        -C $(CLASSDIR) com/syrus/util/StolenLogger.class \
        -C $(CLASSDIR) com/syrus/util/corba/ORBUtil.class \
        -C $(CLASSDIR) com/syrus/util/corba/VisiBrokerORBUtil.class \
        -C $(CLASSDIR) com/syrus/util/corba/AuroraORBUtil.class \
        -C $(CLASSDIR) com/syrus/util/database/ByteArrayDatabase.class \
        -C $(CLASSDIR) com/syrus/util/database/DatabaseConnection.class \
        -C $(CLASSDIR) com/syrus/util/database/Server.class \
        -C $(CLASSDIR) com/syrus/util/database/ServerProperties.class \
        -C $(CLASSDIR) com/syrus/util/database/ServerProperties_SJProfile0.class \
        -C $(CLASSDIR) com/syrus/util/database/ServerProperties_SJProfileKeys.class

.PHONY: dist
dist: distclean
	tar cf ../$(MODULE_NAME)-$(MODULE_VERSION).tar ../$(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

.PHONY: distclean
distclean: clean
	$(RM) -r \
		$(CLASSDIR) \
		SQLChecker.cache \
		*.jar
