#ifndef LOGIN_MANAGER_H
#define LOGIN_MANAGER_H

#include <QObject>
#include <QQmlEngine>
#include <QJSEngine>

#include "nim_cpp_client.h"

class LoginManager : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString username READ userName WRITE setUserName NOTIFY userNameChanged)
    Q_PROPERTY(QString password READ password WRITE setPassword NOTIFY passwordChanged)

public:
    explicit LoginManager(QObject *parent = nullptr);

    // 用户名和密码设置
    QString userName() { return m_userName; }
    void setUserName(const QString& username);
    QString password() { return m_password; }
    void setPassword(const QString& password);

    // callback
    void onLoginCallback(const nim::LoginRes& login_res);

    // appKey
    static const std::string kAppKey;

signals:
    void userNameChanged();
    void passwordChanged();
    void loginCallback(int loginStep, int resCode);

public slots:
    void doLogin();
    void doLogout();

private:
    QString m_userName;
    QString m_password;
};

#endif // LOGIN_MANAGER_H
