<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="用户id">
            <el-input v-model="searchForm.userId"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="queryUser" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <div style="text-align: right">
          <el-button type="primary" plain icon="el-icon-plus" @click="showAddUserDialog" :disabled="disable">新增</el-button>
          <el-button type="primary" plain icon="el-icon-edit" @click="showUpdateUserDialog" :disabled="disable">编辑</el-button>
          <el-button type="danger" plain icon="el-icon-delete" @click="delUser" :disabled="disable">删除</el-button>
        </div>
      </el-col>
    </el-row>
    <el-table :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="userId" label="用户id" width="160" show-overflow-tooltip></el-table-column>
      <el-table-column prop="username" label="用户名" show-overflow-tooltip></el-table-column>
      <el-table-column prop="password" label="登陆密码" width="150" show-overflow-tooltip></el-table-column>
      <el-table-column label="状态" width="130" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-select v-model="scope.row.status" disabled>
            <el-option v-for="status in statusList" :key="status.value" :value="status.value"
                       :label="status.label"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="角色" width="130" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-select v-model="scope.row.roleId" disabled>
            <el-option v-for="role in roleList" :key="role.value" :value="role.value" :label="role.label"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="createdTime" label="创建时间" show-overflow-tooltip></el-table-column>
      <el-table-column prop="updatedTime" label="最后更新" show-overflow-tooltip></el-table-column>
    </el-table>
    <el-pagination
      style="margin-top: 15px; text-align: center"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.currentPage"
      :page-sizes="[10, 25, 40, 100]"
      :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>

    <el-dialog title="新增用户" :visible.sync="addUserDialogVisible" width="40%"
               :before-close="beforeCloseAddUserDialog">
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
        <el-form-item label="用户角色">
          <el-select v-model="userForm.roleId" style="width: 100%">
            <el-option v-for="role in roleList" :key="role.value" :value="role.value" :label="role.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户状态">
          <el-select v-model="userForm.status" style="width: 100%">
            <el-option v-for="status in statusList" :key="status.value" :value="status.value"
                       :label="status.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" @click="register">确定</el-button>
          <el-button @click="resetUserForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="编辑用户" :visible.sync="updateUserDialogVisible" width="40%"
               :before-close="beforeCloseUpdateUserDialog">
      <el-form ref="form" :model="updateForm" label-width="80px" label-position="top">
        <el-form-item label="用户id">
          <el-input v-model="updateForm.userId" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="updateForm.username"></el-input>
        </el-form-item>
        <el-form-item label="登陆密码">
          <el-input v-model="updateForm.password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="updateForm.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item label="用户状态">
          <el-select v-model="updateForm.status" style="width: 100%">
            <el-option v-for="status in statusList" :key="status.value" :value="status.value"
                       :label="status.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户角色">
          <el-select v-model="updateForm.roleId" style="width: 100%">
            <el-option v-for="role in roleList" :key="role.value" :value="role.value" :label="role.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" @click="updateUser">确定</el-button>
          <el-button @click="resetUpdateForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {stat} from "copy-webpack-plugin/dist/utils/promisify";

export default {
  name: 'User',
  data() {
    return {
      userList: [],
      multipleSelection: [],
      disable: false,
      searchForm: {
        userId: ''
      },
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      addUserDialogVisible: false,
      userForm: {
        userId: '',
        username: '',
        password: '',
        confirmPassword: '',
        status: '',
        roleId: ''
      },
      updateForm: {
        userId: '',
        username: '',
        password: '',
        confirmPassword: '',
        status: '',
        roleId: ''
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
      updateUserDialogVisible: false
    }
  },
  created() {
    let roleId = sessionStorage.getItem('roleId')
    this.disable = roleId !== 'ROLE_ADMIN'

    this.queryUserList()
  },
  methods: {
    stat,
    queryUserList() {
      this.axios.get('/api/user/list?pageNum=' + this.page.currentPage + '&pageSize=' + this.page.pageSize).then((res) => {
        this.userList = res.data.list
        this.page.total = res.data.total
      })
    },
    queryUser() {
      if (!this.searchForm.userId) {
        this.queryUserList()
      } else {
        this.axios.get('/api/user/' + this.searchForm.userId).then((res) => {
          this.userList = [res.data]
          this.page.total = this.userList.length
        })
      }
    },
    handleSizeChange(val) {
      this.page.pageSize = val
      this.page.currentPage = 1
      this.queryUserList()
    },
    handleCurrentChange(val) {
      this.page.currentPage = val
      this.queryUserList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delUser() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择一个用户进行删除')
        return false
      }
      if (this.multipleSelection.length > 1) {
        this.$message.warning('仅支持每次删除一个用户')
        return false
      }
      this.axios.put('api/user/delete/' + this.multipleSelection[0].userId).then((res) => {
        this.$message.success('删除成功')
      })
    },
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
      this.axios.post('/api/user', this.userForm).then((res) => {
        this.$message.success('新增成功')
        this.beforeCloseAddUserDialog()
        this.queryUserList()
      })
    },
    beforeCloseAddUserDialog() {
      this.resetUserForm()
      this.addUserDialogVisible = false
    },
    resetUserForm() {
      this.userForm.userId = ''
      this.userForm.username = ''
      this.userForm.password = ''
      this.userForm.confirmPassword = ''
      this.userForm.status = ''
      this.userForm.roleId = ''
    },
    showAddUserDialog() {
      this.resetUserForm()
      this.addUserDialogVisible = true
    },
    beforeCloseUpdateUserDialog() {
      this.updateForm = {
        userId: '',
        username: '',
        password: '',
        confirmPassword: '',
        status: '',
        roleId: ''
      }
      this.updateUserDialogVisible = false
    },
    showUpdateUserDialog() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择一个用户')
        return false
      }
      if (this.multipleSelection.length > 1) {
        this.$message.warning('仅支持单次编辑一个用户')
        return false
      }

      this.updateForm = this.multipleSelection[0]
      this.updateForm.confirmPassword = this.updateForm.password
      this.updateUserDialogVisible = true
    },
    updateUser() {
      if (!this.updateForm.username) {
        this.$message.warning('用户名不能为空')
        return false
      }
      if (!this.updateForm.password) {
        this.$message.warning('登陆密码不能为空')
        return false
      }
      if (this.updateForm.password !== this.updateForm.confirmPassword) {
        this.$message.warning('两次密码不一致')
        return false
      }
      if (!this.updateForm.status) {
        this.$message.warning('账户状态不能为空')
        return false
      }
      if (!this.updateForm.roleId) {
        this.$message.warning('用户角色不能为空')
        return false
      }
      this.axios.put('/api/user', this.updateForm).then((res) => {
        this.$message.success('新增成功')
        this.beforeCloseUpdateUserDialog()
        this.queryUserList()
      })
    },
    resetUpdateForm() {
      this.updateForm.username = ''
      this.updateForm.password = ''
      this.updateForm.confirmPassword = ''
      this.updateForm.status = ''
      this.updateForm.roleId = ''
    }
  }
}
</script>

<style scoped>

</style>
