import QtQuick 2.4

SignalingRoomForm {
    leaveRoom.onClicked: {
        channelManager.leaveChannel()
        pageLoader.setSource(Qt.resolvedUrl("SignalingFront.qml"))
    }

    Component.onCompleted: {
        listModel.append({ userId: "欢迎你", eventType: 2 })
    }

    enum EventType {
        NIMSignalingEventTypeClose			= 1, /**< 返回NIMSignalingNotityInfoClose，支持在线、离线通知 */
        NIMSignalingEventTypeJoin			= 2, /**< 返回NIMSignalingNotityInfoJoin，支持在线、离线通知 */
        NIMSignalingEventTypeInvite         = 3, /**< 返回NIMSignalingNotityInfoInvite，支持在线、离线通知 */
        NIMSignalingEventTypeCancelInvite	= 4, /**< 返回NIMSignalingNotityInfoCancelInvite，支持在线、离线通知 */
        NIMSignalingEventTypeReject         = 5, /**< 返回NIMSignalingNotityInfoReject，支持在线、多端同步、离线通知 */
        NIMSignalingEventTypeAccept         = 6, /**< 返回NIMSignalingNotityInfoAccept，支持在线、多端同步、离线通知 */
        NIMSignalingEventTypeLeave			= 7, /**< 返回NIMSignalingNotityInfoLeave，支持在线、离线通知 */
        NIMSignalingEventTypeCtrl			= 8
    }

    Component {
        id: listDelegate
        Text {
            text: {
                var message

                switch (eventType)
                {
                case SignalingRoom.EventType.NIMSignalingEventTypeClose:
                    message = "关闭了房间"
                    break
                case SignalingRoom.EventType.NIMSignalingEventTypeJoin:
                    message = "加入了房间"
                    break
                case SignalingRoom.EventType.NIMSignalingEventTypeInvite:
                    message = "邀请你加入房间"
                    break
                case SignalingRoom.EventType.NIMSignalingEventTypeCancelInvite:
                    message = "取消了邀请"
                    break
                case SignalingRoom.EventType.NIMSignalingEventTypeReject:
                    message = "拒绝了邀请"
                    break
                case SignalingRoom.EventType.NIMSignalingEventTypeAccept:
                    message = "接受了邀请"
                    break
                case SignalingRoom.EventType.NIMSignalingEventTypeLeave:
                    message = "离开了房间"
                    break
                }

                return userId + " - " + message
            }

            font.pixelSize: 16
        }
    }

    Connections {
        target: channelManager
        onChannelNotifySignal: {
            listModel.append({ userId: userId, eventType: eventType })
            listView.End
        }
    }
}
