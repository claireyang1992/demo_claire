import { asyncRouterMap, constantRouterMap } from '@/router'
import MenuApi from '@/modules/menu/api/menu-api'
import { changeToComponent } from '@/utils/replaceUtil'
// import Vue from 'vue'
/**
 * 通过meta.role判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRouterMap
 * @param roles
 */
function filterAsyncRouter(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRouter(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

// function DynamicRoutes() {
//   return new Promise((resolve, reject) => {
//     SysRoleApi
//       .queryUserRole(id)
//       .then(response => {
//         const data = response.data
//         if (data.code === 200) {
//           const data = response.data.data
//           this.PermForm.checkedPart = data
//         } else {
//           reject(data.message)
//         }
//       })
//       .catch(error => {
//         reject(error)
//       })
//   })
// }

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const { roles } = data
        let accessedRouters
        // 拼接路由
        MenuApi.getRouters().then(response => {
          const routerData = response.data
          if (routerData.code === 200) {
            const changeData = changeToComponent(routerData.data)
            if (roles.includes('admin')) {
              accessedRouters = asyncRouterMap.concat(changeData)
            } else {
              accessedRouters = filterAsyncRouter(asyncRouterMap.concat(changeData), roles)
            }
            commit('SET_ROUTERS', accessedRouters)
            resolve()
          }
        })
      })
    }

    // GenerateDynamicRoutes({ commit }, data) {
    //   return new Promise(resolve => {
    //     const { roles } = data
    //     let accessedRouters
    //     if (roles.includes('admin')) {
    //       accessedRouters = asyncRouterMap
    //     } else {
    //       accessedRouters = filterAsyncRouter(asyncRouterMap, roles)
    //     }
    //     commit('SET_ROUTERS', accessedRouters)
    //     resolve()
    //   })
    // }
  }
}

export default permission
