cmake_minimum_required(VERSION 3.12.2)

project(nim_sdk_cpp_lib)

set(CMAKE_CONFIGURATION_TYPES "Debug;Release"
    CACHE STRING "Configuration types" FORCE)

add_library(nim_sdk_cpp_lib STATIC
  ../third_party/convert_utf/convert_utf.c
  ../third_party/convert_utf/convert_utf.h
  api/nim_cpp_client.cpp
  api/nim_cpp_client.h
  api/nim_cpp_data_sync.cpp
  api/nim_cpp_data_sync.h
  api/nim_cpp_doc_trans.cpp
  api/nim_cpp_doc_trans.h
  api/nim_cpp_friend.cpp
  api/nim_cpp_friend.h
  api/nim_cpp_global.cpp
  api/nim_cpp_global.h
  api/nim_cpp_msglog.cpp
  api/nim_cpp_msglog.h
  api/nim_cpp_nos.cpp
  api/nim_cpp_nos.h
  api/nim_cpp_plugin_in.cpp
  api/nim_cpp_plugin_in.h
  api/nim_cpp_robot.cpp
  api/nim_cpp_robot.h
  api/nim_cpp_rts.cpp
  api/nim_cpp_rts.h
  api/nim_cpp_session.cpp
  api/nim_cpp_session.h
  api/nim_cpp_signaling.cpp
  api/nim_cpp_signaling.h
  api/nim_cpp_subscribe_event.cpp
  api/nim_cpp_subscribe_event.h
  api/nim_cpp_sysmsg.cpp
  api/nim_cpp_sysmsg.h
  api/nim_cpp_talk.cpp
  api/nim_cpp_talk.h
  api/nim_cpp_team.cpp
  api/nim_cpp_team.h
  api/nim_cpp_tool.cpp
  api/nim_cpp_tool.h
  api/nim_cpp_user.cpp
  api/nim_cpp_user.h
  api/nim_cpp_vchat.cpp
  api/nim_cpp_vchat.h
  callback_proxy.h
  helper/nim_client_helper.cpp
  helper/nim_client_helper.h
  helper/nim_doc_trans_helper.cpp
  helper/nim_doc_trans_helper.h
  helper/nim_friend_helper.cpp
  helper/nim_friend_helper.h
  helper/nim_msg_helper.cpp
  helper/nim_msg_helper.h
  helper/nim_msglog_helper.cpp
  helper/nim_msglog_helper.h
  helper/nim_nos_helper.cpp
  helper/nim_nos_helper.h
  helper/nim_robot_helper.cpp
  helper/nim_robot_helper.h
  helper/nim_session_helper.cpp
  helper/nim_session_helper.h
  helper/nim_signaling_helper.cpp
  helper/nim_signaling_helper.h
  helper/nim_subscribe_event_helper.cpp
  helper/nim_subscribe_event_helper.h
  helper/nim_sysmsg_helper.cpp
  helper/nim_sysmsg_helper.h
  helper/nim_talk_helper.cpp
  helper/nim_talk_helper.h
  helper/nim_team_helper.cpp
  helper/nim_team_helper.h
  helper/nim_tool_helper.cpp
  helper/nim_tool_helper.h
  helper/nim_user_helper.cpp
  helper/nim_user_helper.h
  nim_cpp_api.cpp
  nim_cpp_api.h
  util/nim_json_util.cpp
  util/nim_json_util.h
  util/nim_sdk_defines.h
  util/nim_sdk_util.cpp
  util/nim_sdk_util.h
  util/nim_string_util.cpp
  util/nim_string_util.h
)
# Output file name
set_target_properties(nim_sdk_cpp_lib
  PROPERTIES
  OUTPUT_NAME_DEBUG nim_cpp_sdk_d
  OUTPUT_NAME_RELEASE nim_cpp_sdk
)

# Additional include directories
set_property(TARGET nim_sdk_cpp_lib
  APPEND PROPERTY INCLUDE_DIRECTORIES
  $<$<CONFIG:Debug>:
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\;
    ${CMAKE_CURRENT_SOURCE_DIR};
    ${CMAKE_CURRENT_SOURCE_DIR}/util;
    ${CMAKE_CURRENT_SOURCE_DIR}/api;
    ${CMAKE_CURRENT_SOURCE_DIR}/helper;
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\third_party\jsoncpp\include\json\;
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\nim_c_sdk\include\;
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\nim_c_sdk\util\>
  $<$<CONFIG:Release>:
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\;
    ${CMAKE_CURRENT_SOURCE_DIR};
    ${CMAKE_CURRENT_SOURCE_DIR}/util;
    ${CMAKE_CURRENT_SOURCE_DIR}/api;
    ${CMAKE_CURRENT_SOURCE_DIR}/helper;
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\third_party\jsoncpp\include\json\;
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\nim_c_sdk\include\;
    E:\nim_git\NIM_PC_SDK-CPP\nim_sdk_cpp\..\nim_c_sdk\util\>
)

# Preprocessor definitions
target_compile_definitions(nim_sdk_cpp_lib PRIVATE
  $<$<CONFIG:Debug>:_UNICODE;WIN32;NIM_WIN_DESKTOP_ONLY_SDK;_DEBUG;_LIB;NIM_SDK;NIM_SDK_DLL_IMPORT>
  $<$<CONFIG:Release>:_UNICODE;WIN32;NIM_WIN_DESKTOP_ONLY_SDK;NDEBUG;_LIB;NIM_SDK;NIM_SDK_DLL_IMPORT>
)

# Minimal rebuild
if (MSVC)
  target_compile_options(nim_sdk_cpp_lib PRIVATE
    "$<$<CONFIG:Debug>:/Gm>"
    "$<$<CONFIG:Release>:/Gm->"
  )
endif ()

# Precompiled header files
if (MSVC)
  target_compile_options(nim_sdk_cpp_lib PRIVATE
    ""
  )
endif ()
