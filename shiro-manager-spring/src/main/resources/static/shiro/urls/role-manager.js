const RoleManagerUrl = {
  get: {
    roleList: `api-shiro-role/get/roleList`,
    rolePageList: `api-shiro-role/get/rolePageList`,
    rolesForResource: `api-shiro-role/get/rolesForResource`
  },
  post: {
    newRole: `api-shiro-role/post/newRole`,
    validateRole: `api-shiro-role/post/validateRole`
  },
  put: {
    roleStatus: `api-shiro-role/put/roleStatus/{id}`,
    role: `api-shiro-role/put/role/{id}`
  },
  delete: {
    roleList: `api-shiro-role/delete/roleList`
  }
};

export default RoleManagerUrl;
