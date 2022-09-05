const dragPatch = {
  patchDom: null, // 要打补丁的dom
  shadowDom: null, // 影子dom
  dragState: "start", // 拖拽状态 start/dragging/end
  diff: null,
  // 辅助方法 - start
  showPatchDom() {
    this.patchDom.classList.remove("hide");
  },
  // 设置拖拽状态 - start
  updateDragStateStart() {
    this.dragState = "start";
  },
  updateDragStateDragging() {
    this.dragState = "dragging";
  },
  updateDragStateEnd() {
    this.dragState = "end";
  },
  // 设置拖拽状态 - end
  hidePatchDom() {
    this.patchDom.classList.add("hide");
  },
  // 影子内容
  createShadowDom() {
    const copy = this.patchDom.cloneNode(true);
    copy.setAttribute("isCopy", "isCopy");
    this.shadowDom = copy;
    this.shadowDom.remove("hide");
    this.patchDom.parentNode.insertBefore(this.shadowDom, this.patchDom);
  },
  updateShadowDomRect(e) {
    if (e.x === 0 && e.y === 0) {
      return;
    }
    // 获得鼠标点击的位置距离文档边缘的距离，不会为负数。
    const pageRect = { left: e.x, top: e.y };
    // 获取父元素距离视口的位置
    const parentDomRect = this.patchDom.parentNode.getBoundingClientRect();
    // 获取补丁元素距离视口的位置
    const patchDomRect = this.patchDom.getBoundingClientRect();
    // 设置基础的偏移值
    this.setBaseOffsetDiff(pageRect, patchDomRect);
    // 获取基本相对文位置
    const { x, y } = this.getShadowDomOffset(
      pageRect,
      parentDomRect,
      patchDomRect
    );
    // 检查是否超出边界
    this.checkPatchDomSafety({ x, y }, parentDomRect)
      .then(() => {
        this.shadowDom.style.left = `${x}px`;
        this.shadowDom.style.top = `${y}px`;
      })
      .catch(() => {
        console.warn("已经超出安全边界");
        // 未来这里可以提醒用户，已经拖到头了
      })
      .finally(() => {
        // 如果要做边界吸附的功能，这里是个好地方
      });
  },
  // 设置打补丁的元素的位置
  setPatchDomRect() {
    const { left, top } = this.shadowDom.style;
    this.patchDom.style.left = left;
    this.patchDom.style.top = top;
  },
  // 获取相对位置功能区 - start
  getShadowDomOffset(pageRect, parentDomRect, patchDomRect) {
    /**
     * 1. 鼠标的距离视口的位置 - 父元素距离视口的位置 = 打补丁的距离父元素的位置
     * 问题：对齐的基点永远是鼠标的point点，这样不管我们拖拽元素任何的位置，元素总会自动对齐到鼠标的point点上。
     * 2. 当前点击dom距离文档边缘的位置 - 父元素距离文档边缘的位置 = 打补丁的距离父元素的位置
     * 问题：同上
     * 所以说，两个方式只不过是参照物不同，本质上还是要解决元素定位的基点问题。
     * 鼠标点击的位置，只是比打补丁的元素多了那么几px。
     * 所以在第一次点击就保存下这个差异，在整个拖拽过程中去使用。
     */
    const rect = { x: 0, y: 0 };
    // 计算部分
    rect.x =
      pageRect.left -
      parentDomRect.left -
      this.diff.left +
      // 兼容 transform: translate(-50%)
      patchDomRect.width / 2;
    rect.y = pageRect.top - parentDomRect.top - this.diff.top;
    return rect;
  },
  // 设置元素基础的偏移值
  setBaseOffsetDiff(pageRect, patchDomRect) {
    if (this.dragState === "start") {
      // 保存偏移量
      let left = pageRect.left - patchDomRect.left;
      let top = pageRect.top - patchDomRect.top;
      this.diff = {
        left,
        top,
      };
    }
  },
  // 检查打补丁的元素是否出界
  async checkPatchDomSafety(rect, parentDomRect) {
    const SAFETY = 20; // 安全区
    return new Promise((resolve, reject) => {
      if (
        _getCheckHorizontalResult(rect.x) &&
        _getCheckVerticalResult(rect.y)
      ) {
        resolve();
      } else {
        reject();
      }

      function _getCheckHorizontalResult(val) {
        const leftDiff = val;
        const rightDiff = parentDomRect.width - val;
        return _check(leftDiff) && _check(rightDiff);
      }

      function _getCheckVerticalResult(val) {
        const topDiff = val;
        const BottomDiff = parentDomRect.height - val;
        return _check(topDiff) && _check(BottomDiff);
      }
      // 检查值是否在安全区内
      function _check(diff) {
        return diff >= SAFETY;
      }
    });
  },
  // 获取相对位置功能区 - end
  // 清理
  clear() {
    this.shadowDom.parentNode.removeChild(this.shadowDom);
    this.shadowDom = null;
    this.diff = null;
  },
  // 辅助方法 - end
  // 核心方法
  // 入口
  patch(node) {
    // 初始化
    this.patchDom = node;
    this.setEvents();
  },
  // 设置监听事件
  setEvents() {
    this.patchDom.setAttribute("draggable", true);
    this.patchDom.addEventListener("dragstart", (e) => {
      this.createShadowDom();
      this.hidePatchDom();
      this.updateDragStateStart();
    });
    this.patchDom.addEventListener("drag", (e) => {
      this.updateShadowDomRect(e);
      this.updateDragStateDragging();
    });
    this.patchDom.addEventListener("dragend", (e) => {
      this.setPatchDomRect();
      this.clear();
      this.showPatchDom();
      this.updateDragStateEnd();
    });
  },
};
