import QtQuick 2.4

LoginForm {
    password.onEditingFinished: {
    }

    submit.onClicked: {
        if (username.text.length === 0 || password.text.length === 0) {
            messageDialog.title = "提示"
            messageDialog.text = "无效的用户名或密码。"
            messageDialog.visible = true
            return
        }

        loginForm.state = "logon"
        loginManager.doLogin()
    }

    Component.onCompleted: {
        username.clear()
        password.clear()
    }

    Connections {
        target: loginManager
        onLoginCallback: {
            if (loginStep === 3) {
                loginForm.state = ""

                switch (resCode)
                {
                case -1:
                    messageDialog.title = "尚未填入 AppKey"
                    messageDialog.text = "请根据 Github 说明填入 AppKey"
                    messageDialog.visible = true
                    break
                case 200:
                    pageLoader.setSource(Qt.resolvedUrl("SignalingFront.qml"))
                    break
                case 302:
                    messageDialog.title = "提示"
                    messageDialog.text = "无效的用户名或密码"
                    messageDialog.visible = true
                    break
                default:
                    messageDialog.title = "提示"
                    messageDialog.text = "登录失败，错误代码：" + resCode
                    messageDialog.visible = true
                    break
                }
            }
        }
    }
}
