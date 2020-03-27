import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
// import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

var getRouter // 用来获取后台拿到的路由

NProgress.configure({ showSpinner: false }) // NProgress Configuration
// const whiteList = ['/login'] // no redirect whitelist
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)
  const isSearch = store.getters.isSearch
  if (!getRouter) { // 不加这个判断，路由会陷入死循环
    if (!isSearch) {
      // 没有搜索需要打开请求获取
      try {
        await store.dispatch('user/getMediaInfos')
      } catch (e) {
        Message.error(e)
        NProgress.done()
      }
    }
    getRouter = store.getters.menu// 后台拿到路由
    routerGo(to, next)// 执行路由跳转方法
  } else {
    next()
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})

function routerGo(to, next) {
  getRouter = filterAsyncRouter(getRouter) // 过滤路由
  router.addRoutes(getRouter) // 动态添加路由
  global.antRouter = getRouter // 将路由数据传递给全局变量，做侧边栏菜单渲染工作
  next({ ...to, replace: true })
}
const _import = require('./router/_import_' + process.env.NODE_ENV)// 获取组件的方法
import Layout from '@/layout' // Layout 是架构组件，不在后台返回，在文件里单独引入

function filterAsyncRouter(asyncRouterMap) { // 遍历后台传来的路由字符串，转换为组件对象
  const accessedRouters = asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.component === 'Layout') { // Layout组件特殊处理
        route.component = Layout
      } else {
        route.component = _import(route.component)
      }
    }
    if (route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
  return accessedRouters
}
