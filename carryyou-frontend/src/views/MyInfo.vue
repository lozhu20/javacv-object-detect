<template>
  <div>
    <el-form ref="form" :model="myInfo" label-width="80px" label-position="top">
      <el-form-item label="用户id">
        <el-input v-model="myInfo.userId" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="myInfo.username"></el-input>
      </el-form-item>
      <el-form-item label="登陆密码">
        <el-input v-model="myInfo.password"></el-input>
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input v-model="myInfo.confirmPassword"></el-input>
      </el-form-item>
      <el-form-item label="用户状态">
        <el-select v-model="myInfo.status" style="width: 100%" disabled>
          <el-option v-for="status in statusList" :key="status.value" :value="status.value"
                     :label="status.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户角色">
        <el-select v-model="myInfo.roleId" style="width: 100%" disabled>
          <el-option v-for="role in roleList" :key="role.value" :value="role.value" :label="role.label"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-input v-model="myInfo.createdTime" disabled></el-input>
      </el-form-item>
      <el-form-item label="最后更新">
        <el-input v-model="myInfo.updatedTime" disabled></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="updateUser">更新信息</el-button>
        <el-button type="primary" @click="suspendMyAccount">注销账号</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'MyInfo',
  data() {
    return {
      myInfo: {
        userId: '',
        username: '',
        password: '',
        confirmPassword: '',
        status: '',
        roleId: '',
        createdTime: '',
        updatedTime: ''
      },
      statusList: [
        {
          label: '正常',
          value: '01'
        },
        {
          label: '注销',
          value: '98'
        },
        {
          label: '删除',
          value: '99'
        }
      ],
      roleList: [
        {
          label: '管理员',
          value: 'ROLE_ADMIN'
        },
        {
          label: '普通用户',
          value: 'ROLE_USER'
        }
      ],
    }
  },
  created() {
    this.queryMyInfo()
  },
  methods: {
    queryMyInfo() {
      let myUserId = sessionStorage.getItem('userId')
      if (!myUserId) {
        this.$message.warning('登陆信息已失效')
        this.$router.push('/login')
        return false
      }
      this.axios.get('/api/user/' + myUserId).then((res) => {
        let data = res.data
        this.$set(this, 'myInfo', data)
        this.$set(this.myInfo, 'confirmPassword', this.myInfo.password)
      })
    },
    suspendMyAccount() {
      this.$confirm('确认注销？', {
        distinguishCancelAndClose: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.axios.put('/api/user/' + this.myInfo.userId + '/99').then((res) => {
          this.$message.success('注销成功')
          sessionStorage.removeItem('token')
          sessionStorage.removeItem('userId')
          this.$router.push('/login')
        })
      }).catch((action) => {
        this.$message.warning('已取消')
      })
    },
    updateUser() {
      if (!this.myInfo.username) {
        this.$message.warning('用户名不能为空')
        return false
      }
      if (!this.myInfo.password) {
        this.$message.warning('登陆密码不能为空')
        return false
      }
      if (this.myInfo.password !== this.myInfo.confirmPassword) {
        this.$message.warning('两次密码不一致')
        return false
      }
      if (!this.myInfo.status) {
        this.$message.warning('账户状态不能为空')
        return false
      }
      if (!this.myInfo.roleId) {
        this.$message.warning('用户角色不能为空')
        return false
      }
      this.axios.put('/api/user', this.myInfo).then((res) => {
        this.$message.success('更新成功')
        this.queryMyInfo()
      })
    }
  }
}
</script>

<style scoped>

</style>
