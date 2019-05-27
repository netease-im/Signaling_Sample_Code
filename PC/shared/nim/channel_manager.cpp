#include "channel_manager.h"
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonValue>
#include <QDebug>
#include <QUuid>

ChannelManager::ChannelManager(QObject *parent)
    : QObject(parent)
{
    nim::Signaling::RegOnlineNotifyCb(std::bind(&ChannelManager::onlineNotifyCallback, this, std::placeholders::_1));
}

ChannelManager::~ChannelManager()
{
    closeChannel();
}

void ChannelManager::onlineNotifyCallback(std::shared_ptr<nim::SignalingNotityInfo> info)
{
    if (info->event_type_ == nim::kNIMSignalingEventTypeClose) {
        m_fromAccount = "";
        m_toAccount = "";
        m_channelCreator = false;
        m_channelId = "";
        m_requestId = "";
    } else if (info->event_type_ == nim::kNIMSignalingEventTypeInvite) {
        auto inviteInfo = std::reinterpret_pointer_cast<nim::SignalingNotityInfoInvite>(info);
        m_fromAccount = inviteInfo->from_account_id_.c_str();
        m_channelId = inviteInfo->channel_info_.channel_id_.c_str();
        m_requestId = inviteInfo->request_id_.c_str();
        m_toAccount = inviteInfo->to_account_id_.c_str();
        m_channelName = inviteInfo->channel_info_.channel_name_.c_str();
    } else if (info->event_type_ == nim::kNIMSignalingEventTypeAccept) {
        auto acceptInfo = std::reinterpret_pointer_cast<nim::SignalingNotityInfoAccept>(info);
        m_fromAccount = acceptInfo->from_account_id_.c_str();
        m_toAccount = acceptInfo->to_account_id_.c_str();
    } else if (info->event_type_ == nim::kNIMSignalingEventTypeLeave) {
        auto leaveInfo = std::reinterpret_pointer_cast<nim::SignalingNotityInfoLeave>(info);
    }

    qDebug() << "Channel notify:" << info->event_type_ << info->from_account_id_.c_str();
    emit channelNotifySignal(info->from_account_id_.c_str(), info->event_type_);
}

void ChannelManager::createChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param)
{
    if (resCode == 200) {
        auto createResParam = std::reinterpret_pointer_cast<nim::SignalingCreateResParam>(param);
        setChannelId(createResParam->channel_info_.channel_id_.c_str());
        setChannelName(createResParam->channel_info_.channel_name_.c_str());
        m_channelCreator = true;
    }

    emit createChannelSignal(resCode, m_channelId);
}

void ChannelManager::joinChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param)
{
    if (resCode == 200) {
        auto joinResParam = std::reinterpret_pointer_cast<nim::SignalingJoinResParam>(param);
        m_channelId = joinResParam->info_.channel_info_.channel_id_.c_str();
        m_channelName = joinResParam->info_.channel_info_.channel_name_.c_str();
        m_channelCreatorId = joinResParam->info_.channel_info_.creator_id_.c_str();
        for (auto& member : joinResParam->info_.members_) {
            if (joinResParam->info_.channel_info_.creator_id_ == member.account_id_) {
                setCurrentUid(std::to_string(member.uid_).c_str());
            }
        }
    }

    qDebug() << "Join channel callback, result code =" << resCode
             << ", current user id:" << m_currentUid
             << ", channel id:" << m_channelId
             << ", channel name:" << m_channelName
             << ", channel creator:" << m_channelCreator;

    emit joinChannelSignal(resCode, m_channelId);
}

void ChannelManager::leaveChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param)
{
    emit leaveChannelSignal(resCode, m_channelId);
}

void ChannelManager::closeChannelCallback(int resCode, std::shared_ptr<nim::SignalingResParam> /*param*/)
{
    if (resCode == 200) {
        m_fromAccount = "";
        m_toAccount = "";
        m_channelCreator = false;
        m_channelId = "";
        m_requestId = "";
    }
}

void ChannelManager::inviteMemberCallback(int resCode, std::shared_ptr<nim::SignalingResParam> /*param*/)
{
    emit inviteMemberSignal(resCode);
}

void ChannelManager::callCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param)
{
    if (resCode == 200 || resCode == 10201 || resCode == 10202) {
        auto callResParam = std::reinterpret_pointer_cast<nim::SignalingCallResParam>(param);
        m_channelId = callResParam->info_.channel_info_.channel_id_.c_str();
        m_channelName = callResParam->info_.channel_info_.channel_name_.c_str();
        m_channelCreatorId = callResParam->info_.channel_info_.creator_id_.c_str();
        for (auto& member : callResParam->info_.members_) {
            if (callResParam->info_.channel_info_.creator_id_ == member.account_id_) {
                setCurrentUid(std::to_string(member.uid_).c_str());
            }
        }
    }

    emit calledSignal(resCode, m_channelId);
}

void ChannelManager::cancelInviteCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param)
{
    if (resCode == 200) {
        auto cancelInvite = std::reinterpret_pointer_cast<nim::SignalingCancelInviteResParam>(param);
    }

    emit cancelCallSignal(resCode, "");
}

