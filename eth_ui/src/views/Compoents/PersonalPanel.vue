<template>
  <div class="personal-panel">
    <!-- <div class="personal-desc" >
        <div class="name-role">
          <span class="sender">{{ account }} </span>
        </div>
        <div class="registe-info">
          <span class="registe-info">
            {{address}}
          </span>
        </div>
    </div> -->
    <div class="other-operation">
        <!-- <div class="other-operation-item" @click="myWallet">
          <li class="el-icon-coin"></li>
          我的钱包
        </div> -->
        <!-- <div class="other-operation-item" @click="openupdatePasswordDialog">
          <li class="fa fa-key"></li>
          修改密码
        </div> -->
        <div class="other-operation-item" @click="logout">
          <li class="fa fa-sign-out"></li>
          退出登陆
        </div>

    </div>
    <!--修改密码界面-->
    <el-dialog title="修改密码" width="40%" :visible.sync="updatePwdDialogVisible" :close-on-click-modal="false" :modal="false">
      <el-form :model="updatePwdDataForm" label-width="100px" :rules="updatePwdDataFormRules" ref="updatePwdDataForm" :size="size">
        <el-form-item label="原密码" prop="password">
          <el-input v-model="updatePwdDataForm.password" type="password" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="updatePwdDataForm.newPassword" type="password" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="comfirmPassword">
          <el-input v-model="updatePwdDataForm.comfirmPassword" type="password" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="updatePwdDialogVisible = false">确定</el-button>
        <el-button :size="size" type="primary" @click.native="updatePassword" :loading="updatePwdLoading">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { format } from "@/utils/datetime"
export default {
  name: 'PersonalPanel',
  components:{

  },
  data() {
    return {
      account: 0,
      address: 0,
      size: 'small',
      updatePwdDialogVisible: false,
      updatePwdLoading: false,
      updatePwdDataForm: {
				password: '',
				newPassword: '',
				comfirmPassword: ''
			},
      updatePwdDataFormRules: {
				password: [
					{ required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
					{ required: true, message: '请输入新密码', trigger: 'blur' }
        ],
        comfirmPassword: [
					{ required: true, message: '请确认密码', trigger: 'blur' }
				]
			},
    }
  },
  methods: {
    loadinfo(){
      this.account = sessionStorage.getItem('login-account')
      this.address = sessionStorage.getItem('login-addr')
    },
    // 打开修改密码对话框
		openupdatePasswordDialog: function () {
			this.updatePwdDialogVisible = true
		},
    // 修改密码
		updatePassword: function () {
			this.$refs.updatePwdDataForm.validate((valid) => {
				if (valid) {
          if(this.updatePwdDataForm.newPassword != this.updatePwdDataForm.comfirmPassword) {
            this.$message({message: '新密码与确认新密码不一致', type: 'error'})
            return
          }
				this.updatePwdLoading = true
				let params = {password:this.updatePwdDataForm.password, newPassword:this.updatePwdDataForm.newPassword}
				this.$api.user.updatePassword(params).then((res) => {
					this.updatePwdLoading = false
					if(res.code == 200) {
						this.$message({ message: '操作成功', type: 'success' })
				    this.$refs['updatePwdDataForm'].resetFields()
				    this.logoutApi()
				   this.updatePwdDialogVisible = false
					} else {
				    this.$message({message: '操作失败, ' + res.msg, type: 'error'})
					}
				})
				}
			})
		},
    // 退出登录
    logout() {
      this.$confirm("确认退出吗?", "提示", {
        type: "warning"
      })
      .then(() => {
        this.logoutApi()
      })
      .catch(() => {})
    },
    // 我的钱包
    myWallet() {
      this.$router.push('/wallet')
    },
    logoutApi() {
      this.$store.commit('loginout','')
      this.$router.push("/")
    }


  },
  mounted(){
    this.loadinfo()
  }
}
</script>

<style scoped>
.personal-panel {
  font-size: 14px;
  width: 280px;
  text-align: center;
  border-color: rgba(180, 190, 190, 0.2);
  border-width: 1px;
  border-style: solid;
  background: rgba(182, 172, 172, 0.1);
  margin: -14px;
}
.personal-desc {
  padding: 15px;
  color: #000000;
  /* background-image: linear-gradient(to bottom right, yellow, red); */
}
.avatar {
  width: 80px;
  height: 80px;
  border-radius: 90px;
}
.name-role {
  font-size: 16px;
  padding: 5px;
}
.personal-relation {
  font-size: 16px;
  padding: 12px;
  margin-right: 1px;
  background: rgba(200, 209, 204, 0.3);
}
.relation-item {
  padding: 12px;
}
.relation-item:hover {
  cursor: pointer;
  color: rgb(19, 138, 156);
}
.main-operation {
  padding: 8px;
  margin-right: 1px;
  /* background: rgba(175, 182, 179, 0.3); */
  border-color: rgba(201, 206, 206, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.main-operation-item {
  margin: 15px;
}
.other-operation {
  padding: 15px;
  margin-right: 1px;
  text-align: left;
  border-color: rgba(180, 190, 190, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.other-operation-item {
  padding: 12px;
}
.other-operation-item:hover {
  cursor: pointer;
  background: #9e94941e;
  color: rgb(19, 138, 156);
}
.personal-footer {
  margin-right: 1px;
  font-size: 14px;
  text-align: center;
  padding-top: 10px;
  padding-bottom: 10px;
  border-color: rgba(180, 190, 190, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.personal-footer:hover {
  cursor: pointer;
  color: rgb(19, 138, 156);
  background: #b1a6a61e;
}
</style>
