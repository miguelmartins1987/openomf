cmake_minimum_required(VERSION 3.6)
project(SDL2_Android_Port C CXX)

# Set common source files using file(GLOB ...) to mimic wildcard functionality
file(GLOB SDL_SRC_C
        "src/*.c"
        "src/atomic/*.c"
        "src/audio/*.c"
        "src/audio/android/*.c"
        "src/audio/dummy/*.c"
        "src/audio/aaudio/*.c"
        "src/audio/openslES/*.c"
        "src/core/android/*.c"
        "src/cpuinfo/*.c"
        "src/dynapi/*.c"
        "src/events/*.c"
        "src/file/*.c"
        "src/haptic/*.c"
        "src/haptic/android/*.c"
        "src/hidapi/*.c"
        "src/joystick/*.c"
        "src/joystick/android/*.c"
        "src/joystick/dummy/*.c"
        "src/joystick/hidapi/*.c"
        "src/joystick/virtual/*.c"
        "src/loadso/dlopen/*.c"
        "src/locale/*.c"
        "src/locale/android/*.c"
        "src/misc/*.c"
        "src/misc/android/*.c"
        "src/power/*.c"
        "src/power/android/*.c"
        "src/filesystem/android/*.c"
        "src/sensor/*.c"
        "src/sensor/android/*.c"
        "src/render/*.c"
        "src/render/*/*.c"
        "src/stdlib/*.c"
        "src/thread/*.c"
        "src/thread/pthread/*.c"
        "src/timer/*.c"
        "src/timer/unix/*.c"
        "src/video/*.c"
        "src/video/android/*.c"
        "src/video/yuv2rgb/*.c"
        "src/test/*.c"
)

file(GLOB SDL_SRC_CPP
        "src/hidapi/android/*.cpp"
)

set(SDL_ALL_SOURCES
        ${SDL_SRC_C}
        ${SDL_SRC_CPP}
)

# Common include directories
set(SDL_INCLUDE_DIRS
        ${CMAKE_CURRENT_SOURCE_DIR}/include
)

# Common compiler flags
set(SDL_C_FLAGS
        -DGL_GLEXT_PROTOTYPES
        -Wall -Wextra
        -Wdocumentation
        -Wmissing-prototypes
        -Wunreachable-code-break
        -Wunneeded-internal-declaration
        -Wmissing-variable-declarations
        -Wfloat-conversion
        -Wshorten-64-to-32
        -Wunreachable-code-return
        -Wshift-sign-overflow
        -Wstrict-prototypes
        -Wkeyword-macro
        -Wno-unused-parameter
        -Wno-sign-compare
)

###########################
#
# Helper: NDK cpufeatures
#
###########################
if(ANDROID)
    # Define the library target and point directly to the NDK source file
    add_library(cpufeatures STATIC ${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c)

    # Ensure the compiler can resolve <cpu-features.h>
    target_include_directories(cpufeatures PUBLIC ${ANDROID_NDK}/sources/android/cpufeatures)
endif()

###########################
#
# SDL shared library (SDL2)
#
###########################

add_library(SDL2 SHARED ${SDL_ALL_SOURCES})

target_include_directories(SDL2 PUBLIC
        "include"
)

target_compile_options(SDL2 PRIVATE ${SDL_C_FLAGS})

# C++11 standard requirement for C++ source files
set_property(TARGET SDL2 PROPERTY CXX_STANDARD 11)
set_property(TARGET SDL2 PROPERTY CXX_STANDARD_REQUIRED ON)

# Link libraries and flags for shared build
target_link_libraries(SDL2
        PRIVATE
        dl
        GLESv1_CM
        GLESv2
        OpenSLES
        log
        android
        cpufeatures
)

set_target_properties(SDL2 PROPERTIES
        LINK_FLAGS "-Wl,--no-undefined"
)

###########################
#
# SDL static library (SDL2_static)
#
###########################

add_library(SDL2_static STATIC ${SDL_ALL_SOURCES})

target_include_directories(SDL2_static PUBLIC
        "include"
)

target_compile_options(SDL2_static PRIVATE ${SDL_C_FLAGS})

set_property(TARGET SDL2_static PROPERTY CXX_STANDARD 11)
set_property(TARGET SDL2_static PROPERTY CXX_STANDARD_REQUIRED ON)

target_link_libraries(SDL2_static
        PUBLIC
        dl
        GLESv1_CM
        GLESv2
        log
        android
        cpufeatures
)

set_target_properties(SDL2_static PROPERTIES
        OUTPUT_NAME "SDL2"
)

###########################
#
# SDL main static library (SDL2_main)
#
###########################

file(GLOB SDL_MAIN_SOURCES
        "src/main/android/*.c"
)

add_library(SDL2_main STATIC ${SDL_MAIN_SOURCES})

target_include_directories(SDL2_main PUBLIC
        "include"
)

set_target_properties(SDL2_main PROPERTIES
        OUTPUT_NAME "SDL2main"
)
