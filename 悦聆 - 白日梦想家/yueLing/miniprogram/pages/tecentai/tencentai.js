// miniprogram/pages/aix/tencentai.js
Page({
    data: {
        labels: []
    },
    onLoad: function (options) {
        this.distinguish();
    },
    distinguish() {
        let that = this;
        new Promise(function (resolve, reject) {
            wx.chooseImage({
                count: 1,
                sizeType: ['original', 'compressed'],
                sourceType: ['album', 'camera'],
                success(res) {
                    let file = res.tempFiles[0]
                    let filePath = file.path
                    if (file.size > 4 * 1024 * 1024) {
                        wx.showToast({
                            title: '图片不能超过4M',
                            icon: 'error'
                        })
                    }
                    let base64 = wx.getFileSystemManager().readFileSync(filePath, 'base64');
                    resolve(base64)
                }
            })
        }).then(function (base64) {
            wx.cloud.callFunction({
                name: "tecentai",
                data: {
                    action: 'detectLabel',
                    imageBase64: base64
                },
                success: res => {
                    that.setData({
                        labels: res.result?.Labels.map(JSON.stringify)
                    })
                }
            })
        });
    },
})
