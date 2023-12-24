<template>
  <div class="single-page">
    <el-form ref="form" :model="userForm" label-width="80px" label-position="top">
      <el-form-item label="用户id">
        <el-input v-model="userForm.userId"></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="userForm.username"></el-input>
      </el-form-item>
      <el-form-item label="登陆密码">
        <el-input v-model="userForm.password"></el-input>
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input v-model="userForm.confirmPassword"></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="register" icon="el-icon-check">注册</el-button>
        <el-button @click="resetUserForm" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
      <span>已有账号？直接 <el-link @click="toLogin">登陆</el-link></span>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    return {
      userForm: {
        userId: '',
        username: '',
        password: '',
        confirmPassword: ''
      }
    }
  },
  methods: {
    register() {
      if (!this.userForm.username) {
        this.$message.warning('用户名不能为空')
        return false
      }
      if (!this.userForm.password) {
        this.$message.warning('登陆密码不能为空')
        return false
      }
      if (this.userForm.password !== this.userForm.confirmPassword) {
        this.$message.warning('两次密码不一致')
        return false
      }
      this.axios.post('/api/user/register', this.userForm).then((res) => {
        this.$message.success('注册成功，跳转登陆')
        setTimeout(() => {
          this.$router.push('/login')
        }, 1500)
      })
    },
    resetUserForm() {
      this.userForm.userId = ''
      this.userForm.username = ''
      this.userForm.password = ''
      this.userForm.confirmPassword = ''
    },
    toLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>

</style>
