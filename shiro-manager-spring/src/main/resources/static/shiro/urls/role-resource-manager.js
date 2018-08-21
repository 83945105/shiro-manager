const RoleResourceManagerUrl = {
  post: {
    grantResourcesToRoles: `api-shiro-role-resource/post/grantResourcesToRoles`,
    unGrantResourcesToRoles: `api-shiro-role-resource/post/unGrantResourcesToRoles`,
    grantResourceToRole: `api-shiro-role-resource/post/grantResourceToRole`,
    unGrantResourceToRole: `api-shiro-role-resource/post/unGrantResourceToRole`,
    grantResourcesInNodeToRole: `api-shiro-role-resource/post/grantResourcesInNodeToRole`,
    unGrantResourcesInNodeToRole: `api-shiro-role-resource/post/unGrantResourcesInNodeToRole`,
    grantAllChildResourcesInNodeToRole: `api-shiro-role-resource/post/grantAllChildResourcesInNodeToRole`,
    unGrantAllChildResourcesInNodeToRole: `api-shiro-role-resource/post/unGrantAllChildResourcesInNodeToRole`
  },
  get: {
    roleResourceListByRoleId: `api-shiro-role-resource/get/roleResourceListByRoleId`,
    roleResourceListByResourceId: `api-shiro-role-resource/get/roleResourceListByResourceId`
  }
};

export default RoleResourceManagerUrl;
