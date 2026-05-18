<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from './components/Sidebar.vue'

const route = useRoute()
const isLogin = computed(() => route.path === '/login')
</script>

<template>
  <div class="d-flex" v-if="!isLogin">
    <Sidebar />
    <main class="main-content flex-grow-1">
      <router-view />
    </main>
  </div>
  <router-view v-else />
</template>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f6f9; color: #333; }
.main-content { padding: 1.5rem 2rem; margin-left: 260px; min-height: 100vh; }
</style>
