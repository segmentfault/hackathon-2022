<script lang="ts" setup>
import type {Medicine} from "@/types";
import {toRefs} from "vue";

type Props = {
  medicine: Medicine;
}

const props = defineProps<Props>();
const {medicine} = toRefs(props);
</script>

<template lang="pug">
.current-medicine {{medicine.name}}
</template>
