#include <QFile>
#include <QString>
#include <QDateTime>
#include <QQuickStyle>
#include <QQmlContext>
#include <QGuiApplication>
#include <QQmlApplicationEngine>

#include "nim_cpp_client.h"
#include "login_manager.h"
#include "channel_manager.h"

#include <QDebug>

const std::string LoginManager::kAppKey = "";

int main(int argc, char *argv[])
{
    // Init NIM SDK before create application
    nim::Client::Init(LoginManager::kAppKey, "NIM_SAMPLES", "", nim::SDKConfig());

    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
    QGuiApplication app(argc, argv);
    QQuickStyle::setStyle("Material");

    QUrl url(QStringLiteral("qrc:/main.qml"));
    LoginManager*   loginManager    = new LoginManager();
    ChannelManager* channelManager  = new ChannelManager();

    QQmlApplicationEngine engine;
    engine.rootContext()->setContextProperty("loginManager", loginManager);
    engine.rootContext()->setContextProperty("channelManager", channelManager);

    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated, &app, [url](QObject *obj, const QUrl &objUrl) {
        if (!obj && url == objUrl) QCoreApplication::exit(-1);
    }, Qt::QueuedConnection);
    engine.load(url);

    int res = app.exec();

    if (loginManager != nullptr)
        delete loginManager;

    if (channelManager != nullptr)
        delete channelManager;

    nim::Client::Cleanup2(); // cleanup with logout

    return res;
}
