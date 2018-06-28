/**
 * 咚咚服务器返回错误码
 */
function serverErrorMsg(res) {
    var failReason = "";
    switch (res.code) {
        case -1:
            failReason = "您的帐号已在其他设备上登录";
            //自动登陆
            // window.location.href='';//跳转到登录地址
            break;
        case 0:
            failReason = "成功";
            break;
        case 1:
            failReason = res.info;
            break;
        case 101:
            failReason = "参数错误，请联系开发人员";
            break;
        case 102:
            failReason = "服务器异常，请稍后重试";
            break;
        case 103:
            failReason = "您的账号已被禁用，请联系客服人员";
            break;
        default:
            failReason = "未知错误，请联系管理员";
            break;
    }
    //弹窗错误提示（暂留）
    alert(failReason);
}

//HTTP返回状态码错误
function httpErrorMsg(res) {
  var statusCode = res.statusCode;
  var showMsg = '';
  console.log(res);
  switch (statusCode) {
    case 400:
      showMsg = '请求失败，请联系管理员';
      break;
    case 403:
      showMsg = '禁止访问，请联系管理员';
      break;
    case 404:
      showMsg = '服务器连接异常,请联系管理员';
      break;
    case 414:
      showMsg = '请求 - URI 太长,请联系管理员';
      break;
    case 500:
      showMsg = '内部服务器错误,请联系管理员';
      break;
    default:
      showMsg = res.errMsg;
  }
  alert(failReason);
}