TEMPLATE    = subdirs

SUBDIRS     += \
        libs/nim_sdk_cpp_lib \
        signaling \
        signaling_agora_p2p \
        signaling_agora_meeting \

signaling.depends = libs/nim_sdk_cpp_lib
signaling_agora_p2p.depends = libs/nim_sdk_cpp_lib
signaling_agora_meeting.depends = libs/nim_sdk_cpp_lib
