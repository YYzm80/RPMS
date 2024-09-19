import {createRouter, createWebHistory} from 'vue-router'
import {unauthorized} from "@/net";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL), routes: [{
        path: '/',
        name: 'welcome',
        component: () => import('@/views/WelcomeView.vue'),
        children: [{
            path: '',
            name: 'welcome-login',
            component: () => import('@/components/welcome/LoginPage.vue')
        }, {
            path: 'register',
            name: 'welcome-register',
            component: () => import('@/components/welcome/RegisterPage.vue')
        }, {
            path: 'forget',
            name: 'welcome-forget',
            component: () => import('@/components/welcome/ForgetPage.vue')
        }]
    }, {
        path: '/index',
        name: 'index',
        component: () => import('@/views/IndexView.vue'),
        children: [{
            path: 'home',
            name: 'index-home',
            component: () => import('@/components/index/homePage.vue')
        }]
    }]
})

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized()
    if(to.name.startsWith('welcome') && !isUnauthorized) {
        next('/index')
    } else if(to.fullPath.startsWith('/index') && isUnauthorized) {
        next('/')
    } else {
        next()
    }
})

export default router
