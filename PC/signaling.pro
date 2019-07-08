TEMPLATE    = subdirs

SUBDIRS     += \
        libs/nim_sdk_cpp_lib \
        signaling

signaling.depends = libs/nim_sdk_cpp_lib
