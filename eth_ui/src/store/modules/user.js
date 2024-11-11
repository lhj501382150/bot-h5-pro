import { getStrorage,setStrorage,clearStrorage } from "@/utils/storage"
export default {
    state: {
        name: "",  // 用户名称
        address:"",//
        isLogin:false,  // 是否登录
    },
    getters: {
      isLogin(state){// 对应着上面state
            return getStrorage('login-type')
      },
      account(state){// 对应着上面state
            return getStrorage('login-account')
      },
      address(state){// 对应着上面state
        return getStrorage('login-addr')
      }
    },
    mutations: {
        login(state, name){  // 登录
            state.name = name
            state.isLogin = true
            setStrorage('login-account',name);
            setStrorage('login-type',true);
        },
        refresh(state, name){  // 登录
          state.isLogin = true
          setStrorage('login-type',true);
          state.name = getStrorage('login-account')
          state.address = getStrorage('login-addr')

      },
        setAddr(state,addr){  // 登录
          state.address = addr
          setStrorage('login-addr',addr);
      },
        loginout(state,name){// 退出
          state.name = ''
          state.address = ''
          state.isLogin = false
          clearStrorage()
        }
    },
    actions: {
    }
}
