MODULE_NAME := event

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/alarm.idl $(IDLDIR)/event.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)


LIBNAME_GENERALXML=generalxml.jar


DEPENDENCIES = \
administration \
general \
util \
reflectometry

EXTCLASSPATH := $(TROVE4JCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/event/*.java) \
 $(wildcard $(SRCDIR)/com/syrus/AMFICOM/eventv2/*.java)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/event \
		-C $(CLASSDIR) com/syrus/AMFICOM/eventv2

.mach: .corba $(CLASSDIR) $(CLASSDIR)/com/syrus/AMFICOM/eventv2
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/event/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/event/corba/.cvsignore | grep .java) && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/event/corba/.cvsignore | grep Package)
	cd $(SRCDIR)/com/syrus/AMFICOM/eventv2/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/eventv2/corba/.cvsignore | grep .java) && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/eventv2/corba/.cvsignore | grep Package)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/event/corba/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/eventv2/corba/*.java
	touch $@

.PHONY: clean
clean: cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/event/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/eventv2/corba
	$(RM) .corba

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	install -m 0644 $(LIBNAME) $(LIBDIR)


#### Archive ####

.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES)

.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/event text $(PMD_RULES) 

.javadocs: .idl $(JAVADOCDIR)
	$(JAVADOC) \
		$(JAVADOCFLAGS) \
		-sourcepath $(SRCDIR) \
		-classpath $(APPCLASSPATH) \
		-group "$(MODULE_NAME)" "com.syrus.AMFICOM.event:com.syrus.AMFICOM.eventv2" \
		-group "$(MODULE_NAME)-IDL" "com.syrus.AMFICOM.event.corba*:com.syrus.AMFICOM.eventv2.corba*" \
		-subpackages "com.syrus.AMFICOM.event:com.syrus.AMFICOM.eventv2" \
		2>&1 | grep -vE '(XMLInputStream|com\.syrus\.AMFICOM\.[a-z0-9]+\.corba\..*@serial)'
	touch $@
