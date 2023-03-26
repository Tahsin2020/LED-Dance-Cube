# TCL File Generated by Component Editor 18.1
# Sun Mar 19 19:02:42 PDT 2023
# DO NOT MODIFY


# 
# UART_custom_component "UART_custom_component" v1.0
#  2023.03.19.19:02:42
# 
# 

# 
# request TCL package from ACDS 16.1
# 
package require -exact qsys 16.1


# 
# module UART_custom_component
# 
set_module_property DESCRIPTION ""
set_module_property NAME UART_custom_component
set_module_property VERSION 1.0
set_module_property INTERNAL false
set_module_property OPAQUE_ADDRESS_MAP true
set_module_property AUTHOR ""
set_module_property DISPLAY_NAME UART_custom_component
set_module_property INSTANTIATE_IN_SYSTEM_MODULE true
set_module_property EDITABLE true
set_module_property REPORT_TO_TALKBACK false
set_module_property ALLOW_GREYBOX_GENERATION false
set_module_property REPORT_HIERARCHY false


# 
# file sets
# 
add_fileset QUARTUS_SYNTH QUARTUS_SYNTH "" ""
set_fileset_property QUARTUS_SYNTH TOP_LEVEL new_component
set_fileset_property QUARTUS_SYNTH ENABLE_RELATIVE_INCLUDE_PATHS false
set_fileset_property QUARTUS_SYNTH ENABLE_FILE_OVERWRITE_MODE false
add_fileset_file new_component.v VERILOG PATH new_component.v TOP_LEVEL_FILE


# 
# parameters
# 


# 
# display items
# 


# 
# connection point avm_m0
# 
add_interface avm_m0 avalon start
set_interface_property avm_m0 addressUnits SYMBOLS
set_interface_property avm_m0 associatedClock clock
set_interface_property avm_m0 associatedReset reset
set_interface_property avm_m0 bitsPerSymbol 8
set_interface_property avm_m0 burstOnBurstBoundariesOnly false
set_interface_property avm_m0 burstcountUnits WORDS
set_interface_property avm_m0 doStreamReads false
set_interface_property avm_m0 doStreamWrites false
set_interface_property avm_m0 holdTime 0
set_interface_property avm_m0 linewrapBursts false
set_interface_property avm_m0 maximumPendingReadTransactions 0
set_interface_property avm_m0 maximumPendingWriteTransactions 0
set_interface_property avm_m0 readLatency 0
set_interface_property avm_m0 readWaitTime 1
set_interface_property avm_m0 setupTime 0
set_interface_property avm_m0 timingUnits Cycles
set_interface_property avm_m0 writeWaitTime 0
set_interface_property avm_m0 ENABLED true
set_interface_property avm_m0 EXPORT_OF ""
set_interface_property avm_m0 PORT_NAME_MAP ""
set_interface_property avm_m0 CMSIS_SVD_VARIABLES ""
set_interface_property avm_m0 SVD_ADDRESS_GROUP ""

add_interface_port avm_m0 avm_m0_address address Output 8
add_interface_port avm_m0 avm_m0_read read Output 1
add_interface_port avm_m0 avm_m0_readdata readdata Input 16
add_interface_port avm_m0 avm_m0_write write Output 1
add_interface_port avm_m0 avm_m0_writedata writedata Output 16
add_interface_port avm_m0 avm_m0_waitrequest waitrequest Input 1


# 
# connection point clock
# 
add_interface clock clock end
set_interface_property clock clockRate 0
set_interface_property clock ENABLED true
set_interface_property clock EXPORT_OF ""
set_interface_property clock PORT_NAME_MAP ""
set_interface_property clock CMSIS_SVD_VARIABLES ""
set_interface_property clock SVD_ADDRESS_GROUP ""

add_interface_port clock clock_clk clk Input 1


# 
# connection point reset
# 
add_interface reset reset end
set_interface_property reset associatedClock clock
set_interface_property reset synchronousEdges DEASSERT
set_interface_property reset ENABLED true
set_interface_property reset EXPORT_OF ""
set_interface_property reset PORT_NAME_MAP ""
set_interface_property reset CMSIS_SVD_VARIABLES ""
set_interface_property reset SVD_ADDRESS_GROUP ""

add_interface_port reset reset_reset reset Input 1

