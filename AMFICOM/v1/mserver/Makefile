MODULE_NAME = mserver_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

INCIDLDIR=../idl_v1/idl

HOST=mongol
SERVICE_NAME=mongol

APPCLASSPATH = $(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar

IDL2JAVAFLAGS = -I$(INCIDLDIR)

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/DatabaseContextSetup.java
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) \
		$(IDLDIR)/mserver.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(VBJCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	rm -f $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mserver/*.class
	rm -f .mach

.PHONY:	cleancorba
cleancorba:
	rm -rf $(SRCDIR)/com/syrus/AMFICOM/mserver/corba
	rm -rf $(CLASSDIR)/com/syrus/AMFICOM/mserver/corba
	rm -f .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/mserver.idl \
		$(SRCDIR)/com/syrus/AMFICOM/mserver/*.java
