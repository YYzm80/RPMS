import axios from "axios";
import {ElMessage} from "element-plus";

const authItemName = "authorize"
const defaultError = () => ElMessage.error('发生了一些错误，请联系管理员')
const defaultFailure = (message, status, url) => {
    if (status === 401) {
        ElMessage.warning("登录状态已过期，请重新登录！")
        deleteAccessToken()
        return
    }
    console.warn(`请求地址: ${url}, 状态码: ${status}, 错误信息: ${message}`)
    ElMessage.warning(message)
}

const accessHeader = () => {
    return {
        'Authorization': `Bearer ${takeAccessToken()}`,
        'Content-Type': 'application/json'
    }
}

function takeAccessToken() {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName);
    if(!str) return null
    const authObj = JSON.parse(str)
    if(new Date(authObj.expire) <= new Date() || !authObj.token) {
        deleteAccessToken()
        ElMessage.warning("登录状态已过期，请重新登录！")
        return null
    }
    return authObj.token
}

function storeAccessToken(remember, token, expire, data){
    const authObj = {
        token: token,
        expire: expire,
        username: data.username,
        role: data.roleName,
        uid: data.userId,
    }
    const str = JSON.stringify(authObj)
    if(remember)
        localStorage.setItem(authItemName, str)
    else
        sessionStorage.setItem(authItemName, str)
}

function deleteAccessToken() {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

function internalPost(url, data, headers, success, failure, error = defaultError){
    axios.post(url, data, { headers: headers, withCredentials: true }).then(({data}) => {
        if(data.status === 200)
            success(data.message)
        else
            failure(data.message, data.status, url)
    }).catch(err => error(err))
}

function internalPut(url, data, headers, success, failure, error = defaultError){
    axios.put(url, data, { headers: headers, withCredentials: true }).then(({data}) => {
        if(data.status === 200)
            success(data.message)
        else
            failure(data.message, data.status, url)
    }).catch(err => error(err))
}

function internalGet(url, headers, success, failure, error = defaultError){
    axios.get(url, { headers: headers, withCredentials: true}).then(({data}) => {
        if(data.status === 200)
            success(data.message)
        else
            failure(data.message, data.status, url)
    }).catch(err => error(err))
}

function login(username, password, remember, success, failure = defaultFailure){
    internalPost('/api/auth/login', {
        username: username,
        password: password
    }, {
        'Content-Type': 'application/x-www-form-urlencoded'
    }, (data) => {
        if (data.online) {
            ElNotification({
                title: '警告',
                message: '您在其它地方登录的账号已被踢出',
                type: 'warning',
            })
        }
        storeAccessToken(remember, data.token, data.expire, data)
        ElMessage.success(`登录成功，欢迎 ${data.username} 来到我们的系统`)
        success(data)
    }, failure)
}

function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeader() , success, failure)
}

function multipartPost(url, data, success, failure = defaultFailure) {
    internalPost(url, data, {
        'Authorization': `Bearer ${takeAccessToken()}`,
        'Content-Type': 'multipart/form-data'
    }, success, failure)
}

function put(url, data, success, failure = defaultFailure) {
    internalPut(url, data, accessHeader(), success, failure)
}

function logout(success, failure = defaultFailure){
    get('/api/auth/logout', () => {
        deleteAccessToken()
        ElMessage.success(`退出登录成功，欢迎您再次使用`)
        success()
    }, failure)
}

function get(url, success, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure)
}

function blobGet(url) {
    return axios.get(url, { headers: accessHeader(), withCredentials: true, responseType: 'blob'})
}

function unauthorized() {
    return !takeAccessToken()
}

export { post, multipartPost, put, get, blobGet, login, logout, unauthorized }