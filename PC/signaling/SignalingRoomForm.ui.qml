import QtQuick 2.4
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3

Rectangle {
    id: chatroom
    property alias leaveRoom: leaveRoom
    property alias listModel: listModel
    property alias listView: listView
    anchors.fill: parent
    border.width: 1
    border.color: '#9d9d9d'

    Label {
        id: channelName
        anchors.top: parent.top
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.topMargin: 40
        text: '房间名称: ' + channelManager.channelName
        font {
            pixelSize: 24
        }
    }

    Rectangle {
        width: parent.width * 0.8
        height: parent.height * 0.6
        anchors.centerIn: parent

        ListModel {
            id: listModel
        }

        ListView {
            id: listView
            anchors.fill: parent
            model: listModel
            delegate: listDelegate
        }
    }

    Button {
        id: leaveRoom
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 40
        anchors.horizontalCenter: parent.horizontalCenter
        text: qsTr('离开房间')
        highlighted: true
        Material.accent: Material.Red
    }
}




/*##^## Designer {
    D{i:0;autoSize:true;height:480;width:640}
}
 ##^##*/
