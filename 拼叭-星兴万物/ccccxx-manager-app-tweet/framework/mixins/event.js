module.exports = {
	mounted() {
		let {
			events
		} = this.$options
		if (events) {
			Object.keys(events).forEach(v => {
				uni.$on(v, this[events[v]])
			})
		}
	},
	destroyed() {
		let {
			events
		} = this.$options
		if (events) {
			Object.keys(events).forEach(v => {
				uni.$off(v, this[events[v]])
			})
		}
	}
}
