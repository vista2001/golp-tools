################################################################################
# Makefile for libpclog
################################################################################

include ../scripts/makefile.def

#source dir for pclog
SRCLOG=$(SRCTOP)/libpclog

DEFLOG=-DPCLOG_THREAD_ON -DLOG_LOGLVL_NAME $(BDDEF)
HLOG=-I$(SRCLOG)/pclog -I$(SRCLOG)/pclog/src -I${SRCLOG}/gdsl-1.4-pl1 -I${SRCLOG}/gdsl-1.4-pl1/src 
CCLOG=-O1 -Wall -c -fmessage-length=0

# All of the sources participating in the build are defined here

O_SRCS := 
C_SRCS := 
S_UPPER_SRCS := 
OBJ_SRCS := 
ASM_SRCS := 
OBJS := 
C_DEPS := 
ARCHIVES := 

# Every subdirectory with source files must be described here
SUBDIRS := \
pclog/src \
pclog/src/syslogger \
pclog/src/filelogger \
gdsl-1.4-pl1/src \

-include pclog/src/subdir.mk
-include pclog/src/syslogger/subdir.mk
-include pclog/src/filelogger/subdir.mk
-include gdsl-1.4-pl1/src/subdir.mk


ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif


# All Target
all: $(TO_LIB)/$(LIBLOG_AR) $(TO_LIB)/$(LIBLOG_SO)

# Tool invocations
$(TO_LIB)/$(LIBLOG_AR): $(OBJS) $(USER_OBJS)
	@$(ECHO) 'Building target: $@'
	@$(AR) $(AROPT) $(TO_LIB)/$(LIBLOG_AR) $(OBJS) $(USER_OBJS) $(LIBS)
	@$(ECHO) 'Finished building target: $@'
	@$(ECHO) ' '

$(TO_LIB)/$(LIBLOG_SO): $(OBJS) $(USER_OBJS)
	@$(ECHO) 'Building target: $@'
	@gcc -o $(TO_LIB)/$(LIBLOG_SO) $(SOFLAG) $(OBJS)
	@$(ECHO) 'Finished building target: $@'
	@$(ECHO) ' '

# Other Targets
clean:
	-$(RM) $(OBJS)$(C_DEPS) $(MDEPS) $(ARCHIVES) $(TO_LIB)/$(LIBLOG_AR) $(TO_LIB)/$(LIBLOG_SO)
	-@$(ECHO) ' '

mostlyclean:
	-$(RM) $(OBJS)$(C_DEPS) $(MDEPS)
	-@$(ECHO) ' '

install: all
	@$(CP) $(TO_LIB)/$(LIBLOG_SO) $(INSTLIBDIR)/
	@$(CP) $(TO_LIB)/$(LIBLOG_AR) $(INSTLIBDIR)/
	@$(ECHO) 'The libpclog has installed into $(INSTLIBDIR) from $(TO_LIB).'
	@$(ECHO) ' '
