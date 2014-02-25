################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
$(SRCLOG)/pclog/src/syslogger/pc_log2syslog.c 

OBJS += \
./pclog/src/syslogger/pc_log2syslog.o 

C_DEPS += \
./pclog/src/syslogger/pc_log2syslog.d 


# Each subdirectory must supply rules for building sources it contributes
pclog/src/syslogger/%.o: $(SRCLOG)/pclog/src/syslogger/%.c
	gcc $(DEFLOG) $(HLOG) $(CCLOG) -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	-@$(ECHO) ' '

