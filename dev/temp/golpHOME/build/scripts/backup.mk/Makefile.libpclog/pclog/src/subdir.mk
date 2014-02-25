################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
$(SRCLOG)/pclog/src/ap_snprintf.c \
$(SRCLOG)/pclog/src/pc_env.c \
$(SRCLOG)/pclog/src/pc_logcfg.c \
$(SRCLOG)/pclog/src/pc_logdef.c \
$(SRCLOG)/pclog/src/pc_logger.c \
$(SRCLOG)/pclog/src/pc_logsys.c \
$(SRCLOG)/pclog/src/pclogver.c \
$(SRCLOG)/pclog/src/strutil.c \
$(SRCLOG)/pclog/src/strutil_r.c \
$(SRCLOG)/pclog/src/threads.c \
$(SRCLOG)/pclog/src/buildver.c

OBJS += \
./pclog/src/ap_snprintf.o \
./pclog/src/pc_env.o \
./pclog/src/pc_logcfg.o \
./pclog/src/pc_logdef.o \
./pclog/src/pc_logger.o \
./pclog/src/pc_logsys.o \
./pclog/src/pclogver.o \
./pclog/src/strutil.o \
./pclog/src/strutil_r.o \
./pclog/src/threads.o \
./pclog/src/buildver.o

C_DEPS += \
./pclog/src/ap_snprintf.d \
./pclog/src/pc_env.d \
./pclog/src/pc_logcfg.d \
./pclog/src/pc_logdef.d \
./pclog/src/pc_logger.d \
./pclog/src/pc_logsys.d \
./pclog/src/pclogver.d \
./pclog/src/strutil.d \
./pclog/src/strutil_r.d \
./pclog/src/threads.d \
./pclog/src/buildver.d


# Each subdirectory must supply rules for building sources it contributes
pclog/src/%.o: $(SRCLOG)/pclog/src/%.c 
	gcc $(DEFLOG) $(HLOG) $(CCLOG) -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	-@$(ECHO) ' '

#	-gcc $(DEFLOG) $(HLOG) $(CCLOG) -MMD -MP -MF"$(@:%.o=%.d)" -MT $(@:%.o=%.P) -o "$@" "$<"
#	@cp $(@:%.o=%.d) $(@:%.o=%.P); \
#	  sed -e 's/#.*//' -e 's/^[^:]*: *//' -e 's/ *\\$$//' \
#	      -e '/^$$/ d' -e 's/$$/ :/' < $(@:%.o=%.d) >> $(@:%.o=%.P); \
#	      rm -f $(@:%.o=%.d)

#	gcc $(DEFLOG) $(HLOG) $(CCLOG) -o "$@" "$<"
#	gcc $(DEFLOG) $(HLOG) $(CCLOG) -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
#	gcc $(DEFLOG) $(HLOG) $(CCLOG) -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
