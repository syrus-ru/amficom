MODULE_NAME = tools
LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

LIBDIR=../lib

HOST=mongol
SERVICE_NAME=mongol

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com rulesets

$(CLASSDIR):	
	mkdir -p $@

.PHONY:	clean
clean:	
	rm -f $(LIBNAME)

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(PMD_JARS) \
		$(shell find . -name *java)
	
