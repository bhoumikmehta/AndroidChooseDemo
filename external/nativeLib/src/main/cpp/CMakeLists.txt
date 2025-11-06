cmake_minimum_required(VERSION 3.4.1)
project(nativeLib)
ENABLE_LANGUAGE(ASM)
message(STATUS "nativeLib start building ")

set(NATIVE_LIB_ROOT_PATH ${CMAKE_CURRENT_SOURCE_DIR})

add_library(nativeLib INTERFACE)

target_include_directories(
        nativeLib
        INTERFACE
        ${CMAKE_CURRENT_SOURCE_DIR}/includes/
)
include_directories(
        ${CMAKE_CURRENT_SOURCE_DIR}/
        ${CMAKE_CURRENT_SOURCE_DIR}/includes/
)

add_subdirectory(dlfc)
add_subdirectory(adapter)
add_subdirectory(utils)



target_link_libraries(
        nativeLib
        INTERFACE
        dlfc adapter utils  antijit ${log-lib}
)
