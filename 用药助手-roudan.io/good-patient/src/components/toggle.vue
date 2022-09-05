<script lang="ts" setup>
import {counter} from "@/data";
import {ref, toRefs, watch} from "vue";

type Props = {
  /* eslint-disable @typescript-eslint/no-explicit-any */
  modelValue: any;
  valueOn?: any;
  valueOff?: any;
  toggleId?: string;
  /* eslint-enable @typescript-eslint/no-explicit-any */
}
const emit = defineEmits<{
  (e:'update:modelValue', value:any):void; // eslint-disable-line @typescript-eslint/no-explicit-any
}>();

const props = withDefaults(defineProps<Props>(), {
  modelValue: null,
  valueOn: true,
  valueOff: false,
});
const {
  modelValue,
  valueOn,
  valueOff,
  toggleId,
} = toRefs(props);

watch(modelValue, (value) => {
  localValue.value = value;
});

const localId = !toggleId && `switch-${counter.next()}`;
const localValue = ref<boolean>(modelValue.value === valueOn.value);

function onChange(event:Event) {
  const {target} = event;
  const {checked} = target as HTMLInputElement;
  emit('update:modelValue', (checked ? valueOn : valueOff).value);
}
function reset() {
  localValue.value = modelValue.value === valueOn.value;
}

defineExpose({
  reset,
});
</script>

<script lang="ts">
export default {
  name: 'ToggleSwitch',
}
</script>

<template lang="pug">
.switch
  input(
    type="checkbox",
    hidden,
    :id="toggleId || localId",
    v-model="localValue",
    @change="onChange",
  )
  label.switch-wrapper(
    :for="toggleId || localId",
  )
</template>

<style lang="stylus">
.switch-wrapper
  background-color var(--switch-bg)
  border-radius 1rem
  cursor pointer
  display block
  width 3rem
  height 1.5rem
  position relative
  transition background-color .2s

  &::before
    background-color white
    content ''
    border-radius .625rem
    width 1.25rem
    height 1.25rem
    position absolute
    top .125rem
    left .125rem
    transition transform .2s

  :checked + &
    background-color #1ba1ff

    &::before
      transform translateX(1.5rem)

    @media (prefers-color-scheme: dark)
      background-color #548D4E

      .high-contrast &
        background-color #F5793A
</style>
