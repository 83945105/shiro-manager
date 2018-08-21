/*!
 * Title: 用于对接微信相关的一些配置
 * Description:
 * author: 白超
 * date: 2018-3-31
 * version: v1.0
 */

module.exports = {

  /**
   * 公众号相关
   */
  official_accounts: {
    //公众号的唯一标识
    appId: 'wx395af6944120afc3',
    //授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理
    redirect_uri: encodeURIComponent(`http://avalon.nat100.top/jp/view/wx`),
    //返回类型，请填写code
    response_type: 'code',
    //	应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
    scope: 'snsapi_userinfo',
    //重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
    state: '',
    //无论直接打开还是做页面302重定向时候，必须带此参数
    suffix: '#wechat_redirect'
  }

};
