MODULE_NAME = map_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)


XMLDIR=./xml
XMLSRCDIR=$(XMLDIR)/src
XMLCLASSDIR=$(XMLDIR)/classes
XMLBEANSCLASSPATH=$(EXTLIBDIR)/xbean.jar:$(EXTLIBDIR)/jsr173_api.jar
LIBNAME_GENERALXML=generalxml.jar
LIBNAME_MAPXML=mapxml.jar

XMLBEANS_HOME=/mnt/d/xmlbeans-2.0.0-beta1
SCOMP=$(XMLBEANS_HOME)/bin/scomp
SCOMPFLAGS = -src $(XMLSRCDIR) -d $(XMLCLASSDIR)


APPCLASSPATH = $(LIBDIR)/configuration.jar:$(LIBDIR)/general.jar:$(LIBDIR)/administration.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/util.jar
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar


SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/map/*.java)


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com/syrus/AMFICOM/map
		

.mach:	.corba $(LIBNAME_MAPXML) $(LIBNAME_GENERALXML) $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH):$(LIBNAME_MAPXML):$(LIBNAME_GENERALXML):$(XMLBEANSCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/map.idl
	$(JAVAC) $(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/map/corba/*.java 
	touch $@


#### XML ####
.PHONY:	xmlinstall
xmlinstall:	$(LIBNAME_MAPXML) $(LIBNAME_GENERALXML)
	$(CP) $^ $(LIBDIR)

$(LIBNAME_MAPXML):	$(LIBNAME_GENERALXML)
	$(SCOMP) $(SCOMPFLAGS) -out $@ -cp $(XMLBEANSCLASSPATH):$(LIBNAME_GENERALXML) \
		$(XMLDIR)/Map.xsd \
		$(XMLDIR)/Characteristic.xsd \
		$(XMLDIR)/Collector.xsd \
		$(XMLDIR)/NodeLink.xsd \
		$(XMLDIR)/PhysicalLink.xsd \
		$(XMLDIR)/PhysicalLinkType.xsd \
		$(XMLDIR)/SiteNode.xsd \
		$(XMLDIR)/SiteNodeType.xsd \
		$(XMLDIR)/TopologicalNode.xsd

$(LIBNAME_GENERALXML):
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(XMLDIR)/General.xsd



$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/map/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/map/corba
	$(RM) -f .corba

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^


#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/map.idl \
		$(SOURCES)
		
.PHONY: pmd
pmd:
	${JAVA} -classpath $(PMD_JARS) -jar $(PMD_JAR) src/com/syrus/AMFICOM/map text $(PMD_RULES) 

#### Compile and install jar to libdir
lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)	
