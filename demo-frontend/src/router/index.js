import {createRouter, createWebHistory} from 'vue-router'
import {unauthorized} from "@/net";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL), routes: [{
        path: '/403',
        name: '403',
        component: () => import('@/views/ForbiddenView.vue')
    }, {
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
        }, {
            path: '/index/chat',
            name: 'index-chat',
            component: () => import('../components/index/DeepSeekChat.vue')
        }, {
            path: '/index/online-chat',
            name: 'index-online-chat',
            component: () => import('../components/index/ChatPage.vue')
        }]
    }, {
        path: '/manager',
        name: 'manager',
        component: () => import('@/views/ManagerView.vue'),
        children: [{
            path: '',
            name: 'manager-home',
            component: () => import('../components/manager/HomePage.vue')
        }, {
            path: '/manager/user',
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
    const authItemName = "authorize"
    const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))
    const isUnauthorized = unauthorized()
    if (to.name.startsWith('welcome') && !isUnauthorized) {
        next('/index')
    } else if (to.name.startsWith('index') && isUnauthorized) {
        next('/')
    } else if (to.name.startsWith('manager') && !isUnauthorized) {
        if (user.role === 'owner') {
            next('/403')
        } else {
            next()
        }
    } else if (to.name.startsWith('index-chat') && !isUnauthorized) {
        if (user.role === 'admin') {
            next()
        } else {
            next('/403')
        }
    } else {
        next()
    }
})

export default router
