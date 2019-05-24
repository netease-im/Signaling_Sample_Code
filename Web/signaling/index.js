var nim;

let steps = [$("#frame-1"), $("#frame-2"), $("#frame-3")];
function showStep(num) {
  let index = num - 1;
  steps.forEach(function($el, $index) {
    if ($index == index) {
      $el.removeClass("hide");
    } else {
      $el.addClass("hide");
    }
  });
}

$("#login").on("click", function(e) {
  var account = $("#account").val();
  var token = $("#token").val();

  // 1. 这里还是nim的登录
  nim = NIM.getInstance({
    appKey: "45c6afxxxxxxxxxxxxxxxxxxxxxxxxxxx",// WRITE_APPKEY_HERE
    account: account,
    token: token,
    onconnect: function(e) {
      console.log("connected", e);
      // 登录成功后，改变界面
      showStep(2);

      // 显示用户名称，和退出登录按钮
      $("#ui-user").text(account);
    },
    ondisconnect: function(e) {
      console.log("disconnect", e);
    },
    onerror: function(e) {
      console.log("error", e);
    }
  });

  // 在线用户接收到的通知事件,登录之后
  nim.on("signalingNotify", signalingNotifyHandler);
});

function logout() {
  if (nim) {
    nim = nim.destroy();
    showStep(1);
  }
}

$("#create-room").on("click", function(e) {
  var channelName = $("#channelName").val();
  if (nim) {
    var param = {
      type: 1, // 必填字段 通话类型,1:音频;2:视频;3:其他
      channelName: channelName, // 房间别名
      ext: ""
    };
    // 2. 创建房间
    nim
      .signalingCreate(param)
      .then(data => {
        console.warn("独立呼叫信令，创建房间成功，data：", data);

        //  3.创建房间后，自动加入这个房间
        var channelId = data.channelId;
        joinRoom(channelId);

        $("#ui-channelId").text(channelId);
        $("#exit-room")
          .text("关闭房间")
          .one("click", function() {
            closeRoom(channelId);
          });
      })
      .catch(error => {
        console.warn("独立呼叫信令，创建房间失败，error：", error);
        if (error.code == 10405) {
          console.warn("独立呼叫信令：房间已存在，请勿重复创建");
        }
      });
  }
});
function joinRoom(channelId) {
  var param = {
    channelId: channelId,
    offlineEnabled: false,
    attachExt: ""
  };
  // 3.加入房间操作
  nim
    .signalingJoin(param)
    .then(data => {
      console.warn("独立呼叫信令，加入房间成功，data：", data);
      showLog(nim.account + "加入房间");
      showLog(
        "当前房间内有这些用户:" + data.members.map(member => member.accid)
      );

      //展示ui
      showStep(3);
    })
    .catch(error => {
      console.warn("独立呼叫信令，加入房间失败，error：", error);
      switch (error.code) {
        case 10407:
          console.warn("独立呼叫信令：已经在房间内");
          break;
        case 10419:
          console.warn("独立呼叫信令：房间人数超限");
          break;
        case 10417:
          console.warn("独立呼叫信令：房间成员uid冲突了");
          break;
        case 10420:
          console.warn(
            "独立呼叫信令：该账号，在其他端已经登录，并且已经在房间内"
          );
          break;
        case 10404:
          console.warn("独立呼叫信令：房间不存在");
          break;
      }
    });
}

$("#join-room").on("click", function(e) {
  var channelId = $("#channelId-join").val();

  $("#ui-channelId").text(channelId);
  joinRoom(channelId);

  // 绑定退出房间事件
  $("#exit-room").one("click", function(e) {
    var param = {
      channelId: channelId,
      offlineEnabled: true,
      attachExt: ""
    };
    nim
      .signalingLeave(param)
      .then(data => {
        console.warn("独立呼叫信令，离开房间成功，data：", data);
        showStep(2);
        clearLog();
      })
      .catch(error => {
        console.warn("独立呼叫信令，离开房间失败，error：", error);
        if (error.code == 10406) {
          console.warn("独立呼叫信令：不在频道内");
        }
      });
  });
});

//关闭房间
function closeRoom(channelId) {
  var param = {
    channelId: channelId,
    offlineEnabled: true,
    ext: ""
  };
  // 关闭房间操作
  nim
    .signalingClose(param)
    .then(data => {
      console.warn("独立呼叫信令，关闭房间成功，data：", data);
      alert("关闭房间成功！");
      showStep(2);
      clearLog();
    })
    .catch(error => {
      console.warn("独立呼叫信令，关闭房间失败，error：", error);
      if (error.code == 10406) {
        console.warn("独立呼叫信令：你不在房间内，无法关闭");
      }
    });
}

function clearLog() {
  $("#logger").empty();
}
function showLog(text) {
  let $item = $("<div>" + text + "</div>");
  $("#logger").append($item);
  $item.get(0).scrollIntoView();
}

function signalingNotifyHandler(event) {
  console.log("signalingOnlineNotify: ", event);
  switch (event.eventType) {
    case "ROOM_CLOSE":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
        */
      console.log("独立呼叫信令：房间关闭事件");
      alert("房间被关闭了！");
      showStep(2);
      clearLog();
      break;
    case "ROOM_JOIN":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
          event.member 新加入的成员信息
        */
      console.log("独立呼叫信令：加入房间事件");
      showLog(event.from + "加入房间");
      break;
    case "INVITE":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
          event.toAccount 接收者的账号
          event.requestId 邀请者邀请的请求id，用于被邀请者回传request_id_作对应的回应操作
          event.pushInfo 推送信息
        */
      console.log("独立呼叫信令： 邀请事件");
      break;
    case "CANCEL_INVITE":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
          event.toAccount 接收者的账号
          event.requestId 邀请者邀请的请求id，用于被邀请者回传request_id_作对应的回应操作
        */
      console.log("独立呼叫信令：取消邀请事件");
      break;
    case "REJECT":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
          event.toAccount 接收者的账号
          event.requestId 邀请者邀请的请求id，用于被邀请者回传request_id_作对应的回应操作
        */
      console.log("独立呼叫信令：拒绝邀请事件");
      break;
    case "ACCEPT":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
          event.toAccount 接收者的账号
          event.requestId 邀请者邀请的请求id，用于被邀请者回传request_id_作对应的回应操作
        */
      console.log("独立呼叫信令：接受邀请事件");
      break;
    case "LEAVE":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
        */
      console.log("独立呼叫信令：离开房间事件");
      showLog(event.from + "离开房间事件");
      break;
    case "CONTROL":
      /* 该事件的通知内容
          event.eventType 事件类型
          event.channelInfo 房间信息属性
          event.fromAccountId 操作者
          evnet.attachExt 操作者附加的自定义信息，透传给你
          event.time 操作的时间戳
        */
      console.log("独立呼叫信令：自定义控制事件");
      break;
  }
}
