cmake_minimum_required(VERSION 3.4.1)

enable_language(C ASM)

include_directories(
        ${HomePath}/nativeLib/src/main/cpp/includes/
)

add_library(
        jvmtiHelper

        STATIC

        JvmTiWrapper.cpp

)

target_link_libraries(
        jvmtiHelper

        adapter
        dlfc
        utils
        xdl
        ${log-lib}
)











