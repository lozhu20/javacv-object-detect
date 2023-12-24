<template>
  <div>
    <el-form ref="form" :model="loginForm" label-width="80px" label-position="top">
      <el-form-item label="用户id">
        <el-input v-model="loginForm.userId"></el-input>
      </el-form-item>
      <el-form-item label="登陆密码">
        <el-input v-model="loginForm.password"></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="login">登陆</el-button>
        <el-button @click="resetLoginForm">重置</el-button>
      </el-form-item>
      <span>没有账号？先 <el-link @click="toRegister">注册</el-link></span>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        userId: '',
        password: ''
      }
    }
  },
  created() {
    sessionStorage.removeItem('token')
    sessionStorage.removeItem('userId')
    sessionStorage.removeItem('roleId')
  },
  methods: {
    login() {
      if (!this.loginForm.userId) {
        this.$message.warning('uid不能为空')
        return false
      }
      if (!this.loginForm.password) {
        this.$message.warning('密码不能为空')
        return false
      }
      this.axios.post('/api/user/login', this.loginForm).then((res) => {
        let token = res.data.token
        if (token) {
          sessionStorage.setItem('token', token)
          sessionStorage.setItem('userId', res.data.userId)
          sessionStorage.setItem('roleId', res.data.roleId)
          setTimeout(() => {
            this.$message.warning('登陆信息已过期，请重新登陆')
            sessionStorage.removeItem('token')
            sessionStorage.removeItem('userId')
            sessionStorage.removeItem('roleId')
            this.$router.push('/login')
          }, 15 * 60 * 1000)
          this.$router.push('/imageFile')
        }
      })
    },
    resetLoginForm() {
      this.loginForm.userId = ''
      this.loginForm.password = ''
    },
    toRegister() {
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>

</style>
