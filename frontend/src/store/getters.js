const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  menu: state => state.user.menu,
  medias: state => state.user.medias,
  isSearch: state => state.user.isSearch
}
export default getters
