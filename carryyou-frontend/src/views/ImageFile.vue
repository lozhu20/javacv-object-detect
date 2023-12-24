<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="图片名称">
            <el-input v-model="searchForm.imageName"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="queryImage" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <div style="text-align: right">
          <el-button type="primary" plain icon="el-icon-upload2" @click="showUploadDialog">上传</el-button>
          <el-button type="danger" plain icon="el-icon-delete" @click="delImage" :disabled="disable">删除</el-button>
        </div>
      </el-col>
    </el-row>
    <el-table :data="imageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="imageName" label="图片名称" show-overflow-tooltip></el-table-column>
      <el-table-column prop="size" label="图片大小" width="120" show-overflow-tooltip></el-table-column>
      <el-table-column prop="savePath" label="保存路径" show-overflow-tooltip></el-table-column>
      <el-table-column label="识别类型" width="130" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-select v-model="scope.row.useAs" disabled>
            <el-option v-for="useAs in useAsList" :key="useAs.value" :value="useAs.value"
                       :label="useAs.label">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="createdBy" label="上传人" width="180" show-overflow-tooltip></el-table-column>
      <el-table-column prop="createdTime" label="上传时间" width="180" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" min-width="150">
        <template slot-scope="scope">
          <el-button type="primary" plain icon="el-icon-picture-outline" @click="downloadImage(scope.row.id, '1')">查看原图
          </el-button>
          <el-button type="primary" plain icon="el-icon-picture-outline" @click="downloadImage(scope.row.id, '2')"
                     :disabled="scope.row.detectSuccess !== 'Y'">
            查看识别结果
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="id" v-if="false"></el-table-column>
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

    <el-dialog title="上传图片" :visible.sync="uploadDialogVisible" width="30%" :before-close="clearFileList">
      <el-form :inline="true" :model="uploadForm" class="demo-form-inline">
        <el-form-item label="检测类型">
          <el-select v-model="uploadForm.useAs">
            <el-option v-for="useAs in useAsList" :key="useAs.value" :value="useAs.value"
                       :label="useAs.label">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-upload
        ref="upload"
        action="/api/image/upload"
        :data="{'useAs': uploadForm.useAs}"
        :headers="headers"
        :file-list="fileList"
        list-type="picture"
        :limit="1"
        :on-remove="handleRemove"
        :on-exceed="handleExceed"
        :before-upload="beforeUpload"
        :on-success="uploadSuccess"
        :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary" icon="el-icon-plus">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" icon="el-icon-upload2" @click="submitUpload">
          上传
        </el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/jpeg文件，且不超过2M</div>
      </el-upload>
    </el-dialog>

    <iframe id="image" style="display: none"></iframe>
  </div>
</template>

<script>
export default {
  name: 'ImageFile',
  data() {
    return {
      headers: {
        token: '',
        userId: '',
      },
      disable: false,
      imageList: [],
      useAsList: [
        {
          value: '01',
          label: '品种检测'
        },
        {
          value: '02',
          label: '成熟度检测'
        }
      ],
      multipleSelection: [],
      searchForm: {
        imageName: ''
      },
      page: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      uploadForm: {
        useAs: '',
      },
      uploadDialogVisible: false,
      fileList: [],
      imageDialogVisible: false,
      imageSrc: '/Users/lozhu/Documents/projects/carryyou/carryyou-backend/images/2023/12/02/75f4a8d8-7dde-4e34-8cd5-4061be12cfcf.jpg'
    }
  },
  created() {

    let token = sessionStorage.getItem('token')
    let userId = sessionStorage.getItem('userId')
    this.headers.token = token
    this.headers.userId = userId

    let roleId = sessionStorage.getItem('roleId')
    this.disable = roleId !== 'ROLE_ADMIN'

    this.queryImageList()
  },
  methods: {
    queryImage() {
      this.axios.get('api/image?imageName=' + this.searchForm.imageName + '&pageNum='
        + this.page.currentPage + '&pageSize=' + this.page.pageSize).then((res) => {
        let data = res.data
        this.imageList = data.list
        this.page.total = data.total
      })
    },
    queryImageList() {
      this.axios.get('api/image/list?pageNum=' + this.page.currentPage + '&pageSize=' + this.page.pageSize).then((res) => {
        let data = res.data
        this.imageList = data.list
        this.page.total = data.total
      })
    },
    handleSizeChange(val) {
      this.page.pageSize = val
      this.page.currentPage = 1
      this.queryImageList()
    },
    handleCurrentChange(val) {
      this.page.currentPage = val
      this.queryImageList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    clearFileList() {
      this.fileList = []
      this.uploadDialogVisible = false
    },
    showUploadDialog() {
      this.uploadDialogVisible = true
    },
    handleRemove(file, fileList) {
      this.fileList = []
    },
    handleExceed(files, fileList) {
      this.$message.warning('当前限制选择 1 个文件')
    },
    beforeUpload(file) {
      if (!this.uploadForm.useAs) {
        this.$message.warning('检测类型不能为空')
        return false
      }
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/jpg'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB')
      }
      return isJPG && isLt2M
    },
    submitUpload() {
      this.$refs.upload.submit()
    },
    uploadSuccess() {
      this.$message.success('上传成功，正在检测中..')
      this.clearFileList()
      this.queryImageList()
    },
    downloadImage(id, type) {
      document.getElementById('image').src = '/api/image/download/' + id + '/' + type
    },
    delImage() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择一张图片进行删除')
        return false
      }

      if (this.multipleSelection.length > 1) {
        this.$message.warning('仅支持一次删除一个图片')
        return false
      }

      let imageId = this.multipleSelection[0].id
      this.axios.delete('api/image/delete/' + imageId).then((res) => {
        this.$message.success('图片删除成功')
        this.queryImageList()
      })
    }
  }
}
</script>

<style scoped>

</style>
