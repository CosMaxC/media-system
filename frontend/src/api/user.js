import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function updateMediaInfo(data) {
  return request({
    url: '/media/update',
    method: 'post',
    data
  })
}

export function getMediaInfo(data) {
  return request({
    url: '/media/' + data,
    method: 'get'
  })
}

export function deleteMediaInfo(data) {
  return request({
    url: '/media/delete/' + data,
    method: 'post'
  })
}

export function insertMediaInfo(data) {
  return request({
    url: '/media/insert',
    method: 'post',
    data
  })
}

export function refreshMediaInfo(data) {
  return request({
    url: '/media/infos/refresh/' + data,
    method: 'post'
  })
}

export function refreshDoubanInfo(data) {
  return request({
    url: '/media/douban/refresh/' + data,
    method: 'post'
  })
}

export function getMediaInfos() {
  return request({
    url: '/media/infos',
    method: 'get'
  })
}
export function getMediaInfoById(id) {
  return request({
    url: '/media/infos/' + id,
    method: 'get'

  })
}
export function getMovieInfoById(id) {
  return request({
    url: '/media/movies/' + id,
    method: 'get'

  })
}
export function getSeriesInfoById(id) {
  return request({
    url: '/media/series/' + id,
    method: 'get'

  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
