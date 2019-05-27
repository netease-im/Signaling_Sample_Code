import QtQuick 2.12
import QtQuick.Window 2.12
import QtQuick.Dialogs 1.2
import QtQuick.Controls 2.12
import QtQuick.Controls.Material 2.3
import QtGraphicalEffects 1.12

ApplicationWindow {
    id: mainWindow
    visible: true
    width: 800
    height: 560
    title: qsTr("Hello World")
    flags: Qt.Window | Qt.FramelessWindowHint
    background: Rectangle {
        color: '#F0F2F5'
        border.width: 1
        border.color: '#222'
    }

    Component.onCompleted: {
        pageLoader.setSource(Qt.resolvedUrl("Login.qml"))
    }

    MessageDialog {
        id: messageDialog
        title: ""
        text: ""
        Component.onCompleted: visible = false
    }

    Caption {
        id: caption
        windowForm: parent
        title: '网易云信 - 信令通道示例程序'
    }

    Loader {
        id: pageLoader
        anchors.top: caption.bottom
        anchors.bottom: parent.bottom
        anchors.left: parent.left
        anchors.right: parent.right
    }
}
