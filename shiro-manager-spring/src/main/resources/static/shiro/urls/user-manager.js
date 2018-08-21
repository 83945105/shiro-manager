const UserManagerUrl = {
  get: {
    userColumnList: `api-shiro-user/get/userColumnList`,
    userPageList: `api-shiro-user/get/userPageList`
  },
  put: {
    userStatus: `api-shiro-user/put/userStatus/{id}`
  }
};

export default UserManagerUrl;
