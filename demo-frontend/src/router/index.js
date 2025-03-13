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
            path: '/index/personal',
            name: 'index-personal',
            component: () => import('../components/index/PersonalPage.vue')
        }, {
            path: '/index/announce',
            name: 'index-announce',
            component: () => import('../components/index/AnnouncePage.vue')
        }, {
            path: '/index/complaint',
            name: 'index-complaint',
            component: () => import('../components/index/ComplaintPage.vue')
        }, {
            path: '/index/repair',
            name: 'index-repair',
            component: () => import('../components/index/RepairPage.vue')
        }, {
            path: '/index/payment',
            name: 'index-payment',
            component: () => import('../components/index/PaymentPage.vue')
        }]
    }, {
        path: '/manager',
        name: 'manager',
        component: () => import('@/views/ManagerView.vue'),
        children: [{
            path: '',
            name: 'manager-user',
            component: () => import('../components/manager/UserManagePage.vue')
        }, {
            path: '/manager/property',
            name: 'manager-property',
            component: () => import('../components/manager/PropertyManagePage.vue')
        }, {
            path: '/manager/announce',
            name: 'manager-announce',
            component: () => import('../components/manager/AnnounceManagePage.vue')
        }, {
            path: '/manager/complaint',
            name: 'manager-complaint',
            component: () => import('../components/manager/ComplaintManagePage.vue')
        }, {
            path: '/manager/repair',
            name: 'manager-repair',
            component: () => import('../components/manager/RepairManagePage.vue')
        }, {
            path: '/manager/payment',
            name: 'manager-payment',
            component: () => import('../components/manager/PaymentManagePage.vue')
        }]
    }, {
        path: '/payment',
        name: 'payment',
        component: () => import('@/views/PaymentView.vue')
    }]
})

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized()
    if(to.name.startsWith('welcome') && !isUnauthorized) {
        next('/index')
    } else if(to.fullPath.startsWith('/index') && isUnauthorized) {
        next('/')
    } else if (to.fullPath.startsWith('/manager') && isUnauthorized) {
        next('/')
    } else {
        next()
    }
})

export default router
