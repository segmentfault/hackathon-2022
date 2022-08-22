<script lang="ts" setup>
import {onBeforeUnmount, ref, toRefs} from "vue";

interface Props {
  hasMenu: boolean;
  title: string;
}
const props = withDefaults(defineProps<Props>(), {
  hasMenu: false,
  title: '姆伊用药助手',
});
const { hasMenu, title } = toRefs(props);
const isOpen = ref<boolean>(false);
const menu = ref<HTMLDivElement>();

window.addEventListener('click', onBodyCLick);

onBeforeUnmount(() => {
  window.removeEventListener('click', onBodyCLick);
});

function onBodyCLick(event: MouseEvent) {
  const target = event.target as HTMLElement;
  if (menu.value?.contains(target)) return;
  isOpen.value = false;
}
</script>

<script lang="ts">
export default {
  name: 'AppHeader',
}
</script>

<template lang="pug">
header.flex.border-b.items-center.px-4.py-2.bg-gray-100.shadow-md.sticky.w-full.top-0
  router-link.text-blue-500.text-sm.w-8(
    class="hover:text-blue-600"
    :to="{name: 'home'}"
  )
    i.bi.bi-chevron-left

  .flex-1.flex.items-center.justify-center
    img.w-8.h-8.mr-2(src="../assets/logo.png")
    h2.text-lg {{title}}

  .w-8
    button.p-0.w-8.h-8.text-blue-500(
      v-if="hasMenu"
      type="button"
      class="active:text-blue-600"
      :class="{'bg-gray-100': isOpen}"
      @click.stop="isOpen = true"
    )
      i.bi.bi-plus-circle

  .origin-top-right.absolute.right-4.top-10.w-56.rounded-md.shadow-lg.bg-white.ring-1.ring-black.ring-opacity-5(
    v-if="isOpen"
    ref="menu"
    role="menu"
    aria-orientation="vertical"
    aria-labelledby="menu-button"
    class="focus:outline-none"
    tabindex="-1"
  )
    .py-1
      slot
        a.text-gray-700.block.px-4.py-2.text-sm(
          href="#"
          role="menuitem"
          tabindex="-1"
        ) Account settings
        a.text-gray-700.block.px-4.py-2.text-sm(
          href="#"
          role="menuitem"
          tabindex="-1"
        ) Support
        a.text-gray-700.block.px-4.py-2.text-sm(
          href="#"
          role="menuitem"
          tabindex="-1"
        ) License
</template>
