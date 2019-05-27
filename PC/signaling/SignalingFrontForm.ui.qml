import QtQuick 2.4
import QtQuick.Controls 2.12
import QtQuick.Controls.Material 2.3

Rectangle {
    id: frontPage
    property alias createRoom: createRoom
    property alias joinRoom: joinRoom
    property alias createRoomInput: createRoomInput
    property alias joinRoomInput: joinRoomInput
    property alias btnLogout: btnLogout
    anchors.fill: parent
    border.width: 1
    border.color: "#9d9d9d"

    Button {
        id: btnLogout
        width: 100
        height: 30
        anchors.top: parent.top
        anchors.right: parent.right
        anchors.topMargin: 10
        anchors.rightMargin: 15
        text: "立即登出"
    }

    Row {
        width: 420
        height: 140
        anchors.verticalCenter: parent.verticalCenter
        anchors.horizontalCenter: parent.horizontalCenter
        spacing: 40

        Column {
            height: parent.height
            width: 190
            spacing: 35

            TextField {
                id: createRoomInput
                width: parent.width
                horizontalAlignment: Text.AlignHCenter
                placeholderText: "要创建的频道名"
                text: channelManager.createChannelName
                validator: RegExpValidator {
                    regExp: /^\d{1,8}$/
                }
                onTextChanged: channelManager.createChannelName = text
            }

            Button {
                id: createRoom
                width: parent.width
                text: "创建频道"
                highlighted: true
                Material.accent: Material.LightBlue
            }
        }

        Column {
            height: parent.height
            width: 190
            spacing: 35

            TextField {
                id: joinRoomInput
                width: parent.width
                horizontalAlignment: Text.AlignHCenter
                placeholderText: "要加入的频道名"
                text: channelManager.joinChannelName
                validator: RegExpValidator {
                    regExp: /^\d{1,8}$/
                }
                onTextChanged: channelManager.joinChannelName = text
            }

            Button {
                id: joinRoom
                width: parent.width
                text: "加入频道"
                highlighted: true
                Material.accent: Material.LightBlue
            }
        }
    }
}




/*##^## Designer {
    D{i:0;autoSize:true;height:480;width:640}
}
 ##^##*/
