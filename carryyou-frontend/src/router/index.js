import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/Login'
import Register from '@/views/Register'
import Layout from '@/components/Layout'
import ImageFile from '@/views/ImageFile'
import User from '@/views/User'
import MyInfo from '@/views/MyInfo.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '',
      component: Layout,
      children: [
        {
          path: '/imageFile',
          name: 'ImageFile',
          component: ImageFile
        }, {
          path: '/user',
          name: 'User',
          component: User
        }, {
          path: '/myInfo',
          name: 'MyInfo',
          component: MyInfo
        }
      ]
    }, {
      path: '/login',
      name: 'Login',
      component: Login
    }, {
      path: '/register',
      name: 'Register',
      component: Register
    }
  ]
})
