COMMON_DIR = ./common
ANALYSE_DIR = ./analyse
AN2_DIR = ./an2
FIT_DIR = ./fit
JNI_DIR = ./JNI
BREAKL_DIR = ./BreakL
THRESH_DIR = ./thresh
CLASS_DIR = ../classes

OUT = $(SO_PREFIX)dadara.$(SO_EXT)

#@todo: improve system detecton

# unixes
ifeq (${ComSpec}${COMSPEC},)

OBJ_EXT = o
SO_EXT = so
SO_PREFIX = lib

LD = g++
LDFLAGS = -shared 
LD_OUTPUT_FLAG = "-o"
LDLIBS = -lm -lstdc++

CXX = g++
CFLAGS = -O2 -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
CXXFLAGS = -O2 -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
CXX_NOLINK_FLAG = -c 
CXX_OUTPUT_FLAG = -o 
CPPFLAGS = -DDADARA_EXPORTS

JAVAH = javah

else
# MSDOSes

OBJ_EXT = obj
SO_EXT = dll
SO_PREFIX = 

LD = link
LDFLAGS = /nologo /dll
LD_OUTPUT_FLAG = /out:

CXX = cl.exe
CXXFLAGS = /nologo /O2 /GX /MT /FD
CXX_NOLINK_FLAG = /c
CXX_OUTPUT_FLAG = /Fo
CPPFLAGS = /D "WIN32" /D "_WINDOWS" /D "DADARA_EXPORTS" /D "_USRDLL" /D "_MBCS"

JAVAH = javah

endif


