import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import myAxios from '../util/request'
import '@/assets/base.css'

Vue.config.productionTip = false

Vue.use(ElementUI)

Vue.prototype.axios = myAxios

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
