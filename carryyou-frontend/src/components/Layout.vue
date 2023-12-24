<template>
  <div>
    <el-container>
      <el-header class="header">
        <HeaderBar></HeaderBar>
      </el-header>
      <el-main class="main">
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import HeaderBar from '@/components/HeaderBar.vue'

export default {
  name: 'Layout',
  components: { HeaderBar },
  data() {
    return {}
  }
}
</script>

<style scoped>
.header {
  padding: 0;
}
</style>
