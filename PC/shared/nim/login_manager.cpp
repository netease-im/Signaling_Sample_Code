#include "login_manager.h"
#include <QDebug>
#include <QCryptographicHash>

LoginManager::LoginManager(QObject *parent)
    : QObject(parent)
{

}

void LoginManager::setUserName(const QString &username)
{
    if (username == m_userName)
        return;

    m_userName = username;
    emit userNameChanged();
}

void LoginManager::setPassword(const QString &password)
{
    if (password == m_password)
        return;

    m_password = password;
    emit passwordChanged();
}

void LoginManager::onLoginCallback(const nim::LoginRes &login_res)
{
    emit loginCallback(login_res.login_step_, login_res.res_code_);
}

void LoginManager::doLogin()
{
    if (kAppKey.empty()) {
        emit loginCallback(3, -1);
        return;
    }

    // QByteArray md5 = QCryptographicHash::hash(m_password.toUtf8(), QCryptographicHash::Md5);
    // Use md5.toHex() to set password if the password need to encrypt
    nim::Client::Login(kAppKey, m_userName.toStdString(), m_password.toStdString(),
                       std::bind(&LoginManager::onLoginCallback, this, std::placeholders::_1));
}

void LoginManager::doLogout()
{
    nim::Client::Logout(nim::kNIMLogoutChangeAccout, nim::Client::LogoutCallback());
}
