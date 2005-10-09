MODULE_NAME := commonclient

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

LIBNAME_GENERALXML=generalxml.jar

APPCLASSPATH := $(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/event.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/map.jar:$(LIBDIR)/mapview.jar:$(LIBDIR)/scheme.jar:$(LIBDIR)/csbridge.jar:$(LIBDIR)/filter.jar:$(LIBDIR)/leserver_interface.jar:$(LIBDIR)/cmserver_interface.jar:$(LIBDIR)/mscharserver_interface.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/kunststoff.jar:$(EXTLIBDIR)/trove.jar
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

