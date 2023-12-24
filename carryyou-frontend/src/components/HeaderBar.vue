<template>
  <div>
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal"
             text-color="#555"
             :router="true">
      <el-menu-item id="homeMenu">Yolo 目标检测</el-menu-item>
      <template v-for="menu in menuList">
        <el-menu-item v-if="!menu.children" :index="menu.index">
          {{ menu.name }}
        </el-menu-item>
        <el-submenu v-else :index="menu.index">
          <template slot="title">{{ menu.name }}</template>
          <el-menu-item v-for="subMenu in menu.children" :key="subMenu.index" :index="subMenu.index">
            {{ subMenu.name }}
          </el-menu-item>
        </el-submenu>
      </template>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'HeaderBar',
  data() {
    return {
      activeIndex: 'imageFile',
      menuList: [
        {
          index: 'imageFile',
          name: '图片'
        }, {
          index: 'user',
          name: '用户'
        }, {
          index: 'myself',
          name: '我的',
          children: [
            {
              index: 'myInfo',
              name: '个人信息'
            }, {
              index: 'login',
              name: '退出登陆'
            }
          ]
        }, {
          index: 'bak',
          name: '备用',
          children: [
            {
              index: 'register',
              name: '注册'
            }, {
              index: 'login',
              name: '登陆'
            }
          ]
        }
      ]
    }
  }
}
</script>

<style scoped>
#homeMenu {
  margin-left: calc((100% - 1180px) / 2);
}
</style>
