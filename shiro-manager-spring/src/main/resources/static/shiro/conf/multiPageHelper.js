/*!
 * Title: 构建多页应用
 * Description:
 * author: 白超
 * date: 2017/12/6
 * version: v1.0
 */
const webpack = require('webpack')
const path = require('path');
const glob = require('glob');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const buildConf = require('./build.conf');

/**
 * 入口文件前置路径
 * 注意,默认从src目录开始
 * 开头结尾不要带/
 */
const entriesBasePath = 'page';
/**
 * 入口文件名称
 */
const entriesJsName = 'index';
/**
 * html模板名称
 */
const htmlName = 'index';

const entryNames = [];
/**
 * 获取入口配置对象
 */
exports.getEntries = function () {
  let entries = {};
  glob.sync(`./src/${entriesBasePath}/*/${entriesJsName}.js`).forEach(f => {
    let name = f.match(new RegExp(`src\/\\S*\/(\\S*)\/${entriesJsName}.js`))[1];
    for (let i in buildConf.excludeModules) {
      if (name === buildConf.excludeModules[i]) {
        return true;
      }
    }
    entries[`${name}`] = f;
    entryNames.push(name);
  });
  return entries;
};

/**
 * 获取第三方js入口对象
 */
exports.getVendors = function () {
  let vendors = {};
  let rows = buildConf.vendors;
  for (let vendor in rows) {
    if(!Array.isArray(rows[vendor].resources)) {
      throw new Error(`vendors ${vendor} resources属性必须是数组`);
    }
    vendors[vendor] = [];
    for (let js of rows[vendor].resources) {
      vendors[vendor].push(`./${js}`);
    }
  }
  return vendors;
};

/**
 * 获取dev时html配置信息
 */
exports.getDevHtmlWebpackPlugins = function () {
  let dev = [];
  dev.push(new webpack.optimize.CommonsChunkPlugin('vendor'));
  entryNames.forEach(n => {
    dev.push(new HtmlWebpackPlugin({
      title: `${buildConf.mains[n] ? buildConf.mains[n].title ? buildConf.mains[n].title : '未设置' : '未设置'}`,
      filename: `${buildConf.assetsSubDirectory}/${buildConf.htmlsSubDirectory}/${n}/${htmlName}.html`,//该路径影响访问路径和打包路径
      template: path.join(__dirname, `../../src/${entriesBasePath}/${n}/${htmlName}.html`),
      inject: true,
      chunksSortMode: 'manual',//手动排序chunks
      chunks: [...new function () {
        let chunks = [];
        let vendors = buildConf.vendors;
        for (let vendor in vendors) {
          if(!Array.isArray(vendors[vendor].owners)) {
            throw new Error(`vendors ${vendor} owners属性必须是数组`);
          }
          for (let owner of vendors[vendor].owners) {
            if(owner === n) {
              chunks.push(vendor);
            }
          }
        }
        return chunks;
      }(), 'vendor', n]
    }));
  });
  return dev;
};

/**
 * 获取prod时html配置信息
 */
exports.getProdHtmlWebpackPlugins = function () {
  let prod = [];
  prod.push(new webpack.optimize.CommonsChunkPlugin('vendor'));
  entryNames.forEach(n => {
    prod.push(new HtmlWebpackPlugin({
      filename: `${buildConf.assetsSubDirectory}/${buildConf.htmlsSubDirectory}/${n}/${htmlName}.html`,//该路径影响访问路径和打包路径
      template: path.join(__dirname, `../../src/${entriesBasePath}/${n}/${htmlName}.html`),
      inject: true,
      chunksSortMode: 'manual',//手动排序chunks
      chunks: [...new function () {
        let chunks = [];
        let vendors = buildConf.vendors;
        for (let vendor in vendors) {
          if(!Array.isArray(vendors[vendor].owners)) {
            throw new Error(`vendors ${vendor} owners属性必须是数组`);
          }
          for (let owner of vendors[vendor].owners) {
            if(owner === n) {
              chunks.push(vendor);
            }
          }
        }
        return chunks;
      }(), 'vendor', n]
    }));
  });
  return prod;
};
