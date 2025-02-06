import { createRouter, createWebHistory } from 'vue-router'
import NotFound from '../views/error/NotFound.vue'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RankListView from '../views/ranklist/RankListView.vue'
import RecordView from '../views/record/RecordView.vue'
import UserBotView from '../views/user/mybot/UserBotView.vue'

const routes = [
  {
    path:"/",
    name:"home",
    redirect:"/pk/"
  },

  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
  },

  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RankListView,
  },

  {
    path:"/record/",
    name:"record_index",
    component:RecordView,
  },

  {
    path:"/user/bot/",
    name:"user_bot_index",
    component:UserBotView,
  },

  {
    path:"/404/",
    name:"NotFound",
    component:NotFound,
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

export default router
