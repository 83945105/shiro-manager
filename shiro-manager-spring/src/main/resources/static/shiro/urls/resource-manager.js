const ResourceManagerUrl = {
  post: {
    newResource: `api-shiro-resource/post/newResource`
  },
  put: {
    resourceStatus: `api-shiro-resource/put/resourceStatus/{id}`,
    resource: `api-shiro-resource/put/resource/{id}`
  },
  get: {
    resourceList: `api-shiro-resource/get/resourceList`,
    resourcePageList: `api-shiro-resource/get/resourcePageList`,
    rootResourceList: `api-shiro-resource/get/rootResourceList`,
    rootResourcePageList: `api-shiro-resource/get/rootResourcePageList`,
    childResourceList: `api-shiro-resource/get/childResourceList`,
    locationResourceList: `api-shiro-resource/get/locationResourceList`
  },
  delete: {
    resourceList: `api-shiro-resource/delete/resourceList`
  }
};

export default ResourceManagerUrl;
