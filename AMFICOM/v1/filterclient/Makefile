MODULE_NAME = filterclient_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
LIBNAME_INTERFACE= $(subst _v1,_interface,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

APPCLASSPATH = $(LIBDIR)/measurement.jar:$(LIBDIR)/util.jar:$(LIBDIR)/general.jar:$(LIBDIR)/resource.jar:$(LIBDIR)/configuration.jar:$(LIBDIR)/measurement.jar:$(LIBDIR)/administration.jar

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

all: $(LIBNAME)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) $(wildcard $(SRCDIR)/com/syrus/AMFICOM/filterclient/*.java)
	touch $@

.PHONY:	clean
clean:	cleanmach
	$(RM)

.PHONY:	cleanmach
cleanmach:
	$(RM) $(wildcard $(CLASSDIR)/com/syrus/AMFICOM/filterclient/*.class)
	$(RM) .mach

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
#.PHONY:	arch
#arch:
#	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SRCDIR)/com/syrus/AMFICOM/filterclient/*.java
