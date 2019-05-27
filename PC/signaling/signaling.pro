QT     += quick quickcontrols2
CONFIG += c++11

# The following define makes your compiler emit warnings if you use
# any Qt feature that has been marked deprecated (the exact warnings
# depend on your compiler). Refer to the documentation for the
# deprecated API to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

INCLUDEPATH += \
        ../third_party/jsoncpp/include/json \
        ../libs/nim_sdk_cpp_lib/nim_c_sdk \
        ../libs/nim_sdk_cpp_lib/nim_c_sdk/api \
        ../libs/nim_sdk_cpp_lib/nim_c_sdk/util \
        ../libs/nim_sdk_cpp_lib/nim_c_sdk/include \
        ../libs/nim_sdk_cpp_lib/nim_sdk_cpp \
        ../libs/nim_sdk_cpp_lib/nim_sdk_cpp/api \
        ../libs/nim_sdk_cpp_lib/nim_sdk_cpp/helper \
        ../libs/nim_sdk_cpp_lib/nim_sdk_cpp/util \
        ../libs \
        ../shared/nim \

HEADERS += \
        main.h \
        ../shared/nim/channel_manager.h \
        ../shared/nim/login_manager.h

SOURCES += \
        main.cpp \
        ../shared/nim/channel_manager.cpp \
        ../shared/nim/login_manager.cpp

RESOURCES += qml.qrc \
        ../shared/qml/shared.qrc

CONFIG(debug, debug|release) {
    LIBS += -L$$PWD/../libs/ -lnim_sdk_cpp_lib_d \
            -L$$PWD/../libs/ -ljsoncpp_d
} else {
    LIBS += -L$$PWD/../libs/ -lnim_sdk_cpp_lib \
            -L$$PWD/../libs/ -ljsoncpp
}

# Additional import path used to resolve QML modules in Qt Creator's code model
QML_IMPORT_PATH =

# Additional import path used to resolve QML modules just for Qt Quick Designer
QML_DESIGNER_IMPORT_PATH =

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target
