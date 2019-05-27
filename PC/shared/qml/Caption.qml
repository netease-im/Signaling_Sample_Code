import QtQuick 2.12
import QtQuick.Window 2.12
import QtQuick.Controls 2.12

Rectangle {
    id: caption
    height: 45
    width: parent.width
    color: "#001529"

    property alias  title       : captionTitle.text
    property var    windowForm  : undefined
    property point  movePos     : "0,0"

    MouseArea {
        anchors.fill: parent
        acceptedButtons: Qt.LeftButton
        onPressed: {
            movePos = Qt.point(mouse.x, mouse.y)
        }
        onPositionChanged: {
            const delta = Qt.point(mouse.x - movePos.x, mouse.y - movePos.y)
            mainWindow.x = mainWindow.x + delta.x
            mainWindow.y = mainWindow.y + delta.y
        }
    }

    Image {
        id: logo
        anchors.left: parent.left
        anchors.leftMargin: 12
        anchors.verticalCenter: parent.verticalCenter
        source: '../images/public/caption/logo_18x18.png'
    }

    Text {
        id: captionTitle
        color: "#a6ffffff"
        text: ""
        verticalAlignment: Text.AlignVCenter
        anchors.left: logo.right
        anchors.leftMargin: 10
        anchors.verticalCenter: parent.verticalCenter
        font.pixelSize: 16
    }

    ImageButton {
        id: minButton
        width: 24
        height: 24
        anchors.right: closeButton.left
        anchors.rightMargin: 5
        anchors.verticalCenter: parent.verticalCenter

        normalImage: '../images/public/button/btn_wnd_white_min.png'
        hoveredImage: '../images/public/button/btn_wnd_white_min_hovered.png'
        pushedImage: '../images/public/button/btn_wnd_white_min_pushed.png'

        onClicked: {
            mainWindow.showMinimized()
        }
    }

    ImageButton {
        id: closeButton
        width: 24
        height: 24
        anchors.right: parent.right
        anchors.rightMargin: 8
        anchors.verticalCenter: parent.verticalCenter

        normalImage: '../images/public/button/btn_wnd_white_close.png'
        hoveredImage: '../images/public/button/btn_wnd_white_close_hovered.png'
        pushedImage: '../images/public/button/btn_wnd_white_close_pushed.png'

        onClicked: {
            mainWindow.close()
        }
    }
}
