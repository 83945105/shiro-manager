/*!
 * Title: 打包时修改该文件
 * Description:
 * author: 白超
 * date: 2017/12/7
 * version: v1.1
 */

const packageJson = require('../../package.json');

module.exports = {

  /**
   * 排除的模块名
   * 被排除的模块在开发及打包时不会被编译
   * 可以有效提升开发编译效率
   */
  excludeModules: [],

  /**
   * 是否是开发环境
   * 打包时改为false
   * 用来控制如axios的代理地址映射
   * 默认根据系统环境变量来判断
   */
  development_environment: false,

  /**
   * 是否已经存在根路径
   * 一般用于区分包含base标签的JSP页面和Html页面
   * 如果是用于thymeleaf模板,需要页面内定义basePath全局变量
   */
  existsBasePath: false,

  /**
   * 前端打包目标文件夹
   * 所有前端文件将会打包进该配置目录下
   */
  assetsRoot: '../../../后端/shiro-manager-spring/src/main/resources/static',

  /**
   * 前端打包后的根目录文件夹名,默认为项目名
   */
  assetsSubDirectory: `${packageJson.name}`,

  /**
   * 所有页面文件所在的文件夹名
   * 如果配置为view,则页面文件将打包进 ${assetsRoot}/${assetsSubDirectory}/view 文件夹下
   */
  htmlsSubDirectory: 'view',

  /**
   * 该配置影响资源文件的引用路径
   */
  assetsPublicPath: process.env.BUILD_TYPE == 'server' ? `../../${packageJson.name}/` : '../../../',

  /**
   * 入口文件相关配置
   */
  mains: {
    sysconf: {
      title: '系统管理'//暂时无效,后续使用EJS实现
    }
  },

  /**
   * 依赖的第三方js文件
   * 禁止使用vendor,因为会默认把所有入口文件的公共代码打包成vendor文件
   */
  vendors: {
    /*    vendor1: {//组名
          resources: ['static/js/jquery-1.11.3.min.js'],
          owners: ['sysconf']//属于哪个模块入口
        },*/
    /*    vendor2: {
          resources: ['static/easyui/jquery.easyui.min.js'],
          owners: ['sysconf']
        },
        vendor3: {
          resources: ['static/easyui-extension/datagrid-cellediting.js', 'static/easyui-extension/datagrid-filter.js'],
          owners: ['sysconf']
        }*/
  }
};
