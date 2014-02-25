################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
$(SRCLOG)/gdsl-1.4-pl1/src/_gdsl_bintree.c \
$(SRCLOG)/gdsl-1.4-pl1/src/_gdsl_bstree.c \
$(SRCLOG)/gdsl-1.4-pl1/src/_gdsl_list.c \
$(SRCLOG)/gdsl-1.4-pl1/src/_gdsl_node.c \
$(SRCLOG)/gdsl-1.4-pl1/src/_gdsl_tree.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_2darray.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_bstree.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_hash.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_heap.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_list.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_perm.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_queue.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_rbtree.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_sort.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_stack.c \
$(SRCLOG)/gdsl-1.4-pl1/src/gdsl_tree.c 

OBJS += \
./gdsl-1.4-pl1/src/_gdsl_bintree.o \
./gdsl-1.4-pl1/src/_gdsl_bstree.o \
./gdsl-1.4-pl1/src/_gdsl_list.o \
./gdsl-1.4-pl1/src/_gdsl_node.o \
./gdsl-1.4-pl1/src/_gdsl_tree.o \
./gdsl-1.4-pl1/src/gdsl.o \
./gdsl-1.4-pl1/src/gdsl_2darray.o \
./gdsl-1.4-pl1/src/gdsl_bstree.o \
./gdsl-1.4-pl1/src/gdsl_hash.o \
./gdsl-1.4-pl1/src/gdsl_heap.o \
./gdsl-1.4-pl1/src/gdsl_list.o \
./gdsl-1.4-pl1/src/gdsl_perm.o \
./gdsl-1.4-pl1/src/gdsl_queue.o \
./gdsl-1.4-pl1/src/gdsl_rbtree.o \
./gdsl-1.4-pl1/src/gdsl_sort.o \
./gdsl-1.4-pl1/src/gdsl_stack.o \
./gdsl-1.4-pl1/src/gdsl_tree.o 

C_DEPS += \
./gdsl-1.4-pl1/src/_gdsl_bintree.d \
./gdsl-1.4-pl1/src/_gdsl_bstree.d \
./gdsl-1.4-pl1/src/_gdsl_list.d \
./gdsl-1.4-pl1/src/_gdsl_node.d \
./gdsl-1.4-pl1/src/_gdsl_tree.d \
./gdsl-1.4-pl1/src/gdsl.d \
./gdsl-1.4-pl1/src/gdsl_2darray.d \
./gdsl-1.4-pl1/src/gdsl_bstree.d \
./gdsl-1.4-pl1/src/gdsl_hash.d \
./gdsl-1.4-pl1/src/gdsl_heap.d \
./gdsl-1.4-pl1/src/gdsl_list.d \
./gdsl-1.4-pl1/src/gdsl_perm.d \
./gdsl-1.4-pl1/src/gdsl_queue.d \
./gdsl-1.4-pl1/src/gdsl_rbtree.d \
./gdsl-1.4-pl1/src/gdsl_sort.d \
./gdsl-1.4-pl1/src/gdsl_stack.d \
./gdsl-1.4-pl1/src/gdsl_tree.d 


# Each subdirectory must supply rules for building sources it contributes
gdsl-1.4-pl1/src/%.o: $(SRCLOG)/gdsl-1.4-pl1/src/%.c
	gcc $(DEFLOG) $(HLOG) $(CCLOG) -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	-@$(ECHO) ' '

