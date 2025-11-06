cmake_minimum_required(VERSION 3.4.1)
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wno-narrowing -std=c++20")

#添加内联支持
enable_language(C ASM)

include_directories(
        ${HomePath}/external/nativeLib/src/main/cpp/includes/
        ${HomePath}/external/lsplant/src/main/cpp/include/
)
#external库
add_subdirectory(${HomePath}/external external.out)

add_subdirectory(jvmti)

find_library(log-lib log)

add_library(
        testQC

        SHARED

        main.cpp
)

target_link_libraries(
        testQC
        jvmtiHelper
        ${log-lib}
)



target_link_libraries(
        testQC

        adapter
        dlfc

        utils
        xdl
)











