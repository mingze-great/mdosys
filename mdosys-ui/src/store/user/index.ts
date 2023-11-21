import {defineStore} from "pinia"
import {getInfo as GetInfo, login as Login, refreshToken as RefreshToken, logout as LogOut} from "@/apis/login"
import {getToken, removeToken, setExpiresIn, setToken} from "@/utils/auth";

interface loginParam {
    username: string;
    password: string;
    code: string;
    uuid: string;
    rememberMe: boolean;
}

export const useUserStore = defineStore("userStore", {
    state: () => {
        return {
            token: getToken(),
            expiresIn: 0,
            avatar: "",
            roles: [] as string[],
            permissions: [],
            username: "",
            department: "",
            post: "",
            organization: "",
            ip: "",
            nickname: "",
            email: "",
            phoneNumber: "",
            teamNum: 0,
            resourceNum: 0
        }
    },
    getters: {},
    actions: {
        login(userInfo: loginParam) {
            return new Promise((resolve, reject) => {
                Login(userInfo)
                    .then(res => {
                        let data = res.data
                        this.token = data.access_token
                        setToken(this.token)
                        this.expiresIn = data.expires_in
                        setExpiresIn(this.expiresIn)
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },

        getUserInfo() {
            return new Promise((resolve, reject) => {
                GetInfo()
                    .then(res => {
                        let data = res.data
                        this.username = data.userName
                        this.nickname = data.nickName
                        this.email = data.email
                        this.phoneNumber = data.phonenumber
                        this.ip = data.loginIp
                        this.department = data.dept.deptName
                        this.avatar = data.avatar ? require("@/assets/images/profile.jpg") : data.avatar
                        this.organization = data.organization
                        this.teamNum = data.teamNum
                        this.resourceNum = data.resourceNum
                        this.post = data.post
                        this.roles = data.roles
                        if (data.roles && data.roles > 0) {
                            this.roles = data.roles
                            this.permissions = data.permissions
                        } else {
                            this.roles.push("ROLE_DEFAULT")
                        }
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },

        refreshToken() {
            return new Promise<void>((resolve, reject) => {
                RefreshToken()
                    .then(res => {
                        setExpiresIn(res.data)
                        this.expiresIn = res.data
                        resolve()
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },

        logOut() {
            return new Promise<void>((resolve, reject) => {
                LogOut()
                    .then(res => {
                        this.token = ""
                        this.roles = []
                        this.permissions = []
                        removeToken()
                        resolve()
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },

        fedLogOut() {
            return new Promise<void>(resolve => {
                this.token = ""
                removeToken()
                resolve()
            })
        }
    }
})