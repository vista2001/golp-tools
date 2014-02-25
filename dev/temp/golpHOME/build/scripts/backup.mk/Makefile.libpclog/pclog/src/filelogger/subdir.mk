################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
$(SRCLOG)/pclog/src/filelogger/filelock.c \
$(SRCLOG)/pclog/src/filelogger/pc_log2file.c 

OBJS += \
./pclog/src/filelogger/filelock.o \
./pclog/src/filelogger/pc_log2file.o 

C_DEPS += \
./pclog/src/filelogger/filelock.d \
./pclog/src/filelogger/pc_log2file.d 


# Each subdirectory must supply rules for building sources it contributes
pclog/src/filelogger/%.o: $(SRCLOG)/pclog/src/filelogger/%.c
	gcc $(DEFLOG) $(HLOG) $(CCLOG) -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	-@$(ECHO) ' '

