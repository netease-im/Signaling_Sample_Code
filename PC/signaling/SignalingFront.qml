import QtQuick 2.4

SignalingFrontForm {
    createRoom.onClicked: {
        channelManager.createChannel()
    }

    joinRoom.onClicked: {
        channelManager.joinChannel()
    }

    btnLogout.onClicked: {
        loginManager.doLogout()
        pageLoader.setSource(Qt.resolvedUrl("Login.qml"))
    }

    Component.onCompleted: {
        createRoomInput.clear()
        joinRoomInput.clear()
    }

    Connections {
        target: channelManager
        onCreateChannelSignal: {
            if (resCode == 200 && channelId !== '') {
                channelManager.joinChannel()
            } else {
                messageDialog.title = "提示"
                messageDialog.text = "创建信令通道失败，错误代码：" + resCode
                messageDialog.visible = true
            }
        }
        onJoinChannelSignal: {
            if (resCode == 200 && channelId !== '') {
                 pageLoader.setSource(Qt.resolvedUrl("SignalingRoom.qml"))
            } else {
                messageDialog.title = "提示"
                messageDialog.text = "加入信令通道失败，错误代码：" + resCode
                messageDialog.visible = true
            }
        }
    }
}
