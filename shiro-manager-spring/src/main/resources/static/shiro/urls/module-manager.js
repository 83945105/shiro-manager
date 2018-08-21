const ModuleManagerUrl = {
  get: {
    module: `api-shiro-module/get/module/{id}`,
    moduleList: `api-shiro-module/get/moduleList`
  },
  post: {
    validateServiceId: `api-shiro-module/post/validateServiceId`,
    newModule: `api-shiro-module/post/module`
  },
  put: {
    editModule: `api-shiro-module/put/module/{id}`
  },
  delete: {
    deleteModule: `api-shiro-module/delete/module/{id}`
  }
};

export default ModuleManagerUrl;
