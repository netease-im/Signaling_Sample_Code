#-------------------------------------------------
#
# Project created by QtCreator 2019-01-04T16:32:29
#
#-------------------------------------------------

QT          -= core gui
TEMPLATE    = lib
CONFIG      += staticlib

# The following define makes your compiler emit warnings if you use
# any feature of Qt which as been marked as deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS \
    NIM_SDK_DLL_IMPORT
# You can also make your code fail to compile if you use deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += nim_sdk_cpp_lib.cpp \
    ../../third_party/convert_utf/convert_utf.c \
    nim_sdk_cpp/api/nim_cpp_client.cpp \
    nim_sdk_cpp/api/nim_cpp_data_sync.cpp \
    nim_sdk_cpp/api/nim_cpp_doc_trans.cpp \
    nim_sdk_cpp/api/nim_cpp_friend.cpp \
    nim_sdk_cpp/api/nim_cpp_global.cpp \
    nim_sdk_cpp/api/nim_cpp_msglog.cpp \
    nim_sdk_cpp/api/nim_cpp_nos.cpp \
    nim_sdk_cpp/api/nim_cpp_plugin_in.cpp \
    nim_sdk_cpp/api/nim_cpp_robot.cpp \
    nim_sdk_cpp/api/nim_cpp_rts.cpp \
    nim_sdk_cpp/api/nim_cpp_session.cpp \
    nim_sdk_cpp/api/nim_cpp_signaling.cpp \
    nim_sdk_cpp/api/nim_cpp_subscribe_event.cpp \
    nim_sdk_cpp/api/nim_cpp_sysmsg.cpp \
    nim_sdk_cpp/api/nim_cpp_talk.cpp \
    nim_sdk_cpp/api/nim_cpp_team.cpp \
    nim_sdk_cpp/api/nim_cpp_tool.cpp \
    nim_sdk_cpp/api/nim_cpp_user.cpp \
    nim_sdk_cpp/api/nim_cpp_vchat.cpp \
    nim_sdk_cpp/helper/nim_client_helper.cpp \
    nim_sdk_cpp/helper/nim_doc_trans_helper.cpp \
    nim_sdk_cpp/helper/nim_friend_helper.cpp \
    nim_sdk_cpp/helper/nim_msg_helper.cpp \
    nim_sdk_cpp/helper/nim_msglog_helper.cpp \
    nim_sdk_cpp/helper/nim_nos_helper.cpp \
    nim_sdk_cpp/helper/nim_robot_helper.cpp \
    nim_sdk_cpp/helper/nim_session_helper.cpp \
    nim_sdk_cpp/helper/nim_signaling_helper.cpp \
    nim_sdk_cpp/helper/nim_subscribe_event_helper.cpp \
    nim_sdk_cpp/helper/nim_sysmsg_helper.cpp \
    nim_sdk_cpp/helper/nim_talk_helper.cpp \
    nim_sdk_cpp/helper/nim_team_helper.cpp \
    nim_sdk_cpp/helper/nim_tool_helper.cpp \
    nim_sdk_cpp/helper/nim_user_helper.cpp \
    nim_sdk_cpp/util/nim_json_util.cpp \
    nim_sdk_cpp/util/nim_sdk_util.cpp \
    nim_sdk_cpp/util/nim_string_util.cpp

HEADERS += nim_sdk_cpp_lib.h \
    ../../third_party/convert_utf/convert_utf.h \
    nim_sdk_cpp/api/nim_cpp_client.h \
    nim_sdk_cpp/api/nim_cpp_data_sync.h \
    nim_sdk_cpp/api/nim_cpp_doc_trans.h \
    nim_sdk_cpp/api/nim_cpp_friend.h \
    nim_sdk_cpp/api/nim_cpp_global.h \
    nim_sdk_cpp/api/nim_cpp_msglog.h \
    nim_sdk_cpp/api/nim_cpp_nos.h \
    nim_sdk_cpp/api/nim_cpp_plugin_in.h \
    nim_sdk_cpp/api/nim_cpp_robot.h \
    nim_sdk_cpp/api/nim_cpp_rts.h \
    nim_sdk_cpp/api/nim_cpp_session.h \
    nim_sdk_cpp/api/nim_cpp_signaling.h \
    nim_sdk_cpp/api/nim_cpp_subscribe_event.h \
    nim_sdk_cpp/api/nim_cpp_sysmsg.h \
    nim_sdk_cpp/api/nim_cpp_talk.h \
    nim_sdk_cpp/api/nim_cpp_team.h \
    nim_sdk_cpp/api/nim_cpp_tool.h \
    nim_sdk_cpp/api/nim_cpp_user.h \
    nim_sdk_cpp/api/nim_cpp_vchat.h \
    nim_sdk_cpp/callback_proxy.h \
    nim_sdk_cpp/helper/nim_client_helper.h \
    nim_sdk_cpp/helper/nim_doc_trans_helper.h \
    nim_sdk_cpp/helper/nim_friend_helper.h \
    nim_sdk_cpp/helper/nim_msg_helper.h \
    nim_sdk_cpp/helper/nim_msglog_helper.h \
    nim_sdk_cpp/helper/nim_nos_helper.h \
    nim_sdk_cpp/helper/nim_robot_helper.h \
    nim_sdk_cpp/helper/nim_session_helper.h \
    nim_sdk_cpp/helper/nim_signaling_helper.h \
    nim_sdk_cpp/helper/nim_subscribe_event_helper.h \
    nim_sdk_cpp/helper/nim_sysmsg_helper.h \
    nim_sdk_cpp/helper/nim_talk_helper.h \
    nim_sdk_cpp/helper/nim_team_helper.h \
    nim_sdk_cpp/helper/nim_tool_helper.h \
    nim_sdk_cpp/helper/nim_user_helper.h \
    nim_sdk_cpp/util/nim_json_util.h \
    nim_sdk_cpp/util/nim_sdk_defines.h \
    nim_sdk_cpp/util/nim_sdk_util.h \
    nim_sdk_cpp/util/nim_string_util.h

INCLUDEPATH += $$PWD/.\
    $$PWD/../../ \
    $$PWD/../../third_party/jsoncpp/include/json \
    $$PWD/nim_c_sdk\include \
    $$PWD/nim_c_sdk\util \
    $$PWD/nim_sdk_cpp \
    $$PWD/nim_sdk_cpp/api \
    $$PWD/nim_sdk_cpp/helper \
    $$PWD/nim_sdk_cpp/util

CONFIG(debug, debug|release){
    DESTDIR =$$PWD/../
    TARGET = nim_sdk_cpp_lib_d
}else{
    DESTDIR =$$PWD/../
    TARGET = nim_sdk_cpp_lib
}
