import LoginService from '../../service/login.js'
module.exports = {
	methods: { 
		async setDefLogin() { 
			await LoginService.defLogin()
		}
	}
}
