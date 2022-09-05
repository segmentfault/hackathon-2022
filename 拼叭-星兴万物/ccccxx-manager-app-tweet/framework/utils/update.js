module.exports = () => {
	const updateManager = uni.getUpdateManager();
	updateManager.onCheckForUpdate(function(res) {
		console.info('检测到版本更新', res.hasUpdate);
	});
	updateManager.onUpdateReady(function() {
		console.info('版本更新成功');
		uni.showModal({
			title: '更新提示',
			content: '新版本已经准备好，是否重启应用？',
			success(res) {
				if (res.confirm) {
					updateManager.applyUpdate();
				}
			}
		})
	});
	updateManager.onUpdateFailed(function() {
		console.error('版本更新失败');
	});
}
