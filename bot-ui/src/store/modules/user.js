export default {
    state: {
        perms: [],  // 用户权限标识集合
        curUser: 0,    //用户类型
        token:''
    },
    getters: {

    },
    mutations: {
        setPerms(state, perms){  // 用户权限标识集合
            state.perms = perms;
        },
        setCurUser(state, user){  // 用户类型
          state.curUser = user;
        },
        setToken(state, token){  // 用户类型
          state.token = token;
        }
    },
    actions: {
    }
}
