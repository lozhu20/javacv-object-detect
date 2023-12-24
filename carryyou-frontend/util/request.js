import axios, {request} from 'axios'
import {Message} from 'element-ui'

let token = sessionStorage.getItem('token')
let userId = sessionStorage.getItem('userId')

const myAxios = axios.create({
  baseURL: '',
  timeout: 15 * 1000
})

myAxios.interceptors.request.use(
  config => {
    config.headers.token = token
    config.headers.userId = userId
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

myAxios.interceptors.response.use(function (response) {
  let data = response.data
  if (data.code === 0) {
    return data
  } else if (data.code === -1) {
    Message.error(data.message)
    return Promise.reject(data.message)
  }
  return response
}, function (error) {
  return Promise.reject(error)
})

export default myAxios
