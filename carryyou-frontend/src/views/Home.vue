<template>
  <div>
    <el-container>
      <el-header style="padding: 0">
        <HeaderBar></HeaderBar>
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import HeaderBar from '@/components/HeaderBar.vue'

export default {
  name: 'Home',
  components: { HeaderBar },
  data() {
    return {}
  }
}
</script>

<style scoped>

</style>
