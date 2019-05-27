import QtQuick 2.4
import QtQuick.Controls 2.12
import QtQuick.Controls.Material 2.3

Rectangle {
    id: loginForm
    property alias submit: submit
    property alias password: password
    property alias username: username
    property alias loginForm: loginForm
    anchors.fill: parent
    border.width: 1
    border.color: "#9d9d9d"

    Rectangle {
        anchors.fill: parent
        color: "#99000000"
        z: 10
        visible: loginForm.state === "logon"

        BusyIndicator {
            id: loading
            z: 11
            anchors.centerIn: parent
            running: loginForm.state === "logon"
        }
    }

    Column {
        id: column
        width: 330
        height: 160
        anchors.verticalCenter: parent.verticalCenter
        anchors.horizontalCenter: parent.horizontalCenter
        spacing: 20

        TextField {
            id: username
            text: loginManager.username
            width: parent.width
            placeholderText: "输入用户名"
            onTextChanged: loginManager.username = text
            enabled: loginForm.state !== "logon"
        }

        TextField {
            id: password
            text: loginManager.password
            width: parent.width
            placeholderText: "输入密码"
            echoMode: TextField.Password
            onTextChanged: loginManager.password = text
            enabled: loginForm.state !== "logon"
        }

        Button {
            id: submit
            width: parent.width
            text: "登录"
            highlighted: true
            Material.accent: Material.LightBlue
            enabled: loginForm.state !== "logon"
        }
    }
}




/*##^## Designer {
    D{i:0;autoSize:true;height:480;width:640}
}
 ##^##*/
