const RoleUserManagerUrl = {
  get: {
    roleUserListByUserId: `api-shiro-role-user/get/roleUserListByUserId/{userId}`,
    roleUserListByRoleId: `api-shiro-role-user/get/roleUserListByRoleId/{roleId}`
  },
  post: {
    grantRolesToUsers: `api-shiro-role-user/post/grantRolesToUsers`,
    unGrantRolesToUsers: `api-shiro-role-user/post/unGrantRolesToUsers`,
    grantRoleToUser: `api-shiro-role-user/post/grantRoleToUser`,
    unGrantRoleToUser: `api-shiro-role-user/post/unGrantRoleToUser`
  }
};

export default RoleUserManagerUrl;
