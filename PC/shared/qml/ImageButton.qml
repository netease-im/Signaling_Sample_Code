import QtQuick 2.12

Rectangle {
    id: root
    color: 'transparent'

    signal clicked

    property url normalImage: backgroundImage.source
    property url hoveredImage: backgroundImage.source
    property url pushedImage: backgroundImage.source

    Component.onCompleted: {
        backgroundImage.source = normalImage
    }

    MouseArea {
        anchors.fill: parent
        onClicked: root.clicked()
        hoverEnabled: true
        onHoveredChanged: {
            backgroundImage.source = containsMouse ? hoveredImage : normalImage
        }
        onPressedChanged: {
            if (containsMouse) {
                if (pressed) {
                    backgroundImage.source = pushedImage
                }
                else {
                    backgroundImage.source = hoveredImage
                }
            } else {
                backgroundImage.source = normalImage
            }
        }
    }

    Image {
        id: backgroundImage
        anchors.fill: parent
        fillMode: Image.PreserveAspectFit
    }
}