void ChannelManager::acceptCallback(int resCode, std::shared_ptr<nim::SignalingResParam> param)
{
    qDebug() << "Accept invite callback:" << resCode;

    if (resCode == 200) {
        auto acceptParam = std::reinterpret_pointer_cast<nim::SignalingAcceptResParam>(param);
        auto& info = acceptParam->info_;
        for (auto& member : info.members_) {
            if (m_toAccount.toStdString() == member.account_id_) {
                setCurrentUid(std::to_string(member.uid_).c_str());
            }
        }
    }

    emit acceptSignal(resCode, "");
}

void ChannelManager::rejectCallback(int resCode, std::shared_ptr<nim::SignalingResParam> /*param*/)
{
    emit rejectSignal(resCode, "");
}

void ChannelManager::createChannel()
{
    nim::SignalingCreateParam param;
    param.channel_type_ = nim::kNIMSignalingTypeVideo;
    param.channel_name_ = m_createChannelName.toStdString();
    nim::Signaling::SignalingCreate(param, std::bind(&ChannelManager::createChannelCallback, this,
                                                     std::placeholders::_1, std::placeholders::_2));
}

void ChannelManager::joinChannel()
{
    nim::SignalingJoinParam param;
    if (m_channelCreator) {
        param.channel_id_ = m_channelId.toStdString();
        nim::Signaling::Join(param, std::bind(&ChannelManager::joinChannelCallback, this,
                                              std::placeholders::_1, std::placeholders::_2));
    } else {
        // 根据名称查询 channel id 来再加入频道
        nim::SignalingQueryChannelInfoParam query;
        query.channel_name_ = m_joinChannelName.toStdString();
        nim::Signaling::QueryChannelInfo(query, [&](int resCode, std::shared_ptr<nim::SignalingResParam> info) {
            if (resCode == 200) {
                auto channelInfo = std::reinterpret_pointer_cast<nim::SignalingQueryChannelInfoResParam>(info);
                param.channel_id_ = channelInfo->info_.channel_info_.channel_id_;
                nim::Signaling::Join(param, std::bind(&ChannelManager::joinChannelCallback, this,
                                                      std::placeholders::_1, std::placeholders::_2));
            } else {
                qDebug() << "Failed to query channel name: " << m_joinChannelName << ", result code: " << resCode;
                emit joinChannelSignal(resCode, "");
            }
        });

    }
}

void ChannelManager::inviteMember()
{
    m_requestId = QUuid::createUuid().toString();

    nim::SignalingInviteParam param;
    param.account_id_ = m_callAccount.toStdString();
    param.channel_id_ = m_channelId.toStdString();
    param.request_id_ = m_requestId.toStdString();
    nim::Signaling::Invite(param, std::bind(&ChannelManager::inviteMemberCallback, this,
                                           std::placeholders::_1, std::placeholders::_2));
}

void ChannelManager::leaveChannel()
{
    nim::SignalingLeaveParam param;
    param.channel_id_ = m_channelId.toStdString();
    param.offline_enabled_ = false;
    nim::Signaling::Leave(param, std::bind(&ChannelManager::leaveChannelCallback, this,
                                           std::placeholders::_1, std::placeholders::_2));

    closeChannel();
}

void ChannelManager::closeChannel()
{
    if (!m_channelId.isEmpty() && m_channelCreator) {
        nim::SignalingCloseParam param;
        param.channel_id_ = m_channelId.toStdString();
        param.offline_enabled_ = false;
        nim::Signaling::SignalingClose(param, std::bind(&ChannelManager::closeChannelCallback, this,
                                                        std::placeholders::_1, std::placeholders::_2));
    }
}

void ChannelManager::call()
{
    m_requestId = QUuid::createUuid().toString();

    nim::SignalingCallParam param;
    param.account_id_ = m_callAccount.toStdString();
    param.request_id_ = m_requestId.toStdString();
    param.channel_type_ = nim::kNIMSignalingTypeVideo;
    param.offline_enabled_ = false;
    nim::Signaling::Call(param, std::bind(&ChannelManager::callCallback, this,
                                          std::placeholders::_1, std::placeholders::_2));
}

void ChannelManager::cancelInvite()
{
    if (!m_callAccount.isEmpty() && !m_requestId.isEmpty() && !m_channelId.isEmpty()) {
        nim::SignalingCancelInviteParam param;
        param.account_id_ = m_callAccount.toStdString();
        param.channel_id_ = m_channelId.toStdString();
        param.request_id_ = m_requestId.toStdString();
        param.offline_enabled_ = false;
        nim::Signaling::CancelInvite(param, std::bind(&ChannelManager::cancelInviteCallback, this,
                                                      std::placeholders::_1, std::placeholders::_2));
    }
}

void ChannelManager::accept()
{
    nim::SignalingAcceptParam param;
    param.auto_join_ = true;
    param.account_id_ = m_fromAccount.toStdString();
    param.channel_id_ = m_channelId.toStdString();
    param.request_id_ = m_requestId.toStdString();
    param.offline_enabled_ = false;

    nim::Signaling::Accept(param, std::bind(&ChannelManager::acceptCallback, this,
                                            std::placeholders::_1, std::placeholders::_2));
}

void ChannelManager::reject()
{
    nim::SignalingRejectParam param;
    param.account_id_ = m_fromAccount.toStdString();
    param.channel_id_ = m_channelId.toStdString();
    param.request_id_ = m_requestId.toStdString();
    param.offline_enabled_ = false;
    nim::Signaling::Reject(param, std::bind(&ChannelManager::rejectCallback, this,
                                            std::placeholders::_1, std::placeholders::_2));
}
