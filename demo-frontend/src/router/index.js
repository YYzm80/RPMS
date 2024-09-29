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
            path: '',
            name: 'index-game',
            component: () => import('../components/index/GamePage.vue')
        }, {
            path: '/index/project',
            name: 'index-project',
            component: () => import('../components/index/ProjectPage.vue')
        }, {
            path: '/index/manager',
            name: 'index-manager',
            component: () => import('../components/index/ManagerPage.vue')
        }, {
            path: '/index/academy',
            name: 'index-academy',
            component: () => import('../components/index/AcademyPage.vue')
        }, {
            path: '/index/class',
            name: 'index-class',
            component: () => import('../components/index/ClassPage.vue')
        }, {
            path: '/index/registration',
            name: 'index-registration',
            component: () => import('../components/index/RegistrationPage.vue')
        }, {
            path: '/index/myRegistration',
            name: 'index-myRegistration',
            component: () => import('../components/index/MyRegistrationPage.vue')
        }, {
            path: '/index/arrange',
            name: 'index-arrange',
            component: () => import('../components/index/ArrangementPage.vue')
        }, {
            path: '/index/myArrange',
            name: 'index-myArrange',
            component: () => import('../components/index/MyArrangementPage.vue')
        }, {
            path: '/index/addScore',
            name: 'index-addScore',
            component: () => import('../components/index/AddScorePage.vue')
        }, {
            path: '/index/score',
            name: 'index-score',
            component: () => import('../components/index/ScorePage.vue')
        }, {
            path: '/index/myScore',
            name: 'index-myScore',
            component: () => import('../components/index/MyScorePage.vue')
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
