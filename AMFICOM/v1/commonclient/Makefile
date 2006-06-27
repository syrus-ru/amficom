MODULE_NAME := commonclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

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
systemserver_interface \
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
		-C $(CLASSDIR) com \
		-C $(CLASSDIR) images \
		-C $(CLASSDIR) xml

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@


.PHONY:	clean
clean:	cleanmach
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $^ $(LIBDIR)

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SOURCES) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/client/resource/*.properties)

