import Vue from "vue";

/**
 * 这里的工具类使用比较频繁，自动挂到vue实例下
 */
export default {
  $toast(msg, icon, duration = 1500) {
    return new Promise((resolve) => {
      uni.showToast({
        title: msg,
        duration,
        icon: icon || "none",
      });
      setTimeout(() => {
        resolve();
      }, duration);
    });
  },
  $showModal(o) {
    return uni.showModal(o).then((res) => {
      if (!res[1].confirm) {
        return Promise.reject("");
      }
    });
  },
  $setPrePageData(func, data) {
    const prevPage = getCurrentPages()[getCurrentPages().length - 2]; //获取上一级页面实例
    prevPage.$vm[func](data);
    uni.navigateBack({
      delta: 1,
    });
  },
  // 检测是否登录了,拓展业务
  $checkLogin() {
    let USER_TICLET = uni.getStorageSync("ticket") || "";
    let USER_INFO = uni.getStorageSync("userInfo") || "{}";
    if (USER_INFO) {
      return true;
    } else {
      return false;
    }
  },
  // 图片裁剪方法`
  $pictureUrl(url, w, h) {
    if (!url) return;
    uni.getSystemInfo({
      success(res) {
        let pixelRatio = res.pixelRatio;
        w = w * pixelRatio;
        h = h * pixelRatio;
      },
    });
    let urlType = ["?", "?imageView2", "?imageMogr2"];
    if (urlType.includes(url)) {
      return (url += `?imageView2/1/w/${w}/h/${h}/q/80/webp`);
    } else {
      return (url =
        url.split("?").splice(0, 1) + `?imageView2/1/w/${w}/h/${h}/q/80/webp`);
    }
  },
  // 获取url参数
  $getParam(path, name) {
    var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");

    if (reg.test(path)) return unescape(RegExp.$2.replace(/\+/g, " "));

    return null;
  },
};
