MODULE_NAME := map

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.bz2

IDL_SOURCES = $(IDLDIR)/map.idl

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)


LIBNAME_GENERALXML=generalxml.jar
LIBNAME_MAPXML=mapxml.jar

APPCLASSPATH = $(LIBDIR)/configuration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/util.jar:$(LIBDIR)/filter.jar

XMLCLASSPATH := $(LIBNAME_MAPXML):$(LIBDIR)/$(LIBNAME_GENERALXML)

# If TROVE4J_HOME is defined, search in ${TROVE4J_HOME}/lib
ifeq ($(TROVE4J_HOME),)
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar
else
EXTCLASSPATH := $(TROVE4J_HOME)/lib/trove.jar
endif

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/map/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/map \
		-C $(CLASSDIR) com/syrus/AMFICOM/general


.mach: .corba .xml $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/map/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/map/corba/.cvsignore)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) $(LIBNAME_MAPXML) .idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(XMLCLASSPATH):$(XMLBEANSCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/map/corba/*.java
	touch $@


#### XML ####
.PHONY: xml
xml: .xml

.xml: $(LIBNAME_MAPXML)
	touch $@

.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_MAPXML) $(LIBDIR)

$(LIBNAME_MAPXML): 
	$(SCOMP) $(SCOMPFLAGS) -out $@ -cp $(XMLBEANSCLASSPATH):$(LIBDIR)/$(LIBNAME_GENERALXML) \
		$(XMLDIR)/Map.xsd \
		$(XMLDIR)/Collector.xsd \
		$(XMLDIR)/NodeLink.xsd \
		$(XMLDIR)/PhysicalLink.xsd \
		$(XMLDIR)/PhysicalLinkType.xsd \
		$(XMLDIR)/SiteNode.xsd \
		$(XMLDIR)/SiteNodeType.xsd \
		$(XMLDIR)/TopologicalNode.xsd \
		$(XMLDIR)/MapLibrary.xsd \
		$(XMLDIR)/map.xsdconfig

.PHONY: clean
clean: cleanmach cleancorba cleanxml
	$(RM) $(LIBNAME)

.PHONY: distclean
distclean:
	$(RM) -r $(wildcard $(CLASSDIR)/*) $(wildcard *.jar) .xml .idl .mach .corba

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/map/corba
	$(RM) .corba

.PHONY: cleanxml xmlclean
cleanxml xmlclean:
	$(RM) $(LIBNAME_MAPXML)
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/map/xml
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/map/xml
	$(RM) .xml

#### Install ####
.PHONY: install
install: $(LIBNAME) $(LIBDIR)
	install -m 0644 $^

#### Archive ####
.PHONY: arch
arch:
	tar -cjvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/map.idl \
		$(SOURCES)

.PHONY: pmd
pmd:
	$(JAVA) -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/map text $(PMD_RULES) 
