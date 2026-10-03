cmake_minimum_required(VERSION 3.6)

project(OpenOMF)

# 1. Add SDL subdirectory first so its targets are created
add_subdirectory(SDL2)

# 2. Provide the target alias that SDL_mixer's find module expects
if(TARGET SDL2)
    if(NOT TARGET SDL2::SDL2)
        add_library(SDL2::SDL2 ALIAS SDL2)
    endif()
elseif(TARGET SDL2-static)
    if(NOT TARGET SDL2::SDL2)
        add_library(SDL2::SDL2 ALIAS SDL2-static)
    endif()
endif()

# 3. Add SDL_mixer (it will now successfully detect SDL2::SDL2)
add_subdirectory(SDL_mixer)

# 4. Add your game source
add_subdirectory(src)