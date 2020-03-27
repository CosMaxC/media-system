import { logout } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'
import { getMediaInfos } from '@/api/user'
const state = {
  token: getToken(),
  name: '',
  avatar: '',
  menu: [],
  medias: [],
  isSearch: false
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_MENU: (state, menu) => {
    state.menu = menu
  },
  SET_MEDIAS: (state, medias) => {
    state.medias = medias
  },
  SET_ISSEARCH: (state, isSearch) => {
    state.isSearch = isSearch
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    // const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '2333333333333')
      setToken('2333333333333')
      resolve()
      // login({ username: username.trim(), password: password }).then(response => {
      //   const { data } = response
      //   commit('SET_TOKEN', data.token)
      //   setToken(data.token)
      //   resolve()
      // }).catch(error => {
      //   reject(error)
      // })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_NAME', 'aaaa')
      // commit('SET_AVATAR', avatar)
      // resolve(data)
      // getInfo(state.token).then(response => {
      //   const { data } = response
      //
      //   if (!data) {
      //     reject('Verification failed, please Login again.')
      //   }
      //
      //   const { name, avatar } = data
      //
      //   commit('SET_NAME', name)
      //   commit('SET_AVATAR', avatar)
      //   resolve(data)
      // }).catch(error => {
      //   reject(error)
      // })
    })
  },

  // get media info
  getMediaInfos({ commit }) {
    return new Promise((resolve, reject) => {
      getMediaInfos().then(response => {
        const { data } = response
        if (!data) {
          reject('Verification failed, please Login again.')
        }
        const children = []
        data.forEach((d) => {
          const { id, name, type } = d
          const child = {
            path: 'media/' + id,
            component: 'medias/media',
            name: 'media_' + id,
            params: { id: id },
            meta: { id: id, type: type, title: name, icon: 'video' }
          }
          children.push(child)
        })
        const menu = [
          {
            path: '/',
            component: 'Layout',
            redirect: '/dashboard',
            children: [{
              path: 'dashboard',
              name: 'Dashboard',
              component: 'dashboard/index',
              meta: { title: '首页', icon: 'home' }
            }]
          },
          {
            path: '/medias',
            alwaysShow: true,
            component: 'Layout',
            meta: { title: '我的媒体库', icon: 'media' },
            children: children
          },
          // 404 page must be placed at the end !!!
          { path: '*', redirect: '/404', hidden: true }]
        commit('SET_MEDIAS', data)
        commit('SET_MENU', menu)
        commit('SET_ISSEARCH', true)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_SET_ISSEARCH', false)
        removeToken()
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // set isSearch
  resetIsSearch({ commit }) {
    return new Promise(resolve => {
      // commit('SET_ISSEARCH', false)
      // resolve()
      console.log('233333')
      getMediaInfos()
    })
  },
  // set isSearch
  confirmIsSearch({ commit }) {
    return new Promise(resolve => {
      getMediaInfos()
      // commit('SET_ISSEARCH', true)
      // resolve()
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

