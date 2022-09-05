let _hooks = {}

const flatHooks = function (configHooks, hooks = {}, parentName) {
    for (const key in configHooks) {
        const subHook = configHooks[key]
        const name = parentName ? `${parentName}:${key}` : key
        if (typeof subHook === 'object' && subHook !== null) {
            flatHooks(subHook, hooks, name)
        } else {
            hooks[name] = subHook
        }
    }
    return hooks
}

const serial = function(tasks, fn) {
    return tasks.reduce((promise, task) => promise.then((previous) => fn(task, previous)), Promise.resolve(null))
}

export default {
	
	    on(name, fn) {
	        if (!name || typeof fn !== 'function') {
	            return this;
	        }
	        _hooks[name] = _hooks[name] || []
	        _hooks[name].push(fn)
	        return this;
	    },
	
	    addHooks(configHooks) {
	        const hooks = flatHooks(configHooks)
	        for (const key in hooks) {
	            this.on(key, hooks[key])
	        }
	        return this;
	    },
	
	    callHook(name, ...args) {
	        if (!_hooks[name]) {
	            return
	        }
	        return serial(_hooks[name], fn=>fn(...args)).catch(err => {
	            if (name !== 'error') {
	                this.callHook('error', err)
	            }
	        })
	    },
	
	    clearHook(name) {
	        if (name) {
	            delete _hooks[name]
	        }
	        return this;
	    },
	
	    clearHooks() {
	        _hooks = {}
	        return this;
	    }

}