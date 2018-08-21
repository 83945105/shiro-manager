const buildConf = require('./build.conf');

/**
 * 服务端地址
 */
const server_path = basePath;

const serverPath = function () {
  return buildConf.existsBasePath ? '' : server_path;
}();

module.exports = {
  /**
   * 服务端地址
   */
  serverPath: serverPath
};
