import { createRouter, createWebHistory } from 'vue-router'
import NotFound from '../views/error/NotFound.vue'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RankListView from '../views/ranklist/RankListView.vue'
import RecordView from '../views/record/RecordView.vue'
import UserBotView from '../views/user/mybot/UserBotView.vue'
import UserAccountLogin from '@/views/user/account/UserAccountLogin.vue'
import UserAccountRegister from '@/views/user/account/UserAccountRegister.vue'
import store from '../store/index'

const routes = [
  {
    path:"/",
    name:"home",
    redirect:"/pk/",
    meta:{
      requestAuth:true
    }
  },

  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
    meta:{
      requestAuth:true
    }
  },

  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RankListView,
    meta:{
      requestAuth:true
    }
  },

  {
    path:"/record/",
    name:"record_index",
    component:RecordView,
    meta:{
      requestAuth:true
    }
  },

  {
    path:"/user/bot/",
    name:"user_bot_index",
    component:UserBotView,
    meta:{
      requestAuth:true
    }
  },

  {
    path:"/user/account/login",
    name:"user_account_login",
    component:UserAccountLogin,
    meta:{
      requestAuth:false
    }
  },

  {
    path:"/user/account/register",
    name:"user_account_register",
    component:UserAccountRegister,
    meta:{
      requestAuth:false
    }
  },

  {
    path:"/404/",
    name:"NotFound",
    component:NotFound,
    meta:{
      requestAuth:false
    }
  },

  {
    path:"/:catchAll(.*)",
    redirect:"/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next)=>{
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name:"user_account_login"})
  }
  else{
    next();
  }
})

export default router
