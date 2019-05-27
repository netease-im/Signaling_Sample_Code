#ifndef VIDEO_SOURCE_H
#define VIDEO_SOURCE_H

#include <QSize>
#include <QDebug>
#include <QObject>
#include <QVideoSurfaceFormat>
#include <QAbstractVideoSurface>

namespace agora { namespace media {
    class IVideoFrame;
}}

class FrameProvider : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QAbstractVideoSurface *videoSurface READ videoSurface WRITE setVideoSurface)

public:
    explicit FrameProvider(QObject *parent = nullptr);
    ~FrameProvider();

    QAbstractVideoSurface* videoSurface() const { return m_videoSurface; }
    void setVideoSurface(QAbstractVideoSurface* videoSurface);

signals:
    void providerInvalidated();

public slots:
    void deliverFrame(std::shared_ptr<QVideoFrame> frame, const QSize& videoSize);

private:
    QVideoSurfaceFormat     m_videoFormat;
    QAbstractVideoSurface*  m_videoSurface;
};

#endif // VIDEO_SOURCE_H
