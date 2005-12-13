MODULE_NAME := commonclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/commonclient.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

LIBNAME_GENERALXML=generalxml.jar

DEPENDENCIES = \
general \
administration \
configuration \
measurement \
event \
resource \
map \
mapview \
scheme \
csbridge \
reflectometry \
filter \
leserver_interface \
cmserver_interface \
mscharserver_interface \
util \
extensions

EXTCLASSPATH := $(EXTLIBDIR)/kunststoff.jar:$(TROVE4JCLASSPATH):$(XMLBEANSCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/event/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/model/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/resource/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/launcher/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/UI/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/UI/dialogs/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/UI/tree/*.java)


all: $(LIBNAME)

$(LIBNAME): .mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/resource/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/client/resource
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba: $(CLASSDIR) .idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java
	touch $@



.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) .corba

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/general/corba
	$(RM) .idl

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $^ $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/resource/*.properties)

