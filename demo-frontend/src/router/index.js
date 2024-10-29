import {createRouter, createWebHistory} from 'vue-router'
import {unauthorized} from "@/net";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL), routes: [{
        path: '/',
        name: 'welcome',
        component: () => import('@/views/WelcomeView.vue'),
        children: [{
            path: '',
            name: 'login',
            component: () => import('@/components/welcome/LoginPage.vue')
        }, {
            path: '/welcome/register',
            name: 'register',
            component: () => import('@/components/welcome/RegisterPage.vue')
        }, {
            path: '/welcome/forget',
            name: 'forget',
            component: () => import('@/components/welcome/ForgetPage.vue')
        }]
    }, {
        path: '/index',
        name: 'index',
        component: () => import('@/views/IndexView.vue'),
        children: [{
            path: '',
            name: 'index-home',
            component: () => import('../components/index/HomePage.vue')
        }, {
            path: '/index/company',
            name: 'index-company',
            component: () => import('../components/index/CompanyPage.vue')
        }, {
            path: '/index/tags',
            name: 'index-tags',
            component: () => import('../components/index/TagsPage.vue')
        }, {
            path: '/index/personal',
            name: 'index-personal',
            component: () => import('../components/index/PersonalPage.vue')
        }, {
            path: '/index/resume',
            name: 'index-resume',
            component: () => import('../components/index/ResumePage.vue')
        }, {
            path: '/index/meeting',
            name: 'index-meeting',
            component: () => import('../components/index/MeetingPage.vue')
        }, {
            path: '/index/recruitment',
            name: 'index-recruitment',
            component: () => import('../components/index/RecruitmentPage.vue')
        }, {
            path: '/index/center',
            name: 'index-center',
            component: () => import('../components/index/CenterPage.vue')
        }, {
            path: '/index/static',
            name: 'index-static',
            component: () => import('../components/index/StaticPage.vue')
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
