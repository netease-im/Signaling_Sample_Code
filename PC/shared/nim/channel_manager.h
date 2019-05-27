#ifndef CHANNEL_MANAGER_H
#define CHANNEL_MANAGER_H

#include <QMutex>
#include <QObject>

#include "nim_cpp_signaling.h"

class ChannelManager : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString createChannelName READ createChannelName WRITE setCreateChannelName NOTIFY createChannelNameChanged)
    Q_PROPERTY(QString joinChannelName READ joinChannelName WRITE setJoinChannelName NOTIFY joinChannelNameChanged)
    Q_PROPERTY(QString callAccount READ callAccount WRITE setCallAccount NOTIFY callAccountChanged)

    Q_PROPERTY(QString channelName READ channelName WRITE setChannelName NOTIFY channelNameChanged)
    Q_PROPERTY(QString channelId READ channelId WRITE setChannelId NOTIFY channelIdChanged)
    Q_PROPERTY(QString fromAccount READ fromAccount WRITE setFromAccount NOTIFY fromAccountChanged)
    Q_PROPERTY(QString toAccount READ toAccount WRITE setToAccount NOTIFY toAccountChanged)
    Q_PROPERTY(QString currentUid READ currentUid WRITE setCurrentUid NOTIFY currentUidChanged)

    Q_PROPERTY(bool meetingMode READ meetingMode WRITE setMeetingMode NOTIFY meetingModeChanged)

public:
    explicit ChannelManager(QObject *parent = nullptr);
    ~ChannelManager();

private:
    QString createChannelName() { return m_createChannelName; }
    void setCreateChannelName(const QString& strCreateRoomName) { m_createChannelName = strCreateRoomName; }

    QString joinChannelName() { return m_joinChannelName; }
    void setJoinChannelName(const QString& strJoinRoomName) { m_joinChannelName = strJoinRoomName; }

    QString callAccount() { return m_callAccount; }
    void setCallAccount(const QString& callAccount) { m_callAccount = callAccount; }

    QString channelName() { return m_channelName; }
    void setChannelName(const QString& channelName) { m_channelName = channelName; }

    QString channelId() { return m_channelId; }
    void setChannelId(const QString& channelId) { m_channelId = channelId; }

    QString fromAccount() { return m_fromAccount; }
    void setFromAccount(const QString& fromAccount) { m_fromAccount = fromAccount; }

    QString toAccount() { return m_toAccount; }
    void setToAccount(const QString& toAccount) { m_toAccount = toAccount; }

    QString currentUid() { return std::to_string(m_currentUid).c_str(); }
    void setCurrentUid(QString currentUid) { m_currentUid = currentUid.toLongLong(); }

    bool meetingMode() { return m_meetingMode; }
    void setMeetingMode(bool isMeetingMode) { m_meetingMode = isMeetingMode; }

    // callback
    void onlineNotifyCallback(std::shared_ptr<nim::SignalingNotityInfo> info);
    void createChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void joinChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void leaveChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void closeChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void inviteMemberCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void callCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void cancelInviteCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void acceptCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);
    void rejectCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param);

signals:
    void createChannelNameChanged();
    void joinChannelNameChanged();
    void channelNameChanged();
    void channelIdChanged();
    void callAccountChanged();
    void fromAccountChanged();
    void toAccountChanged();
    void currentUidChanged();
    void meetingModeChanged();

    void channelNotifySignal(const QString& userId, int eventType);
    void createChannelSignal(int resCode, const QString& channelId);
    void joinChannelSignal(int resCode, const QString& channelId);
    void inviteMemberSignal(int resCode);
    void leaveChannelSignal(int resCode, const QString& channelId);
    void calledSignal(int resCode, const QString& channelId);
    void cancelCallSignal(int resCode, const QString& channelId);
    void acceptSignal(int resCode, const QString& channelId);
    void rejectSignal(int resCode, const QString& channelId);

public slots:
    void createChannel();
    void joinChannel();
    void inviteMember();
    void leaveChannel();
    void closeChannel();
    void call();
    void cancelInvite();
    void accept();
    void reject();

private:
    ChannelManager* m_Instance;

    // 独立信令演示所需成员
    QString m_createChannelName;
    QString m_joinChannelName;
    QString m_channelName;
    QString m_channelId;
    QString m_channelCreatorId;
    QString m_callAccount;      /* 与 QML 控件绑定 */

    // 通道链接后的数据信息
    QString m_requestId;
    QString m_fromAccount;
    QString m_toAccount;
    int64_t m_currentUid;

    bool    m_channelCreator = false;
    bool    m_meetingMode = false;
};

#endif // CHANNEL_MANAGER_H
